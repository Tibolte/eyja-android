package fr.northborders.eyja.util;

import android.os.Build;

/**
 * Created by thibaultguegan on 25/01/15.
 */
public class Utils {
    /**
     * Uses static final constants to detect if the device's platform version is Lollipop or
     * later.
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
