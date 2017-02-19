package com.example.caxidy.proyectoinvasion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class Ranking extends AppCompatActivity {
    TextView txNombre, txPuntuacion;
    BDPuntuacion bd;
    ArrayList<Puntuacion> registrosTabla;
    TableLayout tablaL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = new BDPuntuacion(this);
        registrosTabla = bd.listado(); //consultamos todos los registros que haya en la tabla
        setContentView(R.layout.activity_main_ranking);
        init(); //creamos las filas dinamicas de la tabla
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.jugardenuevo){
            //Se crea una nueva partida al cerrar esta actividad
            finish();
        }
        else{
            //Aparece un dialog para confirmar que queremos salir
            AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(this);
            alertDialogBu.setTitle(getString(R.string.diagsalirtitulo));
            alertDialogBu.setMessage(getString(R.string.diagsalirtx));
            alertDialogBu.setIcon(R.mipmap.ic_launcher);
            alertDialogBu.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {}
            });
            alertDialogBu.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //Salir de la app
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //para cerrar todas las actividad y por ultimo la principal
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = alertDialogBu.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    //Añadir filas a la tabla de puntuaciones dinamicamente
    public void init() {
        tablaL = (TableLayout) findViewById(R.id.tableLayout);
        //Parametros de cada fila
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        //Ponemos la fila de cabeceras en primer lugar
        TableRow cabeceras = new TableRow(this);
        cabeceras.setLayoutParams(lp);
        txNombre = new TextView(this);
        txPuntuacion = new TextView(this);
        txNombre.setText(getString(R.string.jugador));
        txNombre.setAllCaps(true);
        txNombre.setPadding(0,0,50,0);
        txPuntuacion.setText(getString(R.string.punt));
        txPuntuacion.setAllCaps(true);
        cabeceras.addView(txNombre);
        cabeceras.addView(txPuntuacion);
        if(tablaL != null)
            tablaL.addView(cabeceras);

        //Agregamos cada registro como una fila nueva en el layout
        for(Puntuacion pun : registrosTabla){
            TableRow row = new TableRow(this);
            row.setLayoutParams(lp);
            txNombre = new TextView(this);
            txPuntuacion = new TextView(this);
            txNombre.setText(pun.getNombre());
            txNombre.setPadding(0,0,50,0);
            txPuntuacion.setText(Integer.toString(pun.getPuntuacion()));
            row.addView(txNombre);
            row.addView(txPuntuacion);
            if(tablaL != null)
                tablaL.addView(row);
        }
    }
}
