package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.ComponenteDAO;
import minsal.divap.model.mappers.ComponenteMapper;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.vo.TipoComponenteVO;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;

@Stateless
@LocalBean
public class ComponenteService {
	@EJB
	private ComponenteDAO  componenteDAO;

	public List<ComponentesVO> getComponentes() {
		List<ComponentesVO> ComponentesVO = new ArrayList<ComponentesVO>();
		List<Componente> componentes = this.componenteDAO.getComponentes();
		if(componentes != null && componentes.size() > 0){
			for(Componente componente : componentes){
				ComponentesVO componenteVO = new ComponentesVO();
				componenteVO.setId(componente.getId());
				componenteVO.setNombre(componente.getNombre());
				ComponentesVO.add(componenteVO);
			}
		}
		return ComponentesVO;
	}

	public List<ComponentesVO> getComponenteByPrograma(Integer programaId) {
		List<Componente> componentes = this.componenteDAO.getComponenteByPrograma(programaId);
		List<ComponentesVO> componentesPrograma = new ArrayList<ComponentesVO>();
		for (Componente componente : componentes){
			componentesPrograma.add(new ComponenteMapper().getBasic(componente));
		}
		return componentesPrograma;
	}

//	public List<ComponentesVO> getComponenteByProgramaSubtitulo(int programaId, int sub) {
//		List<Componente> componentes =	this.componenteDAO.getComponenteByProgramaSubtitulo(programaId, sub);
//		List<ComponentesVO> componentesPrograma = new ArrayList<ComponentesVO>();
//		for (Componente componente : componentes){
//			ComponentesVO comVO = new ComponentesVO();
//			comVO.setId(componente.getId());
//			comVO.setNombre(componente.getNombre());
//			componentesPrograma.add(comVO);
//
//		}
//		return componentesPrograma;
//	}

	public ComponentesVO getComponenteById(Integer idComponente) {
		Componente componente = this.componenteDAO.getComponenteByID(idComponente);
		return new ComponenteMapper().getBasic(componente);
	}
	
	public ComponentesVO getComponenteVOById(Integer idComponente) {
		//	List<ComponenteSubtitulo> componentes = this.componenteDAO.getComponenteByProgramaSubtitulo(programaId, sub);
		Componente componente = this.componenteDAO.getComponenteByID(idComponente);
		ComponentesVO compoVO = new ComponentesVO();
		compoVO.setId(componente.getId());
		compoVO.setNombre(componente.getNombre());
		compoVO.setPeso(componente.getPeso());
		
		List<SubtituloVO> subtitulos = new ArrayList<SubtituloVO>();
		for(ComponenteSubtitulo compoSubs : componente.getComponenteSubtitulosComponente()){
			SubtituloVO subVO = new SubtituloVO();
			subVO.setId(compoSubs.getSubtitulo().getIdTipoSubtitulo());
			subVO.setNombre(compoSubs.getSubtitulo().getNombreSubtitulo());
			subtitulos.add(subVO);
		}
 		
		TipoComponenteVO tipoComponente = new TipoComponenteVO();
		tipoComponente.setId(componente.getTipoComponente().getId());
		tipoComponente.setNombre(componente.getTipoComponente().getNombre());
		
		compoVO.setSubtitulos(subtitulos);
		compoVO.setTipoComponente(tipoComponente);
		return compoVO;
	}
	
	public Componente getComponenteByNombre(String nombre){
		Componente componente = this.componenteDAO.getComponenteByNombre(nombre);		
		return componente;		
	}
	
	public List<ComponentesVO> getComponenteByIdTipoComponente(Integer idTipoComponente) {
		List<Componente> componentes = this.componenteDAO.getComponenteByTipoComponente(idTipoComponente);
		List<ComponentesVO> componentesPrograma = new ArrayList<ComponentesVO>();
		for (Componente componente : componentes){
			componentesPrograma.add(new ComponenteMapper().getBasic(componente));
		}
		return componentesPrograma;
	}
	
	public List<String> getNombreComponenteByIdTipoComponente(Integer idTipoComponente) {
		List<Componente> componentes = this.componenteDAO.getComponenteByTipoComponente(idTipoComponente);
		List<String> componentesPrograma = new ArrayList<String>();
		for (Componente componente : componentes){
			componentesPrograma.add(componente.getNombre());
		}
		return componentesPrograma;
	}
	
	public List<String> getNombreComponenteByNotIdTipoComponente(Integer idTipoComponente) {
		List<Componente> componentes = this.componenteDAO.getComponenteByNotTipoComponente(idTipoComponente);
		List<String> componentesPrograma = new ArrayList<String>();
		for (Componente componente : componentes){
			componentesPrograma.add(componente.getNombre());
		}
		return componentesPrograma;
	}
	
	public List<String> getNombreComponenteAll(){
		List<String> resultado = new ArrayList<String>();
		List<Componente> componentes = this.componenteDAO.getComponentes();
		for(Componente componente : componentes){
			resultado.add(componente.getNombre());
		}
		
		return resultado;
	}
	

}
