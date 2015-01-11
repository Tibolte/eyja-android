package fr.northborders.eyja;

import android.app.Application;

import dagger.ObjectGraph;
import fr.northborders.eyja.util.DaggerConfig;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public class EyjaApp extends Application {

    private ObjectGraph globalGraph;

    @Override public void onCreate() {
        super.onCreate();

        globalGraph = ObjectGraph.create(new DaggerConfig());
    }

    public ObjectGraph getGlobalGraph() {
        return globalGraph;
    }

}
