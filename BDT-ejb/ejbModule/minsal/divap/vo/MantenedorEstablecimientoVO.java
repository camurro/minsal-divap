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

	public MantenedorEstablecimientoVO(){
		
	}
	
	public MantenedorEstablecimientoVO(Integer idEstablecimiento, String nombreEstablecimiento, Integer idServicio, String nombreServicio, String codigo, String tipo){
		super();
		this.idEstablecimiento = idEstablecimiento;
		this.nombreEstablecimiento = nombreEstablecimiento;
		this.idServicio = idServicio;
		this.nombreServicio = nombreServicio;
		this.codigo = codigo;
		this.tipo = tipo;
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

	@Override
	public String toString() {
		return "MantenedorEstablecimientoVO [idEstablecimiento="
				+ idEstablecimiento + ", nombreEstablecimiento="
				+ nombreEstablecimiento + ", idServicio=" + idServicio
				+ ", nombreServicio=" + nombreServicio + ", codigo=" + codigo
				+ ", tipo=" + tipo + "]";
	}
	
}
