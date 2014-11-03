package minsal.divap.model.mappers;

import minsal.divap.vo.ResolucionConveniosVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.ConvenioComuna;

public class ConvenioComunaMapper implements Mapper<ConvenioComuna> {

	@Override
	public Object getSummary(ConvenioComuna convenioComuna) {
		throw new NotImplementedException();
	}

	@Override
	public ResolucionConveniosVO getBasic(ConvenioComuna convenioComuna) {
		if(convenioComuna == null){
			return null;
		}
		ResolucionConveniosVO resolucionConveniosVO = new ResolucionConveniosVO();
		resolucionConveniosVO.setIdConvenio(convenioComuna.getIdConvenioComuna());
		Long monto = ((convenioComuna.getMonto() == null)?0L:new Long(convenioComuna.getMonto()));
		resolucionConveniosVO.setMonto(monto);
		if(convenioComuna.getComponente() != null){
			resolucionConveniosVO.setNombreComponente(convenioComuna.getComponente().getNombre());
		}
		if(convenioComuna.getIdComuna() != null){
			resolucionConveniosVO.setNombreComuna(convenioComuna.getIdComuna().getNombre());
		}
		if(convenioComuna.getIdPrograma() != null && convenioComuna.getIdPrograma().getPrograma() != null){
			resolucionConveniosVO.setNombrePrograma(convenioComuna.getIdPrograma().getPrograma().getNombre());
		}
		if(convenioComuna.getIdTipoSubtitulo() != null){
			resolucionConveniosVO.setNombreSubtitulo(convenioComuna.getIdTipoSubtitulo().getNombreSubtitulo());
		}
		return resolucionConveniosVO;
	}

	@Override
	public Object getData(ConvenioComuna convenioComuna) {
		throw new NotImplementedException();
	}

}
