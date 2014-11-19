package minsal.divap.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class ReporteEmailsEnviadosVO implements Serializable{


	private static final long serialVersionUID = 6347357139007909950L;
	private Integer idServicio;
	private String nombreServicio;
	private Date fecha;
	private String fechaFormat;
	
	private List<String> to;
	private List<String> cc;
	
	private List<AdjuntosVO> adjuntos;

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

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public List<AdjuntosVO> getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(List<AdjuntosVO> adjuntos) {
		this.adjuntos = adjuntos;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getFechaFormat() {
		return fechaFormat;
	}

	public void setFechaFormat(String fechaFormat) {
		this.fechaFormat = fechaFormat;
	}
	
	
	
	
}
