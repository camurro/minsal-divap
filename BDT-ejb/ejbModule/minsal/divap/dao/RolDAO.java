package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Rol;



@Singleton
public class RolDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public List<Rol> getRoles() {
		try {
			TypedQuery<Rol> query = this.em.createNamedQuery("Rol.findAll", Rol.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Rol getRolByNombre(String nombre) {
		try {
			TypedQuery<Rol> query = this.em.createNamedQuery("Rol.findByNombre", Rol.class);
			query.setParameter("nombre", nombre);
			List<Rol> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
