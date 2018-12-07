package ifma.dcomp.lbd.pedidovendas;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

public class TesteJPA {


    public static void main(String[] args) {

       Produto produto = new Produto();

        produto.setNome("Caneta bic");
        produto.setPrecoAtual(new BigDecimal(5.40));
        produto.setQuantidaEstoque(10);

        Categoria categoria1 = new Categoria("Escolar ");

        categoria1.adiciona(produto );
        produto.adiciona(categoria1 );


        EntityManagerFactory factory =
                Persistence
                        .createEntityManagerFactory("pedidovendas");
        EntityManager manager = factory.createEntityManager();

       manager.getTransaction().begin();

       manager.persist(categoria1 );
       manager.persist(produto );

       manager.getTransaction().commit();

       manager.close();
       factory.close();




    }
}
