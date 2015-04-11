package minsal.divap.vo;

import java.io.Serializable;
import java.util.Date;

public class CargaConvenioServicioComponenteVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6029640700623154615L;
	private Integer idConvenioServicio;
	private Integer idEstablecimiento;
	private String nombreEstablecimiento;
	private Boolean nuevo;
	private Integer numeroResoucion;
	private ComponenteSummaryVO componente;
	private SubtituloSummaryVO subtitulo;
	private DocumentSummaryVO documento;
	private Integer montoIngresado;
	private Date fecha;

	public CargaConvenioServicioComponenteVO() {
		super();
	}
	
	public Integer getIdConvenioServicio() {
		return idConvenioServicio;
	}

	public void setIdConvenioServicio(Integer idConvenioServicio) {
		this.idConvenioServicio = idConvenioServicio;
	}
	
	public Integer getNumeroResoucion() {
		return numeroResoucion;
	}

	public void setNumeroResoucion(Integer numeroResoucion) {
		this.numeroResoucion = numeroResoucion;
	}

	public DocumentSummaryVO getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentSummaryVO documento) {
		this.documento = documento;
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
	
	public Boolean getNuevo() {
		return nuevo;
	}

	public void setNuevo(Boolean nuevo) {
		this.nuevo = nuevo;
	}

	public ComponenteSummaryVO getComponente() {
		return componente;
	}

	public void setComponente(ComponenteSummaryVO componente) {
		this.componente = componente;
	}

	public SubtituloSummaryVO getSubtitulo() {
		return subtitulo;
	}
	
	public void setSubtitulo(SubtituloSummaryVO subtitulo) {
		this.subtitulo = subtitulo;
	}
	
	public Integer getMontoIngresado() {
		return montoIngresado;
	}

	public void setMontoIngresado(Integer montoIngresado) {
		this.montoIngresado = montoIngresado;
	}
	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "CargaConvenioServicioComponenteVO [idConvenioServicio="
				+ idConvenioServicio + ", idEstablecimiento="
				+ idEstablecimiento + ", nombreEstablecimiento="
				+ nombreEstablecimiento + ", nuevo=" + nuevo
				+ ", numeroResoucion=" + numeroResoucion + ", componente="
				+ componente + ", subtitulo=" + subtitulo + ", documento="
				+ documento + ", montoIngresado="
				+ montoIngresado + "]";
	}
		
}
