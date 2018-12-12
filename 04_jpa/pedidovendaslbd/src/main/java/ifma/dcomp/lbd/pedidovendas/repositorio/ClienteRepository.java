package ifma.dcomp.lbd.pedidovendas.repositorio;

import edu.ifma.dcomp.pedidovendas.modelo.Cliente;

import javax.persistence.EntityManager;
import java.util.List;

public class ClienteRepository {

    private final EntityManager manager;
    private final GenericRepository<Cliente> genericRepository;

    public ClienteRepository(EntityManager manager) {
        this.manager = manager;
        this.genericRepository = new GenericRepository<>(this.manager );
    }


    public void salva(Cliente cliente) {
        this.genericRepository.salva(cliente );
    }

    public Cliente buscaPor(Integer id) {
        return this.genericRepository.buscaPorId(Cliente.class, id);
    }


    public List<Cliente> porNome(String nome) {
        return this.manager.createQuery("from Cliente " +
                "where upper(nome) like :nome", Cliente.class)
                .setParameter("nome", nome.toUpperCase() + "%")
                .getResultList();
    }


    public void exclui(Cliente cliente) {
        manager.remove(cliente );
    }


    public void atualiza(Cliente cliente) {
        manager.merge(cliente );
    }
}
