package ifma.dcomp.lbd.pedidovendas.teste;

import ifma.dcomp.lbd.pedidovendas.modelo.Categoria;
import ifma.dcomp.lbd.pedidovendas.modelo.Produto;
import ifma.dcomp.lbd.pedidovendas.util.EMFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;

public class TesteProduto {

	public static void main(String[] args) {


		// instanciamos e persistimos um produto
		Produto produto = new Produto();
		produto.setNome("Caderno de 10 materias");
		produto.adicionaEstoque(20 );

		produto.setSku("CAD00124" );
		produto.setPrecoAtual(new BigDecimal(12.91) );


		EMFactory emFactory = new EMFactory();
		EntityManager manager = emFactory.getEntityManager();

		EntityTransaction transacao = manager.getTransaction();
		transacao.begin();

	    manager.persist(produto);

		transacao.commit();

		manager.close();
		emFactory.close();
	}
	
}