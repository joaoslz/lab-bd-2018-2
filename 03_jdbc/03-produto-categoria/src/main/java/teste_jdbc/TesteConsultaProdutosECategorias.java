package teste_jdbc;

import ifma.dcomp.lbd.aula_jdbc.dao.CategoriasDAO;
import ifma.dcomp.lbd.aula_jdbc.dao.ProdutoDAO;
import ifma.dcomp.lbd.aula_jdbc.infra.ConnectionPool;
import ifma.dcomp.lbd.aula_jdbc.modelo.Categoria;
import ifma.dcomp.lbd.aula_jdbc.modelo.Produto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TesteConsultaProdutosECategorias {

    public static void main(String[] args) throws SQLException {


        try (Connection conexao = new ConnectionPool().getConexao()) {

            ProdutoDAO produtoDAO = new ProdutoDAO(conexao);
            CategoriasDAO categoriasDAO = new CategoriasDAO(conexao);

            List<Produto> produtos = produtoDAO.lista();

            //produtos.forEach( produto -> System.out.println(produto ) );
            produtos.forEach(System.out::println);

            System.out.println("################################");

            final List<Categoria> categoriasComProdutos = categoriasDAO.buscaCategoriasComProdutos();


            categoriasComProdutos.forEach(c -> {
                        System.out.println(c.getNome());
                        c.getProdutos()
                                .forEach(p -> System.out.println("\t" + p));
                    }

            );
        }


    }
}

