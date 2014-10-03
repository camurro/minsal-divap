package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Comuna;


@Singleton
public class ComunaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	
	public List<Comuna> getComuna(){
		try {
			TypedQuery<Comuna> query = this.em.createNamedQuery("Comuna.findAll", Comuna.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Comuna getComunaById(int id){
		try {
			TypedQuery<Comuna> query = this.em.createNamedQuery("Comuna.findById", Comuna.class);
			query.setParameter("id", id);
			List<Comuna> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
