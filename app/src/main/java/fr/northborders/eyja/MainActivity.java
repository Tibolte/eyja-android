package fr.northborders.eyja;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.northborders.eyja.adapters.FeedsListAdapter;
import fr.northborders.eyja.model.RssFeed;
import fr.northborders.eyja.rss.SortingOrder;
import fr.northborders.eyja.rss.XmlHandler;


public class MainActivity extends ActionBarActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(android.R.id.list)
    ListView mListView;

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

        mRssFeedTask = new RssFeedTask();
        mRssFeedTask.execute();

        //we want the default ripple effect on lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
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
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        private ProgressDialog dialog;
        private boolean isRefreshing = false;
        String response = "";

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage(getString(R.string.chargement));
            dialog.show();
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
            dialog.dismiss();
        }

        public boolean isRefreshing() {
            return isRefreshing;
        }
    }
}
