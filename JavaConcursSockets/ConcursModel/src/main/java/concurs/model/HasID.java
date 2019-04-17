package concurs.model;

import java.io.Serializable;

public interface HasID<Tid> extends Serializable{
    Tid getID();
    void setID(Tid id);

}
