package minsal.divap.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CargaConvenioComponenteSubtituloVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6029640700623154615L;
	private Integer idConvenio;
	private Integer item;
	private String nombre;
	private ProgramaVO programa;
	private Date fechaResolucion;
	private Integer numeroResolucion;
	private Integer numeroResolucionInicial;
	private Integer convenioInicial;
	private Integer documento;
	private Boolean dependenciaMuncipal;
	private List<ConvenioComponenteSubtituloVO> convenioComponentesSubtitulosVO;
	private List<ServiciosSummaryVO> documentosResoluciones;
	
	public CargaConvenioComponenteSubtituloVO() {
		super();
	}
	
	public Integer getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(Integer idConvenio) {
		this.idConvenio = idConvenio;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	public Date getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Date fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public Integer getNumeroResolucion() {
		return numeroResolucion;
	}

	public void setNumeroResolucion(Integer numeroResolucion) {
		this.numeroResolucion = numeroResolucion;
	}

	public Integer getDocumento() {
		return documento;
	}

	public void setDocumento(Integer documento) {
		this.documento = documento;
	}

	public Boolean getDependenciaMuncipal() {
		return dependenciaMuncipal;
	}

	public void setDependenciaMuncipal(Boolean dependenciaMuncipal) {
		this.dependenciaMuncipal = dependenciaMuncipal;
	}

	public List<ConvenioComponenteSubtituloVO> getConvenioComponentesSubtitulosVO() {
		return convenioComponentesSubtitulosVO;
	}

	public void setConvenioComponentesSubtitulosVO(
			List<ConvenioComponenteSubtituloVO> convenioComponentesSubtitulosVO) {
		this.convenioComponentesSubtitulosVO = convenioComponentesSubtitulosVO;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getNumeroResolucionInicial() {
		return numeroResolucionInicial;
	}

	public void setNumeroResolucionInicial(Integer numeroResolucionInicial) {
		this.numeroResolucionInicial = numeroResolucionInicial;
	}

	public Integer getConvenioInicial() {
		return convenioInicial;
	}

	public void setConvenioInicial(Integer convenioInicial) {
		this.convenioInicial = convenioInicial;
	}

	public List<ServiciosSummaryVO> getDocumentosResoluciones() {
		return documentosResoluciones;
	}

	public void setDocumentosResoluciones(
			List<ServiciosSummaryVO> documentosResoluciones) {
		this.documentosResoluciones = documentosResoluciones;
	}
	
}
