package edu.ifma.dcomp.aulajdbc.exemplos.procedures;

import java.math.BigDecimal;
import java.sql.*;

public class Ex01ProcedureParametroIn {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost/estoquedb?useSSL=false";

		try (Connection connection = DriverManager.getConnection(url, "root", "root")) {

			try ( CallableStatement statement =
				 connection
					 .prepareCall("{ call sp_atualizaEstoque(?, ?, ?)}") ) {



				// define os parâmetros
				Integer idProduto = 3;
				Integer quantidadeComprada = 50;
				BigDecimal valorUnitario = new BigDecimal(20.9);

				statement.setInt(1, idProduto);
				statement.setInt(2, quantidadeComprada);
				statement.setBigDecimal(3, valorUnitario);

				// chamando antes da procedure
				exibeEstoqueDoProdutoDeId(idProduto, connection.createStatement() );

				statement.execute();

				// chamando depois da procedure
				exibeEstoqueDoProdutoDeId(idProduto, connection.createStatement() );

			}
		}

	}

	private static void exibeEstoqueDoProdutoDeId(int id, Statement statement) throws SQLException {

		ResultSet resultSet = statement
				.executeQuery("select * from estoque where id = " + id);

		if (resultSet.next() ) {

			String resultado = String.format("Produto: %d, quantidade: %d ) ",
					                       resultSet.getInt("id_produto"),
											resultSet.getInt("qtde"));
			System.out.println(resultado );
		}
	}

}
