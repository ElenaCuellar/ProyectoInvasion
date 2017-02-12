package com.example.caxidy.proyectoinvasion;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Personaje {
    // direction (posicion en el array)= 0 up, 1 left, 2 down, 3 right,
    // animation (posicion en el sprite) = 2 up, 1 left, 3 down, 0 right
    int[] DIRECTION_TO_ANIMATION_MAP = {2, 1, 3, 0};
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private static final int VELOCIDAD = 30;
    public float x;
    public float y;
    public float dirX;
    public float dirY;
    private GameView gameView;
    private Bitmap bmp;
    private int currentFrame = 0;
    private int width;
    private int height;
    boolean moviendose=true;
    float xInicial, yInicial;
    float direccionX;
    float direccionY;
    float distancia;
    int spriteEscogido=-1;
    List<Enemigo> enemigosVivos = new ArrayList<>();

    public Personaje(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        x = 0;
        y = 0;
        dirX = 0;
        dirY = 0;
        distancia=0;
    }

    public void movimientoGradual(){
        //Se configura el recorrido que tiene que seguir, segun el punto de la pantalla donde se haya tocado

        float distanciaAnterior = distancia; //Por si la distancia da NaN (por la raiz cuadrada negativa)
        distancia= (float)(Math.sqrt(Math.pow(dirX-x,2)-Math.pow(dirY-y,2)));
        if(Float.isNaN(distancia))
            distancia=distanciaAnterior;
        direccionX = (dirX-x) / distancia;
        direccionY = (dirY-y) / distancia;
        xInicial=x;
        yInicial=y;
        moviendose = true;
        System.out.println("CONFIGURACION GRADUAL, distancia: "+distancia+
                ", direccionX: "+direccionX+",direccionY: "+direccionY+",xInicial: "+xInicial+", yInicial: "+yInicial);
    }

    private void update() {
        if(moviendose == true) {
            x += direccionX * VELOCIDAD;
            y += direccionY * VELOCIDAD;
            if (Math.sqrt(Math.pow(x - xInicial, 2) + Math.pow(y - yInicial, 2)) >= distancia) {
                x = dirX;
                y = dirY;
                moviendose = false;
            }
            System.out.println("x: "+x+", y: "+y);
            currentFrame = ++currentFrame % BMP_COLUMNS;
        }
        //Si choca con un monstruo...
        if(chocaEnemigo()){
            gameView.quitarVida(x,y);
        }
    }

    public int escogeSprite(){
        double dirDouble = (Math.atan2(direccionX, direccionY) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        spriteEscogido=DIRECTION_TO_ANIMATION_MAP[direction];
        return spriteEscogido;
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = currentFrame * width;
        int srcY = escogeSprite() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect((int)x, (int)y, (int)x + width, (int)y + height);
        canvas.drawBitmap(bmp, src, dst, null);
    }

    public boolean chocaEnemigo(){
        //Si el personaje choca con algun enemigo devuelve true
        for (Enemigo enemigo : enemigosVivos) {
            if (enemigo!=null && enemigo.esGolpeado(x, y))
                return true;
        }
        return false;
    }

    public boolean esGolpeado(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }
}
