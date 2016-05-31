package py.edu.uca.intercajas.shared;

import java.util.Date;

public class BandejaParam {

	public Integer startRow;
	public Integer maxResults;
	public Long beneficiario_id;
	public Long remitente_id;
	public Date fecha_desde;
	public Date fecha_hasta;

	public BandejaParam() {
	}

	public BandejaParam(Integer startRow, Integer maxResults,
			Long beneficiario_id, Long remitente_id, Date fecha_desde,
			Date fecha_hasta) {
		this.startRow = startRow;
		this.maxResults = maxResults;
		this.beneficiario_id = beneficiario_id;
		this.remitente_id = remitente_id;
		this.fecha_desde = fecha_desde;
		this.fecha_hasta = fecha_hasta;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public Long getBeneficiario_id() {
		return beneficiario_id;
	}

	public void setBeneficiario_id(Long beneficiario_id) {
		this.beneficiario_id = beneficiario_id;
	}

	public Long getRemitente_id() {
		return remitente_id;
	}

	public void setRemitente_id(Long remitente_id) {
		this.remitente_id = remitente_id;
	}

	public Date getFecha_desde() {
		return fecha_desde;
	}

	public void setFecha_desde(Date fecha_desde) {
		this.fecha_desde = fecha_desde;
	}

	public Date getFecha_hasta() {
		return fecha_hasta;
	}

	public void setFecha_hasta(Date fecha_hasta) {
		this.fecha_hasta = fecha_hasta;
	}

}
