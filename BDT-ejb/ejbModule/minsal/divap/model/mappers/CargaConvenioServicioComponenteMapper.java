package minsal.divap.model.mappers;

import minsal.divap.vo.CargaConvenioServicioComponenteVO;
import minsal.divap.vo.ComponenteSummaryVO;
import minsal.divap.vo.DocumentSummaryVO;
import minsal.divap.vo.SubtituloSummaryVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.ConvenioServicioComponente;

public class CargaConvenioServicioComponenteMapper implements Mapper<ConvenioServicioComponente>{

	@Override
	public Object getSummary(ConvenioServicioComponente convenioServicioComponente) {
		throw new NotImplementedException();
	}

	@Override
	public CargaConvenioServicioComponenteVO getBasic(ConvenioServicioComponente convenioServicioComponente) {
		if (convenioServicioComponente == null)
			return null;
		CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVO = new CargaConvenioServicioComponenteVO();
		ComponenteSummaryVO componente = new ComponenteSummaryVO();
		componente.setId(convenioServicioComponente.getComponente().getId());
		componente.setNombre(convenioServicioComponente.getComponente().getNombre());
		cargaConvenioServicioComponenteVO.setComponente(componente);
		if(convenioServicioComponente.getDocumentoConvenio() != null) {
			DocumentSummaryVO documento = new DocumentSummaryVO();
			documento.setIdDocumento(convenioServicioComponente.getDocumentoConvenio().getId());
			documento.setDescDocumento(convenioServicioComponente.getDocumentoConvenio().getPath());
			cargaConvenioServicioComponenteVO.setDocumento(documento);
		}
		cargaConvenioServicioComponenteVO.setIdEstablecimiento(convenioServicioComponente.getConvenioServicio().getIdEstablecimiento().getId());
		cargaConvenioServicioComponenteVO.setNombreEstablecimiento(convenioServicioComponente.getConvenioServicio().getIdEstablecimiento().getNombre());
		cargaConvenioServicioComponenteVO.setNuevo(false);
		cargaConvenioServicioComponenteVO.setIdConvenioServicio(convenioServicioComponente.getConvenioServicio().getIdConvenioServicio());
		cargaConvenioServicioComponenteVO.setMontoIngresado(convenioServicioComponente.getMontoIngresado());
		cargaConvenioServicioComponenteVO.setNumeroResoucion(convenioServicioComponente.getConvenioServicio().getNumeroResolucion());
		SubtituloSummaryVO subtitulo = new SubtituloSummaryVO();
		subtitulo.setIdSubtitulo(convenioServicioComponente.getSubtitulo().getIdTipoSubtitulo());
		subtitulo.setNombre(convenioServicioComponente.getSubtitulo().getNombreSubtitulo());
		cargaConvenioServicioComponenteVO.setSubtitulo(subtitulo);
		return cargaConvenioServicioComponenteVO;
	}

	@Override
	public Object getData(ConvenioServicioComponente convenioServicioComponente) {
		throw new NotImplementedException();
	}

}
