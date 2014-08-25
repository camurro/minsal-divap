package minsal.divap.vo;

import java.io.Serializable;

import minsal.divap.enums.TipoDocumentosProcesos;

public class ReferenciaDocumentoVO extends ReferenciaDocumentoSummaryVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4384238848838363601L;
	private TipoDocumentosProcesos tipoDocumentoProceso;
	private String comuna;
	private String servicio;
	
	public ReferenciaDocumentoVO(Integer id, String path, String nodeRef, TipoDocumentosProcesos tipoDocumentoProceso, String comuna) {
		super(id, path, nodeRef);
		this.tipoDocumentoProceso = tipoDocumentoProceso;
		this.comuna = comuna;
	}
	
	public ReferenciaDocumentoVO(Integer id, String path, String nodeRef, Integer tipoDocumentoProceso, String comuna) {
		super(id, path, nodeRef);
		this.tipoDocumentoProceso = TipoDocumentosProcesos.getById(tipoDocumentoProceso);
		this.comuna = comuna;
	}
	
	public ReferenciaDocumentoVO(Integer id, String path, String nodeRef, Integer tipoDocumentoProceso) {
		super(id, path, nodeRef);
		this.tipoDocumentoProceso = TipoDocumentosProcesos.getById(tipoDocumentoProceso);
	}

	public TipoDocumentosProcesos getTipoDocumentoProceso() {
		return tipoDocumentoProceso;
	}

	public void setTipoDocumentoProceso(TipoDocumentosProcesos tipoDocumentoProceso) {
		this.tipoDocumentoProceso = tipoDocumentoProceso;
	}

	public String getComuna() {
		return comuna;
	}

	public void setComuna(String comuna) {
		this.comuna = comuna;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	@Override
	public String toString() {
		return "ReferenciaDocumentoVO [tipoDocumentoProceso="
				+ tipoDocumentoProceso + ", comuna=" + comuna + ", getId()="
				+ getId() + ", getPath()=" + getPath() + ", getNodeRef()="
				+ getNodeRef() + "]";
	}
	
}
