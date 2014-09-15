package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.vo.ComponentesVO;




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

}
