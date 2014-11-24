package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Festivos;
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
	
	public List<Comuna> getComunasCumplimientoByRebajaServicio(Integer idServicio){
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

	public Festivos findFestivoByDiaMesAno(Integer mes, int dia,
			Integer ano) {
		try {
			TypedQuery<Festivos> query = this.em.createNamedQuery("Festivos.findByDiaMesAno", Festivos.class);
			query.setParameter("dia", dia);
			query.setParameter("mes", mes);
			query.setParameter("ano", ano);
			List<Festivos> result = query.getResultList(); 
			if(result != null && result.size() > 0){
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
