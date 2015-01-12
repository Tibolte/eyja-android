package fr.northborders.eyja.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import fr.northborders.eyja.model.Feed;
import fr.northborders.eyja.util.Utils;

/**
 * Created by thibaultguegan on 12/01/15.
 */
public class FeedsView extends ListView {
    @Inject
    List<Feed> feeds;

    public FeedsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Utils.inject(context, this);

        Adapter adapter = new Adapter(getContext(), feeds);

        setAdapter(adapter);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.inject(this);

    }

    private static class Adapter extends ArrayAdapter<Feed> {
        public Adapter(Context context, List<Feed> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }
    }
}
