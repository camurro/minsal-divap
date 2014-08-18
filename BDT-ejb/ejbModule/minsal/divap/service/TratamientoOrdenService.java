package minsal.divap.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.TratamientoOrdenDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorOficioConsulta;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionRecursosPercapitaSheetExcel;
import minsal.divap.model.mappers.AsignacionDistribucionPercapitaMapper;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.VariacionPoblacionVO;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.DistribucionInicialPercapita;

@Stateless
@LocalBean
public class TratamientoOrdenService {
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	
	@EJB
	private TratamientoOrdenDAO tratamientoOrdenDAO;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private DocumentService documentService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private SeguimientoService seguimientoService;
	@EJB
	private EmailService emailService;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="tmpDirDoc")
	private String tmpDirDoc;
	@Resource(name="folderTemplatePercapita")
	private String folderTemplatePercapita;
	@Resource(name="folderPercapita")
	private String folderPercapita;

	
	/**
	 * METODO EL CUAL BUSCARA LOS DATOS CORRESPONDIENTE A LAS ACTIVIDADES:
	 * Revisar Antecedentes Línea Programática / Revisar Antecedentes Per Cápita / Revisar Antecedentes Leyes
	 */
	public List<AsignacionDistribucionPerCapitaVO> findDatos(Integer idPrograma) {
		
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = null;
		antecendentesComunaCalculado = tratamientoOrdenDAO.findAntecendentes(idPrograma);
		
		List<AsignacionDistribucionPerCapitaVO> antecedentesCalculados = new ArrayList<AsignacionDistribucionPerCapitaVO>();
		
		if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
			
			for(AntecendentesComunaCalculado antecendenteComunaCalculado : antecendentesComunaCalculado){
				
				AsignacionDistribucionPerCapitaVO asignacionDistribucionPerCapitaVO = new AsignacionDistribucionPercapitaMapper().getBasic(antecendenteComunaCalculado);
				
				if(asignacionDistribucionPerCapitaVO != null){
					
					antecedentesCalculados.add(asignacionDistribucionPerCapitaVO);
				}
			}
		}
		return antecedentesCalculados;
	}


}
