package edu.sungshin.dolfin;

import android.content.Context;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class list_layout extends LinearLayout implements Checkable {

    public list_layout(Context context) {
        super(context);
    }

    @Override
    public void setChecked(boolean checked) {

    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void toggle() {

    }
}
