package ifma.dcomp.lbd.financas.testecrud;

import ifma.dcomp.lbd.financas.conexao.JPAUtil;
import ifma.dcomp.lbd.financas.modelo.Conta;

import javax.persistence.EntityManager;
import java.util.List;

public class BuscaEAlteraContasTeste {

    public static void main(String[] args) {

        EntityManager manager = new JPAUtil().getEntityManager();

         manager.getTransaction().begin();

         List<Conta> contas = manager
                .createQuery("SELECT c FROM Conta c", Conta.class)
                .getResultList();

        contas.forEach(conta -> conta.setBanco("Bradesco"));

        manager.getTransaction().commit();


        manager.close();


    }
}
