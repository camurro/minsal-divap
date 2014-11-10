package minsal.divap.vo;

import java.io.Serializable;

public class ConvenioMontoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6029640700623154615L;
	private Integer idConvenioMonto;
	private Integer idComponente;
	private String nombreComponente;
	private Integer idSubtitulo;
	private String nombreSubtitulo;
	private Integer monto;
	
	public ConvenioMontoVO() {

	}

	public ConvenioMontoVO(Integer idConvenioMonto, Integer idComponente, String nombreComponente,
			Integer idSubtitulo, String nombreSubtitulo, Integer monto) {
		super();
		this.idConvenioMonto = idConvenioMonto;
		this.idComponente = idComponente;
		this.nombreComponente = nombreComponente;
		this.idSubtitulo = idSubtitulo;
		this.nombreSubtitulo = nombreSubtitulo;
		this.monto = monto;
	}

	public Integer getIdConvenioMonto() {
		return idConvenioMonto;
	}

	public void setIdConvenioMonto(Integer idConvenioMonto) {
		this.idConvenioMonto = idConvenioMonto;
	}

	public Integer getIdComponente() {
		return idComponente;
	}

	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}

	public String getNombreComponente() {
		return nombreComponente;
	}

	public void setNombreComponente(String nombreComponente) {
		this.nombreComponente = nombreComponente;
	}

	public Integer getIdSubtitulo() {
		return idSubtitulo;
	}

	public void setIdSubtitulo(Integer idSubtitulo) {
		this.idSubtitulo = idSubtitulo;
	}

	public String getNombreSubtitulo() {
		return nombreSubtitulo;
	}

	public void setNombreSubtitulo(String nombreSubtitulo) {
		this.nombreSubtitulo = nombreSubtitulo;
	}

	public Integer getMonto() {
		return monto;
	}

	public void setMonto(Integer monto) {
		this.monto = monto;
	}

	@Override
	public String toString() {
		return "ConvenioComunaVO [idConvenioMonto=" + idConvenioMonto + "idComponente=" + idComponente
				+ ", nombreComponente=" + nombreComponente + ", idSubtitulo="
				+ idSubtitulo + ", nombreSubtitulo=" + nombreSubtitulo
				+ ", monto=" + monto + "]";
	}
	
}
