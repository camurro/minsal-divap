package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;



public class ResolucionConveniosServicioVO  implements Serializable {
	
	private static final long serialVersionUID = -3220915341874133549L;
	private Integer idConvenio;
	private Integer idEstablecimiento;
	private	String nombreEstablecimiento;
	private Integer idPrograma;
	private	String nombrePrograma;
	private List<ConvenioMontoVO> conveniosServicio;
	
	public Integer getIdConvenio() {
		return idConvenio;
	}
	
	public void setIdConvenio(Integer idConvenio) {
		this.idConvenio = idConvenio;
	}
	
	public Integer getIdPrograma() {
		return idPrograma;
	}
	
	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}
	
	public String getNombrePrograma() {
		return nombrePrograma;
	}
	
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}
	
	public Integer getIdEstablecimiento() {
		return idEstablecimiento;
	}

	public void setIdEstablecimiento(Integer idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}

	public String getNombreEstablecimiento() {
		return nombreEstablecimiento;
	}

	public void setNombreEstablecimiento(String nombreEstablecimiento) {
		this.nombreEstablecimiento = nombreEstablecimiento;
	}

	public List<ConvenioMontoVO> getConveniosServicio() {
		return conveniosServicio;
	}

	public void setConveniosServicio(List<ConvenioMontoVO> conveniosServicio) {
		this.conveniosServicio = conveniosServicio;
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
		ResolucionConveniosServicioVO other = (ResolucionConveniosServicioVO) obj;
		if (idConvenio == null) {
			if (other.idConvenio != null)
				return false;
		} else if (!idConvenio.equals(other.idConvenio))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ResolucionConveniosServicioVO [idConvenio=" + idConvenio
				+ ", idEstablecimiento=" + idEstablecimiento
				+ ", nombreEstablecimiento=" + nombreEstablecimiento
				+ ", idPrograma=" + idPrograma + ", nombrePrograma="
				+ nombrePrograma + ", conveniosServicio=" + conveniosServicio
				+ "]";
	}
}
