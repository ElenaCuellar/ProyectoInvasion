package com.example.caxidy.proyectoinvasion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class Ranking extends AppCompatActivity {
    TextView txNombre, txPuntuacion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_main_ranking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Snackbar.make(t,item.getTitle().toString(),Snackbar.LENGTH_SHORT).show();
        //El primer parametro de Snackbar es un View que debe estar contenido en el padre donde aparecera el Snackbar
        return super.onOptionsItemSelected(item);
    }

    //Añadir filas a la tabla de puntuaciones dinamicamente
    public void init() {
        TableLayout ll = (TableLayout) findViewById(R.id.tableLayout);

        for (int i = 0; i < 2; i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            txNombre = new TextView(this);
            txPuntuacion = new TextView(this);
            txNombre.setText("columnaNombreDeLaBD-RecorrerBD"); //!!
            txPuntuacion.setText("columnaPuntuacionDeLaBD-RecorrerBD"); //!!
            row.addView(txNombre);
            row.addView(txPuntuacion);
            ll.addView(row, i);
        }
    }
}
