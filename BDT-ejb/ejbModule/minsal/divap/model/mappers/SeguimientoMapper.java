package minsal.divap.model.mappers;

import java.util.Set;

import minsal.divap.enums.TiposDestinatarios;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.AdjuntosSeguimiento;
import cl.minsal.divap.model.Destinatarios;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.SeguimientoReferenciaDocumento;

public class SeguimientoMapper implements Mapper<Seguimiento>{

	@Override
	public Object getSummary(Seguimiento seguimiento) {
		throw new NotImplementedException();
	}

	@Override
	public SeguimientoVO getBasic(Seguimiento seguimiento) {
		if(seguimiento == null){
			return null;
		}
		String to = "";
		String cc = "";
		String cco = "";
		SeguimientoVO seguimientoVO = new SeguimientoVO();
		Set<Destinatarios>  destinatarios = seguimiento.getDestinatariosCollection();
		if(destinatarios != null && destinatarios.size() > 0){
			for(Destinatarios destinatario : destinatarios){
				TiposDestinatarios tipoDestinatario = TiposDestinatarios.getById(destinatario.getTipoDestinatario().getIdTipoDestinatario());
				switch (tipoDestinatario) {
				case PARA:
					to += (to.equals(""))?destinatario.getDestinatario().getValor():(","+destinatario.getDestinatario().getValor());
					break;
				case CC:
					cc += (cc.equals(""))?destinatario.getDestinatario().getValor():(","+destinatario.getDestinatario().getValor());
					break;
				case CCO:
					cco += (cco.equals(""))?destinatario.getDestinatario().getValor():(","+destinatario.getDestinatario().getValor());
					break;
				default:
					break;
				}

			}
		}
		Set<SeguimientoReferenciaDocumento>  adjuntos = seguimiento.getSeguimientoReferenciaDocumentoCollection();
		for(SeguimientoReferenciaDocumento adjunto : adjuntos){
			if(adjunto.getIdReferenciaDocumento() != null){
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoSummaryVO(adjunto.getIdReferenciaDocumento().getId(), adjunto.getIdReferenciaDocumento().getPath(), adjunto.getIdReferenciaDocumento().getNodeRef());
				seguimientoVO.setAttached(referenciaDocumentoSummaryVO);
			}
		}
		seguimientoVO.setDate(seguimiento.getFechaEnvio());
		seguimientoVO.setTo(to);
		seguimientoVO.setCc(cc);
		seguimientoVO.setCco(cco);
		seguimientoVO.setBody(seguimiento.getBody());
		seguimientoVO.setSubject(seguimiento.getSubject());
		return seguimientoVO;
	}

	@Override
	public Object getData(Seguimiento seguimiento) {
		throw new NotImplementedException();
	}

}
