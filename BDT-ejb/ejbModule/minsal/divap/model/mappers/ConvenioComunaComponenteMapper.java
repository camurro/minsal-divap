package minsal.divap.model.mappers;

import minsal.divap.vo.ConvenioComunaComponenteVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.ConvenioComunaComponente;
import cl.minsal.divap.model.DocumentoConvenioComuna;

public class ConvenioComunaComponenteMapper implements Mapper<ConvenioComunaComponente>{

	@Override
	public Object getSummary(ConvenioComunaComponente convenioComunaComponente) {
		throw new NotImplementedException();
	}

	@Override
	public ConvenioComunaComponenteVO getBasic(ConvenioComunaComponente convenioComunaComponente) {
		if (convenioComunaComponente == null)
			return null;
		ConvenioComunaComponenteVO convenioComunaComponenteVO = new ConvenioComunaComponenteVO();
		convenioComunaComponenteVO.setFecha(convenioComunaComponente.getConvenioComuna().getFecha()); 
		convenioComunaComponenteVO.setIdComuna(convenioComunaComponente.getConvenioComuna().getIdComuna().getId());
		convenioComunaComponenteVO.setNombreComuna(convenioComunaComponente.getConvenioComuna().getIdComuna().getNombre());
		convenioComunaComponenteVO.setIdConvenioComuna(convenioComunaComponente.getConvenioComuna().getIdConvenioComuna());
		convenioComunaComponenteVO.setIdConvenioComunaComponente(convenioComunaComponente.getIdConvenioComunaComponente());
		convenioComunaComponenteVO.setMonto(convenioComunaComponente.getMonto());
		convenioComunaComponenteVO.setResolucion(convenioComunaComponente.getConvenioComuna().getNumeroResolucion());
		if(convenioComunaComponente.getConvenioComuna().getDocumentosConvenio() != null && convenioComunaComponente.getConvenioComuna().getDocumentosConvenio().size() > 0){
			for(DocumentoConvenioComuna documentoConvenioComuna : convenioComunaComponente.getConvenioComuna().getDocumentosConvenio()){
				convenioComunaComponenteVO.setIdDocConvenio(documentoConvenioComuna.getDocumento().getId());
			}
		}
		return convenioComunaComponenteVO;
	}

	@Override
	public Object getData(ConvenioComunaComponente convenioComunaComponente) {
		throw new NotImplementedException();
	}

}
