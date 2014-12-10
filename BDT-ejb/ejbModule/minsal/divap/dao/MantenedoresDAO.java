package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.FactorRefAsigZona;
import cl.minsal.divap.model.FactorTramoPobreza;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.TipoCumplimiento;
import cl.minsal.divap.model.Tramo;


@Singleton
public class MantenedoresDAO {
	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public List<FactorRefAsigZona> getFactorRefAsigZonaAll(){
		try {
			TypedQuery<FactorRefAsigZona> query = this.em.createNamedQuery("FactorRefAsigZona.findAll", FactorRefAsigZona.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public FactorRefAsigZona getFactorRefAsigZonaById(Integer idFactorRefAsigZona){
		try {
			TypedQuery<FactorRefAsigZona> query = this.em.createNamedQuery("FactorRefAsigZona.findByIdFactorRefAsigZona", FactorRefAsigZona.class);
			query.setParameter("idFactorRefAsigZona", idFactorRefAsigZona);
			List<FactorRefAsigZona> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<FactorTramoPobreza> getFactorTramoPobrezaAll(){
		try {
			TypedQuery<FactorTramoPobreza> query = this.em.createNamedQuery("FactorTramoPobreza.findAll", FactorTramoPobreza.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public FactorTramoPobreza getFactorTramoPobrezaById(Integer idFactorTramoPobreza){
		try {
			TypedQuery<FactorTramoPobreza> query = this.em.createNamedQuery("FactorTramoPobreza.findByIdFactorTramoPobreza", FactorTramoPobreza.class);
			query.setParameter("idFactorTramoPobreza", idFactorTramoPobreza);
			List<FactorTramoPobreza> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Cuota> getCuotasByProgramaAno(Integer idProgramaAno) {
		try{
			TypedQuery<Cuota> query = this.em.createNamedQuery("Cuota.findByIdProgramaAno", Cuota.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			if(query.getResultList() != null){
				return query.getResultList();
			}else{
				return null;
			}
			
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}	
	}
	
	public Cuota getCuotaByIdProgramaAnoNroCuota(Integer idProgramaAno, Short numeroCuota){
		try{
			TypedQuery<Cuota> query = this.em.createNamedQuery("Cuota.findByIdProgramaAnoNroCuota", Cuota.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("numeroCuota", numeroCuota);
			List<Cuota> results = query.getResultList();
			if(results != null && results.size()>0) {
				return results.get(0);
			}
			return null;
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}		
	}
	
	public Cuota getCuotaByIdCuota(Integer id){
		try{
			TypedQuery<Cuota> query = this.em.createNamedQuery("Cuota.findById", Cuota.class);
			query.setParameter("id", id);
			List<Cuota> results = query.getResultList();
			if(results != null && results.size()>0) {
				return results.get(0);
			}
			return null;
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}		
	}

	
	
	public ProgramaAno getProgramaAnoByIdPrograma(Integer idPrograma){
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByIdPrograma", ProgramaAno.class);
			query.setParameter("idPrograma", idPrograma);

			List <ProgramaAno> programas = query.getResultList(); 
			if(programas != null && programas.size() > 0){
				return programas.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Cumplimiento> getCumplimientoAll(){
		try {
			TypedQuery<Cumplimiento> query = this.em.createNamedQuery("Cumplimiento.findAll", Cumplimiento.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Cumplimiento getCumplimientoById(Integer idCumplimiento){
		try {
			TypedQuery<Cumplimiento> query = this.em.createNamedQuery("Cumplimiento.findByIdCumplimiento", Cumplimiento.class);
			query.setParameter("idCumplimiento", idCumplimiento);
			List<Cumplimiento> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Tramo> getTramoAll(){
		try {
			TypedQuery<Tramo> query = this.em.createNamedQuery("Tramo.findAll", Tramo.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Tramo getTramoById(Integer idTramo){
		try {
			TypedQuery<Tramo> query = this.em.createNamedQuery("Tramo.findByIdTramo", Tramo.class);
			query.setParameter("idTramo", idTramo);
			List<Tramo> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public TipoCumplimiento getTipoCumplimientoById(Integer idTipoCumplimiento){
		try {
			TypedQuery<TipoCumplimiento> query = this.em.createNamedQuery("TipoCumplimiento.findByIdTipoCumplimiento", TipoCumplimiento.class);
			query.setParameter("idTipoCumplimiento", idTipoCumplimiento);
			List<TipoCumplimiento> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
