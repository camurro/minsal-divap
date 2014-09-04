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
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.EstadoPrograma;
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
	
	
	public ProgramaVO getProgramaAno(Integer idProgramaAno) {
		ProgramaVO programa = new ProgramaMapper().getBasic(this.programasDAO.getProgramaAnoByID(idProgramaAno));
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
				comVO.setPeso(componente.getPeso());
				componentesPrograma.add(comVO);
			}
		}
		return componentesPrograma;
	}
	
	

	public ProgramaVO getProgramaAnoPorID(int programaAnoId) {
		ProgramaAno programa = this.programasDAO.getProgramasByIdProgramaAno(programaAnoId);
		return new ProgramaMapper().getBasic(programa);
	}


	public void guardarEstadoFlujoCaja(Integer idEstado, Integer idProgramaAno) {
		// TODO Auto-generated method stub
		this.programasDAO.guardarEstadoFlujoCaja(idEstado,idProgramaAno);
	}


	public void cambiarEstadoProgramaAno(Integer idProgramaAno, EstadosProgramas encurso) {
		// TODO Auto-generated method stub
		
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		EstadoPrograma estadoPrograma = new EstadoPrograma(encurso.getId());
		programaAno.setEstadoFlujoCaja(estadoPrograma);
		
		
		this.programasDAO.saveProgramaAno(programaAno, false);
	}

}
