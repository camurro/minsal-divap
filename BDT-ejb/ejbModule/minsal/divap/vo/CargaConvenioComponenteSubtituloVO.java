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
	private ProgramaVO programa;
	private Date fechaResolucion;
	private Integer numeroResolucion;
	private Integer documento;
	private Boolean dependenciaMuncipal;
	private List<ConvenioComponenteSubtituloVO> convenioComponentesSubtitulosVO;
	
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
	
}
