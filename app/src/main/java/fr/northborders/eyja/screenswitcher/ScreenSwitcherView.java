package fr.northborders.eyja.screenswitcher;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public interface ScreenSwitcherView extends CanShowScreen{
    ViewGroup getCurrentChild();

    ViewGroup getContainerView();

    Context getContext();
}
