package ifma.dcomp.lbd.pedidovendas.modelo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @EmbeddedId
    private ItemPedidoPK id;

    private Integer quantidade;

    private BigDecimal desconto;

    private BigDecimal preco;


    @PrePersist
    private void definePreco() {
        this.preco = id.getProduto().getPrecoAtual();
    }

    public ItemPedidoPK getId() {
        return id;
    }

    public void setId(ItemPedidoPK id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Produto getProduto() {
        return id.getProduto();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemPedido)) return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }


    public void retornaParaOEstoque() {
        this.id.getProduto().adicionaEstoque(this.quantidade );
    }
}
