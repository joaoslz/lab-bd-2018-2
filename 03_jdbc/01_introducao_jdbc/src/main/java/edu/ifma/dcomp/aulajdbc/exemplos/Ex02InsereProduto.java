package edu.ifma.dcomp.aulajdbc.exemplos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ex02InsereProduto {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost/estoque?useSSL=false";

		try (Connection connection = DriverManager.getConnection(url, "root", "root")) {

			try (Statement statement = connection.createStatement()) {

				String insert = "Insert INTO produtos (nome, descricao) " +
						"VALUES ('Computador Core i7', 'Computador Core i7 com 8 GB de RAM')";
				boolean resultado = statement.execute(insert,
						                  Statement.RETURN_GENERATED_KEYS);
				
				ResultSet keys = statement.getGeneratedKeys();
				
				if (keys.next() ) {
					System.out.println(keys.getInt(1 ) );
				}
				

			}
		}

	}

}
