package minsal.divap.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.DocumentoEstimacionFlujoCajaConsolidador;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.EstimacionFlujoCajaConsolidadorSeguimiento;
import cl.minsal.divap.model.EstimacionFlujoCajaSeguimiento;
import cl.minsal.divap.model.FlujoCajaConsolidador;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ReporteEmailsFlujoCajaConsolidador;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.TipoDocumento;
import cl.minsal.divap.model.Usuario;

@Singleton
public class EstimacionFlujoCajaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	@EJB
	private ProgramasDAO programaDAO;

	public Integer calcularPropuesta() {
		return 1;
	}

	public Integer save(DocumentoEstimacionflujocaja documentoEstimacionFlujoCaja) {
		em.persist(documentoEstimacionFlujoCaja);
		return documentoEstimacionFlujoCaja.getId();
	}

	public List<DocumentoEstimacionflujocaja> getDocumentByIDProgramaAnoTipoDocumento(ProgramaAno programaAno, TipoDocumento tipoDocumento) {
		try {
			TypedQuery<DocumentoEstimacionflujocaja> query = this.em.createNamedQuery("DocumentoEstimacionflujocaja.findByProgramaAnoTipoDocumento", DocumentoEstimacionflujocaja.class);
			query.setParameter("idProgramaAno", programaAno.getIdProgramaAno());
			query.setParameter("idTipoDocumento", tipoDocumento.getIdTipoDocumento());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DocumentoEstimacionflujocaja> getDocumentByIDProgramaAnoTipoDocumento(Integer idProgramaAno, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<DocumentoEstimacionflujocaja> query = this.em.createNamedQuery("DocumentoEstimacionflujocaja.findByProgramaAnoTipoDocumento", DocumentoEstimacionflujocaja.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer createSeguimiento(Integer idProgramaAno, Seguimiento seguimiento) {
		ProgramaAno programaAno = programaDAO.getProgramaAnoByID(idProgramaAno);
		EstimacionFlujoCajaSeguimiento estimacionFlujoCajaSeguimiento = new EstimacionFlujoCajaSeguimiento();
		estimacionFlujoCajaSeguimiento.setProgramaAno(programaAno);
		estimacionFlujoCajaSeguimiento.setSeguimiento(seguimiento);
		this.em.persist(estimacionFlujoCajaSeguimiento);
		return estimacionFlujoCajaSeguimiento.getId();
	}

	public Integer createSeguimientoFlujoCajaConsolidador(Integer idProceso, Seguimiento seguimiento) {
		FlujoCajaConsolidador flujoCajaConsolidador = findFlujoCajaConsolidadorById(idProceso);
		EstimacionFlujoCajaConsolidadorSeguimiento estimacionFlujoCajaSeguimiento = new EstimacionFlujoCajaConsolidadorSeguimiento();
		estimacionFlujoCajaSeguimiento.setFlujoCajaConsolidador(flujoCajaConsolidador);
		estimacionFlujoCajaSeguimiento.setSeguimiento(seguimiento);
		this.em.persist(estimacionFlujoCajaSeguimiento);
		return estimacionFlujoCajaSeguimiento.getId();
	}

	public FlujoCajaConsolidador findFlujoCajaConsolidadorById(Integer idProceso) {
		return em.find(FlujoCajaConsolidador.class, idProceso);
	}

	public List<Caja> getFlujoCajaServicios(Integer idProgramaAno, Subtitulo subtitulo) {

		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findBySubtituloAno", Caja.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idSubtitulo", subtitulo.getId());

			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer deleteDocumentoEstimacionflujoCaja(List<Integer> idDocumentosEstimacion) {
		Query query = this.em.createNamedQuery("DocumentoEstimacionflujocaja.deleteUsingIds");
		query.setParameter("idDocumentosEstimacion", idDocumentosEstimacion);
		return query.executeUpdate();
	}

	public Integer deleteDocumento(Integer idDocumento) {
		Query query = this.em.createNamedQuery("ReferenciaDocumento.deleteUsingId");
		query.setParameter("idDocumento", idDocumento);
		return query.executeUpdate();
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

	public List<Cuota> getCuotasByProgramaAno(Integer idProgramaAno) {
		try{
			TypedQuery<Cuota> query = this.em.createNamedQuery("Cuota.findByIdProgramaAno", Cuota.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			return query.getResultList();
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}	
	}

	public Integer crearIntanciaConsolidador(Usuario usuario, Mes mes, AnoEnCurso anoEnCurso) {
		try {
			long current = Calendar.getInstance().getTimeInMillis();
			FlujoCajaConsolidador dto = new FlujoCajaConsolidador();
			dto.setUsuario(usuario);
			dto.setAno(anoEnCurso);
			dto.setMes(mes);
			dto.setFechaCreacion(new Date(current));
			this.em.persist(dto);
			return dto.getIdFlujoCajaConsolidador();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public DocumentoEstimacionFlujoCajaConsolidador save(DocumentoEstimacionFlujoCajaConsolidador documentoEstimacionFlujoCajaConsolidador) {
		em.persist(documentoEstimacionFlujoCajaConsolidador);
		return documentoEstimacionFlujoCajaConsolidador;
	}

	public List<ReporteEmailsFlujoCajaConsolidador> getReporteCorreosByFlujoCajaConsolidador(Integer idProceso) {
		try{
			TypedQuery<ReporteEmailsFlujoCajaConsolidador> query = this.em.createNamedQuery("ReporteEmailsFlujoCajaConsolidador.findByIdFlujoCajaConsolidador", ReporteEmailsFlujoCajaConsolidador.class);
			query.setParameter("idFlujoCajaConsolidador", idProceso);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
