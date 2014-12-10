package minsal.divap.vo;

import java.io.Serializable;
import java.util.Date;

public class MantenedorCuotasVO implements Serializable{

	private static final long serialVersionUID = 5781044938433681192L;
	
	
	private Integer idCuota;
	private Integer nroCuota;
	private Integer porcentaje_cuota;
	private Integer monto_cuota;
	private Integer mes;
	private Date fecha_cuota;
	
	public MantenedorCuotasVO(){
		
	}
	
	public MantenedorCuotasVO(Integer idCuota, Integer nroCuota, Integer porcentaje_cuota, Integer monto_cuota, Integer mes, Date fecha_cuota){
		super();
		this.idCuota = idCuota;
		this.nroCuota = nroCuota;
		this.porcentaje_cuota = porcentaje_cuota;
		this.monto_cuota = monto_cuota;
		this.mes = mes;
		this.fecha_cuota = fecha_cuota;
	}

	public Integer getIdCuota() {
		return idCuota;
	}

	public void setIdCuota(Integer idCuota) {
		this.idCuota = idCuota;
	}

	public Integer getNroCuota() {
		return nroCuota;
	}

	public void setNroCuota(Integer nroCuota) {
		this.nroCuota = nroCuota;
	}

	public Integer getPorcentaje_cuota() {
		return porcentaje_cuota;
	}

	public void setPorcentaje_cuota(Integer porcentaje_cuota) {
		this.porcentaje_cuota = porcentaje_cuota;
	}

	public Integer getMonto_cuota() {
		return monto_cuota;
	}

	public void setMonto_cuota(Integer monto_cuota) {
		this.monto_cuota = monto_cuota;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Date getFecha_cuota() {
		return fecha_cuota;
	}

	public void setFecha_cuota(Date fecha_cuota) {
		this.fecha_cuota = fecha_cuota;
	}

	@Override
	public String toString() {
		return "MantenedorCuotasVO [idCuota=" + idCuota + ", nroCuota="
				+ nroCuota + ", porcentaje_cuota=" + porcentaje_cuota
				+ ", monto_cuota=" + monto_cuota + ", mes=" + mes
				+ ", fecha_cuota=" + fecha_cuota + "]";
	}

}
