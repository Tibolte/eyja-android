package fr.northborders.eyja.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.northborders.eyja.presenters.DummyPresenter;

/**
 * Created by thibaultguegan on 06/01/15.
 */
public class DummyView extends RelativeLayout {

    private final String TAG = DummyView.class.getSimpleName();
    private DummyPresenter presenter;
    private TextView txtDummy;

    public DummyView(Context context) {
        super(context);
    }

    public DummyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        txtDummy = (TextView) findViewById(android.R.id.text1);
    }

    public void setPresenter(DummyPresenter presenter) {
        this.presenter = presenter;

        txtDummy.setText("Hey this is a dummy section!");
    }

    public TextView getTxtDummy() {
        return txtDummy;
    }
}
