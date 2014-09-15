package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Establecimiento;


@Singleton
public class EstablecimientosDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	
	
	public List<Establecimiento> getEstablecimientosByComuna(Integer comunaId){
		try {
			TypedQuery<Establecimiento> query = this.em.createNamedQuery("Establecimiento.findEstablecimientosByComuna", Establecimiento.class);
			query.setParameter("id", comunaId);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Establecimiento getEstablecimientoById(int id){
		try {
			TypedQuery<Establecimiento> query = this.em.createNamedQuery("Establecimiento.findById", Establecimiento.class);
			query.setParameter("id", id);
			List<Establecimiento> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
