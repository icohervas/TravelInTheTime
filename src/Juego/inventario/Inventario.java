package Juego.inventario;

import java.util.ArrayList;

import Juego.inventario.armas.Arma;
import Juego.inventario.consumibles.Consumible;

public class Inventario {

	public final ArrayList<Objeto> objetos;

	public Inventario() {
            //creamos un arrylist de objetos , donde vamos a ir guardando todos los objtos que recogemos 
            //por el mapa.En este contenedor estan incluidos todos los tipos de objetos en el mismo almacen llamado objeto.s
		objetos = new ArrayList<Objeto>();
	}
        

	public void recogerObjetos(final ContenedorObjetos co) {
		for (Objeto objeto : co.obtenerObjetos()) {
			if (objetoExiste(objeto)) {
				incrementarObjeto(objeto, objeto.obtenerCantidad());
			} else {
				objetos.add(objeto);
			}
		}
	}
	
	public void recogerObjetos(final ObjetoUnicoTiled objetoUnico) {
		if (objetoExiste(objetoUnico.obtenerObjeto())) {
			incrementarObjeto(objetoUnico.obtenerObjeto(), objetoUnico.obtenerObjeto().obtenerCantidad());
		} else {
			objetos.add(objetoUnico.obtenerObjeto());
		}
	}

	public boolean incrementarObjeto(final Objeto objeto, final int cantidad) {
		boolean incrementado = false;

		for (Objeto objetoActual : objetos) {
			if (objetoActual.obtenerId() == objeto.obtenerId()) {
				objetoActual.incrementarCantidad(cantidad);
				incrementado = true;
				break;
			}
		}

		return incrementado;
	}

	public boolean objetoExiste(final Objeto objeto) {
		boolean existe = false;

		for (Objeto objetoActual : objetos) {
			if (objetoActual.obtenerId() == objeto.obtenerId()) {
				existe = true;
				break;
			}
		}

		return existe;
	}

        //metodo que crear un arraylist de consumibles, comprueba si el objeto que hemos recogido es una instancia de consumible
        //y lo garda en el arraylist de consumibles, de esta forma este arraylist solo tiene objetos consumibles.
	public ArrayList<Objeto> obtenerConsumibles() {
		ArrayList<Objeto> consumibles = new ArrayList<>();
                    
		for (Objeto objeto : objetos) {
                    //Con esta sentencia evaluamos si objeto pertenece a un objeto de la clase consumible.
                    //aqui se incluyen todo tipo de consumibles, porque hace referencia a la super clase consumible, (incluye frutas,pociones,etc).
			if (objeto instanceof Consumible) {
				consumibles.add(objeto);
			}
		}
                //devolvemos un arraylist con todas los consumibles que hemos recogido.
		return consumibles;
	}

        //metodo que crear un arraylist de armas, comprueba si el objeto que hemos recogido es una instancia de arma
        //y lo garda en el arraylist de armas, de esta forma este arraylist solo tiene armas.
	public ArrayList<Objeto> obtenerArmas() {
		ArrayList<Objeto> armas = new ArrayList<>();

		for (Objeto objeto : objetos) {
                    //Con esta sentencia evaluamos si objeto pertenece a un objeto de la clase arma.
                    //aqui se incluyen todo tipo de armas, porque hace referencia a la super clase arma, (incluye pistolas,escopetas,granadas,etc).
			if (objeto instanceof Arma) {
				armas.add(objeto);
			}
		}
                //devolvemos un arraylist con todas las armas que hemos recogido.
		return armas;
	}
        
       
//para poder obtener el objeto exacto que deseemos tanto de consumibles,armas, etc,
 //necesitamos usar este metodo para acceder al identificado del objeto y asi capturar el objeto actual de forma correcta.
	public Objeto obtenerObjeto(final int id) {
		for (Objeto objetoActual : objetos) {
			if (objetoActual.obtenerId() == id) {
				return objetoActual;
			}
		}

		return null;
	}
}