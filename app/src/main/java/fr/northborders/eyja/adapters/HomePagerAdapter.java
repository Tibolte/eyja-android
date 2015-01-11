package fr.northborders.eyja.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import fr.northborders.eyja.Screens;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public class HomePagerAdapter extends PagerAdapter {

    private static final String TAG = HomePagerAdapter.class.getSimpleName();
    private Context context;

    public HomePagerAdapter(Context context) {
        this.context = context;
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
        return "";
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return new Screens.Dummy();

    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
