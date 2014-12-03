package minsal.divap.vo;

import java.io.Serializable;

public class CargaConvenioComunaComponenteVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6029640700623154615L;
	private Integer idConvenioComuna;
	private Integer idComuna;
	private String nombreComuna;
	private Boolean nuevo;
	private Boolean reemplazar;
	private Integer numeroResoucion;
	private ComponenteSummaryVO componente;
	private SubtituloSummaryVO subtitulo;
	private DocumentSummaryVO documento;
	private Integer monto;
	private Integer montoIngresado;
	private Integer cuotasDescuento;
	
	public CargaConvenioComunaComponenteVO() {
		super();
	}
	
	public Integer getIdConvenioComuna() {
		return idConvenioComuna;
	}

	public void setIdConvenioComuna(Integer idConvenioComuna) {
		this.idConvenioComuna = idConvenioComuna;
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

	public Integer getIdComuna() {
		return idComuna;
	}

	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}

	public String getNombreComuna() {
		return nombreComuna;
	}

	public void setNombreComuna(String nombreComuna) {
		this.nombreComuna = nombreComuna;
	}

	public SubtituloSummaryVO getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(SubtituloSummaryVO subtitulo) {
		this.subtitulo = subtitulo;
	}

	public ComponenteSummaryVO getComponente() {
		return componente;
	}

	public void setComponente(ComponenteSummaryVO componente) {
		this.componente = componente;
	}

	public Integer getMonto() {
		return monto;
	}

	public void setMonto(Integer monto) {
		this.monto = monto;
	}

	public Boolean getNuevo() {
		return nuevo;
	}

	public void setNuevo(Boolean nuevo) {
		this.nuevo = nuevo;
	}

	public Boolean getReemplazar() {
		return reemplazar;
	}

	public void setReemplazar(Boolean reemplazar) {
		this.reemplazar = reemplazar;
	}

	public Integer getMontoIngresado() {
		return montoIngresado;
	}

	public void setMontoIngresado(Integer montoIngresado) {
		this.montoIngresado = montoIngresado;
	}

	public Integer getCuotasDescuento() {
		return cuotasDescuento;
	}

	public void setCuotasDescuento(Integer cuotasDescuento) {
		this.cuotasDescuento = cuotasDescuento;
	}

	@Override
	public String toString() {
		return "CargaConvenioComunaComponenteVO [idConvenioComuna="
				+ idConvenioComuna + ", idComuna=" + idComuna
				+ ", nombreComuna=" + nombreComuna + ", nuevo=" + nuevo
				+ ", reemplazar=" + reemplazar + ", numeroResoucion="
				+ numeroResoucion + ", componente=" + componente
				+ ", subtitulo=" + subtitulo + ", documento=" + documento
				+ ", monto=" + monto + ", montoIngresado=" + montoIngresado
				+ "]";
	}
			
}
