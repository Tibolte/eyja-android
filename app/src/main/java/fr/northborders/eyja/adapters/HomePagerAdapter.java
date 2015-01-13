package fr.northborders.eyja.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedHashMap;
import java.util.Map;

import flow.Layout;
import fr.northborders.eyja.Section;
import fr.northborders.eyja.appflow.Screen;
import fr.northborders.eyja.util.ObjectUtils;

import static fr.northborders.eyja.util.Preconditions.checkNotNull;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public class HomePagerAdapter extends PagerAdapter {

    private static final String TAG = HomePagerAdapter.class.getSimpleName();
    private Context mContext;
    private Section[] mSections;

    private static final Map<Class, View> SCREEN_HOME_CACHE = new LinkedHashMap<>();

    public HomePagerAdapter(Context context, Section[] sections) {
        this.mContext = context;
        this.mSections = sections;
    }

    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return mSections.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mSections == null ? "" : mSections[position].getTile();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Inflate a new layout from our resources
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Screen screen = mSections[position].getScreen();
        Class<Object> screenType = ObjectUtils.getClass(screen);
        //Integer layoutResId = SCREEN_HOME_CACHE.get(screenType);

        View view;
        if (!SCREEN_HOME_CACHE.containsKey(screenType)) {
            Layout layout = screenType.getAnnotation(Layout.class);
            checkNotNull(layout, "@%s annotation not found on class %s", Layout.class.getSimpleName(),
                    screenType.getName());
            int layoutResId = layout.value();
            view = inflater.inflate(layoutResId,
                    container, false);

            view.setTag(screen);
            SCREEN_HOME_CACHE.put(screenType, view);
        }
        else {
            view = SCREEN_HOME_CACHE.get(screenType);
        }

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
