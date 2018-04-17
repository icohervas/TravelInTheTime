package Juego.inventario.armas;

import Juego.protagonistas.Jugador;
import Juego.sprites.Sprite;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Desarmado extends Arma {

	public Desarmado(int id, String nombre, String descripcion, int ataqueMinimo, int ataqueMaximo) {
		super(id, nombre, descripcion, ataqueMinimo, ataqueMaximo, false, false, 0, "/sonidos/golpe.wav", "/sonidos/recarga.wav");
	}

        @Override
	public ArrayList<Rectangle> obtenerAlcance(final Jugador jugador) {
		return null;
	}

    @Override
    public Sprite obtenerSprite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

}
