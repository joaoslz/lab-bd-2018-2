package ifma.dcom.lbd.financas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import ifma.dcom.lbd.financas.dao.ContaDAO;
import ifma.dcom.lbd.financas.infra.BD;
import ifma.dcom.lbd.financas.infra.ConnectionFactory;
import ifma.dcom.lbd.financas.modelo.Conta;

public class TesteJDBCMySQL {

	public static void main(String[] args) throws SQLException  {

		Conta conta = new Conta();
		conta.setTitular("José Ferreira");
		conta.setBanco("Itau");
		conta.setAgencia("0063");
		conta.setNumero("432561");

		Connection conexao = new ConnectionFactory().getConnectionMySQL();
		conexao.setAutoCommit(false);

		ContaDAO dao = new ContaDAO(conexao );
		dao.adiciona(conta);


		List<Conta> lista = dao.lista();

		for (Conta c : lista) {
			System.out.println(c.getTitular() );
		}

		conexao.commit();
		conexao.close();
	}

}
