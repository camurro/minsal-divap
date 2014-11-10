package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;



public class ResolucionConveniosComunaVO  implements Serializable {
	
	private static final long serialVersionUID = -3220915341874133549L;
	private Integer idConvenio;
	private Integer idComuna;
	private	String nombreComuna;
	private Integer idPrograma;
	private	String nombrePrograma;
	private List<ConvenioMontoVO> conveniosComuna;
	
	public Integer getIdConvenio() {
		return idConvenio;
	}
	
	public void setIdConvenio(Integer idConvenio) {
		this.idConvenio = idConvenio;
	}
	
	public Integer getIdComuna() {
		return idComuna;
	}
	
	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}
	
	public String getNombreComuna() {
		return nombreComuna;
	}
	
	public void setNombreComuna(String nombreComuna) {
		this.nombreComuna = nombreComuna;
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
	
	public List<ConvenioMontoVO> getConveniosComuna() {
		return conveniosComuna;
	}
	
	public void setConveniosComuna(List<ConvenioMontoVO> conveniosComuna) {
		this.conveniosComuna = conveniosComuna;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((conveniosComuna == null) ? 0 : conveniosComuna.hashCode());
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
		ResolucionConveniosComunaVO other = (ResolucionConveniosComunaVO) obj;
		if (conveniosComuna == null) {
			if (other.conveniosComuna != null)
				return false;
		} else if (!conveniosComuna.equals(other.conveniosComuna))
			return false;
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
				+ ", idComuna=" + idComuna + ", nombreComuna=" + nombreComuna
				+ ", idPrograma=" + idPrograma + ", nombrePrograma="
				+ nombrePrograma + ", conveniosComuna=" + conveniosComuna + "]";
	}
	
}
