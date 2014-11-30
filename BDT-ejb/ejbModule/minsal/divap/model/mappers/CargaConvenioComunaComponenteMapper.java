package minsal.divap.model.mappers;

import minsal.divap.vo.CargaConvenioComunaComponenteVO;
import minsal.divap.vo.ComponenteSummaryVO;
import minsal.divap.vo.DocumentSummaryVO;
import minsal.divap.vo.SubtituloSummaryVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.ConvenioComunaComponente;

public class CargaConvenioComunaComponenteMapper implements Mapper<ConvenioComunaComponente>{

	@Override
	public Object getSummary(ConvenioComunaComponente convenioComunaComponente) {
		throw new NotImplementedException();
	}

	@Override
	public CargaConvenioComunaComponenteVO getBasic(ConvenioComunaComponente convenioComunaComponente) {
		if (convenioComunaComponente == null)
			return null;
		CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO = new CargaConvenioComunaComponenteVO();
		ComponenteSummaryVO componente = new ComponenteSummaryVO();
		componente.setId(convenioComunaComponente.getComponente().getId());
		componente.setNombre(convenioComunaComponente.getComponente().getNombre());
		cargaConvenioComunaComponenteVO.setComponente(componente);
		if(convenioComunaComponente.getDocumentoConvenio() != null) {
			DocumentSummaryVO documento = new DocumentSummaryVO();
			documento.setIdDocumento(convenioComunaComponente.getDocumentoConvenio().getId());
			documento.setDescDocumento(convenioComunaComponente.getDocumentoConvenio().getPath());
			cargaConvenioComunaComponenteVO.setDocumento(documento);
		}
		cargaConvenioComunaComponenteVO.setIdComuna(convenioComunaComponente.getConvenioComuna().getIdComuna().getId());
		cargaConvenioComunaComponenteVO.setNombreComuna(convenioComunaComponente.getConvenioComuna().getIdComuna().getNombre());
		cargaConvenioComunaComponenteVO.setNuevo(false);
		cargaConvenioComunaComponenteVO.setIdConvenioComuna(convenioComunaComponente.getConvenioComuna().getIdConvenioComuna());
		cargaConvenioComunaComponenteVO.setMontoIngresado(convenioComunaComponente.getMontoIngresado());
		cargaConvenioComunaComponenteVO.setNumeroResoucion(convenioComunaComponente.getConvenioComuna().getNumeroResolucion());
		SubtituloSummaryVO subtitulo = new SubtituloSummaryVO();
		subtitulo.setIdSubtitulo(convenioComunaComponente.getSubtitulo().getIdTipoSubtitulo());
		subtitulo.setNombre(convenioComunaComponente.getSubtitulo().getNombreSubtitulo());
		cargaConvenioComunaComponenteVO.setSubtitulo(subtitulo);
		return cargaConvenioComunaComponenteVO;
	}

	@Override
	public Object getData(ConvenioComunaComponente convenioComunaComponente) {
		throw new NotImplementedException();
	}

}
