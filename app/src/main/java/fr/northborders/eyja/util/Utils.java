package fr.northborders.eyja.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by thibaultguegan on 25/01/15.
 */
public class Utils {

    @SuppressWarnings("deprecation")
    public static void removeOnGlobalLayoutListenerCompat(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (hasJellyBean()) {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
    }

    @SuppressWarnings("deprecation")
    public static void setBackgroundCompat(View v, Drawable drawable) {
        if (hasJellyBean()) {
            v.setBackground(drawable);
        } else {
            v.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Uses static final constants to detect if the device's platform version is Lollipop or
     * later.
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Lollipop or
     * later.
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /***
     * convert layout to bitmap
     * @param view: flameLayout/linearLayout...
     * @param width
     * @param height
     * @return bitmap
     */
    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}
