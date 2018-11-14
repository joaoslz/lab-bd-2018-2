package edu.ifma.dcomp.aulajdbc.exemplos;

import edu.ifma.dcomp.aulajdbc.estoque.dao.ProdutoDAO;
import edu.ifma.dcomp.aulajdbc.estoque.infra.ConnectionPool;
import edu.ifma.dcomp.aulajdbc.estoque.modelo.Produto;

import java.sql.Connection;
import java.sql.SQLException;

public class TesteProdutoDAO {

    public static void main(String[] args) throws SQLException {

        Connection conexao = new ConnectionPool().getConexao();

        ProdutoDAO produtoDAO = new ProdutoDAO(conexao);

        Produto produto = new Produto("Lápis de cor", "Lapis de escrita macia");

        final Produto produtoBD = produtoDAO.salva(produto);

        System.out.println("Id do novo produto: "
                + produto.getId() );

        System.out.println("\n" + produtoDAO.recuperaProdutos() );

    }
}
