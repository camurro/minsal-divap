package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.TipoSubtitulo;
@Singleton
public class TipoSubtituloDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;


	public  TipoSubtitulo getTipoSubtituloPorID(int idTipoSubtitulo){
		try {
			TypedQuery<TipoSubtitulo> query = this.em.createNamedQuery("TipoSubtitulo.findByIdTipoSubtitulo", TipoSubtitulo.class);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			return query.getSingleResult(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public TipoSubtitulo getTipoSubtituloById(int id){
		try {
			TypedQuery<TipoSubtitulo> query = this.em.createNamedQuery("TipoSubtitulo.findById", TipoSubtitulo.class);
			query.setParameter("idTipoSubtitulo", id);
			List<TipoSubtitulo> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<TipoSubtitulo> getTipoSubtituloByDependencia(int id){
		try {
			TypedQuery<TipoSubtitulo> query = this.em.createNamedQuery("TipoSubtitulo.findByTipoSubtituloByDependencia", TipoSubtitulo.class);
			query.setParameter("idDependenciaPrograma", id);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public TipoSubtitulo getTipoSubtituloByName(String name){
		try {
			TypedQuery<TipoSubtitulo> query = this.em.createNamedQuery("TipoSubtitulo.findByNombreSubtitulo", TipoSubtitulo.class);
			query.setParameter("nombreSubtitulo", name.toLowerCase());
			List<TipoSubtitulo> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
