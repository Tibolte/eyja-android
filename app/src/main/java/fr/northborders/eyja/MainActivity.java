package fr.northborders.eyja;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.melnykov.fab.FloatingActionButton;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.northborders.eyja.adapters.FeedsListAdapter;
import fr.northborders.eyja.model.RssFeed;
import fr.northborders.eyja.rss.SortingOrder;
import fr.northborders.eyja.rss.XmlHandler;
import fr.northborders.eyja.util.Utils;


public class MainActivity extends ActionBarActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(android.R.id.list)
    ListView mListView;

    @InjectView(R.id.btn_info)
    FloatingActionButton mFloatingActionButton;

    @InjectView(R.id.progressBarCircularIndeterminate)
    ProgressBarCircularIndeterminate progressBar;

    @InjectView(R.id.infoView)
    FrameLayout infoView;

    private Handler mHandler = new Handler();
    private List<RssFeed> mFeeds;
    private RssFeedTask mRssFeedTask;
    private FeedsListAdapter mAdapter;

    /**
     * MARK: Lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // get the new data from you data source
                mHandler.post(refreshing);
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.primary,
                R.color.primary_dark,
                R.color.accent
                );

        mRssFeedTask = new RssFeedTask();
        mRssFeedTask.execute();

        //we want the default ripple effect on lollipop
        if (!Utils.hasLollipop()) {
            mListView.setCacheColorHint(android.R.color.transparent);
            mListView.setSelector(android.R.color.transparent);
        }
        else {
            mListView.setDrawSelectorOnTop(true);
        }

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (mListView == null || mListView.getChildCount() == 0) ?
                                0 : mListView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        mFloatingActionButton.attachToListView(mListView);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformation(v);
            }
        });
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if(infoView.getVisibility() == View.VISIBLE) {
            showInformation(mFloatingActionButton);
        } else {
          super.onBackPressed();
        }
    }

    private boolean isRefreshing() {
        return mRssFeedTask.isRefreshing();
    }

    private final Runnable refreshing = new Runnable() {

        @Override
        public void run() {
            try {
                if(isRefreshing()){
                    // re run the verification after 1 second
                    mHandler.postDelayed(this, 1000);
                }else{
                    // stop the animation after the data is fully loaded
                    mRssFeedTask = new RssFeedTask();
                    mRssFeedTask.execute();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private class RssFeedTask extends AsyncTask<String, Void, String> {
        private boolean isRefreshing = false;
        String response = "";

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            isRefreshing = true;
            mSwipeRefreshLayout.setRefreshing(isRefreshing);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String feed = getString(R.string.telegramme_belle_ile);
                XmlHandler rh = new XmlHandler();
                mFeeds = rh.getLatestArticles(feed);
            } catch (Exception e) {
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if(mFeeds != null){
                mAdapter = new FeedsListAdapter(MainActivity.this, mFeeds);
                Collections.sort(mFeeds, new SortingOrder());
                isRefreshing = false;
                mSwipeRefreshLayout.setRefreshing(isRefreshing);
                mListView.setAdapter(mAdapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RssFeed feed = mFeeds.get(position);
                        Log.d(TAG, String.format("feed content is: %s", feed.getContent()));
                    }
                });
            }
            progressBar.setVisibility(View.GONE);
        }

        public boolean isRefreshing() {
            return isRefreshing;
        }
    }

    public void showInformation(View view) {
        if (Utils.hasLollipop()) {
            toggleInformationView(view);
        }
        else {

        }
    }

    @TargetApi(21)
    private void toggleInformationView(View view) {
        final View infoContainer = infoView;

        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;
        float radius = Math.max(infoContainer.getWidth(), infoContainer.getHeight()) * 2.0f;

        Animator reveal;
        if (infoContainer.getVisibility() == View.INVISIBLE) {
            infoContainer.setVisibility(View.VISIBLE);
            reveal = ViewAnimationUtils.createCircularReveal(
                    infoContainer, cx, cy, 0, radius);
            reveal.setInterpolator(new AccelerateInterpolator(2.0f));
        } else {
            reveal = ViewAnimationUtils.createCircularReveal(
                    infoContainer, cx, cy, radius, 0);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    infoContainer.setVisibility(View.INVISIBLE);
                }
            });
            reveal.setInterpolator(new DecelerateInterpolator(2.0f));
        }
        reveal.setDuration(600);
        reveal.start();
    }
}
