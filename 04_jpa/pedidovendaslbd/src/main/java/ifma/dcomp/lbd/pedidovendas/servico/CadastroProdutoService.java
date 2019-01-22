package ifma.dcomp.lbd.pedidovendas.servico;

import ifma.dcomp.lbd.pedidovendas.modelo.Produto;
import ifma.dcomp.lbd.pedidovendas.repositorio.ProdutoRepository;
import ifma.dcomp.lbd.pedidovendas.util.EMFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class CadastroProdutoService {

    private final ProdutoRepository repositorio;
    private final EntityManager manager;

    public CadastroProdutoService() {
        this.manager = new EMFactory().getEntityManager();
        repositorio = new ProdutoRepository(manager );
    }


    public Produto salva(Produto produto) throws NegocioException {

        Produto produtoExistente = repositorio.buscaPorSku(produto.getSku() );

        if ( produtoExistente != null && !produtoExistente.equals(produto) ) {
            throw new NegocioException("Já existe um produto com o SKU informado.");
        }
        manager.getTransaction().begin();
        produto = repositorio.salva(produto );
        manager.getTransaction().commit();

        // envia email
        // fazer um log para auditoria
        return produto;

    }

    public Produto buscaPorSku(String sku) {
        return repositorio.buscaPorSku(sku);
    }

    public Produto porId(Integer id) {
        return repositorio.porId(id);
    }

    public List<Produto> porNome(String nome) {
          return repositorio.porNome(nome);
    }
}
