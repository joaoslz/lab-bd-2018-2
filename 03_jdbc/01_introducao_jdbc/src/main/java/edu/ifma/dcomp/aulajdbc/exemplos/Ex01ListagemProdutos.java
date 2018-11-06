package edu.ifma.dcomp.aulajdbc.exemplos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ex01ListagemProdutos {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost/estoque?useSSL=false";

		Connection connection = DriverManager.getConnection(url, "root", "root");

		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("select * from produtos");

		while (resultSet.next()) {

			int id = resultSet.getInt("id");
			String nome = resultSet.getString("nome");
			
			
			String registro = String.format("ID %d \t Nome: %s", id, nome );
			
			System.out.println( registro );

		}
		
		resultSet.close();
		statement.close();
		connection.close();

	}

}
