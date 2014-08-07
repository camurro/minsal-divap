package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.ReferenciaDocumento;



@Singleton
public class ReferenciaDocumentoDAO {


	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public ReferenciaDocumento getReferenciaDocumentoById(Integer idDocumento){
		return this.em.find(ReferenciaDocumento.class, idDocumento);
	}

	public List<ReferenciaDocumento> getReferenciaDocumentosById(List<Integer> idDocumentos){
		
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

}
