package com.example.caxidy.proyectoinvasion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    GameView gameView;
    MediaPlayer musicaFondo;
    BDPuntuacion bd;
    Puntuacion p;
    long registrosAfec;
    int puntosFinales = 0; //puntos finales de la partida

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Te sales de la actividad principal (y de todas) cuando pulsas la opcion de salir en el menu de ranking
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        gameView=new GameView(this);
        setContentView(gameView);

        //Config. de la musica de fondo
        musicaFondo = MediaPlayer.create(this,R.raw.hyrule_field);
        musicaFondo.setLooping(true); //reproducir en bucle
        musicaFondo.setVolume(0.05f,0.05f);
        musicaFondo.start();

        //Iniciamos la BD de puntuaciones
        bd = new BDPuntuacion(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView=new GameView(this);
        setContentView(gameView);
        musicaFondo.start();
    }

    @Override
    protected void onPause() {

        super.onPause();
        musicaFondo.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //parar la musica de fondo y liberarla
        musicaFondo.stop();
        musicaFondo.release();
        gameView.cerrarJuego();
        gameView = null;
        System.exit(0);
    }

    public void mostrarDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(this);
        alertDialogBu.setView(promptView); //le ponemos el layout con el edittext para introducir el nombre de usuario
        final EditText txNombre = (EditText) promptView.findViewById(R.id.diagTx);
        alertDialogBu.setTitle(getString(R.string.titulodiag));
        alertDialogBu.setMessage(getString(R.string.textodiag));
        alertDialogBu.setIcon(R.mipmap.ic_launcher);
        alertDialogBu.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Guardar la nueva puntuacion en la BD
                p = new Puntuacion(txNombre.getText().toString(),puntosFinales);
                altaPuntuacion(p);
                //Se lanza un intent que muestra el contenido de la tabla  y las opciones de salir o volver a jugar
                Intent intent = new Intent(MainActivity.this,Ranking.class);
                startActivity(intent);
                //Dejar de dibujar en el GameView
                gameView.finDibujar=true;
            }
        });

        AlertDialog alertDialog = alertDialogBu.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    //Alta de un nuevo registro de puntuacion
    public void altaPuntuacion(Puntuacion pun) {

        registrosAfec = bd.insertar(pun);

        if (registrosAfec <= 0)
            Toast.makeText(this,getString(R.string.bderrinsertar),Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,getString(R.string.bdinsertar)+" "+ registrosAfec, Toast.LENGTH_LONG).show();
    }
}
