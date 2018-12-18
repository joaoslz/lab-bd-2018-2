package ifma.dcomp.lbd.pedidovendas.teste;

import ifma.dcomp.lbd.pedidovendas.modelo.*;
import ifma.dcomp.lbd.pedidovendas.util.EMFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TestePedido {

	public static void main(String[] args) {


		EMFactory emFactory = new EMFactory();
		EntityManager manager = emFactory.getEntityManager();

		EntityTransaction transacao = manager.getTransaction();
		transacao.begin();
		
		Cliente cliente = manager.find(Cliente.class, 1);
		Produto produto = manager.find(Produto.class, 1);

		EnderecoEntrega enderecoEntrega = new EnderecoEntrega();
		enderecoEntrega.setLogradouro("Rua da Esperança");
		enderecoEntrega.setNumero("456");
		enderecoEntrega.setCidade("São Luis");
		enderecoEntrega.setUf("MA");
		enderecoEntrega.setCep("65123-567");
		
		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);

		pedido.setStatus(StatusPedido.ORCAMENTO);
		pedido.setValorDesconto(BigDecimal.ZERO);
		pedido.setValorFrete(BigDecimal.ZERO);

		pedido.setEnderecoEntrega(enderecoEntrega);
		
		ItemPedido item = new ItemPedido();

		item.getId().setProduto(produto );
		item.setQuantidade(10);
		//item.setPreco(new BigDecimal(2.32));
		item.getId().setPedido(pedido);
		
		pedido.getItens().add(item);
		
		manager.persist(pedido);
		
		transacao.commit();

		manager.close();
		emFactory.close();

	}
	
}