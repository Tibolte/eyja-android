package fr.northborders.eyja;

import android.app.Application;
import android.graphics.Typeface;

/**
 * Created by thibaultguegan on 24/01/15.
 */
public class EyjaApplication extends Application {

    private static EyjaApplication instance;
    private static Typeface fontLight, fontRegular, fontMedium, fontBold;

    public static EyjaApplication getInstance(){ return instance; }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();

        fontLight = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");
        fontRegular = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        fontMedium = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Medium.ttf");
        fontBold = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");
    }

    public static Typeface getFontLight() {
        return fontLight;
    }

    public static Typeface getFontRegular() {
        return fontRegular;
    }

    public static Typeface getFontMedium() {
        return fontMedium;
    }

    public static Typeface getFontBold() {
        return fontBold;
    }
}
