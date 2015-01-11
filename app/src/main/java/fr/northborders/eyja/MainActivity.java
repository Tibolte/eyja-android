package fr.northborders.eyja;

import android.app.Activity;
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
import android.widget.FrameLayout;

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


public class MainActivity extends Activity implements Flow.Listener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private FrameLayout switcher;

    /**
     * MARK: Lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        switcher = (FrameLayout) findViewById(R.id.switcher);
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

    @DebugLog
    @Override
    public void go(Backstack nextBackstack, Flow.Direction direction, Flow.Callback callback) {

    }
}
