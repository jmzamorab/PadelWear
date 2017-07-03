package es.upv.master.padelwear;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by padres on 01/07/2017.
 */

public class ServicioEscuchador extends WearableListenerService {
    private static final String MOVIL_ARRANCAR_ACTIVIDAD="/start_paddle";
    private static final String WEAR_PUNTUACION = "/puntuacion";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.wtf("MOBIL-MessageReceived", "CLASE Servicio Escuchador Ha llegado un mensaje .... ");
        if (messageEvent.getPath().equalsIgnoreCase(MOVIL_ARRANCAR_ACTIVIDAD)) {
            Log.wtf("MOBIL-MessageReceived", "Ha llegado Arrancar Movil");
            Intent intent = new Intent(this, ContadorMovil.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }
}
