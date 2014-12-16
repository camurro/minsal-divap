package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK;
import cl.minsal.divap.model.TipoSubtitulo;


public class ResumenProgramaVO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6303090365999744863L;
	private Integer idServicio;
	private String nombreServicio;
	private Long totalS24;
	
	
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
	public Long getTotalS24() {
		return totalS24;
	}
	public void setTotalS24(Long totalS24) {
		this.totalS24 = totalS24;
	}
	
	
	
	
}
