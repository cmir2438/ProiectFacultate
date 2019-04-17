package concurs.services;

import concurs.model.*;

public interface IConcursServer {
    void login(User user, IConcursObserver observer) throws ConcursException;

    void logout(User user, IConcursObserver observer) throws ConcursException;

    DTOInscriere[] getParticipantiByProba(String idProba) throws ConcursException;

    Proba[] getAllProbe() throws ConcursException;

    void saveInscriere(DTOInscrie inscrie) throws ConcursException;
}