package minsal.divap.vo;

import java.io.Serializable;

public class CargaConvenioServicioComponenteVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6029640700623154615L;
	private Integer idConvenioServicio;
	private Integer idEstablecimiento;
	private String nombreEstablecimiento;
	private Boolean nuevo;
	private Boolean reemplazar;
	private Integer numeroResoucion;
	private ComponenteSummaryVO componente;
	private SubtituloSummaryVO subtitulo;
	private DocumentSummaryVO documento;
	private Integer monto;
	private Integer montoIngresado;

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
	
	public Integer getMonto() {
		return monto;
	}
	
	public void setMonto(Integer monto) {
		this.monto = monto;
	}

	public Integer getMontoIngresado() {
		return montoIngresado;
	}

	public void setMontoIngresado(Integer montoIngresado) {
		this.montoIngresado = montoIngresado;
	}
	
	public Boolean getReemplazar() {
		return reemplazar;
	}

	public void setReemplazar(Boolean reemplazar) {
		this.reemplazar = reemplazar;
	}

	@Override
	public String toString() {
		return "CargaConvenioServicioComponenteVO [idConvenioServicio="
				+ idConvenioServicio + ", idEstablecimiento="
				+ idEstablecimiento + ", nombreEstablecimiento="
				+ nombreEstablecimiento + ", nuevo=" + nuevo
				+ ", numeroResoucion=" + numeroResoucion + ", componente="
				+ componente + ", subtitulo=" + subtitulo + ", documento="
				+ documento + ", monto=" + monto + ", montoIngresado="
				+ montoIngresado + "]";
	}
		
}
