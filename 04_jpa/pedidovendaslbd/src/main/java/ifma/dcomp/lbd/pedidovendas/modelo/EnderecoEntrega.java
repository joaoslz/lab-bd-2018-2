package ifma.dcomp.lbd.pedidovendas.modelo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class EnderecoEntrega {

	@Column(name = "entrega_logradouro", nullable = false, length = 150)
	private String logradouro;


	@Column(name = "entrega_numero", nullable = false, length = 100)
	private String numero;


	@Column(name = "entrega_complemento", length = 150)
	private String complemento;


	@Column(name = "entrega_cidade", nullable = false, length = 60)
	private String cidade;

	@Column(name = "entrega_uf", nullable = false, length = 2)
	private String uf;


	@Column(name = "entrega_cep", nullable = false, length = 9)
	private String cep;

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}


	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EnderecoEntrega)) return false;
		EnderecoEntrega that = (EnderecoEntrega) o;
		return Objects.equals(getLogradouro(), that.getLogradouro()) &&
				Objects.equals(getNumero(), that.getNumero()) &&
				Objects.equals(getComplemento(), that.getComplemento()) &&
				Objects.equals(getCidade(), that.getCidade()) &&
				Objects.equals(getUf(), that.getUf()) &&
				Objects.equals(getCep(), that.getCep());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getLogradouro(), getNumero(), getComplemento(), getCidade(), getUf(), getCep());
	}
}
