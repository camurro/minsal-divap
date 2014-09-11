package minsal.divap.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.TipoComuna;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.ServicioSalud;



@Singleton
public class ServicioSaludDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public List<ServicioSalud> getServicios() {
		try {
			TypedQuery<ServicioSalud> query = this.em.createNamedQuery("ServicioSalud.findAll", ServicioSalud.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	 
	public List<AntecendentesComuna> getAntecedentesComunas(Integer anoCurso) {
		try {
			TypedQuery<AntecendentesComuna> query = this.em.createNamedQuery("AntecendentesComuna.findByAnoEnCurso", AntecendentesComuna.class);
			query.setParameter("anoEnCurso", anoCurso);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<AntecendentesComuna> getAntecedentesComunaPercapita(Integer anoCurso, TipoComuna... tiposComuna) {
		try {
			TypedQuery<AntecendentesComuna> query = this.em.createNamedQuery("AntecendentesComuna.findByClasificacionAno", AntecendentesComuna.class);
			List<Integer> clasificaciones = new ArrayList<Integer>();
			for(TipoComuna tipoComuna : tiposComuna){
				clasificaciones.add(tipoComuna.getId());
			}
			query.setParameter("ano", anoCurso);
			query.setParameter("clasificacion", clasificaciones);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Comuna getComunaById(Integer idComuna) {
		try {
			TypedQuery<Comuna> query = this.em.createNamedQuery("Comuna.findById", Comuna.class);
			query.setParameter("id", idComuna);
			if(query.getResultList() != null && query.getResultList().size() > 0){
				return query.getResultList().get(0); 
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ServicioSalud getById(Integer servicio) {
		try {
			TypedQuery<ServicioSalud> query = this.em.createNamedQuery("ServicioSalud.findById", ServicioSalud.class);
			query.setParameter("idServicio", servicio);
			if(query.getResultList() != null && query.getResultList().size() > 0){
				return query.getResultList().get(0); 
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<AntecendentesComuna> getAntecentesComunasRebaja(Integer anoCurso, TipoComuna... tiposComuna) {
		try {
			TypedQuery<AntecendentesComuna> query = this.em.createNamedQuery("AntecendentesComuna.findByClasificacionAno", AntecendentesComuna.class);
			List<Integer> clasificaciones = new ArrayList<Integer>();
			for(TipoComuna tipoComuna : tiposComuna){
				clasificaciones.add(tipoComuna.getId());
			}
			query.setParameter("ano", anoCurso);
			query.setParameter("clasificacion", clasificaciones);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Region> getAllRegion() {
		try {
			TypedQuery<Region> query = this.em.createNamedQuery("Region.findAll", Region.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
