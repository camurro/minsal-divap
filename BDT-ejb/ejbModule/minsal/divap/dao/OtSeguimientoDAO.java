package minsal.divap.dao;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.OrdenTransferencia;
import cl.minsal.divap.model.OtSeguimiento;
import cl.minsal.divap.model.Seguimiento;



@Singleton
public class OtSeguimientoDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	
	public Integer createSeguimiento(Integer idOrdenTransferencia,
			Seguimiento seguimiento) {
		OrdenTransferencia ordenTransferencia = this.em.find(OrdenTransferencia.class, idOrdenTransferencia);
		
		 OtSeguimiento  otSeguimiento = new  OtSeguimiento();
		 otSeguimiento.setOrdenTransferencia(ordenTransferencia);
		 otSeguimiento.setSeguimiento(seguimiento);
		this.em.persist(otSeguimiento);
		return otSeguimiento.getIdOtSeguimiento();
	}

}
