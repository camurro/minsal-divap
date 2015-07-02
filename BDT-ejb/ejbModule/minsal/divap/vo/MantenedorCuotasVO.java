package minsal.divap.vo;

import java.io.Serializable;
import java.util.Date;

public class MantenedorCuotasVO implements Serializable{

	private static final long serialVersionUID = 5781044938433681192L;
	
	
	private Integer idCuota;
	private Integer nroCuota;
	private Integer porcentajeCuota;
	private Integer mes;
	private Date fecha_cuota;
	private Boolean puedeEliminarse;
	
	public MantenedorCuotasVO(){
		
	}
	
	public MantenedorCuotasVO(Integer idCuota, Integer nroCuota, Integer porcentajeCuota, Integer mes, Date fecha_cuota, Boolean puedeEliminarse){
		super();
		this.idCuota = idCuota;
		this.nroCuota = nroCuota;
		this.porcentajeCuota = porcentajeCuota;
		this.mes = mes;
		this.fecha_cuota = fecha_cuota;
		this.puedeEliminarse = puedeEliminarse;
	}
	
	public MantenedorCuotasVO(MantenedorCuotasVO mantenedorNuevo){
		this.idCuota = mantenedorNuevo.getIdCuota();
		this.nroCuota = mantenedorNuevo.getNroCuota();
		this.porcentajeCuota = mantenedorNuevo.getPorcentajeCuota();
		this.mes = mantenedorNuevo.getMes();
		this.fecha_cuota = mantenedorNuevo.getFecha_cuota();
		this.puedeEliminarse = mantenedorNuevo.getPuedeEliminarse();
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

	public Integer getPorcentajeCuota() {
		return porcentajeCuota;
	}

	public void setPorcentajeCuota(Integer porcentajeCuota) {
		this.porcentajeCuota = porcentajeCuota;
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

	public Boolean getPuedeEliminarse() {
		return puedeEliminarse;
	}

	public void setPuedeEliminarse(Boolean puedeEliminarse) {
		this.puedeEliminarse = puedeEliminarse;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MantenedorCuotasVO other = (MantenedorCuotasVO) obj;
		if (idCuota == null) {
			if (other.idCuota != null)
				return false;
		} else if (!idCuota.equals(other.idCuota))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MantenedorCuotasVO [idCuota=" + idCuota + ", nroCuota="
				+ nroCuota + ", porcentajeCuota=" + porcentajeCuota + ", mes="
				+ mes + ", fecha_cuota=" + fecha_cuota + ", puedeEliminarse="
				+ puedeEliminarse + "]";
	}

}
