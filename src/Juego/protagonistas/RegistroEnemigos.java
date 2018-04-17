package Juego.protagonistas;

public class RegistroEnemigos {
    public static Enemigo obtenerEnemigo(final int idEnemigo) {
        Enemigo enemigo = null;

        switch (idEnemigo) {
            case 1:
                enemigo = new Zombie(idEnemigo, "Zombie", 10, "/sonidos/rugidoZombie.wav");
                break;
                
                case 2:
                enemigo = new Serpiente(idEnemigo, "Serpiente", 10, "/sonidos/rugidoZombie.wav");
                break;
        }

        return enemigo;
    }
}