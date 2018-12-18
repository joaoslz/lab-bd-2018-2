package ifma.dcomp.lbd.pedidovendas.servico;


import ifma.dcomp.lbd.pedidovendas.modelo.Pedido;
import ifma.dcomp.lbd.pedidovendas.modelo.StatusPedido;
import ifma.dcomp.lbd.pedidovendas.repositorio.PedidoRepository;
import ifma.dcomp.lbd.pedidovendas.util.EMFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class CancelamentoPedidoService  {



	private EstoqueService estoqueService;

    private final PedidoRepository repositorio;
	private final EntityManager manager;

	public CancelamentoPedidoService( ) {

		this.manager = new EMFactory().getEntityManager();
		this.repositorio = new PedidoRepository(manager );
	}



	public Pedido cancela(Pedido pedido) throws NegocioException {



		pedido = this.repositorio.porId(pedido.getId());
		
		if (pedido.naoPodeSerCancelado() ) {

			throw new NegocioException("Pedido não pode ser cancelado no status "
					+ pedido.getStatus().getDescricao() + ".");

		}


		EntityTransaction transacao = this.manager.getTransaction();
		transacao.begin();

		if (pedido.isEmitido()) {
			this.estoqueService.retornarItensParaOEstoque(pedido );
		}
		
		pedido.setStatus(StatusPedido.CANCELADO );
		
		pedido = this.repositorio.salva(pedido );

		transacao.commit();
		
		return pedido;
	}

}