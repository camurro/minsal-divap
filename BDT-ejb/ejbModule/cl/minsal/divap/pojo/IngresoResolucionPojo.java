package cl.minsal.divap.pojo;

import java.util.Date;

public class IngresoResolucionPojo {
	private String comuna;
	private String establecimiento;
	private Date fecha;
	private String resolucion;
	private String componente;
	private Integer subtitulo;
	private Long monto;
	private String archivo;
	public String getComuna() {
		return comuna;
	}
	public void setComuna( String comuna ) {
		this.comuna = comuna;
	}
	public String getEstablecimiento() {
		return establecimiento;
	}
	public void setEstablecimiento( String establecimiento ) {
		this.establecimiento = establecimiento;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha( Date fecha ) {
		this.fecha = fecha;
	}
	public String getResolucion() {
		return resolucion;
	}
	public void setResolucion( String resolucion ) {
		this.resolucion = resolucion;
	}
	public String getComponente() {
		return componente;
	}
	public void setComponente( String componente ) {
		this.componente = componente;
	}
	public Integer getSubtitulo() {
		return subtitulo;
	}
	public void setSubtitulo( Integer subtitulo ) {
		this.subtitulo = subtitulo;
	}
	public Long getMonto() {
		return monto;
	}
	public void setMonto( Long monto ) {
		this.monto = monto;
	}
	public String getArchivo() {
		return archivo;
	}
	public void setArchivo( String archivo ) {
		this.archivo = archivo;
	}
	
	
}
