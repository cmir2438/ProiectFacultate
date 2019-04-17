package concurs.model;

import java.io.Serializable;

public class Pair<T, P> implements Serializable {
    public T idParticipant;
    public P idProba;

    public Pair(T idParticipant, P idProba) {
        this.idParticipant = idParticipant;
        this.idProba = idProba;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "idParticipant=" + idParticipant +
                ", idProba=" + idProba +
                '}';
    }
}
