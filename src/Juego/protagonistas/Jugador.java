package Juego.protagonistas;

import java.awt.*;
import java.awt.image.BufferedImage;
import static java.lang.System.exit;
import java.util.ArrayList;

import Juego.Constantes;
import Juego.ElementosPrincipales;
import Juego.control.GestorControles;
import Juego.herramientas.CalculadoraDistancia;
import Juego.herramientas.DibujoDebug;
import Juego.inventario.RegistroObjetos;
import Juego.inventario.armas.Arma;
import Juego.inventario.armas.Desarmado;
import Juego.inventario.armas.disparo;
import Juego.maquinaestado.estados.juego.GestorJuego;
import Juego.sonido.Sonido;
import Juego.sprites.HojaSprites;

public class Jugador {
	
	private static double posicionX;
	private static double posicionY;

        //definimos una variable de tipo entera, para determinar la direccion en la que se mueve nuestro jugador.
	private int direccion;
        
        //definimos una variable de tipo doble, para determinar la velocidad a la que se moverá nuestro jugador.
	private double velocidad = 1;

        //definimos una variable boleana, para saber cuando el jugador se esta moviendo por el mapa.
	private boolean enMovimiento;
        //Creamos una variable de tipo hoja de sprites.
	private HojaSprites hs;
        //declaramos una variable de tipo bufferedimage, que es donde guardaremos nuestras imagenes de los sprites,y texturas.
	private BufferedImage imagenActual;

	private final int ANCHO_JUGADOR = 16;
	private final int ALTO_JUGADOR = 16;
        
        //creamos una variable de tipo rectangulo para definir el rectangulo de parte superior, que utilizaremos para ver las colisiones cuando 
        //el personaje se mueva hacia arriba.
	private final Rectangle LIMITE_ARRIBA = new Rectangle(Constantes.CENTRO_VENTANA_X - ANCHO_JUGADOR / 2,
			Constantes.CENTRO_VENTANA_Y, ANCHO_JUGADOR, 1);
        
        //creamos una variable de tipo rectangulo para definir el rectangulo de parte inferior, que utilizaremos para ver las colisiones cuando 
        //el personaje se mueva hacia abajo.
	private final Rectangle LIMITE_ABAJO = new Rectangle(Constantes.CENTRO_VENTANA_X - ANCHO_JUGADOR / 2,
			Constantes.CENTRO_VENTANA_Y + ALTO_JUGADOR, ANCHO_JUGADOR, 1);
        
        //creamos una variable de tipo rectangulo para definir el rectangulo de parte izquierda, que utilizaremos para ver las colisiones cuando 
        //el personaje se mueva hacia la izquierda.
	private final Rectangle LIMITE_IZQUIERDA = new Rectangle(Constantes.CENTRO_VENTANA_X - ANCHO_JUGADOR / 2,
			Constantes.CENTRO_VENTANA_Y, 1, ALTO_JUGADOR);
        //creamos una variable de tipo rectangulo para definir el rectangulo de parte derecha, que utilizaremos para ver las colisiones cuando 
        //el personaje se mueva hacia la derecha.
	private final Rectangle LIMITE_DERECHA = new Rectangle(Constantes.CENTRO_VENTANA_X + ANCHO_JUGADOR / 2,
			Constantes.CENTRO_VENTANA_Y, 1, ALTO_JUGADOR);
        
        //definimos una variable entera para calcular la cantidad de animacines.
	private int animacion;
        //definimos una variable que guardará el estado del sprite.
	private int estado;

	public static final int RESISTENCIA_TOTAL = 600;
	private int resistencia = 600;
	private int recuperacion = 0;
	private final int RECUPERACION_MAXIMA = 300;
	private boolean recuperado = true;

	public int limitePeso = 70;
	public int pesoActual = 30;
        private static int vidaMaxima = 600;
        private static float vidaActual = 600;

        //creamos una variable de tipo almacen equipo.
	private AlmacenEquipo ae;
        
        
        private ArrayList<Rectangle> alcanceActual;
    
        public int puntos = 0;
        public static boolean cambiarArma=false;
        private disparo disp;
       
        private int centroY;
    

	public  Jugador(final int vidaMaxima) {
            
                this.vidaMaxima = vidaMaxima;
                this.vidaActual = vidaMaxima;
                
                //caputramos en la variable PosicionX , la coordenada inicial del mapa,
                //en este juego lo que se mueve es el mapa realmente, no el jugador, por ello accedemos a la 
                //instancia de mapa para capturar su posicion.El jugador se va a mover en relacion a estas coordenadas.
		posicionX = ElementosPrincipales.mapa.obtenerPosicionInicial().getX();
		posicionY = ElementosPrincipales.mapa.obtenerPosicionInicial().getY();
                
                //inicializamos la variable direccion
		direccion = 0;
                //establecemos que el movimiento al comienzo del juego es falso, el jugador esta quieto
                //hasta que lo mueve el usuario.
		enMovimiento = false;

                
		hs = new HojaSprites(Constantes.RUTA_PERSONAJE, Constantes.LADO_SPRITE, false);

		imagenActual = hs.obtenerSprite(0).obtenerImagen();

		animacion = 0;
		estado = 0;

                //public AlmacenEquipo(final Arma arma1)
		ae = new AlmacenEquipo((Arma) RegistroObjetos.obtenerObjeto(599));

                alcanceActual = new ArrayList<>();
	}

	public void actualizar() {
            
            
                cambiarHojaSprites(); 
		gestionarVelocidadResistencia();
		cambiarAnimacionEstado();
		enMovimiento = false;
		determinarDireccion();
		animar();
                actualizarArmas();
                actualizarDisparo();
                //System.out.println(ae.DimeArmaEquipada().obtenerId());
                }
        
      private void actualizarDisparo() {
        if (GestorControles.teclado.atacando) {

            //creamos una instancia de la clase disparo.            
            disp = new disparo(0, 0);
            
            
            
            //System.out.println("ataque correcto");
            
        }

        //Si el disparo esta vivo, lo movemos. Si no, lo ponemos a null 
        //para poder volver a disparar
        if (disp != null) {
            //si se ha efectuado un dispero y la bala sigue viva, muere al alcanzar la suma de 300 en su coordenada X o coordenada Y.
            if (disp.getVivo()) 
            {
                //llamamos al metodo actualiza de la clase disparo, para actualizar la posicion de la bala.
                disp.actualiza();
            } else {
                //si el disparo ha muerto , eliminamos el objeto disparo.
                disp = null;
            }
        }
    }
        
    private void actualizarArmas() {
        if (ae.DimeArmaEquipada() instanceof  Desarmado) {
            return;
            
        }
        //System.out.println("jugador armado");
        calcularAlcanceAtaque();
        ae.DimeArmaEquipada().actualizar();
    }

    private void calcularAlcanceAtaque() {
        alcanceActual = ae.DimeArmaEquipada().obtenerAlcance(this);
    }

    public void cambiarHojaSprites() {
        
       if (!cambiarArma){
            
         if (ae.DimeArmaEquipada() instanceof  Arma && !(ae.DimeArmaEquipada() instanceof Desarmado)) {
             
             if(ae.DimeArmaEquipada().obtenerId() == 500){
                 
               hs = new HojaSprites(Constantes.RUTA_PERSONAJE_PISTOLA, Constantes.LADO_SPRITE, false);
               cambiarArma = true;
               //System.out.println("cambiaste de arma a pistola");
             } 
             if (ae.DimeArmaEquipada().obtenerId() == 501){
                 hs = new HojaSprites(Constantes.RUTA_PERSONAJE_ESCOPETA, Constantes.LADO_SPRITE, false);
               
                //System.out.println("cambiaste de arma a escopeta");
                cambiarArma = true;
             } 
           
           }
        
        }
    }
	private void gestionarVelocidadResistencia() {
		if (GestorControles.teclado.corriendo && resistencia > 0) {
			velocidad = 2;
			recuperado = false;
			recuperacion = 0;
		} else {
			velocidad = 1;
			if (!recuperado && recuperacion < RECUPERACION_MAXIMA) {
				recuperacion++;
			}
			if (recuperacion == RECUPERACION_MAXIMA && resistencia < 600) {
				resistencia++;
			}
		}
	}

	private void cambiarAnimacionEstado() {
		if (animacion < 60) {
			animacion++;
		} else {
			animacion = 0;
		}

		if (animacion < 10) {
			estado = 1;
		} else if (animacion < 15) {
			estado = 2;
		} else if (animacion < 25){
                    estado = 3;
                }else if (animacion < 30){
                   estado = 4; 
                }else if (animacion < 40){
                   estado = 5; 
                }else if (animacion < 50){
                   estado = 6; 
                }else{
                   estado = 7; 
                }
	}

	private void determinarDireccion() {
		final int velocidadX = evaluarVelocidadX();
		final int velocidadY = evaluarVelocidadY();

		if (velocidadX == 0 && velocidadY == 0) {
			return;
		}

		if ((velocidadX != 0 && velocidadY == 0) || (velocidadX == 0 && velocidadY != 0)) {
			mover(velocidadX, velocidadY);
		} else {
			// izquierda y arriba
			if (velocidadX == -1 && velocidadY == -1) {
				if (GestorControles.teclado.izquierda.obtenerUltimaPulsacion() > GestorControles.teclado.arriba
						.obtenerUltimaPulsacion()) {
					mover(velocidadX, 0);
				} else {
					mover(0, velocidadY);
				}
			}
			// izquierda y abajo
			if (velocidadX == -1 && velocidadY == 1) {
				if (GestorControles.teclado.izquierda.obtenerUltimaPulsacion() > GestorControles.teclado.abajo
						.obtenerUltimaPulsacion()) {
					mover(velocidadX, 0);
				} else {
					mover(0, velocidadY);
				}
			}
			// derecha y arriba
			if (velocidadX == 1 && velocidadY == -1) {
				if (GestorControles.teclado.derecha.obtenerUltimaPulsacion() > GestorControles.teclado.arriba
						.obtenerUltimaPulsacion()) {
					mover(velocidadX, 0);
				} else {
					mover(0, velocidadY);
				}
			}
			// derecha y abajo
			if (velocidadX == 1 && velocidadY == 1) {
				if (GestorControles.teclado.derecha.obtenerUltimaPulsacion() > GestorControles.teclado.abajo
						.obtenerUltimaPulsacion()) {
					mover(velocidadX, 0);
				} else {
					mover(0, velocidadY);
				}
			}
		}
	}

	private int evaluarVelocidadX() {
		int velocidadX = 0;

		if (GestorControles.teclado.izquierda.estaPulsada() && !GestorControles.teclado.derecha.estaPulsada()) {
			velocidadX = -1;
		} else if (GestorControles.teclado.derecha.estaPulsada() && !GestorControles.teclado.izquierda.estaPulsada()) {
			velocidadX = 1;
		}

		return velocidadX;
	}

	private int evaluarVelocidadY() {
		int velocidadY = 0;

		if (GestorControles.teclado.arriba.estaPulsada() && !GestorControles.teclado.abajo.estaPulsada()) {
			velocidadY = -1;
		} else if (GestorControles.teclado.abajo.estaPulsada() && !GestorControles.teclado.arriba.estaPulsada()) {
			velocidadY = 1;
		}

		return velocidadY;
	}

	private void mover(final int velocidadX, final int velocidadY) {
		enMovimiento = true;

		cambiarDireccion(velocidadX, velocidadY);

		if (!fueraMapa(velocidadX, velocidadY)) {
			if (velocidadX == -1 && !enColisionIzquierda(velocidadX)) {
				posicionX += velocidadX * velocidad;
				restarResistencia();
			}
			if (velocidadX == 1 && !enColisionDerecha(velocidadX)) {
				posicionX += velocidadX * velocidad;
				restarResistencia();
			}
			if (velocidadY == -1 && !enColisionArriba(velocidadY)) {
				posicionY += velocidadY * velocidad;
				restarResistencia();
			}
			if (velocidadY == 1 && !enColisionAbajo(velocidadY)) {
				posicionY += velocidadY * velocidad;
				restarResistencia();
			}
		}
	}

	private void restarResistencia() {
		if (GestorControles.teclado.corriendo && resistencia > 0) {
			resistencia--;
		}
	}

	private boolean enColisionArriba(int velocidadY) {
            
            if (GestorJuego.Mapa1Activado){
                
                for (int r = 0; r < GestorJuego.uno.areasColisionPorActualizacion.size(); r++) {
			final Rectangle area = GestorJuego.uno.areasColisionPorActualizacion.get(r);

			int origenX = area.x;
			int origenY = area.y + velocidadY * (int) velocidad + 3 * (int) velocidad;

			final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width,
					area.height);

			if (LIMITE_ARRIBA.intersects(areaFutura)) {
                            //System.out.println("has chocado por arriba");
				return true;
                                
			}
		}

		return false;
                
            }
            
              if (GestorJuego.Mapa2Activado){
                
                for (int r = 0; r < GestorJuego.dos.areasColisionPorActualizacion.size(); r++) {
			final Rectangle area = GestorJuego.dos.areasColisionPorActualizacion.get(r);

			int origenX = area.x;
			int origenY = area.y + velocidadY * (int) velocidad + 3 * (int) velocidad;

			final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width,
					area.height);

			if (LIMITE_ARRIBA.intersects(areaFutura)) {
                            //System.out.println("has chocado por arriba en mapa 2");
				return true;
                                
			}
		}

		return false;
                
            }
		
           return false;     
	}
        
        
        
        

	private boolean enColisionAbajo(int velocidadY) {
            
             if (GestorJuego.Mapa1Activado){
		for (int r = 0; r < ElementosPrincipales.mapa.areasColisionPorActualizacion.size(); r++) {
			final Rectangle area = ElementosPrincipales.mapa.areasColisionPorActualizacion.get(r);

			int origenX = area.x;
			int origenY = area.y + velocidadY * (int) velocidad - 3 * (int) velocidad;

			final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width,
					area.height);

			if (LIMITE_ABAJO.intersects(areaFutura)) {
				return true;
			}
		}
              }  
                
                
                if (GestorJuego.Mapa2Activado){
                    
                  for (int r = 0; r < GestorJuego.dos.areasColisionPorActualizacion.size(); r++) {
			final Rectangle area = GestorJuego.dos.areasColisionPorActualizacion.get(r);

			int origenX = area.x;
			int origenY = area.y + velocidadY * (int) velocidad - 3 * (int) velocidad;

			final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width,
					area.height);

			if (LIMITE_ABAJO.intersects(areaFutura)) {
				return true;
			}
		}  
                    
                } 
     
		return false;
	}

	private boolean enColisionIzquierda(int velocidadX) {
            
            if (GestorJuego.Mapa1Activado){
                
                for (int r = 0; r < ElementosPrincipales.mapa.areasColisionPorActualizacion.size(); r++) {
			final Rectangle area = ElementosPrincipales.mapa.areasColisionPorActualizacion.get(r);

			int origenX = area.x + velocidadX * (int) velocidad + 3 * (int) velocidad;
			int origenY = area.y;

			final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width,
					area.height);

			if (LIMITE_IZQUIERDA.intersects(areaFutura)) {
				return true;
			}
		}
                
            }
		
                
              if (GestorJuego.Mapa2Activado){
                 for (int r = 0; r <  GestorJuego.dos.areasColisionPorActualizacion.size(); r++) {
			final Rectangle area =  GestorJuego.dos.areasColisionPorActualizacion.get(r);

			int origenX = area.x + velocidadX * (int) velocidad + 3 * (int) velocidad;
			int origenY = area.y;

			final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width,
					area.height);

			if (LIMITE_IZQUIERDA.intersects(areaFutura)) {
				return true;
			}
		} 
                  
              }  
                
                

		return false;
	}

	private boolean enColisionDerecha(int velocidadX) 
        {
            
              if (GestorJuego.Mapa1Activado){
                  
                  for (int r = 0; r < ElementosPrincipales.mapa.areasColisionPorActualizacion.size(); r++) {
			final Rectangle area = ElementosPrincipales.mapa.areasColisionPorActualizacion.get(r);

			int origenX = area.x + velocidadX * (int) velocidad - 3 * (int) velocidad;
			int origenY = area.y;

			final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width,
					area.height);

			if (LIMITE_DERECHA.intersects(areaFutura)) {
				return true;
			}
		}
                  
              }
		
                
                
                if (GestorJuego.Mapa2Activado){
                    
                   for (int r = 0; r < GestorJuego.dos.areasColisionPorActualizacion.size(); r++) {
			final Rectangle area = GestorJuego.dos.areasColisionPorActualizacion.get(r);

			int origenX = area.x + velocidadX * (int) velocidad - 3 * (int) velocidad;
			int origenY = area.y;

			final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width,
					area.height);

			if (LIMITE_DERECHA.intersects(areaFutura)) {
				return true;
			}
		}
                   
                }

		return false;
	}

	private boolean fueraMapa(final int velocidadX, final int velocidadY) 
        {

		int posicionFuturaX = (int) posicionX + velocidadX * (int) velocidad;
		int posicionFuturaY = (int) posicionY + velocidadY * (int) velocidad;

		final Rectangle bordesMapa = ElementosPrincipales.mapa.obtenerBordes(posicionFuturaX, posicionFuturaY);

		final boolean fuera;

		if (LIMITE_ARRIBA.intersects(bordesMapa) || LIMITE_ABAJO.intersects(bordesMapa)
				|| LIMITE_IZQUIERDA.intersects(bordesMapa) || LIMITE_DERECHA.intersects(bordesMapa)) {
			fuera = false;
		} else {
			fuera = true;
		}

		return fuera;
	}

	private void cambiarDireccion(final int velocidadX, final int velocidadY) 
        {
		if (velocidadX == -1) {
			direccion = 3;
		} else if (velocidadX == 1) {
			direccion = 2;
		}

		if (velocidadY == -1) {
			direccion = 1;
		} else if (velocidadY == 1) {
			direccion = 0;
		}
	}

	private void animar() 
        {
		if (!enMovimiento) 
                {
			estado = 0;
			animacion = 0;
		}

		imagenActual = hs.obtenerSprite(direccion, estado).obtenerImagen();
	}

	public void dibujar(Graphics g) 
        {
            if (vidaActual <= 0) {
                //System.exit(0);
                
                //cuando el jugador se queda sin vida, debemos llamar a menu game over
                
                //fin de estado game over
              
            DibujoDebug.dibujarString(g, "game over", 200, 200);
            return;
            
        }
            
        final int centroX = Constantes.ANCHO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;
	final int centroY = Constantes.ALTO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;

	DibujoDebug.dibujarImagen(g, imagenActual, centroX, centroY);
           
        
        //!!!dibujamos la bala!!
        //comprobamos si se ha efectuado un disparo, es decir si se ha creado una instnacia de la clase disparo.
        if (disp != null) {
                //comprobamos si el jugador esta desarmado
                if (ae.DimeArmaEquipada() instanceof Desarmado) {
                    //AQUI Tengo que implementar que el jugador pueda defenderse con las manos, puñetazos.
                    //System.out.println("jugador desarmado");
                    //si el jugador en caso contrario , si que eta armado, ejecutamos el codigo.
                } else {
                            //si el jugador esta equipado con el arma pistola
                    if (ae.DimeArmaEquipada().obtenerId() == 500 && ae.DimeArmaEquipada().DimeCantidadDisparos() < 5) {

                        //si la direccion es derecha o izquierda
                        if (this.obtenerDireccion() == 2 || this.obtenerDireccion() == 3) {

                            DibujoDebug.dibujarRectanguloContorno(g, disp.getvalorX() + 20, centroY + 15, 3, 1, Color.BLACK);
                            
                            //en caso contrario , si la direccion es arriba o abajo.
                        } else {
                            DibujoDebug.dibujarRectanguloContorno(g, centroX + 20, disp.getvalorY() + 15, 1, 3, Color.BLACK);
                            
                        }

                    }

                    if (ae.DimeArmaEquipada().obtenerId() == 501) {

                        //si la direccion es derecha o izquierda
                        if (this.obtenerDireccion() == 2 || this.obtenerDireccion() == 3) {
                            DibujoDebug.dibujarRectanguloContorno(g, disp.getvalorX() + 1, centroY + 10, 10, 10, Color.BLACK);
                            DibujoDebug.dibujarRectanguloContorno(g, disp.getvalorX() - 5, centroY + 5, 5, 5, Color.red);
                            //en caso contrario , si la direccion es arriba o abajo.
                        } else {
                            DibujoDebug.dibujarRectanguloContorno(g, centroX + 10, disp.getvalorY() + 1, 10, 10, Color.BLACK);
                            DibujoDebug.dibujarRectanguloContorno(g, disp.getvalorX() - 5, centroY + 5, 5, 5, Color.red);
                        }

                    }

                }

                //
            }
        
        //DIBUJAR BARRA RESISTENCIA JUGADOR
        dibujarBarraVida(g, centroX, centroY, (int) vidaActual,vidaMaxima);
        //DibujoDebug.dibujarRectanguloContorno(g, obtenerArea());
        dibujarDistancia(g, centroX, centroY-8);
        Point puntoJugador = new Point(
        (int) ElementosPrincipales.jugador.dimePosicionX(),
        (int) ElementosPrincipales.jugador.dimePosicionY()
        );
   
        /*if (!alcanceActual.isEmpty()) 
        {
            g.setColor(Color.red);
            dibujarAlcance(g);
        }*/
        
        DibujoDebug.dibujarString(g, posicionX + "-" + posicionY,  centroX, centroY - 8);
	}
        public Rectangle obtenerArea() {
        final int puntoX = (int) posicionX - (int) ElementosPrincipales.jugador.dimePosicionX() + Constantes.MARGEN_X;
        final int puntoY = (int) posicionY - (int) ElementosPrincipales.jugador.dimePosicionY() + Constantes.MARGEN_Y;

        return new Rectangle(puntoX, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
        }
        
        public static void dibujarBarraVida(final Graphics g, final int puntoX, final int puntoY,int vidaActual,int vidaMaxima ) 
        {
        g.setColor(Color.red);
        DibujoDebug.dibujarRectanguloRelleno(g, puntoX, puntoY - 5, Constantes.LADO_SPRITE *  vidaActual / vidaMaxima, 2);
        
        
        int PuntoXenemigo = (int) Enemigo.EnemigoObtenerPosicionX();
        int PuntoYenemigo = (int)Enemigo.EnemigoObtenerPosiciony();
        
        //Enemigo.obtenerDistancia(PuntoXenemigo, PuntoYenemigo);
        //System.out.println(Enemigo.obtenerDistancia(PuntoXenemigo, PuntoYenemigo));
        
        }
        
    public void dibujarDistancia(final Graphics g, final int puntoX, final int puntoY) {
       
    	Point puntoJugador = new Point(
                (int) ElementosPrincipales.jugador.dimePosicionX(),
                (int) ElementosPrincipales.jugador.dimePosicionY()
        );
	
        Point puntoEnemigo = new Point((int) posicionX, (int) posicionY);
        
        Double distancia = CalculadoraDistancia.obtenerDistanciaEntrePuntos(puntoJugador, puntoEnemigo);

        DibujoDebug.dibujarString(g, String.format("%.2f" ,distancia), puntoX, puntoY - 8);
    }
    public static float  dimeVidaActual() {
        return vidaActual;
    }   
    
    public static float  obtenerVidaMaxima() {
        return vidaMaxima;
    }
        

    private void dibujarAlcance(final Graphics g) {
        DibujoDebug.dibujarRectanguloRelleno(g, alcanceActual.get(0));
    }

	public void establecerPosicionX(double posicionX) {
		this.posicionX = posicionX;
	}

	public void establecerPosicionY(double posicionY) {
		this.posicionY = posicionY;
	}

	public static double dimePosicionX() {
		return posicionX;
	}

	public static double dimePosicionY() {
		return posicionY;
	}

	public int dimePosicionXInt() {
		return (int) posicionX;
	}

	public int dimePosicionYInt() {
		return (int) posicionY;
	}

	public int dimeAncho() {
		return ANCHO_JUGADOR;
	}

	public int obtenerAlto() {
		return ALTO_JUGADOR;
	}

	public int obtenerResistencia() {
		return resistencia;
	}

	public Rectangle obtener_LIMITE_ARRIBA() {
		return LIMITE_ARRIBA;
	}

    public AlmacenEquipo obtenerAlmacenEquipo() {
        return ae;
    }

    public int obtenerDireccion() {
        return direccion;
    }
    
    public Point obtenerPosicion() {
    	return new Point((int)posicionX, (int)posicionY);
    }
    
    public ArrayList<Rectangle> obtenerAlcanceActual() {
    	return  alcanceActual;
    }
    
    public static void perderVida(float ataqueRecibido) {
    	/*if (lamentoSiguiente <= 0) {
    		lamento.reproducir();
    		lamentoSiguiente = duracionLamento;
    	}*/
    	
        if (vidaActual - ataqueRecibido < 0) {
            vidaActual = 0;
         
        } else {
            vidaActual -= ataqueRecibido;
        }
        
        //if (ElementosPrincipales.jugador.obtenerAlcanceActual().get(0).intersects(enemigo.obtenerArea())) {
                        //System.out.println("impacto");
                    	//enemigosAlcanzados.add(enemigo);
                   // }
       // ElementosPrincipales.jugador.
    }
    
   
    
}