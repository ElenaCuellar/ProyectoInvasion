package com.example.caxidy.proyectoinvasion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {

    GameView gameView;
    MediaPlayer musicaFondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView=new GameView(this);
        setContentView(gameView);

        //Config. de la musica de fondo
        musicaFondo = MediaPlayer.create(this,R.raw.hyrule_field);
        musicaFondo.setLooping(true); //reproducir en bucle
        musicaFondo.setVolume(0.1f,0.1f);
        musicaFondo.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView=new GameView(this);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
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
}
