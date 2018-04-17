package Juego.inventario;

import Juego.inventario.armas.Desarmado;
import Juego.inventario.armas.Escopeta;
import Juego.inventario.armas.Pistola;
import Juego.inventario.consumibles.Consumible;

public class RegistroObjetos {

	public static Objeto obtenerObjeto(final int idObjeto) {

		Objeto objeto = null;

		switch (idObjeto) {
		// 0-499: objetos consumibles
		case 0:
			objeto = new Consumible(idObjeto, "Manzana roja", "");
			break;
		case 1:
			objeto = new Consumible(idObjeto, "Manzana amarilla", "");
			break;
		case 2:
			objeto = new Consumible(idObjeto, "Zanahoria", "");
			break;
		case 3:
			objeto = new Consumible(idObjeto, "Galleta", "");
			break;
		case 4:
			objeto = new Consumible(idObjeto, "Limón rojizo", "");
			break;
		case 5:
			objeto = new Consumible(idObjeto, "Fruta verde", "");
			break;
		// 500 - 599: armas
		case 500:
			objeto = new Pistola(idObjeto, "Default Black", "", 1, 3, false, true, 0.4);
			break;
                        
                case 501:
			objeto = new Escopeta(idObjeto, "Escopeta Recortada", "", 1, 3, false, true, 0.4);
			break;
                /*case 502:
			objeto = new Granada(idObjeto, "Granada", "", 1, 3, false, true, 0.4);
			break;    */  
                        
		case 599:
			objeto = new Desarmado(idObjeto, "Desarmado", "", 0, 0);
			break;
		}

		return objeto;

	}
}