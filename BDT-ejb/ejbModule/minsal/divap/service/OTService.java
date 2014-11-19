package minsal.divap.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RecursosFinancierosProgramasReforzamientoDAO;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.OTResumenDependienteServicioVO;
import minsal.divap.vo.OTResumenMunicipalVO;
import minsal.divap.vo.ProgramaServicioVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloProgramasVO;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;

@Stateless
@LocalBean
public class OTService {
	
	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private EstablecimientosDAO establecimientosDAO;
	@EJB
	private RecursosFinancierosProgramasReforzamientoDAO recursosFinancierosProgramasReforzamientoDAO;
	
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="tmpDirDoc")
	private String tmpDirDoc;
	@Resource(name="folderTemplateOT")
	private String folderTemplateTo;
	@Resource(name="folderOT")
	private String folderOT;

	
	public List<ProgramaVO> getProgramas(String username) {
		List<ProgramaVO> programasVO = new ArrayList<ProgramaVO>();
		List<ProgramaAno> programas = programasDAO.getProgramasByUserAno(username, getAnoCurso());
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				programasVO.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return programasVO;
	}

	public Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}


	public ProgramaVO getProgramaById(Integer programaSeleccionado) {
		ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(programaSeleccionado);
		return new ProgramaMapper().getBasic(programaAno);
	}

	public List<OTResumenDependienteServicioVO> getDetalleOTServicio(Integer componenteSeleccionado,
			Integer servicioSeleccionado, Integer idTipoSubtitulo, Integer idProgramaAno) {
		List<ProgramaServicioCoreComponente> result = programasDAO.findByServicioComponenteSubtitulo(componenteSeleccionado,
				servicioSeleccionado, idTipoSubtitulo, idProgramaAno);
		List<ProgramaServicioVO> listaProgramaServicioVO = new ArrayList<ProgramaServicioVO>();
		
		/*for (ProgramaServicioCoreComponente programaServicio : result) {
			ProgramaServicioVO prog = new ProgramaServicioVO();
			prog.setIdEstablecimiento(programaServicio.getProgramaServicioCore1().getEstablecimiento().getId());
			prog.setNombreEstablecimiento(programaServicio.getProgramaServicioCore1().getEstablecimiento().getNombre());
			prog.setIdServicioCore(programaServicio.getProgramaServicioCore1().getIdProgramaServicioCore());
			prog.setIdComponente(programaServicio.getServicioCoreComponente().getId());
			
			SubtituloProgramasVO subProg = new SubtituloProgramasVO();
			subProg.setId(programaServicio.getSubtitulo().getIdTipoSubtitulo());
			subProg.setNombre(programaServicio.getSubtitulo().getNombreSubtitulo());
			subProg.setCantidad(programaServicio.getCantidad());
			subProg.setMonto(programaServicio.getMonto());
			subProg.setTotal(programaServicio.getTarifa());
			
			int posicion = listaProgramaServicioVO.indexOf(prog);
			if(posicion != -1){
				prog = listaProgramaServicioVO.get(posicion);
				
				if(subProg.getId()==1){
					prog.setSubtitulo21(subProg);
				}
				if(subProg.getId()==2){	
					prog.setSubtitulo22(subProg);	
							}
				if(subProg.getId()==4){
					prog.setSubtitulo29(subProg);
				}				
			}else{
				if(subProg.getId()==1){
					prog.setSubtitulo21(subProg);
				}
				if(subProg.getId()==2){
					prog.setSubtitulo22(subProg);	
							}
				if(subProg.getId()==4){
					prog.setSubtitulo29(subProg);
				}
				listaProgramaServicioVO.add(prog);
			}
		}
		return listaProgramaServicioVO;*/
		return null;
	}

	public List<OTResumenMunicipalVO> getDetalleOTMunicipal(
			Integer servicioSeleccionado, Integer integer) {
		// TODO Auto-generated method stub
		return null;
	}

}