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
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;

public class ConvenioComunaComponenteSubtituloMapper implements Mapper<ConvenioComuna>{

	@Override
	public Object getSummary(ConvenioComuna convenioComuna) {
		throw new NotImplementedException();
	}

	@Override
	public CargaConvenioComponenteSubtituloVO getBasic(ConvenioComuna convenioComuna) {
		if (convenioComuna == null)
			return null;
		CargaConvenioComponenteSubtituloVO cargaConvenioComponenteSubtituloVO = new CargaConvenioComponenteSubtituloVO();
		cargaConvenioComponenteSubtituloVO.setDependenciaMuncipal(true);
		cargaConvenioComponenteSubtituloVO.setFechaResolucion(convenioComuna.getFechaResolucion());
		cargaConvenioComponenteSubtituloVO.setNumeroResolucionInicial(convenioComuna.getNumeroResolucion());
		cargaConvenioComponenteSubtituloVO.setConvenioInicial(convenioComuna.getIdConvenioComuna());
		if(convenioComuna.getDocumentoConvenio() != null){
			cargaConvenioComponenteSubtituloVO.setDocumento(convenioComuna.getDocumentoConvenio().getId());
		}
		ProgramaVO programa = new ProgramaMapper().getBasic(convenioComuna.getIdPrograma());
		cargaConvenioComponenteSubtituloVO.setPrograma(programa);
		if(convenioComuna.getIdComuna() != null){
			cargaConvenioComponenteSubtituloVO.setItem(convenioComuna.getIdComuna().getId());
			cargaConvenioComponenteSubtituloVO.setNombre(convenioComuna.getIdComuna().getNombre());
		}
		
		if(convenioComuna.getConvenioComunaComponentes() != null && convenioComuna.getConvenioComunaComponentes().size() > 0){
			List<ConvenioComponenteSubtituloVO> convenioComponentesSubtitulosVO = new ArrayList<ConvenioComponenteSubtituloVO>();
			for(ConvenioComunaComponente convenioComunaComponente : convenioComuna.getConvenioComunaComponentes()){
				ConvenioComponenteSubtituloVO convenioComponenteSubtituloVO = new ConvenioComponenteSubtituloVO();
				convenioComponenteSubtituloVO.setMonto(convenioComunaComponente.getMontoIngresado());
				ComponentesVO componente = new ComponenteMapper().getBasic(convenioComunaComponente.getComponente());
				convenioComponenteSubtituloVO.setComponente(componente);
			    SubtituloVO subtitulo = null;
			    if(convenioComunaComponente.getSubtitulo() != null){
			    	subtitulo = new SubtituloVO(convenioComunaComponente.getSubtitulo().getIdTipoSubtitulo(), convenioComunaComponente.getSubtitulo().getNombreSubtitulo());
					DependenciaVO dependencia = new DependenciaVO();
					if(convenioComunaComponente.getSubtitulo().getDependencia() != null){
						dependencia.setId(convenioComunaComponente.getSubtitulo().getDependencia().getIdDependenciaPrograma());
						dependencia.setNombre(convenioComunaComponente.getSubtitulo().getDependencia().getNombre());
					}
					subtitulo.setDependencia(dependencia);
			    }
				convenioComponenteSubtituloVO.setSubtitulo(subtitulo);
				convenioComponentesSubtitulosVO.add(convenioComponenteSubtituloVO);
			}
			cargaConvenioComponenteSubtituloVO.setConvenioComponentesSubtitulosVO(convenioComponentesSubtitulosVO);
		}
		List<ServiciosSummaryVO> documentosResoluciones = new ArrayList<ServiciosSummaryVO>();
		if(convenioComuna.getDocumentoConvenio() != null){
			ServiciosSummaryVO serviciosSummaryVO = new ServiciosSummaryVO();
			serviciosSummaryVO.setNombre_servicio(convenioComuna.getNumeroResolucion().toString());
			serviciosSummaryVO.setId_servicio(convenioComuna.getDocumentoConvenio().getId());
			documentosResoluciones.add(serviciosSummaryVO);
			cargaConvenioComponenteSubtituloVO.setDocumentosResoluciones(documentosResoluciones);
		}
		return cargaConvenioComponenteSubtituloVO;
	}

	@Override
	public Object getData(ConvenioComuna convenioComuna) {
		throw new NotImplementedException();
	}

}
