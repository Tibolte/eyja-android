package fr.northborders.eyja.util;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;

import fr.northborders.eyja.EyjaApp;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public final class Utils {

    public static void inject(Context context, Object thing) {
        ((EyjaApp) context.getApplicationContext()).getGlobalGraph().inject(thing);
    }

    public interface OnMeasuredCallback {
        void onMeasured(View view, int width, int height);
    }

    public static void waitForMeasure(final View view, final OnMeasuredCallback callback) {
        int width = view.getWidth();
        int height = view.getHeight();

        if (width > 0 && height > 0) {
            callback.onMeasured(view, width, height);
            return;
        }

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override public boolean onPreDraw() {
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }

                callback.onMeasured(view, view.getWidth(), view.getHeight());

                return true;
            }
        });
    }

    private Utils() {
    }
}
