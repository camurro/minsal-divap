package minsal.divap.model.mappers;

import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReferenciaDocumentoVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.DocumentoDistribucionInicialPercapita;

public class PercapitaReferenciaDocumentoMapper implements Mapper<DocumentoDistribucionInicialPercapita>{

	@Override
	public ReferenciaDocumentoSummaryVO getSummary(DocumentoDistribucionInicialPercapita documentoDistribucionInicialPercapita) {
		throw new NotImplementedException();
	}

	@Override
	public ReferenciaDocumentoVO getBasic(DocumentoDistribucionInicialPercapita documentoDistribucionInicialPercapita) {
		if(documentoDistribucionInicialPercapita == null){
			return null;
		}
		ReferenciaDocumentoVO referenciaDocumentoVO =  new ReferenciaDocumentoVO(documentoDistribucionInicialPercapita.getIdDocumento().getId(), documentoDistribucionInicialPercapita.getIdDocumento().getPath(), 
				documentoDistribucionInicialPercapita.getIdDocumento().getNodeRef(), documentoDistribucionInicialPercapita.getTipoDocumento().getIdTipoDocumento());
		if(documentoDistribucionInicialPercapita.getComuna() != null){
			referenciaDocumentoVO.setComuna(documentoDistribucionInicialPercapita.getComuna().getNombre());
			if(documentoDistribucionInicialPercapita.getComuna().getServicioSalud() != null){
				referenciaDocumentoVO.setServicio(documentoDistribucionInicialPercapita.getComuna().getServicioSalud().getNombre());
			}
		}
		return referenciaDocumentoVO;
	}

	@Override
	public Object getData(DocumentoDistribucionInicialPercapita documentoDistribucionInicialPercapita) {
		throw new NotImplementedException();
	}

}
