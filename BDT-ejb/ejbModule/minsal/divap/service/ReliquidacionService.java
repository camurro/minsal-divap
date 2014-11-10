package minsal.divap.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.AnoDAO;
import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.ReliquidacionDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.FieldType;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.CrearPlanillaCumplimientoMunicialProgramaSheetExcel;
import minsal.divap.excel.impl.PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel;
import minsal.divap.excel.impl.ReliquidacionCalculoExcelValidator;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CalculoReliquidacionBaseVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.ComponenteCumplimientoVO;
import minsal.divap.vo.ComponenteReliquidacionSummaryVO;
import minsal.divap.vo.ComponenteReliquidacionVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.CumplimientoApsMunicipalProgramaVO;
import minsal.divap.vo.CuotaSummaryVO;
import minsal.divap.vo.EstablecimientoSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;
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
import cl.minsal.divap.model.MarcoPresupuestario;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.Reliquidacion;
import cl.minsal.divap.model.ReliquidacionComuna;
import cl.minsal.divap.model.ReliquidacionServicio;
import cl.minsal.divap.model.ServicioSalud;
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
	private ServicioSaludService servicioSaludService;
	@EJB
	private ProgramasService programasService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private ReliquidacionDAO reliquidacionDAO;
	@EJB
	private ComunaService comunaService;
	@EJB
	private ComponenteService componenteService;
	@EJB 
	private ConveniosDAO conveniosDAO;
	@EJB
	private CajaDAO cajaDAO;
	@EJB
	private EstablecimientosDAO establecimientosDAO;

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

	private Integer getAnoCurso() {
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
		if(plantillaIdMuncipal != null || plantillaIdServicio != null){
			System.out.println("las recupera de la BD");
			if(plantillaIdMuncipal != null){
				Integer plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaIdMuncipal);
				planillaTrabajoId = plantillaId.toString();
			}
			if(plantillaIdServicio != null){
				Integer plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaIdServicio);
				planillaTrabajoId = ((planillaTrabajoId==null) ? plantillaId.toString() : (planillaTrabajoId+"#"+plantillaId.toString()));
			}
		}else{
			System.out.println("son nuevas");
			List<CellExcelVO> header = new ArrayList<CellExcelVO>();
			List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
			if(programaVO.getDependenciaMunicipal()){		
				System.out.println("tiene dependencia comuna");
				Integer plantillaMunicipal = plantillaCumplimientoMunicipalNivelPrograma(programaVO, header, subHeader, idReliquidacion);
				planillaTrabajoId = plantillaMunicipal.toString();
			}
			if(programaVO.getDependenciaServicio()){
				System.out.println("tiene dependencia servicio");
				Integer plantillaServicio = plantillaCumplimientoEstablecimientoServicioNivelPrograma(programaVO, header, subHeader, idReliquidacion);
				planillaTrabajoId = ((planillaTrabajoId==null) ? plantillaServicio.toString() : (planillaTrabajoId+"#"+plantillaServicio.toString()));
			}
		}
		System.out.println("planillaTrabajoId ---> "+planillaTrabajoId);
		return planillaTrabajoId;
	}

	public Integer plantillaCumplimientoMunicipalNivelPrograma(ProgramaVO programaAno, List<CellExcelVO> header, List<CellExcelVO> subHeader, Integer idReliquidacion){
		Integer planillaTrabajoId = null;
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "plantillaBaseReliquidacionMunicpal"+programaAno.getNombre()+".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		List<ComponentesVO> componentes = programaAno.getComponentes();
		List<Double> porc_cumplimiento = new ArrayList<Double>();
		Programa programa = programasDAO.getProgramaPorID(programaAno.getId());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 2, 2));
		header.add(new CellExcelVO("COMUNAS", 2, 2));
		header.add(new CellExcelVO(programaAno.getNombre().toUpperCase(), componentes.size(), 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Comuna", 1, 1));
		for(int i=0;i<componentes.size();i++){
			for(int contadorSubtitulos = 0; contadorSubtitulos < componentes.get(i).getSubtitulos().size(); contadorSubtitulos++){
				header.add(new CellExcelVO(componentes.get(i).getNombre().toUpperCase(), 1, 1));
				subHeader.add(new CellExcelVO("% Cumplimiento/"+componentes.get(i).getSubtitulos().get(contadorSubtitulos).getNombre(), 1, 1));
				porc_cumplimiento.add(null);
			}
		}

		List<ServiciosVO> servicios = programaAno.getServicios();
		List<CumplimientoApsMunicipalProgramaVO> items = new ArrayList<CumplimientoApsMunicipalProgramaVO>();
		for(int i=0;i<servicios.size();i++){
			List<ComunaSummaryVO> comunas = servicios.get(i).getComunas(); 
			for(int j=0;j<comunas.size();j++){				
				CumplimientoApsMunicipalProgramaVO fila = new CumplimientoApsMunicipalProgramaVO();
				fila.setIdServicio(servicios.get(i).getId_servicio());
				fila.setServicio(servicios.get(i).getNombre_servicio());
				fila.setIdComuna(comunas.get(j).getId());
				fila.setComuna(comunas.get(j).getNombre());
				fila.setPorcCumplimiento(porc_cumplimiento);
				items.add(fila);
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
		String filename = tmpDir + File.separator + "plantillaBaseReliquidacionServicio"+programaAno.getNombre()+".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		List<ComponentesVO> componentes = programaAno.getComponentes();
		List<Double> porc_cumplimiento = new ArrayList<Double>();
		Programa programa = programasDAO.getProgramaPorID(programaAno.getId());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 2, 2));
		header.add(new CellExcelVO("ESTABLECIMIENTOS", 2, 2));
		int sizeComponentes = 0;
		for(ComponentesVO componentesVO : componentes){
			sizeComponentes += componentesVO.getSubtitulos().size();
		}
		header.add(new CellExcelVO(programaAno.getNombre().toUpperCase(), sizeComponentes, 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Establecimiento", 1, 1));
		for(int i=0; i<componentes.size() ; i++){
			for(int contadorSubtitulos = 0; contadorSubtitulos < componentes.get(i).getSubtitulos().size(); contadorSubtitulos++){
				if(contadorSubtitulos == 0){
					header.add(new CellExcelVO(componentes.get(i).getNombre().toUpperCase(), componentes.get(i).getSubtitulos().size(), 1));
				}
				subHeader.add(new CellExcelVO("% Cumplimiento/"+componentes.get(i).getSubtitulos().get(contadorSubtitulos).getNombre(), 1, 1));
				porc_cumplimiento.add(null);
			}
		}
		List<ServiciosVO> servicios = programaAno.getServicios();
		List<CumplimientoApsMunicipalProgramaVO> items = new ArrayList<CumplimientoApsMunicipalProgramaVO>();
		for(int i=0;i<servicios.size();i++){
			List<EstablecimientoSummaryVO> comunas = servicios.get(i).getEstableclimientos();
			for(int j=0;j<comunas.size();j++){				
				CumplimientoApsMunicipalProgramaVO fila = new CumplimientoApsMunicipalProgramaVO();
				fila.setIdServicio(servicios.get(i).getId_servicio());
				fila.setServicio(servicios.get(i).getNombre_servicio());
				fila.setIdComuna(comunas.get(j).getId());
				fila.setComuna(comunas.get(j).getNombre());
				fila.setPorcCumplimiento(porc_cumplimiento);
				items.add(fila);
			}
		}
		CrearPlanillaCumplimientoMunicialProgramaSheetExcel crearPlanillaCumplimientoMunicialProgramaSheetExcel = new CrearPlanillaCumplimientoMunicialProgramaSheetExcel(header, subHeader, items);
		generadorExcel.addSheet(crearPlanillaCumplimientoMunicialProgramaSheetExcel, "Cumplimiento APS Servicio");
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
		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		if(programaVO.getDependenciaMunicipal()  && programaVO.getDependenciaServicio()){
			for(ServiciosVO servicio: servicios){
				calcularReliquidacionMixto(idProgramaAno, servicio.getId_servicio(), idReliquidacion);
			}
			List<CellExcelVO> header = new ArrayList<CellExcelVO>();
			List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
			List<CellExcelVO> headerServicio = new ArrayList<CellExcelVO>();
			List<CellExcelVO> subHeaderServicio = new ArrayList<CellExcelVO>();
			planillaTrabajoMunicipal = planillaTrabajoCumplimientoReliquidacionMunicipal(programaVO, header, subHeader, idReliquidacion);
			planillaTrabajoServicio = planillaTrabajoCumplimientoReliquidacionServicio(programaVO, headerServicio, subHeaderServicio, idReliquidacion);
			idDocReliquidacion = planillaTrabajoMunicipal + "#" + planillaTrabajoServicio;
		}else{
			if(programaVO.getDependenciaMunicipal()){
				for(ServiciosVO servicio: servicios){
					calcularReliquidacionComuna(idProgramaAno, servicio.getId_servicio(), idReliquidacion);
				}
				List<CellExcelVO> header = new ArrayList<CellExcelVO>();
				List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
				planillaTrabajoMunicipal = planillaTrabajoCumplimientoReliquidacionMunicipal(programaVO, header, subHeader, idReliquidacion);
				idDocReliquidacion = planillaTrabajoMunicipal.toString();
			}else{
				for(ServiciosVO servicio: servicios){
					calcularReliquidacionServicio(idProgramaAno, servicio.getId_servicio(), idReliquidacion);
				}
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
		String filename = tmpDir + File.separator + "planillaTrabajoCumplimientoReliquidacionMunicipal"+programaVO.getNombre()+".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		List<ComponentesVO> componentes = programaVO.getComponentes();
		//		ProgramaAno programa = programasDAO.getProgramaAnoByID(programaVO.getIdProgramaAno());
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 2, 2));
		header.add(new CellExcelVO("COMUNAS", 2, 2));
		header.add(new CellExcelVO(programaVO.getNombre().toUpperCase(), (componentes.size() * 9), 1));
		header.add(new CellExcelVO("Marco Final", 1, 4));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Comuna", 1, 1));
		for(ComponentesVO componente : componentes){
			header.add(new CellExcelVO(componente.getNombre().toUpperCase(), 9, 1));
			subHeader.add(new CellExcelVO("Convenio", 1, 2));
			List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAnoComponente(programaVO.getIdProgramaAno(), componente.getId());
			System.out.println("planillaTrabajoCumplimientoReliquidacionMunicipal cuotasPagadas.size()="+((cuotasPagadas!=null)?cuotasPagadas.size():"0"));
			if(cuotasPagadas != null && cuotasPagadas.size() > 0){
				for(int cuotas = 0; cuotas < cuotasPagadas.size(); cuotas++){
					subHeader.add(new CellExcelVO("Cuota N° " + (cuotas+1), 2, 1));
				}
			}
			subHeader.add(new CellExcelVO("Reliquidación", 4, 1));
			subHeader.add(new CellExcelVO("%_pc", 1, 1));
			subHeader.add(new CellExcelVO("Monto_pc", 1, 1));
			subHeader.add(new CellExcelVO("%_sc", 1, 1));
			subHeader.add(new CellExcelVO("Monto_sc", 1, 1));
			subHeader.add(new CellExcelVO("% Cumplimiento", 1, 1));
			subHeader.add(new CellExcelVO("% Reliquidación", 1, 1));
			subHeader.add(new CellExcelVO("% Descuento última Cuota", 1, 1));
			subHeader.add(new CellExcelVO("% Monto Final última Cuota", 1, 1));

		}
		List<ValorizarReliquidacionSummaryVO> items = calcularReliquidacionProgramaComuna(programaVO.getIdProgramaAno(), idReliquidacion);
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
		String filename = tmpDir + File.separator + "planillaTrabajoCumplimientoReliquidacionServicio"+programaVO.getNombre()+".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		List<ComponentesVO> componentes = programaVO.getComponentes();
		//ProgramaAno programa = programasDAO.getProgramaAnoByID(programaVO.getIdProgramaAno());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 2, 2));
		header.add(new CellExcelVO("ESTABLECIMIENTOS", 2, 2));
		header.add(new CellExcelVO(programaVO.getNombre().toUpperCase(), (componentes.size() * 9), 1));
		header.add(new CellExcelVO("Marco Final", 1, 4));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Establecimiento", 1, 1));
		for(ComponentesVO componente : componentes){
			int totalSubtitulo = componente.getSubtitulos().size();
			header.add(new CellExcelVO(componente.getNombre().toUpperCase(), totalSubtitulo * 9, 1));
			for(int iteracion = 0; iteracion < totalSubtitulo; iteracion++){
				subHeader.add(new CellExcelVO("Convenio", 1, 2));
				List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAnoComponente(programaVO.getIdProgramaAno(), componente.getId());
				System.out.println("planillaTrabajoCumplimientoReliquidacionServicio cuotasPagadas.size()="+((cuotasPagadas!=null)?cuotasPagadas.size():"0"));
				if(cuotasPagadas != null && cuotasPagadas.size() > 0){
					for(int cuotas = 0; cuotas < cuotasPagadas.size(); cuotas++){
						subHeader.add(new CellExcelVO("Cuota N° " + (cuotas+1), 2, 1));
					}
				}
				subHeader.add(new CellExcelVO("Reliquidación", 4, 1));
				subHeader.add(new CellExcelVO("%_pc", 1, 1));
				subHeader.add(new CellExcelVO("Monto_pc", 1, 1));
				subHeader.add(new CellExcelVO("%_sc", 1, 1));
				subHeader.add(new CellExcelVO("Monto_sc", 1, 1));
				subHeader.add(new CellExcelVO("% Cumplimiento", 1, 1));
				subHeader.add(new CellExcelVO("% Reliquidación", 1, 1));
				subHeader.add(new CellExcelVO("% Descuento última Cuota", 1, 1));
				subHeader.add(new CellExcelVO("% Monto Final última Cuota", 1, 1));
			}
		}
		List<ValorizarReliquidacionSummaryVO> items = calcularReliquidacionProgramaServicio(programaVO.getIdProgramaAno(), idReliquidacion);
		System.out.println("cantidad de items --> "+items.size());
		PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel planillaCumplimiento = new PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel(header, subHeader, items);
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
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<ConvenioComuna> conveniosComunas = conveniosDAO.getConveniosComunaByProgramaAnoServicio(idProgramaAno, idServicio);
		if(conveniosComunas != null && conveniosComunas.size() > 0){
			for(ConvenioComuna convenioComuna : conveniosComunas){
				if(convenioComuna.getConvenioComunaComponentes() != null && convenioComuna.getConvenioComunaComponentes().size() > 0){
					for(ConvenioComunaComponente convenioComunaComponente : convenioComuna.getConvenioComunaComponentes()){
						ReliquidacionComuna reliquidacionComuna = reliquidacionDAO.getReliquidacionComunaByProgramaAnoComunaComponenteReliquidacion(idProgramaAno, convenioComuna.getIdComuna().getId(), convenioComunaComponente.getComponente().getId(), idReliquidacion);
						if(reliquidacionComuna != null){
							CumplimientoPrograma cumplimientoRebaja = getCumplimientoPrograma(programaVO.getId(), reliquidacionComuna.getPorcentajeCumplimiento());
							List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAnoComponente(idProgramaAno, convenioComunaComponente.getComponente().getId());
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
							}else{
								continue;
							}
							if(cumplimientoRebaja != null && cumplimientoRebaja.getRebaja() != null){
								reliquidacionComuna.setCumplimiento(cumplimientoRebaja);
								Integer rebajaUltimaCouta = (int) (convenioComunaComponente.getMonto() * (1 - porcentajeAcumulado) * (cumplimientoRebaja.getRebaja()/100.0));
								convenioComunaComponente.setMontoRebaja(rebajaUltimaCouta);
							}
						}
					}
				}
			}
		}
	}

	public void calcularReliquidacionServicio(Integer idProgramaAno, Integer idServicio, Integer idReliquidacion){
		System.out.println("calcularReliquidacionServicio idProgramaAno="+idProgramaAno+" idServicio="+idServicio+" idReliquidacion="+idReliquidacion);
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<ConvenioServicio> conveniosServicio = conveniosDAO.getConveniosServicioByProgramaAnoServicio(idProgramaAno, idServicio);
		System.out.println("conveniosServicio.size()="+((conveniosServicio!=null) ? conveniosServicio.size(): "0"));
		if(conveniosServicio != null && conveniosServicio.size() > 0){
			for(ConvenioServicio convenioServicio : conveniosServicio){
				System.out.println("convenioServicio.getConvenioServicioComponentes().size()="+((convenioServicio.getConvenioServicioComponentes()!=null) ? convenioServicio.getConvenioServicioComponentes().size(): "0"));
				if(convenioServicio.getConvenioServicioComponentes() != null && convenioServicio.getConvenioServicioComponentes().size() > 0){
					for(ConvenioServicioComponente convenioServicioComponente : convenioServicio.getConvenioServicioComponentes()){
						ReliquidacionServicio reliquidacionServicio = reliquidacionDAO.getReliquidacionServicioByProgramaAnoEstablecimientoComponenteReliquidacion(idProgramaAno, convenioServicio.getIdEstablecimiento().getId(), convenioServicioComponente.getComponente().getId(), idReliquidacion);
						if(reliquidacionServicio != null){
							CumplimientoPrograma cumplimientoRebaja = getCumplimientoPrograma(programaVO.getId(), reliquidacionServicio.getPorcentajeCumplimiento());
							List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAnoComponente(idProgramaAno, convenioServicioComponente.getComponente().getId());
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
							}else{
								continue;
							}
							if(cumplimientoRebaja != null && cumplimientoRebaja.getRebaja() != null){
								reliquidacionServicio.setCumplimiento(cumplimientoRebaja);
								System.out.println("calcularReliquidacionServicio convenioServicioComponente.getMonto()="+convenioServicioComponente.getMonto()+" idServicio="+idServicio+" idReliquidacion="+idReliquidacion);
								Integer rebajaUltimaCouta = (int) (convenioServicioComponente.getMonto() * (1 - porcentajeAcumulado) * (cumplimientoRebaja.getRebaja()/100.0));
								System.out.println("calcularReliquidacionServicio convenioServicioComponente.getMonto()="+convenioServicioComponente.getMonto()+" rebajaUltimaCouta="+rebajaUltimaCouta);
								convenioServicioComponente.setMontoRebaja(rebajaUltimaCouta);
							}
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

	private List<ValorizarReliquidacionSummaryVO> calcularReliquidacionProgramaServicio(Integer idProgramaAno, Integer idReliquidacion) {
		System.out.println("idReliquidacion ----> "+idReliquidacion);
		System.out.println("idProgramaAno ---> "+idProgramaAno);
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<ServiciosVO> servicios = programaVO.getServicios();
		List<ValorizarReliquidacionSummaryVO> lista = new ArrayList<ValorizarReliquidacionSummaryVO>();
		for(ServiciosVO serv: servicios){
			List<ConvenioServicio> conveniosServicios = conveniosDAO.getConveniosServicioByProgramaAnoServicio(idProgramaAno, serv.getId_servicio());
			if(conveniosServicios != null && conveniosServicios.size() > 0){
				for(ConvenioServicio convenioServicio : conveniosServicios){
					ValorizarReliquidacionSummaryVO valorizarReliquidacionSummaryVO = new ValorizarReliquidacionSummaryVO();
					valorizarReliquidacionSummaryVO.setIdServicio(serv.getId_servicio());
					valorizarReliquidacionSummaryVO.setServicio(serv.getNombre_servicio());
					valorizarReliquidacionSummaryVO.setIdComuna(convenioServicio.getIdEstablecimiento().getId());
					valorizarReliquidacionSummaryVO.setComuna(convenioServicio.getIdEstablecimiento().getNombre());
					if(convenioServicio.getConvenioServicioComponentes() != null && convenioServicio.getConvenioServicioComponentes().size() > 0){
						List<ComponenteReliquidacionVO>  componentesReliquidacionVO = new ArrayList<ComponenteReliquidacionVO>();
						Long marcoFinal = 0L;
						for(ConvenioServicioComponente convenioServicioComponente : convenioServicio.getConvenioServicioComponentes()){
							ComponenteReliquidacionVO componenteReliquidacionVO = new ComponenteReliquidacionVO();
							componenteReliquidacionVO.setNumeroResolucion(convenioServicio.getNumeroResolucion());
							componenteReliquidacionVO.setIdComponente(convenioServicioComponente.getComponente().getId());
							ReliquidacionServicio reliquidacionServicio = reliquidacionDAO.getReliquidacionServicioByProgramaAnoEstablecimientoComponenteReliquidacion(idProgramaAno, convenioServicio.getIdEstablecimiento().getId(), convenioServicioComponente.getComponente().getId(), idReliquidacion);
							if(reliquidacionServicio != null){
								List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAnoComponente(idProgramaAno, convenioServicioComponente.getComponente().getId());
								Cuota ultimaCuota = null;
								if(cuotasPagadas != null && cuotasPagadas.size() > 0){
									List<CuotaSummaryVO> cuotasSummaryVO = new ArrayList<CuotaSummaryVO>();
									int size = cuotasPagadas.size();
									int posicion = 1;
									for(Cuota cuota : cuotasPagadas){
										CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
										Integer monto = (int)(convenioServicioComponente.getMonto() * (cuota.getPorcentaje() / 100.0));
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
								componenteReliquidacionVO.setPorcentajeCumplimiento(reliquidacionServicio.getPorcentajeCumplimiento());
								componenteReliquidacionVO.setPorcentajeReliquidacion(reliquidacionServicio.getCumplimiento().getRebaja());
								componenteReliquidacionVO.setRebajaUltimaCuota(convenioServicioComponente.getMontoRebaja());
								Integer montoUltimaCuota = 0;
								if(ultimaCuota != null){
									montoUltimaCuota = (int)((convenioServicioComponente.getMonto() * (ultimaCuota.getPorcentaje()/100.0)) - convenioServicioComponente.getMontoRebaja());
								}
								componenteReliquidacionVO.setMontoUltimaCuota(montoUltimaCuota);
								marcoFinal+=(convenioServicioComponente.getMonto() - convenioServicioComponente.getMontoRebaja());
							}
							componentesReliquidacionVO.add(componenteReliquidacionVO);
						}
						valorizarReliquidacionSummaryVO.setMarcoFinal(marcoFinal);
						valorizarReliquidacionSummaryVO.setComponentesReliquidacion(componentesReliquidacionVO);
					}
					lista.add(valorizarReliquidacionSummaryVO);
				}
			}
		}
		return lista;
	}

	private List<ValorizarReliquidacionSummaryVO> calcularReliquidacionProgramaComuna(Integer idProgramaAno, Integer idReliquidacion) {
		System.out.println("idReliquidacion ----> "+idReliquidacion);
		System.out.println("idProgramaAno ---> "+idProgramaAno);
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<ServiciosVO> servicios = programaVO.getServicios();
		List<ValorizarReliquidacionSummaryVO> lista = new ArrayList<ValorizarReliquidacionSummaryVO>();
		for(ServiciosVO serv: servicios){
			List<ConvenioComuna> conveniosComunas = conveniosDAO.getConveniosComunaByProgramaAnoServicio(idProgramaAno, serv.getId_servicio());
			if(conveniosComunas != null && conveniosComunas.size() > 0){
				for(ConvenioComuna convenioComuna : conveniosComunas){
					ValorizarReliquidacionSummaryVO valorizarReliquidacionSummaryVO = new ValorizarReliquidacionSummaryVO();
					valorizarReliquidacionSummaryVO.setIdServicio(serv.getId_servicio());
					valorizarReliquidacionSummaryVO.setServicio(serv.getNombre_servicio());
					valorizarReliquidacionSummaryVO.setIdComuna(convenioComuna.getIdComuna().getId());
					valorizarReliquidacionSummaryVO.setComuna(convenioComuna.getIdComuna().getNombre());
					if(convenioComuna.getConvenioComunaComponentes() != null && convenioComuna.getConvenioComunaComponentes().size() > 0){
						List<ComponenteReliquidacionVO>  componentesReliquidacionVO = new ArrayList<ComponenteReliquidacionVO>();
						Long marcoFinal = 0L;
						for(ConvenioComunaComponente convenioComunaComponente : convenioComuna.getConvenioComunaComponentes()){
							ComponenteReliquidacionVO componenteReliquidacionVO = new ComponenteReliquidacionVO();
							componenteReliquidacionVO.setNumeroResolucion(convenioComuna.getNumeroResolucion());
							ReliquidacionComuna reliquidacionComuna = reliquidacionDAO.getReliquidacionComunaByProgramaAnoComunaComponenteReliquidacion(idProgramaAno, convenioComuna.getIdComuna().getId(), convenioComunaComponente.getComponente().getId(), idReliquidacion);
							if(reliquidacionComuna != null){
								List<Cuota> cuotasPagadas = reliquidacionDAO.getCuotasByProgramaAnoComponente(idProgramaAno, convenioComunaComponente.getComponente().getId());

								Cuota ultimaCuota = null;
								if(cuotasPagadas != null && cuotasPagadas.size() > 0){
									List<CuotaSummaryVO> cuotasSummaryVO = new ArrayList<CuotaSummaryVO>();
									int size = cuotasPagadas.size();
									int posicion = 1;
									for(Cuota cuota : cuotasPagadas){
										CuotaSummaryVO cuotaSummaryVO = new CuotaSummaryVO();
										Integer monto = (int)(convenioComunaComponente.getMonto() * (cuota.getPorcentaje() / 100.0));
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
								componenteReliquidacionVO.setPorcentajeCumplimiento(reliquidacionComuna.getPorcentajeCumplimiento());
								componenteReliquidacionVO.setPorcentajeReliquidacion(reliquidacionComuna.getCumplimiento().getRebaja());
								componenteReliquidacionVO.setRebajaUltimaCuota(convenioComunaComponente.getMontoRebaja());
								Integer montoUltimaCuota = 0;
								if(ultimaCuota != null){
									montoUltimaCuota = (int)((convenioComunaComponente.getMonto() * (ultimaCuota.getPorcentaje()/100.0)) - convenioComunaComponente.getMontoRebaja());
								}
								componenteReliquidacionVO.setMontoUltimaCuota(montoUltimaCuota);
								marcoFinal+=(convenioComunaComponente.getMonto() - convenioComunaComponente.getMontoRebaja());
							}
							componentesReliquidacionVO.add(componenteReliquidacionVO);
						}
						valorizarReliquidacionSummaryVO.setMarcoFinal(marcoFinal);
						valorizarReliquidacionSummaryVO.setComponentesReliquidacion(componentesReliquidacionVO);
					}
					lista.add(valorizarReliquidacionSummaryVO);
				}
			}
		}
		return lista;
	}


	public ValorizarReliquidacionSummaryVO getValorizarReliquidacionSummaryVOPorServicioComuna(ServiciosVO servicio, ComunaSummaryVO comuna, ProgramaVO programa, Integer idReliquidacion){

		ValorizarReliquidacionSummaryVO resultado = new ValorizarReliquidacionSummaryVO();
		resultado.setComuna(comuna.getNombre());
		resultado.setIdComuna(comuna.getId());
		resultado.setIdServicio(servicio.getId_servicio());
		resultado.setServicio(servicio.getNombre_servicio());

		List<ComponentesVO> componentes = programa.getComponentes();



		List<ComponenteReliquidacionSummaryVO>  listaComponenteReliquidacionSummaryVO =new ArrayList<ComponenteReliquidacionSummaryVO>();
		for(ComponentesVO componenteVO: componentes){


			/*ComponenteReliquidacionSummaryVO componenteReliquidacionSummaryVO = getComponenteReliquidacionSummaryVO(programa, servicio.getId_servicio(), componenteVO.getId(), comuna.getId(), idReliquidacion);


			listaComponenteReliquidacionSummaryVO.add(componenteReliquidacionSummaryVO);*/

		}

		//		/resultado.setComponenteReliquidacionSummaryVO(listaComponenteReliquidacionSummaryVO);

		MarcoPresupuestario marco = cajaDAO.getMarcoPresupuestarioByProgramaAnoServicio(programa.getIdProgramaAno(), servicio.getId_servicio());
		long total = marco.getMarcoModificado();
		for(ComponenteReliquidacionSummaryVO componenteReliquidacionSummaryVO : listaComponenteReliquidacionSummaryVO){
			total -= componenteReliquidacionSummaryVO.getDescuento_2cuota();
		}
		//		resultado.setTotal(total);
		//		System.out.println("servicio -> "+resultado.getIdServicio()+" comuna -> "+resultado.getIdComuna()+" marco final -> "+resultado.getTotal());
		return resultado;

	}

	public void procesarCalculoReliquidacionMunicipal(Integer idProgramaAno, Integer idReliquidacion, XSSFWorkbook workbook) throws ExcelFormatException{
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
			if (row.getRowNum() == 3){
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
		ReliquidacionCalculoExcelValidator reliquidacionCalculoExcelValidator = new ReliquidacionCalculoExcelValidator(cells.size(), cells, true, 0, 2);
		reliquidacionCalculoExcelValidator.validateFormat(worksheet);
		List<CalculoReliquidacionBaseVO> items = reliquidacionCalculoExcelValidator.getItems();
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		Reliquidacion reliquidacion = reliquidacionDAO.getReliquidacionById(idReliquidacion);
		if(reliquidacion.getIdProgramaAno() == null){
			reliquidacion.setIdProgramaAno(programaAno);
		}
		reliquidacionDAO.deleteReliquidacionComuna(idProgramaAno);
		for(CalculoReliquidacionBaseVO calculoReliquidacionBaseVO : items){
			ServicioSalud servicio = servicioSaludService.getServicioSaludPorID(calculoReliquidacionBaseVO.getId_servicio());
			Comuna comuna = comunaService.getComunaById(calculoReliquidacionBaseVO.getId_comuna().intValue());
			int contadorComponenete = 0;
			for(ComponenteCumplimientoVO componenteCumplimientoVO : calculoReliquidacionBaseVO.getComponentesCumplimientoVO()){
				Componente componente = componenteService.getComponenteByNombre(nombresComponentes.get(contadorComponenete++));
				ReliquidacionComuna reliquidacionComuna = new ReliquidacionComuna();
				System.out.println("servicio="+servicio);
				reliquidacionComuna.setServicio(servicio);
				System.out.println("comuna="+comuna);
				reliquidacionComuna.setComuna(comuna);
				System.out.println("reliquidacion="+reliquidacion);
				reliquidacionComuna.setReliquidacion(reliquidacion);
				System.out.println("programaAno.getPrograma()="+programaAno.getPrograma());
				reliquidacionComuna.setPrograma(programaAno);
				System.out.println("componente="+componente);
				reliquidacionComuna.setComponente(componente);
				System.out.println("componenteCumplimientoVO "+componenteCumplimientoVO.getPorcentajeCumplimiento()+" componente --> "+componente.getNombre());
				reliquidacionComuna.setPorcentajeCumplimiento(componenteCumplimientoVO.getPorcentajeCumplimiento());
				reliquidacionComuna.setCumplimiento(getCumplimientoPrograma(programaAno, componenteCumplimientoVO));
				reliquidacionDAO.save(reliquidacionComuna);	
			}
		}
	}

	public void procesarCalculoReliquidacionServicio(Integer idProgramaAno, Integer idReliquidacion, XSSFWorkbook workbook) throws ExcelFormatException{
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
			if (row.getRowNum() == 3){
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
		ReliquidacionCalculoExcelValidator reliquidacionCalculoExcelValidator = new ReliquidacionCalculoExcelValidator(cells.size(), cells, true, 0, 2);
		reliquidacionCalculoExcelValidator.validateFormat(worksheet);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		Reliquidacion reliquidacion = reliquidacionDAO.getReliquidacionById(idReliquidacion);
		if(reliquidacion.getIdProgramaAno() == null){
			reliquidacion.setIdProgramaAno(programaAno);
		}
		reliquidacionDAO.deleteReliquidacionServicio(idProgramaAno);
		List<CalculoReliquidacionBaseVO> items = reliquidacionCalculoExcelValidator.getItems();
		for(CalculoReliquidacionBaseVO calculoReliquidacionBaseVO : items){
			ServicioSalud servicio = servicioSaludService.getServicioSaludPorID(calculoReliquidacionBaseVO.getId_servicio());
			Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(calculoReliquidacionBaseVO.getId_comuna().intValue());
			int contadorComponente = 0;
			for(ComponenteCumplimientoVO componenteCumplimientoVO : calculoReliquidacionBaseVO.getComponentesCumplimientoVO()){
				Componente componente = componenteService.getComponenteByNombre(nombresComponentes.get(contadorComponente++));
				ReliquidacionServicio reliquidacionServicio = new ReliquidacionServicio();
				System.out.println("servicio="+servicio);
				reliquidacionServicio.setServicio(servicio);
				System.out.println("establecimiento="+establecimiento);
				reliquidacionServicio.setEstablecimiento(establecimiento);
				System.out.println("reliquidacion="+reliquidacion);
				reliquidacionServicio.setReliquidacion(reliquidacion);
				System.out.println("programaAno.getPrograma()="+programaAno.getPrograma());
				reliquidacionServicio.setPrograma(programaAno);
				System.out.println("componente="+componente);
				reliquidacionServicio.setComponente(componente);
				System.out.println("componenteCumplimientoVO "+componenteCumplimientoVO.getPorcentajeCumplimiento()+" componente --> "+componente.getNombre());
				reliquidacionServicio.setPorcentajeCumplimiento(componenteCumplimientoVO.getPorcentajeCumplimiento());
				reliquidacionServicio.setCumplimiento(getCumplimientoPrograma(programaAno, componenteCumplimientoVO));
				reliquidacionDAO.save(reliquidacionServicio);	
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


}
