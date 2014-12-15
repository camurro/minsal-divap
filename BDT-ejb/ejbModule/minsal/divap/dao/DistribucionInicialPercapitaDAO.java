package minsal.divap.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.TipoDocumentosProcesos;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DistribucionInicialPercapitaSeguimiento;
import cl.minsal.divap.model.DocumentoDistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoModificacionPercapita;
import cl.minsal.divap.model.ModificacionDistribucionInicialPercapita;
import cl.minsal.divap.model.ModificacionDistribucionInicialPercapitaSeguimiento;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;
import cl.minsal.divap.model.ReporteEmailsEnviados;
import cl.minsal.divap.model.ReporteEmailsModificacionPercapita;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.Usuario;



@Singleton
public class DistribucionInicialPercapitaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public DistribucionInicialPercapita findById(Integer idDistribucionInicialPercapita) {
		return this.em.find(DistribucionInicialPercapita.class, idDistribucionInicialPercapita);
	}
	
	public DistribucionInicialPercapita update(DistribucionInicialPercapita distribucionInicialPercapita) {
		return em.merge(distribucionInicialPercapita);
	}
	
	public Integer save(DocumentoDistribucionInicialPercapita documentoDistribucionInicialPercapita) {
		em.persist(documentoDistribucionInicialPercapita);
		return documentoDistribucionInicialPercapita.getIdDocumentoDistribucionInicialPercapita();
	}

	public Integer crearIntanciaDistribucionInicialPercapita(Usuario usuario, AnoEnCurso anoEnCurso){
		try {
			long current = Calendar.getInstance().getTimeInMillis();
			DistribucionInicialPercapita dto = new DistribucionInicialPercapita();
			dto.setUsuario(usuario);
			dto.setAno(anoEnCurso);
			dto.setFechaCreacion(new Date(current));
			this.em.persist(dto);
			return dto.getIdDistribucionInicialPercapita();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer createSeguimiento(Integer idDistribucionInicialPercapita,
			Seguimiento seguimiento) {
		DistribucionInicialPercapita distribucionInicialPercapita = findById(idDistribucionInicialPercapita);
		DistribucionInicialPercapitaSeguimiento distribucionInicialPercapitaSeguimiento = new DistribucionInicialPercapitaSeguimiento();
		distribucionInicialPercapitaSeguimiento.setDistribucionInicialPercapita(distribucionInicialPercapita);
		distribucionInicialPercapitaSeguimiento.setSeguimiento(seguimiento);
		this.em.persist(distribucionInicialPercapitaSeguimiento);
		return distribucionInicialPercapitaSeguimiento.getIdDistribucionInicialPercapitaSeguimiento();
	}

	public DistribucionInicialPercapita findLast(Integer idAno) {
		try {
			TypedQuery<DistribucionInicialPercapita> query = this.em.createNamedQuery("DistribucionInicialPercapita.findLast", DistribucionInicialPercapita.class);
			query.setParameter("idAno", idAno);
			List<DistribucionInicialPercapita> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public List<DocumentoDistribucionInicialPercapita> getByIdDistribucionInicialPercapitaTipo(
			Integer idDistribucionInicialPercapita,
			TipoDocumentosProcesos tipoDocumentoProceso) {
		try{
			TypedQuery<DocumentoDistribucionInicialPercapita> query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.findByTypeIdDistribucionInicialPercapita", DocumentoDistribucionInicialPercapita.class);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTipoDocumento", tipoDocumentoProceso.getId());
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer crearIntanciaModificacionDistribucionInicialPercapita(Usuario usuario, AnoEnCurso anoEnCurso) {
		try {
			long current = Calendar.getInstance().getTimeInMillis();
			ModificacionDistribucionInicialPercapita dto = new ModificacionDistribucionInicialPercapita();
			dto.setUsuario(usuario);
			dto.setAno(anoEnCurso);
			dto.setFechaCreacion(new Date(current));
			this.em.persist(dto);
			return dto.getIdModificacionDistribucionInicialPercapita();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ModificacionDistribucionInicialPercapita findModificacionDistribucionInicialById(Integer idDistribucionInicialPercapita) {
		try{
			TypedQuery<ModificacionDistribucionInicialPercapita> query = this.em.createNamedQuery("ModificacionDistribucionInicialPercapita.findByIdModificacionDistribucionInicialPercapita", ModificacionDistribucionInicialPercapita.class);
			query.setParameter("idModificacionDistribucionInicialPercapita", idDistribucionInicialPercapita);
			List<ModificacionDistribucionInicialPercapita> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public DocumentoModificacionPercapita save(DocumentoModificacionPercapita documentoModificacionPercapita) {
		em.merge(documentoModificacionPercapita);
		return documentoModificacionPercapita;
	}

	public ReporteEmailsEnviados save(ReporteEmailsEnviados reporteEmailsEnviados) {
		em.persist(reporteEmailsEnviados);
		return reporteEmailsEnviados;
	}

	public ReporteEmailsDestinatarios save(ReporteEmailsDestinatarios destinatarioPara) {
		em.persist(destinatarioPara);
		return destinatarioPara;
	}

	public ReporteEmailsAdjuntos save(ReporteEmailsAdjuntos reporteEmailsAdjuntos) {
		em.persist(reporteEmailsAdjuntos);
		return reporteEmailsAdjuntos;
	}

	public ReporteEmailsModificacionPercapita save(ReporteEmailsModificacionPercapita reporteEmailsModificacionPercapita) {
		em.persist(reporteEmailsModificacionPercapita);
		return reporteEmailsModificacionPercapita;
	}

	public Integer createSeguimientoModificacion(Integer idDistribucionInicialPercapita, Seguimiento seguimiento) {
		ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita = findModificacionDistribucionInicialById(idDistribucionInicialPercapita);
		ModificacionDistribucionInicialPercapitaSeguimiento modificacionDistribucionInicialPercapitaSeguimiento = new ModificacionDistribucionInicialPercapitaSeguimiento();
		modificacionDistribucionInicialPercapitaSeguimiento.setModificacionDistribucionInicialPercapita(modificacionDistribucionInicialPercapita);
		modificacionDistribucionInicialPercapitaSeguimiento.setSeguimiento(seguimiento);
		this.em.persist(modificacionDistribucionInicialPercapitaSeguimiento);
		return modificacionDistribucionInicialPercapitaSeguimiento.getIdModificacionDistribucionInicialPercapitaSeguimiento();
	}
	
}
