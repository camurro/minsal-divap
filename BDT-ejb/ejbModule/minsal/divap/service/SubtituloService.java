package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.vo.DependenciaVO;
import minsal.divap.vo.SubtituloVO;
import cl.minsal.divap.model.TipoSubtitulo;

@Stateless
@LocalBean
public class SubtituloService {


	@EJB
	private TipoSubtituloDAO tipoSubtituloDAO;


	public List<SubtituloVO> getSubtituloByDependenciaPrograma(Integer id) {
		List<TipoSubtitulo> tipoSubtitulos = this.tipoSubtituloDAO.getTipoSubtituloByDependencia(id);
		List<SubtituloVO> result = new ArrayList<SubtituloVO>();
		if(tipoSubtitulos != null && tipoSubtitulos.size() > 0){
			for(TipoSubtitulo tipoSubtitulo : tipoSubtitulos){
				SubtituloVO subtituloVO = new SubtituloVO();
				subtituloVO.setId(tipoSubtitulo.getIdTipoSubtitulo());
				subtituloVO.setNombre(tipoSubtitulo.getNombreSubtitulo());
				if(tipoSubtitulo.getDependencia() != null){
					DependenciaVO dependencia = new DependenciaVO();
					dependencia.setId(tipoSubtitulo.getDependencia().getIdDependenciaPrograma());
					dependencia.setNombre(tipoSubtitulo.getDependencia().getNombre());
					subtituloVO.setDependencia(dependencia);
				}
				result.add(subtituloVO);
			}
		}
		return result;
	}
	
	public Double getInflactor(int idSubtitulo){
		return this.tipoSubtituloDAO.getInfactorById(idSubtitulo);
	}

}
