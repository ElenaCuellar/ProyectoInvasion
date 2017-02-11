package com.example.caxidy.proyectoinvasion;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Personaje {
    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 2 up, 1 left, 3 down, 0 right
    int[] DIRECTION_TO_ANIMATION_MAP = {2, 1, 3, 0};
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private static final int VELOCIDAD = 20;
    private int x;
    private int y;
    public int dirX;
    public int dirY;
    private GameView gameView;
    private Bitmap bmp;
    private int currentFrame = 0;
    private int width;
    private int height;
    boolean moviendose=true;
    double xInicial, yInicial;
    double direccionX;
    double direccionY;
    double distancia;

    public Personaje(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        x = 0;
        y = 0;
        dirX = 0;
        dirY = 0;
    }

    public void movimientoGradual(){
        //Se configura el recorrido que tiene que seguir, segun el punto de la pantalla donde se haya tocado
        distancia= Math.sqrt(Math.pow(dirX-x,2)-Math.pow(dirY-y,2));
        direccionX = (dirX-x) / distancia;
        direccionY = (dirY-y) / distancia;
        xInicial=x;
        yInicial=y;
        moviendose = true;
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
            currentFrame = ++currentFrame % BMP_COLUMNS;
        }
    }

    public int escogeSprite(){
        /*if(dirX < (gameView.getWidth()/2) && dirY < (gameView.getHeight()/2)) {
            //Izquierda
            return DIRECTION_TO_ANIMATION_MAP[1];
        }
        else if (dirX >= (gameView.getWidth()/2) && dirY < (gameView.getHeight()/2)) {
            //Abajo
            return DIRECTION_TO_ANIMATION_MAP[3];
        }
        else if(dirX < (gameView.getWidth()/2) && dirY >= (gameView.getHeight()/2)) {
            //Derecha
            return DIRECTION_TO_ANIMATION_MAP[0];
        }
        else if (dirX >= (gameView.getWidth()/2) && dirY >= (gameView.getHeight()/2)) {
            //Arriba
            return DIRECTION_TO_ANIMATION_MAP[2];
        }*/

        double dirDouble = (Math.atan2(direccionX, direccionY) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = currentFrame * width;
        int srcY = escogeSprite() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
    }
}
