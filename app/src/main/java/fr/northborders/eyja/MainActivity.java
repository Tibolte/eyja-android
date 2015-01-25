package fr.northborders.eyja;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.northborders.eyja.adapters.FeedsCursorAdapter;
import fr.northborders.eyja.adapters.FeedsListAdapter;
import fr.northborders.eyja.database.DatabaseHelper;
import fr.northborders.eyja.model.RssFeed;
import fr.northborders.eyja.rss.SortingOrder;
import fr.northborders.eyja.rss.XmlHandler;
import fr.northborders.eyja.ui.SpotlightView;
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

    @InjectView(R.id.spotlight)
    SpotlightView spotlightView;

    @InjectView(R.id.description)
    TextView txtDescription;

    @InjectView(R.id.infoScroll)
    ScrollView scrollInfo;

    private Handler mHandler = new Handler();
    private List<RssFeed> mFeeds;
    private RssFeedTask mRssFeedTask;
    private FeedsListAdapter mAdapter;
    private float maskScale = 0;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * MARK: Lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        txtDescription.setTypeface(EyjaApplication.getFontRegular());

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
                final Picasso picasso = Picasso.with(getApplicationContext());
                if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    picasso.resumeTag(getApplicationContext());
                } else {
                    picasso.pauseTag(getApplicationContext());
                }
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

        infoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                if(infoView.getWidth() > 0) {

                    Bitmap b = Utils.viewToBitmap(infoView, infoView.getWidth(), infoView.getHeight());
                    spotlightView.createShaderB(b);
                    maskScale = spotlightView.computeMaskScale(Math.max(spotlightView.getHeight(), spotlightView.getWidth()) * 4.0f);

                    Utils.removeOnGlobalLayoutListenerCompat(infoView, this);
                }

            }
        });

        spotlightView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                spotlightView.setMaskX(mFloatingActionButton.getRight() - (mFloatingActionButton.getWidth() / 2));
                spotlightView.setMaskY(mFloatingActionButton.getBottom() - (mFloatingActionButton.getHeight() / 2));

                Utils.removeOnGlobalLayoutListenerCompat(spotlightView, this);
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

                /*DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                databaseHelper.flush();
                for(RssFeed rssFeed: mFeeds) {
                    databaseHelper.insertFeed(rssFeed);
                }

                Cursor cursor = databaseHelper.getWritableDatabase().query(DatabaseHelper.TABLE_FEED, new String[] {"COL_ID AS _id, *"},null, null, null, null, null);
                FeedsCursorAdapter feedsCursorAdapter = new FeedsCursorAdapter(getApplicationContext(), cursor);
                mListView.setAdapter(feedsCursorAdapter);
                feedsCursorAdapter.notifyDataSetChanged();*/

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //do stuff
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
            if(infoView.getVisibility() == View.INVISIBLE) {
                createScaleAnimation(spotlightView);
            } else {
                createShrinkAnimation(spotlightView);
            }
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

    private void createScaleAnimation(final SpotlightView spotlight) {
        spotlight.setVisibility(View.VISIBLE);

        com.nineoldandroids.animation.ObjectAnimator superScale = com.nineoldandroids.animation.ObjectAnimator.ofFloat(spotlight, "maskScale", maskScale);
        superScale.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                infoView.setVisibility(View.VISIBLE);
                spotlight.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        superScale.start();
    }

    private void createShrinkAnimation(final SpotlightView spotlight) {
        infoView.setVisibility(View.INVISIBLE);
        spotlight.setVisibility(View.VISIBLE);
        spotlight.setMaskScale(maskScale);

        com.nineoldandroids.animation.ObjectAnimator superShrink = ObjectAnimator.ofFloat(spotlight, "maskScale", maskScale, 0.5f);
        superShrink.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                spotlight.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        superShrink.start();
    }
}
