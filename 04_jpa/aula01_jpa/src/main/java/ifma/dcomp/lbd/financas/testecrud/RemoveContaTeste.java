package ifma.dcomp.lbd.financas.testecrud;

import ifma.dcomp.lbd.financas.conexao.JPAUtil;
import ifma.dcomp.lbd.financas.modelo.Conta;

import javax.persistence.EntityManager;

public class RemoveContaTeste {

    public static void main(String[] args) {

        EntityManager manager = new JPAUtil().getEntityManager();


        manager.getTransaction().begin();

        Conta conta = manager.find(Conta.class, 2);
        manager.remove(conta );
        // ...
        manager.persist(conta);
        conta.setTitular("Fulano da Silva");

        manager.getTransaction().commit();



        manager.close();


    }
}
