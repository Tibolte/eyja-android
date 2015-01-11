package fr.northborders.eyja.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.northborders.eyja.R;
import fr.northborders.eyja.util.Utils;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public class HomeView extends LinearLayout{

    @InjectView(R.id.sliding_tabs)
    SlidingTabLayout slidingTabLayout;

    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);
        Utils.inject(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.inject(this);

        //TODO: set adapter
    }
}
