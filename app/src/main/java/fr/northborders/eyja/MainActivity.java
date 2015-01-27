package fr.northborders.eyja;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.northborders.eyja.adapters.GridAdpater;
import fr.northborders.eyja.model.RssFeed;
import fr.northborders.eyja.rss.SortingOrder;
import fr.northborders.eyja.rss.XmlHandler;


public class MainActivity extends BaseActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.progressBarCircularIndeterminate)
    ProgressBarCircularIndeterminate progressBar;

    @InjectView(R.id.gridView)
    GridView gridView;

    private Handler mHandler = new Handler();
    private List<RssFeed> mFeeds;
    private RssFeedTask mRssFeedTask;

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

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (gridView == null || gridView.getChildCount() == 0) ?
                                0 : gridView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
                Collections.sort(mFeeds, new SortingOrder());
                isRefreshing = false;

                mSwipeRefreshLayout.setRefreshing(isRefreshing);
                GridAdpater adpater = new GridAdpater(mFeeds);
                gridView.setAdapter(adpater);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String url = mFeeds.get(position).getImgLink();
                        DetailActivity.launch(MainActivity.this, view.findViewById(R.id.image), url, mFeeds.get(position).getTitle(), mFeeds.get(position).getDescription());
                    }
                });
            }
            progressBar.setVisibility(View.GONE);
        }

        public boolean isRefreshing() {
            return isRefreshing;
        }
    }

}
