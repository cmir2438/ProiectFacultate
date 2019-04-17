package concurs.model;

import java.io.Serializable;

public class DTOInscrie implements Serializable {
    private String nume;
    private String varsta;
    private String CNP;
    private String probe;

    public DTOInscrie(String nume, String varsta, String CNP, String probe) {
        this.nume = nume;
        this.varsta = varsta;
        this.CNP = CNP;
        this.probe = probe;
    }

    public String getNume() {
        return nume;
    }

    public String getVarsta() {
        return varsta;
    }

    public String getCNP() {
        return CNP;
    }

    public String getProbe() {
        return probe;
    }

    @Override
    public String toString() {
        return "DTOInscrie{" +
                "nume='" + nume + '\'' +
                ", varsta='" + varsta + '\'' +
                ", CNP='" + CNP + '\'' +
                ", probe='" + probe + '\'' +
                '}';
    }
}
