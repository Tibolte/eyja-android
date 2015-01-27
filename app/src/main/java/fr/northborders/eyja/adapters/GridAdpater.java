package fr.northborders.eyja.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.northborders.eyja.EyjaApplication;
import fr.northborders.eyja.R;
import fr.northborders.eyja.model.RssFeed;

/**
 * Created by thibaultguegan on 27/01/15.
 */
public class GridAdpater extends BaseAdapter {

    private List<RssFeed> mFeeds;

    public GridAdpater(List<RssFeed> feeds) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.txtFeedText = (TextView) convertView.findViewById(R.id.text);
            viewHolder.imgFeed = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtFeedText.setText(mFeeds.get(position).getTitle());
        viewHolder.txtFeedText.setTypeface(EyjaApplication.getInstance().getFontRegular());


        Picasso.with(convertView.getContext())
                .load(mFeeds.get(position).getImgLink())
                .into(viewHolder.imgFeed);

        return convertView;
    }

    private class ViewHolder {
        TextView txtFeedText;
        ImageView imgFeed;
    }
}
