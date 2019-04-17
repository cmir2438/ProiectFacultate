package concurs.model;

import java.io.Serializable;

public class DTOInscriere implements Serializable {
    private String nume;
    private int varsta;
    private String probe;

    public DTOInscriere(String nume, int varsta, String probe) {
        this.nume = nume;
        this.varsta = varsta;
        this.probe = probe;
    }

    public String getNume() {
        return nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public String getProbe() {
        return probe;
    }

    @Override
    public String toString() {
        return "DTOInscriere{" +
                "nume='" + nume + '\'' +
                ", varsta=" + varsta +
                ", probe='" + probe + '\'' +
                '}';
    }
}
