package fr.northborders.eyja.util;

import dagger.Module;
import dagger.Provides;
import fr.northborders.eyja.views.DummyView;
import fr.northborders.eyja.views.HomeView;

/**
 * Created by thibaultguegan on 11/01/15.
 */
@Module(
        injects = {
                DummyView.class,
                HomeView.class,
        },
        library = true
)
public class DaggerConfig {

}
