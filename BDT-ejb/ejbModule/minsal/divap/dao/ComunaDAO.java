package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.TipoComuna;


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
	
	public Comuna getComunaById(Integer id){
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

	public List<Comuna> getComunasByServicio(Integer idServicio) {
		try {
			TypedQuery<Comuna> query = this.em.createNamedQuery("Comuna.findByServicio", Comuna.class);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<TipoComuna> getAllTipoComuna(){
		try {
			TypedQuery<TipoComuna> query = this.em.createNamedQuery("TipoComuna.findAll", TipoComuna.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public TipoComuna getTipoComunaById(Integer idTipoComuna){
		try {
			TypedQuery<TipoComuna> query = this.em.createNamedQuery("TipoComuna.findByIdTipoComuna", TipoComuna.class);
			query.setParameter("idTipoComuna", idTipoComuna);
			List<TipoComuna> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Comuna> getComunaServicioAuxiliares(Integer idServicio){
		try {
			TypedQuery<Comuna> query = this.em.createNamedQuery("Comuna.findComunaServicioAuxiliar", Comuna.class);
			query.setParameter("idServicio", idServicio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
}
