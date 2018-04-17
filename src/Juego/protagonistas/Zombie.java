package Juego.protagonistas;


import Juego.Constantes;
import Juego.herramientas.DibujoDebug;
import Juego.sonido.Sonido;
import Juego.sprites.HojaSprites;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Zombie extends Enemigo {

    private static HojaSprites hojaZombie;

    public Zombie(final int idEnemigo, final String nombre, final int vidaMaxima, final String rutaLamento) {
        super(idEnemigo, nombre, vidaMaxima, rutaLamento);

        if (hojaZombie == null) {
            hojaZombie = new HojaSprites(Constantes.RUTA_ENEMIGOS + 1 + ".png",
                    Constantes.LADO_SPRITE, false);
        }
    }

    @Override
    public void dibujar(final Graphics g, final int puntoX, final int puntoY,BufferedImage imagen) {
        
        
        double velocidad=0.25;
        if (animacion < 400) {
			animacion++;
		} else {
			animacion = 0;
		}

		if (animacion < 150) {
			estado = 3;
		} else if (animacion < 300) {
			estado = 7;
                }else{
                   estado = 11; 
                }
                
                if (movimientoDerecha == true) {
                    posicionX = posicionX + velocidad;
           
           DibujoDebug.dibujarImagen(g, hojaZombie.obtenerSprite(estado-1).obtenerImagen(), puntoX, puntoY);
        super.dibujar(g, puntoX, puntoY,hojaZombie.obtenerSprite(estado-1).obtenerImagen());
           if(posicionX > 500){
               movimientoDerecha=false;
           }
        }else{
          posicionX = posicionX - velocidad;
           DibujoDebug.dibujarImagen(g, hojaZombie.obtenerSprite(estado).obtenerImagen(), puntoX, puntoY);
        super.dibujar(g, puntoX, puntoY,hojaZombie.obtenerSprite(estado).obtenerImagen());
           if(posicionX < 400){
               movimientoDerecha=true;
           } 
        }
        enMovimiento = true;
 
        
    }
}