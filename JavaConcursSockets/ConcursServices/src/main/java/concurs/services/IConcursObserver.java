package concurs.services;

import concurs.model.ConcursException;
import concurs.model.DTOInscriere;
import concurs.model.Proba;

public interface IConcursObserver {
    void updateProbe(Proba[] proba) throws ConcursException;
    //void updateInscrieri() throws  ConcursException;


}
