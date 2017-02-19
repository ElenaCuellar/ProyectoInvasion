package com.example.caxidy.proyectoinvasion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView{
    protected HiloGameLoop hiloLoop;
    private List<MuerteEnemigo> muEnem = new ArrayList<MuerteEnemigo>();
    public MuertePj muPj = null;
    private List<Enemigo> enemigos = new ArrayList<Enemigo>();
    private List<Flecha> flechas = new ArrayList<Flecha>(); //array con los cuatro bitmap de las cuatro posiciones de una flecha
    private List<Fuego> llamas = new ArrayList<Fuego>();
    protected  Flecha flechaActual;
    protected Fuego fuegoActual;
    protected Personaje personaje;
    protected BotonFlecha botonFlecha;
    protected float yBoton, xBoton;
    private long lastClick;
    protected Bitmap bmpKillEnem,bmpKillPj, bitmapBoton, bmpVida;
    private SoundPool sp;
    protected int sonidoKillEnem = 0;
    protected int sonidoKillPj = 0;
    protected int sonidoFlecha = 0;
    protected int sonidoFuego = 0;
    protected int sonidoHitPj = 0;
    public MainActivity contexto;
    public boolean finJuego = false;
    public List<Vida> vidas = new ArrayList<Vida>();

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
        //Bitmap de la vida
        bmpVida = BitmapFactory.decodeResource(getResources(),R.drawable.vida);

        //Llenamos la lista de vidas con 3 vidas
        for(int i=1;i<4;i++)
            vidas.add(new Vida(this,bmpVida));

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
                crearEnemigos(15);
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

    private void crearEnemigos(int maximo) {
        int enem1 = R.drawable.enemigo1;
        int enem2 = R.drawable.enemigo2;
        int enem3 = R.drawable.enemigo3;
        int totalEnem = (int)(Math.random()*maximo+5);
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
        if(enemigos.get(pos)!=null) {
            //sumamos los puntos (enemigo1=100puntos, enemigo2=200puntos y enemigo3=50puntos)
            if(enemigos.get(pos).idRecurso==R.drawable.enemigo1)
                personaje.puntuacion += 100;
            else if(enemigos.get(pos).idRecurso==R.drawable.enemigo2)
                personaje.puntuacion += 200;
            else if(enemigos.get(pos).idRecurso==R.drawable.enemigo3)
                personaje.puntuacion += 50;
            //lo eliminamos
            enemigos.remove(pos);
        }
        //Aparece el sprite temporal
        muEnem.add(new MuerteEnemigo(muEnem, this, x, y, bmpKillEnem));
        //suena el sonido correspondiente
        sp.play(sonidoKillEnem,1,1,1,0,1.0f);
        //Aparecen nuevos enemigos (entre 1 y 4)
        crearEnemigos(4);
    }
    //Se le quita una vida al personaje
    public void quitarVida(float x, float y){
        boolean vidaRestada=false;
        int i=3;
        while(!vidaRestada && i>0){
            i--;
            if(personaje.vidas[i]) {
                personaje.vidas[i] = false;
                //Quitar un corazon de la pantalla
                vidas.remove(vidas.size()-1);
                vidaRestada=true;
                System.out.println("- UNA VIDA....");
            }
        }
        //Si hemos perdido las tres vidas (es decir, la vida en la pos 0 es false, asi que las dos siguientes tambien), se termina la partida
        if(!personaje.vidas[0]){
            contexto.puntosFinales=personaje.puntuacion; //guardamos los puntos del pj para poder guardarlos en la BD
            //Efectos de la muerte del personaje
            muPj = new MuertePj(this, x, y, bmpKillPj);
            sp.play(sonidoKillPj,1,1,1,0,1.0f);
            personaje = null;
            finJuego=true;
            System.out.println("PARTIDA TERMINADA");
        }
        else
            sp.play(sonidoHitPj,1,1,1,0,1.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Dibujar el fondo
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundmazmo), 0, 0, null);

        //Dibujamos las vidas
        for(int i=0;i<vidas.size();i++) {
            Vida v = vidas.get(i);
            if(i==0){ //vida dibujada en la posicion mas hacia la esquina
                v.anchura1 = v.bmp.getWidth();
                v.anchura2 = 0;
            }
            else if(i==1){
                v.anchura1 = v.bmp.getWidth()*2;
                v.anchura2 = v.bmp.getWidth();
            }
            else{
                v.anchura1 = v.bmp.getWidth()*3;
                v.anchura2 = v.bmp.getWidth()*2;
            }
            v.onDraw(canvas);
        }

        //Dibujar la puntuacion
        if(personaje!=null) {
            Path path = new Path();
            //Rectangulo donde estara el texto de la puntuacion
            path.addRect((float) this.getHeight() / 7, (float) this.getWidth() / 5, (float) this.getHeight() / 7, (float) this.getWidth() / 5, Path.Direction.CCW);
            Paint pincel = new Paint();
            pincel.setColor(getResources().getColor(R.color.colorPuntos));
            pincel.setStrokeWidth(4);
            pincel.setStyle(Paint.Style.FILL_AND_STROKE);
            pincel.setTextSize(30);
            pincel.setTypeface(Typeface.MONOSPACE);
            //Se dibuja el texto en el canvas: a la misma altura que los corazones de vida y el offset de anchura en mitad de la pantalla
            canvas.drawTextOnPath(Integer.toString(personaje.puntuacion) + " "+contexto.getString(R.string.puntos), path,
                    getWidth()/2, bmpVida.getHeight(), pincel);
        }

        //Dibujamos los sprites temporales
        for (int i = muEnem.size() - 1; i >= 0; i--) {
            muEnem.get(i).onDraw(canvas);
        }
        //Dibujamos los enemigos
        for (Enemigo enemigo : enemigos) {
            enemigo.onDraw(canvas);
        }

        if(personaje!=null) {
            //Actualizamos la lista de enemigos vivos del personaje
            personaje.enemigosVivos = enemigos;
            //...y lo dibujamos
            personaje.onDraw(canvas);
        }

        //Dibujamos el boton de los disparos
        botonFlecha.onDraw(canvas);

        //Dibujamos la flecha, si la hay
        if (flechaActual != null && flechaActual.viva)
            flechaActual.onDraw(canvas);
        else
            flechaActual = null;

        //Dibujamos la llama, si la hay
        if (fuegoActual != null && fuegoActual.vivo)
            fuegoActual.onDraw(canvas);
        else
            fuegoActual=null;

        //Dibujamos el sprite de la muerte del personaje, en caso de que haya muerto
        if(muPj!=null && !muPj.desaparece)
            muPj.onDraw(canvas);

        //Si ha acabado la partida, dibujamos el mensaje FIN DEL JUEGO
        if(finJuego) {
            //Primer texto
            Path pathFin = new Path();
            pathFin.addRect((float)this.getHeight() / 4, (float) this.getWidth() / 5, (float) this.getHeight() / 4, (float) this.getWidth() / 5, Path.Direction.CCW);
            Paint pincel = new Paint();
            pincel.setColor(getResources().getColor(R.color.colorFin));
            pincel.setStrokeWidth(4);
            pincel.setStyle(Paint.Style.FILL_AND_STROKE);
            pincel.setTextSize(60);
            pincel.setTypeface(Typeface.MONOSPACE);
            canvas.drawTextOnPath(contexto.getString(R.string.fin1), pathFin,
                    getWidth()/5, getHeight()/2, pincel);
            //Segundo texto
            Path pathFin2 = new Path();
            pathFin2.addRect((float)this.getHeight() / 4, (float) this.getWidth() / 5, (float) this.getHeight() / 4, (float) this.getWidth() / 5, Path.Direction.CCW);
            Paint pincel2 = new Paint();
            pincel2.setColor(getResources().getColor(R.color.colorFin2));
            pincel2.setStrokeWidth(4);
            pincel2.setStyle(Paint.Style.FILL_AND_STROKE);
            pincel2.setTextSize(30);
            pincel2.setTypeface(Typeface.MONOSPACE);
            canvas.drawTextOnPath(contexto.getString(R.string.fin2), pathFin2,
                    getWidth()/9, getHeight()/1.6f, pincel2);
        }
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
                if(personaje!= null && y>=yBoton && x>=xBoton){
                    //Si hemos pulsado el boton de disparo...disparamos una flecha
                    flechaActual = flechas.get(personaje.spriteEscogido); //cogemos el sprite de la flecha correcto...
                    //obtenemos la posicion de personaje, desde donde saldra la flecha, y la direccion que tomara dicha flecha
                    flechaActual.obtenerPosPj(personaje.x,personaje.y,personaje.spriteEscogido);
                    //guardamos la lista de enemigos vivos para que si la flecha choca con un enemigo podamos saberlo
                    flechaActual.enemigosVivos=enemigos;
                    sp.play(sonidoFlecha,1,1,1,0,1.0f);
                }
                else if(personaje!= null) {
                    //Si NO hemos pulsado el boton de disparo...el personaje se mueve en la direccion que hemos tocado
                    personaje.dirX = x;
                    personaje.dirY = y;
                    personaje.movimientoGradual();

                    //Cada vez que caminamos, puede que un enemigo aleatorio dispare fuego
                    //Config. del disparo de fuego: dispara una llama si el numero aleatorio que sale es mayor a 5
                    if ((int) (Math.random() * 100 + 1) > 5) {
                        int posEnemigo=(int) (Math.random() * (enemigos.size()-1)); //escogemos el enemigo que dispara fuego
                        fuegoActual=llamas.get(enemigos.get(posEnemigo).spriteEscogido);
                        fuegoActual.obtenerPosEnemigo(enemigos.get(posEnemigo).x,
                                enemigos.get(posEnemigo).y, enemigos.get(posEnemigo).spriteEscogido);
                        fuegoActual.pj = personaje; //si le da al personaje, tiene que perder una vida
                        sp.play(sonidoFuego, 1, 1, 1, 0, 1.0f);
                    }
                }
                else if(finJuego){
                    //Al acabar la partida y pulsar: mostrar dialog para introducir un nombre de jugador y ya lanzar el intent del ranking
                    contexto.mostrarDialog();
                }


                /*
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
