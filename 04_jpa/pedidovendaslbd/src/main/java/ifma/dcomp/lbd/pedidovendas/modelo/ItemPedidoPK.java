package ifma.dcomp.lbd.pedidovendas.modelo;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemPedidoPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemPedidoPK)) return false;
        ItemPedidoPK that = (ItemPedidoPK) o;
        return Objects.equals(getProduto(), that.getProduto()) &&
                Objects.equals(getPedido(), that.getPedido());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getProduto(), getPedido());
    }
}
