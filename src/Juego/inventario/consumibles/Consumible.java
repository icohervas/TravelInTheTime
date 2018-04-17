package Juego.inventario.consumibles;

import Juego.Constantes;
import Juego.inventario.Objeto;
import static Juego.inventario.armas.Arma.hojaArmasEscopeta;
import Juego.sprites.HojaSprites;
import Juego.sprites.Sprite;

public class Consumible extends Objeto {

	public static HojaSprites hojaConsumibles = new HojaSprites(Constantes.RUTA_OBJETOS, Constantes.LADO_SPRITE, false);

	public Consumible(int id, String nombre, String descripcion) {
		super(id, nombre, descripcion);
	}

	public Consumible(int id, String nombre, String descripcion, int cantidad) {
		super(id, nombre, descripcion, cantidad);
	}

        @Override
	public Sprite obtenerSprite() {
		return hojaConsumibles.obtenerSprite(id);
	}
        @Override
        public Sprite obtenerSpriteEscopeta() {
        return hojaArmasEscopeta.obtenerSprite(id);
    }

}