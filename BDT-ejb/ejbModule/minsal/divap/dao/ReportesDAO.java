package minsal.divap.dao;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.DocumentoReportes;

@Singleton
public class ReportesDAO {
	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	
	public Integer save(DocumentoReportes documentoReportes) {
		em.persist(documentoReportes);
		return documentoReportes.getIdDocumentoReporte();
	}

}
