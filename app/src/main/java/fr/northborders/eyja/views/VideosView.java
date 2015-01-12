package fr.northborders.eyja.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import fr.northborders.eyja.util.Utils;

/**
 * Created by thibaultguegan on 12/01/15.
 */
public class VideosView extends LinearLayout {

    public VideosView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);
        Utils.inject(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.inject(this);

    }

}
