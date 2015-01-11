package fr.northborders.eyja.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.northborders.eyja.R;
import fr.northborders.eyja.util.Utils;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public class DummyView extends LinearLayout {

    @InjectView(R.id.text1)
    TextView txtDummy;

    public DummyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);
        Utils.inject(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.inject(this);

        txtDummy.setText(getResources().getString(R.string.dummy_section_text));
    }

}
