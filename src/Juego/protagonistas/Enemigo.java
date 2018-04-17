package Juego.protagonistas;

import Juego.Constantes;
import Juego.ElementosPrincipales;
import Juego.inteligienciaArtificial.Nodo;
import Juego.herramientas.CalculadoraDistancia;
import Juego.herramientas.DibujoDebug;
import Juego.sonido.Sonido;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static java.util.Optional.empty;
import Juego.sprites.HojaSprites;
import java.util.Iterator;

public class Enemigo {

    protected Sonido lamento;
    protected long duracionLamento;
    protected long lamentoSiguiente = 0;
    protected int idEnemigo;
    protected double posicionX;
    protected double posicionY;
    protected int direccion;
    private final double velocidad = 1;
    protected boolean enMovimiento;
    protected HojaSprites hs;
    protected BufferedImage imagenActual;
    protected static int animacion;
    protected static int estado;
    protected static double EnemigoPosicionX;
    protected static double EnemigoPosicionY;
    protected String nombre;
    protected int vidaMaxima;
    protected float vidaActual;
    protected int JugadorVidaMaxima = 600;
    protected int JugadorVidaActual;
    protected Nodo siguienteNodo;
    protected boolean movimientoDerecha;
    protected static HojaSprites hojaSerpiente;
    protected static HojaSprites hojaZombie;
    protected Enemigo zombie1;
    protected String nombreZombie1;
    protected Enemigo Serpiente1;
    protected String nombreSerpiente1;
    protected ArrayList<Enemigo> ArryaListSerpientes;
    protected ArrayList<Enemigo> ArryaListZombie;

    public Enemigo(final int idEnemigo, final String nombre, final int vidaMaxima, final String rutaLamento) {
        this.idEnemigo = idEnemigo;

        this.posicionX = 0;
        this.posicionY = 0;
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.lamento = new Sonido(rutaLamento);
        this.duracionLamento = lamento.obtenerDuracion();
        ArryaListSerpientes = new ArrayList<>();
        ArryaListZombie = new ArrayList<>();

        if (hojaSerpiente == null) {
            hojaSerpiente = new HojaSprites(Constantes.RUTA_ENEMIGOS + 2 + ".png",
                    Constantes.LADO_SPRITE, false);
        }
        if (hojaZombie == null) {
            hojaZombie = new HojaSprites(Constantes.RUTA_ENEMIGOS + 1 + ".png",
                    Constantes.LADO_SPRITE, false);
        }

    }

    public void actualizar(ArrayList<Enemigo> enemigos) {

        if (lamentoSiguiente > 0) {
            lamentoSiguiente -= 1000000 / 60;
        }

        //Los jugadores se mueven lateralmente.
        //ArryaListSerpientes.addAll(enemigos);
        for (int i = 0; i < enemigos.size(); i++) {

            if (enemigos.get(i).idEnemigo == 1) {

                ArryaListZombie.add(enemigos.get(i));
            }
            if (enemigos.get(i).idEnemigo == 2) {

                ArryaListSerpientes.add(enemigos.get(i));

            }

        }
        //comprobamos que el array de serpientes tiene mas de un elemento.
        if (ArryaListSerpientes.size() >= 1) {
            //ordenamos al ultimo elemento del array, que ataque al jugador.
            ArryaListSerpientes.get(ArryaListSerpientes.size() - 1).moverHaciaSiguienteNodo();
        }

        //System.out.println("arraySerpiente" + ArryaListSerpientes.get(0).obtenerIdEnemigo());
        if (!enemigos.isEmpty() && enemigos.size() > 0) {

            Iterator<Enemigo> iterador = ArryaListZombie.iterator();

            while (iterador.hasNext()) {
                Enemigo enemigo = iterador.next();

                if (enemigo.obtenerVidaActual() <= 0) {
                    iterador.remove();

                } else {
                    if (enemigo.idEnemigo == 1) {
                        enemigo.movimientoLateral();
                    }

                }

            }

            //  System.out.println("array" + enemigos.size()); 
            //  System.out.println("vida" + enemigos.get(10).vidaActual);
        }

    }

    private static void cambiarAnimacionEstado() {

        //System.out.println("animacion" + animacion);
    }

    private void animar() {
        if (!enMovimiento) {
            estado = 0;
            animacion = 0;
        }

        //imagenActual = hs.obtenerSprite(0, estado).obtenerImagen();
    }

    private void movimientoLateral() {
        /* double velocidadLateral=0.005;
       
        if (movimientoDerecha == true) {
            
           posicionX = posicionX + velocidadLateral;
           if(posicionX > 500){
               movimientoDerecha=false;
           }
        }else{
           posicionX = posicionX - velocidadLateral;
           if(posicionX < 400){
               movimientoDerecha=true;
           } 
        }
        enMovimiento = true;*/
    }

    private void moverHaciaSiguienteNodo() {

        double velocidadNodo = 1;
        if (siguienteNodo == null) {
            return;
        }

        int xSiguienteNodo = siguienteNodo.obtenerPosicion().x * Constantes.LADO_SPRITE;
        int ySiguienteNodo = siguienteNodo.obtenerPosicion().y * Constantes.LADO_SPRITE;

        if (posicionX < xSiguienteNodo) {
            posicionX += velocidadNodo;
        }

        if (posicionX > xSiguienteNodo) {
            posicionX -= velocidadNodo;
        }

        if (posicionY < ySiguienteNodo) {
            posicionY += velocidadNodo;
        }

        if (posicionY > ySiguienteNodo) {
            posicionY -= velocidadNodo;
        }
    }

    public void dibujar(final Graphics g, final int puntoX, final int puntoY, BufferedImage imagen) {
        if (vidaActual <= 0) {
            return;
        }

        dibujarBarraVida(g, puntoX, puntoY);
        //DibujoDebug.dibujarRectanguloContorno(g, obtenerArea());
        dibujarDistancia(g, puntoX, puntoY);
        //obtenerDistancia( puntoX,  puntoY);

        int puntoJugadorX = (int) ElementosPrincipales.jugador.dimePosicionX();
        int puntoJugadorY = (int) ElementosPrincipales.jugador.dimePosicionY();
        //Jugador.dibujarBarraVida(g, puntoJugadorX, puntoJugadorY);

    }
//metodo qeu dibuja la barra de vida de los objetos enemigos.

    private void dibujarBarraVida(final Graphics g, final int puntoX, final int puntoY) {
        g.setColor(Color.red);
        DibujoDebug.dibujarRectanguloRelleno(g, puntoX, puntoY - 5, Constantes.LADO_SPRITE * (int) vidaActual / vidaMaxima, 2);

    }

    private void dibujarDistancia(final Graphics g, int puntoX, int puntoY) {

        Point puntoJugador = new Point(
                (int) ElementosPrincipales.jugador.dimePosicionX(),
                (int) ElementosPrincipales.jugador.dimePosicionY()
        );

        int puntoXJugador = (int) ElementosPrincipales.jugador.dimePosicionX();
        int puntoYJugador = (int) ElementosPrincipales.jugador.dimePosicionY();

        Point puntoEnemigo = new Point((int) posicionX, (int) posicionY);

        Double distancia = CalculadoraDistancia.obtenerDistanciaEntrePuntos(puntoJugador, puntoEnemigo);

        //DibujoDebug.dibujarString(g, String.format("%.2f" ,distancia), puntoX, puntoY - 8);
        //System.out.println(distancia);
        double cercania = distancia;
        //System.out.println(cercania);
        //si estamos a menos de 10 pixeles, y entran en contacto los rectangulos de colisiones restamos vida.
        if (cercania < 10) {
            //System.out.println(cercania);
            //llamamos al metodo perder vida del jugador para ir quitando vida.
            Jugador.perderVida((float) 0.5);
            //System.out.println(Jugador.obtenerVidaActual());
            int vidaActualJ = (int) Jugador.dimeVidaActual();
            int vidaMaxJ = (int) Jugador.obtenerVidaMaxima();
            //mostramos en pantalla la vida actual.
            //System.out.println(vidaActualJ);
            //DibujoDebug.dibujarRectanguloRelleno(g, puntoXJugador, puntoYJugador - 5, Constantes.LADO_SPRITE * (int) vidaActualJ / vidaMaxJ, 2);   
        }
        g.setColor(Color.red);
        int vidaahora = 0;
        //Jugador.dibujarBarraVida(g, puntoX, puntoY,vidaahora,600);

    }
//Metodo que nos permite obtener el nombre del enemigo.
    public String obtenerNombreEnemigo() {
        return nombre;
    }
//metodo que nos permite obtener la animacion actual.
    public static double obtenerAnimacion() {
        cambiarAnimacionEstado();
        return animacion;
    }

    public static double obtenerEstado() {
        return estado;
    }

    public void establecerPosicion(final double posicionX, final double posicionY) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    public double obtenerPosicionX() {
        return posicionX;
    }

    public double obtenerPosicionY() {
        return posicionY;
    }
//metodo por el cual podemos saber la posicion x del enemigo en el juego.
//esta posicion la necesitamos para realizar la heurisita de la IA.
    public static double EnemigoObtenerPosicionX() {
        return EnemigoPosicionX;
    }
//metodo por el cual podemos saber la posicion y del enemigo en el juego.
//esta posicion la necesitamos para realizar la heurisita de la IA.
    public static double EnemigoObtenerPosiciony() {
        return EnemigoPosicionY;
    }
//con este metodo obtenemos el identificador del enemigo.
    public int obtenerIdEnemigo() {
        return idEnemigo;
    }

    public float obtenerVidaActual() {
        return vidaActual;
    }

    public String obtenerNombre() {
        return nombre;
    }

    public Rectangle obtenerArea() {
        final int puntoX = (int) posicionX - (int) ElementosPrincipales.jugador.dimePosicionX() + Constantes.MARGEN_X;
        final int puntoY = (int) posicionY - (int) ElementosPrincipales.jugador.dimePosicionY() + Constantes.MARGEN_Y;

        return new Rectangle(puntoX, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
    }

    public void perderVida(float ataqueRecibido) {
        if (lamentoSiguiente <= 0) {
            lamento.reproducir();
            lamentoSiguiente = duracionLamento;
        }

        if (vidaActual - ataqueRecibido < 0) {
            vidaActual = 0;

        } else {
            vidaActual -= ataqueRecibido;
        }
    }

    public Rectangle obtenerAreaPosicional() {
        return new Rectangle((int) posicionX, (int) posicionY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
    }

    public void cambiarSiguienteNodo(Nodo nodo) {
        //cuidado con posible bug
        siguienteNodo = nodo;
    }

    public Nodo obtenerSiguienteNodo() {
        return siguienteNodo;
    }

    public static Double obtenerDistancia(final int puntoX, final int puntoY) {

        Point puntoJugador = new Point(
                (int) ElementosPrincipales.jugador.dimePosicionX(),
                (int) ElementosPrincipales.jugador.dimePosicionY()
        );

        int puntoXJugador = (int) ElementosPrincipales.jugador.dimePosicionX();
        int puntoYJugador = (int) ElementosPrincipales.jugador.dimePosicionY();

        Point puntoEnemigo = new Point((int) puntoX, (int) puntoY);

        Double distancia = CalculadoraDistancia.obtenerDistanciaEntrePuntos(puntoJugador, puntoEnemigo);

        //System.out.println(distancia);
        double cercania = distancia;
        //System.out.println(cercania);
        if (cercania < 10) {
            //System.out.println(cercania);
            //Jugador.perderVida((float) 1);
            //int vidaActualJ = (int) Jugador.obtenerVidaActual();
            //int vidaMaxJ = (int) Jugador.obtenerVidaMaxima();

            //System.out.println(vidaActualJ);
        }

        int vidaahora = 0;
        //Jugador.dibujarBarraVida(g, puntoX, puntoY,vidaahora,600);

        return distancia;
    }

}
