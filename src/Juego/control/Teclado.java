package Juego.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import Juego.GestorPrincipal;
import Juego.herramientas.CargadorRecursos;
import Juego.inventario.armas.Arma;
import Juego.protagonistas.Jugador;
import Juego.sonido.Sonido;

public class Teclado implements KeyListener {
	//establecemos una variable, para almacenar el sonido del disparo de la pistola.
	Sonido bang = new Sonido("/sonidos/disparo.wav");
	
	public Tecla arriba = new Tecla();
	public Tecla abajo = new Tecla();
	public Tecla izquierda = new Tecla();
	public Tecla derecha = new Tecla();

	public boolean atacando = false;
	public boolean recogiendo = false;
	public boolean corriendo = false;
	public boolean debug = false;
	public boolean inventarioActivo = false;

        @Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			arriba.teclaPulsada();
			break;
		case KeyEvent.VK_S:
			abajo.teclaPulsada();
			break;
		case KeyEvent.VK_A:
			izquierda.teclaPulsada();
			break;
		case KeyEvent.VK_D:
			derecha.teclaPulsada();
			break;
		case KeyEvent.VK_E:
			recogiendo = true;
			break;
		case KeyEvent.VK_SHIFT:
			corriendo = true;
			break;
		case KeyEvent.VK_F1:
			debug = !debug;
			break;
		case KeyEvent.VK_I:
                    //cambiamo el valor de la variable inventarioactivo.Una especie de interruptor para poder cambiar de estado.
			inventarioActivo = !inventarioActivo;
                        //cada vez que entramos al inventario, establecemos la variable cambiar arma a falso, para permitir cambiar de arma al protagonista.
                        Jugador.cambiarArma=false;
			break;
                   case KeyEvent.VK_R:
			Arma.recargaArma();
                        
			break;     
                        
                        
		case KeyEvent.VK_SPACE:
			atacando = true;
			break;
		case KeyEvent.VK_ESCAPE:
                    //pulsando la tecla escape salimos del juego, se cierra todo.
			System.exit(0);
		}
	}

        @Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			arriba.teclaLiberada();
			break;
		case KeyEvent.VK_S:
			abajo.teclaLiberada();
			break;
		case KeyEvent.VK_A:
			izquierda.teclaLiberada();
			break;
		case KeyEvent.VK_D:
			derecha.teclaLiberada();
			break;
		case KeyEvent.VK_E:
			recogiendo = false;
			break;
		case KeyEvent.VK_SHIFT:
			corriendo = false;
			break;
		case KeyEvent.VK_SPACE:
			atacando = false;
			break;
		}
	}

        @Override
	public void keyTyped(KeyEvent e) {
	}
}