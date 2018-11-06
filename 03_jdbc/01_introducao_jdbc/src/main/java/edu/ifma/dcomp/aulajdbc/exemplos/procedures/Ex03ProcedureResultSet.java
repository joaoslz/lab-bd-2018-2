package edu.ifma.dcomp.aulajdbc.exemplos.procedures;

import java.sql.*;

public class Ex03ProcedureResultSet {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost/estoquedb?useSSL=false";

		try (Connection connection = DriverManager.getConnection(url, "root", "root")) {

			try ( CallableStatement statement =
						  connection
								  .prepareCall("{ call sp_get_produtos_em_estoque()}") ) {

			   // chamando a procedure
				statement.execute();

				// recuperando o cursor da procedure por intermédio de um resultset
                ResultSet resultSet = statement.getResultSet();

                // chamando depois da procedure
				exibeProdutosEmEstoque(resultSet );

			}
		}

	}

	private static void exibeProdutosEmEstoque(ResultSet resultSet) throws SQLException {


		while (resultSet.next() ) {


			String registro = String.format("( Produto_id: %d, Quantidade: %d, Valor Unitário: %.2f ) ",
                                  resultSet.getInt("id_produto"),
                                  resultSet.getInt("qtde"),
                                  resultSet.getBigDecimal("valor_unitario")      );

			System.out.println(registro );
		}
	}

}
