package fr.northborders.eyja.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.northborders.eyja.R;
import fr.northborders.eyja.Section;
import fr.northborders.eyja.adapters.HomePagerAdapter;
import fr.northborders.eyja.util.Utils;
import fr.northborders.eyja.views.slidingtabs.SlidingTabLayout;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public class HomeView extends LinearLayout{

    @InjectView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;

    @InjectView(R.id.viewpager)
    ViewPager mViewPager;

    private Context mContext;
    private HomePagerAdapter mHomePagerAdapter;

    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        setOrientation(VERTICAL);
        Utils.inject(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.inject(this);

    }

    public void setSections(Section[] sections) {
        mHomePagerAdapter = new HomePagerAdapter(mContext, sections);

        mViewPager.setAdapter(mHomePagerAdapter);
        mSlidingTabLayout.setViewPager(mViewPager);
    }
}
