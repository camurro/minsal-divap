package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.vo.ConvenioMontoVO;
import minsal.divap.vo.ResolucionConveniosServicioVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.ConvenioServicioComponente;

public class ConvenioServicioMapper implements Mapper<ConvenioServicio>{

	@Override
	public Object getSummary(ConvenioServicio convenioServicio) {
		throw new NotImplementedException();
	}

	@Override
	public ResolucionConveniosServicioVO getBasic(ConvenioServicio convenioServicio) {
		if(convenioServicio == null){
			return null;
		}
		ResolucionConveniosServicioVO resolucionConveniosVO = new ResolucionConveniosServicioVO();
		resolucionConveniosVO.setIdConvenio(convenioServicio.getIdConvenioServicio());
		if(convenioServicio.getIdEstablecimiento() != null){
			resolucionConveniosVO.setNombreEstablecimiento(convenioServicio.getIdEstablecimiento().getNombre());
		}
		if(convenioServicio.getIdPrograma() != null && convenioServicio.getIdPrograma().getPrograma() != null){
			resolucionConveniosVO.setNombrePrograma(convenioServicio.getIdPrograma().getPrograma().getNombre());
		}
		if(convenioServicio.getConvenioServicioComponentes() != null && convenioServicio.getConvenioServicioComponentes().size() > 0){
			List<ConvenioMontoVO> conveniosServicio = new ArrayList<ConvenioMontoVO>();
			for(ConvenioServicioComponente convenioServicioComponente : convenioServicio.getConvenioServicioComponentes()){
				ConvenioMontoVO convenioMontoVO = new ConvenioMontoVO();
				convenioMontoVO.setIdConvenioMonto(convenioServicioComponente.getIdConvenioServicioComponente());
				if(convenioServicioComponente.getComponente() != null){
					convenioMontoVO.setIdComponente(convenioServicioComponente.getComponente().getId());
					convenioMontoVO.setNombreComponente(convenioServicioComponente.getComponente().getNombre());
				}
				if(convenioServicioComponente.getSubtitulo() != null){
					convenioMontoVO.setIdSubtitulo(convenioServicioComponente.getSubtitulo().getIdTipoSubtitulo());
					convenioMontoVO.setNombreSubtitulo(convenioServicioComponente.getSubtitulo().getNombreSubtitulo());
				}
				convenioMontoVO.setMonto(convenioServicioComponente.getMonto());
				conveniosServicio.add(convenioMontoVO);
			}
			resolucionConveniosVO.setConveniosServicio(conveniosServicio);
		}else{
			resolucionConveniosVO.setConveniosServicio(new ArrayList<ConvenioMontoVO>());
		}
		return resolucionConveniosVO;
	}

	@Override
	public Object getData(ConvenioServicio convenioServicio) {
		throw new NotImplementedException();
	}

}
