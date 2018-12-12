package ifma.dcomp.lbd.pedidovendas.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "pagamento_cartao")
public final class PagamentoCartao extends Pagamento {

    @Column(name = "numero_parcelas")
    private Short numeroDeParcelas;

    public Short getNumeroDeParcelas() {
        return numeroDeParcelas;
    }

    public void setNumeroDeParcelas(Short numeroDeParcelas) {

        if (numeroDeParcelas < 1) {
            throw new IllegalArgumentException("Número de parcelas inválida ");
        }
        this.numeroDeParcelas = numeroDeParcelas;

    }
}
