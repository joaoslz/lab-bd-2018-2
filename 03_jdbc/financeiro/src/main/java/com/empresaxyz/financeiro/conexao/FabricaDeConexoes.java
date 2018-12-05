package com.empresaxyz.financeiro.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabricaDeConexoes {

    private final String url = "jdbc:mysql://localhost/estoque";
    private final String usuario = "root";
    private final String senha = "root";

    private final Connection conexao;

    public FabricaDeConexoes() {

        try {
            conexao = DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            throw new RuntimeException("Não foi conectar com o banco de dados ");
        }

    }



}
