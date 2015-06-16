package minsal.divap.dao;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.minsal.divap.model.OrdenTransferencia;


@Singleton
public class OrdenTransferenciaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public OrdenTransferencia findById(Integer idOrdenTransferencia) {
		return this.em.find(OrdenTransferencia.class, idOrdenTransferencia);
	}

	public Integer save(OrdenTransferencia ordenTransferencia)
	{
		this.em.persist(ordenTransferencia);
		return ordenTransferencia.getIdOrdenTransferencia();
	}

}
