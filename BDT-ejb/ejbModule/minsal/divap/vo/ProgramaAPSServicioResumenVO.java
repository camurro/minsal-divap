package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ProgramaAPSServicioResumenVO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5350348844958418574L;
	private Integer idServicioSalud;
	private String servicioSalud;
	private Integer idEstablecimiento;
	private String establecimiento;
	private String codigoEstablecimiento;
	
	
	private List<ComponentesVO> componentes;
	
	private Integer idComponente;
	private Integer idSubtitulo;
	
	//S21
	private Integer tarifaS21;
	private Integer cantidadS21;
	private Integer totalS21;
	
	//S22
	private Integer tarifaS22;
	private Integer cantidadS22;
	private Integer totalS22;
	
	//S29
	private Integer tarifaS29;
	private Integer cantidadS29;
	private Integer totalS29;
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
	public Integer getTarifaS21() {
		return tarifaS21;
	}
	public void setTarifaS21(Integer tarifaS21) {
		this.tarifaS21 = tarifaS21;
	}
	public Integer getCantidadS21() {
		return cantidadS21;
	}
	public void setCantidadS21(Integer cantidadS21) {
		this.cantidadS21 = cantidadS21;
	}
	public Integer getTotalS21() {
		return totalS21;
	}
	public void setTotalS21(Integer totalS21) {
		this.totalS21 = totalS21;
	}
	public Integer getTarifaS22() {
		return tarifaS22;
	}
	public void setTarifaS22(Integer tarifaS22) {
		this.tarifaS22 = tarifaS22;
	}
	public Integer getCantidadS22() {
		return cantidadS22;
	}
	public void setCantidadS22(Integer cantidadS22) {
		this.cantidadS22 = cantidadS22;
	}
	public Integer getTotalS22() {
		return totalS22;
	}
	public void setTotalS22(Integer totalS22) {
		this.totalS22 = totalS22;
	}
	public Integer getTarifaS29() {
		return tarifaS29;
	}
	public void setTarifaS29(Integer tarifaS29) {
		this.tarifaS29 = tarifaS29;
	}
	public Integer getCantidadS29() {
		return cantidadS29;
	}
	public void setCantidadS29(Integer cantidadS29) {
		this.cantidadS29 = cantidadS29;
	}
	public Integer getTotalS29() {
		return totalS29;
	}
	public void setTotalS29(Integer totalS29) {
		this.totalS29 = totalS29;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idEstablecimiento == null) ? 0 : idEstablecimiento
						.hashCode());
		result = prime * result
				+ ((idServicioSalud == null) ? 0 : idServicioSalud.hashCode());
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
		ProgramaAPSServicioResumenVO other = (ProgramaAPSServicioResumenVO) obj;
		if (idEstablecimiento == null) {
			if (other.idEstablecimiento != null)
				return false;
		} else if (!idEstablecimiento.equals(other.idEstablecimiento))
			return false;
		if (idServicioSalud == null) {
			if (other.idServicioSalud != null)
				return false;
		} else if (!idServicioSalud.equals(other.idServicioSalud))
			return false;
		return true;
	}
	public String getCodigoEstablecimiento() {
		return codigoEstablecimiento;
	}
	public void setCodigoEstablecimiento(String codigoEstablecimiento) {
		this.codigoEstablecimiento = codigoEstablecimiento;
	}
	public Integer getIdComponente() {
		return idComponente;
	}
	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}
	public Integer getIdSubtitulo() {
		return idSubtitulo;
	}
	public void setIdSubtitulo(Integer idSubtitulo) {
		this.idSubtitulo = idSubtitulo;
	}
	public List<ComponentesVO> getComponentes() {
		return componentes;
	}
	public void setComponentes(List<ComponentesVO> componentes) {
		this.componentes = componentes;
	}

	
	
}
