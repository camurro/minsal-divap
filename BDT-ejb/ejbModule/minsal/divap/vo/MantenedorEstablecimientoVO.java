package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorEstablecimientoVO implements Serializable{

	private static final long serialVersionUID = 3176377302112261711L;
	
	private Integer idEstablecimiento;
	private String nombreEstablecimiento;
	private Integer idServicio;
	private String nombreServicio;
	private String codigo;
	private String tipo;
	private Boolean esAuxiliar;
	private Boolean puedeEliminarse;

	public MantenedorEstablecimientoVO(){
		
	}
	

	public Integer getIdEstablecimiento() {
		return idEstablecimiento;
	}

	public void setIdEstablecimiento(Integer idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}

	public String getNombreEstablecimiento() {
		return nombreEstablecimiento;
	}

	public void setNombreEstablecimiento(String nombreEstablecimiento) {
		this.nombreEstablecimiento = nombreEstablecimiento;
	}

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

	public Boolean getPuedeEliminarse() {
		return puedeEliminarse;
	}

	public void setPuedeEliminarse(Boolean puedeEliminarse) {
		this.puedeEliminarse = puedeEliminarse;
	}

	public Boolean getEsAuxiliar() {
		return esAuxiliar;
	}

	public void setEsAuxiliar(Boolean esAuxiliar) {
		this.esAuxiliar = esAuxiliar;
	}

	@Override
	public String toString() {
		return "MantenedorEstablecimientoVO [idEstablecimiento="
				+ idEstablecimiento + ", nombreEstablecimiento="
				+ nombreEstablecimiento + ", idServicio=" + idServicio
				+ ", nombreServicio=" + nombreServicio + ", codigo=" + codigo
				+ ", tipo=" + tipo + ", esAuxiliar=" + esAuxiliar
				+ ", puedeEliminarse=" + puedeEliminarse + "]";
	}

}
