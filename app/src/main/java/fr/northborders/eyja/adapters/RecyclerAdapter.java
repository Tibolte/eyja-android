package fr.northborders.eyja.adapters;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.northborders.eyja.BaseActivity;
import fr.northborders.eyja.DetailActivity;
import fr.northborders.eyja.EyjaApplication;
import fr.northborders.eyja.R;
import fr.northborders.eyja.model.RssFeed;
import fr.northborders.eyja.util.Utils;

/**
 * Created by thibaultguegan on 03/02/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<RssFeed> mFeeds;
    private BaseActivity mActivity;

    public RecyclerAdapter(List<RssFeed> feeds, BaseActivity activity) {
        mFeeds = feeds;
        mActivity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @InjectView(R.id.img_feed)
        ImageView imfFeed;
        @InjectView(R.id.txt_feed_title)
        TextView txtFeed;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String url = mFeeds.get(getPosition()).getImgLink();
            DetailActivity.launch(mActivity, view.findViewById(R.id.img_feed), url, mFeeds.get(getPosition()).getTitle(), mFeeds.get(getPosition()).getContent());
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_feed, parent, false);
        // set the view's size, margins, paddings and layout parameters

        CardView cardView = (CardView) v;
        if(!Utils.hasLollipop()) {
            cardView.setPreventCornerOverlap(false);
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtFeed.setText(mFeeds.get(position).getTitle());
        holder.txtFeed.setTypeface(EyjaApplication.getInstance().getFontRegular());

        Picasso.with(holder.imfFeed.getContext())
                .load(mFeeds.get(position).getImgLink())
                .into(holder.imfFeed);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mFeeds.size();
    }

}
