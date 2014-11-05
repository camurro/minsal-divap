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
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.CumplimientoApsMunicipalProgramaVO;
import minsal.divap.vo.EstablecimientoSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.ValorizarReliquidacionSummaryVO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ConvenioComuna;
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
import cl.minsal.divap.model.TipoDocumento;
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
		header.add(new CellExcelVO(programaAno.getNombre().toUpperCase(), (componentes.size()), 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Comuna", 1, 1));
		for(int i=0;i<componentes.size();i++){
			header.add(new CellExcelVO(componentes.get(i).getNombre().toUpperCase(), 1, 1));
			subHeader.add(new CellExcelVO("% Cumplimiento", 1, 1));
			porc_cumplimiento.add(null);
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
		header.add(new CellExcelVO("ESTABLECIMIENTO", 2, 2));
		header.add(new CellExcelVO(programaAno.getNombre().toUpperCase(), (componentes.size()), 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Establecimiento", 1, 1));
		for(int i=0;i<componentes.size();i++){
			header.add(new CellExcelVO(componentes.get(i).getNombre().toUpperCase(), 1, 1));
			subHeader.add(new CellExcelVO("% Cumplimiento", 1, 1));
			porc_cumplimiento.add(null);
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
		generadorExcel.addSheet(crearPlanillaCumplimientoMunicialProgramaSheetExcel, "Cumplimiento APS Municipales");
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




	public Integer valorizarMontosReliquidacion(Integer idProgramaAno, Integer idReliquidacion) {
		
		
		Integer planillaTrabajoMunicipal = 0;
		ProgramaVO programaAno = programasService.getProgramaAno(idProgramaAno);
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		if(programaAno.getDependenciaMunicipal()){
			planillaTrabajoMunicipal = planillaTrabajoCumplimientoReliquidacionMunicipal(programaAno, header, subHeader, idReliquidacion);
		}
		else{
			System.out.println("no tiene dependencia municipal");
			planillaTrabajoMunicipal = planillaTrabajoCumplimientoReliquidacionMunicipal(programaAno, header, subHeader, idReliquidacion);
		}
		return planillaTrabajoMunicipal;
	}

	private Integer planillaTrabajoCumplimientoReliquidacionMunicipal(ProgramaVO programaAno, List<CellExcelVO> header,
			List<CellExcelVO> subHeader, Integer idReliquidacion) {

		Integer planillaTrabajoId = null;
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaTrabajoCumplimientoReliquidacionMunicipal"+programaAno.getNombre()+".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		List<ComponentesVO> componentes = programaAno.getComponentes();
		ProgramaAno programa = programasDAO.getProgramaAnoByID(programaAno.getIdProgramaAno());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 2, 2));
		header.add(new CellExcelVO("COMUNAS", 2, 2));
		header.add(new CellExcelVO(programaAno.getNombre().toUpperCase(), (componentes.size() * 9), 1));
		header.add(new CellExcelVO("Marco Final", 1, 4));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Comuna", 1, 1));
		for(int i=0;i<componentes.size();i++){
			header.add(new CellExcelVO(componentes.get(i).getNombre().toUpperCase(), 9, 1));
			subHeader.add(new CellExcelVO("Convenio", 1, 2));
			subHeader.add(new CellExcelVO("Primera cuota", 2, 1));
			subHeader.add(new CellExcelVO("Segunda cuota", 2, 1));
			subHeader.add(new CellExcelVO("Reliquidación", 4, 1));
			subHeader.add(new CellExcelVO("%_pc", 1, 1));
			subHeader.add(new CellExcelVO("Monto_pc", 1, 1));
			subHeader.add(new CellExcelVO("%_sc", 1, 1));
			subHeader.add(new CellExcelVO("Monto_sc", 1, 1));
			subHeader.add(new CellExcelVO("% Cumplimiento", 1, 1));
			subHeader.add(new CellExcelVO("% Reliquidación", 1, 1));
			subHeader.add(new CellExcelVO("% Descuento Segunda Cuota", 1, 1));
			subHeader.add(new CellExcelVO("% Monto Final Segunda Cuota", 1, 1));

		}
		
		

		List<ValorizarReliquidacionSummaryVO> items = calcularReliquidacionPrograma(programaAno.getIdProgramaAno(), idReliquidacion);
		System.out.println("cantidad de items --> "+items.size());
		

		Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));


		PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel planillaCumplimiento = new PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel(header, subHeader, items);

		generadorExcel.addSheet(planillaCumplimiento, "Cumplimiento APS Municipales");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderDocReliquidacion.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response crearPlanillaCumplimientoMunicialProgramaSheetExcel --->" + response);
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.BASECUMPLIMIENTO.getId());
			planillaTrabajoId = documentService.createDocumentBaseReliquidacion(programa, tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, idReliquidacion, mes);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return planillaTrabajoId;
	}
	
	public ComponenteReliquidacionSummaryVO getComponenteReliquidacionSummaryVO(ProgramaVO programa, Integer idServicio, Integer idComponente, Integer idComuna, Integer idReliquidacion){
		ComponenteReliquidacionSummaryVO resultado = new ComponenteReliquidacionSummaryVO();
		Long descuentoSegundaCouta = 0L;
		List<ConvenioComuna> convenios = conveniosDAO.getConveniosByProgramaAnoComponenteComuna(programa.getIdProgramaAno(), idComponente, idComuna);
		Integer convenio = 0;
		for(int i=0; i<convenios.size(); i++){
			convenio += convenios.get(i).getMonto();
		}
		resultado.setConvenio(convenio);
		Short cuota1 = 1;
		Short cuota2 = 2;
		Cuota primeraCuota = reliquidacionDAO.getCuotaByIdProgramaAnoNroCuota(programa.getIdProgramaAno(), cuota1);
		Cuota segundaCuota = reliquidacionDAO.getCuotaByIdProgramaAnoNroCuota(programa.getIdProgramaAno(), cuota2);
		resultado.setPorc_1cuota(0.6);
		resultado.setMonto_1cuota((long)primeraCuota.getMonto());
		resultado.setPorc_2cuota(0.4);
		resultado.setMonto_2cuota((long)segundaCuota.getMonto());
		
		ReliquidacionComuna reliquidacionComuna = reliquidacionDAO.getReliquidacionComunaByReliquidacionProgramaComponenteServicioComuna
				(programa.getId(), idComponente, idServicio, idComuna, idReliquidacion);
		
		resultado.setPorc_cumplimiento(reliquidacionComuna.getPorcentajeCumplimiento());
		
		CumplimientoPrograma cumplimiento = reliquidacionComuna.getCumplimiento();
		resultado.setPorc_reliquidacion(cumplimiento.getRebaja());
		
		descuentoSegundaCouta = (long) (convenio*(1 - resultado.getPorc_1cuota())*(cumplimiento.getRebaja()/100));
		resultado.setDescuento_2cuota(descuentoSegundaCouta);
		resultado.setMontoFinal_2cuota(segundaCuota.getMonto() - descuentoSegundaCouta);
		
		return resultado;
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
			
			
			ComponenteReliquidacionSummaryVO componenteReliquidacionSummaryVO = getComponenteReliquidacionSummaryVO(programa, servicio.getId_servicio(), componenteVO.getId(), comuna.getId(), idReliquidacion);
			
			
			listaComponenteReliquidacionSummaryVO.add(componenteReliquidacionSummaryVO);
			
		}
		
		resultado.setComponenteReliquidacionSummaryVO(listaComponenteReliquidacionSummaryVO);
		
		MarcoPresupuestario marco = cajaDAO.getMarcoPresupuestarioByProgramaAnoServicio(programa.getIdProgramaAno(), servicio.getId_servicio());
		long total = marco.getMarcoModificado();
		for(ComponenteReliquidacionSummaryVO componenteReliquidacionSummaryVO : listaComponenteReliquidacionSummaryVO){
			total -= componenteReliquidacionSummaryVO.getDescuento_2cuota();
		}
		resultado.setTotal(total);
		System.out.println("servicio -> "+resultado.getIdServicio()+" comuna -> "+resultado.getIdComuna()+" marco final -> "+resultado.getTotal());
		return resultado;
		
	}
	
	public List<ValorizarReliquidacionSummaryVO> calcularReliquidacionPrograma(Integer idProgramaAno, Integer idReliquidacion) {
		System.out.println("idReliquidacion ----> "+idReliquidacion);
		System.out.println("idProgramaAno ---> "+idProgramaAno);
		
		List<ValorizarReliquidacionSummaryVO> lista = new ArrayList<ValorizarReliquidacionSummaryVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		List<ServiciosVO> servicios = programa.getServicios();
		
		for(ServiciosVO serv: servicios){
			List<ComunaSummaryVO> comunas = serv.getComunas();
			for(ComunaSummaryVO com:comunas){
				ValorizarReliquidacionSummaryVO valorizarReliquidacionSummaryVO = getValorizarReliquidacionSummaryVOPorServicioComuna(serv,com,programa, idReliquidacion);
				lista.add(valorizarReliquidacionSummaryVO);
			}
		}
		
		return lista;
		
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
