package edu.ifma.dcomp.aulajdbc.exemplos.tipoblob;

import java.io.*;
import java.sql.*;

public class Ex01EscrevendoTipoBlob {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost/estoquedb?useSSL=false";

		try (Connection connection = DriverManager.getConnection(url, "root", "root")) {

			String sql = "select foto from produto where id=2";

			try (Statement statement = connection.createStatement() ) {

				ResultSet resultSet = statement.executeQuery(sql );

				try {
					File fotoDoBanco = new File("foto_do_banco.png" );
					FileOutputStream fileOutputStream = new FileOutputStream(fotoDoBanco);

                    //lendo o tipo blob
					if (resultSet.next() ) {
						InputStream input = resultSet.getBinaryStream("foto");

						byte [] buffer = new byte[100000];
						while (input.read(buffer) > 0) {
							fileOutputStream.write(buffer );
						}

					}

					System.out.println("\nFoto Salva no arquivo " +
							      fotoDoBanco.getAbsolutePath() );


				} catch (FileNotFoundException e) {
					System.out.println("Problema ao carregar a foto!");
					e.printStackTrace();
				}

				catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

}
