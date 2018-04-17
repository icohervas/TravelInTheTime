/*
 * Clase que efectua un disparo cuando el jugador esta armado y pulsa la tecla espacio.
 */
package Juego.inventario.armas;

import Juego.Constantes;
import Juego.ElementosPrincipales;

/**
 *
 * @author FCO JAVIER HERVAS CALLE
 */
public class disparo {

    
    //Creamos 2 variables que definiran las coordenadas del disparo.
    private int X = 0; //posición del disparo en la coordenadaX
    private int Y;//posición del disparo en la coordenadaY

    //private Texture2D textura; //textura del disparo 
    private boolean vivo; //indica si el disparo aun esta en el juego
    private int valorX;
    private int valorY;

    public disparo(int X, int Y /*Texture2D sprite*/) {
        this.X = X;
        this.Y = Y;
        //textura = sprite; 
        vivo = true;

    }

    public void actualiza() {
        final int centroX = Constantes.ANCHO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;
        final int centroY = Constantes.ALTO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;

       //System.out.println(ElementosPrincipales.jugador.obtenerDireccion());

        if (ElementosPrincipales.jugador.obtenerDireccion() == 2) {//si el personaje se mueve a la derecha

            setX(getX() + 20);//marcamos la velocidad de la vala a 20 avances de la coordenada X.
            if (this.X == 300) {
                setX(getX() - getX());
                this.vivo = false;
            }
            valorX = getX() + centroX;
        //System.out.println("ataque actualiza");
       /* System.out.println("ataque actualizax  " + getX());
             System.out.println("ataque actualizay " + getY());
        
             System.out.println("ataque centroX  " + centroX);
             System.out.println("ataque centroY " + centroY);
        
             System.out.println("sume centrox y x" + valor);*/

        } else if (ElementosPrincipales.jugador.obtenerDireccion() == 3) {//si el personaje se mueve a la izquierda
            setX(getX() + 20);//marcamos la velocidad de la vala a 20 avances de la coordenada X en negativo.
            if (this.X == 300) {
                setX(getX() - getX());
                this.vivo = false;
            }
            valorX = centroX - getX();
        } else if (ElementosPrincipales.jugador.obtenerDireccion() == 0) {//si el personaje se mueve hacia ABAJO

            setY(getY() + 20);//marcamos la velocidad de la vala a 20 avances de la coordenada y.
            
            if (this.Y == 300) {
                setY(getY() - getY());
                this.vivo = false;
            }
            valorY = getY() + centroY;
            //System.out.println("valor valor " + valorY);
        } else if (ElementosPrincipales.jugador.obtenerDireccion() == 1) {//si el personaje se mueve hacia ARRIBA
            setY(getY() + 20);//marcamos la velocidad de la vala a 20 avances de la coordenada y en negativo.
            if (this.Y == 300) {
                setX(getY() - getY());
                this.vivo = false;
            }
            valorY = centroY - getY();
        }

    }

    /**
     * @return the X
     */
    public int getX() {
        return X;
    }

    /**
     * @param X the X to set
     */
    public void setX(int X) {
        this.X = X;
    }

    /**
     * @return the Y
     */
    public int getY() {
        return Y;
    }

    /**
     * @param Y the Y to set
     */
    public void setY(int Y) {
        this.Y = Y;
    }

    public int getvalorX() {
        return valorX;
    }
    public int getvalorY() {
        return valorY;
    }

    public boolean getVivo() {
        return vivo;
    }

    /**
     * @param valor
     */
    public void setvalorX(int valorX) {
        this.valorX = valorX;
    }
    
    public void setvalorY(int valorY) {
        this.valorY = valorY;
    }

}
