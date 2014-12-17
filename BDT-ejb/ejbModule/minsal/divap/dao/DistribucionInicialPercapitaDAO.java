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

	public List<DocumentoModificacionPercapita> getByIdModificacionPercapitaTipoNotFinal(Integer idModificacionDistribucionInicialPercapita, TipoDocumentosProcesos ... tiposDocumentos) {
		try{
			List<Integer> idDocumentos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumento : tiposDocumentos){
				idDocumentos.add(tipoDocumento.getId());
			}
			TypedQuery<DocumentoModificacionPercapita> query = this.em.createNamedQuery("DocumentoModificacionPercapita.findByIdModificacionPercapitaTiposNotFinal", DocumentoModificacionPercapita.class);
			query.setParameter("idModificacionDistribucionInicialPercapita", idModificacionDistribucionInicialPercapita);
			query.setParameter("idTiposDocumento", idDocumentos);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer deleteDocumentoModificacionPercapita(Integer idDocumentoModificacionPercapita) {
		List<Integer> idDocumentosModificacion = new ArrayList<Integer>();
		idDocumentosModificacion.add(idDocumentoModificacionPercapita);
		return deleteDocumentoModificacionPercapita(idDocumentosModificacion);
	}
	
	private Integer deleteDocumentoModificacionPercapita(List<Integer> idDocumentosModificacionPercapita) {
		Query query = this.em.createNamedQuery("DocumentoModificacionPercapita.deleteUsingIds");
		query.setParameter("idDocumentosModificacionPercapita", idDocumentosModificacionPercapita);
		return query.executeUpdate();
	}
	
	public List<DocumentoDistribucionInicialPercapita> getByIdDistribucionInicialPercapitaTipoNotFinal(Integer idDistribucionInicialPercapita, TipoDocumentosProcesos ... tiposDocumentos) {
		try{
			List<Integer> idDocumentos = new ArrayList<Integer>();
			for(TipoDocumentosProcesos tipoDocumento : tiposDocumentos){
				idDocumentos.add(tipoDocumento.getId());
			}
			TypedQuery<DocumentoDistribucionInicialPercapita> query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.findByIdDistribucionInicialPercapitaTiposNotFinal", DocumentoDistribucionInicialPercapita.class);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTiposDocumento", idDocumentos);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer deleteDocumentoDistribucionInicialPercapita(Integer idDistribucionInicialPercapita) {
		List<Integer> idDocumentosDistribucionInicialPercapita = new ArrayList<Integer>();
		idDocumentosDistribucionInicialPercapita.add(idDistribucionInicialPercapita);
		return deleteDocumentoDistribucionInicialPercapita(idDocumentosDistribucionInicialPercapita);
	}
	
	private Integer deleteDocumentoDistribucionInicialPercapita(List<Integer> idDocumentosDistribucionInicialPercapita) {
		Query query = this.em.createNamedQuery("DocumentoDistribucionInicialPercapita.deleteUsingIds");
		query.setParameter("idDocumentosDistribucionInicialPercapita", idDocumentosDistribucionInicialPercapita);
		return query.executeUpdate();
	}

	public Integer deleteDocumento(Integer idDocumento) {
		Query query = this.em.createNamedQuery("ReferenciaDocumento.deleteUsingId");
		query.setParameter("idDocumento", idDocumento);
		return query.executeUpdate();
	}
	
}
