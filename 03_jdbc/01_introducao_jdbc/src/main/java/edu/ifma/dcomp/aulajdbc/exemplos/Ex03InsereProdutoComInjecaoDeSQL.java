package edu.ifma.dcomp.aulajdbc.exemplos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ex03InsereProdutoComInjecaoDeSQL {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost/estoque?useSSL=false";

		try (Connection connection = DriverManager.getConnection(url, "root", "root")) {

			String sql = "Insert INTO produtos (nome, descricao) VALUES (?, ?)";

			try ( PreparedStatement statement = connection
					                           .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) ) {
				
				String nome = "Computador Core i7";
				String descricao = "Computador Core i7 com 8 GB de RAM, tela 10'pol";

            /*	String insert = "Insert INTO produtos (nome, descricao) VALUES "
						+ "('"+ nome + "', '" + descricao  + "')";   */
				
				statement.setString(1, nome );
				statement.setString(2, descricao ); 
				
				statement.execute();
				
				ResultSet keys = statement.getGeneratedKeys();
				
				if (keys.next() ) {
					System.out.println(keys.getInt(1 ) );
				}
				

			}
		}

	}

}
