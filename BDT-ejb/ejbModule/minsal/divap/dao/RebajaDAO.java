package minsal.divap.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import minsal.divap.vo.CumplimientoVO;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ComunaCumplimiento;
import cl.minsal.divap.model.ComunaRebaja;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.Rebaja;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.TipoCumplimiento;
import cl.minsal.divap.model.Usuario;



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
	
	public void updateRebajaComuna(Integer idComunaCumplimiento, Double rebajaFinal ){
		try{
		TypedQuery<ComunaRebaja> query = this.em.createNamedQuery("ComunaRebaja.findByIdCumplimiento", ComunaRebaja.class);
		query.setParameter("idComunaCumplimiento", idComunaCumplimiento);
		if(query.getResultList()!=null && query.getResultList().size() > 0){
			ComunaRebaja comunaRebaja = query.getResultList().get(0);
			comunaRebaja.setRebajaFinal(rebajaFinal);
			em.merge(comunaRebaja);
		}
		}catch(RuntimeException e){
			e.printStackTrace();
		}
		
	}
	
	public int deleteComunaRebajaByIdCumplimiento(List<Integer> listaIdCumplimientos){
		Query query = this.em.createNamedQuery("ComunaRebaja.deleteUsingIdCumplimiento");
		query.setParameter("listaIdCumplimientos", listaIdCumplimientos);
		return query.executeUpdate();
	}
	
	public int deleteComunaCumplimientoByIdCumplimiento(List<Integer> listaIdCumplimientos){
		Query query = this.em.createNamedQuery("ComunaCumplimiento.deleteUsingIdCumplimiento");
		query.setParameter("listaIdCumplimientos", listaIdCumplimientos);
		return query.executeUpdate();
	}
	

	public Integer crearIntanciaRebaja(Usuario usuario) {
			try {
				long current = Calendar.getInstance().getTimeInMillis();
				Rebaja dto = new Rebaja();
				dto.setUsuario(usuario);
				dto.setFechaCreacion(new Date(current));
				this.em.persist(dto);
				return dto.getIdRebaja();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	
	public Rebaja findRebajaById(int idRebaja){
		TypedQuery<Rebaja> query = this.em.createNamedQuery("Rebaja.findByIdRebaja", Rebaja.class);
		query.setParameter("idRebaja", idRebaja);
		List<Rebaja> rebajas = query.getResultList();
		if(rebajas!=null && rebajas.size() > 0)
			return rebajas.get(0);
		return null;
	}

	public List<ReferenciaDocumento> getReferenciaDocumentosById(
			List<Integer> allDocuments) {
		TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("ReferenciaDocumento.findByAllId", ReferenciaDocumento.class);
		query.setParameter("listaIdReferencia", allDocuments);
		return query.getResultList(); 
	}



}
