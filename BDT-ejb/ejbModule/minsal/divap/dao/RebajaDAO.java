package minsal.divap.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.enums.TiposCumplimientos;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.ComunaCumplimiento;
import cl.minsal.divap.model.ComunaCumplimientoRebaja;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.DocumentoRebaja;
import cl.minsal.divap.model.Rebaja;
import cl.minsal.divap.model.RebajaCorte;
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


	public Integer crearIntanciaRebaja(Usuario usuario, RebajaCorte rebajaCorte, AnoEnCurso anoCurso) {
		try {
			long current = Calendar.getInstance().getTimeInMillis();
			Rebaja rebaja = new Rebaja();
			rebaja.setUsuario(usuario);
			rebaja.setAno(anoCurso);
			rebaja.setRebajaCorte(rebajaCorte);
			rebaja.setFechaCreacion(new Date(current));
			this.em.persist(rebaja);
			return rebaja.getIdRebaja();
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
		System.out.println("Inicio deleteComunaCumplimientoByIdRebaja " + idProcesoRebaja);
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

	public TipoCumplimiento getCumplimientoByType(TiposCumplimientos tiposCumplimiento) {
		try{
			TypedQuery<TipoCumplimiento> query = this.em.createNamedQuery("TipoCumplimiento.findByIdTipoCumplimiento", TipoCumplimiento.class);
			query.setParameter("idTipoCumplimiento", tiposCumplimiento.getId());
			List<TipoCumplimiento> tipoCumplimientos = query.getResultList();
			if(tipoCumplimientos!=null && tipoCumplimientos.size() > 0)
				return tipoCumplimientos.get(0);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public RebajaCorte getCorteByMes(Integer mesCorte) {
		try{
			TypedQuery<RebajaCorte> query = this.em.createNamedQuery("RebajaCorte.findByMes", RebajaCorte.class);
			query.setParameter("mesCorte", mesCorte);
			List<RebajaCorte> cortes = query.getResultList();
			if(cortes != null && cortes.size() > 0){
				return cortes.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Rebaja> findRebajaByByUsuarioAno(String usuario, Integer ano){
		try{
			TypedQuery<Rebaja> query = this.em.createNamedQuery("Rebaja.findByIdRebaja", Rebaja.class);
			query.setParameter("usuario", usuario);
			query.setParameter("ano", ano);
			return query.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public List<ComunaCumplimiento> getCumplimientoPorAnoComuna(int idComuna, int ano) {
		TypedQuery<ComunaCumplimiento> query = this.em.createNamedQuery("ComunaCumplimiento.findByAnoComuna", ComunaCumplimiento.class);
		query.setParameter("idComuna", idComuna);
		query.setParameter("ano", ano);
		return query.getResultList(); 
	}
	
	public Rebaja getRebajaByRebajaCorteAno(Integer rebajaCorteId, Integer ano){
		TypedQuery<Rebaja> query = this.em.createNamedQuery("Rebaja.findByRebajaCorteAno", Rebaja.class);
		query.setParameter("rebajaCorteId", rebajaCorteId);
		query.setParameter("ano", ano);
		List<Rebaja> rebajas = query.getResultList();
		if(rebajas!=null && rebajas.size() > 0)
			return rebajas.get(0);
		return null;
	}
	
	
	public RebajaCorte getCorteById(Integer rebajaCorteId) {
		try{
			TypedQuery<RebajaCorte> query = this.em.createNamedQuery("RebajaCorte.findByRebajaCorteId", RebajaCorte.class);
			query.setParameter("rebajaCorteId", rebajaCorteId);
			List<RebajaCorte> cortes = query.getResultList();
			if(cortes != null && cortes.size() > 0){
				return cortes.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<DocumentoRebaja> getByIdRebajaTipoNotFinal(Integer idProceso, TipoDocumentosProcesos ... tiposDocumentos) {
		try{
			List<Integer> idDocumentos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumento : tiposDocumentos){
				idDocumentos.add(tipoDocumento.getId());
			}
			TypedQuery<DocumentoRebaja> query = this.em.createNamedQuery("DocumentoRebaja.findByIdRebajaTiposNotFinal", DocumentoRebaja.class);
			query.setParameter("idRebaja", idProceso);
			query.setParameter("idTiposDocumento", idDocumentos);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer deleteDocumentoRebaja(Integer idDocumentoRebaja) {
		List<Integer> idDocumentosRebaja = new ArrayList<Integer>();
		idDocumentosRebaja.add(idDocumentoRebaja);
		return deleteDocumentoRebaja(idDocumentosRebaja);
		
	}
	
	private Integer deleteDocumentoRebaja(List<Integer> idDocumentosRebaja) {
		Query query = this.em.createNamedQuery("DocumentoRebaja.deleteUsingIds");
		query.setParameter("idDocumentosRebaja", idDocumentosRebaja);
		return query.executeUpdate();
	}

	public Integer deleteDocumento(Integer idDocumento) {
		Query query = this.em.createNamedQuery("ReferenciaDocumento.deleteUsingId");
		query.setParameter("idDocumento", idDocumento);
		return query.executeUpdate();
	}

	public List<RebajaCorte> getCortes(Integer mesRebaja) {
		try{
			TypedQuery<RebajaCorte> query = this.em.createNamedQuery("RebajaCorte.findByMesRebaja", RebajaCorte.class);
			query.setParameter("mesCorte", mesRebaja);
			return query.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
}
