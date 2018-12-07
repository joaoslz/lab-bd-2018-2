package ifma.dcomp.lbd.pedidovendas;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produto")
public class Produto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(name = "preco_atual")
    private BigDecimal precoAtual;

    @Column(name = "quantidade_estoque")
    private Integer quantidaEstoque;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @ManyToMany
    @JoinTable(
          name = "produto_categoria",
          joinColumns = @JoinColumn(name = "produto_id"),
          inverseJoinColumns = @JoinColumn(name ="categoria_id")
    )
    private List<Categoria> categorias = new ArrayList<>();


    @PrePersist
    private void prePersit() {
        this.dataCadastro = LocalDateTime.now();
    }


    public void adiciona(Categoria categoria ) {
        this.categorias.add(categoria );
    }

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

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }

    public Integer getQuantidaEstoque() {
        return quantidaEstoque;
    }

    public void setQuantidaEstoque(Integer quantidaEstoque) {
        this.quantidaEstoque = quantidaEstoque;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }


    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
}
