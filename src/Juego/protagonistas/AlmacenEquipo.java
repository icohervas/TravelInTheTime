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

    public Arma DimeArmaEquipada() {
        return arma;
    }

    public void cambiarArma1(final Arma arma1) {
        this.arma = arma1;
    }
}