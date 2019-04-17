package concurs.persistance;


import concurs.model.DTOInscriere;
import concurs.model.Inscriere;
import concurs.model.Pair;

public interface IRepositoryInscrieri extends RepositoryCRUD<Pair<String,String>, Inscriere> {
    public Iterable<DTOInscriere> findParticipantiByProba(String idProba);
}
