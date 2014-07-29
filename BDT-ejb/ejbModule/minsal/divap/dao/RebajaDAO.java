package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.vo.CumplimientoVO;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ComunaCumplimiento;
import cl.minsal.divap.model.ComunaRebaja;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.TipoCumplimiento;



@Singleton
public class RebajaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public List<TipoCumplimiento> getAllTipoCumplimiento(){
		TypedQuery<TipoCumplimiento> query = this.em.createNamedQuery("TipoCumplimiento.findAll", TipoCumplimiento.class);
		return query.getResultList(); 
	}

	public int persistCumplimientoComunas(ComunaCumplimiento item) {
		em.persist(item);
		return item.getIdComunaCumplimiento();
	}

	public List<Cumplimiento> getAllCumplimientoByTipoCumplimiento(int idTipoCumplimiento){
		TypedQuery<Cumplimiento> query = this.em.createNamedQuery("Cumplimiento.findByIdTipoCumplimiento", Cumplimiento.class);
		query.setParameter("idTipoCumplimiento", idTipoCumplimiento);
		return query.getResultList(); 
	}

	public int persistRebajaComuna(ComunaRebaja comunaRebaja) {
		em.persist(comunaRebaja);
		return comunaRebaja.getIdComunaRebaja();
		
	}

	public List<ComunaCumplimiento> getAllCumplimientoPorMes(int idMes) {
		TypedQuery<ComunaCumplimiento> query = this.em.createNamedQuery("ComunaCumplimiento.findByIdMes", ComunaCumplimiento.class);
		query.setParameter("idMes", idMes);
		return query.getResultList(); 
	}
	
	public List<ComunaCumplimiento> getAllCumplimientoPorComuna(int idComuna) {
		TypedQuery<ComunaCumplimiento> query = this.em.createNamedQuery("ComunaCumplimiento.findByComuna", ComunaCumplimiento.class);
		query.setParameter("idComuna", idComuna);
		return query.getResultList(); 
	}

	public List<Comuna> getRebajasByComunas(List<Integer> listaId, int idMes) {
		TypedQuery<Comuna> query = this.em.createNamedQuery("Comuna.findRebajasByComunas", Comuna.class);
		query.setParameter("listaId", listaId);
		query.setParameter("idMes", idMes);
		return query.getResultList();
	}

	public List<ComunaRebaja> getallRebajaByComuna(Integer idComunaRebaja) {
		TypedQuery<ComunaRebaja> query = this.em.createNamedQuery("ComunaRebaja.findByIdComunaRebaja", ComunaRebaja.class);
		query.setParameter("idComunaRebaja", idComunaRebaja);
		return query.getResultList(); 
	}

}
