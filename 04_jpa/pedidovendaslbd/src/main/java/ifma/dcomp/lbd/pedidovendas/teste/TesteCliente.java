package ifma.dcomp.lbd.pedidovendas.teste;

import ifma.dcomp.lbd.pedidovendas.modelo.Cliente;
import ifma.dcomp.lbd.pedidovendas.modelo.Endereco;
import ifma.dcomp.lbd.pedidovendas.util.EMFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class TesteCliente {

	public static void main(String[] args) {

		EMFactory emFactory = new EMFactory();
		EntityManager manager = emFactory.getEntityManager();

		EntityTransaction transacao = manager.getTransaction();
		transacao.begin();
		
		Cliente cliente = new Cliente();
		cliente.setNome("João de Sousa");
		cliente.setEmail("joao@desousa.com");
		cliente.setDocumentoReceitaFederal("123.123.123-12");

		Endereco endereco = new Endereco();
		endereco.setLogradouro("Rua da Felicidade");
		endereco.setNumero("123");
		endereco.setCidade("São Luís");
		endereco.setUf("MA");
		endereco.setCep("65064-512");
		endereco.setCliente(cliente);
		
		//cliente.getEnderecos().add(endereco);
		cliente.adicionaUmEndereco(endereco );
		
		manager.persist(cliente);
		
		transacao.commit();

		manager.close();
		emFactory.close();

	}
	
}