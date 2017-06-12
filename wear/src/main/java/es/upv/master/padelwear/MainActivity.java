package es.upv.master.padelwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    String[] elementos = {"Partida", "Terminar partida", "Historial", "Notificación", "Pasos", "Pulsaciones", "Terminar partida"};

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
                switch (tag){
                case 1:
                    startActivity((new Intent(MainActivity.this, Confirmacion.class)));
                    break;}
            }
        });
        lista.setAdapter(adaptador);
    }
}