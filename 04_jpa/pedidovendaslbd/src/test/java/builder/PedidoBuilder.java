package builder;

import edu.ifma.dcomp.pedidovendas.modelo.*;

import java.math.BigDecimal;
import java.util.Set;

public class PedidoBuilder {

    private Pedido pedido;


    private PedidoBuilder() { }

    public static PedidoBuilder umPedido() {
        PedidoBuilder builder = new PedidoBuilder();

        builder.pedido = new Pedido();
        builder.pedido.setEnderecoEntrega(defineEnderecoPadrao() );


        return builder;
    }

    private static EnderecoEntrega defineEnderecoPadrao() {
        EnderecoEntrega enderecoEntrega = new EnderecoEntrega();
        enderecoEntrega.setLogradouro("Rua Existente");
        enderecoEntrega.setNumero("10");
        enderecoEntrega.setCep("65000-000");
        enderecoEntrega.setCidade("São Luís");
        enderecoEntrega.setUf("MA");

        return enderecoEntrega;
    }


    public PedidoBuilder paraUmCliente(Cliente cliente) {
        pedido.setCliente(cliente);
        return this;
    }

    public PedidoBuilder comItens(Set<ItemPedido> itens) {
        pedido.setItens(itens );
        return this;
    }

    public PedidoBuilder cancela() {
        // a validação desta regra de negócio é feita na camada de serviço
        pedido.setStatus(StatusPedido.CANCELADO );
        return this;
    }


    public PedidoBuilder comFrete(BigDecimal valorFrete) {
        pedido.setValorFrete(valorFrete);
        return this;
    }

    public PedidoBuilder finaliza() {
        pedido.setStatus(StatusPedido.EMITIDO );

        return this;
    }


    public Pedido constroi() {
        return pedido;
    }


    public PedidoBuilder comPagamentoCartao() {

        PagamentoCartao pagamentoCartao = new PagamentoCartao();
        pagamentoCartao.setNumeroDeParcelas((short) 1 );
        pagamentoCartao.setPedido(this.pedido );
        this.pedido.setPagamento(pagamentoCartao );

        this.pedido.setStatus(StatusPedido.EMITIDO);

        return this;
    }
}