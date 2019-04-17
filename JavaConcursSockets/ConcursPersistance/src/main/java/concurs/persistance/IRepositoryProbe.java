package concurs.persistance;

import concurs.model.Proba;

public interface IRepositoryProbe extends RepositoryCRUD<String, Proba> {
   public Proba findOneByDistantaStil(int distanta, String stil);
}
