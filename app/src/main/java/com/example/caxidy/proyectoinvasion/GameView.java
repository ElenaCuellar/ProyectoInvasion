package com.example.caxidy.proyectoinvasion;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView {
    protected HiloGameLoop hiloLoop;
    private List<TempSprite> temps = new ArrayList<TempSprite>();
    private List<Enemigo> enemigos = new ArrayList<Enemigo>();
    protected Personaje personaje;
    private long lastClick;
    protected Bitmap bmpKillEnem,bmpKillPj;
    private SoundPool sp;
    protected int sonidoKillEnem = 0;
    protected int sonidoKillPj = 0;
    protected int sonidoFlecha = 0;
    protected int sonidoFuego = 0;
    protected int sonidoHitPj = 0;
    protected int sonidoHitEnem = 0;
    public MainActivity contexto;
    public boolean finJuego = false;

    public GameView(Context context) {
        super(context);

        contexto = (MainActivity) context;
        //Configuracion de los sonidos
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC,0);
        sonidoKillEnem = sp.load(context,R.raw.muerte_enemigo,1);
        sonidoKillPj = sp.load(context,R.raw.muerte_pj,1);
        sonidoFlecha = sp.load(context,R.raw.disparo_flecha,1);
        sonidoFuego = sp.load(context,R.raw.bola_fuego,1);
        sonidoHitPj = sp.load(context,R.raw.pj_herido,1);
        sonidoHitEnem = sp.load(context,R.raw.golpe_flecha,1);

        hiloLoop = new HiloGameLoop(this);
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                if(!finJuego) {
                    hiloLoop.setRunning(false);
                    while (retry) {
                        try {
                            hiloLoop.join();
                            retry = false;
                        } catch (InterruptedException e) {
                        }
                    }
                }
                else
                    finJuego=false;
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                crearEnemigos();
                crearPersonaje();
                hiloLoop.setRunning(true);
                hiloLoop.start();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {}
        });
        bmpKillEnem = BitmapFactory.decodeResource(getResources(), R.drawable.killenemigo);
        bmpKillPj = BitmapFactory.decodeResource(getResources(), R.drawable.killpj);
    }

    private void crearEnemigos() {
        int enem1 = R.drawable.enemigo1;
        int enem2 = R.drawable.enemigo2;
        int enem3 = R.drawable.enemigo3;
        int totalEnem = (int)(Math.random()*15+5);
        int random;
        for(int b=1;b<=totalEnem;b++) {
            random = (int)(Math.random()*3+1);
            if(random==3)
                enemigos.add(crearEnemigo(enem1));
            else if (random==2)
                enemigos.add(crearEnemigo(enem2));
            else
                enemigos.add(crearEnemigo(enem3));
        }
    }

    private Enemigo crearEnemigo(int res) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), res);
        return new Enemigo(this,bmp,res);
    }

    private void crearPersonaje(){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.personaje);
        personaje = new Personaje(this,bmp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Dibujar el fondo
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundmazmo),0,0,null);

        for (int i = temps.size() - 1; i >= 0; i--) {
            temps.get(i).onDraw(canvas);
        }
        for (Enemigo enemigo : enemigos) {
            enemigo.onDraw(canvas);
        }
        //Dibujamos al personaje
        personaje.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();

            synchronized (getHolder()) {
                //El personaje se mueve en la direccion que hemos tocado
                personaje.dirX=(int)x;
                personaje.dirY=(int)y;
                personaje.movimientoGradual();

                /*for (int i = enemigos.size() - 1; i >= 0; i--) {
                    Enemigo enemigo = enemigos.get(i);
                    if (enemigo.isCollition(x,y)) {
                        enemigos.remove(enemigo);
                        temps.add(new TempSprite(temps, this, x, y, bmpKillEnem));
                        sp.play(sonidoKillEnem,1,1,1,0,1.0f);

                        //sale un dialog de que has terminado el juego si ya no hay mas bolas
                        /*if(bolas.size()<=0){
                            AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(contexto);
                            alertDialogBu.setTitle(contexto.getString(R.string.titulodiag));
                            alertDialogBu.setMessage(contexto.getString(R.string.textodiag));
                            alertDialogBu.setIcon(R.mipmap.ic_launcher);
                            alertDialogBu.setPositiveButton(contexto.getString(R.string.nuevoj), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Se vuelve a empezar el juego
                                    crearSprites();
                                }
                            });
                            alertDialogBu.setNegativeButton(contexto.getString(R.string.salir), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finJuego=true;
                                    hiloLoop.setRunning(false);
                                    contexto.finish();
                                }
                            });

                            AlertDialog alertDialog = alertDialogBu.create();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                        }
                        break;
                    }
                }*/
            }
        }
        return true;
    }

    protected void cerrarJuego(){
        sp.release();
        if(hiloLoop!=null)
            hiloLoop=null;
    }

}
