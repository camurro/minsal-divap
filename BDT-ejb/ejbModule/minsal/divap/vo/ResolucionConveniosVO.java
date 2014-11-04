package minsal.divap.vo;

import java.io.Serializable;


public class ResolucionConveniosVO  implements Serializable {
	
	private static final long serialVersionUID = -3220915341874133549L;
	private Integer idConvenio;
	private	String nombreComuna;
	private	String nombreEstablecimiento;
	private	String nombrePrograma;
	private	String nombreComponente;
	private	String nombreSubtitulo;
	private	Long monto;
	
	public Integer getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(Integer idConvenio) {
		this.idConvenio = idConvenio;
	}

	public String getNombreComuna() {
		return nombreComuna;
	}
	
	public void setNombreComuna(String nombreComuna) {
		this.nombreComuna = nombreComuna;
	}
	
	public String getNombreEstablecimiento() {
		return nombreEstablecimiento;
	}
	
	public void setNombreEstablecimiento(String nombreEstablecimiento) {
		this.nombreEstablecimiento = nombreEstablecimiento;
	}
	
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	public String getNombreComponente() {
		return nombreComponente;
	}

	public void setNombreComponente(String nombreComponente) {
		this.nombreComponente = nombreComponente;
	}

	public String getNombreSubtitulo() {
		return nombreSubtitulo;
	}
	
	public void setNombreSubtitulo(String nombreSubtitulo) {
		this.nombreSubtitulo = nombreSubtitulo;
	}
	
	public Long getMonto() {
		return monto;
	}
	
	public void setMonto(Long monto) {
		this.monto = monto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idConvenio == null) ? 0 : idConvenio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResolucionConveniosVO other = (ResolucionConveniosVO) obj;
		if (idConvenio == null) {
			if (other.idConvenio != null)
				return false;
		} else if (!idConvenio.equals(other.idConvenio))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ResolucionConveniosVO [idConvenio=" + idConvenio
				+ ", nombreComuna=" + nombreComuna + ", nombreEstablecimiento="
				+ nombreEstablecimiento + ", nombrePrograma=" + nombrePrograma
				+ ", nombreComponente=" + nombreComponente
				+ ", nombreSubtitulo=" + nombreSubtitulo + ", monto=" + monto
				+ "]";
	}
	
}
