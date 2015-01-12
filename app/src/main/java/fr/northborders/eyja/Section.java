package fr.northborders.eyja;

import fr.northborders.eyja.appflow.Screen;

/**
 * Created by thibaultguegan on 12/01/15.
 */
public class Section {

    private Screen mScreen;
    private String mTile;

    public Section(Screen screen, String title) {
        this.mScreen = screen;
        this.mTile = title;
    }

    public Screen getScreen() {
        return mScreen;
    }

    public String getTile() {
        return mTile;
    }
}
