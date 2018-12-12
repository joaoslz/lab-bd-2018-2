package ifma.dcomp.lbd.pedidovendas.modelo;


import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String email;

    @Column(name = "doc_receita_federal")
    private String documentoReceitaFederal;


    @Enumerated(EnumType.STRING)
    private TipoCliente tipo;


    // tutorial: https://en.wikibooks.org/wiki/Java_Persistence/ElementCollection
    @ElementCollection
    @CollectionTable(
            name = "telefones",
            joinColumns = @JoinColumn(name = "cliente_id", nullable = false)
    )
    @Column(name = "numero", nullable = false)
    private Set<String> telefones = new LinkedHashSet<>();
    //private List<String> telefones;


    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Endereco> enderecos = new ArrayList<>();


    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();


    public Cliente() {  }

    public Cliente(Endereco endereco) {
        this.enderecos.add(endereco );
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumentoReceitaFederal() {
        return documentoReceitaFederal;
    }

    public void setDocumentoReceitaFederal(String documentoReceitaFederal) {
            this.documentoReceitaFederal = documentoReceitaFederal;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public void setTipo(TipoCliente tipo) {
        this.tipo = tipo;
    }

    public List<Endereco> getEnderecos() {
        return Collections.unmodifiableList(enderecos );
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public void adicionaUmEndereco(Endereco endereco) {
        this.enderecos.add(endereco );
    }

    public Set<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<String> telefones) {
        this.telefones = telefones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(getId(), cliente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}