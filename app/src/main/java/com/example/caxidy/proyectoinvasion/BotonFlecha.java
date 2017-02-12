package com.example.caxidy.proyectoinvasion;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class BotonFlecha {
    GameView gameView;
    Bitmap bmp;
    int anchura, altura;

    public BotonFlecha(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = bmp;
        anchura = bmp.getWidth();
        altura = bmp.getHeight();
    }

    public void onDraw(Canvas canvas) {
        Rect canvasDestino = new Rect(gameView.getWidth() -anchura,gameView.getHeight() - altura,gameView.getWidth(),gameView.getHeight());
        canvas.drawBitmap(bmp, null, canvasDestino, null);
    }
}
