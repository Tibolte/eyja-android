package fr.northborders.eyja.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import fr.northborders.eyja.R;
import fr.northborders.eyja.model.RssFeed;

/**
 * Created by thibaultguegan on 13/01/15.
 */
public class FeedsListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<RssFeed> mFeeds;


    public FeedsListAdapter(Context context, List<RssFeed> feeds) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFeeds = feeds;
    }

    @Override
    public int getCount() {
        return mFeeds.size();
    }

    @Override
    public Object getItem(int position) {
        return mFeeds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_feed, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.txtFeedText = (TextView) convertView.findViewById(R.id.txt_feed_title);
            viewHolder.txtFeedUpdateTime = (TextView) convertView.findViewById(R.id.txt_feed_updated_time);
            //viewHolder.imgFeed = (ImageView) convertView.findViewById(R.id.feed_image);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {

            //Log.d("rssfeed", "imageAndTexts1.get(position).getImgLink() :: " + mImagesAndText.get(position).getImgLink() + " :: " + mImagesAndText.get(position).getTitle());
            viewHolder.txtFeedText.setText(mFeeds.get(position).getTitle());
            SpannableString content = new SpannableString(mFeeds.get(position).getPubDate());
            content.setSpan(new UnderlineSpan(), 0, 13, 0);

            viewHolder.txtFeedUpdateTime.setText(content);
            if(mFeeds.get(position).getImgLink() !=null){


                URL feedImage= new URL(mFeeds.get(position).getImgLink().toString());
                /*if(!feedImage.toString().equalsIgnoreCase("null")){
                    HttpURLConnection conn= (HttpURLConnection)feedImage.openConnection();
                    InputStream is = conn.getInputStream();
                    Bitmap img = BitmapFactory.decodeStream(is);
                    viewHolder.imgFeed.setImageBitmap(img);
                }
                else{
                    viewHolder.imgFeed.setBackgroundResource(R.drawable.im);
                }*/
            }


        } catch (MalformedURLException e) {

        }
        catch (IOException e) {

        }

        return convertView;
    }

    private class ViewHolder {
        TextView txtFeedText;
        TextView txtFeedUpdateTime;
        ImageView imgFeed;
    }
}
