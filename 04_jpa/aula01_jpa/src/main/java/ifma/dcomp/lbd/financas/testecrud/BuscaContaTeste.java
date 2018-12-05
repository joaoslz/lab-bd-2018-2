package ifma.dcomp.lbd.financas.testecrud;

import ifma.dcomp.lbd.financas.conexao.JPAUtil;
import ifma.dcomp.lbd.financas.modelo.Conta;

import javax.persistence.EntityManager;

public class BuscaContaTeste {

    public static void main(String[] args) {

        EntityManager manager = new JPAUtil().getEntityManager();



        Conta conta = manager.find(Conta.class, 1);

        System.out.println(conta.getBanco() );

        manager.getTransaction().begin();
        conta.setBanco("Itau" );
        manager.getTransaction().commit();

        System.out.println(conta.getBanco() );



        System.out.println(conta );


        manager.close();


    }
}
