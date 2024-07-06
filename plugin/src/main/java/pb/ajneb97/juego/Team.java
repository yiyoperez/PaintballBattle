package pb.ajneb97.juego;

import org.bukkit.Location;

import java.util.ArrayList;


public class Team {

    private ArrayList<PaintballPlayer> jugadores;
    private String tipoEquipo;
    //Tipos: blue,red,yellow,green,orange,purple,black,white
    //brown,magenta,light_blue,lime,pink,gray,light_gray,cyan
    private Location spawn;
    private int vidasActuales;
    private boolean random;


    public Team(String tipoEquipo) {
        this.jugadores = new ArrayList<PaintballPlayer>();
        this.tipoEquipo = tipoEquipo;
        this.vidasActuales = 0;
    }

    public boolean esRandom() {
        return this.random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public int getVidas() {
        return this.vidasActuales;
    }

    public void disminuirVidas(int cantidad) {
        this.vidasActuales = this.vidasActuales - cantidad;
    }

    public void aumentarVidas(int cantidad) {
        this.vidasActuales = this.vidasActuales + cantidad;
    }

    public void setVidas(int cantidad) {
        this.vidasActuales = cantidad;
    }

    public void setTipo(String tipo) {
        this.tipoEquipo = tipo;
    }

    public String getTipo() {
        return this.tipoEquipo;
    }

    public boolean contieneJugador(String jugador) {
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).getPlayer().getName().equals(jugador)) {
                return true;
            }
        }
        return false;
    }

    public boolean agregarJugador(PaintballPlayer jugador) {
        if (!contieneJugador(jugador.getPlayer().getName())) {
            this.jugadores.add(jugador);
            return true;
        } else {
            return false;
        }
    }

    public boolean removerJugador(String jugador) {
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).getPlayer().getName().equals(jugador)) {
                jugadores.remove(i);
                return true;
            }
        }
        return false;
    }

    public ArrayList<PaintballPlayer> getJugadores() {
        return this.jugadores;
    }

    public int getCantidadJugadores() {
        return this.jugadores.size();
    }

    public Location getSpawn() {
        return this.spawn;
    }

    public void setSpawn(Location l) {
        this.spawn = l;
    }

    public int getAsesinatosTotales() {
        int kills = 0;
        for (PaintballPlayer j : this.jugadores) {
            kills = kills + j.getKills();
        }
        return kills;
    }
}
