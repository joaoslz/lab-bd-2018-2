package ifma.dcomp.lbd.aula_jdbc.dao;

import ifma.dcomp.lbd.aula_jdbc.modelo.Categoria;
import ifma.dcomp.lbd.aula_jdbc.modelo.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

	private Connection conexao;

	public ProdutoDAO(Connection connection) {
		this.conexao = connection;
	}

	public Produto salva(Produto produto) throws SQLException {

		String sql = "insert into produtos (nome, descricao) values (?, ?)";
		try (PreparedStatement statement = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			statement.setString(1, produto.getNome());
			statement.setString(2, produto.getDescricao());

			statement.execute();

			try (ResultSet keys = statement.getGeneratedKeys()) {
				keys.next();
				int id = keys.getInt(1);
				produto.setId(id);
			}
		}
		return produto;

	}

	public List<Produto> lista() throws SQLException {

		String sql = "select * from produtos";

		try (PreparedStatement statment = conexao.prepareStatement(sql)) {
			/*stmt.execute();
			ResultSet resultSet = stmt.getResultSet();*/
			
			ResultSet resultSet = statment.executeQuery(sql);
			
			List<Produto> produtos = new ArrayList<>();
			
			while (resultSet.next()) {

				String nome = resultSet.getString("nome");
				String descricao = resultSet.getString("descricao");
				int id = resultSet.getInt("id");
				Produto p = new Produto(nome, descricao);
				p.setId(id);
				
				produtos.add(p);
			}
			return produtos;
		}

	}

	public List<Produto> buscaProdutosDa(Categoria categoria) throws SQLException {

		String sql = "select distinct * from produtos where categoria_id = " + categoria.getId() ;

		try (PreparedStatement statment = conexao.prepareStatement(sql)) {

			ResultSet resultSet = statment.executeQuery(sql);

			final List<Produto> produtos = new ArrayList<>();

			while (resultSet.next()) {

				String nome = resultSet.getString("nome");
				String descricao = resultSet.getString("descricao");
				int id = resultSet.getInt("id");
				Produto p = new Produto(nome, descricao);
				p.setId(id);

				produtos.add(p);
			}
			return produtos;

		}

	}

}
