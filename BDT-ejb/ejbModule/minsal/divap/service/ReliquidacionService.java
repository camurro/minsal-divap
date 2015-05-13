package minsal.divap.service;

import java.io.File;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.AnoDAO;
import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.ReliquidacionDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.EstadosConvenios;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.FieldType;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.CrearPlanillaCumplimientoMunicialProgramaSheetExcel;
import minsal.divap.excel.impl.CrearPlanillaCumplimientoServicioProgramaSheetExcel;
import minsal.divap.excel.impl.PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel;
import minsal.divap.excel.impl.PlanillaTrabajoCumplimientoReliquidacionServicioSheetExcel;
import minsal.divap.excel.impl.ReliquidacionCalculoExcelValidator;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.model.mappers.ProgramaComponentesCuotasMapper;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CalculoReliquidacionBaseVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.ComponenteCumplimientoVO;
import minsal.divap.vo.ComponenteReliquidacionPageVO;
import minsal.divap.vo.ComponenteReliquidacionVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.CumplimientoApsMunicipalProgramaVO;
import minsal.divap.vo.CuotaSummaryVO;
import minsal.divap.vo.EstablecimientoSummaryVO;
import minsal.divap.vo.ProgramaComponentesCuotasSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.vo.ValorizarReliquidacionPageSummaryVO;
import minsal.divap.vo.ValorizarReliquidacionSummaryVO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.ConvenioServicioComponente;
import cl.minsal.divap.model.CumplimientoPrograma;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.Reliquidacion;
import cl.minsal.divap.model.ReliquidacionComuna;
import cl.minsal.divap.model.ReliquidacionComunaComponente;
import cl.minsal.divap.model.ReliquidacionServicio;
import cl.minsal.divap.model.ReliquidacionServicioComponente;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoSubtitulo;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class ReliquidacionService {
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private AnoDAO anoDAO;
	@EJB
	private DocumentService documentService;
	@EJB
	private MesDAO mesDAO;
	@EJB
	private ProgramasDAO programasDAO;	
	@EJB
	private ComunaDAO comunaDAO;
	@EJB
	private MantenedoresDAO mantenedoresDAO;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private ProgramasService programasService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private ReliquidacionDAO reliquidacionDAO;
	@EJB
	private ComponenteService componenteService;
	@EJB 
	private ConveniosDAO conveniosDAO;
	@EJB
	private CajaDAO cajaDAO;
	@EJB
	private EstablecimientosDAO establecimientosDAO;
	@EJB
	private TipoSubtituloDAO tipoSubtituloDAO;

	private final double EPSILON = 0.0000001;


	@Resource(name="tmpDir")
	private String tmpDir;

	@Resource(name = "folderTemplateReliquidacion")
	private String folderTemplateReliquidacion;

	@Resource(name = "folderDocReliquidacion")
	private String folderDocReliquidacion;


	public Integer crearIntanciaDistribucionInicialPercapita(String username){
		System.out.println("username-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		AnoEnCurso anoEnCurso = anoDAO.getAnoById(getAnoCurso());
		return distribucionInicialPercapitaDAO.crearIntanciaDistribucionInicialPercapita(usuario, anoEnCurso);
	}

	public void moveToAlfresco(Integer idReliquidacion, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Boolean lastVersion ) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderDocReliquidacion.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			Reliquidacion reliquidacion = reliquidacionDAO.getReliquidacionById(idReliquidacion);
			documentService.createDocumentReliquidacion(reliquidacion, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}

	public Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}

	public String getMesCurso(Boolean numero) {
		SimpleDateFormat dateFormat = null;
		String mesCurso = null;
		if(numero){
			dateFormat = new SimpleDateFormat("MM");
			mesCurso = dateFormat.format(new Date());
		}else{
			dateFormat = new SimpleDateFormat("MMMM");
			mesCurso = dateFormat.format(new Date());
		}
		return mesCurso;
	}

	public Integer crearInstanciaReliquidacion(String username) {
		System.out.println("username-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		Mes mesEnCurso = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
		return reliquidacionDAO.crearInstanciaReliquidacion(usuario, mesEnCurso);
	}

	public void cambiarEstadoPrograma(Integer idPrograma,
			EstadosProgramas estadoPrograma) {
		System.out.println("idPrograma-->" + idPrograma + " estadoPrograma.getId()-->"+estadoPrograma.getId());
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idPrograma);
		programaAno.setEstadoreliquidacion(new EstadoPrograma(estadoPrograma.getId()));
		System.out.println("Cambia estado ok");
	}

	//############RELIQUIDACION#########################
	public String elaborarPlantillasBaseReliquidacion(Integer idProgramaAno, Integer idReliquidacion) {
		String planillaTrabajoId = null;
		Integer plantillaIdMuncipal = null;
		Integer plantillaIdServicio = null;

		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		if(programaVO.getDependenciaMunicipal() != null && programaVO.getDependenciaMunicipal()){
			plantillaIdMuncipal = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLABASECUMPLIMIENTOMUNICIPAL, programaVO.getId());
		}
		if(programaVO.getDependenciaServicio() != null && programaVO.getDependenciaServicio()){
			plantillaIdServicio = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLABASECUMPLIMIENTOSERVICIO, programaVO.getId());
		}
		
		System.out.println("son nuevas");
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		List<CellExcelVO> headerServicio = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeaderServicio = new ArrayList<CellExcelVO>();
		if(programaVO.getDependenciaMunicipal()){		
			System.out.println("tiene dependencia comuna");
			Integer plantillaMunicipal = plantillaCumplimientoMunicipalNivelPrograma(programaVO, header, subHeader, idReliquidacion);
			planillaTrabajoId = plantillaMunicipal.toString();
		}
		if(programaVO.getDependenciaServicio()){
			System.out.println("tiene dependencia servicio");
			Integer plantillaServicio = plantillaCumplimientoEstablecimientoServicioNivelPrograma(programaVO, headerServicio, subHeaderServicio, idReliquidacion);
			planillaTrabajoId = ((planillaTrabajoId == null) ? plantillaServicio.toString() : (planillaTrabajoId + "#" +plantillaServicio.toString()));
		}
		System.out.println("planillaTrabajoId ---> "+planillaTrabajoId);
		return planillaTrabajoId;
	}

	public Integer plantillaCumplimientoMunicipalNivelPrograma(ProgramaVO programaAno, List<CellExcelVO> header, List<CellExcelVO> subHeader, Integer idReliquidacion){
		Integer planillaTrabajoId = null;
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String name = programaAno.getNombre().replace(":", "");
		name = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		String filename = tmpDir + File.separator + "plantillaBaseReliquidacionMunicpal " + name + ".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programaAno.getId(),  Subtitulo.SUBTITULO24);
		List<Double> porc_cumplimiento = new ArrayList<Double>();
		Programa programa = programasDAO.getProgramaPorID(programaAno.getId());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		header.add(new CellExcelVO("SERVICIOS DE SALUD", 2, 2));
		header.add(new CellExcelVO("COMUNAS", 2, 2));

		header.add(new CellExcelVO(programaAno.getNombre().toUpperCase(), componentes.size(), 1));
		subHeader.add(new CellExcelVO("ID", 1, 2));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
		subHeader.add(new CellExcelVO("ID", 1, 2));
		subHeader.add(new CellExcelVO("Comuna", 1, 2));

		for(int i=0 ; i < componentes.size(); i++){
			header.add(new CellExcelVO(componentes.get(i).getNombre(), 1, 1));
			for(SubtituloVO subtitulo : componentes.get(i).getSubtitulos()){
				if(subtitulo.getId().equals(Subtitulo.SUBTITULO24.getId())){
					subHeader.add(new CellExcelVO(subtitulo.getNombre(), 1, 1));
					subHeader.add(new CellExcelVO("% Cumplimiento", 1, 1));
					porc_cumplimiento.add(null);
				}
			}
		}

		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		List<CumplimientoApsMunicipalProgramaVO> items = new ArrayList<CumplimientoApsMunicipalProgramaVO>();
		for(ServiciosVO servicio : servicios){
			if(servicio.getComunas() != null && servicio.getComunas().size() > 0){
				for(ComunaSummaryVO comuna : servicio.getComunas()){
					CumplimientoApsMunicipalProgramaVO cumplimiento = new CumplimientoApsMunicipalProgramaVO();
					cumplimiento.setIdServicio(servicio.getId_servicio());
					cumplimiento.setServicio(servicio.getNombre_servicio());
					cumplimiento.setIdComuna(comuna.getId());
					cumplimiento.setComuna(comuna.getNombre());
					cumplimiento.setPorcCumplimiento(porc_cumplimiento);
					items.add(cumplimiento);
				}
			}
		}
		CrearPlanillaCumplimientoMunicialProgramaSheetExcel crearPlanillaCumplimientoMunicialProgramaSheetExcel = new CrearPlanillaCumplimientoMunicialProgramaSheetExcel(header, subHeader, items);
		generadorExcel.addSheet(crearPlanillaCumplimientoMunicialProgramaSheetExcel, "Cumplimiento APS Municipales");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplateReliquidacion.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response crearPlanillaCumplimientoMunicialProgramaSheetExcel --->" + response);
			planillaTrabajoId = documentService.createTemplate(programa, TipoDocumentosProcesos.PLANTILLABASECUMPLIMIENTOMUNICIPAL, response.getNodeRef(), response.getFileName(), contenType);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	private Integer plantillaCumplimientoEstablecimientoServicioNivelPrograma(ProgramaVO programaAno, List<CellExcelVO> header, List<CellExcelVO> subHeader, Integer idReliquidacion){
		Integer planillaTrabajoId = null;
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String name = programaAno.getNombre().replace(":", "");
		name = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		String filename = tmpDir + File.separator + "plantillaBaseReliquidacionServicio " + name + ".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		Programa programa = programasDAO.getProgramaPorID(programaAno.getId());
		Subtitulo[] subtitulosServicio = {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programaAno.getId(), subtitulosServicio);
		int totalSubtitulos = 0;
		Map<Integer, Integer> subtitulosPorComponente = new HashMap<Integer, Integer>();
		for(ComponentesVO componente : componentes){
			Integer subtitulos = 0;
			for(SubtituloVO subtitulo : componente.getSubtitulos()){
				if(subtitulo.getId().equals(Subtitulo.SUBTITULO21.getId()) || subtitulo.getId().equals(Subtitulo.SUBTITULO22.getId()) || subtitulo.getId().equals(Subtitulo.SUBTITULO29.getId())){
					subtitulos++;
					totalSubtitulos++;
				}
			}
			if(subtitulos > 0){
				subtitulosPorComponente.put(componente.getId(), subtitulos);
			}
		}

		List<Double> porc_cumplimiento = new ArrayList<Double>();

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 2, 2));
		header.add(new CellExcelVO("ESTABLECIMIENTOS", 2, 2));

		header.add(new CellExcelVO(programaAno.getNombre().toUpperCase(), totalSubtitulos, 1));
		subHeader.add(new CellExcelVO("ID", 1, 2));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
		subHeader.add(new CellExcelVO("ID", 1, 2));
		subHeader.add(new CellExcelVO("Establecimiento", 1, 2));
		for(ComponentesVO componente : componentes){
			if(subtitulosPorComponente.get(componente.getId()) != null){
				header.add(new CellExcelVO(componente.getNombre().toUpperCase(), subtitulosPorComponente.get(componente.getId()), 1));
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId().equals(Subtitulo.SUBTITULO21.getId()) || subtitulo.getId().equals(Subtitulo.SUBTITULO22.getId()) || subtitulo.getId().equals(Subtitulo.SUBTITULO29.getId())){
						subHeader.add(new CellExcelVO(subtitulo.getNombre(), 1, 1));
						subHeader.add(new CellExcelVO("% Cumplimiento", 1, 1));
						porc_cumplimiento.add(null);
					}
				}
			}
		}
		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		List<CumplimientoApsMunicipalProgramaVO> items = new ArrayList<CumplimientoApsMunicipalProgramaVO>();
		for(ServiciosVO servicio : servicios ){
			if(servicio.getEstableclimientos() != null && servicio.getEstableclimientos().size() > 0){
				for(EstablecimientoSummaryVO establecimiento : servicio.getEstableclimientos()){				
					CumplimientoApsMunicipalProgramaVO cumplimiento = new CumplimientoApsMunicipalProgramaVO();
					cumplimiento.setIdServicio(servicio.getId_servicio());
					cumplimiento.setServicio(servicio.getNombre_servicio());
					cumplimiento.setIdComuna(establecimiento.getId());
					cumplimiento.setComuna(establecimiento.getNombre());
					cumplimiento.setPorcCumplimiento(porc_cumplimiento);
					items.add(cumplimiento);
				}
			}
		}
		CrearPlanillaCumplimientoServicioProgramaSheetExcel crearPlanillaCumplimientoServicioProgramaSheetExcel = new CrearPlanillaCumplimientoServicioProgramaSheetExcel(header, subHeader, items);
		generadorExcel.addSheet(crearPlanillaCumplimientoServicioProgramaSheetExcel, "Cumplimiento APS Servicio");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplateReliquidacion.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response crearPlanillaCumplimientoMunicialProgramaSheetExcel --->" + response);
			planillaTrabajoId = documentService.createTemplate(programa, TipoDocumentosProcesos.PLANTILLABASECUMPLIMIENTOSERVICIO, response.getNodeRef(), response.getFileName(), contenType);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	//#####################valorizar montos #####################

	public String valorizarMontosReliquidacion(Integer idProgramaAno, Integer idReliquidacion) {
		String idDocReliquidacion = null;
		Integer planillaTrabajoMunicipal = null;
		Integer planillaTrabajoServicio = null;
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		if(programaVO.getDependenciaMunicipal()  && programaVO.getDependenciaServicio()){
			List<CellExcelVO> header = new ArrayList<CellExcelVO>();
			List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
			List<CellExcelVO> headerServicio = new ArrayList<CellExcelVO>();
			List<CellExcelVO> subHeaderServicio = new ArrayList<CellExcelVO>();
			planillaTrabajoMunicipal = planillaTrabajoCumplimientoReliquidacionMunicipal(programaVO, header, subHeader, idReliquidacion);
			planillaTrabajoServicio = planillaTrabajoCumplimientoReliquidacionServicio(programaVO, headerServicio, subHeaderServicio, idReliquidacion);
			System.out.println("planillaTrabajoMunicipal="+planillaTrabajoMunicipal);
			System.out.println("planillaTrabajoServicio="+planillaTrabajoServicio);
			idDocReliquidacion = planillaTrabajoMunicipal + "#" + planillaTrabajoServicio;
			System.out.println("idDocReliquidacion="+idDocReliquidacion);
		}else{
			if(programaVO.getDependenciaMunicipal()){
				List<CellExcelVO> header = new ArrayList<CellExcelVO>();
				List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
				planillaTrabajoMunicipal = planillaTrabajoCumplimientoReliquidacionMunicipal(programaVO, header, subHeader, idReliquidacion);
				idDocReliquidacion = planillaTrabajoMunicipal.toString();
			}else{
				List<CellExcelVO> headerServicio = new ArrayList<CellExcelVO>();
				List<CellExcelVO> subHeaderServicio = new ArrayList<CellExcelVO>();
				planillaTrabajoServicio = planillaTrabajoCumplimientoReliquidacionServicio(programaVO, headerServicio, subHeaderServicio, idReliquidacion);
				idDocReliquidacion = planillaTrabajoServicio.toString();
			}
		}
		System.out.println("idDocReliquidacion ---> "+idDocReliquidacion);
		return idDocReliquidacion;
	}

	private Integer planillaTrabajoCumplimientoReliquidacionMunicipal(ProgramaVO programaVO, List<CellExcelVO> header,
			List<CellExcelVO> subHeader, Integer idReliquidacion) {

		Integer planillaTrabajoId = null;
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String name = programaVO.getNombre().replace(":", "");
		name = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		String filename = tmpDir + File.separator + "planillaTrabajoCumplimientoReliquidacionMunicipal " + name + ".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		Subtitulo[] subtitulosMunicipal = {Subtitulo.SUBTITULO24};
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programaVO.getId(), subtitulosMunicipal);
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 2, 2));
		header.add(new CellExcelVO("COMUNAS", 2, 2));
		header.add(new CellExcelVO(programaVO.getNombre().toUpperCase(), (componentes.size() * 9), 1));
		subHeader.add(new CellExcelVO("ID", 1, 2));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
		subHeader.add(new CellExcelVO("ID", 1, 2));
		subHeader.add(new CellExcelVO("Comuna", 1, 2));
		List<Integer> idComponentes = new ArrayList<Integer>();
		for(ComponentesVO componente : componentes){
			idComponentes.add(componente.getId());
			header.add(new CellExcelVO(componente.getNombre().toUpperCase(), 9, 1));
			//header.add(new CellExcelVO("Subtítulo 24", 9, 1));
			subHeader.add(new CellExcelVO("Convenio", 1, 2));
			List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAno(programaVO.getIdProgramaAno());
			System.out.println("planillaTrabajoCumplimientoReliquidacionMunicipal cuotasPagadas.size()="+((cuotasPagadas!=null)?cuotasPagadas.size():"0"));
			if(cuotasPagadas != null && cuotasPagadas.size() > 0){
				for(int cuotas = 0; cuotas < cuotasPagadas.size(); cuotas++){
					subHeader.add(new CellExcelVO("Cuota N° " + (cuotas+1), 2, 1));
					subHeader.add(new CellExcelVO("%", 1, 1));
					subHeader.add(new CellExcelVO("Monto", 1, 1));
				}
			}
			subHeader.add(new CellExcelVO("Reliquidación", 4, 1));
			subHeader.add(new CellExcelVO("% Cumplimiento", 1, 1));
			subHeader.add(new CellExcelVO("% Reliquidación", 1, 1));
			subHeader.add(new CellExcelVO("% Descuento última Cuota", 1, 1));
			subHeader.add(new CellExcelVO("% Monto Final última Cuota", 1, 1));
		}
		header.add(new CellExcelVO("Marco Final", 1, 5));
		List<ValorizarReliquidacionSummaryVO> items = calcularReliquidacionProgramaComuna(programaVO.getIdProgramaAno(), idComponentes, idReliquidacion);
		System.out.println("cantidad de items --> "+items.size());
		PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel planillaCumplimiento = new PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel(header, subHeader, items);
		generadorExcel.addSheet(planillaCumplimiento, "Cumplimiento APS Municipales");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderDocReliquidacion.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel --->" + response);
			planillaTrabajoId = documentService.createDocumentBaseReliquidacion(TipoDocumentosProcesos.PLANILLABASECUMPLIMIENTOMUNICIPAL, response.getNodeRef(), response.getFileName(), contenType, idReliquidacion);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	private Integer planillaTrabajoCumplimientoReliquidacionServicio(ProgramaVO programaVO, List<CellExcelVO> header,
			List<CellExcelVO> subHeader, Integer idReliquidacion) {

		Integer planillaTrabajoId = null;
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String name = programaVO.getNombre().replace(":", "");
		name = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		String filename = tmpDir + File.separator + "planillaTrabajoCumplimientoReliquidacionServicio " + name + ".xlsx";
		System.out.println("planillaTrabajoCumplimientoReliquidacionServicio--->"+filename);
		String contenType = mimemap.getContentType(filename.toLowerCase());
		Subtitulo[] subtitulosServicio = {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programaVO.getId(), subtitulosServicio);
		int totalSubtitulos = 0;
		Map <Integer , List<SubtituloVO>> subtitulosPorComponente = new HashMap<Integer, List<SubtituloVO>>();
		for(ComponentesVO componente : componentes){
			List<SubtituloVO> subtitulos = new ArrayList<SubtituloVO>();
			for(SubtituloVO subtituloVO : componente.getSubtitulos()){
				if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId()) || Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId()) || Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
					subtitulos.add(subtituloVO);
					totalSubtitulos++;
				}
			}
			subtitulosPorComponente.put(componente.getId(), subtitulos);
		}
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 2, 3));
		header.add(new CellExcelVO("ESTABLECIMIENTOS", 2, 3));
		header.add(new CellExcelVO(programaVO.getNombre().toUpperCase(), (totalSubtitulos * 9), 1));
		subHeader.add(new CellExcelVO("ID", 1, 2));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
		subHeader.add(new CellExcelVO("ID", 1, 2));
		subHeader.add(new CellExcelVO("Establecimiento", 1, 2));
		List<Integer> idComponentes = new ArrayList<Integer>();
		List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAno(programaVO.getIdProgramaAno());
		for(ComponentesVO componente : componentes){
			idComponentes.add(componente.getId());
			List<SubtituloVO> subtitulos = subtitulosPorComponente.get(componente.getId());
			header.add(new CellExcelVO(componente.getNombre().toUpperCase(), subtitulos.size() * 9, 1));
			for(SubtituloVO subtitulo : subtitulos){
				header.add(new CellExcelVO(subtitulo.getNombre().toUpperCase(), 9, 1));
				subHeader.add(new CellExcelVO("Convenio", 1, 2));
				System.out.println("planillaTrabajoCumplimientoReliquidacionServicio cuotasPagadas.size()="+((cuotasPagadas!=null)?cuotasPagadas.size():"0"));
				if(cuotasPagadas != null && cuotasPagadas.size() > 0){
					for(int cuotas = 0; cuotas < cuotasPagadas.size(); cuotas++){
						subHeader.add(new CellExcelVO("Cuota N° " + (cuotas+1), 2, 1));
						subHeader.add(new CellExcelVO("%", 1, 1));
						subHeader.add(new CellExcelVO("Monto", 1, 1));
					}
				}
				subHeader.add(new CellExcelVO("Reliquidación", 4, 1));
				subHeader.add(new CellExcelVO("% Cumplimiento", 1, 1));
				subHeader.add(new CellExcelVO("% Reliquidación", 1, 1));
				subHeader.add(new CellExcelVO("% Descuento última Cuota", 1, 1));
				subHeader.add(new CellExcelVO("% Monto Final última Cuota", 1, 1));
			}
		}
		header.add(new CellExcelVO("Marco Final", 1, 5));
		List<ValorizarReliquidacionSummaryVO> items = calcularReliquidacionProgramaServicio(programaVO.getIdProgramaAno(), idComponentes, subtitulosPorComponente, idReliquidacion);
		System.out.println("cantidad de items --> "+items.size());
		PlanillaTrabajoCumplimientoReliquidacionServicioSheetExcel planillaCumplimiento = new PlanillaTrabajoCumplimientoReliquidacionServicioSheetExcel(header, subHeader, items);
		generadorExcel.addSheet(planillaCumplimiento, "Cumplimiento APS Servicio");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderDocReliquidacion.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response crearPlanillaCumplimientoMunicialProgramaSheetExcel --->" + response);
			planillaTrabajoId = documentService.createDocumentBaseReliquidacion(TipoDocumentosProcesos.PLANILLABASECUMPLIMIENTOSERVICIO, response.getNodeRef(), response.getFileName(), contenType, idReliquidacion);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	private void calcularReliquidacionComuna(Integer idProgramaAno, Integer idServicio, Integer idReliquidacion){
		System.out.println("calcularReliquidacionServicio idProgramaAno="+idProgramaAno+" idServicio="+idServicio+" idReliquidacion="+idReliquidacion);
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);

		Subtitulo[] subtitulosServicio = {Subtitulo.SUBTITULO24};
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programaVO.getId(), subtitulosServicio);

		List<Comuna> comunas = comunaDAO.getComunasByServicio(idServicio);
		for(Comuna comuna : comunas){
			for(ComponentesVO componente : componentes){
				List<ConvenioComuna> conveniosComuna = conveniosDAO.getConveniosComunaByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.PAGADO.getId());
				Integer montoConvenio = 0;
				if(conveniosComuna != null && conveniosComuna.size() > 0){
					ConvenioComuna convenioComuna = conveniosComuna.get(0);
					int size = convenioComuna.getConvenioComunaComponentes().size();
					ConvenioComunaComponente convenioComunaComponente = convenioComuna.getConvenioComunaComponentes().get(size - 1);
					montoConvenio = convenioComunaComponente.getMontoIngresado();
				}
				ReliquidacionComuna reliquidacionComuna = reliquidacionDAO.getReliquidacionComunaByProgramaAnoComunaComponenteSubtituloReliquidacion(idProgramaAno, comuna.getId(), componente.getId(), Subtitulo.SUBTITULO24.getId(), idReliquidacion);
				if(reliquidacionComuna != null){
					ReliquidacionComunaComponente reliquidacionComunaComponente =  reliquidacionComuna.getReliquidacionComunaComponentes().iterator().next();
					CumplimientoPrograma cumplimientoRebaja = getCumplimientoPrograma(programaVO.getId(), reliquidacionComunaComponente.getPorcentajeCumplimiento());
					List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
					System.out.println("cuotasPagadas.size()="+((cuotasPagadas!=null) ? cuotasPagadas.size(): "0"));
					double porcentajeAcumulado = 0.0;
					if(cuotasPagadas != null && cuotasPagadas.size() > 0){
						int size = cuotasPagadas.size();
						int cuotaNro = 1;
						for(Cuota cuota : cuotasPagadas){
							if(cuotaNro == size){
								break;
							}
							porcentajeAcumulado+=cuota.getPorcentaje();
							cuotaNro++;
						}
						porcentajeAcumulado = porcentajeAcumulado/100.0;
					} 
					if(cumplimientoRebaja != null && cumplimientoRebaja.getRebaja() != null){
						reliquidacionComunaComponente.setCumplimiento(cumplimientoRebaja);
						System.out.println("calcularReliquidacionServicio convenioServicioComponente.getMonto()="+montoConvenio+" idServicio="+idServicio+" idReliquidacion="+idReliquidacion);
						Integer rebajaUltimaCouta = (int) (montoConvenio * (1 - porcentajeAcumulado) * (cumplimientoRebaja.getRebaja()/100.0));
						System.out.println("calcularReliquidacionServicio convenioServicioComponente.getMonto()="+montoConvenio+" rebajaUltimaCouta="+rebajaUltimaCouta);
						reliquidacionComunaComponente.setMontoRebaja(rebajaUltimaCouta);
					}
				}
			}
		}
	}

	public void calcularReliquidacionServicio(Integer idProgramaAno, Integer idServicio, Integer idReliquidacion){
		System.out.println("calcularReliquidacionServicio idProgramaAno="+idProgramaAno+" idServicio="+idServicio+" idReliquidacion="+idReliquidacion);
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);

		Subtitulo[] subtitulosServicio = {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programaVO.getId(), subtitulosServicio);
		Map <Integer , List<SubtituloVO>> subtitulosPorComponente = new HashMap<Integer, List<SubtituloVO>>();
		for(ComponentesVO componente : componentes){
			List<SubtituloVO> subtitulos = new ArrayList<SubtituloVO>();
			for(SubtituloVO subtituloVO : componente.getSubtitulos()){
				if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId()) || Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId()) || Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
					subtitulos.add(subtituloVO);
				}
			}
			subtitulosPorComponente.put(componente.getId(), subtitulos);
		}
		List<Establecimiento> estableclimientos = establecimientosDAO.getEstablecimientosByServicio(idServicio);
		for(Establecimiento establecimiento : estableclimientos){
			for(ComponentesVO componente : componentes){
				for(SubtituloVO subtitulo : subtitulosPorComponente.get(componente.getId())){
					List<ConvenioServicio> conveniosServicios = conveniosDAO.getConveniosServicioByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getId(), establecimiento.getId(), EstadosConvenios.PAGADO.getId());
					Integer montoConvenio = 0;
					if(conveniosServicios != null && conveniosServicios.size() > 0){
						ConvenioServicio convenioServicio = conveniosServicios.get(0);
						int size = convenioServicio.getConvenioServicioComponentes().size();
						ConvenioServicioComponente convenioServicioComponente = convenioServicio.getConvenioServicioComponentes().get(size - 1);
						montoConvenio = convenioServicioComponente.getMontoIngresado();
					}
					ReliquidacionServicio reliquidacionServicio = reliquidacionDAO.getReliquidacionServicioByProgramaAnoEstablecimientoComponenteSubtituloReliquidacion(idProgramaAno, establecimiento.getId(), componente.getId(), subtitulo.getId(), idReliquidacion);
					if(reliquidacionServicio != null){
						ReliquidacionServicioComponente reliquidacionServicioComponente =  reliquidacionServicio.getReliquidacionServicioComponentes().iterator().next();
						CumplimientoPrograma cumplimientoRebaja = getCumplimientoPrograma(programaVO.getId(), reliquidacionServicioComponente.getPorcentajeCumplimiento());
						List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
						System.out.println("cuotasPagadas.size()="+((cuotasPagadas!=null) ? cuotasPagadas.size(): "0"));
						double porcentajeAcumulado = 0.0;
						if(cuotasPagadas != null && cuotasPagadas.size() > 0){
							int size = cuotasPagadas.size();
							int cuotaNro = 1;
							for(Cuota cuota : cuotasPagadas){
								if(cuotaNro == size){
									break;
								}
								porcentajeAcumulado+=cuota.getPorcentaje();
								cuotaNro++;
							}
							porcentajeAcumulado = porcentajeAcumulado/100.0;
						} 
						if(cumplimientoRebaja != null && cumplimientoRebaja.getRebaja() != null){
							reliquidacionServicioComponente.setCumplimiento(cumplimientoRebaja);
							System.out.println("calcularReliquidacionServicio convenioServicioComponente.getMonto()="+montoConvenio+" idServicio="+idServicio+" idReliquidacion="+idReliquidacion);
							Integer rebajaUltimaCouta = (int) (montoConvenio * (1 - porcentajeAcumulado) * (cumplimientoRebaja.getRebaja()/100.0));
							System.out.println("calcularReliquidacionServicio convenioServicioComponente.getMonto()="+montoConvenio+" rebajaUltimaCouta="+rebajaUltimaCouta);
							reliquidacionServicioComponente.setMontoRebaja(rebajaUltimaCouta);
						}
					}
				}
			}
		}
	}

	public void calcularReliquidacionMixto(Integer idProgramaAno, Integer idServicio, Integer idReliquidacion){
		calcularReliquidacionComuna(idProgramaAno, idServicio, idReliquidacion);
		calcularReliquidacionServicio(idProgramaAno, idServicio, idReliquidacion);
	}

	private List<ValorizarReliquidacionSummaryVO> calcularReliquidacionProgramaServicio(Integer idProgramaAno, List<Integer> idComponentes, Map <Integer , List<SubtituloVO>> subtitulosPorComponente, Integer idReliquidacion) {
		System.out.println("idReliquidacion ----> "+idReliquidacion);
		System.out.println("idProgramaAno ---> "+idProgramaAno);
		List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		List<ValorizarReliquidacionSummaryVO> lista = new ArrayList<ValorizarReliquidacionSummaryVO>();
		for(ServiciosVO serv: servicios){
			if(serv.getEstableclimientos() != null && serv.getEstableclimientos().size() > 0){
				for(EstablecimientoSummaryVO establecimiento : serv.getEstableclimientos()){
					ValorizarReliquidacionSummaryVO valorizarReliquidacionSummaryVO = new ValorizarReliquidacionSummaryVO();
					valorizarReliquidacionSummaryVO.setIdServicio(serv.getId_servicio());
					valorizarReliquidacionSummaryVO.setServicio(serv.getNombre_servicio());
					valorizarReliquidacionSummaryVO.setIdComuna(establecimiento.getId());
					valorizarReliquidacionSummaryVO.setComuna(establecimiento.getNombre());
					Long marcoInicialEstablecimiento = 0L;
					Long montoReliquidacionServicio = 0L;
					List<ComponenteReliquidacionVO> componentesReliquidacion = new ArrayList<ComponenteReliquidacionVO>();
					/*for(Integer idComponente : idComponentes){
						for(SubtituloVO subtitulo : subtitulosPorComponente.get(idComponente)){
							ComponenteReliquidacionVO componenteReliquidacionVO = new ComponenteReliquidacionVO();
							componenteReliquidacionVO.setIdComponente(idComponente);
							componenteReliquidacionVO.setIdSubtitulo(subtitulo.getId());
							List<ConvenioServicio> conveniosServicios = conveniosDAO.getConveniosServicioByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, idComponente, subtitulo.getId(), establecimiento.getId(), EstadosConvenios.PAGADO.getId());
							Integer montoConvenio = 0;
							if(conveniosServicios != null && conveniosServicios.size() > 0){
								ConvenioServicio convenioServicio = conveniosServicios.get(0);
								componenteReliquidacionVO.setNumeroResolucion(convenioServicio.getNumeroResolucion());
								int size = convenioServicio.getConvenioServicioComponentes().size();
								ConvenioServicioComponente convenioServicioComponente = convenioServicio.getConvenioServicioComponentes().get(size - 1);
								marcoFinalEstablecimiento += convenioServicioComponente.getMontoIngresado();
								montoConvenio = convenioServicioComponente.getMontoIngresado();
							}

							ReliquidacionServicio reliquidacionServicio = reliquidacionDAO.getReliquidacionServicioByProgramaAnoEstablecimientoComponenteSubtituloReliquidacion(idProgramaAno, establecimiento.getId(), idComponente, subtitulo.getId(), idReliquidacion);
							if(reliquidacionServicio != null){
								ReliquidacionServicioComponente reliquidacionServicioComponente =  reliquidacionServicio.getReliquidacionServicioComponentes().iterator().next();
								Cuota ultimaCuota = null;
								if(cuotasPagadas != null && cuotasPagadas.size() > 0){
									List<CuotaSummaryVO> cuotasSummaryVO = new ArrayList<CuotaSummaryVO>();
									int size = cuotasPagadas.size();
									int posicion = 1;
									for(Cuota cuota : cuotasPagadas){
										CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
										Integer monto = (int)(montoConvenio * (cuota.getPorcentaje() / 100.0));
										cuotaSummaryVO.setPorcentaje(cuota.getPorcentaje());
										cuotaSummaryVO.setMonto(monto);
										cuotasSummaryVO.add(cuotaSummaryVO);
										if(posicion == size){
											ultimaCuota = cuota;
										}
										posicion++;
									}
									componenteReliquidacionVO.setCuotasSummaryVO(cuotasSummaryVO);
								}
								componenteReliquidacionVO.setPorcentajeCumplimiento(reliquidacionServicioComponente.getPorcentajeCumplimiento());
								componenteReliquidacionVO.setPorcentajeReliquidacion(reliquidacionServicioComponente.getCumplimiento().getRebaja());
								componenteReliquidacionVO.setRebajaUltimaCuota(reliquidacionServicioComponente.getMontoRebaja().longValue());
								Integer montoUltimaCuota = 0;
								if(ultimaCuota != null){
									montoUltimaCuota = (int)((montoConvenio * (ultimaCuota.getPorcentaje()/100.0)) - reliquidacionServicioComponente.getMontoRebaja());
								}
								componenteReliquidacionVO.setMontoUltimaCuota(montoUltimaCuota.longValue());
								componentesReliquidacion.add(componenteReliquidacionVO);
							}
						}
					}*/
					for(Integer idComponente : idComponentes){
						for(SubtituloVO subtitulo : subtitulosPorComponente.get(idComponente)){
							ComponenteReliquidacionVO componenteReliquidacionVO = new ComponenteReliquidacionVO();
							componenteReliquidacionVO.setIdComponente(idComponente);
							componenteReliquidacionVO.setIdSubtitulo(subtitulo.getId());
							Long marcoEstablecimientoComponenteSubtitulo = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, idComponente, subtitulo.getId());
							marcoInicialEstablecimiento += marcoEstablecimientoComponenteSubtitulo;
							componenteReliquidacionVO.setMarcoInicial(marcoEstablecimientoComponenteSubtitulo);
							componenteReliquidacionVO.setIdSubtitulo(subtitulo.getId());

							//ReliquidacionServicio reliquidacionServicio = reliquidacionDAO.getReliquidacionServicioByProgramaAnoEstablecimientoComponenteSubtituloReliquidacion(idProgramaAno, establecimiento.getId(), idComponente, subtitulo.getId(), idReliquidacion);
							ReliquidacionServicioComponente reliquidacionServicioComponente = reliquidacionDAO.getReliquidacionServicioComponenteByProgramaAnoEstablecimientoComponenteSubtituloReliquidacion(idProgramaAno, establecimiento.getId(), idComponente, subtitulo.getId(), idReliquidacion);
							if(reliquidacionServicioComponente != null){
								//ReliquidacionServicioComponente reliquidacionServicioComponente =  reliquidacionServicio.getReliquidacionServicioComponentes().iterator().next();
								Cuota ultimaCuota = null;
								if(cuotasPagadas != null && cuotasPagadas.size() > 0){
									List<CuotaSummaryVO> cuotasSummaryVO = new ArrayList<CuotaSummaryVO>();
									int size = cuotasPagadas.size();
									int posicion = 1;
									for(Cuota cuota : cuotasPagadas){
										CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
										Integer monto = (int)(marcoEstablecimientoComponenteSubtitulo * (cuota.getPorcentaje() / 100.0));
										cuotaSummaryVO.setPorcentaje(cuota.getPorcentaje());
										cuotaSummaryVO.setMonto(monto);
										cuotasSummaryVO.add(cuotaSummaryVO);
										if(posicion == size){
											ultimaCuota = cuota;
										}
										posicion++;
									}
									componenteReliquidacionVO.setCuotasSummaryVO(cuotasSummaryVO);
								}
								componenteReliquidacionVO.setPorcentajeCumplimiento(reliquidacionServicioComponente.getPorcentajeCumplimiento());
								componenteReliquidacionVO.setPorcentajeReliquidacion(reliquidacionServicioComponente.getCumplimiento().getRebaja());
								componenteReliquidacionVO.setRebajaUltimaCuota(reliquidacionServicioComponente.getMontoRebaja().longValue());
								Integer montoUltimaCuota = 0;
								if(ultimaCuota != null){
									montoUltimaCuota = (int)((marcoEstablecimientoComponenteSubtitulo * (ultimaCuota.getPorcentaje()/100.0)) - reliquidacionServicioComponente.getMontoRebaja());
								}
								componenteReliquidacionVO.setMontoUltimaCuota(montoUltimaCuota.longValue());
								componentesReliquidacion.add(componenteReliquidacionVO);
								montoReliquidacionServicio += reliquidacionServicioComponente.getMontoRebaja().longValue();
							}
						}
					}
					if(componentesReliquidacion.size() == 0){
						continue;
					}
					Long marcoFinalEstablecimiento = marcoInicialEstablecimiento - montoReliquidacionServicio ;
					System.out.println("marcoFinalEstablecimiento="+marcoFinalEstablecimiento+" marcoInicialEstablecimiento="+marcoInicialEstablecimiento+" montoReliquidacionComuna="+montoReliquidacionServicio);
					valorizarReliquidacionSummaryVO.setMarcoFinal(marcoFinalEstablecimiento);
					valorizarReliquidacionSummaryVO.setComponentesReliquidacion(componentesReliquidacion);
					lista.add(valorizarReliquidacionSummaryVO);
				}
			}
		}
		return lista;
	}

	private List<ValorizarReliquidacionSummaryVO> calcularReliquidacionProgramaComuna(Integer idProgramaAno, List<Integer> idComponentes, Integer idReliquidacion) {
		System.out.println("idReliquidacion ----> "+idReliquidacion);
		System.out.println("idProgramaAno ---> "+idProgramaAno);
		List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		List<ValorizarReliquidacionSummaryVO> lista = new ArrayList<ValorizarReliquidacionSummaryVO>();
		for(ServiciosVO serv: servicios){
			if(serv.getComunas() != null && serv.getComunas().size() > 0){
				for(ComunaSummaryVO comuna : serv.getComunas()){
					ValorizarReliquidacionSummaryVO valorizarReliquidacionSummaryVO = new ValorizarReliquidacionSummaryVO();
					valorizarReliquidacionSummaryVO.setIdServicio(serv.getId_servicio());
					valorizarReliquidacionSummaryVO.setServicio(serv.getNombre_servicio());
					valorizarReliquidacionSummaryVO.setIdComuna(comuna.getId());
					valorizarReliquidacionSummaryVO.setComuna(comuna.getNombre());
					Long marcoInicialComuna = 0L;
					Long montoReliquidacionComuna = 0L;
					List<ComponenteReliquidacionVO> componentesReliquidacion = new ArrayList<ComponenteReliquidacionVO>();
					/*for(Integer idComponente : idComponentes){
						ComponenteReliquidacionVO componenteReliquidacionVO = new ComponenteReliquidacionVO();
						componenteReliquidacionVO.setIdComponente(idComponente);
						Long marcoComunaComponenteSubtitulo = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, idComponente, Subtitulo.SUBTITULO24.getId());
						componenteReliquidacionVO.setMarcoInicial(marcoComunaComponenteSubtitulo);
						componenteReliquidacionVO.setIdSubtitulo(Subtitulo.SUBTITULO24.getId());
						List<ConvenioComuna> conveniosComuna = conveniosDAO.getConveniosComunaByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, idComponente, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.PAGADO.getId());
						Integer montoConvenioComponenteSubtitulo = 0;
						if(conveniosComuna != null && conveniosComuna.size() > 0){
							ConvenioComuna convenioComuna = conveniosComuna.get(0);
							componenteReliquidacionVO.setNumeroResolucion(convenioComuna.getNumeroResolucion());
							int size = convenioComuna.getConvenioComunaComponentes().size();
							ConvenioComunaComponente convenioComunaComponente = convenioComuna.getConvenioComunaComponentes().get(size - 1);
							marcoInicialComuna += convenioComunaComponente.getMontoIngresado();
							montoConvenioComponenteSubtitulo = convenioComunaComponente.getMontoIngresado();
						}
						ReliquidacionComuna reliquidacionComuna = reliquidacionDAO.getReliquidacionComunaByProgramaAnoComunaComponenteSubtituloReliquidacion(idProgramaAno, comuna.getId(), idComponente, Subtitulo.SUBTITULO24.getId(), idReliquidacion);
						if(reliquidacionComuna != null){
							ReliquidacionComunaComponente reliquidacionComunaComponente =  reliquidacionComuna.getReliquidacionComunaComponentes().iterator().next();
							Cuota ultimaCuota = null;
							if(cuotasPagadas != null && cuotasPagadas.size() > 0){
								List<CuotaSummaryVO> cuotasSummaryVO = new ArrayList<CuotaSummaryVO>();
								int size = cuotasPagadas.size();
								int posicion = 1;
								for(Cuota cuota : cuotasPagadas){
									CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
									Integer monto = (int)(montoConvenioComponenteSubtitulo * (cuota.getPorcentaje() / 100.0));
									cuotaSummaryVO.setPorcentaje(cuota.getPorcentaje());
									cuotaSummaryVO.setMonto(monto);
									cuotasSummaryVO.add(cuotaSummaryVO);
									if(posicion == size){
										ultimaCuota = cuota;
									}
									posicion++;
								}
								componenteReliquidacionVO.setCuotasSummaryVO(cuotasSummaryVO);
							}
							componenteReliquidacionVO.setPorcentajeCumplimiento(reliquidacionComunaComponente.getPorcentajeCumplimiento());
							componenteReliquidacionVO.setPorcentajeReliquidacion(reliquidacionComunaComponente.getCumplimiento().getRebaja());
							componenteReliquidacionVO.setRebajaUltimaCuota(reliquidacionComunaComponente.getMontoRebaja().longValue());
							Integer montoUltimaCuota = 0;
							if(ultimaCuota != null){
								montoUltimaCuota = (int)((montoConvenioComponenteSubtitulo * (ultimaCuota.getPorcentaje()/100.0)) - reliquidacionComunaComponente.getMontoRebaja());
							}
							componenteReliquidacionVO.setMontoUltimaCuota(montoUltimaCuota.longValue());
							componentesReliquidacion.add(componenteReliquidacionVO);
							montoReliquidacionComuna += reliquidacionComunaComponente.getMontoRebaja().longValue();
						}
					}*/
					for(Integer idComponente : idComponentes){
						ComponenteReliquidacionVO componenteReliquidacionVO = new ComponenteReliquidacionVO();
						componenteReliquidacionVO.setIdComponente(idComponente);
						Long marcoComunaComponenteSubtitulo = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, idComponente, Subtitulo.SUBTITULO24.getId());
						marcoInicialComuna += marcoComunaComponenteSubtitulo;
						componenteReliquidacionVO.setMarcoInicial(marcoComunaComponenteSubtitulo);
						componenteReliquidacionVO.setIdSubtitulo(Subtitulo.SUBTITULO24.getId());

						//ReliquidacionComuna reliquidacionComuna = reliquidacionDAO.getReliquidacionComunaByProgramaAnoComunaComponenteSubtituloReliquidacion(idProgramaAno, comuna.getId(), idComponente, Subtitulo.SUBTITULO24.getId(), idReliquidacion);
						ReliquidacionComunaComponente reliquidacionComunaComponente = reliquidacionDAO.getReliquidacionComunaComponenteByProgramaAnoComunaComponenteSubtituloReliquidacion(idProgramaAno, comuna.getId(), idComponente, Subtitulo.SUBTITULO24.getId(), idReliquidacion);

						if(reliquidacionComunaComponente != null){
							Cuota ultimaCuota = null;
							if(cuotasPagadas != null && cuotasPagadas.size() > 0){
								List<CuotaSummaryVO> cuotasSummaryVO = new ArrayList<CuotaSummaryVO>();
								int size = cuotasPagadas.size();
								int posicion = 1;
								for(Cuota cuota : cuotasPagadas){
									CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
									Integer monto = (int)(marcoComunaComponenteSubtitulo * (cuota.getPorcentaje() / 100.0));
									cuotaSummaryVO.setPorcentaje(cuota.getPorcentaje());
									cuotaSummaryVO.setMonto(monto);
									cuotasSummaryVO.add(cuotaSummaryVO);
									if(posicion == size){
										ultimaCuota = cuota;
									}
									posicion++;
								}
								componenteReliquidacionVO.setCuotasSummaryVO(cuotasSummaryVO);
							}
							componenteReliquidacionVO.setPorcentajeCumplimiento(reliquidacionComunaComponente.getPorcentajeCumplimiento());
							componenteReliquidacionVO.setPorcentajeReliquidacion(reliquidacionComunaComponente.getCumplimiento().getRebaja());
							componenteReliquidacionVO.setRebajaUltimaCuota(reliquidacionComunaComponente.getMontoRebaja().longValue());
							Integer montoUltimaCuota = 0;
							if(ultimaCuota != null){
								montoUltimaCuota = (int)((marcoComunaComponenteSubtitulo * (ultimaCuota.getPorcentaje()/100.0)) - reliquidacionComunaComponente.getMontoRebaja());
							}
							componenteReliquidacionVO.setMontoUltimaCuota(montoUltimaCuota.longValue());
							componentesReliquidacion.add(componenteReliquidacionVO);
							montoReliquidacionComuna += reliquidacionComunaComponente.getMontoRebaja().longValue();
						}
					}
					if(componentesReliquidacion.size() == 0){
						continue;
					}
					Long marcoFinalComuna = marcoInicialComuna - montoReliquidacionComuna ;
					System.out.println("marcoFinalComuna="+marcoFinalComuna+" marcoInicialComuna="+marcoInicialComuna+" montoReliquidacionComuna="+montoReliquidacionComuna);
					valorizarReliquidacionSummaryVO.setMarcoFinal(marcoFinalComuna);
					valorizarReliquidacionSummaryVO.setComponentesReliquidacion(componentesReliquidacion);
					lista.add(valorizarReliquidacionSummaryVO);
				}
			}
		}
		return lista;
	}

	public void procesarCalculoReliquidacionMunicipal(Integer idProgramaAno, Integer idReliquidacion, XSSFWorkbook workbook) throws ExcelFormatException{
		System.out.println("idProgramaAno="+idProgramaAno+" idReliquidacion="+idReliquidacion); 
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		int celdas = 0;
		List<String> nombresComponentes = new ArrayList<String>();
		while(rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum()==1 ){
				Iterator<Cell> cellIterator = row.cellIterator();
				int pos_celda = 0;
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if(pos_celda > 3){
						nombresComponentes.add(cell.getStringCellValue().toString());
					}
					pos_celda++;
				}
			}
			if (row.getRowNum() == 4){
				Iterator<Cell> cellIterator = row.cellIterator();

				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK){
						break;
					}
					celdas++;

				}
			}else if(row.getRowNum() > 4){
				break;
			}
		}
		List<CellTypeExcelVO> cells = new ArrayList<CellTypeExcelVO>();
		CellTypeExcelVO fieldOne = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldOne);
		CellTypeExcelVO fieldTwo = new CellTypeExcelVO(true, FieldType.STRINGFIELD);
		cells.add(fieldTwo);
		CellTypeExcelVO fieldThree = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldThree);
		CellTypeExcelVO fieldFour = new CellTypeExcelVO(true, FieldType.STRINGFIELD);
		cells.add(fieldFour);


		for(int i = 4; i < celdas; i++){
			CellTypeExcelVO porc_cumplimiento = new CellTypeExcelVO(true, FieldType.PERCENTAGEFIELD);
			cells.add(porc_cumplimiento);
		}

		XSSFSheet worksheet = workbook.getSheetAt(0);
		ReliquidacionCalculoExcelValidator reliquidacionCalculoExcelValidator = new ReliquidacionCalculoExcelValidator(cells.size(), cells, true, 0, 3);
		reliquidacionCalculoExcelValidator.validateFormat(worksheet);
		List<CalculoReliquidacionBaseVO> items = reliquidacionCalculoExcelValidator.getItems();
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		Reliquidacion reliquidacion = reliquidacionDAO.getReliquidacionById(idReliquidacion);
		if(reliquidacion.getIdProgramaAno() == null){
			reliquidacion.setIdProgramaAno(programaAno);
		}
		List<ReliquidacionComuna> reliquidacionesComuna = reliquidacionDAO.findByIdProgramaAno(idProgramaAno);
		if(reliquidacionesComuna != null && reliquidacionesComuna.size() > 0){
			for(ReliquidacionComuna reliquidacionComuna : reliquidacionesComuna){
				reliquidacionDAO.deleteByIdReliquidacionComuna(reliquidacionComuna.getReliquidacionComunaId());
			}
		}
		//reliquidacionDAO.deleteReliquidacionComuna(idProgramaAno);

		for(CalculoReliquidacionBaseVO calculoReliquidacionBaseVO : items){
			ServicioSalud servicio = servicioSaludService.getServicioSaludPorID(calculoReliquidacionBaseVO.getId_servicio());
			Comuna comuna = comunaDAO.getComunaById(calculoReliquidacionBaseVO.getId_comuna().intValue());
			int contadorComponente = 0;
			ReliquidacionComuna reliquidacionComuna = new ReliquidacionComuna();
			System.out.println("servicio="+servicio);
			reliquidacionComuna.setServicio(servicio);
			System.out.println("comuna="+comuna);
			reliquidacionComuna.setComuna(comuna);
			System.out.println("reliquidacion="+reliquidacion);
			reliquidacionComuna.setReliquidacion(reliquidacion);
			System.out.println("programaAno.getPrograma()="+programaAno.getPrograma());
			reliquidacionComuna.setPrograma(programaAno);
			reliquidacionDAO.save(reliquidacionComuna);	
			for(ComponenteCumplimientoVO componenteCumplimientoVO : calculoReliquidacionBaseVO.getComponentesCumplimientoVO()){
				Componente componente = componenteService.getComponenteByNombre(nombresComponentes.get(contadorComponente++));
				System.out.println("componente="+componente);
				if(componente == null){
					continue;
				}
				System.out.println("componenteCumplimientoVO "+componenteCumplimientoVO.getPorcentajeCumplimiento()+" componente --> "+componente.getNombre());
				ReliquidacionComunaComponente reliquidacionComunaComponente = new ReliquidacionComunaComponente();
				reliquidacionComunaComponente.setComponente(componente);
				reliquidacionComunaComponente.setPorcentajeCumplimiento(componenteCumplimientoVO.getPorcentajeCumplimiento());
				CumplimientoPrograma cumplimientoPrograma = getCumplimientoPrograma(programaAno, componenteCumplimientoVO);
				reliquidacionComunaComponente.setCumplimiento(cumplimientoPrograma);
				reliquidacionComunaComponente.setSubtitulo(tipoSubtituloDAO.getTipoSubtituloById(Subtitulo.SUBTITULO24.getId()));
				reliquidacionComunaComponente.setReliquidacionComuna(reliquidacionComuna);
				//Buscar MP para la comuna
				Long marcoComuna = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), programaAno.getIdProgramaAno(), componente.getId(), Subtitulo.SUBTITULO24.getId());
				List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
				if(cuotasPagadas != null && cuotasPagadas.size() > 0){
					Double porcentajePrimerasCuota = 0.0;
					Double porcentajeUltimaCuota = 0.0;
					int size = cuotasPagadas.size();
					for(int cont = 0; cont < size; cont++){
						if(cont != (size - 1)){
							porcentajePrimerasCuota+= cuotasPagadas.get(cont).getPorcentaje();
						}else{
							porcentajeUltimaCuota = cuotasPagadas.get(cont).getPorcentaje() + 0.0;
						}
					}
					/*porcentajePrimerasCuota = porcentajePrimerasCuota/100.0;
					Double cumplimiento = componenteCumplimientoVO.getPorcentajeCumplimiento()/100.0;
					Double montoReliquidacion = marcoComuna * (1 - porcentajePrimerasCuota) * cumplimiento;
					reliquidacionComunaComponente.setMontoRebaja((int)Math.round(montoReliquidacion));*/
					porcentajeUltimaCuota = porcentajeUltimaCuota/100.0;
					Double montoReliquidacion = marcoComuna * porcentajeUltimaCuota * (cumplimientoPrograma.getRebaja() / 100.0);
					System.out.println("marcoComuna="+marcoComuna+" porcentajeUltimaCuota="+porcentajeUltimaCuota+" (cumplimientoPrograma.getRebaja() / 100.0="+((cumplimientoPrograma.getRebaja() / 100.0)));
					System.out.println("montoReliquidacion="+montoReliquidacion);
					reliquidacionComunaComponente.setMontoRebaja((int)Math.round(montoReliquidacion));
					reliquidacionDAO.save(reliquidacionComunaComponente);	
				}

			}
		}
	}

	public void procesarCalculoReliquidacionServicio(Integer idProgramaAno, Integer idReliquidacion, XSSFWorkbook workbook) throws ExcelFormatException{
		System.out.println("idProgramaAno="+idProgramaAno+" idReliquidacion="+idReliquidacion); 
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		int celdas = 0;
		List<String> nombresComponentes = new ArrayList<String>();
		Map<String, Integer> numSubtitulosPorComponente = new HashMap<String, Integer>();
		List<String> nombresSubtitulos = new ArrayList<String>();
		while(rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum()==1 ){
				Iterator<Cell> cellIterator = row.cellIterator();
				int pos_celda = 0;
				String componente = null;
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if(pos_celda > 3){
						System.out.println("Procesando el excel Nombre componente -->"+cell.getStringCellValue().toString());
						String componenteTmp = cell.getStringCellValue().toString();
						if(componenteTmp != null && !componenteTmp.trim().isEmpty()){
							componente = componenteTmp;
							nombresComponentes.add(componente);
							numSubtitulosPorComponente.put(componente, 1);
						}else{
							Integer contSubtitulos = numSubtitulosPorComponente.get(componente);
							contSubtitulos++;
							numSubtitulosPorComponente.put(componente, contSubtitulos);
						}
					}
					pos_celda++;
				}
			}
			if (row.getRowNum() == 2){
				Iterator<Cell> cellIterator = row.cellIterator();
				int pos_celda = 0;
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if(pos_celda > 3){
						nombresSubtitulos.add(cell.getStringCellValue().toString());
					}
					pos_celda++;
				}
			}
			if (row.getRowNum() == 4){
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK){
						break;
					}
					celdas++;
				}
			}else if(row.getRowNum() > 3){
				break;
			}
		}
		List<CellTypeExcelVO> cells = new ArrayList<CellTypeExcelVO>();
		CellTypeExcelVO fieldOne = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldOne);
		CellTypeExcelVO fieldTwo = new CellTypeExcelVO(true, FieldType.STRINGFIELD);
		cells.add(fieldTwo);
		CellTypeExcelVO fieldThree = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldThree);
		CellTypeExcelVO fieldFour = new CellTypeExcelVO(true, FieldType.STRINGFIELD);
		cells.add(fieldFour);

		for(int i = 4; i < celdas; i++){
			CellTypeExcelVO porc_cumplimiento = new CellTypeExcelVO(true, FieldType.PERCENTAGEFIELD);
			cells.add(porc_cumplimiento);
		}

		XSSFSheet worksheet = workbook.getSheetAt(0);
		ReliquidacionCalculoExcelValidator reliquidacionCalculoExcelValidator = new ReliquidacionCalculoExcelValidator(cells.size(), cells, true, 0, 3);
		reliquidacionCalculoExcelValidator.validateFormat(worksheet);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		Reliquidacion reliquidacion = reliquidacionDAO.getReliquidacionById(idReliquidacion);
		if(reliquidacion.getIdProgramaAno() == null){
			reliquidacion.setIdProgramaAno(programaAno);
		}
		List<ReliquidacionServicio> reliquidacionesServicio = reliquidacionDAO.findByReliquidacionServicioIdProgramaAno(idProgramaAno);
		if(reliquidacionesServicio != null && reliquidacionesServicio.size() > 0){
			for(ReliquidacionServicio reliquidacionServicio : reliquidacionesServicio){
				reliquidacionDAO.deleteByIdReliquidacionServicio(reliquidacionServicio.getReliquidacionServicioId());
			}
		}
		//reliquidacionDAO.deleteReliquidacionServicio(idProgramaAno);
		List<CalculoReliquidacionBaseVO> items = reliquidacionCalculoExcelValidator.getItems();
		for(CalculoReliquidacionBaseVO calculoReliquidacionBaseVO : items){
			ServicioSalud servicio = servicioSaludService.getServicioSaludPorID(calculoReliquidacionBaseVO.getId_servicio());
			Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(calculoReliquidacionBaseVO.getId_comuna().intValue());
			ReliquidacionServicio reliquidacionServicio = new ReliquidacionServicio();
			System.out.println("servicio="+servicio);
			reliquidacionServicio.setServicio(servicio);
			System.out.println("establecimiento="+establecimiento);
			reliquidacionServicio.setEstablecimiento(establecimiento);
			System.out.println("reliquidacion="+reliquidacion);
			reliquidacionServicio.setReliquidacion(reliquidacion);
			System.out.println("programaAno.getPrograma()="+programaAno.getPrograma());
			reliquidacionServicio.setPrograma(programaAno);
			reliquidacionDAO.save(reliquidacionServicio);
			int posicionCumplimiento = 0;
			for(int contComponente = 0 ; contComponente < nombresComponentes.size(); contComponente++){
				System.out.println("nombresComponentes.get(contComponente)="+nombresComponentes.get(contComponente));
				Componente componente = componenteService.getComponenteByNombre(nombresComponentes.get(contComponente));
				if(componente == null){
					continue;
				}
				int numSubtitulos = numSubtitulosPorComponente.get(nombresComponentes.get(contComponente));
				for(int contS = 0; contS < numSubtitulos; contS++){
					TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloByName(nombresSubtitulos.get(posicionCumplimiento));
					ComponenteCumplimientoVO componenteCumplimientoVO = calculoReliquidacionBaseVO.getComponentesCumplimientoVO().get(posicionCumplimiento);
					ReliquidacionServicioComponente reliquidacionServicioComponente = new ReliquidacionServicioComponente();
					reliquidacionServicioComponente.setComponente(componente);
					reliquidacionServicioComponente.setPorcentajeCumplimiento(componenteCumplimientoVO.getPorcentajeCumplimiento());
					CumplimientoPrograma cumplimientoPrograma = getCumplimientoPrograma(programaAno, componenteCumplimientoVO);
					reliquidacionServicioComponente.setCumplimiento(cumplimientoPrograma);
					reliquidacionServicioComponente.setSubtitulo(subtitulo);
					reliquidacionServicioComponente.setReliquidacionServicio(reliquidacionServicio);
					//Buscar MP para el establecimiento
					System.out.println("ir a consultar marco del establecimiento");
					System.out.println("establecimiento.getId()="+establecimiento.getId());
					System.out.println("programaAno.getIdProgramaAno()="+programaAno.getIdProgramaAno());
					System.out.println("componente.getId()="+componente.getId());
					System.out.println("subtitulo.getIdTipoSubtitulo()="+subtitulo.getIdTipoSubtitulo());
					Long marcoEstablecimiento = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), programaAno.getIdProgramaAno(), componente.getId(), subtitulo.getIdTipoSubtitulo());
					List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
					if(cuotasPagadas != null && cuotasPagadas.size() > 0){
						Double porcentajePrimerasCuota = 0.0;
						Double porcentajeUltimaCuota = 0.0;
						int size = cuotasPagadas.size();
						for(int cont = 0; cont < size; cont++){
							if(cont != (size - 1)){
								porcentajePrimerasCuota += cuotasPagadas.get(cont).getPorcentaje();
								System.out.println("cuota n°="+cuotasPagadas.get(cont).getNumeroCuota()+" porcentaje="+cuotasPagadas.get(cont).getPorcentaje());
							}else{
								porcentajeUltimaCuota = cuotasPagadas.get(cont).getPorcentaje() + 0.0;
								System.out.println("cuota n°="+cuotasPagadas.get(cont).getNumeroCuota()+" porcentaje="+cuotasPagadas.get(cont).getPorcentaje());
							}
						}
						porcentajeUltimaCuota = porcentajeUltimaCuota / 100.0;
						System.out.println("marcoEstablecimiento="+marcoEstablecimiento);
						System.out.println("porcentajeUltimaCuota="+porcentajeUltimaCuota);
						System.out.println("(cumplimientoPrograma.getRebaja() / 100.0)="+(cumplimientoPrograma.getRebaja() / 100.0));
						Double montoReliquidacion = marcoEstablecimiento * porcentajeUltimaCuota * (cumplimientoPrograma.getRebaja() / 100.0);
						reliquidacionServicioComponente.setMontoRebaja((int)Math.round(montoReliquidacion));
						reliquidacionDAO.save(reliquidacionServicioComponente);	
					}
					posicionCumplimiento++;
				}
			}
		}
	}

	private CumplimientoPrograma getCumplimientoPrograma(ProgramaAno programaAno, ComponenteCumplimientoVO componenteCumplimientoVO){
		List<CumplimientoPrograma> cumplimientosPrograma = reliquidacionDAO.getCumplimientoProgramaByPrograma(programaAno.getPrograma().getId());
		CumplimientoPrograma cumplimientoProgramaSeleccionado = null;
		if(cumplimientosPrograma != null && cumplimientosPrograma.size()>0){	
			for(CumplimientoPrograma cumplimientoPrograma : cumplimientosPrograma){
				if(( greaterThan(componenteCumplimientoVO.getPorcentajeCumplimiento(), cumplimientoPrograma.getPorcentajeDesde().doubleValue()) || equals(componenteCumplimientoVO.getPorcentajeCumplimiento(), cumplimientoPrograma.getPorcentajeDesde().doubleValue()) ) 
						&& (lessThan(componenteCumplimientoVO.getPorcentajeCumplimiento(), cumplimientoPrograma.getPorcentajeHasta().doubleValue()) || equals(componenteCumplimientoVO.getPorcentajeCumplimiento(), cumplimientoPrograma.getPorcentajeHasta().doubleValue())) ){
					cumplimientoProgramaSeleccionado = cumplimientoPrograma;
					System.out.println("cumplimientoProgramaSeleccionado ---> "+cumplimientoProgramaSeleccionado);
					break;
				}
			}
		}
		return cumplimientoProgramaSeleccionado;	
	}

	private CumplimientoPrograma getCumplimientoPrograma(Integer idPrograma, double cumplimiento){
		List<CumplimientoPrograma> cumplimientosPrograma = reliquidacionDAO.getCumplimientoProgramaByPrograma(idPrograma);
		CumplimientoPrograma rebaja = null;
		if(cumplimientosPrograma != null && cumplimientosPrograma.size()>0){	
			for(CumplimientoPrograma cumplimientoPrograma : cumplimientosPrograma){
				if(( greaterThan(cumplimiento, cumplimientoPrograma.getPorcentajeDesde().doubleValue()) || equals(cumplimiento, cumplimientoPrograma.getPorcentajeDesde().doubleValue()) ) 
						&& (lessThan(cumplimiento, cumplimientoPrograma.getPorcentajeHasta().doubleValue()) || equals(cumplimiento, cumplimientoPrograma.getPorcentajeHasta().doubleValue())) ){
					rebaja = cumplimientoPrograma;
					break;
				}
			}
		}
		return rebaja;	
	}

	private boolean equals(double a, double b){
		return equals(a, b, EPSILON);
	}

	private boolean equals(double a, double b, double epsilon){
		return a == b ? true : Math.abs(a - b) < epsilon;
	}

	private boolean greaterThan(double a, double b){
		return greaterThan(a, b, EPSILON);
	}

	private boolean greaterThan(double a, double b, double epsilon){
		return a - b > epsilon;
	}

	private boolean lessThan(double a, double b){
		return lessThan(a, b, EPSILON);
	}

	private boolean lessThan(double a, double b, double epsilon){
		return b - a > epsilon;
	}

	public List<ValorizarReliquidacionPageSummaryVO> getReliquidacionSummaryVO(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idServicio, Integer idReliquidacion) {
		System.out.println("idReliquidacion ----> "+idReliquidacion);
		System.out.println("idProgramaAno ---> "+idProgramaAno);
		System.out.println("idServicio ---> "+idServicio);
		System.out.println("idComponente ---> "+idComponente);
		System.out.println("idSubtitulo ---> "+idSubtitulo);
		List<ServiciosVO> servicios = null;
		if(idServicio == null){
			servicios = servicioSaludService.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServiciosVO>();
			ServiciosVO serviciosVO = servicioSaludService.getServicioSaludById(idServicio);
			servicios.add(serviciosVO);
		}
		List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		List<ValorizarReliquidacionPageSummaryVO> lista = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
		for(ServiciosVO serv : servicios){
			if(serv.getEstableclimientos() != null && serv.getEstableclimientos().size() > 0){
				for(EstablecimientoSummaryVO establecimiento : serv.getEstableclimientos()){
					ValorizarReliquidacionPageSummaryVO valorizarReliquidacionSummaryVO = new ValorizarReliquidacionPageSummaryVO();
					valorizarReliquidacionSummaryVO.setIdServicio(serv.getId_servicio());
					valorizarReliquidacionSummaryVO.setServicio(serv.getNombre_servicio());
					valorizarReliquidacionSummaryVO.setIdComuna(establecimiento.getId());
					valorizarReliquidacionSummaryVO.setComuna(establecimiento.getNombre());
	
					/*Long marcoFinalEstablecimiento = 0L;
					ComponenteReliquidacionPageVO componenteReliquidacionPageVO = new ComponenteReliquidacionPageVO();
					componenteReliquidacionPageVO.setIdComponente(idComponente);
					List<ConvenioServicio> conveniosServicios = conveniosDAO.getConveniosServicioByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, idComponente, idSubtitulo, establecimiento.getId(), EstadosConvenios.PAGADO.getId());
					Integer montoConvenio = 0;
					if(conveniosServicios != null && conveniosServicios.size() > 0){
						ConvenioServicio convenioServicio = conveniosServicios.get(0);
						componenteReliquidacionPageVO.setNumeroResolucion(convenioServicio.getNumeroResolucion());
						int size = convenioServicio.getConvenioServicioComponentes().size();
						ConvenioServicioComponente convenioServicioComponente = convenioServicio.getConvenioServicioComponentes().get(size - 1);
						marcoFinalEstablecimiento += convenioServicioComponente.getMontoIngresado();
						montoConvenio = convenioServicioComponente.getMontoIngresado();
					}
	
					ReliquidacionServicio reliquidacionServicio = reliquidacionDAO.getReliquidacionServicioByProgramaAnoEstablecimientoComponenteSubtituloReliquidacion(idProgramaAno, establecimiento.getId(), idComponente, idSubtitulo, idReliquidacion);
					if(reliquidacionServicio != null){
						ReliquidacionServicioComponente reliquidacionServicioComponente =  reliquidacionServicio.getReliquidacionServicioComponentes().iterator().next();
						Cuota ultimaCuota = null;
						if(cuotasPagadas != null && cuotasPagadas.size() > 0){
							int size = cuotasPagadas.size();
							int posicion = 1;
							for(Cuota cuota : cuotasPagadas){
								CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
								Integer monto = (int)(montoConvenio * (cuota.getPorcentaje() / 100.0));
								cuotaSummaryVO.setPorcentaje(cuota.getPorcentaje());
								cuotaSummaryVO.setMonto(monto);
								if(posicion == 1){
									componenteReliquidacionPageVO.setCuotasUno(cuotaSummaryVO);
								}
								if(posicion == size){
									componenteReliquidacionPageVO.setUltimaCuota(cuotaSummaryVO);
									ultimaCuota = cuota;
								}
								posicion++;
							}
						}
	
						componenteReliquidacionPageVO.setPorcentajeCumplimiento(reliquidacionServicioComponente.getPorcentajeCumplimiento());
						componenteReliquidacionPageVO.setPorcentajeReliquidacion(reliquidacionServicioComponente.getCumplimiento().getRebaja());
						componenteReliquidacionPageVO.setRebajaUltimaCuota(reliquidacionServicioComponente.getMontoRebaja().longValue());
						Integer montoUltimaCuota = 0;
						if(ultimaCuota != null){
							montoUltimaCuota = (int)((montoConvenio * (ultimaCuota.getPorcentaje()/100.0)) - reliquidacionServicioComponente.getMontoRebaja());
						}
						componenteReliquidacionPageVO.setMontoUltimaCuota(montoUltimaCuota.longValue());
						valorizarReliquidacionSummaryVO.setComponenteReliquidacionPageVO(componenteReliquidacionPageVO);
					}*/
					ComponenteReliquidacionPageVO componenteReliquidacionPageVO = new ComponenteReliquidacionPageVO();
					Long marcoInicialEstablecimiento = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, idComponente, idSubtitulo);
					componenteReliquidacionPageVO.setIdComponente(idComponente);
					componenteReliquidacionPageVO.setMarcoInicial(marcoInicialEstablecimiento);
					componenteReliquidacionPageVO.setIdComponente(idComponente);
					componenteReliquidacionPageVO.setNumeroResolucion(0);
					
					ReliquidacionServicioComponente reliquidacionServicioComponente =  reliquidacionDAO.getReliquidacionServicioComponenteByProgramaAnoEstablecimientoComponenteSubtituloReliquidacion(idProgramaAno, establecimiento.getId(), idComponente, idSubtitulo, idReliquidacion);
					if(reliquidacionServicioComponente != null){
						Cuota ultimaCuota = null;
						if(cuotasPagadas != null && cuotasPagadas.size() > 0){
							int size = cuotasPagadas.size();
							int posicion = 1;
							for(Cuota cuota : cuotasPagadas){
								CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
								Integer monto = (int)(marcoInicialEstablecimiento * (cuota.getPorcentaje() / 100.0));
								cuotaSummaryVO.setPorcentaje(cuota.getPorcentaje());
								cuotaSummaryVO.setMonto(monto);
								if(posicion == 1){
									componenteReliquidacionPageVO.setCuotasUno(cuotaSummaryVO);
								}
								if(posicion == size){
									componenteReliquidacionPageVO.setUltimaCuota(cuotaSummaryVO);
									ultimaCuota = cuota;
								}
								posicion++;
							}
						}
	
						componenteReliquidacionPageVO.setPorcentajeCumplimiento(reliquidacionServicioComponente.getPorcentajeCumplimiento() / 100.0);
						componenteReliquidacionPageVO.setPorcentajeReliquidacion(reliquidacionServicioComponente.getCumplimiento().getRebaja() / 100.0);
						componenteReliquidacionPageVO.setRebajaUltimaCuota(reliquidacionServicioComponente.getMontoRebaja().longValue());
						Integer montoUltimaCuota = 0;
						if(ultimaCuota != null){
							montoUltimaCuota = componenteReliquidacionPageVO.getUltimaCuota().getMonto() - ((reliquidacionServicioComponente.getMontoRebaja() == null) ? 0 : reliquidacionServicioComponente.getMontoRebaja());
						}
						componenteReliquidacionPageVO.setMontoUltimaCuota(montoUltimaCuota.longValue());
						valorizarReliquidacionSummaryVO.setComponenteReliquidacionPageVO(componenteReliquidacionPageVO);
						valorizarReliquidacionSummaryVO.setMarcoFinal(marcoInicialEstablecimiento - ((reliquidacionServicioComponente.getMontoRebaja() == null) ? 0 : reliquidacionServicioComponente.getMontoRebaja()));
						lista.add(valorizarReliquidacionSummaryVO);
					}
				}
			}
		}
		for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionSummaryVO : lista){
			System.out.println("*********************************"+valorizarReliquidacionSummaryVO+"*********************************");
		}
		return lista;
	}

	public ProgramaComponentesCuotasSummaryVO getProgramaComponenteCuotas(Integer idProgramaAno, Integer idComponente) {
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		return  new ProgramaComponentesCuotasMapper().getSummary(programaAno, idComponente);
	}

	public List<ValorizarReliquidacionPageSummaryVO> getReliquidacionMunicipalSummaryVO(Integer idProgramaAno, Integer idComponente, Integer idServicio, Integer idReliquidacion) {
		System.out.println("idReliquidacion ----> "+idReliquidacion);
		System.out.println("idProgramaAno ---> "+idProgramaAno);
		System.out.println("idServicio ---> "+idServicio);
		System.out.println("idComponente ---> "+idComponente);
		List<ServiciosVO> servicios = null;
		if(idServicio == null){
			servicios = servicioSaludService.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServiciosVO>();
			ServiciosVO serviciosVO = servicioSaludService.getServicioSaludById(idServicio);
			servicios.add(serviciosVO);
		}
		List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		List<ValorizarReliquidacionPageSummaryVO> lista = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
		for(ServiciosVO serv : servicios){
			if(serv.getComunas() != null && serv.getComunas().size() > 0){
				for(ComunaSummaryVO comuna : serv.getComunas()){
					ValorizarReliquidacionPageSummaryVO valorizarReliquidacionSummaryVO = new ValorizarReliquidacionPageSummaryVO();
					valorizarReliquidacionSummaryVO.setIdServicio(serv.getId_servicio());
					valorizarReliquidacionSummaryVO.setServicio(serv.getNombre_servicio());
					valorizarReliquidacionSummaryVO.setIdComuna(comuna.getId());
					valorizarReliquidacionSummaryVO.setComuna(comuna.getNombre());
					/*Long marcoFinalComuna = 0L;
				ComponenteReliquidacionPageVO componenteReliquidacionPageVO = new ComponenteReliquidacionPageVO();
				Long marcoComuna= programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, idComponente, Subtitulo.SUBTITULO24.getId());

				componenteReliquidacionPageVO.setMarcoInicial(marcoComuna);
				componenteReliquidacionPageVO.setIdComponente(idComponente);
				List<ConvenioComuna> conveniosComuna = conveniosDAO.getConveniosComunaByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, idComponente, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.PAGADO.getId());
				Integer montoConvenio = 0;
				if(conveniosComuna != null && conveniosComuna.size() > 0){
					ConvenioComuna convenioComuna = conveniosComuna.get(0);
					componenteReliquidacionPageVO.setNumeroResolucion(convenioComuna.getNumeroResolucion());
					int size = convenioComuna.getConvenioComunaComponentes().size();
					ConvenioComunaComponente convenioComunaComponente = convenioComuna.getConvenioComunaComponentes().get(size - 1);
					marcoFinalComuna += convenioComunaComponente.getMontoIngresado();
					montoConvenio = convenioComunaComponente.getMontoIngresado();
				}else{
					componenteReliquidacionPageVO.setNumeroResolucion(0);
				}

				ReliquidacionComuna reliquidacionComuna = reliquidacionDAO.getReliquidacionComunaByProgramaAnoComunaComponenteSubtituloReliquidacion(idProgramaAno, comuna.getId(), idComponente, Subtitulo.SUBTITULO24.getId(), idReliquidacion);
				if(reliquidacionComuna != null){
					ReliquidacionComunaComponente reliquidacionComunaComponente =  reliquidacionComuna.getReliquidacionComunaComponentes().iterator().next();
					Cuota ultimaCuota = null;
					if(cuotasPagadas != null && cuotasPagadas.size() > 0){
						int size = cuotasPagadas.size();
						int posicion = 1;
						for(Cuota cuota : cuotasPagadas){
							CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
							Integer monto = (int)(montoConvenio * (cuota.getPorcentaje() / 100.0));
							cuotaSummaryVO.setPorcentaje(cuota.getPorcentaje());
							cuotaSummaryVO.setMonto(monto);
							if(posicion == 1){
								componenteReliquidacionPageVO.setCuotasUno(cuotaSummaryVO);
							}
							if(posicion == size){
								componenteReliquidacionPageVO.setUltimaCuota(cuotaSummaryVO);
								ultimaCuota = cuota;
							}
							posicion++;
						}
					}

					componenteReliquidacionPageVO.setPorcentajeCumplimiento(reliquidacionComunaComponente.getPorcentajeCumplimiento()/100.0);
					componenteReliquidacionPageVO.setPorcentajeReliquidacion(reliquidacionComunaComponente.getCumplimiento().getRebaja()/100.0);
					componenteReliquidacionPageVO.setRebajaUltimaCuota(reliquidacionComunaComponente.getMontoRebaja().longValue());

					Long marcoFinal = marcoComuna - reliquidacionComunaComponente.getMontoRebaja().longValue();

					valorizarReliquidacionSummaryVO.setMarcoFinal(marcoFinal);
					Integer montoUltimaCuota = 0;
					if(ultimaCuota != null){
						montoUltimaCuota = (int)((montoConvenio * (ultimaCuota.getPorcentaje()/100.0)) - reliquidacionComunaComponente.getMontoRebaja());
					}
					componenteReliquidacionPageVO.setMontoUltimaCuota(montoUltimaCuota.longValue());
					valorizarReliquidacionSummaryVO.setComponenteReliquidacionPageVO(componenteReliquidacionPageVO);

					lista.add(valorizarReliquidacionSummaryVO);

				}*/

					ComponenteReliquidacionPageVO componenteReliquidacionPageVO = new ComponenteReliquidacionPageVO();
					Long marcoInicialComuna = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, idComponente, Subtitulo.SUBTITULO24.getId());

					componenteReliquidacionPageVO.setMarcoInicial(marcoInicialComuna);
					componenteReliquidacionPageVO.setIdComponente(idComponente);
					componenteReliquidacionPageVO.setNumeroResolucion(0);
					ReliquidacionComuna reliquidacionComuna = reliquidacionDAO.getReliquidacionComunaByProgramaAnoComunaComponenteSubtituloReliquidacion(idProgramaAno, comuna.getId(), idComponente, Subtitulo.SUBTITULO24.getId(), idReliquidacion);
					if(reliquidacionComuna != null){
						ReliquidacionComunaComponente reliquidacionComunaComponente =  reliquidacionComuna.getReliquidacionComunaComponentes().iterator().next();
						Cuota ultimaCuota = null;
						if(cuotasPagadas != null && cuotasPagadas.size() > 0){
							int size = cuotasPagadas.size();
							int posicion = 1;
							for(Cuota cuota : cuotasPagadas){
								CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
								Integer monto = (int)(marcoInicialComuna * (cuota.getPorcentaje() / 100.0));
								cuotaSummaryVO.setPorcentaje(cuota.getPorcentaje());
								cuotaSummaryVO.setMonto(monto);
								if(posicion == 1){
									componenteReliquidacionPageVO.setCuotasUno(cuotaSummaryVO);
								}
								if(posicion == size){
									componenteReliquidacionPageVO.setUltimaCuota(cuotaSummaryVO);
									ultimaCuota = cuota;
								}
								posicion++;
							}
						}
						componenteReliquidacionPageVO.setPorcentajeCumplimiento(reliquidacionComunaComponente.getPorcentajeCumplimiento()/100.0);
						componenteReliquidacionPageVO.setPorcentajeReliquidacion(reliquidacionComunaComponente.getCumplimiento().getRebaja()/100.0);
						componenteReliquidacionPageVO.setRebajaUltimaCuota(reliquidacionComunaComponente.getMontoRebaja().longValue());
						Long marcoFinal = marcoInicialComuna - reliquidacionComunaComponente.getMontoRebaja().longValue();
						valorizarReliquidacionSummaryVO.setMarcoFinal(marcoFinal);
						Integer montoUltimaCuota = 0;
						if(ultimaCuota != null){
							montoUltimaCuota = componenteReliquidacionPageVO.getUltimaCuota().getMonto() - reliquidacionComunaComponente.getMontoRebaja();
						}
						componenteReliquidacionPageVO.setMontoUltimaCuota(montoUltimaCuota.longValue());
						valorizarReliquidacionSummaryVO.setComponenteReliquidacionPageVO(componenteReliquidacionPageVO);

						lista.add(valorizarReliquidacionSummaryVO);
					}
				}
			}
		}
		for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionSummaryVO : lista){
			System.out.println("*********************************"+valorizarReliquidacionSummaryVO+"*********************************");
		}
		return lista;
	}

}
