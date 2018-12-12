package ifma.dcomp.lbd.pedidovendas.repositorio;

import javax.persistence.EntityManager;

class GenericRepository<T> {

    private final EntityManager manager;

    GenericRepository(EntityManager manager) {
        this.manager = manager;
    }

    void salva(T t) {
        this.manager.persist(t );
    }

    T buscaPorId(Class<T> clazz, Integer id) {
        return this.manager.find(clazz, id);
    }
}
