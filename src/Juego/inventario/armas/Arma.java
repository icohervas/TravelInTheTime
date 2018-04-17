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
	
    public static HojaSprites hojaArmas = new HojaSprites(Constantes.RUTA_ARMAS, Constantes.LADO_SPRITE, false);
    public static HojaSprites hojaArmasEscopeta = new HojaSprites(Constantes.RUTA_ARMA_ESCOPETA, Constantes.LADO_SPRITE, false);

    protected int ataqueMinimo;
    protected int ataqueMaximo;
    protected boolean automatica;
    protected boolean penetrante;
    protected double ataquesPorSegundo;
    protected int actualizacionesParaSiguienteAtaque;

    public Arma(final int id, final String nombre, final String descripcion, final int ataqueMinimo,
                final int ataqueMaximo, final boolean automatica, final boolean penetrante, final double ataquesPorSegundo,
                final String rutaSonidoDisparo) {

        super(id, nombre, descripcion);

        this.ataqueMinimo = ataqueMinimo;
        this.ataqueMaximo = ataqueMaximo;
        this.automatica = automatica;
        this.penetrante = penetrante;
        this.ataquesPorSegundo = ataquesPorSegundo;
        this.actualizacionesParaSiguienteAtaque = 0;
        this.sonidoDisparo = new Sonido(rutaSonidoDisparo);
    }

    public abstract ArrayList<Rectangle> obtenerAlcance(final Jugador jugador);

    public void actualizar() {
        if (actualizacionesParaSiguienteAtaque > 0) {
            actualizacionesParaSiguienteAtaque--;
        }
    }

    public void atacar(final ArrayList<Enemigo> enemigos) {
        /*
    	if (enemigos.isEmpty() || actualizacionesParaSiguienteAtaque > 0) {
            return;
        }
        */
        if (actualizacionesParaSiguienteAtaque > 0)  {
        	return;
        }
        actualizacionesParaSiguienteAtaque = (int) (ataquesPorSegundo * 60);
        
        sonidoDisparo.reproducir();
        
        if (enemigos.isEmpty()) {
        	return;
        }

        float ataqueActual = obtenerAtaqueMedio();

        for (Enemigo enemigo : enemigos) {
            enemigo.perderVida(ataqueActual);
        }
    }

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
}