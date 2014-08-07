package minsal.divap.model.mappers;

import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.ReferenciaDocumento;

public class ReferenciaDocumentoMapper implements Mapper<ReferenciaDocumento>{

	@Override
	public ReferenciaDocumentoSummaryVO getSummary(ReferenciaDocumento referenciaDocumento) {
		if(referenciaDocumento == null){
			return null;
		}
		return new ReferenciaDocumentoSummaryVO(referenciaDocumento.getId(), referenciaDocumento.getPath(), referenciaDocumento.getNodeRef());
	}

	@Override
	public Object getBasic(ReferenciaDocumento referenciaDocumento) {
		throw new NotImplementedException();
	}

	@Override
	public Object getData(ReferenciaDocumento referenciaDocumento) {
		throw new NotImplementedException();
	}

}