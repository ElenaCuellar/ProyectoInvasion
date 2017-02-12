package com.example.caxidy.proyectoinvasion;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Flecha {

    GameView gameView;
    Bitmap bmp;
    int anchura, altura;
    int direccFlecha;
    float x,y;
    int velocX, velocY;
    private static final int VELOCIDAD_FLECHA = 40;
    boolean viva=true;
    List<Enemigo> enemigosVivos = new ArrayList<>();

    public Flecha(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = bmp;
        anchura = bmp.getWidth();
        altura = bmp.getHeight();
        x=0;
        y=0;

        //Velocidad uniforme de la flecha
        velocX = VELOCIDAD_FLECHA * 2 - VELOCIDAD_FLECHA;
        velocY = VELOCIDAD_FLECHA * 2 - VELOCIDAD_FLECHA;
    }

    //Tenemos que saber desde donde se dispara la flecha y en que direccion va
    public void obtenerPosPj(float dirXPj,float dirYPj, int dirFlecha){
        if(dirFlecha!=-1)
            direccFlecha=dirFlecha;
        x=dirXPj;
        y= dirYPj;
        velocX = VELOCIDAD_FLECHA * 2 - VELOCIDAD_FLECHA;
        velocY = VELOCIDAD_FLECHA * 2 - VELOCIDAD_FLECHA;

        viva=true;
    }

    public boolean chocaEnemigo(){
        //Si la flecha ha golpeado a alguno de los enemigos, lo elimina y devuelve true
        for (int i = enemigosVivos.size() - 1; i >= 0; i--) {
            Enemigo enemigo = enemigosVivos.get(i);
            if (enemigo!=null && enemigo.esGolpeado(x, y)) {
                gameView.matarEnemigo(i,enemigo.x,enemigo.y);
                return true;
            }
        }
        return false;
    }

    private void updateX() {
        if (x >= gameView.getWidth() - anchura - velocX || x + velocX <= 0 || chocaEnemigo()) {
            //Desaparece si choca con un enemigo o llega al borde de la pantalla
            velocX = 0;
            viva=false;
        }
        //si va a la derecha (la direccion es 0) el eje X es positivo, si va a la izquierda es negativo (multiplico por -1)
        if(direccFlecha==0)
            x += velocX;
        else
            x += velocX*-1;
    }

    private void updateY() {
        if (y >= gameView.getHeight() - altura - velocY || y + velocY <= 0 || chocaEnemigo()) {
            velocY = 0;
            viva=false;
        }

        //Hacia arriba (direccion=2) el eje Y es negativo (en este caso) y hacia abajo es positivo
        if(direccFlecha==2)
            y += velocY*-1;
        else
            y +=velocY;
    }

    public void onDraw(Canvas canvas) {
        //La flecha va en linea recta hacia arriba, abajo, dcha o izq
        if(direccFlecha==0 || direccFlecha==1)
            updateX();
        else
            updateY();
        Rect canvasDestino = new Rect((int)x,(int)y,(int)x+anchura,(int)y+altura);
        canvas.drawBitmap(bmp, null, canvasDestino, null);
    }
}
