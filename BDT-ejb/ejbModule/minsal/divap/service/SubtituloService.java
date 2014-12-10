package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.vo.DependenciaVO;
import minsal.divap.vo.SubtituloVO;
import cl.minsal.divap.model.ComponenteSubtitulo;
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
	
	public List<SubtituloVO> getAllSubtitulosVO(){
		List<TipoSubtitulo> tipoSubtitulos = this.tipoSubtituloDAO.getTipoSubtituloAll();
		List<SubtituloVO> result = new ArrayList<SubtituloVO>();
		if(tipoSubtitulos != null && tipoSubtitulos.size() > 0){
			for(TipoSubtitulo tipoSubtitulo : tipoSubtitulos){
				SubtituloVO subtituloVO = new SubtituloVO();
				subtituloVO.setId(tipoSubtitulo.getIdTipoSubtitulo());
				subtituloVO.setNombre(tipoSubtitulo.getNombreSubtitulo());
				DependenciaVO dependencia = new DependenciaVO();
				subtituloVO.setDependencia(dependencia);
				subtituloVO.setCantidad(0);
				subtituloVO.setTarifa(0);
				subtituloVO.setTotal(0);
				result.add(subtituloVO);
			}
		}
		return result;
	}
	
	public SubtituloVO getSubtituloVOById(Integer idSubtitulo){
		TipoSubtitulo tipoSubtitulo = this.tipoSubtituloDAO.getTipoSubtituloById(idSubtitulo);
		SubtituloVO subtituloVO = new SubtituloVO();
		subtituloVO.setId(tipoSubtitulo.getIdTipoSubtitulo());
		subtituloVO.setNombre(tipoSubtitulo.getNombreSubtitulo());
		
		return subtituloVO;
		
	}

	public List<SubtituloVO> getSubtitulosVOByIdComponente(Integer idComponente) {
		List<TipoSubtitulo> tipoSubtitulos = this.tipoSubtituloDAO.getTipoSubtituloAll();
		List<ComponenteSubtitulo> componenteSubtitulos = this.tipoSubtituloDAO.getByIdComponente(idComponente);
		for(ComponenteSubtitulo componenteSubtitulo : componenteSubtitulos){
			System.out.println("componenteSubtitulo.toString() ----> "+componenteSubtitulo.toString());
		}
		List<SubtituloVO> resultado = new ArrayList<SubtituloVO>();
		
		for(TipoSubtitulo tipoSubtitulo : tipoSubtitulos){
			
			for(ComponenteSubtitulo componenteSubtitulo : componenteSubtitulos){
				SubtituloVO subtituloVO = new SubtituloVO();
				if(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo() == tipoSubtitulo.getIdTipoSubtitulo()){
					subtituloVO.setId(tipoSubtitulo.getIdTipoSubtitulo());
					subtituloVO.setNombre(tipoSubtitulo.getNombreSubtitulo());
					System.out.println("subtituloVO --> "+subtituloVO.toString());
					resultado.add(subtituloVO);
				}
			}
		}
		return resultado;
	}
	
	public List<SubtituloVO> getSubtitulosVOFaltantesComponente(Integer idComponente){
		List<TipoSubtitulo> tipoSubtitulos = this.tipoSubtituloDAO.getTipoSubtituloAll();
		List<ComponenteSubtitulo> componenteSubtitulos = this.tipoSubtituloDAO.getByIdComponente(idComponente);
		List<SubtituloVO> resultado = new ArrayList<SubtituloVO>();
		
		for(TipoSubtitulo tipoSubtitulo : tipoSubtitulos){
			
			for(ComponenteSubtitulo componenteSubtitulo : componenteSubtitulos){
				SubtituloVO subtituloVO = new SubtituloVO();
				if(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo() != tipoSubtitulo.getIdTipoSubtitulo()){
					subtituloVO.setId(tipoSubtitulo.getIdTipoSubtitulo());
					subtituloVO.setNombre(tipoSubtitulo.getNombreSubtitulo());
					System.out.println("subtituloVO --> "+subtituloVO.toString());
					resultado.add(subtituloVO);
				}
			}
		}
		return resultado;
	}
	
	

}
