package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RolDAO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.Rol;

@Stateless
@LocalBean
public class ProgramasService {
	@EJB
	private ProgramasDAO programasDAO;

	public List<ProgramaVO> getProgramasByUser(String username) {
		List<Programa> programa = this.programasDAO.getProgramasByUser(username);
		ArrayList<ProgramaVO> result = new ArrayList<ProgramaVO>(programa.size());
		for (Programa prog : programa){
			ProgramaVO progVO = new ProgramaVO();
			progVO.setNombre(prog.getNombre());
			progVO.setTipo_programa(prog.getTipoPrograma().getNombre());
			progVO.setCantidad_cuotas(prog.getCantidadCuotas());
			progVO.setDescripcion(prog.getDescripcion());
			progVO.setUsername(prog.getUsuario().getUsername());
			progVO.setId(prog.getId());
			result.add(progVO);
		}
		return result;
	}
	
	public List<ComponentesVO> getComponenteByPrograma(int programaId) {
		List<Programa> programa = this.programasDAO.getComponenteByPrograma(programaId);
		List<ComponentesVO> componentesPrograma = new ArrayList<ComponentesVO>();
		for (Programa prog : programa){
			for (Componente componente : prog.getComponentes()) {
				ComponentesVO comVO = new ComponentesVO();
				comVO.setComponente_id(componente.getId());
				comVO.setNombre(componente.getNombre());
				componentesPrograma.add(comVO);
			}
		}
		return componentesPrograma;
	}
	
	

	public Programa getProgramaPorID(int programaId) {
		Programa programa = this.programasDAO.getProgramaPorID(programaId);
		
		return programa;
	}

}
