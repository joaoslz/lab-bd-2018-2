package ifma.dcomp.lbd.pedidovendas.teste;


import ifma.dcomp.lbd.pedidovendas.util.EMFactory;

import javax.persistence.EntityManager;

public class ProdutoTeste {

    public static void main(String[] args) {
        EMFactory emFactory = new EMFactory();

        EntityManager manager = emFactory.getEntityManager();

        manager.close();
        emFactory.close();



    }
}
