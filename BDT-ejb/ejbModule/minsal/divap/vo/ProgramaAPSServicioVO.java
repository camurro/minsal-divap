package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.enums.TiposPrograma;
import minsal.divap.enums.Subtitulo;

public class ProgramaAPSServicioVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3626358011786076101L;
	private Integer idServicioSalud;
	private String servicioSalud;
	private Integer idComuna;
	private String comuna;
	private Integer idEstablecimiento;
	private String codigo;
	private String tipo;
	private String establecimiento;
	private Integer tarifa;
	private Integer cantidad;
	private Integer total;
	private TiposPrograma tipoPrograma;
	private Subtitulo subtitulo;
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
		if(getIdEstablecimiento() != null){
			row.add(getIdEstablecimiento());
		}
		if(getEstablecimiento() != null){
			row.add(getEstablecimiento());
		}
		if(getTarifa()!= null){
			row.add(getTarifa());
		}
		if(getCantidad()!= null){
			row.add(getCantidad());
		}
		return row;
	}
	public Integer getIdEstablecimiento() {
		return idEstablecimiento;
	}
	public void setIdEstablecimiento(Integer idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}
	public String getEstablecimiento() {
		return establecimiento;
	}
	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
}
