package ifma.dcomp.lbd.pedidovendas.modelo;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;


    @ManyToOne
    @JoinColumn(name = "categoriapai_id")
    private Categoria categoriaPai;

    @OneToMany(mappedBy = "categoriaPai", cascade = CascadeType.ALL)
    private List<Categoria> subCategorias = new ArrayList<>();



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

    public Categoria getCategoriaPai() {
        return this.categoriaPai;
    }

    public void setCategoriaPai(Categoria categoriaPai) {
        this.categoriaPai = categoriaPai;
    }

    public List<Categoria> getSubCategorias() {
        return Collections.unmodifiableList(this.subCategorias);
    }

    public void setSubCategorias(List<Categoria> subCategorias) {
        this.subCategorias = subCategorias;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(getId(), categoria.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


    public void adicionaSubCategorias(Categoria... categorias) {

        for (Categoria categoria : categorias ) {
            this.subCategorias.add(categoria );
        }

        // this.subCategorias.addAll(Arrays.asList(categorias ) );

    }
}
