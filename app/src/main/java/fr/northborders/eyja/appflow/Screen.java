package fr.northborders.eyja.appflow;

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import fr.northborders.eyja.util.ObjectUtils;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public abstract class Screen {
    private SparseArray<Parcelable> viewState;

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof Screen && this.getName().equals(((Screen) o).getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public String getName() {
        return ObjectUtils.getClass(this).getName();
    }

    protected SparseArray<Parcelable> getViewState() {
        return viewState;
    }

    public void setViewState(SparseArray<Parcelable> viewState) {
        this.viewState = viewState;
    }

    public void restoreHierarchyState(View view) {
        if (getViewState() != null) {
            view.restoreHierarchyState(getViewState());
        }
    }

    protected void buildPath(List<Screen> path) {
    }

    public final List<Screen> getPath() {
        List<Screen> path = new ArrayList<>();
        buildPath(path);
        // For convenience, we don't require leaf classes to override buildPath().
        if (path.isEmpty() || isPathLeaf(path)) {
            path.add(this);
        }
        return path;
    }

    private boolean isPathLeaf(List<Screen> path) {
        return !equals(path.get(path.size() - 1));
    }

}
