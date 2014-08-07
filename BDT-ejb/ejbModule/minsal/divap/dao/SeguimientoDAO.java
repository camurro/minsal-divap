package minsal.divap.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TiposDestinatarios;
import cl.minsal.divap.model.Destinatarios;
import cl.minsal.divap.model.Email;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.SeguimientoReferenciaDocumento;
import cl.minsal.divap.model.TareaSeguimiento;
import cl.minsal.divap.model.TipoDestinatario;



@Singleton
public class SeguimientoDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public Seguimiento getSeguimientoById(Integer idSeguimiento){
		return this.em.find(Seguimiento.class, idSeguimiento);
	}

	public Seguimiento save(Seguimiento seguimiento) {
		this.em.persist(seguimiento);
		return seguimiento;
	}

	public Integer createSeguimiento(String subject, String body, Email emailFrom, TareaSeguimiento tareaSeguimiento) {
		Seguimiento seguimiento = new Seguimiento();
		long current = Calendar.getInstance().getTimeInMillis();
		seguimiento.setBody(body);
		seguimiento.setSubject(subject);
		seguimiento.setMailFrom(emailFrom);
		seguimiento.setFechaEnvio(new Date(current));
		seguimiento.setTareaSeguimiento(tareaSeguimiento);
		this.em.persist(seguimiento);
		return seguimiento.getId();
	}

	public void createSeguimientoDestinatarios(Integer idSeguimiento,
			List<Email> destinoPara, List<Email> destinoConCopia,
			List<Email> destinoConCopiaOcualta) {
		Seguimiento seguimiento = getSeguimientoById(idSeguimiento);
		if(destinoPara != null && destinoPara.size() > 0){
			for(Email emailDestinatario : destinoPara){
				Destinatarios  destinatario = new Destinatarios();
				destinatario.setDestinatario(emailDestinatario);
				destinatario.setSeguimiento(seguimiento);
				destinatario.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.PARA.getId()));
				this.em.persist(destinatario);
			}
		}
		if(destinoConCopia != null && destinoConCopia.size() > 0){
			for(Email emailDestinatario : destinoConCopia){
				Destinatarios  destinatario = new Destinatarios();
				destinatario.setDestinatario(emailDestinatario);
				destinatario.setSeguimiento(seguimiento);
				destinatario.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.CC.getId()));
				this.em.persist(destinatario);
			}
		}
		if(destinoConCopiaOcualta != null && destinoConCopiaOcualta.size() > 0){
			for(Email emailDestinatario : destinoConCopiaOcualta){
				Destinatarios  destinatario = new Destinatarios();
				destinatario.setDestinatario(emailDestinatario);
				destinatario.setSeguimiento(seguimiento);
				destinatario.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.CCO.getId()));
				this.em.persist(destinatario);
			}
		}

	}

	public void createSeguimientoDocumentos(Integer idSeguimiento,
			List<ReferenciaDocumento> documentosSeguimiento) {
		Seguimiento seguimiento = getSeguimientoById(idSeguimiento);
		if(documentosSeguimiento != null){
			for(ReferenciaDocumento referenciaDocumento : documentosSeguimiento){
				SeguimientoReferenciaDocumento seguimientoReferenciaDocumento = new SeguimientoReferenciaDocumento();
				seguimientoReferenciaDocumento.setIdReferenciaDocumento(referenciaDocumento);
				seguimientoReferenciaDocumento.setIdSeguimiento(seguimiento);
				this.em.persist(seguimientoReferenciaDocumento);
			}
		}
	}

	public List<Seguimiento> getBitacora(Integer idDistribucionInicialPercapita, TareasSeguimiento tareaSeguimiento) {
		try {
			TypedQuery<Seguimiento> query = this.em.createNamedQuery("Seguimiento.findByIdDistribucionInicialTarea", Seguimiento.class);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idTareaSeguimiento", tareaSeguimiento.getId());
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
