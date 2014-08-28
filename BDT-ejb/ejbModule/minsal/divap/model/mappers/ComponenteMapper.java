package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.vo.TipoComponenteVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;

public class ComponenteMapper implements Mapper<Componente>{

	@Override
	public Object getSummary(Componente componente) {
		throw new NotImplementedException();
	}

	@Override
	public ComponentesVO getBasic(Componente componente) {
		if (componente == null)
			return null;
		ComponentesVO componentesVO = new ComponentesVO();
		componentesVO.setComponente_id(componente.getId());
		componentesVO.setNombre(componente.getNombre());
		if(componente.getTipoComponente() != null){
			TipoComponenteVO tipoComponenteVO = new TipoComponenteVO(componente.getTipoComponente().getId(), componente.getTipoComponente().getNombre());
			componentesVO.setTipoComponente(tipoComponenteVO);
		}
		if(componente.getComponenteSubtitulos() != null && componente.getComponenteSubtitulos().size() > 0){
			List<SubtituloVO> subtitulos = new ArrayList<SubtituloVO>();
			for(ComponenteSubtitulo componenteSubtitulo : componente.getComponenteSubtitulos()){
				SubtituloVO subtituloVO = new SubtituloVO(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), componenteSubtitulo.getSubtitulo().getNombreSubtitulo());
				subtitulos.add(subtituloVO);
			}
			componentesVO.setSubtitulos(subtitulos);
		}
		return componentesVO;

	}

	@Override
	public Object getData(Componente componente) {
		throw new NotImplementedException();
	}

}
