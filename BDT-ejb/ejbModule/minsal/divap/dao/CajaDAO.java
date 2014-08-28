package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transaction;

import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.Email;
import cl.minsal.divap.model.ReferenciaDocumento;



@Singleton
public class CajaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public List<Caja> getByIDPrograma(Integer idPrograma){
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByIdPrograma", Caja.class);
			query.setParameter("idPrograma",idPrograma);
			List<Caja> results = query.getResultList();
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public List<Caja> getByIDProgramaAno(Integer idPrograma, Integer ano){
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByIdProgramaAno", Caja.class);
			query.setParameter("idPrograma",idPrograma);
			query.setParameter("ano",ano);
			List<Caja> results = query.getResultList();
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	//Verificar que devuelve
	//TODO: Guardar la lista
	public List<Caja> save(List<Caja> caja) {

		for (Caja caja2 : caja) {
			this.em.detach(caja2);
			this.em.persist(caja2);
			
		}
		return caja;
	}

	public List<Caja> getByIDProgramaAnoSubtitulo(Integer idPrograma,
			Integer ano, Integer subtitulo) {
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByIdProgramaAnoSubtitulo", Caja.class);
			query.setParameter("idPrograma",idPrograma);
			query.setParameter("ano",ano);
			query.setParameter("idSubtitulo",subtitulo);
			List<Caja> results = query.getResultList();
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	

}
