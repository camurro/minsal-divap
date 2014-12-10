package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.Dependencia;
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
	
	public TipoSubtitulo getTipoSubtituloById(Integer id){
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

	public Double getInfactorById(int idSubtitulo) {
		try {
			TypedQuery<TipoSubtitulo> query = this.em.createNamedQuery("TipoSubtitulo.findById", TipoSubtitulo.class);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			List<TipoSubtitulo> results = query.getResultList();
			if (results.size() >= 1)
				return Double.valueOf(results.get(0).getInflactor());
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<TipoSubtitulo> getTipoSubtituloAll(){
		try {
			TypedQuery<TipoSubtitulo> query = this.em.createNamedQuery("TipoSubtitulo.findAll", TipoSubtitulo.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Dependencia> getDependenciaAll(){
		try {
			TypedQuery<Dependencia> query = this.em.createNamedQuery("DependenciaPrograma.findAll", Dependencia.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Dependencia getDependenciaById(Integer idDependenciaPrograma){
		try {
			TypedQuery<Dependencia> query = this.em.createNamedQuery("DependenciaPrograma.findByIdDependenciaPrograma", Dependencia.class);
			query.setParameter("idDependenciaPrograma", idDependenciaPrograma);
			List<Dependencia> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ComponenteSubtitulo> getByIdComponente(Integer idComponente) {
		try {
			TypedQuery<ComponenteSubtitulo> query = this.em.createNamedQuery("ComponenteSubtitulo.findByIdComponente", ComponenteSubtitulo.class);
			query.setParameter("idComponente", idComponente);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
