package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.vo.CargaConvenioComponenteSubtituloVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ConvenioComponenteSubtituloVO;
import minsal.divap.vo.DependenciaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosSummaryVO;
import minsal.divap.vo.SubtituloVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.ConvenioServicioComponente;

public class ConvenioServicioComponenteSubtituloMapper implements Mapper<ConvenioServicio>{

	@Override
	public Object getSummary(ConvenioServicio convenioServicio) {
		throw new NotImplementedException();
	}

	@Override
	public CargaConvenioComponenteSubtituloVO getBasic(ConvenioServicio convenioServicio) {
		if (convenioServicio == null)
			return null;
		CargaConvenioComponenteSubtituloVO cargaConvenioComponenteSubtituloVO = new CargaConvenioComponenteSubtituloVO();
		cargaConvenioComponenteSubtituloVO.setDependenciaMuncipal(false);
		cargaConvenioComponenteSubtituloVO.setFechaResolucion(convenioServicio.getFechaResolucion());
		cargaConvenioComponenteSubtituloVO.setNumeroResolucionInicial(convenioServicio.getNumeroResolucion());
		cargaConvenioComponenteSubtituloVO.setConvenioInicial(convenioServicio.getIdConvenioServicio());
		if(convenioServicio.getDocumentoConvenio() != null){
			cargaConvenioComponenteSubtituloVO.setDocumento(convenioServicio.getDocumentoConvenio().getId());
		}
		ProgramaVO programa = new ProgramaMapper().getBasic(convenioServicio.getIdPrograma());
		cargaConvenioComponenteSubtituloVO.setPrograma(programa);
		if(convenioServicio.getIdEstablecimiento() != null){
			cargaConvenioComponenteSubtituloVO.setItem(convenioServicio.getIdEstablecimiento().getId());
			cargaConvenioComponenteSubtituloVO.setNombre(convenioServicio.getIdEstablecimiento().getNombre());
		}
		
		if(convenioServicio.getConvenioServicioComponentes() != null && convenioServicio.getConvenioServicioComponentes().size() > 0){
			List<ConvenioComponenteSubtituloVO> convenioComponentesSubtitulosVO = new ArrayList<ConvenioComponenteSubtituloVO>();
			for(ConvenioServicioComponente convenioServicioComponente : convenioServicio.getConvenioServicioComponentes()){
				ConvenioComponenteSubtituloVO convenioComponenteSubtituloVO = new ConvenioComponenteSubtituloVO();
				convenioComponenteSubtituloVO.setMonto(convenioServicioComponente.getMontoIngresado());
				ComponentesVO componente = new ComponenteMapper().getBasic(convenioServicioComponente.getComponente());
				convenioComponenteSubtituloVO.setComponente(componente);
			    SubtituloVO subtitulo = null;
			    if(convenioServicioComponente.getSubtitulo() != null){
			    	subtitulo = new SubtituloVO(convenioServicioComponente.getSubtitulo().getIdTipoSubtitulo(), convenioServicioComponente.getSubtitulo().getNombreSubtitulo());
					DependenciaVO dependencia = new DependenciaVO();
					if(convenioServicioComponente.getSubtitulo().getDependencia() != null){
						dependencia.setId(convenioServicioComponente.getSubtitulo().getDependencia().getIdDependenciaPrograma());
						dependencia.setNombre(convenioServicioComponente.getSubtitulo().getDependencia().getNombre());
					}
					subtitulo.setDependencia(dependencia);
			    }
				convenioComponenteSubtituloVO.setSubtitulo(subtitulo);
				convenioComponentesSubtitulosVO.add(convenioComponenteSubtituloVO);
			}
			cargaConvenioComponenteSubtituloVO.setConvenioComponentesSubtitulosVO(convenioComponentesSubtitulosVO);
		}
		List<ServiciosSummaryVO> documentosResoluciones = new ArrayList<ServiciosSummaryVO>();
		Integer idConvenioServicio = convenioServicio.getIdConvenioServicio();
		if(convenioServicio.getConveniosServicio() != null && convenioServicio.getConveniosServicio().size() > 0){
			for(ConvenioServicio convenioServicioLocal : convenioServicio.getConveniosServicio()){
				ServiciosSummaryVO serviciosSummaryVO = new ServiciosSummaryVO();
				serviciosSummaryVO.setNombre_servicio(convenioServicioLocal.getNumeroResolucion().toString());
				serviciosSummaryVO.setId_servicio(convenioServicioLocal.getDocumentoConvenio().getId());
				documentosResoluciones.add(serviciosSummaryVO);
			}
		}else{
			if(convenioServicio.getNumeroResolucionInicial() != null && convenioServicio.getNumeroResolucionInicial().getConveniosServicio() != null && convenioServicio.getNumeroResolucionInicial().getConveniosServicio().size() > 0){
				for(ConvenioServicio convenioServicioLocal : convenioServicio.getNumeroResolucionInicial().getConveniosServicio()){
					if(!idConvenioServicio.equals(convenioServicioLocal.getIdConvenioServicio())){
						ServiciosSummaryVO serviciosSummaryVO = new ServiciosSummaryVO();
						serviciosSummaryVO.setNombre_servicio(convenioServicioLocal.getNumeroResolucion().toString());
						serviciosSummaryVO.setId_servicio(convenioServicioLocal.getDocumentoConvenio().getId());
						documentosResoluciones.add(serviciosSummaryVO);
					}
				}
			}
		}
		cargaConvenioComponenteSubtituloVO.setDocumentosResoluciones(documentosResoluciones);
		return cargaConvenioComponenteSubtituloVO;
	}

	@Override
	public Object getData(ConvenioServicio convenioServicio) {
		throw new NotImplementedException();
	}

}
