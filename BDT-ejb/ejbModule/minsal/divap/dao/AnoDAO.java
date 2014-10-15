package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.AnoEnCurso;
@Singleton
public class AnoDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;


	public AnoEnCurso getAnoById(Integer idAno){
		try {
			TypedQuery<AnoEnCurso> query = this.em.createNamedQuery("AnoEnCurso.findByAno", AnoEnCurso.class);
			query.setParameter("ano", idAno);
			List<AnoEnCurso> results = query.getResultList();
			if(results != null && results.size() >0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public List<AnoEnCurso> getAll(){
		try {
			TypedQuery<AnoEnCurso> query = this.em.createNamedQuery("AnoEnCurso.findAll", AnoEnCurso.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
