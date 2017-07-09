package es.upv.master.padelwear;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.util.Date;

//adb -d forward tcp:5601 tcp:5601
public class MainActivity extends AppCompatActivity implements SensorEventListener
{

    public static ContadorPasosSet contadorPasos;
    private SensorManager sensorManager;
    public static GoogleApiClient apiClient;

    private Integer contadorPasosSet;
    private Integer contadorSet;
    private static final String ITEM_FOTO = "/item_foto";
    private static final String ASSET_FOTO = "/asset_foto";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contadorPasos = new ContadorPasosSet();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        apiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    public void init()
    {
        contadorPasosSet = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor != null) {
            sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "¡Contador de pasos no encontrado!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        contadorPasosSet = Math.round(event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onStop() {
        if (apiClient != null && apiClient.isConnected()) {
            apiClient.disconnect();
        }
        super.onStop();
    }

    private void mandarFoto() {
        Intent intencion = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intencion.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intencion, 1234);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent datos) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            Bundle extras = datos.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            Asset asset = createAssetFromBitmap(bitmap);
            //COPIAR
            PutDataMapRequest putDataMapReq = PutDataMapRequest.create(ITEM_FOTO);
            putDataMapReq.getDataMap().putAsset(ASSET_FOTO, asset);
            putDataMapReq.getDataMap().putLong("marca_de_tiempo", new Date().getTime());
            PutDataRequest request = putDataMapReq.asPutDataRequest();
            PendingResult<DataApi.DataItemResult> resultdado = Wearable.DataApi.putDataItem(apiClient, request);
        }
    }

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.accion_contador) {
            Log.wtf("Partida Movil", "Abro la partida porque me lo piden desde el Wear");
            startContador();
            return true;
        }
        if (id == R.id.tomar_foto) {
            Log.wtf("Partida Movil", "Mandar foto al wear");
            mandarFoto();
            return true;
        }
        if (id == R.id.pasos) {
            Log.wtf("Partida Movil", "Mandar foto al wear");
            startActivity(new Intent(this, Pasos2.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startContador() {
        Log.wtf("Partida Movil", "antes de entrara en la clase Contador Movil");
        startActivity(new Intent(this, ContadorMovil.class));
    }

}



