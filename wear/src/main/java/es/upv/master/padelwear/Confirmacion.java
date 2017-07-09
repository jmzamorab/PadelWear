package es.upv.master.padelwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.DismissOverlayView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by padres on 13/06/2017.
 */

public class Confirmacion extends Activity {
    private DismissOverlayView dismissOverlay;
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmacion);
        dismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay_conf);
        dismissOverlay.setIntroText("Para salir de la aplicación, haz una pulsación larga");
        dismissOverlay.showIntroIfNecessary();
        // Configuramos el detector de pulsaciones
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent evento) {
                dismissOverlay.show();
            }
        });
        ImageButton aceptar = (ImageButton) findViewById(R.id.aceptar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Confirmacion.this, CuentaAtras.class), 9);
            }
        });
        ImageButton cancelar = (ImageButton) findViewById(R.id.cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Accion cancelada", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK) {
            Toast.makeText(this, "Accion aceptada", Toast.LENGTH_SHORT).show();
            //Guardamos datos de partida
            finish();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        return detector.onTouchEvent(evento) || super.onTouchEvent(evento);
    }
}
