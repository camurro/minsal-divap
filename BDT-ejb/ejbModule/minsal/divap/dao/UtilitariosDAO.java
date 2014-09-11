package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.ServicioSalud;



@Singleton
public class UtilitariosDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public List<Region> getAllRegion() {
		try {
			TypedQuery<Region> query = this.em.createNamedQuery("Region.findAll", Region.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<ServicioSalud> getServiciosByRegion(int idRegion) {
		try {
			TypedQuery<ServicioSalud> query = this.em.createNamedQuery("ServicioSalud.findServiciosByRegion", ServicioSalud.class);
			query.setParameter("idRegion", idRegion);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Comuna> getComunasByServicio(int idServicio){
		try {
			TypedQuery<Comuna> query = this.em.createNamedQuery("Comuna.findByServicio", Comuna.class);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Comuna> getComunasCumplimientoByServicio(Integer idServicio){
		try {
			TypedQuery<Comuna> query = this.em.createNamedQuery("ComunaCumplimiento.findByServicio", Comuna.class);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ServicioSalud> getServicios() {
		try {
			TypedQuery<ServicioSalud> query = this.em.createNamedQuery("ServicioSalud.findAll", ServicioSalud.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
