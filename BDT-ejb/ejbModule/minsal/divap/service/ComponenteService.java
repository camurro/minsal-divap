package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.Dependencia;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.DependenciaVO;
import minsal.divap.vo.SubtituloVO;




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
	
	
	
	public List<ComponentesVO> getComponenteByPrograma(int programaId) {
		List<Componente> componentes = this.componenteDAO.getComponenteByPrograma(programaId);
		List<ComponentesVO> componentesPrograma = new ArrayList<ComponentesVO>();
		for (Componente componente : componentes){
				ComponentesVO comVO = new ComponentesVO();
				comVO.setId(componente.getId());
				comVO.setNombre(componente.getNombre());
				comVO.setPeso(componente.getPeso());
				List<SubtituloVO> listaSubVO = new ArrayList<SubtituloVO>(); 
				for(ComponenteSubtitulo comSub : componente.getComponenteSubtitulos()){
					SubtituloVO subVO = new SubtituloVO();
					subVO.setId(comSub.getSubtitulo().getIdTipoSubtitulo());
					subVO.setNombre(comSub.getSubtitulo().getNombreSubtitulo());
					
					DependenciaVO dependencia = new DependenciaVO();
					dependencia.setId(comSub.getSubtitulo().getDependencia().getIdDependenciaPrograma());
					dependencia.setNombre(comSub.getSubtitulo().getDependencia().getNombre());
					subVO.setDependencia(dependencia);
					listaSubVO.add(subVO);
					
				}
				comVO.setSubtitulos(listaSubVO);
				
				
				componentesPrograma.add(comVO);
			
		}
		return componentesPrograma;
	}
	
	public List<ComponentesVO> getComponenteByProgramaSubtitulo(int programaId,int sub) {
	//	List<ComponenteSubtitulo> componentes = this.componenteDAO.getComponenteByProgramaSubtitulo(programaId, sub);
		List<Componente> componentes =	this.componenteDAO.getComponenteByProgramaSubtitulo(programaId, sub);
		List<ComponentesVO> componentesPrograma = new ArrayList<ComponentesVO>();
		for (Componente componente : componentes){
				ComponentesVO comVO = new ComponentesVO();
				comVO.setId(componente.getId());
				comVO.setNombre(componente.getNombre());
				componentesPrograma.add(comVO);
			
		}
		return componentesPrograma;
		
		
		
	}
	
	public Componente getComponenteById(Integer idComponente) {
		//	List<ComponenteSubtitulo> componentes = this.componenteDAO.getComponenteByProgramaSubtitulo(programaId, sub);
			Componente componente = this.componenteDAO.getComponenteByID(idComponente);
			
			return componente;
			
			
			
		}

}
