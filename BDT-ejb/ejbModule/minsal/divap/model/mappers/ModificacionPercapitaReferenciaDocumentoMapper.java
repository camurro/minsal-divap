package minsal.divap.model.mappers;

import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReferenciaDocumentoVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.DocumentoModificacionPercapita;

public class ModificacionPercapitaReferenciaDocumentoMapper implements Mapper<DocumentoModificacionPercapita>{

	@Override
	public ReferenciaDocumentoSummaryVO getSummary(DocumentoModificacionPercapita documentoModificacionPercapita) {
		throw new NotImplementedException();
	}

	@Override
	public ReferenciaDocumentoVO getBasic(DocumentoModificacionPercapita documentoModificacionPercapita) {
		if(documentoModificacionPercapita == null){
			return null;
		}
		ReferenciaDocumentoVO referenciaDocumentoVO =  new ReferenciaDocumentoVO(documentoModificacionPercapita.getDocumento().getId(), documentoModificacionPercapita.getDocumento().getPath(), 
				documentoModificacionPercapita.getDocumento().getNodeRef(), documentoModificacionPercapita.getTipoDocumento().getIdTipoDocumento());
		if(documentoModificacionPercapita.getComuna() != null){
			referenciaDocumentoVO.setComuna(documentoModificacionPercapita.getComuna().getNombre());
			if(documentoModificacionPercapita.getComuna().getServicioSalud() != null){
				referenciaDocumentoVO.setServicio(documentoModificacionPercapita.getComuna().getServicioSalud().getNombre());
			}
		}
		return referenciaDocumentoVO;
	}

	@Override
	public Object getData(DocumentoModificacionPercapita documentoModificacionPercapita) {
		throw new NotImplementedException();
	}

}
