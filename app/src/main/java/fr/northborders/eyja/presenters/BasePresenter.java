package fr.northborders.eyja.presenters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by thibaultguegan on 06/01/15.
 */
public abstract class BasePresenter {

    public interface PresenterView {
        public void setPresenter(BasePresenter presenter);
    }

    abstract public View getView(Context context, ViewGroup container);

    abstract public String getTitle();

    public void onAttached() {
    }

    public void onDetached() {
    }

}
