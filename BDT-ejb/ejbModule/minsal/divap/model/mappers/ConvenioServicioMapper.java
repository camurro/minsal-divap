package minsal.divap.model.mappers;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import minsal.divap.vo.ResolucionConveniosVO;
import cl.minsal.divap.model.ConvenioServicio;

public class ConvenioServicioMapper implements Mapper<ConvenioServicio>{

	@Override
	public Object getSummary(ConvenioServicio convenioServicio) {
		throw new NotImplementedException();
	}

	@Override
	public ResolucionConveniosVO getBasic(ConvenioServicio convenioServicio) {
		if(convenioServicio == null){
			return null;
		}
		ResolucionConveniosVO resolucionConveniosVO = new ResolucionConveniosVO();
		resolucionConveniosVO.setIdConvenio(convenioServicio.getIdConvenioServicio());
		Long monto = ((convenioServicio.getMonto() == null)?0L:new Long(convenioServicio.getMonto()));
		resolucionConveniosVO.setMonto(monto);
		if(convenioServicio.getComponente() != null){
			resolucionConveniosVO.setNombreComponente(convenioServicio.getComponente().getNombre());
		}
		if(convenioServicio.getIdEstablecimiento() != null){
			resolucionConveniosVO.setNombreEstablecimiento(convenioServicio.getIdEstablecimiento().getNombre());
		}
		if(convenioServicio.getIdPrograma() != null && convenioServicio.getIdPrograma().getPrograma() != null){
			resolucionConveniosVO.setNombrePrograma(convenioServicio.getIdPrograma().getPrograma().getNombre());
		}
		if(convenioServicio.getIdTipoSubtitulo() != null){
			resolucionConveniosVO.setNombreSubtitulo(convenioServicio.getIdTipoSubtitulo().getNombreSubtitulo());
		}
		return resolucionConveniosVO;
	}

	@Override
	public Object getData(ConvenioServicio convenioServicio) {
		throw new NotImplementedException();
	}

}
