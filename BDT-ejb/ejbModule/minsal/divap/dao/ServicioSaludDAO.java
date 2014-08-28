package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ServicioSalud;



@Singleton
public class ServicioSaludDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public List<ServicioSalud> getServicios() {
		try {
			TypedQuery<ServicioSalud> query = this.em.createNamedQuery("ServicioSalud.findAll", ServicioSalud.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public  ServicioSalud getServicioSaludPorID(int idServicioSalud){
		try {
			TypedQuery<ServicioSalud> query = this.em.createNamedQuery("ServicioSalud.findByIdServicioSalud", ServicioSalud.class);
			query.setParameter("id", idServicioSalud);
			return query.getSingleResult(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
