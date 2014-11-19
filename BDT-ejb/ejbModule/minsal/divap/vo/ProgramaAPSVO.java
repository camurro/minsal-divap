package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.enums.TiposPrograma;
import minsal.divap.enums.Subtitulo;

public class ProgramaAPSVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8466103003802069012L;
	private Integer idServicioSalud;
	private String servicioSalud;
	private Integer idComuna;
	private String comuna;
	private Integer tarifa;
	private Integer cantidad;
	private Integer total;
	private TiposPrograma tipoPrograma;
	private Subtitulo subtitulo;
	private Integer idPrograma;
	private Integer idComponente;
	//TO-DO Componentes
	public Integer getIdServicioSalud() {
		return idServicioSalud;
	}
	public void setIdServicioSalud(Integer idServicioSalud) {
		this.idServicioSalud = idServicioSalud;
	}
	public String getServicioSalud() {
		return servicioSalud;
	}
	public void setServicioSalud(String servicioSalud) {
		this.servicioSalud = servicioSalud;
	}
	public Integer getIdComuna() {
		return idComuna;
	}
	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}
	public String getComuna() {
		return comuna;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public Integer getTarifa() {
		return tarifa;
	}
	public void setTarifa(Integer tarifa) {
		this.tarifa = tarifa;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public TiposPrograma getTipoPrograma() {
		return tipoPrograma;
	}
	public void setTipoPrograma(TiposPrograma tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}

	public Subtitulo getSubtitulo() {
		return subtitulo;
	}
	public void setSubtitulo(Subtitulo subtitulo) {
		this.subtitulo = subtitulo;
	}
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getIdServicioSalud() != null){
			row.add(getIdServicioSalud()) ;
		}
		if(getServicioSalud() != null){
			row.add(getServicioSalud());
		}
		if(getIdComuna() != null){
			row.add(getIdComuna());
		}
		if(getComuna() != null){
			row.add(getComuna());
		}
		if(getTarifa()!= null){
			row.add(getTarifa());
		}
		if(getCantidad()!= null){
			row.add(getCantidad());
		}
		return row;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getIdPrograma() {
		return idPrograma;
	}
	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}
	public Integer getIdComponente() {
		return idComponente;
	}
	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}


	
}
