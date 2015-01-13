package fr.northborders.eyja.util;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import fr.northborders.eyja.SampleData;
import fr.northborders.eyja.model.RssFeed;
import fr.northborders.eyja.views.DummyView;
import fr.northborders.eyja.views.FeedsView;
import fr.northborders.eyja.views.HomeView;
import fr.northborders.eyja.views.VideosView;
import fr.northborders.eyja.views.WeatherView;

/**
 * Created by thibaultguegan on 11/01/15.
 */
@Module(
        injects = {
                DummyView.class,
                HomeView.class,
                FeedsView.class,
                VideosView.class,
                WeatherView.class,
        },
        library = true
)
public class DaggerConfig {

}
