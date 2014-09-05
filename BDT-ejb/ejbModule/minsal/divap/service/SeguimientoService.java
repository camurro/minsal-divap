package minsal.divap.service;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import minsal.divap.dao.EmailDAO;
import minsal.divap.dao.ReferenciaDocumentoDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.model.mappers.SeguimientoMapper;
import minsal.divap.service.EmailService.Adjunto;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;
import cl.minsal.divap.model.Email;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.TareaSeguimiento;


@Stateless
public class SeguimientoService {

	@EJB
	EmailDAO emailDAO ;
	@EJB
	SeguimientoDAO seguimientoDAO;
	@EJB
	ReferenciaDocumentoDAO referenciaDocumentoDao;
	@EJB
	EmailService emailService;

	public Integer createSeguimiento(TareasSeguimiento tareaSeguimiento, String subject, String body, String from, List<String> para, List<String> conCopia, List<String> conCopiaOculta, List<ReferenciaDocumentoSummaryVO> documentos){
		List<Email> destinoConCopia = null;
		List<Email> destinoConCopiaOculta = null;
		List<Email> destinoPara = getCorreos(para);
		Email correoOrigen = getCorreo(from);
		if(conCopia != null){
			destinoConCopia = getCorreos(conCopia);
		}
		if(conCopiaOculta != null){
			destinoConCopiaOculta = getCorreos(conCopiaOculta);
		}
		List<Adjunto> adjuntos = new ArrayList<Adjunto>();
		try {
			if(documentos != null && documentos.size() > 0){
				for(ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO : documentos){
					Adjunto adjunto = new Adjunto();
					adjunto.setName(referenciaDocumentoSummaryVO.getPath().substring((referenciaDocumentoSummaryVO.getPath().lastIndexOf(File.separator) + 1)));
					adjunto.setDescripcion("Archivo adjunto");
					adjunto.setUrl(  (new File(referenciaDocumentoSummaryVO.getPath()).toURI().toURL()) );
					adjuntos.add(adjunto);
				}
			}
		} catch (MalformedURLException e) {
			System.out.println("!!Error al adjuntar archivos al correo");
			e.printStackTrace();
		}
		emailService.sendMail(para, conCopia, conCopiaOculta, subject, body, adjuntos); 
		Integer seguimiento = seguimientoDAO.createSeguimiento(subject, body, correoOrigen, new TareaSeguimiento(tareaSeguimiento.getId()));
		seguimientoDAO.createSeguimientoDestinatarios(seguimiento, destinoPara, destinoConCopia, destinoConCopiaOculta);
		seguimientoDAO.createSeguimientoDocumentos(seguimiento, referenciaDocumentoDao.getReferenciaDocumentosBySummaryId(documentos));
		return seguimiento; 
	}

	private List<Email> getCorreos(List<String> correos) {
		List<Email> emails = new ArrayList<Email>();
		for(String correo : correos){
			emails.add(getCorreo(correo));
		}
		return emails;
	}

	private Email getCorreo(String correo){
		Email email = emailDAO.getEmailIdByValor(correo);
		if(email == null){
			Email newMail = new Email();
			newMail.setValor(correo);
			email = emailDAO.save(newMail);
		}
		return email;
	}

	public List<SeguimientoVO> getBitacora(
			Integer idDistribucionInicialPercapita,
			TareasSeguimiento tareaSeguimiento) {
		List<SeguimientoVO> bitacora = new ArrayList<SeguimientoVO>();
		List<Seguimiento> bitacoraSeguimiento = seguimientoDAO.getBitacora(idDistribucionInicialPercapita, tareaSeguimiento);
		if(bitacoraSeguimiento != null && bitacoraSeguimiento.size() > 0){
			for(Seguimiento seguimiento : bitacoraSeguimiento){
				bitacora.add(new SeguimientoMapper().getBasic(seguimiento));
			}
		}
		return bitacora;
	}
	

	public List<SeguimientoVO> getBitacoraEstimacionFlujoCaja(
			Integer idProgramaAno,
			TareasSeguimiento tareaSeguimiento) {
		List<SeguimientoVO> bitacora = new ArrayList<SeguimientoVO>();
		List<Seguimiento> bitacoraSeguimiento = seguimientoDAO.getBitacoraEstimacionFlujoCaja(idProgramaAno, tareaSeguimiento);

		if(bitacoraSeguimiento != null && bitacoraSeguimiento.size() > 0){
			for(Seguimiento seguimiento : bitacoraSeguimiento){
				bitacora.add(new SeguimientoMapper().getBasic(seguimiento));
			}
		}
		return bitacora; 
	}
	
	public List<SeguimientoVO> getBitacoraProcesoOT(
			Integer idOrdenTransferencia,
			TareasSeguimiento tareaSeguimiento) {
		List<SeguimientoVO> bitacora = new ArrayList<SeguimientoVO>();
		List<Seguimiento> bitacoraSeguimiento = seguimientoDAO.getBitacoraProcesoOT(idOrdenTransferencia, tareaSeguimiento);

		if(bitacoraSeguimiento != null && bitacoraSeguimiento.size() > 0){
			for(Seguimiento seguimiento : bitacoraSeguimiento){
				bitacora.add(new SeguimientoMapper().getBasic(seguimiento));
			}
		}
		return bitacora;
	}

}
