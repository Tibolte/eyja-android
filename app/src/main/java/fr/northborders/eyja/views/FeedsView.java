package fr.northborders.eyja.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import fr.northborders.eyja.adapters.FeedsListAdapter;
import fr.northborders.eyja.model.RssFeed;
import fr.northborders.eyja.rss.XmlHandler;
import fr.northborders.eyja.util.Utils;

/**
 * Created by thibaultguegan on 12/01/15.
 */
public class FeedsView extends ListView {

    private Context mContext;
    private List<RssFeed> mFeeds;
    private FeedsListAdapter mAdapter;

    public FeedsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        Utils.inject(context, this);
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
            //TODO: sort
            if(mFeeds != null){
                mAdapter = new FeedsListAdapter(mContext, mFeeds);
                setAdapter(mAdapter);
            }
            Dialog.dismiss();
        }
    }
}
