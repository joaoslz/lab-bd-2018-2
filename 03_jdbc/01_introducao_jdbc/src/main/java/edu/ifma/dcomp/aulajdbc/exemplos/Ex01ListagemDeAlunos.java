package edu.ifma.dcomp.aulajdbc.exemplos;

import java.sql.*;

public class Ex01ListagemDeAlunos {

    public static void main(String[] args)  throws SQLException {

        String url = "jdbc:mysql://localhost/cursodb?useSSL=false";
        String user = "root";
        String senha = "root";

        try ( Connection conexao = DriverManager
                    .getConnection(url, user, senha) ) {


            try (Statement statement = conexao.createStatement()) {
                ResultSet resultSet = statement
                        .executeQuery("select * from aluno");

                while (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String email = resultSet.getString("email");

                    System.out.println(nome);
                    System.out.println(email);

                }


                System.out.println("Conexão realizada com sucesso");
                resultSet.close();
                // statement.close();
                // conexao.close();
            }
        }


    }
}
