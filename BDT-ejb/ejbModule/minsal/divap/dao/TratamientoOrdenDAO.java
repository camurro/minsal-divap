package minsal.divap.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DistribucionInicialPercapitaSeguimiento;
import cl.minsal.divap.model.DocumentoDistribucionInicialPercapita;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.Usuario;



@Singleton
public class TratamientoOrdenDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public DistribucionInicialPercapita findById(Integer idPrograma) {
		return this.em.find(DistribucionInicialPercapita.class, idPrograma);
	}
	
	
	public List<AntecendentesComunaCalculado> findAntecendentes(Integer idDistribucionInicialPercapita) {
		List<AntecendentesComunaCalculado> results = null;
		try {
			
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByServicioDistribucionInicialPercapita", AntecendentesComunaCalculado.class);
			query.setParameter("idServicio", 1);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			results = query.getResultList(); 
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}
}
