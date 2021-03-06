package py.edu.uca.intercajas.shared;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import py.edu.uca.intercajas.shared.entity.Adjunto;

public class NuevoConcedido {

	Adjunto[] adjuntos;
	String cuerpoMensaje;
	String numeroResolucion;
	Long solicitudBeneficiarioId;
	Long destino_id;

	BigDecimal bx;
	BigDecimal bt;
	Integer tx;
	Integer tmin;

	public NuevoConcedido() {

	}

	public NuevoConcedido(String numeroResolucion,
			Long solicitudBeneficiarioId, BigDecimal bx, BigDecimal bt,
			Integer tx, Integer tmin) {
		this.numeroResolucion = numeroResolucion;
		this.solicitudBeneficiarioId = solicitudBeneficiarioId;
		this.bx = bx;
		this.bt = bt;
		this.tx = tx;
		this.tmin = tmin;
	}

	public String getNumeroResolucion() {
		return numeroResolucion;
	}

	public void setNumeroResolucion(String numeroResolucion) {
		this.numeroResolucion = numeroResolucion;
	}

	public Long getSolicitudBeneficiarioId() {
		return solicitudBeneficiarioId;
	}

	public void setSolicitudBeneficiarioId(Long solicitudBeneficiarioId) {
		this.solicitudBeneficiarioId = solicitudBeneficiarioId;
	}

	public BigDecimal getBx() {
		return bx;
	}

	public void setBx(BigDecimal bx) {
		this.bx = bx;
	}

	public BigDecimal getBt() {
		return bt;
	}

	public void setBt(BigDecimal bt) {
		this.bt = bt;
	}

	public Integer getTx() {
		return tx;
	}

	public void setTx(Integer tx) {
		this.tx = tx;
	}

	public Integer getTmin() {
		return tmin;
	}

	public void setTmin(Integer tmin) {
		this.tmin = tmin;
	}

	public Adjunto[] getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(Adjunto[] adjuntos) {
		this.adjuntos = adjuntos;
	}

	public String getCuerpoMensaje() {
		return cuerpoMensaje;
	}

	public void setCuerpoMensaje(String cuerpoMensaje) {
		this.cuerpoMensaje = cuerpoMensaje;
	}

	public Long getDestino_id() {
		return destino_id;
	}

	public void setDestino_id(Long destino_id) {
		this.destino_id = destino_id;
	}

}