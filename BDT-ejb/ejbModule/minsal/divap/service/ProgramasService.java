package minsal.divap.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.ProgramasDAO;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;

@Stateless
@LocalBean
public class ProgramasService {
	@EJB
	private ProgramasDAO programasDAO;

	public Programa getProgramasByID(Integer idPrograma) {
		Programa programa = this.programasDAO.getProgramaByID(idPrograma);
		return programa;
	}
	
	
	public List<ProgramaVO> getProgramasByUser(String username) {
		List<ProgramaAno> programas = this.programasDAO.getProgramasByUserAno(username, getAnoCurso());
		List<ProgramaVO> result = new ArrayList<ProgramaVO>();
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				result.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return result;
	}
	
	public List<ProgramaVO> getProgramasByUserAno(String username, Integer ano) {
		List<ProgramaAno> programas = this.programasDAO.getProgramasByUserAno(username, ano);
		List<ProgramaVO> result = new ArrayList<ProgramaVO>();
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				result.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return result;
	}
	
	private Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
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

}
