package concurs.persistance;

import concurs.model.HasID;

public interface RepositoryCRUD<ID, E extends HasID<ID>> {
    void save(E e);
    void delete(ID id);
    E findOne(ID id);
    void update(ID id, E e);
    Iterable<E> getAll();
}
