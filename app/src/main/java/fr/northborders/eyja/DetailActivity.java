package fr.northborders.eyja;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.northborders.eyja.util.Keys;

/**
 * Created by thibaultguegan on 26/01/15.
 */
public class DetailActivity extends BaseActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @InjectView(R.id.title)
    TextView txtTitle;

    @InjectView(R.id.description)
    TextView txtDescription;

    @InjectView(R.id.container)
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);

        setData();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail;
    }

    private void setData() {
        final ImageView image = (ImageView) findViewById(R.id.image);
        ViewCompat.setTransitionName(image, Keys.KEY_PHOTO);
        Picasso.with(this)
                .load(getIntent().getStringExtra(Keys.KEY_PHOTO))
                .into(image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        colorize(bitmap);
                    }

                    @Override
                    public void onError() {

                    }
                });

        txtTitle.setText(getIntent().getStringExtra(Keys.KEY_TITLE));
        txtDescription.setText(getIntent().getStringExtra(Keys.KEY_CONTENT));

        if(getIntent().getStringExtra(Keys.KEY_PHOTO) == null) {
            image.setVisibility(View.GONE);
        }
    }

    private void colorize(Bitmap photo) {
        Palette palette = Palette.generate(photo);
        applyPalette(palette);
    }

    public void applyPalette(Palette palette) {
        Resources res = getResources();

        container.setBackgroundColor(palette.getDarkMutedColor(res.getColor(R.color.default_dark_muted)));

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setTextColor(palette.getVibrantColor(res.getColor(R.color.default_vibrant)));

        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setTextColor(palette.getLightVibrantColor(res.getColor(R.color.default_light_vibrant)));
    }

    public static void launch(BaseActivity activity, View transitionView, String url, String title, String content) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, Keys.KEY_PHOTO);
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(Keys.KEY_PHOTO, url);
        intent.putExtra(Keys.KEY_TITLE, title);
        intent.putExtra(Keys.KEY_CONTENT, content);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}
