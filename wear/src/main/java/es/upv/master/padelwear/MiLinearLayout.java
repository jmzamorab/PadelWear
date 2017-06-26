package es.upv.master.padelwear;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by padres on 16/06/2017.
 */

public class MiLinearLayout extends LinearLayout {
    public MiLinearLayout(Context context) {
        super(context);
    }

    public MiLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MiLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canScrollHorizontally(int dir) {
        return true;
    }
}