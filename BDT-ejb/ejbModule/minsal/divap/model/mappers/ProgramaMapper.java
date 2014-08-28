package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.enums.EstadosProgramas;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.DependenciaProgramaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.TipoProgramaVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ProgramaAno;

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
		programaVO.setId(programaAno.getIdProgramaAno());
		
		
		if(programaAno.getPrograma() != null){
			programaVO.setNombre(programaAno.getPrograma().getNombre());
			programaVO.setDescripcion(programaAno.getPrograma().getDescripcion());
			if(programaAno.getPrograma().getUsuario() != null){
				programaVO.setUsername(programaAno.getPrograma().getUsuario().getUsername());
			}
			if(programaAno.getPrograma().getIdTipoPrograma() != null){
				programaVO.setTipoPrograma(new TipoProgramaVO(programaAno.getPrograma().getIdTipoPrograma().getId(), programaAno.getPrograma().getIdTipoPrograma().getNombre()));
			}
			if(programaAno.getPrograma().getDependencia() != null){
				programaVO.setDependenciaPrograma(new DependenciaProgramaVO(programaAno.getPrograma().getDependencia().getIdDependenciaPrograma(), programaAno.getPrograma().getDependencia().getNombre()));
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
			    programaVO.setEstadoFlujocaja(programaAno.getEstadoFlujoCaja());
			}
		}
		return programaVO;
	}

	@Override
	public Object getData(ProgramaAno programaAno) {
		throw new NotImplementedException();
	}

}
