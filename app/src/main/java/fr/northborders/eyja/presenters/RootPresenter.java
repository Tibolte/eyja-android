package fr.northborders.eyja.presenters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.annotations.Expose;

/**
 * Created by thibaultguegan on 06/01/15.
 */
public class RootPresenter extends BasePresenter {

    @Expose
    private int currentItem;

    public RootPresenter() {
        this(0);
    }

    public RootPresenter(int currentItem) {

        this.currentItem = currentItem;
    }

    @Override
    public View getView(Context context, ViewGroup container) {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }


    public void setCurrentItem(int position) {
        this.currentItem = position;
    }

    public int getCurrentItem() {
        return currentItem;
    }
}
