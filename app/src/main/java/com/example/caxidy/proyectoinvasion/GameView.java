package com.example.caxidy.proyectoinvasion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView{
    protected HiloGameLoop hiloLoop;
    Canvas nuestroCanvas;
    private List<MuerteEnemigo> muEnem = new ArrayList<MuerteEnemigo>();
    private List<Enemigo> enemigos = new ArrayList<Enemigo>();
    private List<Flecha> flechas = new ArrayList<Flecha>(); //array con los cuatro bitmap de las cuatro posiciones de una flecha
    private List<Fuego> llamas = new ArrayList<Fuego>();
    protected  Flecha flechaActual;
    protected Personaje personaje;
    protected BotonFlecha botonFlecha;
    protected float yBoton, xBoton;
    private long lastClick;
    protected Bitmap bmpKillEnem,bmpKillPj, bitmapBoton;
    private SoundPool sp;
    protected int sonidoKillEnem = 0;
    protected int sonidoKillPj = 0;
    protected int sonidoFlecha = 0;
    protected int sonidoFuego = 0;
    protected int sonidoHitPj = 0;
    public MainActivity contexto;
    public boolean finJuego = false;

    public GameView(Context context){
        super(context);

        contexto = (MainActivity) context;
        //Configuracion de los sonidos
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC,0);
        sonidoKillEnem = sp.load(context,R.raw.muerte_enemigo,1);
        sonidoKillPj = sp.load(context,R.raw.muerte_pj,1);
        sonidoFlecha = sp.load(context,R.raw.disparo_flecha,1);
        sonidoFuego = sp.load(context,R.raw.bola_fuego,1);
        sonidoHitPj = sp.load(context,R.raw.pj_herido,1);

        //Bitmap del boton de disparo
        bitmapBoton = BitmapFactory.decodeResource(getResources(), R.drawable.botondisparo);

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
                crearFuego();
                crearEnemigos();
                crearPersonaje();
                crearBotonDisparo();
                crearFlechas();
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
        return new Enemigo(this,bmp,res,llamas);
    }

    private void crearPersonaje(){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.personaje);
        personaje = new Personaje(this,bmp);
    }

    private void crearBotonDisparo(){
        botonFlecha = new BotonFlecha(this,bitmapBoton);
    }

    private void crearFlechas(){
        Bitmap flechadcha = BitmapFactory.decodeResource(getResources(), R.drawable.flechadcha);
        Flecha flecha1 = new Flecha(this,flechadcha);
        flechas.add(flecha1);
        Bitmap flechaizq = BitmapFactory.decodeResource(getResources(), R.drawable.flechaizq);
        Flecha flecha2 = new Flecha(this,flechaizq);
        flechas.add(flecha2);
        Bitmap flechaarr = BitmapFactory.decodeResource(getResources(), R.drawable.flechaarriba);
        Flecha flecha3 = new Flecha(this,flechaarr);
        flechas.add(flecha3);
        Bitmap flechaab = BitmapFactory.decodeResource(getResources(), R.drawable.flechaabajo);
        Flecha flecha4 = new Flecha(this,flechaab);
        flechas.add(flecha4);
    }

    private void crearFuego(){
        Bitmap llamaarr = BitmapFactory.decodeResource(getResources(), R.drawable.fuegoarriba);
        Fuego fu1 = new Fuego(this,llamaarr);
        llamas.add(fu1);
        Bitmap llamadcha = BitmapFactory.decodeResource(getResources(), R.drawable.fuegodcha);
        Fuego fu2 = new Fuego(this,llamadcha);
        llamas.add(fu2);
        Bitmap llamaab = BitmapFactory.decodeResource(getResources(), R.drawable.fuegoabajo);
        Fuego fu3 = new Fuego(this,llamaab);
        llamas.add(fu3);
        Bitmap llamaizq = BitmapFactory.decodeResource(getResources(), R.drawable.fuegoizq);
        Fuego fu4 = new Fuego(this,llamaizq);
        llamas.add(fu4);
    }

    //Se elimina a un enemigo
    public void matarEnemigo(int pos,int x, int y){
        if(enemigos.get(pos)!=null)
            enemigos.remove(pos);
        //Aparece el sprite temporal
        muEnem.add(new MuerteEnemigo(muEnem, this, x, y, bmpKillEnem));
        //suena el sonido correspondiente
        sp.play(sonidoKillEnem,1,1,1,0,1.0f);
    }
    //Se le quita una vida al personaje
    public void quitarVida(float x, float y){
        //!!resta vida, sonido de golpe, y si vida = 0 sonido kill, sprite kill y fin del juego
        System.out.println("- UNA VIDA....");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Guardamos el canvas para usarlo en otras clases
        nuestroCanvas = canvas;
        //Dibujar el fondo
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundmazmo),0,0,null);

        //Dibujamos los sprites temporales
        for (int i = muEnem.size() - 1; i >= 0; i--) {
            muEnem.get(i).onDraw(canvas);
        }
        //Dibujamos los enemigos
        for (Enemigo enemigo : enemigos) {
            enemigo.onDraw(canvas);
            //Dispara fuego si el numero aleatorio que sale es 5
            if((int)(Math.random()*100+1)==5){
                enemigo.disparaFuego=true;
                enemigo.pj=personaje;
                sp.play(sonidoFuego,1,1,1,0,1.0f);
            }
            //!!¿?¿no aparece la llama
            if(enemigo.disparaFuego){
                //Dibujamos la llama, si la hay
                if(enemigo.fuegoActual != null && enemigo.fuegoActual.vivo)
                    enemigo.fuegoActual.onDraw(canvas);
                else
                    enemigo.fuegoActual = null;
                enemigo.disparaFuego=false;
            }
        }

        //Actualizamos la lista de enemigos vivos del personaje
        personaje.enemigosVivos=enemigos;
        //...y lo dibujamos
        personaje.onDraw(canvas);

        //Dibujamos el boton de los disparos
        botonFlecha.onDraw(canvas);

        //Dibujamos la flecha, si la hay
        if(flechaActual != null && flechaActual.viva)
            flechaActual.onDraw(canvas);
        else
            flechaActual = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();

            //Se calcula la posicion donde va a estar el boton de disparo, para reservar ese espacio para disparar y no para moverse
            yBoton = getHeight()-bitmapBoton.getHeight();
            xBoton = getWidth()-bitmapBoton.getWidth();

            synchronized (getHolder()) {
                if(y>=yBoton && x>=xBoton){
                    //Si hemos pulsado el boton de disparo...disparamos una flecha
                    flechaActual = flechas.get(personaje.spriteEscogido); //cogemos el sprite de la flecha correcto...
                    //obtenemos la posicion de personaje, desde donde saldra la flecha, y la direccion que tomara dicha flecha
                    flechaActual.obtenerPosPj(personaje.x,personaje.y,personaje.spriteEscogido);
                    //guardamos la lista de enemigos vivos para que si la flecha choca con un enemigo podamos saberlo
                    flechaActual.enemigosVivos=enemigos;
                    sp.play(sonidoFlecha,1,1,1,0,1.0f);
                }
                else {
                    //Si NO hemos pulsado el boton de disparo...el personaje se mueve en la direccion que hemos tocado
                    personaje.dirX = x;
                    personaje.dirY = y;
                    personaje.movimientoGradual();
                }

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
