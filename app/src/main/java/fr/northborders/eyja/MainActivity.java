package fr.northborders.eyja;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import flow.Backstack;
import flow.Flow;
import hugo.weaving.DebugLog;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity implements Flow.Listener{

    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.switcher)
    FrameLayout switcher;

    /**
     * MARK: Lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
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
