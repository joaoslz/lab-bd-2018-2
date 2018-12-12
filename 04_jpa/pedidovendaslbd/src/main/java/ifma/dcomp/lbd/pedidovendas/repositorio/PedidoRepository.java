package ifma.dcomp.lbd.pedidovendas.repositorio;

import ifma.dcomp.lbd.pedidovendas.modelo.PagamentoCartao;
import ifma.dcomp.lbd.pedidovendas.modelo.Pedido;
import ifma.dcomp.lbd.pedidovendas.modelo.StatusPedido;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

public class PedidoRepository {


    private final EntityManager manager;


    public PedidoRepository(EntityManager manager) {
        this.manager = manager;
    }


    public Pedido porId(Integer id) {
        return this.manager.find(Pedido.class, id);
    }


    public Pedido salva(Pedido pedido) {
       return manager.merge( pedido );
    }


    public List<Pedido> finalizados() {

        return manager
                .createQuery("from Pedido p where p.status = :statusPedido", Pedido.class)
                .setParameter("statusPedido", StatusPedido.EMITIDO)
                .getResultList();

    }


    public List<Pedido> comPagamentoCartao() {

        return manager
                .createQuery("Select p From Pedido p where TYPE(p.pagamento) IN :tipoPagamento", Pedido.class)
                .setParameter("tipoPagamento", Arrays.asList(PagamentoCartao.class) )
                .getResultList();

    }


}
