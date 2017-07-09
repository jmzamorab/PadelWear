package es.upv.master.padelwear;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.WearableRecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity
        implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient apiClient;
    private static final String ITEM_FOTO = "/item_foto";
    private static final String ASSET_FOTO = "/asset_foto";
    String[] elementos = {"Partida", "Terminar partida", "Historial", "Jugadores", "Notificación", "Pasos", "Pulsaciones", "SwipeDismiss"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).build();
        WearableRecyclerView lista = (WearableRecyclerView) findViewById(R.id.lista);
        Adaptador adaptador = new Adaptador(this, elementos);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer tag = (Integer) v.getTag();
                // Toast.makeText(MainActivity.this, "Elegida opción:" + tag, Toast.LENGTH_SHORT).show();
                switch (tag) {
                    case 0:
                        startActivity((new Intent(MainActivity.this, Contador.class)));
                        break;
                    case 1:
                        startActivity((new Intent(MainActivity.this, Confirmacion.class)));
                        break;
                    case 2:
                        startActivity((new Intent(MainActivity.this, Historial.class)));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, Jugadores.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, Pasos.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, CuentaPasos.class));
                        break;
                    case 7:
                        startActivity(new Intent(MainActivity.this, SwipeDismiss.class));
                        break;
                }
            }
        });
        lista.setAdapter(adaptador);
        lista.setCenterEdgeItems(true);
        //lista.setLayoutManager(new CurvedChildLayoutManager(this));
        lista.setLayoutManager(new MyChildLayoutManager(this));
        lista.setCircularScrollingGestureEnabled(true);
        lista.setScrollDegreesPerScreen(180);
        lista.setBezelWidth(0.5f);
    }


    @Override
    public void onDataChanged(DataEventBuffer eventos) {
        for (DataEvent evento : eventos) {
            if (evento.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = evento.getDataItem();
                if (item.getUri().getPath().equals(ITEM_FOTO)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(item);
                    Asset asset = dataMapItem.getDataMap().getAsset(ASSET_FOTO);
                    LoadBitmapFromAsset tarea = new LoadBitmapFromAsset();
                    tarea.execute(asset);
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(apiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    class LoadBitmapFromAsset extends AsyncTask<Asset, Void, Bitmap> {
        private static final int TIMEOUT_MS = 2000;

        @Override
        protected Bitmap doInBackground(Asset... assets) {
            if (assets.length < 1 || assets[0] == null) {
                throw new IllegalArgumentException("El asset no puede ser null");
            }

            ConnectionResult resultado = apiClient.blockingConnect(TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!resultado.isSuccess()) {
                return null;
            } // convertimos el asset en Stream, bloqueando hasta tenerlo
            InputStream assetInputStream = Wearable.DataApi.getFdForAsset(apiClient, assets[0]).await().getInputStream();
            if (assetInputStream == null) {
                Log.w("Sincronización", "Asset desconocido");
                return null;
            } // decodificamos el Stream en un Bitmap
            return BitmapFactory.decodeStream(assetInputStream);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ((BoxInsetLayout) findViewById(R.id.boxInsert)).setBackground(new BitmapDrawable(getResources(), bitmap));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onStop() {
        Wearable.DataApi.removeListener(apiClient, this);
        if (apiClient != null && apiClient.isConnected()) {
            apiClient.disconnect();
        }
        super.onStop();
    }

}

