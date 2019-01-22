package repositorio;

import edu.ifma.dcomp.pedidovendas.builder.ClienteBuilder;
import edu.ifma.dcomp.pedidovendas.builder.PedidoBuilder;
import edu.ifma.dcomp.pedidovendas.modelo.Cliente;
import edu.ifma.dcomp.pedidovendas.modelo.Pedido;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

public class PedidoRepositoryTest {
	
	
	private EntityManager manager;
	private static EntityManagerFactory emf;
	private PedidoRepository pedidoRepository;
	private ClienteRepository clienteRepository;

	@BeforeClass
	public static void inicio() {
		emf = Persistence.createEntityManagerFactory("pedidovendasPU-test");
	}
	
	@Before
	public void antes() {
		manager = emf.createEntityManager();
		manager.getTransaction().begin();
		
		pedidoRepository = new PedidoRepository(manager );
		clienteRepository = new ClienteRepository(manager );
		
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
	public void testSalva() { 	}

	@Test
	public void testBuscaPedidosEmAtraso() {  	}


	@Test
    public void deveTrazerSomentePedidosFinalizados() {

		ClienteRepository clienteRepository = new ClienteRepository(this.manager);

		Cliente joao = ClienteBuilder.umCliente().comNome("Joao da Silva").constroi();
		Cliente jose = ClienteBuilder.umCliente().comNome("José da Silva").constroi();

    	clienteRepository.salva(joao );
    	clienteRepository.salva(jose );
    	manager.flush();

		// criando os pedidos, cada um com um status
		Pedido finalizado1 = PedidoBuilder.umPedido().paraUmCliente(joao).finaliza().constroi();
		Pedido cancelado = PedidoBuilder.umPedido().paraUmCliente(joao).cancela().constroi();
		Pedido finalizado2 = PedidoBuilder.umPedido().paraUmCliente(jose).finaliza().constroi();


		pedidoRepository.salva(finalizado1 );
		pedidoRepository.salva(cancelado );
		pedidoRepository.salva(finalizado2 );

		// chamando o método para testar
        List<Pedido> finalizados = pedidoRepository.finalizados();

        // garantindo que a query funcionou
        Assert.assertEquals(2, finalizados.size());


        //verificação
        Assert.assertEquals("Joao da Silva", finalizados.get(0).getCliente().getNome() );
		Assert.assertEquals("José da Silva", finalizados.get(1).getCliente().getNome() );
    }
	
	
    //@Test
    public void deveTrazerLocacoesPagosComCartao() {

		ClienteRepository clienteRepository = new ClienteRepository(this.manager);

		Cliente joao = ClienteBuilder.umCliente().comNome("Joao da Silva").constroi();
		Cliente jose = ClienteBuilder.umCliente().comNome("José da Silva").constroi();

		clienteRepository.salva(joao );
		clienteRepository.salva(jose );
		manager.flush();

		// criando os pedidos, cada um com um status
		Pedido pedido1 = PedidoBuilder.umPedido().paraUmCliente(joao).comPagamentoCartao().constroi();
		Pedido pedido2 = PedidoBuilder.umPedido().paraUmCliente(joao).cancela().constroi();
		Pedido pedido3 = PedidoBuilder.umPedido().paraUmCliente(jose).comPagamentoCartao().constroi();


		pedidoRepository.salva(pedido1 );
		pedidoRepository.salva(pedido2 );
		pedidoRepository.salva(pedido3 );

		// chamando o método para testar
		List<Pedido> pedidosPagosComCartao = pedidoRepository.comPagamentoCartao();

		// garantindo que a query funcionou
		Assert.assertEquals(2, pedidosPagosComCartao.size() );


		//verificação
		Assert.assertEquals("Joao da Silva", pedidosPagosComCartao.get(0).getCliente().getNome() );
		Assert.assertEquals("José da Silva", pedidosPagosComCartao.get(1).getCliente().getNome() );

    }

}