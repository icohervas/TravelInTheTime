package Juego.maquinaestado;

import java.awt.Graphics;

import Juego.graficos.SuperficieDibujo;
import Juego.maquinaestado.estados.juego.GestorJuego;
import Juego.maquinaestado.estados.menujuego.GestorComenzar;
import Juego.maquinaestado.estados.menujuego.GestorMenu;

public class GestorEstados {

    //declaramos un array de tipo estado juego para guardar los distintos estado en los que se puede encontrar el juego.
	private EstadoJuego[] estados;
        //definimos una variable tipo estado juego para saber cual es el estado actual.
	private EstadoJuego estadoActual;

        
	public GestorEstados(final SuperficieDibujo sd) {
		iniciarEstados(sd);
		iniciarEstadoActual();
	}
//metodo que inicia los estados dle juego, le pasamos como parametro la superficie de dibujo para poder dibujar el menu.
	private void iniciarEstados(final SuperficieDibujo sd) {
            
		estados = new EstadoJuego[3];
		estados[1] = new GestorJuego();
		estados[2] = new GestorMenu(sd);
                estados[0] = new GestorComenzar(sd);
                
		// Añadir e iniciar los demás estados a medida que los creemos
	}

        
        //metodo que inicia el estado inicial del juego en el estado 0
	private void iniciarEstadoActual() {
		estadoActual = estados[0];
	}

	public void actualizar() {
		estadoActual.actualizar();
	}

	public void dibujar(final Graphics g) {
		estadoActual.dibujar(g);
	}

	public void cambiarEstadoActual(final int nuevoEstado) {
		estadoActual = estados[nuevoEstado];
	}

	public EstadoJuego obtenerEstadoActual() {
		return estadoActual;
	}
}