package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.TemplatesType;
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
		ReferenciaDocumento referenciaDocumento = new ReferenciaDocumento();
		referenciaDocumento.setContentType(contentType);
		referenciaDocumento.setDocumentoFinal(last);
		referenciaDocumento.setPath(path + "/" + filename);
		this.em.persist(referenciaDocumento);
		return referenciaDocumento.getId();
	}
	
	public Integer createDocumentAlfresco(String nodeRef, String filename, String contentType, boolean last) {
		ReferenciaDocumento referenciaDocumento = new ReferenciaDocumento();
		referenciaDocumento.setContentType(contentType);
		referenciaDocumento.setDocumentoFinal(last);
		referenciaDocumento.setPath(filename);
		referenciaDocumento.setNodeRef(nodeRef);
		this.em.persist(referenciaDocumento);
		return referenciaDocumento.getId();
	}
	
	public ReferenciaDocumento save(ReferenciaDocumento documento) {
		this.em.persist(documento);
		return documento;
	}

	public Integer getPlantillaByType(TemplatesType template) {
		Integer docId = null;
		try {
			TypedQuery<Plantilla> query = this.em.createQuery("select p from Plantilla p WHERE p.tipoPlantilla.idTipoPlantilla = :idTipoPlantilla", Plantilla.class);
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

}
