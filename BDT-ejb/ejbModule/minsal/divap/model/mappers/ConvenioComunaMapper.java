package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.vo.ConvenioMontoVO;
import minsal.divap.vo.ResolucionConveniosComunaVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;

public class ConvenioComunaMapper implements Mapper<ConvenioComuna> {

	@Override
	public Object getSummary(ConvenioComuna convenioComuna) {
		throw new NotImplementedException();
	}

	@Override
	public ResolucionConveniosComunaVO getBasic(ConvenioComuna convenioComuna) {
		if(convenioComuna == null){
			return null;
		}
		ResolucionConveniosComunaVO resolucionConveniosVO = new ResolucionConveniosComunaVO();
		resolucionConveniosVO.setIdConvenio(convenioComuna.getIdConvenioComuna());
		if(convenioComuna.getIdComuna() != null){
			resolucionConveniosVO.setIdComuna(convenioComuna.getIdComuna().getId());
			resolucionConveniosVO.setNombreComuna(convenioComuna.getIdComuna().getNombre());
		}
		if(convenioComuna.getIdPrograma() != null && convenioComuna.getIdPrograma().getPrograma() != null){
			resolucionConveniosVO.setIdPrograma(convenioComuna.getIdPrograma().getPrograma().getId());
			resolucionConveniosVO.setNombrePrograma(convenioComuna.getIdPrograma().getPrograma().getNombre());
		}
		if(convenioComuna.getConvenioComunaComponentes() != null && convenioComuna.getConvenioComunaComponentes().size() > 0){
			List<ConvenioMontoVO> conveniosComuna = new ArrayList<ConvenioMontoVO>();
			for(ConvenioComunaComponente convenioComunaComponente : convenioComuna.getConvenioComunaComponentes()){
				ConvenioMontoVO convenioMontoVO = new ConvenioMontoVO();
				convenioMontoVO.setIdConvenioMonto(convenioComunaComponente.getIdConvenioComunaComponente());
				if(convenioComunaComponente.getComponente() != null){
					convenioMontoVO.setIdComponente(convenioComunaComponente.getComponente().getId());
					convenioMontoVO.setNombreComponente(convenioComunaComponente.getComponente().getNombre());
				}
				if(convenioComunaComponente.getSubtitulo() != null){
					convenioMontoVO.setIdSubtitulo(convenioComunaComponente.getSubtitulo().getIdTipoSubtitulo());
					convenioMontoVO.setNombreSubtitulo(convenioComunaComponente.getSubtitulo().getNombreSubtitulo());
				}
				convenioMontoVO.setMonto(convenioComunaComponente.getMonto());
				conveniosComuna.add(convenioMontoVO);
			}
			resolucionConveniosVO.setConveniosComuna(conveniosComuna);
		}else{
			resolucionConveniosVO.setConveniosComuna(new ArrayList<ConvenioMontoVO>());
		}
		return resolucionConveniosVO;
	}

	@Override
	public Object getData(ConvenioComuna convenioComuna) {
		throw new NotImplementedException();
	}

}
