package Juego.maquinaestado.estados.menujuego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import Juego.Constantes;
import Juego.herramientas.DibujoDebug;

public class EstructuraMenu {

    //definimos los colores que vamos a utilizar en la pantalla del menu
	public final Color COLOR_BANNER_SUPERIOR;
	public final Color COLOR_BANNER_LATERAL;
	public final Color COLOR_FONDO;

        //declaramos las variables de los rectangulos para cada una de las secciones.
	public final Rectangle BANNER_SUPERIOR;
	public final Rectangle BANNER_LATERAL;
	public final Rectangle FONDO;

        //declaramos variables que utilizaremos para saber los margenes y el ancho
	public final int MARGEN_HORIZONTAL_ETIQUETAS;
	public final int MARGEN_VERTICAL_ETIQUETAS;
	public final int ANCHO_ETIQUETAS;
	public final int ALTO_ETIQUETAS;

	public EstructuraMenu() {
		COLOR_BANNER_SUPERIOR =Color.blue; //new Color(0xff6700);
		COLOR_BANNER_LATERAL = Color.black;
		COLOR_FONDO = Color.GRAY;

                //rectangulo del banner superior que ocupa todo el ancho del camvas y de altura 30
		BANNER_SUPERIOR = new Rectangle(0, 0, Constantes.ANCHO_JUEGO, 30);
                //dibujamos el rectangulo a partir de la coordenada x del anterior.ocupa todo el alto menos el banner superior.140 pixeles de ancho.
		BANNER_LATERAL = new Rectangle(0, BANNER_SUPERIOR.height, 140, Constantes.ALTO_JUEGO - BANNER_SUPERIOR.height);
                //creamos el rectangulo del fondo, con el espacio que no esta ocupado por los rectangulos creados anterior mente, banner superior y banner lateral.
                //sumamos la coordenada x del banner lateral y le sumamos su ancho.
		FONDO = new Rectangle(BANNER_LATERAL.x + BANNER_LATERAL.width, BANNER_LATERAL.y, Constantes.ANCHO_JUEGO - BANNER_LATERAL.width,
				Constantes.ALTO_JUEGO - BANNER_SUPERIOR.height);

		MARGEN_HORIZONTAL_ETIQUETAS = 20;
		MARGEN_VERTICAL_ETIQUETAS = 20;
		ANCHO_ETIQUETAS = 100;
		ALTO_ETIQUETAS = 20;
	}

	public void actualizar() {

	}

	public void dibujar(final Graphics g) {
		DibujoDebug.dibujarRectanguloRelleno(g, BANNER_SUPERIOR, COLOR_BANNER_SUPERIOR);
		DibujoDebug.dibujarRectanguloRelleno(g, BANNER_LATERAL, COLOR_BANNER_LATERAL);
		DibujoDebug.dibujarRectanguloRelleno(g, FONDO, COLOR_FONDO);
	}
}