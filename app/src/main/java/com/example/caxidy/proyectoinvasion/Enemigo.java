package com.example.caxidy.proyectoinvasion;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemigo {
    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 0 up, 3 left, 2 down, 1 right
    int[] DIRECTION_TO_ANIMATION_MAP = { 0, 3, 2, 1 };
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private static final int MAX_SPEED = 5;
    public int x = 0;
    public int y = 0;
    private int xSpeed = 5;
    private int ySpeed;
    private GameView gameView;
    private Bitmap bmp;
    public int idRecurso;
    private int currentFrame = 0;
    private int width;
    private int height;
    int spriteEscogido=-1;
    private List<Fuego> llamas = new ArrayList<Fuego>();
    boolean disparaFuego=false;
    Fuego fuegoActual;
    Personaje pj;

    public Enemigo(GameView gameView, Bitmap bmp, int idRec,List<Fuego> fuegs) {
        this.gameView = gameView;
        this.bmp = bmp;
        this.idRecurso= idRec;
        llamas=fuegs;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        Random rnd = new Random();
        //Los enemigos no pueden aparecer en x=0 e y=0 al principio, porque el pj aparece ahi
        do{
            x = rnd.nextInt(gameView.getWidth() - width);
            y = rnd.nextInt(gameView.getHeight() - height);
        }while(x==0 && y==0);

        xSpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
        ySpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
    }

    private void update() {
        if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
            xSpeed = -xSpeed;
        }
        x += xSpeed;

        if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
            ySpeed = -ySpeed;
        }
        y += ySpeed;

        currentFrame = ++currentFrame % BMP_COLUMNS;
        if(disparaFuego){
            fuegoActual=llamas.get(spriteEscogido);
            fuegoActual.obtenerPosEnemigo(x,y,spriteEscogido);
            fuegoActual.pj=pj; //si le da al personaje, tiene que perder una vida
        }
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = currentFrame * width;
        int srcY = getAnimationRow() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
        //!!pero esto debe ir en el onDraw del gameView porque tiene q ser independiente a la clase del enemigo
        /*if(disparaFuego){
            //Dibujamos la llama, si la hay
            if(fuegoActual != null && fuegoActual.vivo)
                fuegoActual.onDraw(gameView.nuestroCanvas);
            else
                fuegoActual = null;
            disparaFuego=false;
        }*/
    }

    private int getAnimationRow() {
        double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        spriteEscogido=DIRECTION_TO_ANIMATION_MAP[direction];
        return spriteEscogido;
    }

    //Comprueba si el enemigo ha sido golpeado por una flecha o choca con el personaje
    public boolean esGolpeado(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }
}
