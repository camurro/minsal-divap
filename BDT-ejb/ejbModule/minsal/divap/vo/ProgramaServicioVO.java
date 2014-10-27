package minsal.divap.vo;

import java.io.Serializable;


public class ProgramaServicioVO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7179172026106880794L;
	private String nombreEstablecimiento;
	private Integer idEstablecimiento;
	
	private SubtituloProgramasVO subtitulo21;
	private SubtituloProgramasVO subtitulo22;
	private SubtituloProgramasVO subtitulo29;
	private SubtituloProgramasVO subtitulo24;
	
	private Integer totalEstablecimiento;
	
	private Integer idServicioCore;
	private Integer idComponente;

	public String getNombreEstablecimiento() {
		return nombreEstablecimiento;
	}

	public void setNombreEstablecimiento(String nombreEstablecimiento) {
		this.nombreEstablecimiento = nombreEstablecimiento;
	}

	public Integer getIdEstablecimiento() {
		return idEstablecimiento;
	}

	public void setIdEstablecimiento(Integer idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}

	public SubtituloProgramasVO getSubtitulo21() {
		return subtitulo21;
	}

	public void setSubtitulo21(SubtituloProgramasVO subtitulo21) {
		this.subtitulo21 = subtitulo21;
	}

	public SubtituloProgramasVO getSubtitulo22() {
		return subtitulo22;
	}

	public void setSubtitulo22(SubtituloProgramasVO subtitulo22) {
		this.subtitulo22 = subtitulo22;
	}

	public SubtituloProgramasVO getSubtitulo29() {
		return subtitulo29;
	}

	public void setSubtitulo29(SubtituloProgramasVO subtitulo29) {
		this.subtitulo29 = subtitulo29;
	}

	public SubtituloProgramasVO getSubtitulo24() {
		return subtitulo24;
	}

	public void setSubtitulo24(SubtituloProgramasVO subtitulo24) {
		this.subtitulo24 = subtitulo24;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProgramaServicioVO other = (ProgramaServicioVO) obj;
		if (idEstablecimiento == null) {
			if (other.idEstablecimiento != null)
				return false;
		} else if (!idEstablecimiento.equals(other.idEstablecimiento))
			return false;
		return true;
	}

	public Integer getTotalEstablecimiento() {
		totalEstablecimiento=0;
		if(subtitulo21!=null){
			totalEstablecimiento+=subtitulo21.getTotal();
		}
		if(subtitulo22!=null){
			totalEstablecimiento+=subtitulo22.getTotal();
		}
		if(subtitulo29!=null){
			totalEstablecimiento+=subtitulo29.getTotal();
		}
		return totalEstablecimiento;
	}

	public void setTotalEstablecimiento(Integer totalEstablecimiento) {
		this.totalEstablecimiento = totalEstablecimiento;
	}

	public Integer getIdServicioCore() {
		return idServicioCore;
	}

	public void setIdServicioCore(Integer idServicioCore) {
		this.idServicioCore = idServicioCore;
	}

	public Integer getIdComponente() {
		return idComponente;
	}

	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}

		
	
	
	
}
