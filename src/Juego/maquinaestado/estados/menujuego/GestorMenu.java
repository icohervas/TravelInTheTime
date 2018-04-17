package Juego.maquinaestado.estados.menujuego;

import java.awt.*;

import Juego.graficos.SuperficieDibujo;
import Juego.maquinaestado.EstadoJuego;

public class GestorMenu implements EstadoJuego {
//declaramos una variable de tipo superficie de dibujo.
	private final SuperficieDibujo sd;

        //declaramos una variable de tipo estructura menu
        //que contiene toda la estructura del menu.
	private final EstructuraMenu estructuraMenu;
        
        //aqui instanciamos  un array para guardar  las secciones del menu
	private final SeccionMenu[] secciones;
        //aqui guardamos la seccion actual.
	private SeccionMenu seccionActual;
        //declaramos variables enteras para la posicion x, ,y ,ancho y alto.
        private int posicionxBoton;
        private int posicionyBoton;
        private int anchoBoton;
        private int altoBoton;

	public GestorMenu(final SuperficieDibujo sd)
        {
            
		this.sd = sd;

		estructuraMenu = new EstructuraMenu();

                //creamos un  array para guardar las secciones del menu.
		secciones = new SeccionMenu[2];
                //situamos la posicion x del boton cogiendo como referencia el rectangulo lateral izquierdo y sumando 20 pixeles que hemos 
                //definido como margen horizontal.
                posicionxBoton=estructuraMenu.BANNER_LATERAL.x + estructuraMenu.MARGEN_HORIZONTAL_ETIQUETAS;
                 //situamos la posicion x del boton cogiendo como referencia el rectangulo superior y sumando 20 pixeles que hemos 
                //definido como margen vertical.
                posicionyBoton=estructuraMenu.BANNER_LATERAL.y + estructuraMenu.MARGEN_VERTICAL_ETIQUETAS;
                
                anchoBoton=estructuraMenu.ANCHO_ETIQUETAS;
                altoBoton=estructuraMenu.ALTO_ETIQUETAS;
                
		final Rectangle etiquetaInventario = new Rectangle(posicionxBoton,posicionyBoton,anchoBoton, altoBoton);

                //creamos el boton del inventario, y lo guardamos en el array de tipo seccion de menu.
		secciones[0] = new MenuInventario("Inventario", etiquetaInventario, estructuraMenu);

		final Rectangle etiquetaEquipo = new Rectangle(
				estructuraMenu.BANNER_LATERAL.x + estructuraMenu.MARGEN_HORIZONTAL_ETIQUETAS,
				etiquetaInventario.y + etiquetaInventario.height + estructuraMenu.MARGEN_VERTICAL_ETIQUETAS,
				estructuraMenu.ANCHO_ETIQUETAS, estructuraMenu.ALTO_ETIQUETAS);
                
                //creamos el boton del equipo, y lo guardamos en el array de tipo seccion de menu.
		secciones[1] = new MenuEquipo("Equipo", etiquetaEquipo, estructuraMenu);
		seccionActual = secciones[0];
	}

        @Override
	public void actualizar() 
        {
		for (int i = 0; i < secciones.length; i++) 
                {
			if (sd.obtenerRaton().obtenerClick() && sd.obtenerRaton().obtenerRectanguloPosicion()
					.intersects(secciones[i].obtenerEtiquetaMenuEscalada()))
                        {
                            if (secciones[i] instanceof  MenuEquipo) 
                            {
                                MenuEquipo seccion = (MenuEquipo) secciones[i];
                                    if (seccion.objetoSeleccionado != null) 
                                    {
                                        seccion.eliminarObjetoSeleccionado();
                                    }
                            }

			 seccionActual = secciones[i];
			}
		}

	seccionActual.actualizar();
	}

        @Override
	public void dibujar(final Graphics g) 
        {
		estructuraMenu.dibujar(g);

		for (int i = 0; i < secciones.length; i++) 
                {
                    //Si la seccion actual es igual al valor del array de secciones,
                    //si se trata de la primera seccion,correspondiente a la seccio inventario
			if (seccionActual == secciones[i]) 
                        {
                            //
				if (sd.obtenerRaton().obtenerRectanguloPosicion().intersects(secciones[i].obtenerEtiquetaMenuEscalada())) 
                                {
                                    //dibujamos la etiqueta resaltada con el rectangulo naranja para indicar que la hemos seleccionado.
                                    secciones[i].dibujarEtiquetaActivaResaltada(g);
				} else 
                                {
                                    //dibujamos la etiquetaactiva pero sin resaltar, la etiqueta normal.
                                    secciones[i].dibujarEtiquetaActiva(g);
				}
                                
			} 
                        //Si la seccion actual es igual al valor del array de secciones,
                    //si se trata de la segunda seccion,correspondiente a la seccio equipo
                        else 
                        {
				if (sd.obtenerRaton().obtenerRectanguloPosicion()
						.intersects(secciones[i].obtenerEtiquetaMenuEscalada())) 
                                {
                                    secciones[i].dibujarEtiquetaInactivaResaltada(g);
				} else 
                                {
                                    secciones[i].dibujarEtiquetaInactiva(g);
				}
			}
		}

		seccionActual.dibujar(g, sd, estructuraMenu);
	}
}