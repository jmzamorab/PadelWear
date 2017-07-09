package es.upv.master.padelwear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.GridViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by padres on 14/06/2017.
 */

public class Historial extends WearableActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selector2d);

        final GridViewPager paginador = (GridViewPager) findViewById(R.id.paginador);
        paginador.setAdapter(new AdaptadorGridPager(this, getFragmentManager()));
    }


}
