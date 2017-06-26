package es.upv.master.padelwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    String[] elementos = {"Partida", "Terminar partida", "Historial", "Notificación", "Pasos", "Pulsaciones", "Terminar partida", "SwipeDismiss"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    case 4:
                        startActivity(new Intent(MainActivity.this, Pasos.class));
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
        lista.setScrollDegreesPerScreen(180); lista.setBezelWidth(0.5f);
    }
}
