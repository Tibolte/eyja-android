package fr.northborders.eyja.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import flow.Layout;
import fr.northborders.eyja.MainActivity;
import fr.northborders.eyja.R;
import fr.northborders.eyja.Screens;
import fr.northborders.eyja.appflow.Screen;
import fr.northborders.eyja.screenswitcher.PathContext;
import fr.northborders.eyja.screenswitcher.SimpleSwitcher;
import fr.northborders.eyja.util.ObjectUtils;

import static fr.northborders.eyja.util.Preconditions.checkNotNull;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public class HomePagerAdapter extends PagerAdapter {

    private static final String TAG = HomePagerAdapter.class.getSimpleName();
    private Context mContext;

    public HomePagerAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Item " + (position + 1);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Inflate a new layout from our resources
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Screen screen = new Screens.Dummy();
        Class<Object> screenType = ObjectUtils.getClass(screen);
        Layout layout = screenType.getAnnotation(Layout.class);
        checkNotNull(layout, "@%s annotation not found on class %s", Layout.class.getSimpleName(),
                screenType.getName());
        int layoutResId = layout.value();


        View view = inflater.inflate(layoutResId,
                container, false);
        // Add the newly created View to the ViewPager
        container.addView(view);

        // Return the View
        return view;

    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
