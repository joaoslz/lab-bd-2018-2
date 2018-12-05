package ifma.dcomp.lbd.financas.testecrud;

import ifma.dcomp.lbd.financas.conexao.JPAUtil;
import ifma.dcomp.lbd.financas.modelo.Conta;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class BuscaContasTeste {

    public static void main(String[] args) {

        EntityManager manager = new JPAUtil().getEntityManager();

 /*       Query query = manager.createQuery("SELECT c FROM Conta c");
        List contas = query.getResultList();

        contas.forEach(System.out::println );

       ((Conta) contas.get(0)).getTitular();

 */


/*
        TypedQuery<Conta> query = manager.createQuery("SELECT c FROM Conta c", Conta.class);
        List<Conta> contas = query.getResultList();

        contas.get(0).getTitular();
*/

        List<Conta> contas = manager
                .createQuery("SELECT c FROM Conta c", Conta.class)
                .getResultList();

        for (Conta conta: contas ) {
            System.out.println(conta );
        }

        contas.forEach(conta -> System.out.println(conta) );

        contas.forEach(System.out::println );

        System.out.println( contas.get(0).getTitular() );


        manager.close();


    }
}
