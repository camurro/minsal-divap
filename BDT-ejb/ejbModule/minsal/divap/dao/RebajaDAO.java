package minsal.divap.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import minsal.divap.enums.TipoDocumentosProcesos;
import cl.minsal.divap.model.ComunaCumplimiento;
import cl.minsal.divap.model.ComunaCumplimientoRebaja;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.DocumentoRebaja;
import cl.minsal.divap.model.Rebaja;
import cl.minsal.divap.model.RebajaSeguimiento;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.Seguimiento;
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

	public int persistRebajaComuna(ComunaCumplimientoRebaja comunaCumplimientoRebaja) {
		em.persist(comunaCumplimientoRebaja);
		return comunaCumplimientoRebaja.getComunaCumplimientoRebajaId();

	}

	public List<ComunaCumplimiento> getAllCumplimientoPorMes(int idMes) {
		TypedQuery<ComunaCumplimiento> query = this.em.createNamedQuery("ComunaCumplimiento.findByIdMes", ComunaCumplimiento.class);
		query.setParameter("idMes", idMes);
		return query.getResultList(); 
	}

	public List<ComunaCumplimiento> getComunaCumplimientosByRebaja(Integer idRebaja) {
		TypedQuery<ComunaCumplimiento> query = this.em.createNamedQuery("ComunaCumplimiento.findByRebaja", ComunaCumplimiento.class);
		query.setParameter("idRebaja", idRebaja);
		return query.getResultList(); 
	}

	public List<ComunaCumplimiento> getAllCumplimientoPorComuna(int idComuna) {
		TypedQuery<ComunaCumplimiento> query = this.em.createNamedQuery("ComunaCumplimiento.findByComuna", ComunaCumplimiento.class);
		query.setParameter("idComuna", idComuna);
		return query.getResultList(); 
	}

	public List<ComunaCumplimiento> getRebajasByComunas(Integer rebajaId, List<Integer> listaId) {
		TypedQuery<ComunaCumplimiento> query = this.em.createNamedQuery("ComunaCumplimiento.findByRebajaComunas", ComunaCumplimiento.class);
		query.setParameter("listaId", listaId);
		query.setParameter("idRebaja", rebajaId);
		return query.getResultList();
	}

	public void updateRebajaComuna(Integer idComunaCumplimiento, Double rebajaFinal ){
		try{
			TypedQuery<ComunaCumplimiento> query = this.em.createNamedQuery("ComunaCumplimiento.findByIdComunaCumplimiento", ComunaCumplimiento.class);
			query.setParameter("idComunaCumplimiento", idComunaCumplimiento);
			if(query.getResultList() != null && query.getResultList().size() > 0){
				ComunaCumplimiento comunaCumplimiento = query.getResultList().get(0);
				comunaCumplimiento.getRebajaFinal().setRebaja(rebajaFinal);
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
		TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("ReferenciaDocumento.findByIds", ReferenciaDocumento.class);
		query.setParameter("ids", allDocuments);
		return query.getResultList(); 
	}

	public Integer deleteComunaCumplimientoByIdRebaja(Integer idProcesoRebaja) {
		System.out.println("Inicio deleteComunaCumplimientoByIdRebaja" + idProcesoRebaja);
		Query queryCumplimiento = this.em.createNamedQuery("ComunaCumplimiento.deleteByRebaja");
		queryCumplimiento.setParameter("rebaja", idProcesoRebaja);
		System.out.println("deleteByRebaja para rebaja" + idProcesoRebaja);
		Integer cumplmientosDelete = queryCumplimiento.executeUpdate();

		Query queryCumplimientoRebajaCalculada = this.em.createNamedQuery("ComunaCumplimientoRebaja.deleteByRebajaCalculada");
		queryCumplimientoRebajaCalculada.setParameter("rebaja", idProcesoRebaja);
		System.out.println("deleteByRebajaCalculada " + queryCumplimientoRebajaCalculada.executeUpdate());

		Query queryCumplimientoRebajaFinal = this.em.createNamedQuery("ComunaCumplimientoRebaja.deleteByRebajaFinal");
		queryCumplimientoRebajaFinal.setParameter("rebaja", idProcesoRebaja);

		System.out.println("deleteByRebajaFinal " + queryCumplimientoRebajaFinal.executeUpdate());


		return cumplmientosDelete;
	}

	public Integer save(ComunaCumplimientoRebaja rebaja) {
		em.persist(rebaja);
		return rebaja.getComunaCumplimientoRebajaId();
	}

	public List<ComunaCumplimiento> getCumplimientoByRebejaComuna(
			Integer idRebaja, Integer idComuna) {
		TypedQuery<ComunaCumplimiento> query = this.em.createNamedQuery("ComunaCumplimiento.findByRebajaComuna", ComunaCumplimiento.class);
		query.setParameter("idComuna", idComuna);
		query.setParameter("idRebaja", idRebaja);
		return query.getResultList();
	}

	public Integer save(DocumentoRebaja documentoRebaja) {
		em.persist(documentoRebaja);
		return documentoRebaja.getIdDocumentoRebaja();
	}

	public Integer createSeguimiento(Integer idProcesoRebaja,
			Seguimiento seguimiento) {
		Rebaja rebaja = findRebajaById(idProcesoRebaja);
		RebajaSeguimiento rebajaSeguimiento = new RebajaSeguimiento();
		rebajaSeguimiento.setRebaja(rebaja);
		rebajaSeguimiento.setSeguimiento(seguimiento);
		this.em.persist(rebajaSeguimiento);
		return rebajaSeguimiento.getIdRebajaSeguimiento();
	}

	public List<DocumentoRebaja> getByIdRebajaTipo(Integer idRebaja, TipoDocumentosProcesos tipoDocumentoProceso){
		try{
			TypedQuery<DocumentoRebaja> query = this.em.createNamedQuery("DocumentoRebaja.findByIdRebajaTipo", DocumentoRebaja.class);
			query.setParameter("idRebaja", idRebaja);
			query.setParameter("idTipoDocumento", tipoDocumentoProceso.getId());
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ComunaCumplimiento getCumplimientoById(Integer idCumplimiento) {
		try{
			TypedQuery<ComunaCumplimiento> query = this.em.createNamedQuery("ComunaCumplimiento.findByIdComunaCumplimiento", ComunaCumplimiento.class);
			query.setParameter("idComunaCumplimiento", idCumplimiento);
			List<ComunaCumplimiento> comunaCumplimientos = query.getResultList();
			if(comunaCumplimientos!=null && comunaCumplimientos.size() > 0)
				return comunaCumplimientos.get(0);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
