package fr.northborders.eyja;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Backstack;
import flow.Flow;
import flow.HasParent;
import fr.northborders.eyja.appflow.AppFlow;
import fr.northborders.eyja.appflow.FlowBundler;
import fr.northborders.eyja.appflow.Screen;
import fr.northborders.eyja.screenswitcher.FrameScreenSwitcherView;
import fr.northborders.eyja.util.GsonParcer;
import fr.northborders.eyja.views.HomeView;


public class MainActivity extends ActionBarActivity implements Flow.Listener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private Section[] mSections = null;

    /**
     * Persists the {@link Flow} in the bundle. Initialized with the home screen,
     * {@link fr.northborders.eyja.Screens.Dummy}.
     */
    private final FlowBundler mFlowBundler =
            new FlowBundler(new Screens.Home(), MainActivity.this,
                    new GsonParcer<>(new Gson()));

    @InjectView(R.id.container)
    FrameScreenSwitcherView mContainer;

    private AppFlow mAppFlow;

    /**
     * MARK: Lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppFlow = mFlowBundler.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        AppFlow.loadInitialScreen(this);

        initSections();
    }

    @Override public Object getSystemService(String name) {
        if (AppFlow.isAppFlowSystemService(name)) return mAppFlow;
        return super.getSystemService(name);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFlowBundler.onSaveInstanceState(outState);
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

    /**
     * MARK: Overrides
     */

    @Override public void onBackPressed() {
        if (!mContainer.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void go(Backstack nextBackstack, Flow.Direction direction, Flow.Callback callback) {
        Screen screen = (Screen) nextBackstack.current().getScreen();
        mContainer.showScreen(screen, direction, callback);

        setTitle(screen.getClass().getSimpleName());

        ActionBar actionBar = getSupportActionBar();
        boolean hasUp = screen instanceof HasParent;
        actionBar.setDisplayHomeAsUpEnabled(hasUp);
        actionBar.setHomeButtonEnabled(hasUp);

        invalidateOptionsMenu();
    }

    /**
     * MARK: Private methods
     */

    private void initSections() {

        if(mSections == null) {
            mSections = new Section[] {
                    new Section(new Screens.Feeds(), "Feeds"),
                    new Section(new Screens.Videos(), "Videos"),
                    new Section(new Screens.Weather(), "Weather"),
            };
        }

        HomeView mHomeview = (HomeView) mContainer.getChildAt(0);
        mHomeview.setSections(mSections);
    }
}
