package minsal.divap.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.EstimacionFlujoCajaSeguimiento;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.TipoDocumento;

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

}
