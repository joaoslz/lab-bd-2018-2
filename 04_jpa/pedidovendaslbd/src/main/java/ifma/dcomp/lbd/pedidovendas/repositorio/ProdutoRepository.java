package ifma.dcomp.lbd.pedidovendas.repositorio;


import ifma.dcomp.lbd.pedidovendas.modelo.Produto;
import ifma.dcomp.lbd.pedidovendas.servico.NegocioException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

public class ProdutoRepository {

    private final EntityManager manager;

    public ProdutoRepository(EntityManager manager) {
        this.manager = manager;
    }

    public Produto salva(Produto produto) {

        Produto produtoSalvo = manager.merge(produto );

        return produtoSalvo;
    }

    public void remover(Produto produto) throws NegocioException {
        try {
            produto = porId(produto.getId() );
            manager.remove(produto);
            manager.flush();

        } catch (PersistenceException e) {
            throw new NegocioException("Produto não pode ser excluído.");
        }
    }

    public Produto buscaPorSku(String sku) {
        try {
            return manager.createQuery("from Produto where upper(sku) = :sku",
                                        Produto.class)
                    .setParameter("sku", sku.toUpperCase())
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }


    public Produto porId(Integer id) {
        return manager.find(Produto.class, id);
    }

    public List<Produto> porNome(String nome) {
        return this.manager
                .createQuery("from Produto where upper(nome) like :nome", Produto.class)
                .setParameter("nome", nome.toUpperCase() + "%")
                .getResultList();
    }
}
