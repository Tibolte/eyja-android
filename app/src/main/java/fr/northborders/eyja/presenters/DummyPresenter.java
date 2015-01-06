package fr.northborders.eyja.presenters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.northborders.eyja.R;
import fr.northborders.eyja.views.DummyView;

/**
 * Created by thibaultguegan on 06/01/15.
 */
public class DummyPresenter extends BasePresenter {

    private DummyView view;

    @Override
    public View getView(Context context, ViewGroup container) {

        view = (DummyView) LayoutInflater.from(context).inflate(
                R.layout.view_dummy,
                container,
                false);

        view.setPresenter(this);
        return view;
    }

    @Override
    public String getTitle() {
        return null;
    }
}
