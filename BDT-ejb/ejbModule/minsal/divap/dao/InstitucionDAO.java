package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Institucion;
@Singleton
public class InstitucionDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;


	public Institucion findById(Integer idInstitucion){
		try {
			TypedQuery<Institucion> query = this.em.createNamedQuery("Institucion.findByIdInstitucion", Institucion.class);
			query.setParameter("idInstitucion", idInstitucion);
			List<Institucion> results = query.getResultList();
			if(results != null && results.size() >0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public List<Institucion> getAll(){
		try {
			TypedQuery<Institucion> query = this.em.createNamedQuery("Institucion.findAll", Institucion.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public Institucion save(Institucion institucion){
		em.persist(institucion);
		return institucion;
	}
}
