package fr.northborders.eyja.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import fr.northborders.eyja.adapters.FeedsListAdapter;
import fr.northborders.eyja.model.RssFeed;
import fr.northborders.eyja.rss.SortingOrder;
import fr.northborders.eyja.rss.XmlHandler;

/**
 * Created by thibaultguegan on 12/01/15.
 */
public class FeedsView extends ListView {

    private static final String TAG = FeedsView.class.getSimpleName();

    private Context mContext;
    private List<RssFeed> mFeeds;
    private FeedsListAdapter mAdapter;

    public FeedsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.inject(this);

        RssFeedTask rssTask = new RssFeedTask();
        rssTask.execute();
    }

    private class RssFeedTask extends AsyncTask<String, Void, String> {
        // private String Content;
        private ProgressDialog Dialog;
        String response = "";

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(FeedsView.this.mContext);
            Dialog.setMessage("Rss Loading...");
            Dialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String feed = "http://www.letelegramme.fr/morbihan/belle-ile-en-mer/rss.xml";
                XmlHandler rh = new XmlHandler();
                mFeeds = rh.getLatestArticles(feed);
            } catch (Exception e) {
            }
            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            if(mFeeds != null){
                mAdapter = new FeedsListAdapter(mContext, mFeeds);
                Collections.sort(mFeeds, new SortingOrder());
                setAdapter(mAdapter);

                setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RssFeed feed = mFeeds.get(position);
                        Log.d(TAG, String.format("feed content is: %s", feed.getContent()));
                    }
                });
            }
            Dialog.dismiss();
        }
    }
}
