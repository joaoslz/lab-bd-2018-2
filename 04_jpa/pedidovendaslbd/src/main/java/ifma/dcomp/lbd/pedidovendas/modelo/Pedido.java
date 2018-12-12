package ifma.dcomp.lbd.pedidovendas.modelo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "instante_criacao")
    private LocalDateTime instanteCriacao;


    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto = BigDecimal.ZERO;


    @Column(name = "valor_frete")
    private BigDecimal valorFrete = BigDecimal.ZERO;


    @Column(columnDefinition = "text")
    private String observacoes;


    @OneToOne(mappedBy = "pedido")
    private Pagamento pagamento;


    @OneToMany(mappedBy = "id.pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemPedido> itens = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido", nullable = false, length = 20)
    private StatusPedido status = StatusPedido.ORCAMENTO;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Embedded
    private EnderecoEntrega enderecoEntrega;


    @PrePersist
    private void prePersist() {
        this.instanteCriacao = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getInstanteCriacao() {
        return instanteCriacao;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }


    public Set<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(Set<ItemPedido> itens) {
        this.itens = itens;
    }

    public boolean isOrcamento() {
        return StatusPedido.ORCAMENTO.equals(this.getStatus());
    }

    @Transient
    public boolean isNovo() {
        return getId() == null;
    }

    @Transient
    public boolean isExistente() {
        return !isNovo();
    }


    @Transient
    public boolean isEmitido() {
        return StatusPedido.EMITIDO.equals(this.getStatus());
    }

    @Transient
    public boolean isNaoEmissivel() {
        return !this.isEmissivel();
    }

    @Transient
    public boolean isEmissivel() {
        return this.isExistente() && this.isOrcamento();
    }

    @Transient
    public boolean podeSerCancelado() {
        return this.isExistente() && !this.isCancelado();
    }

    @Transient
    private boolean isCancelado() {
        return StatusPedido.CANCELADO.equals(this.getStatus());
    }

    @Transient
    public boolean naoPodeSerCancelado() {
        return !this.podeSerCancelado();
    }

    @Transient
    public boolean isAlteravel() {
        return this.isOrcamento();
    }

    @Transient
    public boolean isNaoAlteravel() {
        return !this.isAlteravel();
    }


    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public EnderecoEntrega getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(EnderecoEntrega enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
