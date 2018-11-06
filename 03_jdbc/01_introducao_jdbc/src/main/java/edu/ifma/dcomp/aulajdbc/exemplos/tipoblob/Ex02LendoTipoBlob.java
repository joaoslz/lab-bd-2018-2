package edu.ifma.dcomp.aulajdbc.exemplos.tipoblob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Ex02LendoTipoBlob {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost/estoquedb?useSSL=false";

		try (Connection connection = DriverManager.getConnection(url, "root", "root")) {

			String sqlUpdate = "update produto set foto=? where id=2";

			try (PreparedStatement statement = connection.prepareStatement(sqlUpdate) ) {

				// configurando a foto do produto
				//File novaFoto = new File("doc01.pdf");
				File novaFoto = new File("produto01.png");

				try {
					FileInputStream streamDaFoto = new FileInputStream(novaFoto);
					statement.setBinaryStream(1, streamDaFoto );

				} catch (FileNotFoundException e) {
					System.out.println("Problema ao carregar a foto!");
					e.printStackTrace();
				}
               // salva a foto no banco
				statement.executeUpdate();

				System.out.println("Foto salva com sucesso");



			}
		}

	}

}
