package py.edu.uca.intercajas.shared.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("concedido")
@Entity
public class Concedido extends Finiquito {

	private static final long serialVersionUID = 1L;

	@NotNull
	private BigDecimal bt;
	@NotNull
	private int tx;
	@NotNull
	private int tmin;
	@NotNull
	private BigDecimal bx;

	public BigDecimal getBt() {
		return bt;
	}
	public void setBt(BigDecimal bt) {
		this.bt = bt;
	}
	public int getTx() {
		return tx;
	}
	public void setTx(int tx) {
		this.tx = tx;
	}
	public int getTmin() {
		return tmin;
	}
	public void setTmin(int tmin) {
		this.tmin = tmin;
	}
	public BigDecimal getBx() {
		return bx;
	}
	public void setBx(BigDecimal bx) {
		this.bx = bx;
	}
	
	
	
}
