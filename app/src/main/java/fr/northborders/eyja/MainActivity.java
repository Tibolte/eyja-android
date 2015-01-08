package fr.northborders.eyja;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import flow.Backstack;
import flow.Flow;
import fr.northborders.eyja.presenters.BasePresenter;
import fr.northborders.eyja.presenters.DummyPresenter;
import fr.northborders.eyja.presenters.RootPresenter;
import fr.northborders.eyja.utils.FlowBundler;
import fr.northborders.eyja.utils.GsonParcer;
import fr.northborders.eyja.utils.Section;
import hugo.weaving.DebugLog;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, Flow.Listener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private Section[] sections = null;

    DummyPagerAdapter mDummyPagerAdpater;

    private Gson gson = new Gson();
    private RootPresenter rootPresenter = new RootPresenter();
    private final FlowBundler flowBundler =
            new FlowBundler(rootPresenter,
                    MainActivity.this,
                    new GsonParcer<>(new GsonBuilder()
                            .excludeFieldsWithoutExposeAnnotation()
                            .create()));

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    /**
     * MARK: Lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flowBundler.onCreate(savedInstanceState);

        mDummyPagerAdpater = new DummyPagerAdapter(this);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDummyPagerAdpater);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
                rootPresenter.setCurrentItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mDummyPagerAdpater.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mDummyPagerAdpater.getPageTitle(i))
                            .setTabListener(this));
        }

        initSections();
    }

    /**
     * MARK: Overrides
     */

    @Override
    public void onBackPressed() {
        if(!getFlow().goBack()) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
        rootPresenter.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @DebugLog
    @Override
    public void go(Backstack nextBackstack, Flow.Direction direction, Flow.Callback callback) {

        BasePresenter from = (BasePresenter) getFlow().getBackstack().current().getScreen();
        BasePresenter to = (BasePresenter)nextBackstack.current().getScreen();

    }

    /**
     * MARK: Pager adapter
     */

    public static class DummyPagerAdapter extends PagerAdapter {

        private Section[] sections = new Section[0];
        private Context context;

        public DummyPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = sections[position].getPresenter().getView(context, container);
            view.setTag(position);
            container.addView(view);
            return view;

        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public Section getSection(int position) {
            return sections[position];
        }

        public void setSections(Section[] sections) {
            this.sections = sections;
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }

    /**
     * MARK: Private methods
     */

    private void initSections() {
        sections = new Section[] {new Section(new DummyPresenter(), "Dummy 1"), new Section(new DummyPresenter(), "Dummy 2"), new Section(new DummyPresenter(), "Dummy 3")};
        mDummyPagerAdpater.setSections(sections);
    }

    /**
     * MARK: Public methods
     */
    public Flow getFlow() {
        return flowBundler.getFlow();
    }
}
