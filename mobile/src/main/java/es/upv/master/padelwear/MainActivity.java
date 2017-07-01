package es.upv.master.padelwear;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

//adb -d forward tcp:5601 tcp:5601
public class MainActivity extends AppCompatActivity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks {
    private GoogleApiClient apiClient;
    private static final String MOVIL_ARRANCAR_ACTIVIDAD = "/start_paddle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        apiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).build();
    }

    @Override
    protected void onStop() {
        Wearable.MessageApi.removeListener(apiClient, this);
        super.onStop();
    }

    @Override
    public void onMessageReceived(final MessageEvent mensaje) {
        Log.wtf("Partida Movil", "Ha llegado un mensaje .... ");
        if (mensaje.getPath().equalsIgnoreCase(MOVIL_ARRANCAR_ACTIVIDAD)) {
            Log.wtf("Partida Movil", "EL mensaje es el de arrancar actividad .... ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startContador();
                }
            });
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(apiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
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
           // Log.wtf("Partida Movil", "antes de entrara en la clase Contador Movil");
           // startActivity(new Intent(this, ContadorMovil.class));
            startContador();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startContador()
    {
        Log.wtf("Partida Movil", "antes de entrara en la clase Contador Movil");
        startActivity(new Intent(this, ContadorMovil.class));
    }


}
