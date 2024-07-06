package pb.ajneb97.juego;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Random;


public class Partida {

    private Team team1;
    private Team team2;
    private String nombre;
    private int cantidadMaximaJugadores;
    private int cantidadMinimaJugadores;
    private int cantidadActualJugadores;
    private GameState estado;
    private Location lobby;
    private int tiempo;
    private int tiempoMaximo;
    private int vidasIniciales;
    private boolean enNuke;

    public Partida(String nombre, int tiempoMaximo, String equipo1, String equipo2, int vidasIniciales) {
        //por defecto
        this.team1 = new Team(equipo1);
        this.team2 = new Team(equipo2);
        this.nombre = nombre;
        this.cantidadMaximaJugadores = 16;
        this.cantidadMinimaJugadores = 4;
        this.cantidadActualJugadores = 0;
        this.estado = GameState.DISABLED;
        this.tiempo = 0;
        this.tiempoMaximo = tiempoMaximo;
        this.vidasIniciales = vidasIniciales;
        this.enNuke = false;
    }

    public boolean isEnNuke() {
        return enNuke;
    }

    public void setEnNuke(boolean enNuke) {
        this.enNuke = enNuke;
    }

    public void setVidasIniciales(int cantidad) {
        this.vidasIniciales = cantidad;
    }

    public int getVidasIniciales() {
        return this.vidasIniciales;
    }

    public void setTiempoMaximo(int tiempo) {
        this.tiempoMaximo = tiempo;
    }

    public int getTiempoMaximo() {
        return this.tiempoMaximo;
    }

    public void disminuirTiempo() {
        this.tiempo--;
    }

    public void aumentarTiempo() {
        this.tiempo++;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getTiempo() {
        return this.tiempo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void agregarJugador(PaintballPlayer player) {
        //ANTES DE INICIAR LA PARTIDA TODOS ESTAN EN EL TEAM 1 Y LUEGO SE REPARTEN LOS DEMAS AL TEAM 2
        if (team1.agregarJugador(player)) {
            this.cantidadActualJugadores++;
        }
    }

    public void repartirJugadorTeam2(PaintballPlayer player) {
        this.team1.removerJugador(player.getPlayer().getName());
        this.team2.agregarJugador(player);
    }

    public void removerJugador(String player) {
        if (team1.removerJugador(player) || team2.removerJugador(player)) {
            this.cantidadActualJugadores--;
        }
    }

    public ArrayList<PaintballPlayer> getJugadores() {
        ArrayList<PaintballPlayer> jugadores = new ArrayList<PaintballPlayer>();

        ArrayList<PaintballPlayer> jugadoresTeam1 = team1.getJugadores();
        for (int i = 0; i < jugadoresTeam1.size(); i++) {
            jugadores.add(jugadoresTeam1.get(i));
        }
        ArrayList<PaintballPlayer> jugadoresTeam2 = team2.getJugadores();
        for (int i = 0; i < jugadoresTeam2.size(); i++) {
            jugadores.add(jugadoresTeam2.get(i));
        }

        return jugadores;
    }

    public PaintballPlayer getJugador(String jugador) {
        for (int i = 0; i < getJugadores().size(); i++) {
            if (getJugadores().get(i).getPlayer().getName().equals(jugador)) {
                return getJugadores().get(i);
            }
        }
        return null;
    }

    public Team getEquipoJugador(String jugador) {
        ArrayList<PaintballPlayer> jugadoresTeam1 = team1.getJugadores();
        for (int i = 0; i < jugadoresTeam1.size(); i++) {
            if (jugadoresTeam1.get(i).getPlayer().getName().equals(jugador)) {
                return this.team1;
            }
        }
        ArrayList<PaintballPlayer> jugadoresTeam2 = team2.getJugadores();
        for (int i = 0; i < jugadoresTeam2.size(); i++) {
            if (jugadoresTeam2.get(i).getPlayer().getName().equals(jugador)) {
                return this.team2;
            }
        }

        return null;
    }

    public Team getTeam1() {
        return this.team1;
    }

    public Team getTeam2() {
        return this.team2;
    }

    public int getCantidadMaximaJugadores() {
        return this.cantidadMaximaJugadores;
    }

    public void setCantidadMaximaJugadores(int max) {
        this.cantidadMaximaJugadores = max;
    }

    public int getCantidadMinimaJugadores() {
        return this.cantidadMinimaJugadores;
    }

    public void setCantidadMinimaJugadores(int min) {
        this.cantidadMinimaJugadores = min;
    }

    public int getCantidadActualJugadores() {
        return this.cantidadActualJugadores;
    }

    public GameState getEstado() {
        return this.estado;
    }

    public void setEstado(GameState estado) {
        this.estado = estado;
    }

    public boolean estaIniciada() {
        if (!this.estado.equals(GameState.WAITING) && !this.estado.equals(GameState.STARTING)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean estaLlena() {
        if (this.cantidadActualJugadores == this.cantidadMaximaJugadores) {
            return true;
        } else {
            return false;
        }
    }

    public boolean estaActivada() {
        if (this.estado.equals(GameState.DISABLED)) {
            return false;
        } else {
            return true;
        }
    }

    public void setLobby(Location l) {
        this.lobby = l;
    }

    public Location getLobby() {
        return this.lobby;
    }

    public Team getGanador() {
        if (team1.getJugadores().size() == 0) {
            return team2;
        }
        if (team2.getJugadores().size() == 0) {
            return team1;
        }

        int vidasTeam1 = team1.getVidas();
        int vidasTeam2 = team2.getVidas();
        if (vidasTeam1 > vidasTeam2) {
            return team1;
        } else if (vidasTeam2 > vidasTeam1) {
            return team2;
        } else {
            return null; //empate
        }
    }

    public ArrayList<PaintballPlayer> getJugadoresKills() {
        ArrayList<PaintballPlayer> nuevo = new ArrayList<PaintballPlayer>();
        for (int i = 0; i < getJugadores().size(); i++) {
            nuevo.add(getJugadores().get(i));
        }

        for (int i = 0; i < nuevo.size(); i++) {
            for (int c = i + 1; c < nuevo.size(); c++) {
                if (nuevo.get(i).getKills() < nuevo.get(c).getKills()) {
                    PaintballPlayer j = nuevo.get(i);
                    nuevo.set(i, nuevo.get(c));
                    nuevo.set(c, j);
                }
            }
        }

        return nuevo;
    }

    public boolean puedeSeleccionarEquipo(String equipo) {
        int mitad = 0;
        if (this.cantidadActualJugadores % 2 != 0) {
            mitad = ((int) this.cantidadActualJugadores / 2) + 1;
        } else {
            mitad = (int) this.cantidadActualJugadores / 2;
        }
        if (equipo.equals(this.team1.getTipo())) {
            int cantidadPreferenciaTeam1 = 0;
            for (PaintballPlayer j : this.getJugadores()) {
                if (j.getPreferenciaTeam() != null && j.getPreferenciaTeam().equals(this.team1.getTipo())) {
                    cantidadPreferenciaTeam1++;
                }
            }

            if (this.cantidadActualJugadores == 1) {
                return true;
            }


            if (cantidadPreferenciaTeam1 >= mitad) {
                return false;
            }
        } else {
            int cantidadPreferenciaTeam2 = 0;
            for (PaintballPlayer j : this.getJugadores()) {
                if (j.getPreferenciaTeam() != null && j.getPreferenciaTeam().equals(this.team2.getTipo())) {
                    cantidadPreferenciaTeam2++;
                }
            }

            if (this.cantidadActualJugadores == 1) {
                return true;
            }
            if (cantidadPreferenciaTeam2 >= mitad) {
                return false;
            }
        }

        return true;
    }

    public void modificarTeams(YamlDocument config) {
        Team team1 = this.team1;
        Team team2 = this.team2;
        String nTeam1 = team1.getTipo();
        String nTeam2 = team2.getTipo();
        Random r = new Random();
        ArrayList<String> nombres = new ArrayList<String>();
        for (String key : config.getSection("teams").getRoutesAsStrings(false)) {
            nombres.add(key);
        }

        int max = nombres.size();
        if (team1.esRandom() && !team2.esRandom()) {
            int num = r.nextInt(max);
            nTeam1 = nombres.get(num);
            while (nTeam1.equals(nTeam2)) {
                num = r.nextInt(max);
                nTeam1 = nombres.get(num);
            }
            team1.setTipo(nTeam1);
        } else if (!team1.esRandom() && team2.esRandom()) {
            int num = r.nextInt(max);
            nTeam2 = nombres.get(num);
            while (nTeam2.equals(nTeam1)) {
                num = r.nextInt(max);
                nTeam2 = nombres.get(num);
            }
            team2.setTipo(nTeam2);
        } else if (team1.esRandom() && team2.esRandom()) {
            int num = r.nextInt(max);
            nTeam1 = nombres.get(num);
            num = r.nextInt(max);
            nTeam2 = nombres.get(num);
            while (nTeam2.equals(nTeam1)) {
                num = r.nextInt(max);
                nTeam2 = nombres.get(num);
            }
            team1.setTipo(nTeam1);
            team2.setTipo(nTeam2);
        }
    }
}
