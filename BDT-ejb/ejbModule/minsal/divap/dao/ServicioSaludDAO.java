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
import cl.minsal.divap.model.Persona;
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
	
	public List<ServicioSalud> getServiciosOrderId() {
		try {
			TypedQuery<ServicioSalud> query = this.em.createNamedQuery("ServicioSalud.findAllOrderId", ServicioSalud.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public  ServicioSalud getServicioSaludPorID(Integer idServicioSalud){
		try {
			System.out.println("getServicioSaludPorID idServicioSalud="+idServicioSalud);
			TypedQuery<ServicioSalud> query = this.em.createNamedQuery("ServicioSalud.findByIdServicioSalud", ServicioSalud.class);
			query.setParameter("idServicioSalud", idServicioSalud);
			List<ServicioSalud> result =  query.getResultList();
			System.out.println("result result="+result);
			if(result != null && result.size() >0 ){
				return result.get(0);
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public  ServicioSalud getServicioSaludById(Integer idServicioSalud){
		try {
			TypedQuery<ServicioSalud> query = this.em.createNamedQuery("ServicioSalud.findByIdServicioSalud", ServicioSalud.class);
			query.setParameter("idServicioSalud", idServicioSalud);
			List<ServicioSalud> results = query.getResultList();
			if(results != null && results.size() >0){
				return results.get(0);
			}
			return null; 
		}catch(Exception e){
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
	
	
	public List<Persona> getAllPersonas(){
		try {
			TypedQuery<Persona> query = this.em.createNamedQuery("Persona.findAll", Persona.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Persona getPersonaById(Integer idPersona){
		try {
			TypedQuery<Persona> query = this.em.createNamedQuery("Persona.findByIdPersona", Persona.class);
			query.setParameter("idPersona", idPersona);
			if(query.getResultList() != null && query.getResultList().size() > 0){
				return query.getResultList().get(0); 
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Region getRegionById(Integer idRegion) {
		try {
			TypedQuery<Region> query = this.em.createNamedQuery("Region.findById", Region.class);
			query.setParameter("idRegion", idRegion);
			if(query.getResultList() != null && query.getResultList().size() > 0){
				return query.getResultList().get(0); 
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
