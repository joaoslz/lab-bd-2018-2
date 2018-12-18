package ifma.dcomp.lbd.pedidovendas.teste;

import ifma.dcomp.lbd.pedidovendas.modelo.Categoria;
import ifma.dcomp.lbd.pedidovendas.util.EMFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class TesteCategoria {

	public static void main(String[] args) {

		// instanciamos a categoria pai
		Categoria categoriaPai = new Categoria();
		categoriaPai.setNome("Papelaria" );

		// instanciamos a categoria filha (Cadernos)
		Categoria categoriaFilha1 = new Categoria();
		categoriaFilha1.setNome("Caderno" );
		categoriaFilha1.setCategoriaPai(categoriaPai );
		// adicionamos a categoria caderno como filha de Papelaria

		// instanciamos a categoria filha (Cadernos)
		Categoria categoriaFilha2 = new Categoria();
		categoriaFilha2.setNome("Caneta" );
		categoriaFilha2.setCategoriaPai(categoriaPai );

		// adicionamos a categoria caderno como filha de Papelaria


		EMFactory emFactory = new EMFactory();
		EntityManager manager = emFactory.getEntityManager();

		EntityTransaction transacao = manager.getTransaction();
		transacao.begin();

		// ao persistir a categoria pai (Papelaria), a filha (Caderno)
		// deve ser persistida também

		categoriaPai.getSubCategorias().add(categoriaFilha1);
		categoriaPai.getSubCategorias().add(categoriaFilha2 );

		manager.persist(categoriaPai );

		transacao.commit();

		manager.close();
		emFactory.close();
	}
	
}