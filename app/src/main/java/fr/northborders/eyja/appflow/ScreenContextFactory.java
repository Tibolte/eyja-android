package fr.northborders.eyja.appflow;

import android.content.Context;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public interface ScreenContextFactory {
    Context createContext(Screen screen, Context parentContext);

    void destroyContext(Context context);
}
