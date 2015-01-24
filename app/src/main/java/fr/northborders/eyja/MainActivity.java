package fr.northborders.eyja;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    SwipeRefreshLayout swipeRefreshLayout;

    @InjectView(android.R.id.list)
    ListView listView;

    private Handler handler = new Handler();
    private List<RssFeed> mFeeds;
    private RssFeedTask mRssFeedTask;

    private String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
            "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
            "Android", "iPhone", "WindowsMobile" };

    /**
     * MARK: Lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // get the new data from you data source
                // TODO : request data here
                // our swipeRefreshLayout needs to be notified when the data is returned in order for it to stop the animation
                handler.post(refreshing);
            }
        });

        mRssFeedTask = new RssFeedTask();
        mRssFeedTask.execute();
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
                // TODO : isRefreshing should be attached to your data request status
                if(isRefreshing()){
                    // re run the verification after 1 second
                    handler.postDelayed(this, 1000);
                }else{
                    // stop the animation after the data is fully loaded
                    swipeRefreshLayout.setRefreshing(false);
                    // TODO : update your list with the new data
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private class RssFeedTask extends AsyncTask<String, Void, String> {
        private ProgressDialog Dialog;
        private boolean isRefreshing = false;
        String response = "";

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(MainActivity.this);
            Dialog.setMessage(getString(R.string.chargement));
            Dialog.show();
            isRefreshing = true;
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
                //mAdapter = new FeedsListAdapter(mContext, mFeeds);
                Collections.sort(mFeeds, new SortingOrder());
                isRefreshing = false;
                //setAdapter(mAdapter);

                /*setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RssFeed feed = mFeeds.get(position);
                        Log.d(TAG, String.format("feed content is: %s", feed.getContent()));
                    }
                });*/
            }
            Dialog.dismiss();
        }

        public boolean isRefreshing() {
            return isRefreshing;
        }
    }
}
