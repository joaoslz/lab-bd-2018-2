package ifma.dcomp.lbd.financas.testecrud;

import ifma.dcomp.lbd.financas.conexao.JPAUtil;
import ifma.dcomp.lbd.financas.modelo.Conta;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class InsereContaTeste {

    public static void main(String[] args) {

        Conta conta = new Conta();
        conta.setTitular("José da Silva");
        conta.setAgencia("1289");
        conta.setNumero("958-5");

        EntityManager manager = new JPAUtil().getEntityManager();

        manager.getTransaction().begin();
        manager.persist(conta );
        manager.getTransaction().commit();


        manager.close();


    }
}
