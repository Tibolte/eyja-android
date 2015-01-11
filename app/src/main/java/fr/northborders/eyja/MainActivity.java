package fr.northborders.eyja;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import flow.Backstack;
import flow.Flow;
import flow.HasParent;
import fr.northborders.eyja.appflow.AppFlow;
import fr.northborders.eyja.appflow.FlowBundler;
import fr.northborders.eyja.appflow.Screen;
import fr.northborders.eyja.screenswitcher.FrameScreenSwitcherView;
import fr.northborders.eyja.util.GsonParcer;
import hugo.weaving.DebugLog;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity implements Flow.Listener{

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Persists the {@link Flow} in the bundle. Initialized with the home screen,
     * {@link fr.northborders.eyja.Screens.Dummy}.
     */
    private final FlowBundler flowBundler =
            new FlowBundler(new Screens.Dummy(), MainActivity.this,
                    new GsonParcer<>(new Gson()));

    @InjectView(R.id.container)
    FrameScreenSwitcherView container;

    private AppFlow appFlow;

    /**
     * MARK: Lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appFlow = flowBundler.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        AppFlow.loadInitialScreen(this);
    }

    @Override public Object getSystemService(String name) {
        if (AppFlow.isAppFlowSystemService(name)) return appFlow;
        return super.getSystemService(name);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        flowBundler.onSaveInstanceState(outState);
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

    @Override public void onBackPressed() {
        if (!container.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @DebugLog
    @Override
    public void go(Backstack nextBackstack, Flow.Direction direction, Flow.Callback callback) {
        Screen screen = (Screen) nextBackstack.current().getScreen();
        container.showScreen(screen, direction, callback);

        setTitle(screen.getClass().getSimpleName());

        ActionBar actionBar = getSupportActionBar();
        boolean hasUp = screen instanceof HasParent;
        actionBar.setDisplayHomeAsUpEnabled(hasUp);
        actionBar.setHomeButtonEnabled(hasUp);

        invalidateOptionsMenu();
    }
}
