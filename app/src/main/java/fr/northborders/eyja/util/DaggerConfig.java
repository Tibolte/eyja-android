package fr.northborders.eyja.util;

import dagger.Module;
import fr.northborders.eyja.views.FeedsView;
import fr.northborders.eyja.views.HomeView;
import fr.northborders.eyja.views.VideosView;
import fr.northborders.eyja.views.WeatherView;

/**
 * Created by thibaultguegan on 11/01/15.
 */
@Module(
        injects = {
                HomeView.class,
                FeedsView.class,
                VideosView.class,
                WeatherView.class,
        },
        library = true
)
public class DaggerConfig {

}
