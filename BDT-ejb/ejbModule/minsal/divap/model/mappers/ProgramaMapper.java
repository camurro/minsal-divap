package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.enums.Subtitulo;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.EstadoProgramaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.MarcoPresupuestario;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ServicioSalud;

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
		if(programaAno.getAno() != null){
			programaVO.setAno(programaAno.getAno().getAno());
		}
		programaVO.setDependenciaMunicipal(false);
		programaVO.setDependenciaServicio(false);
		
		if (programaAno.getEstado() != null){
			programaVO.setEstado(new EstadoProgramaVO(programaAno.getEstado().getIdEstadoPrograma(), programaAno.getEstado().getNombreEstado()));
		}else{
			programaVO.setEstado(new EstadoProgramaVO());
		}
		if (programaAno.getEstadoFlujoCaja()!=null)	{
			programaVO.setEstadoFlujocaja(new EstadoProgramaVO(programaAno.getEstadoFlujoCaja().getIdEstadoPrograma(), programaAno.getEstadoFlujoCaja().getNombreEstado()));
		}else{
			programaVO.setEstadoFlujocaja(new EstadoProgramaVO());
		}
		if (programaAno.getEstadoConvenio()!=null)	{
			programaVO.setEstadoConvenio(new EstadoProgramaVO(programaAno.getEstadoConvenio().getIdEstadoPrograma(), programaAno.getEstadoConvenio().getNombreEstado()));
		}else{
			programaVO.setEstadoConvenio(new EstadoProgramaVO());
		}
		if (programaAno.getEstadoreliquidacion()!=null)	{
			programaVO.setEstadoReliquidacion(new EstadoProgramaVO(programaAno.getEstadoreliquidacion().getIdEstadoPrograma(), programaAno.getEstadoreliquidacion().getNombreEstado()));
		}else{
			programaVO.setEstadoReliquidacion(new EstadoProgramaVO());
		}
		if(programaAno.getPrograma() != null){
			programaVO.setNombre(programaAno.getPrograma().getNombre());
			programaVO.setId(programaAno.getPrograma().getId());
			programaVO.setDescripcion(programaAno.getPrograma().getDescripcion());
			programaVO.setRevisaFonasa(programaAno.getPrograma().getRevisaFonasa());
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
		
		}
		if(programaVO.getComponentes() != null && programaVO.getComponentes().size() > 0){
			for(ComponentesVO componenteVO : programaVO.getComponentes()){
				if(componenteVO.getSubtitulos().contains(new SubtituloVO(Subtitulo.SUBTITULO24.getId()))){
					programaVO.setDependenciaMunicipal(true);
				}
				if((componenteVO.getSubtitulos().contains(new SubtituloVO(Subtitulo.SUBTITULO21.getId()))) || (componenteVO.getSubtitulos().contains(new SubtituloVO(Subtitulo.SUBTITULO22.getId()))) || (componenteVO.getSubtitulos().contains(new SubtituloVO(Subtitulo.SUBTITULO29.getId())))){
					programaVO.setDependenciaServicio(true);
				}
			}
		}
		if(programaAno.getMarcosPresupuestarios() != null && programaAno.getMarcosPresupuestarios().size() >0){
			List<ServiciosVO> servicios = new ArrayList<ServiciosVO>();
			for(MarcoPresupuestario marcoPresupuestario: programaAno.getMarcosPresupuestarios()){
				ServicioSalud servicioSalud = marcoPresupuestario.getServicioSalud();
				servicios.add(new ServicioMapper().getBasic(servicioSalud));
			}
			programaVO.setServicios(servicios);
		}
		return programaVO;
	}

	@Override
	public Object getData(ProgramaAno programaAno) {
		throw new NotImplementedException();
	}

}
