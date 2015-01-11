package fr.northborders.eyja.screenswitcher;

import flow.Flow;
import fr.northborders.eyja.appflow.Screen;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public interface CanShowScreen {
    void showScreen(Screen screen, Flow.Direction direction, Flow.Callback callback);
}
