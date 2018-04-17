package Juego;

import Juego.interfaz_usuario.MenuInferior;
import Juego.protagonistas.Jugador;
import Juego.inventario.Inventario;
import Juego.mapas.MapaTiled;

public class ElementosPrincipales {
	
    //Variable estatica donde guardamos la ruta del mapa a utilizar.
	public static MapaTiled mapa = new MapaTiled(1,"bosque","/mapas/mapa1.json");
        public static MapaTiled mapa2 = new MapaTiled(2,"egipto","/mapas/desierto.json");

	//public static Mapa mapa = new Mapa(Constantes.RUTA_MAPA);
        
        
        // variable estatica donde realizamos una instancia de la clase jugador.
	public static Jugador jugador = new Jugador(600);
        // variable estatica donde realizamos una instancia de la clase inventario.
	public static Inventario inventario = new Inventario();
        
        public static MenuInferior menuInferior = new MenuInferior();

}