package Juego.maquinaestado.estados.menujuego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import Juego.Constantes;
import Juego.ElementosPrincipales;
import Juego.GestorPrincipal;
import Juego.graficos.SuperficieDibujo;
import Juego.herramientas.DibujoDebug;
import Juego.herramientas.EscaladorElementos;
import Juego.herramientas.MedidorStrings;
import Juego.inventario.Objeto;
import Juego.inventario.armas.Arma;
import Juego.inventario.armas.Desarmado;

public class MenuEquipo extends SeccionMenu {

	final Rectangle panelObjetos = new Rectangle(em.FONDO.x + margenGeneral,
			barraPeso.y + barraPeso.height + margenGeneral, 248,
			Constantes.ALTO_JUEGO - barraPeso.y - barraPeso.height - margenGeneral * 2);
	final Rectangle titularPanelObjetos = new Rectangle(panelObjetos.x, panelObjetos.y, panelObjetos.width, 24);

	final Rectangle panelEquipo = new Rectangle(panelObjetos.x + panelObjetos.width + margenGeneral, panelObjetos.y, 88,
			panelObjetos.height);
	final Rectangle titularPanelEquipo = new Rectangle(panelEquipo.x, panelEquipo.y, panelEquipo.width, 24);

	final Rectangle panelAtributos = new Rectangle(panelEquipo.x + panelEquipo.width + margenGeneral, panelObjetos.y,
			132, panelEquipo.height);
	final Rectangle titularPanelAtributos = new Rectangle(panelAtributos.x, panelAtributos.y, panelAtributos.width, 24);

        
        
	final Rectangle etiquetaArma = new Rectangle(titularPanelEquipo.x + margenGeneral,
			titularPanelEquipo.y + titularPanelEquipo.height + margenGeneral,
			titularPanelEquipo.width - margenGeneral * 2,
			margenGeneral * 2 + MedidorStrings.medirAltoPixeles(GestorPrincipal.sd.getGraphics(), "Arma"));
        
        //es el cuadrado donde se va a incluir el dibujo del arma.
	final Rectangle contenedorArma = new Rectangle(etiquetaArma.x + 1, etiquetaArma.y + etiquetaArma.height,
			etiquetaArma.width - 2, Constantes.LADO_SPRITE);

	Objeto objetoSeleccionado = null;

	public MenuEquipo(String nombreSeccion, Rectangle etiquetaMenu, EstructuraMenu em) {
		super(nombreSeccion, etiquetaMenu, em);
	}

	public void actualizar() {
		actualizarPosicionesMenu();
		actualizarSeleccionRaton();
		actualizarObjetoSeleccionado();
	}

	private void actualizarPosicionesMenu() {
		if (ElementosPrincipales.inventario.obtenerArmas().isEmpty()) {
			return;
		}

		for (int i = 0; i < ElementosPrincipales.inventario.obtenerArmas().size(); i++) {
			final Point puntoInicial = new Point(titularPanelObjetos.x + margenGeneral,
					titularPanelObjetos.y + titularPanelObjetos.height + margenGeneral);

			final int lado = Constantes.LADO_SPRITE;

			int idActual = ElementosPrincipales.inventario.obtenerArmas().get(i).obtenerId();

			ElementosPrincipales.inventario.obtenerObjeto(idActual).establecerPosicionMenu(
					new Rectangle(puntoInicial.x + i * (lado + margenGeneral), puntoInicial.y, lado, lado));
		}
	}

	private void actualizarSeleccionRaton() {
		Rectangle posicionRaton = GestorPrincipal.sd.obtenerRaton().obtenerRectanguloPosicion();

		if (posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(panelObjetos))) {
			if (ElementosPrincipales.inventario.obtenerArmas().isEmpty()) {
				return;
			}

			for (Objeto objeto : ElementosPrincipales.inventario.obtenerArmas()) {
				if (GestorPrincipal.sd.obtenerRaton().obtenerClick() && posicionRaton
						.intersects(EscaladorElementos.escalarRectanguloArriba(objeto.obtenerPosicionMenu()))) {
					objetoSeleccionado = objeto;
				}
			}
		} else if (posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(panelEquipo))) {
			if (objetoSeleccionado != null && objetoSeleccionado instanceof Arma
                    && GestorPrincipal.sd.obtenerRaton().obtenerClick()
                    && posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(contenedorArma))) {
                ElementosPrincipales.jugador.obtenerAlmacenEquipo().cambiarArma1((Arma) objetoSeleccionado);
                objetoSeleccionado = null;
			}
		} else if (posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(panelAtributos))) {

		}
	}

	private void actualizarObjetoSeleccionado() {
		if (objetoSeleccionado != null) {
			if (GestorPrincipal.sd.obtenerRaton().obtenerClick2()) {
				objetoSeleccionado = null;
				return;
			}

			Point posicionRaton = EscaladorElementos
					.escalarPuntoAbajo(GestorPrincipal.sd.obtenerRaton().obtenerPuntoPosicion());
			objetoSeleccionado.establecerPosicionFlotante(
					new Rectangle(posicionRaton.x, posicionRaton.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE));
		}
	}

	public void dibujar(Graphics g, SuperficieDibujo sd, EstructuraMenu em) {
		dibujarLimitePeso(g, em);

		dibujarPaneles(g);

		if (sd.obtenerRaton().obtenerRectanguloPosicion()
				.intersects(EscaladorElementos.escalarRectanguloArriba(barraPeso))) {
			dibujarTooltipPeso(g, sd, em);
		}
	}

	private void dibujarPaneles(final Graphics g) {
		dibujarPanelObjetos(g, panelObjetos, titularPanelObjetos, "Equipables");
		dibujarPanelEquipo(g, panelEquipo, titularPanelEquipo, "Equipo");
		dibujarPanelAtributos(g, panelAtributos, titularPanelAtributos, "Atributos");
	}
        
        
	private void dibujarPanelObjetos(final Graphics g, final Rectangle panel, final Rectangle titularPanel,
			final String nombrePanel) {
		dibujarPanel(g, panel, titularPanel, nombrePanel);
                //dibujar objetos equipables que puede tener el personaje.
		dibujarElementosEquipables(g, panel, titularPanel);
	}

	private void dibujarElementosEquipables(final Graphics g, final Rectangle panelObjetos,final Rectangle titularPanel) {

            //si no existen armas , no hay nada que dibujar y salimos.
		if (ElementosPrincipales.inventario.obtenerArmas().isEmpty()) {
			return;
		}
                //
		final Point puntoInicial = new Point(titularPanel.x + margenGeneral,titularPanel.y + titularPanel.height + margenGeneral);

		final int lado = Constantes.LADO_SPRITE;

                //recorremos el arraylist de armas, 
		for (int i = 0; i < ElementosPrincipales.inventario.obtenerArmas().size(); i++) {

                    //capturamos el id de cada una de las armas.
			int idActual = ElementosPrincipales.inventario.obtenerArmas().get(i).obtenerId();
                        //capturamos le objetoactual que corresponde con el identificador asignado, ( para diferencias pistola,de escopeta , etc).
			Objeto objetoActual = ElementosPrincipales.inventario.obtenerObjeto(idActual);
                        
                        //comprobamos el identificador del objeto si se trata de una pistola la equipamos y la dibujamos.
                        if (idActual == 500){
                        //dibujamos en el contenedor de objetos equipados el arma equipada.
                          DibujoDebug.dibujarImagen(g, objetoActual.obtenerSprite().obtenerImagen(),objetoActual.obtenerPosicionMenu().x, objetoActual.obtenerPosicionMenu().y); 
                          //si el objeto arma equipado se trata de una escopeta , la dibujamos en la ranura de objeto equipados.
                        }else if (idActual == 501){
                           DibujoDebug.dibujarImagen(g, objetoActual.obtenerSpriteEscopeta().obtenerImagen(),objetoActual.obtenerPosicionMenu().x, objetoActual.obtenerPosicionMenu().y);  
                        }
			
                        //aqui mostraremos la cantidad de cada objeto.
			g.setColor(Color.black);

			DibujoDebug.dibujarRectanguloRelleno(g, puntoInicial.x + i * (lado + margenGeneral) + lado - 12,puntoInicial.y + 32 - 8, 12, 8);

			g.setColor(Color.white);

			String texto = "";

			if (objetoActual.obtenerCantidad() < 10) {
				texto = "0" + objetoActual.obtenerCantidad();
			} else {
				texto = "" + objetoActual.obtenerCantidad();
			}

			g.setFont(g.getFont().deriveFont(10f));
			DibujoDebug.dibujarString(g, texto,
					puntoInicial.x + i * (lado + margenGeneral) + lado - MedidorStrings.medirAnchoPixeles(g, texto),
					puntoInicial.y + 31);
		}
		g.setFont(g.getFont().deriveFont(12f));
                
                        //comprobamos si el objeto seleccionado no esta vacío.
		if (objetoSeleccionado != null) {
			
                        //si el objeto seleccionado es una pistola, cuyo id es 500.
                    if (objetoSeleccionado.obtenerId() == 500){
                        
                        //si el objeto seleccionado es de tipo pistola, dibujamos la imagen del sprite de la pistola en relacion al cursor,
                        //lo que dará la sensacion de que estamos arrastrando la pistola para equiparla.
                          DibujoDebug.dibujarImagen(g, objetoSeleccionado.obtenerSprite().obtenerImagen(), new Point(
					objetoSeleccionado.obtenerPosicionFlotante().x, objetoSeleccionado.obtenerPosicionFlotante().y)); 
                          
                           //si el objeto seleccionado es una escopeta, cuyo id es 501.
                        }else if (objetoSeleccionado.obtenerId() == 501){
                          //si el objeto seleccionado es de tipo escopeta, dibujamos la imagen del sprite de la escopeta en relacion al cursor,
                          //lo que dará la sensacion de que estamos arrastrando la escopeta para equiparla.
                           DibujoDebug.dibujarImagen(g, objetoSeleccionado.obtenerSpriteEscopeta().obtenerImagen(), new Point(
					objetoSeleccionado.obtenerPosicionFlotante().x, objetoSeleccionado.obtenerPosicionFlotante().y));
                        }    
                        
                        
		}
	}

	private void dibujarPanelEquipo(final Graphics g, final Rectangle panel, final Rectangle titularPanel,final String nombrePanel) 
        {
            
		dibujarPanel(g, panel, titularPanel, nombrePanel);

		g.setColor(Color.black);
                
                //dibujamos un rectangulo relleno , pasando el rectangulo etiquetaarma.
		DibujoDebug.dibujarRectanguloRelleno(g, etiquetaArma);
                //dibujamos el rectangulo con contorno y le pasamos el rectangulo contenedor arma.
		DibujoDebug.dibujarRectanguloContorno(g, contenedorArma);

                //comprobamos si el arma esta equipada y la dibujamos.
                // dibujar arma equipada si la hay
        if (!(ElementosPrincipales.jugador.obtenerAlmacenEquipo().DimeArmaEquipada() instanceof Desarmado)) {
            Point coordenadaImagen = new Point(contenedorArma.x + contenedorArma.width / 2 - Constantes.LADO_SPRITE / 2,
                    contenedorArma.y);

            //comprobamos si el objeto que tenemos equipado es un arma, en concreto una pistola, si es asi, dibujamos la pistola en la ranura de elementos equipados.
            if (ElementosPrincipales.jugador.obtenerAlmacenEquipo().DimeArmaEquipada().obtenerId() == 500){
               DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.obtenerAlmacenEquipo().DimeArmaEquipada().obtenerSprite().obtenerImagen(),coordenadaImagen);
                    //System.out.println(ElementosPrincipales.jugador.obtenerAlmacenEquipo().DimeArmaEquipada().obtenerId()); 
              //comprobamos si el objeto que tenemos equipado es un arma, en concreto una escopeta, si es asi, dibujamos la pistola en la ranura de elementos equipados.  
            }else if (ElementosPrincipales.jugador.obtenerAlmacenEquipo().DimeArmaEquipada().obtenerId() == 501){
               DibujoDebug.dibujarImagen(g, ElementosPrincipales.jugador.obtenerAlmacenEquipo().DimeArmaEquipada().obtenerSpriteEscopeta().obtenerImagen(),coordenadaImagen);
                //System.out.println(ElementosPrincipales.jugador.obtenerAlmacenEquipo().DimeArmaEquipada().obtenerId()); 
           }
            
        }
                //escribimos con letra de color blanco el nombre del equipamiento de nuestra ranura
		g.setColor(Color.white);
		DibujoDebug.dibujarString(g, "Arma",new Point(etiquetaArma.x + etiquetaArma.width / 2 - MedidorStrings.medirAnchoPixeles(g, "Arma") / 2,
						etiquetaArma.y + etiquetaArma.height / 2 + MedidorStrings.medirAltoPixeles(g, "Arma") / 2));
		
	}

	private void dibujarPanelAtributos(final Graphics g, final Rectangle panel, final Rectangle titularPanel,
			final String nombrePanel) {
		dibujarPanel(g, panel, titularPanel, nombrePanel);
		// dibujar los atributos
	}

	private void dibujarPanel(final Graphics g, final Rectangle panel, final Rectangle titularPanel,
			final String nombrePanel) {
		g.setColor(new Color(0xff6700));
		DibujoDebug.dibujarRectanguloContorno(g, panel);
		DibujoDebug.dibujarRectanguloRelleno(g, titularPanel);
		g.setColor(Color.white);
		DibujoDebug.dibujarString(g, nombrePanel,
				new Point(titularPanel.x + titularPanel.width / 2
						- MedidorStrings.medirAnchoPixeles(g, nombrePanel) / 2,
				titularPanel.y + titularPanel.height - MedidorStrings.medirAltoPixeles(g, nombrePanel) / 2 - 4));
		g.setColor(new Color(0xff6700));
	}

    public Objeto obtenerObjetoSeleccionado() {
        return objetoSeleccionado;
    }

    public void eliminarObjetoSeleccionado() {
        objetoSeleccionado = null;
    }
}