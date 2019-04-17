package concurs.model;

import java.io.Serializable;

public class Participant implements HasID<String> {
    private String ID;
    private String name;
    private int varsta;

    public Participant(String ID, String name, int varsta) {
        this.ID = ID;
        this.name = name;
        this.varsta = varsta;
    }
    @Override
    public String getID() {
        return ID;
    }

    @Override
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }
}
