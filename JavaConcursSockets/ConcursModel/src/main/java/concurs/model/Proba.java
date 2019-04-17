package concurs.model;

public class Proba implements HasID<String>  {
    private String ID;
    private int  distanta;
    private Stil stil;
    private int noP =0;


    public int getNoP() {
        return noP;
    }

    public void setNoP(int noP) {
        this.noP = noP;
    }

    public Proba(String ID, int distanta, Stil stil, int noP) {
        this.ID = ID;
        this.distanta = distanta;
        this.stil = stil;
        this.noP = noP;
    }

    public int getDistanta() {
        return distanta;
    }

    public void setDistanta(int distanta) {
        this.distanta = distanta;
    }

    public Stil getStil() {
        return stil;
    }

    public void setStil(Stil stil) {
        this.stil = stil;
    }


    @Override
    public String getID() {
        return ID;
    }

    @Override
    public void setID(String id) {

    }

    @Override
    public String toString() {
        return "Proba{" +
                "ID='" + ID + '\'' +
                ", distanta=" + distanta +
                ", stil=" + stil +
                ", noP=" + noP +
                '}';
    }
}