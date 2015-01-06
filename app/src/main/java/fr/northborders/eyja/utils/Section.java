package fr.northborders.eyja.utils;

import fr.northborders.eyja.presenters.BasePresenter;

/**
 * Created by thibaultguegan on 06/01/15.
 */
public class Section {

    private BasePresenter presenter;
    private String title;

    public Section(BasePresenter presenter, String title) {
        this.presenter = presenter;
        this.title = title;
    }

    public BasePresenter getPresenter() {
        return presenter;
    }

    public String getTitle() {
        return title;
    }
}
