package fr.northborders.eyja.appflow;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public final class AppFlowContextFactory implements ScreenContextFactory {
    @Nullable
    private final ScreenContextFactory delegate;

    public AppFlowContextFactory() {
        delegate = null;
    }

    public AppFlowContextFactory(ScreenContextFactory delegate) {
        this.delegate = delegate;
    }

    @Override public Context createContext(Screen screen, Context parentContext) {
        if (delegate != null) {
            parentContext = delegate.createContext(screen, parentContext);
        }
        return AppFlow.setScreen(parentContext, screen);
    }

    @Override public void destroyContext(Context context) {
        if (delegate != null) {
            delegate.destroyContext(context);
        }
    }
}