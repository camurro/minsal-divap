package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Establecimiento;


@Singleton
public class EstablecimientosDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public List<Establecimiento> getEstablecimientosByComuna(Integer idComuna){
		try {
			TypedQuery<Establecimiento> query = this.em.createNamedQuery("Establecimiento.findEstablecimientosByComuna", Establecimiento.class);
			query.setParameter("idComuna", idComuna);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Establecimiento getEstablecimientoById(Integer id){
		try {
			TypedQuery<Establecimiento> query = this.em.createNamedQuery("Establecimiento.findById", Establecimiento.class);
			query.setParameter("idEstablecimiento", id);
			List<Establecimiento> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Establecimiento> getEstablecimientos() {
		try {
			TypedQuery<Establecimiento> query = this.em.createNamedQuery("Establecimiento.findAll", Establecimiento.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Establecimiento> getEstablecimientosByServicio(	Integer idServicio) {
		try {
			TypedQuery<Establecimiento> query = this.em.createNamedQuery("Establecimiento.findEstablecimientosByServicio", Establecimiento.class);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Establecimiento getEstablecimientoByCodigo(String codigoEstablecimiento) {
		try {
			TypedQuery<Establecimiento> query = this.em.createNamedQuery("Establecimiento.findByCodigo", Establecimiento.class);
			query.setParameter("codigo", codigoEstablecimiento);
			List<Establecimiento> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Establecimiento getEstablecimientoServicioAuxiliar(Integer idServicio){
		try {
			TypedQuery<Establecimiento> query = this.em.createNamedQuery("Establecimiento.findEstablecimientoServicioAuxiliar", Establecimiento.class);
			query.setParameter("idServicio", idServicio);
			List<Establecimiento> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
}
