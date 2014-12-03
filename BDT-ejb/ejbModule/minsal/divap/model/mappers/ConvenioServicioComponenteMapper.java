package minsal.divap.model.mappers;

import minsal.divap.vo.ConvenioServicioComponenteVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.ConvenioServicioComponente;
import cl.minsal.divap.model.DocumentoConvenioServicio;

public class ConvenioServicioComponenteMapper implements Mapper<ConvenioServicioComponente>{

	@Override
	public Object getSummary(ConvenioServicioComponente convenioServicioComponente) {
		throw new NotImplementedException();
	}

	@Override
	public ConvenioServicioComponenteVO getBasic(ConvenioServicioComponente convenioServicioComponente) {
		if (convenioServicioComponente == null)
			return null;
		ConvenioServicioComponenteVO convenioServicioComponenteVO = new ConvenioServicioComponenteVO();
		convenioServicioComponenteVO.setFecha(convenioServicioComponente.getConvenioServicio().getFecha()); 
		convenioServicioComponenteVO.setIdEstablecimiento(convenioServicioComponente.getConvenioServicio().getIdEstablecimiento().getId());
		convenioServicioComponenteVO.setNombreEstablecimiento(convenioServicioComponente.getConvenioServicio().getIdEstablecimiento().getNombre());
		convenioServicioComponenteVO.setIdConvenioServicio(convenioServicioComponente.getConvenioServicio().getIdConvenioServicio());
		convenioServicioComponenteVO.setIdConvenioServicioComponente(convenioServicioComponente.getIdConvenioServicioComponente());
		convenioServicioComponenteVO.setMonto(convenioServicioComponente.getMonto());
		convenioServicioComponenteVO.setResolucion(convenioServicioComponente.getConvenioServicio().getNumeroResolucion());
		if(convenioServicioComponente.getConvenioServicio().getDocumentosConvenio() != null && convenioServicioComponente.getConvenioServicio().getDocumentosConvenio().size() > 0){
			for(DocumentoConvenioServicio documentoConvenioServicio : convenioServicioComponente.getConvenioServicio().getDocumentosConvenio()){
				convenioServicioComponenteVO.setIdDocConvenio(documentoConvenioServicio.getDocumento().getId());
			}
		}
		if(convenioServicioComponente.getDocumentoConvenio() != null){
			convenioServicioComponenteVO.setIdDocConvenio(convenioServicioComponente.getDocumentoConvenio().getId());
		}
		return convenioServicioComponenteVO;
	}

	@Override
	public Object getData(ConvenioServicioComponente convenioServicioComponente) {
		throw new NotImplementedException();
	}

}
