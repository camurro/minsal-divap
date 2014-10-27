package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK;
import cl.minsal.divap.model.TipoSubtitulo;


public class ResumenProgramaServiciosVO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1164552171832886895L;
	private Integer idServicio;
	private String nombreServicio;
	private Integer totalS21;
	private Integer totalS22;
	private Integer totalS29;
	private Integer totalServicio;
	
	
	public Integer getIdServicio() {
		return idServicio;
	}
	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}
	public String getNombreServicio() {
		return nombreServicio;
	}
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}
	public Integer getTotalS21() {
		return totalS21;
	}
	public void setTotalS21(Integer totalS21) {
		this.totalS21 = totalS21;
	}
	public Integer getTotalS22() {
		return totalS22;
	}
	public void setTotalS22(Integer totalS22) {
		this.totalS22 = totalS22;
	}
	public Integer getTotalS29() {
		return totalS29;
	}
	public void setTotalS29(Integer totalS29) {
		this.totalS29 = totalS29;
	}
	
	public Integer getTotalServicio() {
		totalServicio=0;
		if(totalS21!=null && totalS22!=null && totalS29!=null){
			totalServicio = totalS21+totalS22+totalS29;
		}
		
		return totalServicio;
	}
	public void setTotalServicio(Integer totalServicio) {
		this.totalServicio = totalServicio;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResumenProgramaServiciosVO other = (ResumenProgramaServiciosVO) obj;
		if (idServicio == null) {
			if (other.idServicio != null)
				return false;
		} else if (!idServicio.equals(other.idServicio))
			return false;
		return true;
	}
	
	
	
	
}
