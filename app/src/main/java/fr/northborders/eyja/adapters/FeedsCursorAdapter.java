package fr.northborders.eyja.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.northborders.eyja.EyjaApplication;
import fr.northborders.eyja.R;
import fr.northborders.eyja.database.DatabaseHelper;

/**
 * Created by thibaultguegan on 25/01/15.
 */
public class FeedsCursorAdapter extends CursorAdapter {

    public FeedsCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_feed, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.txtFeedText = (TextView) view.findViewById(R.id.txt_feed_title);
        holder.imgFeed = (ImageView) view.findViewById(R.id.img_feed);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.txtFeedText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TITLE)));
        holder.txtFeedText.setTypeface(EyjaApplication.getInstance().getFontRegular());

        Picasso.with(context)
                .load(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_IMG_LINK)))
                .tag(context)
                .into(holder.imgFeed);
    }

    private class ViewHolder {
        TextView txtFeedText;
        ImageView imgFeed;
    }
}
