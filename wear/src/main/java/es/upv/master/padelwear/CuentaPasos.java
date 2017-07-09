package es.upv.master.padelwear;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.SwipeDismissFrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CuentaPasos extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private TextView count;
    boolean actividadActiva;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta_pasos);
        count = (TextView) findViewById(R.id.contador);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        SwipeDismissFrameLayout root = (SwipeDismissFrameLayout) findViewById(R.id.swipe_dismiss_root_pasos);
        root.addCallback(new SwipeDismissFrameLayout.Callback() {
            @Override
            public void onDismissed(SwipeDismissFrameLayout layout) {
                CuentaPasos.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        actividadActiva = true;
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "¡Contador de pasos no encontrado!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        actividadActiva = false;
        // si desregistras el último, el hardware deja de contar pasos
        // sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (actividadActiva) {
            count.setText(String.valueOf(Math.round(event.values[0])));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}