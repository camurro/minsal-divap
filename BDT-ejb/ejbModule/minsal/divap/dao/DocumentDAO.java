package minsal.divap.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.TipoDocumentosProcesos;
import cl.minsal.divap.model.DocumentoConvenio;
import cl.minsal.divap.model.DocumentoConvenioComuna;
import cl.minsal.divap.model.DocumentoConvenioServicio;
import cl.minsal.divap.model.DocumentoDistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.DocumentoModificacionPercapita;
import cl.minsal.divap.model.DocumentoOt;
import cl.minsal.divap.model.DocumentoProgramasReforzamiento;
import cl.minsal.divap.model.DocumentoRebaja;
import cl.minsal.divap.model.DocumentoReportes;
import cl.minsal.divap.model.Plantilla;
import cl.minsal.divap.model.ReferenciaDocumento;



@Singleton
public class DocumentDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public ReferenciaDocumento findById(Integer documentId) {
		return this.em.find(ReferenciaDocumento.class, documentId);
	}

	public Integer createDocument(String path, String filename, String contentType, boolean last) {
		long current = Calendar.getInstance().getTimeInMillis();
		ReferenciaDocumento referenciaDocumento = new ReferenciaDocumento();
		referenciaDocumento.setContentType(contentType);
		referenciaDocumento.setDocumentoFinal(last);
		referenciaDocumento.setFechaCreacion(new Date(current));
		referenciaDocumento.setPath(path + "/" + filename);
		this.em.persist(referenciaDocumento);
		return referenciaDocumento.getId();
	}

	public Integer createDocumentAlfresco(String nodeRef, String filename, String contentType, boolean last) {
		long current = Calendar.getInstance().getTimeInMillis();
		ReferenciaDocumento referenciaDocumento = new ReferenciaDocumento();
		referenciaDocumento.setContentType(contentType);
		referenciaDocumento.setDocumentoFinal(last);
		referenciaDocumento.setFechaCreacion(new Date(current));
		referenciaDocumento.setPath(filename);
		referenciaDocumento.setNodeRef(nodeRef);
		this.em.persist(referenciaDocumento);
		return referenciaDocumento.getId();
	}

	public ReferenciaDocumento save(ReferenciaDocumento documento) {
		this.em.persist(documento);
		return documento;
	}

	public Integer getPlantillaByType(TipoDocumentosProcesos template) {
		Integer docId = null;
		try {
			TypedQuery<Plantilla> query = this.em.createQuery("select p from Plantilla p WHERE p.tipoPlantilla.idTipoDocumento = :idTipoPlantilla order by p.fechaCreacion desc", Plantilla.class);
			query.setParameter("idTipoPlantilla", template.getId());
			List<Plantilla> plantillas = query.getResultList(); 
			if(plantillas != null && plantillas.size() > 0){
				docId = plantillas.get(0).getIdPlantilla();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return docId;
	}

	public Integer save(Plantilla plantilla) {
		this.em.persist(plantilla);
		return plantilla.getIdPlantilla();
	}

	public Integer getDocumentoIdByPlantillaId(Integer plantillaId) {
		System.out.println("getDocumentoIdByPlantillaId("+plantillaId+")");
		Plantilla plantilla = this.em.find(Plantilla.class, plantillaId);
		if(plantilla != null){
			return plantilla.getDocumento().getId();
		}
		return null;
	}

	public ReferenciaDocumento getDocumentByPlantillaId(Integer plantillaId) {
		System.out.println("getDocumentoByPlantillaId("+plantillaId+")");
		ReferenciaDocumento referenciaDocumento = null;
		try {
			TypedQuery<Plantilla> query = this.em.createNamedQuery("Plantilla.findByIdPlantilla", Plantilla.class);
			query.setParameter("idPlantilla", plantillaId);
			List<Plantilla> plantillas = query.getResultList(); 
			if(plantillas != null && plantillas.size() > 0){
				referenciaDocumento = plantillas.get(0).getDocumento();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return referenciaDocumento;
	}

	public ReferenciaDocumento getDocumentByTypeDistribucionInicialPercapita(Integer idDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumentoProceso) {
		ReferenciaDocumento referenciaDocumento = null;
		try {
			TypedQuery<DocumentoDistribucionInicialPercapita> query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.findByTypeIdDistribucionInicialPercapita", DocumentoDistribucionInicialPercapita.class);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTipoDocumento", tipoDocumentoProceso.getId());
			List<DocumentoDistribucionInicialPercapita> referenciasDocumentos = query.getResultList(); 
			if(referenciasDocumentos != null && referenciasDocumentos.size() > 0){
				referenciaDocumento = referenciasDocumentos.get(0).getIdDocumento();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return referenciaDocumento;
	}
	public List<ReferenciaDocumento> getDocumentByTypesServicioDistribucionInicialPercapita(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		List<ReferenciaDocumento> result = new ArrayList<ReferenciaDocumento>();
		try {
			List<Integer> tipos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumentosProcesos: tiposDocumentoProceso){
				tipos.add(tipoDocumentosProcesos.getId());
			}
			TypedQuery<DocumentoDistribucionInicialPercapita> query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.findByTypesIdServicioIdDistribucionInicialPercapita", DocumentoDistribucionInicialPercapita.class);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTiposDocumento", tipos);
			query.setParameter("idServicio", idServicio);
			List<DocumentoDistribucionInicialPercapita> referenciasDocumentos = query.getResultList(); 
			if(referenciasDocumentos != null && referenciasDocumentos.size() > 0){
				for(DocumentoDistribucionInicialPercapita documentoDistribucionInicialPercapita : referenciasDocumentos){
					result.add(documentoDistribucionInicialPercapita.getIdDocumento());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<ReferenciaDocumento> getReferenciasDocumentosById(List<Integer> idDocumentos){
		try {
			TypedQuery<ReferenciaDocumento> query = this.em
					.createNamedQuery("ReferenciaDocumento.findByIds", 
							ReferenciaDocumento.class);
			query.setParameter("ids", idDocumentos);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public ReferenciaDocumento getLastDocumentByTypeDistribucionInicialPercapita(
			Integer idDistribucionInicialPercapita,
			TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumento referenciaDocumento = null;
		try {
			TypedQuery<DocumentoDistribucionInicialPercapita> query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.findLastByTypeIdDistribucionInicialPercapita", DocumentoDistribucionInicialPercapita.class);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			List<DocumentoDistribucionInicialPercapita> referenciasDocumentos = query.getResultList(); 
			if(referenciasDocumentos != null && referenciasDocumentos.size() > 0){
				referenciaDocumento = referenciasDocumentos.get(0).getIdDocumento();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return referenciaDocumento;
	}

	public ReferenciaDocumento getLastDocumentByTypeEstimacionFlujoCaja(Integer idProgramaAno, TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumento referenciaDocumento = null;
		try {
			TypedQuery<DocumentoEstimacionflujocaja> query = this.em.createNamedQuery("DocumentoEstimacionflujocaja.findByTypesIdProgramaAno", DocumentoEstimacionflujocaja.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			List<DocumentoEstimacionflujocaja> referenciasDocumentos = query.getResultList(); 
			if(referenciasDocumentos != null && referenciasDocumentos.size() > 0){
				referenciaDocumento = referenciasDocumentos.get(0).getIdDocumento();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return referenciaDocumento;
	}

	public ReferenciaDocumento getLastDocumentByTipoDocumentoEstimacionFlujoCaja(TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumento referenciaDocumento = null;
		try {
			TypedQuery<DocumentoEstimacionflujocaja> query = this.em.createNamedQuery("DocumentoEstimacionflujocaja.findByTipoDocumento", DocumentoEstimacionflujocaja.class);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			List<DocumentoEstimacionflujocaja> referenciasDocumentos = query.getResultList();
			if(referenciasDocumentos != null && referenciasDocumentos.size() > 0){
				referenciaDocumento = referenciasDocumentos.get(0).getIdDocumento();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return referenciaDocumento;
	}


	public List<DocumentoDistribucionInicialPercapita> getDocumentosByTypeServicioDistribucionInicialPercapita(
			Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		try {
			TypedQuery<DocumentoDistribucionInicialPercapita> query = null;
			List<Integer> tipos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumentosProcesos: tiposDocumentoProceso){
				tipos.add(tipoDocumentosProcesos.getId());
			}
			if(idServicio == null){
				query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.findByTypesIdDistribucionInicialPercapita", DocumentoDistribucionInicialPercapita.class);
			}else{
				query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.findByTypesServicioIdDistribucionInicialPercapita", DocumentoDistribucionInicialPercapita.class);
				query.setParameter("idServicio", idServicio);
			}
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTiposDocumento", tipos);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ReferenciaDocumento getLastDocumentByOrdinarioOrdenTransferencia(
			Integer idOrdenTransferencia,Integer idTipoDocumento) {
		ReferenciaDocumento referenciaDocumento = null;
		try {
			TypedQuery<DocumentoOt> query = this.em.createNamedQuery("DocumentoOt.findLastByidOrdenTransferencia", DocumentoOt.class);
			query.setParameter("idOrdenTransferencia", idOrdenTransferencia);
			query.setParameter("idTipoDocumento", idTipoDocumento);

			List<DocumentoOt> referenciasDocumentos = query.getResultList();
			if(referenciasDocumentos != null && referenciasDocumentos.size() > 0){
				referenciaDocumento = referenciasDocumentos.get(0).getIdDocumento();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return referenciaDocumento;
	}

	public List<DocumentoConvenioComuna> getDocumentPopUpConvenioComuna(Integer idConvenioComuna) {
		try {
			TypedQuery<DocumentoConvenioComuna> query = this.em.createNamedQuery("DocumentoConvenioComuna.findByIdConvenioComuna", DocumentoConvenioComuna.class);
			query.setParameter("idConvenioComuna", idConvenioComuna);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DocumentoConvenioServicio> getDocumentPopUpConvenioServicio(Integer idConvenioServicio) {
		try {
			TypedQuery<DocumentoConvenioServicio> query = this.em.createNamedQuery("DocumentoConvenioServicio.findByIdConvenioServicio", DocumentoConvenioServicio.class);
			query.setParameter("idConvenioServicio", idConvenioServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer getPlantillaByTypeAndProgram(TipoDocumentosProcesos tipoDocumentoProceso, Integer programaSeleccionado) {
		Integer docId = null;
		try {
			TypedQuery<Plantilla> query = this.em.createQuery("select p from Plantilla p WHERE p.tipoPlantilla.idTipoDocumento = :idTipoPlantilla and p.idPrograma.id = :idPrograma", Plantilla.class);
			query.setParameter("idTipoPlantilla", tipoDocumentoProceso.getId());
			query.setParameter("idPrograma", programaSeleccionado);
			List<Plantilla> plantillas = query.getResultList(); 
			if(plantillas != null && plantillas.size() > 0){
				docId = plantillas.get(0).getIdPlantilla();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return docId;
	}
	public DocumentoEstimacionflujocaja getDocumentByTypProgramaAno(Integer idProgramaAno, TipoDocumentosProcesos tipoDocumentoProceso) {
		try {
			TypedQuery<DocumentoEstimacionflujocaja> query = this.em.createNamedQuery("DocumentoEstimacionflujocaja.findByTypesIdProgramaAno", DocumentoEstimacionflujocaja.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idTipoDocumento", tipoDocumentoProceso.getId());
			List<DocumentoEstimacionflujocaja> results = query.getResultList(); 
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer getPlantillaByType(TipoDocumentosProcesos template, Integer idPrograma) {
		Integer docId = null;
		try {
			TypedQuery<Plantilla> query = this.em.createQuery("select p from Plantilla p WHERE p.tipoPlantilla.idTipoDocumento = :idTipoPlantilla and p.idPrograma.id = :idPrograma", Plantilla.class);
			query.setParameter("idTipoPlantilla", template.getId());
			query.setParameter("idPrograma", idPrograma);
			List<Plantilla> plantillas = query.getResultList(); 
			if(plantillas != null && plantillas.size() > 0){
				docId = plantillas.get(0).getIdPlantilla();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return docId;
	}

	public ReferenciaDocumento getDocumentByTypeAnoReportes(TipoDocumentosProcesos tipoDocumentoProceso, Integer ano) {
		try {
			TypedQuery<DocumentoReportes> query = this.em.createNamedQuery("DocumentoReportes.findByTypesAno", DocumentoReportes.class);
			query.setParameter("idTipoDocumento", tipoDocumentoProceso.getId());
			query.setParameter("ano", ano);
			List<DocumentoReportes> results = query.getResultList(); 
			if(results != null && results.size() > 0){
				return results.get(0).getDocumento();
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ReferenciaDocumento getLastDocumentoSummaryByResolucionAPSType(
			Integer idProgramaAno, TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumento referenciaDocumento = null;
		try {
			TypedQuery<DocumentoProgramasReforzamiento> query = this.em.createNamedQuery("DocumentoProgramasReforzamiento.findByProgramaAnoTipoDocumento", DocumentoProgramasReforzamiento.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			List<DocumentoProgramasReforzamiento> referenciasDocumentos = query.getResultList(); 
			if(referenciasDocumentos != null && referenciasDocumentos.size() > 0){
				referenciaDocumento = referenciasDocumentos.get(0).getIdDocumento();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return referenciaDocumento;
	}

	public DocumentoConvenioComuna save(DocumentoConvenioComuna documentoConvenioComuna) {
		this.em.persist(documentoConvenioComuna);
		return documentoConvenioComuna;
	}

	public DocumentoConvenioServicio save(DocumentoConvenioServicio documentoConvenioServicio) {
		this.em.persist(documentoConvenioServicio);
		return documentoConvenioServicio;
	}

	public ReferenciaDocumento getLastDocumentByTypeConvenio(Integer idConvenio, TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumento referenciaDocumento = null;
		try {
			TypedQuery<DocumentoConvenio> query = this.em.createNamedQuery("DocumentoConvenio.findByIdConvenioTipoDocumento", DocumentoConvenio.class);
			query.setParameter("idConvenio", idConvenio);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			List<DocumentoConvenio> referenciasDocumentos = query.getResultList(); 
			if(referenciasDocumentos != null && referenciasDocumentos.size() > 0){
				referenciaDocumento = referenciasDocumentos.get(0).getDocumento();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return referenciaDocumento;
	}

	public List<ReferenciaDocumento> getVersionFinalConvenioByType(Integer idConvenio, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoConvenio.findVersionFinalByIdConvenioTipoDocumento", ReferenciaDocumento.class);
			query.setParameter("idConvenio", idConvenio);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ReferenciaDocumento> getVersionFinalModificacionDistribucionInicialByType(Integer idDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoModificacionPercapita.findVersionFinalByIdModificacionDistribucionInicialTipoDocumento", ReferenciaDocumento.class);
			query.setParameter("idModificacionDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ReferenciaDocumento getLastDocumentSummaryModificacionPercapitaByType(Integer idDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumento referenciaDocumento = null;
		try {
			TypedQuery<DocumentoModificacionPercapita> query = this.em.createNamedQuery("DocumentoModificacionPercapita.findByIdModificacionDistribucionTipoDocumento", DocumentoModificacionPercapita.class);
			query.setParameter("idModificacionDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			List<DocumentoModificacionPercapita> referenciasDocumentos = query.getResultList(); 
			if(referenciasDocumentos != null && referenciasDocumentos.size() > 0){
				referenciaDocumento = referenciasDocumentos.get(0).getDocumento();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return referenciaDocumento;
	}

	public List<ReferenciaDocumento> getVersionFinalModificacionPercapitaByType(Integer idDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoModificacionPercapita.findVersionFinalByIdModificacionPercapitaTipoDocumento", ReferenciaDocumento.class);
			query.setParameter("idModificacionDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DocumentoModificacionPercapita> getDocumentosByTypeServicioModificacionPercapita(Integer idModificacionPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		try {
			TypedQuery<DocumentoModificacionPercapita> query = null;
			List<Integer> tipos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumentosProcesos: tiposDocumentoProceso){
				tipos.add(tipoDocumentosProcesos.getId());
			}
			if(idServicio == null){
				query = this.em.createNamedQuery("DocumentoModificacionPercapita.findByTypesIdModificacionPercapita", DocumentoModificacionPercapita.class);
			}else{
				query = this.em.createNamedQuery("DocumentoModificacionPercapita.findByTypesServicioIdModificacionPercapita", DocumentoModificacionPercapita.class);
				query.setParameter("idServicio", idServicio);
			}
			query.setParameter("idModificacionDistribucionInicialPercapita", idModificacionPercapita);
			query.setParameter("idTiposDocumento", tipos);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public DocumentoModificacionPercapita getLastDocumentosByTypeServicioModificacionPercapita(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		try {
			TypedQuery<DocumentoModificacionPercapita> query = null;
			List<Integer> tipos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumentosProcesos: tiposDocumentoProceso){
				tipos.add(tipoDocumentosProcesos.getId());
			}
			query = this.em.createNamedQuery("DocumentoModificacionPercapita.findLastByTypesServicioIdModificacionPercapita", DocumentoModificacionPercapita.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idModificacionDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTiposDocumento", tipos);
			List<DocumentoModificacionPercapita> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public DocumentoDistribucionInicialPercapita getLastDocumentosByTypeServicioDistribucionInicialPercapita(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		try {
			List<Integer> tipos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumentosProcesos: tiposDocumentoProceso){
				tipos.add(tipoDocumentosProcesos.getId());
			}
			TypedQuery<DocumentoDistribucionInicialPercapita> query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.findLastByTypesServicioIdDistribucionInicialPercapita", DocumentoDistribucionInicialPercapita.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTiposDocumento", tipos);
			List<DocumentoDistribucionInicialPercapita> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ReferenciaDocumento> getVersionFinalByServicioModificacionPercapitaTypes(Integer idModificacionPercapita, Integer idServicio, TipoDocumentosProcesos... tipoDocumento) {
		try {
			List<Integer> tipos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumentosProcesos: tipoDocumento){
				tipos.add(tipoDocumentosProcesos.getId());
			}
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoModificacionPercapita.findByServicioModificacionPercapitaTypes", ReferenciaDocumento.class);
			query.setParameter("idModificacionPercapita", idModificacionPercapita);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTiposDocumento", tipos);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ReferenciaDocumento> getVersionFinalByServicioDistribucionInicialPercapitaTypes(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tipoDocumento) {
		try {
			List<Integer> tipos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumentosProcesos: tipoDocumento){
				tipos.add(tipoDocumentosProcesos.getId());
			}
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.findByServicioDistribucionInicialPercapitaTypes", ReferenciaDocumento.class);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTiposDocumento", tipos);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ReferenciaDocumento> getVersionFinalModificacionPercapitaByType(Integer idModificacionPercapita, Integer idServicio, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoModificacionPercapita.findVersionFinalByIdModificacionPercapitaIdServicioTipoDocumento", ReferenciaDocumento.class);
			query.setParameter("idModificacionDistribucionInicialPercapita", idModificacionPercapita);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ReferenciaDocumento> getVersionFinalDistribucionInicialPercapitaByType(Integer idDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.findVersionFinalByIdDistribucionInicialPercapitaTipoDocumento", ReferenciaDocumento.class);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ReferenciaDocumento> getVersionFinalDistribucionInicialPercapitaByType(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.findVersionFinalByIdDistribucionInicialPercapitaIdServicioTipoDocumento", ReferenciaDocumento.class);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public DocumentoRebaja getLastDocumentosByTypeServicioRebaja(Integer idRebaja, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		try {
			List<Integer> tipos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumentosProcesos: tiposDocumentoProceso){
				tipos.add(tipoDocumentosProcesos.getId());
			}
			TypedQuery<DocumentoRebaja> query = this.em.createNamedQuery("DocumentoRebaja.findLastByTypesServicioIdRebaja", DocumentoRebaja.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idRebaja", idRebaja);
			query.setParameter("idTiposDocumento", tipos);
			List<DocumentoRebaja> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DocumentoRebaja> getDocumentosByTypeServicioRebaja(Integer idRebaja, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		try {
			TypedQuery<DocumentoRebaja> query = null;
			List<Integer> tipos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumentosProcesos: tiposDocumentoProceso){
				tipos.add(tipoDocumentosProcesos.getId());
			}
			if(idServicio == null){
				query = this.em.createNamedQuery("DocumentoRebaja.findByTypesIdRebaja", DocumentoRebaja.class);
			}else{
				query = this.em.createNamedQuery("DocumentoRebaja.findByTypesServicioIdRebaja", DocumentoRebaja.class);
				query.setParameter("idServicio", idServicio);
			}
			query.setParameter("idRebaja", idRebaja);
			query.setParameter("idTiposDocumento", tipos);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ReferenciaDocumento> getVersionFinalRebajaByType(Integer idProcesoRebaja, Integer idServicio, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoRebaja.findVersionFinalByIdRebajaIdServicioTipoDocumento", ReferenciaDocumento.class);
			query.setParameter("idRebaja", idProcesoRebaja);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ReferenciaDocumento> getVersionFinalByServicioRebajaTypes(Integer idProcesoRebaja, Integer idServicio, TipoDocumentosProcesos... tipoDocumento) {
		try {
			List<Integer> tipos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumentosProcesos: tipoDocumento){
				tipos.add(tipoDocumentosProcesos.getId());
			}
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoRebaja.findByServicioRebajaTypes", ReferenciaDocumento.class);
			query.setParameter("idRebaja", idProcesoRebaja);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTiposDocumento", tipos);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ReferenciaDocumento> getVersionFinalEstimacionFlujoCajaByType(Integer idProceso, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoEstimacionFlujoCajaConsolidador.findVersionFinalByIdEstimacionFlujoCajaTipoDocumento", ReferenciaDocumento.class);
			query.setParameter("idFlujoCajaConsolidador", idProceso);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ReferenciaDocumento getLastDocumentByTipoDocumentoEstimacionFlujoCaja(Integer idProceso, TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumento referenciaDocumento = null;
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoEstimacionFlujoCajaConsolidador.findLastDocumentByIdEstimacionFlujoCajaIdTipoDocumento", ReferenciaDocumento.class);
			query.setParameter("idFlujoCajaConsolidador", idProceso);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			List<ReferenciaDocumento> referenciasDocumentos = query.getResultList();
			if(referenciasDocumentos != null && referenciasDocumentos.size() > 0){
				referenciaDocumento = referenciasDocumentos.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return referenciaDocumento;
	}
	
	public List<ReferenciaDocumento> getVersionFinalOTByType(Integer idProceso, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoRemesas.findVersionFinalByIdOTTipoDocumento", ReferenciaDocumento.class);
			query.setParameter("idRemesa", idProceso);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ReferenciaDocumento getLastDocumentRecursosFinancierosByType(Integer idProceso, Integer idProgramaAno, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<DocumentoProgramasReforzamiento> query = this.em.createNamedQuery("DocumentoProgramasReforzamiento.findByProgramaAnoDistribucionRecursosTipoDocumento", DocumentoProgramasReforzamiento.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			query.setParameter("idProceso", idProceso);
			List<DocumentoProgramasReforzamiento> referenciasDocumentos = query.getResultList(); 
			if(referenciasDocumentos != null && referenciasDocumentos.size() > 0){
				return referenciasDocumentos.get(0).getIdDocumento();
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ReferenciaDocumento> getVersionFinalRecursosFinancierosByType(Integer idProceso, Integer idProgramaAno, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoProgramasReforzamiento.findVersionFinalByIdRecursosFinancierosTipoDocumento", ReferenciaDocumento.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			query.setParameter("idProceso", idProceso);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
