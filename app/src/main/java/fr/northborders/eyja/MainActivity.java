package fr.northborders.eyja;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.northborders.eyja.adapters.FeedsListAdapter;
import fr.northborders.eyja.adapters.GridAdpater;
import fr.northborders.eyja.adapters.RecyclerAdapter;
import fr.northborders.eyja.model.RssFeed;
import fr.northborders.eyja.rss.SortingOrder;
import fr.northborders.eyja.rss.XmlHandler;


public class MainActivity extends BaseActivity{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_TO_EXECUTE = 3;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.progressBarCircularIndeterminate)
    ProgressBarCircularIndeterminate progressBar;

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView;

    private Handler mHandler = new Handler();
    private List<RssFeed> mFeeds;
    private RecyclerView.LayoutManager mLayoutManager;

    // each task increments this count as soon as it enters its doInBackground()
    // so this allows to track the order of tasks execution
    private AtomicInteger executedTasksCount;

    /**
     * MARK: Lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        executedTasksCount = new AtomicInteger(0); // reset this count

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(mLayoutManager);

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

        startTasks();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private final Runnable refreshing = new Runnable() {

        @Override
        public void run() {
            try {
                if(isRefreshing()){
                    // re run the verification after 1 second
                    mHandler.postDelayed(this, 1000);
                }else{
                    startTasks();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startTasks() {
        RssFeedOuestFranceTask ouestFranceTask = new RssFeedOuestFranceTask();
        RssFeedOuestFranceLocamriaTask rssFeedOuestFranceLocamriaTask = new RssFeedOuestFranceLocamriaTask();
        RssFeedTelegrammeTask telegrammeTask = new RssFeedTelegrammeTask();
        ouestFranceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        rssFeedOuestFranceLocamriaTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        telegrammeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void finishTask() {
        int taskExecutionNumber = executedTasksCount.incrementAndGet();
        Log.d(TAG, "FinishTask: entered, taskExecutionNumber = " + taskExecutionNumber);

        if(taskExecutionNumber == TASK_TO_EXECUTE) {

            RecyclerAdapter adapter = new RecyclerAdapter(mFeeds, this);
            recyclerView.setAdapter(adapter);

            executedTasksCount = new AtomicInteger(0);
            progressBar.setVisibility(View.GONE);
        }
    }

    private boolean isRefreshing() {
        if(executedTasksCount.get() < TASK_TO_EXECUTE) {
            return true;
        }
        else {
            return false;
        }
    }

    private class RssFeedTelegrammeTask extends AsyncTask<String, Void, String> {
        private boolean isRefreshing = false;
        String response = "";
        private List<RssFeed> telegrammeFeeds = new ArrayList<RssFeed>();

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
                telegrammeFeeds = rh.getLatestArticles(feed);
            } catch (Exception e) {
                Log.d(TAG, "Telegrammes task, inBackground Exception");
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if(telegrammeFeeds != null){
                Collections.sort(telegrammeFeeds, new SortingOrder());

                if(mFeeds == null) {
                    mFeeds = new ArrayList<RssFeed>();
                }
                mFeeds.addAll(telegrammeFeeds);

                finishTask();
            }
        }
    }

    private class RssFeedOuestFranceTask extends AsyncTask<String, Void, String> {

        String response = "";
        private List<RssFeed> ouestFeeds = new ArrayList<RssFeed>();

        @Override
        protected String doInBackground(String... params) {
            try {
                String feed = getString(R.string.ouest_france_sauzon);
                XmlHandler rh = new XmlHandler();
                ouestFeeds = rh.getLatestArticles(feed);
            } catch (Exception e) {
                Log.d(TAG, "Telegrammes task, inBackground Exception");
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if(ouestFeeds != null){
                Collections.sort(ouestFeeds, new SortingOrder());

                if(mFeeds == null) {
                    mFeeds = new ArrayList<RssFeed>();
                }
                mFeeds.addAll(ouestFeeds);

                finishTask();
            }
        }
    }

    private class RssFeedOuestFranceLocamriaTask extends AsyncTask<String, Void, String> {

        String response = "";
        private List<RssFeed> ouestFeeds = new ArrayList<RssFeed>();

        @Override
        protected String doInBackground(String... params) {
            try {
                String feed = getString(R.string.ouest_france_locmaria);
                XmlHandler rh = new XmlHandler();
                ouestFeeds = rh.getLatestArticles(feed);
            } catch (Exception e) {
                Log.d(TAG, "Telegrammes task, inBackground Exception");
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if(ouestFeeds != null){
                Collections.sort(ouestFeeds, new SortingOrder());

                if(mFeeds == null) {
                    mFeeds = new ArrayList<RssFeed>();
                }
                mFeeds.addAll(ouestFeeds);

                finishTask();
            }
        }
    }
}
