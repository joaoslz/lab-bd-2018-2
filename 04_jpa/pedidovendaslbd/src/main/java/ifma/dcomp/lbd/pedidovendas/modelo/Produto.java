package ifma.dcomp.lbd.pedidovendas.modelo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "produto")
public class Produto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @Column(unique = true)
    private String sku;

    @Column(name = "preco_atual")
    private BigDecimal precoAtual;

    @Column(name = "quantidade_estoque")
    private Integer quantidadeEstoque = 0;

    @ManyToMany
    @JoinTable(name = "produto_categoria",
               joinColumns = @JoinColumn(name = "produto_id"),
               inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void adiciona(Categoria categoria ) {
        categorias.add(categoria );
    }

    public void setCategorias(Categoria... categorias) {
        this.categorias = Arrays.asList(categorias );
    }


    public void adicionaEstoque(Integer quantidade ) {
        if ( quantidade < 0 ) {
            throw new IllegalArgumentException("Quantidade Inválida: " + quantidade);
        }

        this.quantidadeEstoque = this.quantidadeEstoque + quantidade;
    }


 /*   public void baixarEstoque(Integer quantidade) throws NegocioException {
        int novaQuantidade = this.getQuantidadeEstoque() - quantidade;

        if (novaQuantidade < 0) {

            throw new NegocioException("Não há disponibilidade no estoque de "
                    + quantidade + " itens do produto " + this.getSku() + ".");
        }

        this.quantidadeEstoque = novaQuantidade;
    }
*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto)) return false;
        Produto produto = (Produto) o;
        return Objects.equals(getId(), produto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
