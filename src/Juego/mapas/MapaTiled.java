package Juego.mapas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import Juego.Constantes;
import Juego.ElementosPrincipales;
import Juego.control.GestorControles;
import Juego.inteligienciaArtificial.Dijkstra;
import Juego.protagonistas.Enemigo;
import Juego.protagonistas.RegistroEnemigos;
import Juego.herramientas.CalculadoraDistancia;
import Juego.herramientas.CargadorRecursos;
import Juego.herramientas.DibujoDebug;
import Juego.inventario.Objeto;
import Juego.inventario.ObjetoUnicoTiled;
import Juego.inventario.RegistroObjetos;
import Juego.inventario.armas.Desarmado;
import Juego.sprites.HojaSprites;
import Juego.sprites.Sprite;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import Juego.maquinaestado.estados.juego.GestorJuego;
import java.awt.Color;

public class MapaTiled implements Runnable{
    private static MapaTiled cambiamapa;
    private int anchoMapaEnTiles;
    private int altoMapaEnTiles;
    private Point puntoInicial;
    private ArrayList<CapaSprites> capasSprites;
    private ArrayList<CapaColisiones> capasColisiones;
    private ArrayList<Rectangle> areasColisionOriginales;
    public ArrayList<Rectangle> areasColisionPorActualizacion;
    private Sprite[] paletaSprites;
    private Dijkstra d;
    private ArrayList<ObjetoUnicoTiled> objetosMapa;
    private ArrayList<Enemigo> enemigosMapa;
    private  ArrayList<Enemigo> enemigosMapaSerpiente;   
    private static Rectangle zonaSalida;
    private final Point puntoSalida;
    private String siguienteMapa;
    private Rectangle zonaSalida2;
    private BufferedImage BufferedImage;
    private BufferedImage imagen;
    private final int centroX = Constantes.ANCHO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;
    private final int centroY = Constantes.ALTO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;       
    private int puntosalidaX;
    private Object objetoActual;
    private int puntosalidaY;
    public Rectangle areaJugador;
    private int id;
    private String nombre;
   

	public MapaTiled(int id, String nombre,final String ruta) {
            
            
		String contenido = CargadorRecursos.leerArchivoTexto(ruta);
                this.id=id;
                this.nombre=nombre;
                this.puntoSalida = new Point();
                puntoSalida.x = 4;  //Integer.parseInt(datosSalida[0]);
                puntoSalida.y = 5;    //Integer.parseInt(datosSalida[1]);
		
		//ANCHO, ALTO
		JSONObject globalJSON = obtenerObjetoJSON(contenido);
		this.anchoMapaEnTiles = obtenerIntDesdeJSON(globalJSON, "width");
		this.altoMapaEnTiles = obtenerIntDesdeJSON(globalJSON, "height");
		
		//PUNTO INICIAL
		JSONObject puntoInicial = obtenerObjetoJSON(globalJSON.get("start").toString());
		this.puntoInicial = new Point(obtenerIntDesdeJSON(puntoInicial, "x"), 
				obtenerIntDesdeJSON(puntoInicial, "y"));
		
		//CAPAS
		JSONArray capas = obtenerArrayJSON(globalJSON.get("layers").toString());
		
		this.capasSprites = new ArrayList<>();
		this.capasColisiones = new ArrayList<>();
		
		//INICIAR CAPAS
		for (int i = 0; i < capas.size(); i++) {
			JSONObject datosCapa = obtenerObjetoJSON(capas.get(i).toString());
			
			int anchoCapa = obtenerIntDesdeJSON(datosCapa, "width");
			int altoCapa = obtenerIntDesdeJSON(datosCapa, "height");
			int xCapa = obtenerIntDesdeJSON(datosCapa, "x");
			int yCapa = obtenerIntDesdeJSON(datosCapa, "y");
			String tipo = datosCapa.get("type").toString();
			
			switch(tipo) {
			case "tilelayer":
				JSONArray sprites = obtenerArrayJSON(datosCapa.get("data").toString());
				int[] spritesCapa = new int[sprites.size()];
				for (int j = 0; j< sprites.size(); j++) {
					int codigoSprite = Integer.parseInt(sprites.get(j).toString());
					spritesCapa[j] = codigoSprite - 1;
				}
				this.capasSprites.add(new CapaSprites(anchoCapa, altoCapa, xCapa, yCapa, spritesCapa));
				break;
			case "objectgroup":
				JSONArray rectangulos = obtenerArrayJSON(datosCapa.get("objects").toString());
				Rectangle[] rectangulosCapa = new Rectangle[rectangulos.size()];
				for (int j = 0; j < rectangulos.size(); j++) {
					JSONObject datosRectangulo = obtenerObjetoJSON(rectangulos.get(j).toString());
					
					int x = obtenerIntDesdeJSON(datosRectangulo, "x");
					int y = obtenerIntDesdeJSON(datosRectangulo, "y");
					int ancho = obtenerIntDesdeJSON(datosRectangulo, "width");
					int alto = obtenerIntDesdeJSON(datosRectangulo, "height");
					
					if (x == 0) x = 1;
					if (y == 0) y = 1;
					if (ancho == 0) ancho = 1;
					if (alto == 0) alto = 1;
					
					Rectangle rectangulo = new Rectangle(x, y, ancho, alto);
					rectangulosCapa[j] = rectangulo;
				}
				this.capasColisiones.add(new CapaColisiones(anchoCapa, altoCapa, xCapa, yCapa, rectangulosCapa));
				
				break;
			}
		}
		
		//COMBINAR COLISIONES EN UN SOLO ARRAYLIST POR EFICIENCIA
		areasColisionOriginales = new ArrayList<>();
		for (int i = 0; i < capasColisiones.size(); i++) {
			Rectangle[] rectangulos = capasColisiones.get(i).obtenerColisionables();
			
			for (int j = 0; j < rectangulos.length; j++) {
				areasColisionOriginales.add(rectangulos[j]);
			}
		}
		
		d = new Dijkstra(new Point(10, 10), anchoMapaEnTiles, altoMapaEnTiles, areasColisionOriginales);
		
		//AVERIGUAR TOTAL DE SPRITES EXISTENTES EN TODAS LAS CAPAS
		JSONArray coleccionesSprites = obtenerArrayJSON(globalJSON.get("tilesets").toString());
		int totalSprites = 0;
		for (int i = 0; i < coleccionesSprites.size(); i++) {
			JSONObject datosGrupo = obtenerObjetoJSON(coleccionesSprites.get(i).toString());
			totalSprites += obtenerIntDesdeJSON(datosGrupo, "tilecount");
		}
		paletaSprites = new Sprite[totalSprites];
		
		//ASIGNAR SPRITES NECESARIOS A LA PALETA A PARTIR DE LAS CAPAS
		for (int i = 0; i < coleccionesSprites.size(); i++) {
			JSONObject datosGrupo = obtenerObjetoJSON(coleccionesSprites.get(i).toString());
			
			String nombreImagen = datosGrupo.get("image").toString();
			int anchoTiles = obtenerIntDesdeJSON(datosGrupo, "tilewidth");
			int altoTiles = obtenerIntDesdeJSON(datosGrupo, "tileheight");
			HojaSprites hoja = new HojaSprites("/imagenes/hojasTexturas/" + nombreImagen,
					anchoTiles, altoTiles, false);
			
			int primerSpriteColeccion = obtenerIntDesdeJSON(datosGrupo, "firstgid") - 1;
			int ultimoSpriteColeccion = primerSpriteColeccion + obtenerIntDesdeJSON(datosGrupo, "tilecount") - 1;
			
			for (int j = 0; j < this.capasSprites.size(); j++) {
				CapaSprites capaActual = this.capasSprites.get(j);
				int[] spritesCapa = capaActual.obtenerArraySprites();
				
				for (int k = 0; k < spritesCapa.length; k++) {
					int idSpriteActual = spritesCapa[k];
					if (idSpriteActual >= primerSpriteColeccion && idSpriteActual <= ultimoSpriteColeccion) {
						if (paletaSprites[idSpriteActual] == null) {
							paletaSprites[idSpriteActual] = hoja.obtenerSprite(idSpriteActual - primerSpriteColeccion);
						}
					}
				}
			}	
		}
		
		//OBTENER OBJETOS
		objetosMapa = new ArrayList<>();
		JSONArray coleccionObjetos = obtenerArrayJSON(globalJSON.get("objetos").toString());
		for (int i = 0; i < coleccionObjetos.size(); i++) {
			JSONObject datosObjeto = obtenerObjetoJSON(coleccionObjetos.get(i).toString());
			
			int idObjeto = obtenerIntDesdeJSON(datosObjeto, "id");
			int cantidadObjeto = obtenerIntDesdeJSON(datosObjeto, "cantidad");
			int xObjeto = obtenerIntDesdeJSON(datosObjeto, "x");
			int yObjeto = obtenerIntDesdeJSON(datosObjeto, "y");
			
			Point posicionObjeto = new Point(xObjeto, yObjeto);
			Objeto objeto = RegistroObjetos.obtenerObjeto(idObjeto);
			ObjetoUnicoTiled objetoUnico = new ObjetoUnicoTiled(posicionObjeto, objeto);
			objetosMapa.add(objetoUnico);
		}
		
		//OBTENER ENEMIGOS ZOMBIES
		enemigosMapa = new ArrayList<>();
		JSONArray coleccionEnemigos = obtenerArrayJSON(globalJSON.get("enemigos").toString());
		for (int i = 0; i < coleccionEnemigos.size(); i++) {
			JSONObject datosEnemigo = obtenerObjetoJSON(coleccionEnemigos.get(i).toString());
			
			int idEnemigo = obtenerIntDesdeJSON(datosEnemigo, "id");
			int xEnemigo = obtenerIntDesdeJSON(datosEnemigo, "x");
			int yEnemigo = obtenerIntDesdeJSON(datosEnemigo, "y");
			
			Point posicionEnemigo = new Point(xEnemigo, yEnemigo);
			Enemigo enemigo = RegistroEnemigos.obtenerEnemigo(idEnemigo);
			enemigo.establecerPosicion(posicionEnemigo.x, posicionEnemigo.y);
			
			enemigosMapa.add(enemigo);
		}
                
                //OBTENER ENEMIGOS Serpiente
		enemigosMapaSerpiente = new ArrayList<>();
		JSONArray coleccionEnemigos2 = obtenerArrayJSON(globalJSON.get("enemigos").toString());
		for (int i = 0; i < coleccionEnemigos2.size(); i++) {
			JSONObject datosEnemigo = obtenerObjetoJSON(coleccionEnemigos2.get(i).toString());
			
			int idEnemigoSerpiente = obtenerIntDesdeJSON(datosEnemigo, "id");
			int xEnemigoSerpiente = obtenerIntDesdeJSON(datosEnemigo, "x");
			int yEnemigoSerpiente = obtenerIntDesdeJSON(datosEnemigo, "y");
			
			Point posicionEnemigoSerpiente = new Point(xEnemigoSerpiente, yEnemigoSerpiente);
			Enemigo enemigoSerpiente = RegistroEnemigos.obtenerEnemigo(idEnemigoSerpiente);
			enemigoSerpiente.establecerPosicion(posicionEnemigoSerpiente.x, posicionEnemigoSerpiente.y);
			
			enemigosMapaSerpiente.add(enemigoSerpiente);
		}
   
		areasColisionPorActualizacion = new ArrayList<>();
	}
	int contador = 0;
        
	public void actualizar() {
            
            //System.out.println("posicion jugador EN x" + ElementosPrincipales.jugador.dimePosicionXInt());
           // System.out.println("puntosalidaX" + puntosalidaX);
            
		actualizarAreasColision();
		actualizarRecogidaObjetos();
		actualizarEnemigos();
		actualizarAtaques();
                actualizarZonaSalida();
       
           
		Point punto = new Point(ElementosPrincipales.jugador.dimePosicionXInt(),
				ElementosPrincipales.jugador.dimePosicionYInt());
		Point puntoCoincidente = d.obtenerCoordenadasNodoCoincidente(punto);
                    contador = contador + 1;
                    d.reiniciarYEvaluar(puntoCoincidente);
               
              // System.out.println(ElementosPrincipales.jugador.obtenerPosicionXInt());
		//System.out.println(ElementosPrincipales.jugador.obtenerPosicionYInt());
                
               // System.out.println(puntoCoincidente);
		
                
	}
        private void actualizarZonaSalida() {
            
       areaJugador = new Rectangle(centroX,
                    centroY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);    
            int puntoX = (int) puntoSalida.getX()
					- (int) ElementosPrincipales.jugador.dimePosicionX() + Constantes.MARGEN_X;
			int puntoY = (int) puntoSalida.getY()
					- (int) ElementosPrincipales.jugador.dimePosicionY() + Constantes.MARGEN_Y;
                
         
         puntosalidaX = centroX - ElementosPrincipales.jugador.dimePosicionXInt() + Constantes.MARGEN_X;
                        
	 puntosalidaY = centroY - ElementosPrincipales.jugador.dimePosicionYInt() + Constantes.MARGEN_Y;               
                        

        if(GestorJuego.Mapa1Activado==true){
            
            zonaSalida = new Rectangle(centroX - ElementosPrincipales.jugador.dimePosicionXInt() + Constantes.MARGEN_X, centroY - ElementosPrincipales.jugador.dimePosicionYInt() + Constantes.MARGEN_Y,32,32); 
            if (areaJugador.intersects(zonaSalida) ) 
         {
            System.out.println("se ha interceptado el rectangulo zona salida");
            GestorJuego.establecerMapa();
                   
         }
       }else if(GestorJuego.Mapa2Activado==true)
{
            zonaSalida = new Rectangle((centroX + 1650) - (ElementosPrincipales.jugador.dimePosicionXInt() + Constantes.MARGEN_X), (centroY + 1535) - (ElementosPrincipales.jugador.dimePosicionYInt() + Constantes.MARGEN_Y),32,32); 
            System.out.println("mapa 2 else activado ");
            System.out.println("zona salida 2 " +zonaSalida);
            System.out.println("areaJugador " +areaJugador);
            
      if (areaJugador.intersects(zonaSalida)) 
         {
            System.out.println("se ha interceptado el rectangulo zona salida");
            GestorJuego.establecerMapa();
                   
         }
       }
             
    }
	
	private void actualizarAreasColision() {
		if (!areasColisionPorActualizacion.isEmpty()) {
			areasColisionPorActualizacion.clear();
		}
		
		for (int i = 0; i < areasColisionOriginales.size(); i++) {
			Rectangle rInicial = areasColisionOriginales.get(i);
			
			int puntoX = rInicial.x - (int) ElementosPrincipales.jugador.dimePosicionX() + Constantes.MARGEN_X;
			int puntoY = rInicial.y - (int) ElementosPrincipales.jugador.dimePosicionY() + Constantes.MARGEN_Y;
			
			final Rectangle rFinal = new Rectangle(puntoX, puntoY, rInicial.width, rInicial.height);
			
			areasColisionPorActualizacion.add(rFinal);
		}
                
	}
	
	private void actualizarRecogidaObjetos() {
		if (!objetosMapa.isEmpty()) {
            final Rectangle areaJugador = new Rectangle(ElementosPrincipales.jugador.dimePosicionXInt(),
                    ElementosPrincipales.jugador.dimePosicionYInt(), Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);

            for (int i = 0; i < objetosMapa.size(); i++) {
                final ObjetoUnicoTiled objetoActual = objetosMapa.get(i);

                final Rectangle posicionObjetoActual = new Rectangle(
                        objetoActual.obtenerPosicion().x,
                        objetoActual.obtenerPosicion().y, Constantes.LADO_SPRITE,
                        Constantes.LADO_SPRITE);
                
                /*zonaSalida = new Rectangle(objetoActual.obtenerPosicion().x +180,
                        objetoActual.obtenerPosicion().y +30, Constantes.LADO_SPRITE,
                        Constantes.LADO_SPRITE); */

                if (areaJugador.intersects(posicionObjetoActual) && GestorControles.teclado.recogiendo) {
                    
                    ElementosPrincipales.inventario.recogerObjetos(objetoActual);
                    
                if (objetoActual.obtenerObjeto().obtenerId() == 500){
                    objetosMapa.remove(i);
                }else{
                 
                    //objetosMapa.remove(i);
                }
                    
                }
               /* if (areaJugador.intersects(zonaSalida) ) {
                    System.out.println("se ha interceptado el rectangulo zona salida");
                   GestorJuego.establecerMapa();
                    
               }*/
                
            }
        }
	}
	
	private void actualizarEnemigos() {
		if (!enemigosMapa.isEmpty()) {
			for (Enemigo enemigo : enemigosMapa) {
				enemigo.cambiarSiguienteNodo(d.encontrarSiguienteNodoParaEnemigo(enemigo));
				enemigo.actualizar(enemigosMapa);
			}
		}
	}
	
	private void actualizarAtaques() {

        if (enemigosMapa.isEmpty()
                || ElementosPrincipales.jugador.obtenerAlcanceActual().isEmpty()
                || ElementosPrincipales.jugador.obtenerAlmacenEquipo().DimeArmaEquipada() instanceof Desarmado) {
            return;
        }
       

        if (GestorControles.teclado.atacando) {     	 
            ArrayList<Enemigo> enemigosAlcanzados = new ArrayList<>();

            if (ElementosPrincipales.jugador.obtenerAlmacenEquipo().DimeArmaEquipada().esPenetrante()) {
                for (Enemigo enemigo : enemigosMapa) {
                	//System.out.println(ElementosPrincipales.jugador.obtenerAlcanceActual().get(0));
                	//System.out.println(enemigo.obtenerAreaPosicional());
                    if (ElementosPrincipales.jugador.obtenerAlcanceActual().get(0).intersects(enemigo.obtenerArea())) {
                       // System.out.println("impacto");
                    	enemigosAlcanzados.add(enemigo);
                    }
                }
            } else {
                Enemigo enemigoMasCercano = null;
                Double distanciaMasCercana = null;

                for (Enemigo enemigo : enemigosMapa) {
                    if (ElementosPrincipales.jugador.obtenerAlcanceActual().get(0).intersects(enemigo.obtenerArea())) {
                        Point puntoJugador = new Point((int) ElementosPrincipales.jugador.dimePosicionX() / 32, (int) ElementosPrincipales.jugador.dimePosicionY() / 32);
                        Point puntoEnemigo = new Point((int) enemigo.obtenerPosicionX(), (int) enemigo.obtenerPosicionY());

                        Double distanciaActual = CalculadoraDistancia.obtenerDistanciaEntrePuntos(puntoJugador, puntoEnemigo);

                        if (enemigoMasCercano == null) {
                            enemigoMasCercano = enemigo;
                            distanciaMasCercana = distanciaActual;
                        } else if (distanciaActual < distanciaMasCercana) {
                            enemigoMasCercano = enemigo;
                            distanciaMasCercana = distanciaActual;
                        }

                    }
                }
                enemigosAlcanzados.add(enemigoMasCercano);
            }
            System.out.println(enemigosAlcanzados.size());
            ElementosPrincipales.jugador.obtenerAlmacenEquipo().DimeArmaEquipada().atacar(enemigosAlcanzados);
        }

        Iterator<Enemigo> iterador = enemigosMapa.iterator();

        while (iterador.hasNext()) {
            Enemigo enemigo = iterador.next();

            if (enemigo.obtenerVidaActual() <= 0) {
                iterador.remove();
                ElementosPrincipales.jugador.puntos += 100;
            }
        }
    }
	
	public void dibujar(Graphics g) {
		for (int i = 0; i < capasSprites.size(); i++) {
                    
			int[] spritesCapa = capasSprites.get(i).obtenerArraySprites();
			
			for (int y = 0; y < altoMapaEnTiles; y++) {
				for (int x = 0; x < anchoMapaEnTiles; x++) {
					int idSpriteActual = spritesCapa[x + y * anchoMapaEnTiles];
					if (idSpriteActual != -1) {
						int puntoX = x * Constantes.LADO_SPRITE
								- (int) ElementosPrincipales.jugador.dimePosicionX() + Constantes.MARGEN_X;
						int puntoY = y * Constantes.LADO_SPRITE
								- (int) ElementosPrincipales.jugador.dimePosicionY() + Constantes.MARGEN_Y;
						
						if (puntoX < 0 - Constantes.LADO_SPRITE ||
							puntoX > Constantes.ANCHO_JUEGO ||
							puntoY < 0 - Constantes.LADO_SPRITE ||
							puntoY > Constantes.ALTO_JUEGO - 65) {
							continue;
						}
						
						DibujoDebug.dibujarImagen(g, paletaSprites[idSpriteActual].obtenerImagen(),
								puntoX, puntoY);
                                                
                                                
					}
                                        
				}
                                
			}
               
                        
		}
           
		for (int i = 0; i < objetosMapa.size(); i++) {
			ObjetoUnicoTiled objetoActual = objetosMapa.get(i);
                        //ObjetoUnicoTiled objetoEscopeta = objetosMapa.get(1);
			
			int puntoX = objetoActual.obtenerPosicion().x
					- (int) ElementosPrincipales.jugador.dimePosicionX() + Constantes.MARGEN_X;
                        
			int puntoY = objetoActual.obtenerPosicion().y
					- (int) ElementosPrincipales.jugador.dimePosicionY() + Constantes.MARGEN_Y;
			
			if (puntoX < 0 - Constantes.LADO_SPRITE ||
					puntoX > Constantes.ANCHO_JUEGO ||
					puntoY < 0 - Constantes.LADO_SPRITE ||
					puntoY > Constantes.ALTO_JUEGO - 65) {
					continue;
				}
			
                        
                            
                           //DibujoDebug.dibujarImagen(g, objetoActual.obtenerObjeto().obtenerSprite().obtenerImagen(),
			//puntoX+20, puntoY); 
                        
                         // DibujoDebug.dibujarImagen(g, objetoActual.obtenerObjeto().obtenerSpriteEscopeta().obtenerImagen(), 
                       // puntoX+40, puntoY);  
                        
                        
                        if (objetoActual.obtenerObjeto().obtenerId() == 500){
                          //DibujoDebug.dibujarRectanguloRelleno(g, puntoX+180, puntoY+30, 32, 32);
                        //System.out.println(puntoX + " " + puntoY);
                        DibujoDebug.dibujarImagen(g, objetoActual.obtenerObjeto().obtenerSprite().obtenerImagen(),
			puntoX+20, puntoY); 
                        
                        
                        }
                        if (objetoActual.obtenerObjeto().obtenerId() == 501){
                         
                        DibujoDebug.dibujarImagen(g, objetoActual.obtenerObjeto().obtenerSpriteEscopeta().obtenerImagen(), 
                       puntoX+80, puntoY); 
                        
                        
                        }
          
          //DibujoDebug.dibujarRectanguloRelleno(g, zonaSalida, Color.red);
          // g.setColor(Color.red);
          // g.fillRect(zonaSalida.x, zonaSalida.y, zonaSalida.width, zonaSalida.height);
                         
		}
	
		for (int i = 0; i < enemigosMapa.size(); i++) {
			Enemigo enemigo = enemigosMapa.get(i);
			int puntoX = (int) enemigo.obtenerPosicionX()
					- (int) ElementosPrincipales.jugador.dimePosicionX() + Constantes.MARGEN_X;
			int puntoY = (int) enemigo.obtenerPosicionY()
					- (int) ElementosPrincipales.jugador.dimePosicionY() + Constantes.MARGEN_Y;
			
			if (puntoX < 0 - Constantes.LADO_SPRITE ||
					puntoX > Constantes.ANCHO_JUEGO ||
					puntoY < 0 - Constantes.LADO_SPRITE ||
					puntoY > Constantes.ALTO_JUEGO - 65) {
					continue;
				}
			
       enemigo.dibujar(g, puntoX, puntoY, imagen);
//	DibujoDebug.dibujarRectanguloContorno(g, puntoX, puntoY, 32, 32);
                    
                        
		}
                
       //   g.fillRect(600, 800, 32, 32); 
                
             if(GestorJuego.Mapa1Activado==true){
            areaJugador = new Rectangle(centroX,centroY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE); 
            zonaSalida = new Rectangle(centroX - ElementosPrincipales.jugador.dimePosicionXInt() + Constantes.MARGEN_X, centroY - ElementosPrincipales.jugador.dimePosicionYInt() + Constantes.MARGEN_Y,32,32);
            
            DibujoDebug.dibujarString(g, (centroX) - (ElementosPrincipales.jugador.dimePosicionXInt()+Constantes.MARGEN_X) + "-" + (ElementosPrincipales.jugador.dimePosicionYInt() + Constantes.MARGEN_Y),  
                    centroX - ElementosPrincipales.jugador.dimePosicionXInt() + 500,
                    centroY - ElementosPrincipales.jugador.dimePosicionYInt() + 410);
           
            DibujoDebug.dibujarRectanguloRelleno(g, zonaSalida);
            DibujoDebug.dibujarRectanguloRelleno(g, areaJugador,Color.YELLOW);
            DibujoDebug.dibujarRectanguloContorno(g, areaJugador, Color.BLUE);
            //System.out.println(ElementosPrincipales.mapa.id);
            //System.out.println(GestorJuego.Mapa1Activado);
            //DibujoDebug.dibujarRectanguloRelleno(g, areaJugador);
            
                 }else{
                 
            zonaSalida = new Rectangle((centroX + 1650) - (ElementosPrincipales.jugador.dimePosicionXInt() + Constantes.MARGEN_X), (centroY + 1535) - (ElementosPrincipales.jugador.dimePosicionYInt() + Constantes.MARGEN_Y),32,32); 
            areaJugador = new Rectangle(centroX,centroY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
            
            DibujoDebug.dibujarString(g, (centroX + 1650) + (ElementosPrincipales.jugador.dimePosicionXInt()+Constantes.MARGEN_X) + "-" + (centroY+ 1535) +(ElementosPrincipales.jugador.dimePosicionYInt() + Constantes.MARGEN_Y),
                    
            centroX - ElementosPrincipales.jugador.dimePosicionXInt() + Constantes.MARGEN_X,
            centroY - ElementosPrincipales.jugador.dimePosicionYInt() + Constantes.MARGEN_Y);
             
            final int centroX = Constantes.ANCHO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;
            final int centroY = Constantes.ALTO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;
        
           // DibujoDebug.dibujarRectanguloRelleno(g, centroX, centroY, 32, 32);  
              
            DibujoDebug.dibujarRectanguloRelleno(g, zonaSalida);
            
            DibujoDebug.dibujarRectanguloContorno(g, zonaSalida, Color.BLUE);

            DibujoDebug.dibujarRectanguloRelleno(g, areaJugador,Color.YELLOW);
            DibujoDebug.dibujarRectanguloContorno(g, areaJugador, Color.BLUE);
            
            DibujoDebug.dibujarRectanguloContorno(g, areaJugador, Color.red);
            
            //System.out.println(ElementosPrincipales.mapa.id);
            //System.out.println(GestorJuego.Mapa1Activado);
            //System.out.println("mapa 2 else activado del dibujar");
            System.out.println("zona salida X " + (centroX + 1650 - ElementosPrincipales.jugador.dimePosicionXInt() + Constantes.MARGEN_X));
            System.out.println("zona salida Y " + (centroY + 1535 - ElementosPrincipales.jugador.dimePosicionYInt() + Constantes.MARGEN_Y));
            
            
             }   
                 
            
            

	}

	private JSONObject obtenerObjetoJSON(final String codigoJSON) {
		JSONParser lector = new JSONParser();
		JSONObject objetoJSON = null;
		
		try {
			Object recuperado = lector.parse(codigoJSON);
			objetoJSON = (JSONObject) recuperado;
		} catch(ParseException e) {
			//System.out.println("Posicion: " + e.getPosition());
			//System.out.println(e);
		}
		
		return objetoJSON;
	}
	
	private JSONArray obtenerArrayJSON(final String codigoJSON) {
		JSONParser lector = new JSONParser();
		JSONArray arrayJSON = null;
		
		try {
			Object recuperado = lector.parse(codigoJSON);
			arrayJSON = (JSONArray) recuperado;
		} catch(ParseException e) {
			//System.out.println("Posicion: " + e.getPosition());
			System.out.println(e);
		}
		
		return arrayJSON;
	}
	
	private int obtenerIntDesdeJSON(final JSONObject objetoJSON, final String clave) {
		return Integer.parseInt(objetoJSON.get(clave).toString());
	}
	
	public Point obtenerPosicionInicial() {
		return puntoInicial;
	}
	
	public Rectangle obtenerBordes(final int posicionX, final int posicionY) {
		int x = Constantes.MARGEN_X - posicionX + ElementosPrincipales.jugador.dimeAncho();
        int y = Constantes.MARGEN_Y - posicionY + ElementosPrincipales.jugador.obtenerAlto();
        
        int ancho = this.anchoMapaEnTiles * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.dimeAncho() * 2;
        int alto = this.altoMapaEnTiles * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.obtenerAlto() * 2;

        return new Rectangle(x, y, ancho, alto);
	}
      
        public Point obtenerPuntoSalida() {
        return  puntoSalida;
    }
        public String obtenerSiguienteMapa() {
        return siguienteMapa;
    }

    public Rectangle obtenerZonaSalida() {
        return zonaSalida;
    }	
        
     public Rectangle obtenerZonaSalida2() {
        return zonaSalida2;
    }    
 
    @Override
    public void run() {
        
        
    }

   
}