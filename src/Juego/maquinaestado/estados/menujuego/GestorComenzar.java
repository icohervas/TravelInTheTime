package Juego.maquinaestado.estados.menujuego;

import Juego.Constantes;
import Juego.control.GestorControles;
import java.awt.*;

import Juego.graficos.SuperficieDibujo;
import Juego.herramientas.CargadorRecursos;
import Juego.herramientas.DibujoDebug;
import Juego.maquinaestado.EstadoJuego;
import Juego.maquinaestado.GestorEstados;
import java.awt.image.BufferedImage;

public class GestorComenzar implements EstadoJuego {
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
    private BufferedImage logo;
    private BufferedImage imagen;
    public static int Comenzar;

	public GestorComenzar(final SuperficieDibujo sd)
        {
            
		this.sd = sd;

		estructuraMenu = new EstructuraMenu();
                
            

                //creamos un  array para guardar las secciones del menu.
		secciones = new SeccionMenu[2];
                //situamos la posicion x del boton cogiendo como referencia el rectangulo lateral izquierdo y sumando 20 pixeles que hemos 
                //definido como margen horizontal.
                posicionxBoton=0;
                 //situamos la posicion x del boton cogiendo como referencia el rectangulo superior y sumando 20 pixeles que hemos 
                //definido como margen vertical.
                posicionyBoton=0;
                
                anchoBoton=estructuraMenu.ANCHO_ETIQUETAS;
                altoBoton=estructuraMenu.ALTO_ETIQUETAS;
                
		final Rectangle etiquetaInventario = new Rectangle(posicionxBoton+500,posicionyBoton+700,anchoBoton+100, altoBoton);

                //creamos el boton del inventario, y lo guardamos en el array de tipo seccion de menu.
		secciones[0] = new MenuInventario("SALIR", etiquetaInventario, estructuraMenu);

		final Rectangle etiquetaEquipo = new Rectangle(500,650,estructuraMenu.ANCHO_ETIQUETAS+100, estructuraMenu.ALTO_ETIQUETAS);
                
                //creamos el boton del equipo, y lo guardamos en el array de tipo seccion de menu.
		secciones[1] = new MenuEquipo("COMENZAR", etiquetaEquipo, estructuraMenu);
		seccionActual = secciones[0];
                //DibujoDebug.dibujarImagen(g, img, 640, 480);
                
	}

        @Override
	public void actualizar() 
        {
		for (int i = 0; i < secciones.length; i++) 
                {
			if (sd.obtenerRaton().obtenerClick() && sd.obtenerRaton().obtenerRectanguloPosicion().intersects(secciones[i].obtenerEtiquetaMenuEscalada()))
                        {
                            if (secciones[i] instanceof  MenuEquipo) 
                            {

                                        //System.out.println("hola");
                                        Comenzar=1532;
                                        
                                    
                            }else{
                                System.out.println("has pulsado salir");
                                System.exit(0);
                            }

			 seccionActual = secciones[i];
			}
		}

	seccionActual.actualizar();
	}

        @Override
	public void dibujar(final Graphics g) 
        {
            
            imagen = CargadorRecursos.cargarImagenCompatibleOpaca(Constantes.RUTA_MENU_COMENZAR);
                
		//estructuraMenu.dibujar(g);
                //DibujoDebug.dibujarRectanguloRelleno(g, 50, 50, 500, 500, Color.red);
                DibujoDebug.dibujarImagen(g,imagen, 0, 0);
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
				if (sd.obtenerRaton().obtenerRectanguloPosicion().intersects(secciones[i].obtenerEtiquetaMenuEscalada())) 
                                {
                                    secciones[i].dibujarEtiquetaInactivaResaltada(g);
				} else 
                                {
                                    secciones[i].dibujarEtiquetaInactiva(g);
				}
			}
		}

		//seccionActual.dibujar(g, sd, estructuraMenu);
	}
}