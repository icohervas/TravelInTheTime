package Juego.protagonistas;

import Juego.inventario.armas.Arma;

public class AlmacenEquipo {

    private Arma arma;
    //armadura
    //munición en uso
    //cinturón

    public AlmacenEquipo(final Arma arma1) {
        this.arma = arma1;
    }

    //Metodo que devuelve el arma equipada por el personaje.
    public Arma DimeArmaEquipada() {
        return arma;
    }
//metodo que permite cambiar de arma al personaje.
    public void cambiarArma1(final Arma arma1) {
        this.arma = arma1;
    }
}