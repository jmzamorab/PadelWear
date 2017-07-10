package es.upv.master.padelwear;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import es.upv.master.comun.DireccionesGestureDetector;
import es.upv.master.comun.Partida;


public class ContadorMovil extends Activity implements MessageApi.MessageListener,
        GoogleApiClient.ConnectionCallbacks,
        DataApi.DataListener,
        SensorEventListener {
    private Partida partida;
    private SensorManager sensorManager;
    boolean actividadActiva;
    private TextView misPuntos, misJuegos, misSets, susPuntos, susJuegos, susSets, hora;
    private Vibrator vibrador;
    private long[] vibrEntrada = {0l, 500};
    private long[] vibrDeshacer = {0l, 500, 500, 500};
    private GoogleApiClient apiClient;

    //Bundle parametros;
    private static final String MOVIL_ARRANCAR_ACTIVIDAD = "/start_paddle";
    private static final String WEAR_PUNTUACION = "/puntuacion";
    private static final String KEY_MIS_PUNTOS = "com.example.padel.key.mis_puntos";
    private static final String KEY_MIS_JUEGOS = "com.example.padel.key.mis_juegos";
    private static final String KEY_MIS_SETS = "com.example.padel.key.mis_sets";
    private static final String KEY_SUS_PUNTOS = "com.example.padel.key.sus_puntos";
    private static final String KEY_SUS_JUEGOS = "com.example.padel.key.sus_juegos";
    private static final String KEY_SUS_SETS = "com.example.padel.key.sus_sets";


    private String sMisPuntos;
    private String sMisJuegos;
    private String sMisSets;
    private String sSusJuegos;
    private String sSusPuntos;
    private String sSusSets;

    private Boolean isFirst = true;
    private int setMio;
    private int setSuyo;
    ContadorSingleton contadorPasos;
    private int misPasosIni;
    private int misPasos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf("Partida Movil", "pasado onCreate");
        setContentView(R.layout.contador_movil);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Log.wtf("Partida Movil", "pasado Set Content View");
        // Evitar que entre en modo ambiente .... ¿entra, el emulador?
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.wtf("Partida Movil", "ANTES de llamara al contructor de PARTIDA que es de COMUN");
        apiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).build();
        Log.wtf("Partida Movil", "apiClient Creado");
        // parametros = this.getIntent().getExtras();
        contadorPasos = ContadorSingleton.getContadorSingletonInstance();
        partida = new Partida();
        Log.wtf("Partida Movil", "PARTIDA creada");
        vibrador = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        misPuntos = (TextView) findViewById(R.id.misPuntos);
        susPuntos = (TextView) findViewById(R.id.susPuntos);
        misJuegos = (TextView) findViewById(R.id.misJuegos);
        susJuegos = (TextView) findViewById(R.id.susJuegos);
        misSets = (TextView) findViewById(R.id.misSets);
        susSets = (TextView) findViewById(R.id.susSets);
        hora = (TextView) findViewById(R.id.hora);

        Log.wtf("Partida Movil", "Entra no por cambio de datos .....");
        actualizaNumeros(false);
        View fondo = findViewById(R.id.fondo);
        fondo.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector detector = new DireccionesGestureDetector(ContadorMovil.this, new DireccionesGestureDetector.SimpleOnDireccionesGestureListener() {
                @Override
                public boolean onArriba(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                    partida.rehacerPunto();
                    vibrador.vibrate(vibrDeshacer, -1);
                    actualizaNumeros(false);
                    return true;
                }

                @Override
                public boolean onAbajo(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                    partida.deshacerPunto();
                    vibrador.vibrate(vibrDeshacer, -1);
                    actualizaNumeros(false);
                    return true;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent evento) {
                detector.onTouchEvent(evento);
                return true;
            }
        });
        misPuntos.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector detector = new DireccionesGestureDetector(ContadorMovil.this, new DireccionesGestureDetector.SimpleOnDireccionesGestureListener() {
                @Override
                public boolean onDerecha(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                    partida.puntoPara(true);
                    vibrador.vibrate(vibrEntrada, -1);
                    actualizaNumeros(false);
                    return true;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent evento) {
                detector.onTouchEvent(evento);
                return true;
            }
        });
        susPuntos.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector detector = new DireccionesGestureDetector(ContadorMovil.this, new DireccionesGestureDetector.SimpleOnDireccionesGestureListener() {
                @Override
                public boolean onDerecha(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                    partida.puntoPara(false);
                    vibrador.vibrate(vibrEntrada, -1);
                    actualizaNumeros(false);
                    return true;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent evento) {
                detector.onTouchEvent(evento);
                return true;
            }
        });

        PendingResult<DataItemBuffer> resultado = Wearable.DataApi.getDataItems(apiClient);
        resultado.setResultCallback(new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(DataItemBuffer dataItems) {
                Log.wtf("Partida Movil", "Llega de cambio de datos .... ");
                for (DataItem dataItem : dataItems) {
                    if (dataItem.getUri().getPath().equals(WEAR_PUNTUACION)) {
                        DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                        sMisPuntos = dataMap.getString(KEY_MIS_PUNTOS);
                        sMisJuegos = dataMap.getString(KEY_MIS_JUEGOS);
                        sMisSets = dataMap.getString(KEY_MIS_SETS);
                        sSusJuegos = dataMap.getString(KEY_SUS_JUEGOS);
                        sSusPuntos = dataMap.getString(KEY_SUS_PUNTOS);
                        sSusSets = dataMap.getString(KEY_SUS_SETS);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                actualizaNumerosParam();
                            }
                        });
                    }
                }
                dataItems.release();
            }
        });
    }

    void actualizaNumeros(Boolean lInic) {
        Log.wtf("Partida Movil", "ActualizaNúmeros");
        if (lInic) {
            misPuntos.setText("0");
            susPuntos.setText("0");
            misJuegos.setText("0");
            susJuegos.setText("0");
            misSets.setText("0");
            susSets.setText("0");

            // REPENSAR
            // Hacer que la clase ContadorPasos sea un Singleton
            // de manera que en Main voya actualizando su contador
            // y desde Contador "reinicio" o pido datos
            /*MainActivity.contadorPasos.init();
            MainActivity.contadorPasos.getContadorPasos()*/
        } else {
            misPuntos.setText(partida.getMisPuntos());
            susPuntos.setText(partida.getSusPuntos());
            misJuegos.setText(partida.getMisJuegos());
            susJuegos.setText(partida.getSusJuegos());
            misSets.setText(partida.getMisSets());
            susSets.setText(partida.getSusSets());
            if ((Integer.parseInt(partida.getMisSets()) != setMio) ||
                    (Integer.parseInt(partida.getSusSets()) != setSuyo)) {
                int total = misPasos - misPasosIni;
                Toast.makeText(this, String.valueOf(total), Toast.LENGTH_SHORT).show();
            }
        }
    }

    void actualizaNumerosParam() {
        Log.wtf("Partida Movil", "Actualiza Numeros Param");

        misPuntos.setText(sMisPuntos);
        susPuntos.setText(sSusPuntos);
        misSets.setText(sMisSets);
        susSets.setText(sSusSets);
        susJuegos.setText(sSusJuegos);
        misJuegos.setText(sMisJuegos);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.wtf("Partida Movil", "OnStart");
        apiClient.connect();
        isFirst = true;
        // Inicializo con los pasos actuales
        contadorPasos.init(contadorPasos.getPasos());
        setMio = Integer.parseInt(misSets.getText().toString());
        setSuyo = Integer.parseInt(susSets.getText().toString());
    }


    @Override
    protected void onStop() {
        Wearable.MessageApi.removeListener(apiClient, this);
        //Wearable.DataApi.removeListener(apiClient, this);//esto seria de contador con tu metodo
        Wearable.DataApi.removeListener(apiClient, this);
        if (apiClient != null && apiClient.isConnected()) {
            apiClient.disconnect();
        }
        super.onStop();
    }


    @Override
    public void onDataChanged(DataEventBuffer eventos) {
        for (DataEvent evento : eventos) {
            if (evento.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = evento.getDataItem();
                if (item.getUri().getPath().equals(WEAR_PUNTUACION)) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    sMisPuntos = dataMap.getString(KEY_MIS_PUNTOS);
                    sMisJuegos = dataMap.getString(KEY_MIS_JUEGOS);
                    sMisSets = dataMap.getString(KEY_MIS_SETS);
                    sSusJuegos = dataMap.getString(KEY_SUS_JUEGOS);
                    sSusPuntos = dataMap.getString(KEY_SUS_PUNTOS);
                    sSusSets = dataMap.getString(KEY_SUS_SETS);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            actualizaNumerosParam();
                        }
                    });
                }
            } else if (evento.getType() == DataEvent.TYPE_DELETED) { // Algún ítem ha sido borrado } } }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.MessageApi.addListener(apiClient, this);
        Wearable.DataApi.addListener(apiClient, this);
        //Wearable.MessageApi.addListener(apiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onMessageReceived(final MessageEvent mensaje) {
        Log.wtf("Partida Movil", "Ha llegado un mensaje .... ");
        if (mensaje.getPath().equalsIgnoreCase(MOVIL_ARRANCAR_ACTIVIDAD)) {
            Log.wtf("Partida Movil", "EL mensaje es el de arrancar actividad .... ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actualizaNumeros(true);
                }
            });
        }
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
    public void onSensorChanged(SensorEvent event) {
        if (actividadActiva) {
            //contadorPasos.setPasos(Math.round(event.values[0]));
            misPasos = Math.round(event.values[0]);
            Toast.makeText(this, "Pasos = " + String.valueOf(misPasosIni), Toast.LENGTH_SHORT).show();
        }
        else {
            misPasosIni  = Math.round(event.values[0]);
            //contadorPasos.init(Math.round(event.values[0]));
            misPasos = Math.round(event.values[0]);
            Toast.makeText(this, "COMIENZO= " + String.valueOf(misPasosIni), Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        @Override protected void onPause () {
            super.onPause();
            actividadActiva = false;
        }// si desregistras el último, el hardware deja de contar pasos // sensorManager.unregisterListener(this); }
    }
