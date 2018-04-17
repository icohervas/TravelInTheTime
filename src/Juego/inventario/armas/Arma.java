package Juego.inventario.armas;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import Juego.Constantes;
import Juego.protagonistas.Enemigo;
import Juego.protagonistas.Jugador;
import Juego.inventario.Objeto;
import Juego.sonido.Sonido;
import Juego.sprites.HojaSprites;
import Juego.sprites.Sprite;

public abstract class Arma extends Objeto {

    public Sonido sonidoDisparo;
    public static Sonido sonidorecargaArma;

    public static HojaSprites hojaArmas = new HojaSprites(Constantes.RUTA_ARMAS, Constantes.LADO_SPRITE, false);
    public static HojaSprites hojaArmasEscopeta = new HojaSprites(Constantes.RUTA_ARMA_ESCOPETA, Constantes.LADO_SPRITE, false);

    protected int ataqueMinimo;
    protected int ataqueMaximo;
    protected boolean automatica;
    protected boolean penetrante;
    protected double ataquesPorSegundo;
    protected int actualizacionesParaSiguienteAtaque;
    protected static int cantidadDisparos;

    public Arma(final int id, final String nombre, final String descripcion, final int ataqueMinimo,
            final int ataqueMaximo, final boolean automatica, final boolean penetrante, final double ataquesPorSegundo,
            final String rutaSonidoDisparo, final String rutaSonidoRecarga) {

            super(id, nombre, descripcion);

            this.ataqueMinimo = ataqueMinimo;
            this.ataqueMaximo = ataqueMaximo;
            this.automatica = automatica;
            this.penetrante = penetrante;
            this.ataquesPorSegundo = ataquesPorSegundo;
            this.actualizacionesParaSiguienteAtaque = 0;
            this.sonidoDisparo = new Sonido(rutaSonidoDisparo);
            this.sonidorecargaArma = new Sonido(rutaSonidoRecarga);

    }

    public abstract ArrayList<Rectangle> obtenerAlcance(final Jugador jugador);

    public void actualizar() {
        if (actualizacionesParaSiguienteAtaque > 0) {
            actualizacionesParaSiguienteAtaque--;
        }
    }

    public void atacar(final ArrayList<Enemigo> enemigos) 
    {
        /*
    	if (enemigos.isEmpty() || actualizacionesParaSiguienteAtaque > 0) 
        {
            return;
        }
         */
        if (actualizacionesParaSiguienteAtaque > 0) 
        {
            return;
        }
        actualizacionesParaSiguienteAtaque = (int) (ataquesPorSegundo * 60);

        //establecemos que si la cantidad de disparos es menor que 5, el arma se puede disparar
        if (this.DimeCantidadDisparos() < 5) 
        {
            //reproducimos el sonido del disparo de una pistola
            sonidoDisparo.reproducir();
            //aumentamos la cantidad de disparos en 1
            disparaArma();
            //System.out.println("cantidadDisparos: " + cantidadDisparos);
            //si no hay enemigos no hacemos nada.
            if (enemigos.isEmpty()) 
            {
                return;
            }
            //obtenemos el ataque que vamos a efectura
            float ataqueActual = obtenerAtaqueMedio();
            //aplicamos el ataque a todos los enemigos que colisionen con nuestra bala, es penetrante por lo que daña a los que estan detras tambien.
            for (Enemigo enemigo : enemigos) 
            {
                enemigo.perderVida(ataqueActual);
            }

        }

    }
//funcion que genera un ataque medio de forma aleatoria.

    private float obtenerAtaqueMedio() {
        Random r = new Random();

        return r.nextInt(ataqueMaximo - ataqueMinimo) + ataqueMinimo;
    }

    @Override
    public Sprite obtenerSprite() {
        return hojaArmas.obtenerSprite(id - 500);

    }

    @Override
    public Sprite obtenerSpriteEscopeta() {
        return hojaArmasEscopeta.obtenerSprite(id - 501);
    }

    public boolean esAutomatica() {
        return automatica;
    }

    public boolean esPenetrante() {
        return penetrante;
    }

    public int DimeCantidadDisparos() {
        return cantidadDisparos;
    }

    public static void recargaArma() {
        Arma.cantidadDisparos = 0;
        Arma.sonidorecargaArma.reproducir();
    }

    public void disparaArma() {
        this.cantidadDisparos += 1;
    }
}
