package concurs.server;


import concurs.model.*;
import concurs.persistance.jdbc.RepositoryInscrieri;
import concurs.persistance.jdbc.RepositoryParticipanti;
import concurs.persistance.jdbc.RepositoryProbe;
import concurs.persistance.jdbc.RepositoryUsers;
import concurs.services.IConcursObserver;
import concurs.services.IConcursServer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerImplementation  implements IConcursServer {
    private RepositoryUsers repositoryUsers;
    private RepositoryInscrieri repositoryInscrieri;
    private RepositoryProbe repositoryProbe;
    private RepositoryParticipanti repositoryParticipanti;
    private Map<String, IConcursObserver> connectedUsers;
    public ServerImplementation(RepositoryUsers repositoryUsers, RepositoryInscrieri repositoryInscrieri, RepositoryProbe repositoryProbe, RepositoryParticipanti repositoryParticipanti) {
        this.repositoryUsers = repositoryUsers;
        this.repositoryInscrieri = repositoryInscrieri;
        this.repositoryProbe = repositoryProbe;
        this.repositoryParticipanti = repositoryParticipanti;
        connectedUsers = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(User user, IConcursObserver observer) throws ConcursException {
        User u = repositoryUsers.findBy(user.getID(), user.getPassw());
        if(u != null){
            IConcursObserver obs = connectedUsers.putIfAbsent(user.getID(), observer);
            if(obs != null)
                throw  new ConcursException("User is already connected!");
        }
        else
            throw new ConcursException("Authtentification failes");
    }

    @Override
    public synchronized void logout(User user, IConcursObserver observer) throws ConcursException {
        IConcursObserver obs = connectedUsers.remove(user.getID());
        if(obs == null)
            throw  new ConcursException("User "+ user.getID() +"is not connected");

    }

    @Override
    public synchronized DTOInscriere[] getParticipantiByProba(String idProba) throws ConcursException {
        List<DTOInscriere> inscrieri =(List<DTOInscriere>)repositoryInscrieri.findParticipantiByProba(idProba);
        return  inscrieri.toArray(new DTOInscriere[inscrieri.size()]);

    }

    @Override
    public Proba[] getAllProbe() throws ConcursException {
        List<Proba> probe = (List<Proba>) repositoryProbe.getAll();
        return probe.toArray(new Proba[probe.size()]);
    }

    @Override
    public void saveInscriere(DTOInscrie inscrie) throws ConcursException {
        System.out.println(inscrie);
        if(repositoryParticipanti.findOne(inscrie.getCNP()) == null)
            repositoryParticipanti.save(new Participant(inscrie.getCNP(), inscrie.getNume(), Integer.parseInt(inscrie.getVarsta())));
        List<String> probe = Arrays.asList(inscrie.getProbe().split("/"));
        for(int i =0 ; i < probe.size(); ++i){
            String[] proba = probe.get(i).split(" ");
            Proba prb = repositoryProbe.findOneByDistantaStil(Integer.parseInt(proba[0]), proba[1]);
            if(prb != null){
                repositoryProbe.update(prb.getID(), new Proba(prb.getID(), prb.getDistanta(), prb.getStil(), prb.getNoP() +1));
                repositoryInscrieri.save(new Inscriere(inscrie.getCNP(), prb.getID()));

            }
        }
        notifyAllConnected();


    }

    private   void notifyAllConnected() throws  ConcursException{

        List<Proba> probe = (List<Proba>) repositoryProbe.getAll();
        for (IConcursObserver obs : connectedUsers.values()) {
            obs.updateProbe( probe.toArray(new Proba[probe.size()]));
        }



    }
}
