package es.upv.master.padelwear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.GridViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by padres on 14/06/2017.
 */

public class Historial extends Activity {
    private DismissOverlayView dismissOverlay;
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selector2d);
        final GridViewPager paginador = (GridViewPager) findViewById(R.id.paginador);
        paginador.setAdapter(new AdaptadorGridPager(this, getFragmentManager()));
        dismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        dismissOverlay.setIntroText("Para salir de la aplicación, haz una pulsación larga");
        dismissOverlay.showIntroIfNecessary();
        // Configuramos el detector de pulsaciones
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent evento) {
                dismissOverlay.show();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        return detector.onTouchEvent(evento) || super.onTouchEvent(evento);
    }
}
