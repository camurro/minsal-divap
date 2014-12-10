package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReporteEstadoSituacionByComunaVO implements Serializable{

	
	private static final long serialVersionUID = 2173300659712232494L;
	
	private String programa;
	private String servicio;
	private String comuna;
	private Long marco_inicial;
	private Long marco_modificado;
	private Long convenioRecibido_monto;
	private Double convenioRecibido_porcentaje;
	private Long convenioPendiente_monto;
	private Double convenioPendiente_porcentaje;
	private Long remesaAcumulada_monto;
	private Double remesaAcumulada_porcentaje;
	private Long remesaPendiente_monto;
	private Double remesaPendiente_porcentaje;
	private Long reliquidacion_monto;
	private Double reliquidacion_porcentaje;
	private Long incremento;
	
	public ReporteEstadoSituacionByComunaVO(){
		
	}
	public ReporteEstadoSituacionByComunaVO(String programa, String servicio, String comuna, Long marco_inicial, Long marco_modificado, Long convenioRecibido_monto, Double convenioRecibido_porcentaje, Long convenioPendiente_monto, Double convenioPendiente_porcentaje, Long remesaAcumulada_monto, Double remesaAcumulada_porcentaje, Long remesaPendiente_monto, Double remesaPendiente_porcentaje, Long reliquidacion_monto, Double reliquidacion_porcentaje, Long incremento){
		super();
		this.programa = programa;
		this.servicio = servicio;
		this.comuna = comuna;
		this.marco_inicial =
		this.marco_modificado =
		this.convenioRecibido_monto = convenioRecibido_monto;
		this.convenioRecibido_porcentaje = convenioRecibido_porcentaje;
		this.convenioPendiente_monto = convenioPendiente_monto;
		this.convenioPendiente_porcentaje = convenioPendiente_porcentaje;
		this.remesaAcumulada_monto = remesaAcumulada_monto;
		this.remesaAcumulada_porcentaje = remesaAcumulada_porcentaje;
		this.remesaPendiente_monto = remesaPendiente_monto;
		this.remesaPendiente_porcentaje = remesaPendiente_porcentaje;
		this.reliquidacion_monto = reliquidacion_monto;
		this.reliquidacion_porcentaje = reliquidacion_porcentaje;
		this.incremento = incremento;
	}
	public String getPrograma() {
		return programa;
	}
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public String getComuna() {
		return comuna;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public Long getMarco_inicial() {
		return marco_inicial;
	}
	public void setMarco_inicial(Long marco_inicial) {
		this.marco_inicial = marco_inicial;
	}
	public Long getMarco_modificado() {
		return marco_modificado;
	}
	public void setMarco_modificado(Long marco_modificado) {
		this.marco_modificado = marco_modificado;
	}
	public Long getConvenioRecibido_monto() {
		return convenioRecibido_monto;
	}
	public void setConvenioRecibido_monto(Long convenioRecibido_monto) {
		this.convenioRecibido_monto = convenioRecibido_monto;
	}
	public Double getConvenioRecibido_porcentaje() {
		return convenioRecibido_porcentaje;
	}
	public void setConvenioRecibido_porcentaje(Double convenioRecibido_porcentaje) {
		this.convenioRecibido_porcentaje = convenioRecibido_porcentaje;
	}
	public Long getConvenioPendiente_monto() {
		return convenioPendiente_monto;
	}
	public void setConvenioPendiente_monto(Long convenioPendiente_monto) {
		this.convenioPendiente_monto = convenioPendiente_monto;
	}
	public Double getConvenioPendiente_porcentaje() {
		return convenioPendiente_porcentaje;
	}
	public void setConvenioPendiente_porcentaje(Double convenioPendiente_porcentaje) {
		this.convenioPendiente_porcentaje = convenioPendiente_porcentaje;
	}
	public Long getRemesaAcumulada_monto() {
		return remesaAcumulada_monto;
	}
	public void setRemesaAcumulada_monto(Long remesaAcumulada_monto) {
		this.remesaAcumulada_monto = remesaAcumulada_monto;
	}
	public Double getRemesaAcumulada_porcentaje() {
		return remesaAcumulada_porcentaje;
	}
	public void setRemesaAcumulada_porcentaje(Double remesaAcumulada_porcentaje) {
		this.remesaAcumulada_porcentaje = remesaAcumulada_porcentaje;
	}
	public Long getRemesaPendiente_monto() {
		return remesaPendiente_monto;
	}
	public void setRemesaPendiente_monto(Long remesaPendiente_monto) {
		this.remesaPendiente_monto = remesaPendiente_monto;
	}
	public Double getRemesaPendiente_porcentaje() {
		return remesaPendiente_porcentaje;
	}
	public void setRemesaPendiente_porcentaje(Double remesaPendiente_porcentaje) {
		this.remesaPendiente_porcentaje = remesaPendiente_porcentaje;
	}
	public Long getReliquidacion_monto() {
		return reliquidacion_monto;
	}
	public void setReliquidacion_monto(Long reliquidacion_monto) {
		this.reliquidacion_monto = reliquidacion_monto;
	}
	public Double getReliquidacion_porcentaje() {
		return reliquidacion_porcentaje;
	}
	public void setReliquidacion_porcentaje(Double reliquidacion_porcentaje) {
		this.reliquidacion_porcentaje = reliquidacion_porcentaje;
	}
	public Long getIncremento() {
		return incremento;
	}
	public void setIncremento(Long incremento) {
		this.incremento = incremento;
	}
	
	
	
	@Override
	public String toString() {
		return "ReporteEstadoSituacionByComunaVO [programa=" + programa
				+ ", servicio=" + servicio + ", comuna=" + comuna
				+ ", marco_inicial=" + marco_inicial + ", marco_modificado="
				+ marco_modificado + ", convenioRecibido_monto="
				+ convenioRecibido_monto + ", convenioRecibido_porcentaje="
				+ convenioRecibido_porcentaje + ", convenioPendiente_monto="
				+ convenioPendiente_monto + ", convenioPendiente_porcentaje="
				+ convenioPendiente_porcentaje + ", remesaAcumulada_monto="
				+ remesaAcumulada_monto + ", remesaAcumulada_porcentaje="
				+ remesaAcumulada_porcentaje + ", remesaPendiente_monto="
				+ remesaPendiente_monto + ", remesaPendiente_porcentaje="
				+ remesaPendiente_porcentaje + ", reliquidacion_monto="
				+ reliquidacion_monto + ", reliquidacion_porcentaje="
				+ reliquidacion_porcentaje + ", incremento=" + incremento + "]";
	}
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		
		if(getPrograma() != null){
			row.add(getPrograma());
		}
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getComuna() != null){
			row.add(getComuna());
		}
		if(getMarco_inicial() != null){
			row.add(getMarco_inicial());
		}
		if(getMarco_modificado() != null){
			row.add(getMarco_modificado());
		}
		if(getConvenioRecibido_monto() != null){
			row.add(getConvenioRecibido_monto());
		}
		if(getConvenioRecibido_porcentaje() != null){
			row.add(getConvenioRecibido_porcentaje());
		}
		if(getConvenioPendiente_monto() != null){
			row.add(getConvenioPendiente_monto());
		}
		if(getConvenioPendiente_porcentaje() != null){
			row.add(getConvenioPendiente_porcentaje());
		}
		if(getRemesaAcumulada_monto() != null){
			row.add(getRemesaAcumulada_monto());
		}
		if(getRemesaAcumulada_porcentaje() != null){
			row.add(getRemesaAcumulada_porcentaje());
		}
		if(getRemesaPendiente_monto() != null){
			row.add(getRemesaPendiente_monto());
		}
		if(getRemesaPendiente_porcentaje() != null){
			row.add(getRemesaPendiente_porcentaje());
		}
		if(getReliquidacion_monto() != null){
			row.add(getReliquidacion_monto());
		}
		if(getReliquidacion_porcentaje() != null){
			row.add(getReliquidacion_porcentaje());
		}
		if(getIncremento() != null){
			row.add(getIncremento());
		}
		
		return row;
	}
	
	
}
