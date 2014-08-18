package minsal.divap.dao;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.minsal.divap.model.DocumentoDistribucionInicialPercapita;

@Singleton
public class EstimacionFlujoCajaDAO {
	
	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public Integer calcularPropuesta() {
		return 1;
	}

}
