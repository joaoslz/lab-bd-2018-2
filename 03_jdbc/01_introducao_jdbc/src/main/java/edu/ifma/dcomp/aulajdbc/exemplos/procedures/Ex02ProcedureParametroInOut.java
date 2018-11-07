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
				statement.setInt(2, 680 );
				statement.registerOutParameter(2, Types.INTEGER);

				// chamando antes da procedure
				// exibeEstoqueMaximoDoProdutoDeId(2, statement );

				statement.execute();

				String parametroOut = statement.getString(2);
				System.out.println("Novo Estoque Máximo " + parametroOut );

				// chamando depois da procedure
				//exibeEstoqueMaximoDoProdutoDeId(2, statement );

			}
		}

	}

	private static void exibeEstoqueMaximoDoProdutoDeId(int id, Statement statement) throws SQLException {


		//ResultSet resultSet = statement.executeQuery("select estoque_maximo from produto where id = " + id);
		ResultSet resultSet = statement.getResultSet() ;


		if (resultSet.next() ) {
			String resultado = String.format("( Produto: %d, estoque_máximo: %d ) ",
					                                     id, resultSet.getInt("novoEstoqueMaximo") );
			System.out.println(resultado );
		}
	}

}
