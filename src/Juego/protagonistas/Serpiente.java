package Juego.protagonistas;


import Juego.Constantes;
import Juego.herramientas.DibujoDebug;
import Juego.sonido.Sonido;
import Juego.sprites.HojaSprites;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Serpiente extends Enemigo {

    private static HojaSprites hojaSerpiente;
    private int animacion;
    private int estado;
    private boolean enMovimiento;
    private BufferedImage imagenActual;
    private int direccion;
    private HojaSprites hs;
        

    public Serpiente(final int idEnemigo, final String nombre, final int vidaMaxima, final String rutaLamento) {
        super(idEnemigo, nombre, vidaMaxima, rutaLamento);

        if (hojaSerpiente == null) {
            hojaSerpiente = new HojaSprites(Constantes.RUTA_ENEMIGOS + 2 + ".png",
                    Constantes.LADO_SPRITE, false);
        }
        
       
    }

    /**
     *
     * @param g
     * @param puntoX
     * @param puntoY
     * @param imagen
     */
    @Override
    public void dibujar(final Graphics g, final int puntoX, final int puntoY,BufferedImage imagen) {
    
       DibujoDebug.dibujarImagen(g, hojaSerpiente.obtenerSprite(estado).obtenerImagen(), puntoX, puntoY);
       super.dibujar(g, puntoX, puntoY,hojaSerpiente.obtenerSprite(estado).obtenerImagen());
        
        //System.out.println("estadoserpiente" + Serpiente.obtenerEstado());
        //System.out.println("animacion" + Serpiente.obtenerAnimacion());
    }
 
    
   
    
 
    
}