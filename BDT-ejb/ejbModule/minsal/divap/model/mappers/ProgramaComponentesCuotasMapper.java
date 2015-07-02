package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.vo.ComponenteSummaryVO;
import minsal.divap.vo.CuotaSummaryVO;
import minsal.divap.vo.ProgramaComponentesCuotasSummaryVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaComponente;

public class ProgramaComponentesCuotasMapper implements Mapper<ProgramaAno>{

	@Override
	public ProgramaComponentesCuotasSummaryVO getSummary(ProgramaAno programaAno) {
		if (programaAno == null)
			return null;
		ProgramaComponentesCuotasSummaryVO programaComponentesCuotasSummaryVO = new ProgramaComponentesCuotasSummaryVO();
		programaComponentesCuotasSummaryVO.setIdProgramaAno(programaAno.getIdProgramaAno());
		programaComponentesCuotasSummaryVO.setIdPrograma(programaAno.getPrograma().getId());
		programaComponentesCuotasSummaryVO.setNombrePrograma(programaAno.getPrograma().getNombre());
		if(programaAno.getProgramaComponentes() != null && programaAno.getProgramaComponentes().size() > 0){
			for(ProgramaComponente programaComponente : programaAno.getProgramaComponentes()){
				ComponenteSummaryVO componenteSummaryVO = new ComponenteSummaryVO();
				componenteSummaryVO.setNombre(programaComponente.getComponente().getNombre());
				programaComponentesCuotasSummaryVO.setComponente(componenteSummaryVO);
				break;
			}
		}
		if(programaAno.getCuotas() != null && programaAno.getCuotas().size() > 0){
			List<CuotaSummaryVO> cuotas = new ArrayList<CuotaSummaryVO>();
			for(Cuota cuota : programaAno.getCuotas()){
				CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
				cuotaSummaryVO.setNumeroCuota(new Integer(cuota.getNumeroCuota()));
				cuotaSummaryVO.setPorcentaje(cuota.getPorcentaje());
				cuotas.add(cuotaSummaryVO);
			}
			programaComponentesCuotasSummaryVO.setCuotas(cuotas);
		}
		return programaComponentesCuotasSummaryVO;
	}

	public ProgramaComponentesCuotasSummaryVO getSummary(ProgramaAno programaAno, Integer idComponente) {
		if (programaAno == null)
			return null;
		ProgramaComponentesCuotasSummaryVO programaComponentesCuotasSummaryVO = new ProgramaComponentesCuotasSummaryVO();
		programaComponentesCuotasSummaryVO.setIdProgramaAno(programaAno.getIdProgramaAno());
		programaComponentesCuotasSummaryVO.setIdPrograma(programaAno.getPrograma().getId());
		programaComponentesCuotasSummaryVO.setNombrePrograma(programaAno.getPrograma().getNombre());
		if(programaAno.getProgramaComponentes() != null && programaAno.getProgramaComponentes().size() > 0){
			for(ProgramaComponente programaComponente : programaAno.getProgramaComponentes()){
				if(idComponente.equals(programaComponente.getComponente().getId())){
					ComponenteSummaryVO componenteSummaryVO = new ComponenteSummaryVO();
					componenteSummaryVO.setNombre(programaComponente.getComponente().getNombre());
					programaComponentesCuotasSummaryVO.setComponente(componenteSummaryVO);
					break;
				}
			} 

		}
		if(programaAno.getCuotas() != null && programaAno.getCuotas().size() > 0){
			List<CuotaSummaryVO> cuotas = new ArrayList<CuotaSummaryVO>();
			for(Cuota cuota : programaAno.getCuotas()){
				CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
				cuotaSummaryVO.setNumeroCuota(new Integer(cuota.getNumeroCuota()));
				cuotaSummaryVO.setPorcentaje(cuota.getPorcentaje());
				cuotas.add(cuotaSummaryVO);
			}
			programaComponentesCuotasSummaryVO.setCuotas(cuotas);
		}
		return programaComponentesCuotasSummaryVO;
	}

	@Override
	public Object getBasic(ProgramaAno programaAno) {
		throw new NotImplementedException();

	}

	@Override
	public Object getData(ProgramaAno programaAno) {
		throw new NotImplementedException();
	}

}
