package ifma.dcomp.lbd.pedidovendas.repositorio;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import edu.ifma.dcomp.pedidovendas.builder.ClienteBuilder;
import edu.ifma.dcomp.pedidovendas.modelo.Cliente;
import edu.ifma.dcomp.pedidovendas.repositorio.ClienteRepository;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;


public class ClienteRepositorioTest {

    private EntityManager manager;
    private static EntityManagerFactory emf;
    private ClienteRepository clienteRepository;

    @BeforeClass
    public static void inicio() {
        emf = Persistence.createEntityManagerFactory("pedidovendasPU-test");
    }

    @Before
    public void antes() {
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        clienteRepository = new ClienteRepository(manager);
    }

    @After
    public void depois() {
        manager.getTransaction().rollback();
    }

    @AfterClass
    public static void fim() {
        emf.close();
    }

    @Test
    public void deveEncontrarClientePeloNome() {

        Cliente cliente = ClienteBuilder.umCliente().comNome("João da Silva").constroi();

        clienteRepository.salva(cliente );
        manager.flush();
        manager.clear();

        Cliente clienteDoBanco = clienteRepository.porNome("João da Silva").get(0);

        Assert.assertThat("João da Silva", is(equalTo(clienteDoBanco.getNome()) ) );
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void naoDeveEncontrarUsuarioPeloNome() {

        Cliente clienteDoBanco = clienteRepository.porNome("Pedro Jose").get(0);

    }

    @Test
    public void deveExcluirUmCliente() {

        Cliente cliente = ClienteBuilder.umCliente().comNome("João Carlos").constroi();

        clienteRepository.salva(cliente);
        clienteRepository.exclui(cliente);
        manager.flush();
        manager.clear();

        List<Cliente> clientes = clienteRepository.porNome("João Carlos");

        Assert.assertTrue(clientes.isEmpty() );

    }

    @Test
    public void deveAlterarUmCliente() {
        Cliente cliente = ClienteBuilder.umCliente().comNome("João Carlos").constroi();

        clienteRepository.salva(cliente);

        cliente.setNome("João da Silva");

        clienteRepository.atualiza(cliente );
        manager.flush();

        List<Cliente> clientes = clienteRepository.porNome("João da Silva");

        Assert.assertThat(clientes.size(), is(1));
        Assert.assertThat(clientes.get(0).getNome(), is("João da Silva") );

        clientes = clienteRepository.porNome("João Carlos");
        Assert.assertTrue(clientes.isEmpty() );
    }

}
