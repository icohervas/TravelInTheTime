package Juego;

import Juego.control.GestorControles;
import Juego.graficos.SuperficieDibujo;
import Juego.graficos.Ventana;
import Juego.maquinaestado.GestorEstados;
import Juego.maquinaestado.estados.menujuego.GestorComenzar;
import Juego.sonido.Sonido;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JButton;

public class GestorPrincipal {
	private boolean enFuncionamiento = false;
	private String titulo;
	private int ancho;
	private int alto;

	public static SuperficieDibujo sd;
	private Ventana ventana;
	private GestorEstados ge;

	private static int fps = 0;
	private static int aps = 0;
        
	
	//private final Sonido musica = new Sonido("/sonidos/Juhani Junkala - Epic Boss Battle [Seamlessly Looping].wav");

	public GestorPrincipal(final String titulo, final int ancho, final int alto) {
		this.titulo = titulo;
		this.ancho = ancho;
		this.alto = alto;
	}

	public static void main(String[] args) {
	
                    
                    System.setProperty("sun.java2d.ddforcevram", "True");
		 
                //System.setProperty("sun.java2d.accthreshold","false");
		//System.setProperty("sun.java2d.transaccel", "True");
		
		GestorPrincipal gp = new GestorPrincipal("TraveInTheTime", 1024,768);
		gp.iniciarJuego();
		gp.iniciarBuclePrincipal();
	
	}

	public void iniciarJuego() {
		enFuncionamiento = true;
		inicializar();
		//musica.repetir();
	}

	private void inicializar() {
		sd = new SuperficieDibujo(ancho, alto);
		ventana = new Ventana(titulo, sd);
		ge = new GestorEstados(sd);
                
	}

	public void iniciarBuclePrincipal() {
		int actualizacionesAcumuladas = 0;
		int framesAcumulados = 0;

		final int NS_POR_SEGUNDO = 1000000000;
		final int APS_OBJETIVO = 60;
		final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

		long referenciaActualizacion = System.nanoTime();
		long referenciaContador = System.nanoTime();

		double tiempoTranscurrido;
		double delta = 0;

		while (enFuncionamiento) {
                    
			final long inicioBucle = System.nanoTime();

			tiempoTranscurrido = inicioBucle - referenciaActualizacion;
			referenciaActualizacion = inicioBucle;

			delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;

			while (delta >= 1) {
				actualizar();
				actualizacionesAcumuladas++;
				delta--;
			}

			dibujar();
			framesAcumulados++;

			if (System.nanoTime() - referenciaContador > NS_POR_SEGUNDO) {

				aps = actualizacionesAcumuladas;
				fps = framesAcumulados;

				actualizacionesAcumuladas = 0;
				framesAcumulados = 0;
				referenciaContador = System.nanoTime();
			}
		}
	}

	private void actualizar() {
            System.out.println(GestorComenzar.Comenzar);
            //Si pulsamos la tecla i para activar el inventario
            if (GestorComenzar.Comenzar==0){
                
                ge.cambiarEstadoActual(0);
                
            }else{
               if (GestorControles.teclado.inventarioActivo) {
                    //cambiamos el estado del gestor de estados a 1, que es el estado del juego en modo menu.
			ge.cambiarEstadoActual(2);
                        
		} else {
                    //en caso contrario cambiamos el estado, si el inventarioactivo es falso, cambiamos al estado de modo juego.
			ge.cambiarEstadoActual(1);
		} 
                
            }
		
                //actualizamos el gestor de estados en cada actualizacion del bucle principal
		ge.actualizar();
                //actualizamos la superficie de dibujo en cada actualizacion del bucle principal
		sd.actualizar();
	}

	private void dibujar() {
		sd.dibujar(ge);
	}

	public static int obtenerFPS() {
		return fps;
	}

	public static int obtenerAPS() {
		return aps;
	}
}
