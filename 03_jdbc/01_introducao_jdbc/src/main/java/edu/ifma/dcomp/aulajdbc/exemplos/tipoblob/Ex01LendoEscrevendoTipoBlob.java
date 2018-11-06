package edu.ifma.dcomp.aulajdbc.exemplos.tipoblob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

public class Ex01LendoEscrevendoTipoBlob {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost/estoquedb?useSSL=false";

		try (Connection connection = DriverManager.getConnection(url, "root", "root")) {

			String sqlUpdate = "update produto set foto=? where id=1";

			try (PreparedStatement statement = connection.prepareStatement(sqlUpdate) ) {

				// configurando a foto do produto
				File novaFoto = new File("doc01.pdf");

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
