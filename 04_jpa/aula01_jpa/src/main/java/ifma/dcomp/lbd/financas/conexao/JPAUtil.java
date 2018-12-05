package ifma.dcomp.lbd.financas.conexao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

   private static final EntityManagerFactory factory =
            Persistence
              .createEntityManagerFactory("teste-jpa");


   public EntityManager getEntityManager() {
       return factory.createEntityManager();
   }

   public static void close() {
        factory.close();
   }

}
