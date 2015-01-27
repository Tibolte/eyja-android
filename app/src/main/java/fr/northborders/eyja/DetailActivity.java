package fr.northborders.eyja;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.northborders.eyja.ui.TransitionAdapter;
import fr.northborders.eyja.util.Keys;
import fr.northborders.eyja.util.Utils;

/**
 * Created by thibaultguegan on 26/01/15.
 */
public class DetailActivity extends Activity {

    @InjectView(R.id.container)
    LinearLayout container;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);

        setupText();
        if(Utils.hasLollipop()) {
            applySystemWindowsBottomInset(R.id.container);
            addWindowListener();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void colorize(Bitmap photo) {
        Palette palette = Palette.generate(photo);
        applyPalette(palette);
    }

    private void applyPalette(Palette palette) {
        Resources res = getResources();

        container.setBackgroundColor(palette.getDarkMutedColor(res.getColor(R.color.default_dark_muted)));
        findViewById(R.id.content).setBackgroundColor(palette.getDarkMutedColor(res.getColor(R.color.default_dark_muted)));

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setTextColor(palette.getVibrantColor(res.getColor(R.color.default_vibrant)));

        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setTextColor(palette.getLightVibrantColor(res.getColor(R.color.default_light_vibrant)));
    }

    private void setupText() {
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(getIntent().getStringExtra(Keys.KEY_TITLE));

        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText(getIntent().getStringExtra(Keys.KEY_DESCRIPTION));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addWindowListener() {
        getWindow().getEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                getWindow().getEnterTransition().removeListener(this);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private void applySystemWindowsBottomInset(int container) {
        View containerView = findViewById(container);
        containerView.setFitsSystemWindows(true);
        containerView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                if (metrics.widthPixels < metrics.heightPixels) {
                    view.setPadding(0, 0, 0, windowInsets.getSystemWindowInsetBottom());
                } else {
                    view.setPadding(0, 0, windowInsets.getSystemWindowInsetRight(), 0);
                }
                return windowInsets.consumeSystemWindowInsets();
            }
        });
    }

}
