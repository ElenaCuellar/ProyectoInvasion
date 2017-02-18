package com.example.caxidy.proyectoinvasion;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Vida {
    GameView gameView;
    Bitmap bmp;
    int anchura1,anchura2, altura; //anchura1 = primer punto X, anchura2 = segundo punto X

    public Vida(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = bmp;
        anchura1 = bmp.getWidth();
        anchura2 = 0;
        altura = bmp.getHeight();
    }

    public void onDraw(Canvas canvas) {
        Rect canvasDestino = new Rect(gameView.getWidth() -anchura1,0,gameView.getWidth()-anchura2,altura);
        canvas.drawBitmap(bmp, null, canvasDestino, null);
    }
}
