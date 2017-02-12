package com.example.caxidy.proyectoinvasion;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Fuego {
    GameView gameView;
    Bitmap bmp;
    int anchura, altura;
    int direccFuego;
    float x,y;
    int velocX, velocY;
    private static final int VELOCIDAD_FUEGO = 40;
    boolean vivo=true;
    Personaje pj;

    public Fuego(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = bmp;
        anchura = bmp.getWidth();
        altura = bmp.getHeight();
        x=0;
        y=0;

        //Velocidad uniforme del fuego
        velocX = VELOCIDAD_FUEGO * 2 - VELOCIDAD_FUEGO;
        velocY = VELOCIDAD_FUEGO * 2 - VELOCIDAD_FUEGO;
    }

    //Tenemos que saber desde donde se dispara el fuego y en que direccion va
    public void obtenerPosEnemigo(float dirXEn,float dirYEn, int dirFuego){
        if(dirFuego!=-1)
            direccFuego=dirFuego;
        x=dirXEn;
        y= dirYEn;
        velocX = VELOCIDAD_FUEGO * 2 - VELOCIDAD_FUEGO;
        velocY = VELOCIDAD_FUEGO * 2 - VELOCIDAD_FUEGO;

        vivo=true;
    }

    public boolean chocaPj(){
        //Si el fuego ha golpeado al personaje, le quita una vida y devuelve true
        if (pj!=null && pj.esGolpeado(x, y)) {
            gameView.quitarVida(pj.x,pj.y);
            return true;
        }
        return false;
    }

    private void updateX() {
        if (x >= gameView.getWidth() - anchura - velocX || x + velocX <= 0 || chocaPj()) {
            //Desaparece si choca con el pj o llega al borde de la pantalla
            velocX = 0;
            vivo=false;
        }
        //si va a la derecha (la direccion es 1) el eje X es positivo, si va a la izquierda es negativo (multiplico por -1)
        if(direccFuego==1)
            x += velocX;
        else
            x += velocX*-1;
    }

    private void updateY() {
        if (y >= gameView.getHeight() - altura - velocY || y + velocY <= 0 || chocaPj()) {
            velocY = 0;
            vivo=false;
        }

        //Hacia arriba (direccion=0) el eje Y es negativo (en este caso) y hacia abajo es positivo
        if(direccFuego==0)
            y += velocY*-1;
        else
            y +=velocY;
    }

    public void onDraw(Canvas canvas) {
        //El fuego va en linea recta hacia arriba, abajo, dcha o izq
        if(direccFuego==1 || direccFuego==3)
            updateX();
        else
            updateY();
        Rect canvasDestino = new Rect((int)x,(int)y,(int)x+anchura,(int)y+altura);
        canvas.drawBitmap(bmp, null, canvasDestino, null);
    }
}
