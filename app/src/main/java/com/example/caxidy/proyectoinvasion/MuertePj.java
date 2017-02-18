package com.example.caxidy.proyectoinvasion;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class MuertePj {
    private float x;
    private float y;
    private Bitmap bmp;
    private int life = 15;
    public boolean desaparece = false;

    public MuertePj(GameView gameView, float x,float y, Bitmap bmp) {
        this.x = Math.min(Math.max(x - bmp.getWidth() / 2, 0),
                gameView.getWidth() - bmp.getWidth());
        this.y = Math.min(Math.max(y - bmp.getHeight() / 2, 0),
                gameView.getHeight() - bmp.getHeight());
        this.bmp = bmp;
    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.drawBitmap(bmp, x, y, null);
    }

    private void update() {
        //Mientras life no sea menor a 1, el sprite no desaparece de la pantalla
        if (--life < 1)
            desaparece=true;
    }
}
