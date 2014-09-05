package minsal.divap.dao;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Mes;
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

}
