package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK;
import cl.minsal.divap.model.TipoSubtitulo;


public class ProgramaMunicipalVO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5722857110023282200L;
	private Integer idComuna;
	private String nombreComuna;
	private Integer precio;
	private Integer cantidad;
	private Integer total;
	private Integer idComponente;
	private Integer idProgramaMunicipalCore;
	
	private List<SubtituloVO> subtitulos;
	
		
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
	public Integer getPrecio() {
		return precio;
	}
	public void setPrecio(Integer precio) {
		this.precio = precio;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getIdComponente() {
		return idComponente;
	}
	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}
	public Integer getIdProgramaMunicipalCore() {
		return idProgramaMunicipalCore;
	}
	public void setIdProgramaMunicipalCore(Integer idProgramaMunicipalCore) {
		this.idProgramaMunicipalCore = idProgramaMunicipalCore;
	}
	public List<SubtituloVO> getSubtitulos() {
		return subtitulos;
	}
	public void setSubtitulos(List<SubtituloVO> subtitulos) {
		this.subtitulos = subtitulos;
	}
	
	
}
