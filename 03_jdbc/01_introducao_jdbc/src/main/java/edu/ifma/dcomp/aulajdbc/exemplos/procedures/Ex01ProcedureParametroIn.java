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
				statement.setInt(1, 2);
				statement.setInt(2, 50);
				statement.setBigDecimal(3, new BigDecimal(20.9));

				// chamando antes da procedure
				exibeEstoqueDoProdutoDeId(2, connection.createStatement() );

				statement.execute();

				// chamando depois da procedure
				exibeEstoqueDoProdutoDeId(2, connection.createStatement() );

			}
		}

	}

	private static void exibeEstoqueDoProdutoDeId(int id, Statement statement) throws SQLException {

		ResultSet resultSet = statement.executeQuery("select * from estoque where id = " + id);

		if (resultSet.next() ) {

			String resultado = String.format("Produto: %d, quantidade: %d ) ",
					                       resultSet.getInt("id_produto"),
											resultSet.getInt("qtde"));
			System.out.println(resultado );
		}
	}

}
