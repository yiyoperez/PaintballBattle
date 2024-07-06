package pb.ajneb97.juego;


public class Killstreak {

    private String type;
    private int time;

    public Killstreak(String type, int tiempo) {
        this.type = type;
        this.time = tiempo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
