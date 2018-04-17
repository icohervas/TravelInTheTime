package Juego.maquinaestado.estados.juego;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Juego.Constantes;
import Juego.ElementosPrincipales;
import Juego.herramientas.CargadorRecursos;
import Juego.herramientas.DatosDebug;
import Juego.interfaz_usuario.MenuInferior;
import Juego.mapas.MapaTiled;
import Juego.maquinaestado.EstadoJuego;

public class GestorJuego implements EstadoJuego {
    public static boolean Mapa1Activado=true;
    public static boolean Mapa2Activado=false;

	BufferedImage logo;
	MenuInferior menuInferior;
        public static MapaTiled uno = ElementosPrincipales.mapa;
        public static MapaTiled dos = ElementosPrincipales.mapa2;
        private MapaTiled cambiamapa;
        private Rectangle zonaSalida;
        private int puntoX;
        private int puntoY;
    private Graphics g;
  
	public GestorJuego() {
		menuInferior = new MenuInferior();
		logo = CargadorRecursos.cargarImagenCompatibleTranslucida(Constantes.RUTA_LOGOTIPO);
	}

        @Override
	public void actualizar() {

		ElementosPrincipales.jugador.actualizar();
		//ementosPrincipales.mapa.actualizar();
                
                
                if (Mapa1Activado){
                     uno.actualizar();
                     //System.out.println("actualizando mapa numero uno: ");
                 }else if (Mapa2Activado){
                    dos.actualizar();
                    //System.out.println("actualizando mapa numero dos: ");
                 }
        
               // ElementosPrincipales.mapa.actualizar();
   /*      final Rectangle areaJugador = new Rectangle(ElementosPrincipales.jugador.obtenerPosicionXInt(),
                    ElementosPrincipales.jugador.obtenerPosicionYInt(), Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
           puntoX = 680
					- (int) ElementosPrincipales.jugador.obtenerPosicionX() + Constantes.MARGEN_X;
			puntoY = 680
					- (int) ElementosPrincipales.jugador.obtenerPosicionY() + Constantes.MARGEN_Y;
                        zonaSalida = new Rectangle(puntoX,
                        puntoY, Constantes.LADO_SPRITE,
                        Constantes.LADO_SPRITE); */
                        
                   //   if (areaJugador.intersects(zonaSalida) ) {
                   //ystem.out.println("se ha interceptado el rectangulo zona salida");
        //          GestorJuego.establecerMapa();
                    
             // }
	}

        @Override
	public void dibujar(Graphics g) {
           // if (ElementosPrincipales.jugador.obtener_LIMITE_ARRIBA().intersects(MapaTiled.obtenerSalida())){
            
                 GestorJuego.uno =GestorJuego.dimeMapa();
                 GestorJuego.dos =GestorJuego.dimeMapa();
                 //DibujoDebug.dibujarRectanguloRelleno(g, 380, 3, 32, 32, Color.red);
                 
                 if (Mapa1Activado){
                     GestorJuego.uno.dibujar(g);
                     //System.out.println("Mapa1Activado: " + Mapa1Activado);
                 }else if (Mapa2Activado){
                    GestorJuego.dos.dibujar(g);
                     //System.out.println("Mapa2Activado: " + Mapa2Activado);
                 }
                //dibujamos un rectangulo relleno alrededor del personaje, que siempre esta situado en el centro de la pantalla
		//DibujoDebug.dibujarRectanguloRelleno(g, puntoX,puntoY,32,32, Color.red);
                //dibujamos al jugador
		ElementosPrincipales.jugador.dibujar(g);
                //dibujamos el menu inferior
		menuInferior.dibujar(g);
               //DibujoDebug.dibujarRectanguloRelleno(g, 20, 20, 32, 32, Color.red);
                //dibujamos la imagen del logo en la parte superior de la pantalla
		//DibujoDebug.dibujarImagen(g, logo, 640 - logo.getWidth() - 5, 0 + 5);

		DatosDebug.enviarDato("X = " + ElementosPrincipales.jugador.dimePosicionXInt());
		DatosDebug.enviarDato("Y = " + ElementosPrincipales.jugador.dimePosicionYInt());
               // System.out.println(ElementosPrincipales.jugador.obtenerPosicionXInt());
               //System.out.println(ElementosPrincipales.jugador.obtenerPosicionYInt());                
        }
         public static void establecerMapa() {
             
             if (GestorJuego.uno == ElementosPrincipales.mapa2){
                 
                GestorJuego.uno = ElementosPrincipales.mapa;
                Mapa1Activado = true;
                Mapa2Activado = false;
             }else{
                 GestorJuego.uno = ElementosPrincipales.mapa2;
                 GestorJuego.dos = GestorJuego.uno;
                 Mapa2Activado = true;
                 Mapa1Activado = false;
             }
		
	}//DibujoDebug.dibujarRectanguloRelleno(g, uno.obtenerSalida(), Color.red)
         
         public static MapaTiled dimeMapa(){
             return GestorJuego.uno;
         }
         public static MapaTiled dimeMapa2(){
             return GestorJuego.dos;
         }
}