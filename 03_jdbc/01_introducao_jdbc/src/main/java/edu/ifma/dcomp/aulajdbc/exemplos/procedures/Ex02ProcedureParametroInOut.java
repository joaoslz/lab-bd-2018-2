package edu.ifma.dcomp.aulajdbc.exemplos.procedures;

import java.sql.*;

public class Ex02ProcedureParametroInOut {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost/estoquedb?useSSL=false";

		try (Connection connection = DriverManager.getConnection(url, "root", "root")) {

			try ( CallableStatement statement =
						  connection
								  .prepareCall("{ call sp_aumenta_estoque_maximo(?, ?)}") ) {

				// define os parâmetros
				statement.setInt(1, 2 );
				statement.setInt(2, 600 );

				// chamando antes da procedure
				exibeEstoqueMaximoDoProdutoDeId(2, connection.createStatement() );

				statement.execute();

				// chamando depois da procedure
				exibeEstoqueMaximoDoProdutoDeId(2, connection.createStatement() );

			}
		}

	}

	private static void exibeEstoqueMaximoDoProdutoDeId(int id, Statement statement) throws SQLException {

		ResultSet resultSet = statement.executeQuery("select estoque_maximo from produto where id = " + id);




		if (resultSet.next() ) {
			String resultado = String.format("( Produto: %d, estoque_máximo: %d ) ",
					                                     id, resultSet.getInt("estoque_maximo") );
			System.out.println(resultado );
		}
	}

}
