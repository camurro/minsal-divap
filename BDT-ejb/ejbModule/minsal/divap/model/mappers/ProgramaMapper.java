package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.enums.Subtitulo;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.EstadoProgramaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaServicioCore;

public class ProgramaMapper implements Mapper<ProgramaAno>{

	@Override
	public Object getSummary(ProgramaAno programaAno) {
		throw new NotImplementedException();
	}

	@Override
	public ProgramaVO getBasic(ProgramaAno programaAno) {
		
		if (programaAno == null)
			return null;
		
		ProgramaVO programaVO = new ProgramaVO();		
		programaVO.setIdProgramaAno(programaAno.getIdProgramaAno());
		programaVO.setDependenciaMunicipal(false);
		programaVO.setDependenciaServicio(false);
		if (programaAno.getEstado() != null){
			programaVO.setEstado(new EstadoProgramaVO(programaAno.getEstado().getIdEstadoPrograma(), programaAno.getEstado().getNombreEstado()));
		}else{
			programaVO.setEstado(new EstadoProgramaVO());
		}
		if(programaAno.getPrograma() != null){
			programaVO.setNombre(programaAno.getPrograma().getNombre());
			programaVO.setId(programaAno.getPrograma().getId());
			programaVO.setDescripcion(programaAno.getPrograma().getDescripcion());
			if(programaAno.getPrograma().getUsuario() != null){
				programaVO.setUsername(programaAno.getPrograma().getUsuario().getUsername());
			}
			if(programaAno.getPrograma().getComponentes() != null && programaAno.getPrograma().getComponentes().size() >0){
				List<ComponentesVO> componentesVO = new ArrayList<ComponentesVO>();
				for(Componente componente : programaAno.getPrograma().getComponentes()){
					componentesVO.add(new ComponenteMapper().getBasic(componente));
				}
				programaVO.setComponentes(componentesVO);
			}
			
			if (programaAno.getEstadoFlujoCaja()!=null)
			{
				EstadoProgramaVO estadoProgramaVO = new EstadoProgramaVO();
				estadoProgramaVO.setId(programaAno.getEstadoFlujoCaja().getIdEstadoPrograma());
				estadoProgramaVO.setNombre(programaAno.getEstadoFlujoCaja().getNombreEstado());
				
			    programaVO.setEstadoFlujocaja(estadoProgramaVO);
			}
		}
		
		if (programaAno.getProgramasServiciosCore()!=null)
		{
			List<ServiciosVO> lstServiciosVO = new ArrayList<ServiciosVO>();
			
			for (ProgramaServicioCore programaServicio : programaAno.getProgramasServiciosCore()) {
				ServiciosVO serviciosVO = new ServiciosVO();
				serviciosVO.setId_servicio(programaServicio.getServicio().getId());
				serviciosVO.setNombre_servicio(programaServicio.getServicio().getNombre());
				List<ComunaVO> lstComunas = new ArrayList<ComunaVO>();
				for (Comuna comuna : programaServicio.getServicio().getComunas()) {
					ComunaVO comunaVO = new ComunaVO();
					comunaVO.setNombre(comuna.getNombre());
					comunaVO.setIdComuna(comuna.getId());
					lstComunas.add(comunaVO);
				}
				serviciosVO.setComuna(lstComunas);
			
				
				lstServiciosVO.add(serviciosVO);
			}
			programaVO.setServicios(lstServiciosVO);
			
		}
		System.out.println("programaVO.getComponentes().size()==>"+((programaVO.getComponentes() == null) ? 0 : programaVO.getComponentes().size()));
		if(programaVO.getComponentes() != null && programaVO.getComponentes().size() > 0){
			for(ComponentesVO componenteVO : programaVO.getComponentes()){
				System.out.println("Antes de determinar si es dependiente municipal o servicio");
				if(componenteVO.getSubtitulos().contains(new SubtituloVO(Subtitulo.SUBTITULO24.getId()))){
					programaVO.setDependenciaMunicipal(true);
					System.out.println("Dependencia municipal");
				}
				if((componenteVO.getSubtitulos().contains(new SubtituloVO(Subtitulo.SUBTITULO21.getId()))) || (componenteVO.getSubtitulos().contains(new SubtituloVO(Subtitulo.SUBTITULO22.getId()))) || (componenteVO.getSubtitulos().contains(new SubtituloVO(Subtitulo.SUBTITULO29.getId())))){
					programaVO.setDependenciaServicio(true);
					System.out.println("Dependencia servicio");
				}
			}
		}
		return programaVO;
	}

	@Override
	public Object getData(ProgramaAno programaAno) {
		throw new NotImplementedException();
	}

}
