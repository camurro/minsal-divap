package minsal.divap.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RecursosFinancierosProgramasReforzamientoDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesSheetExcel;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ProgramaVO;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class RecursosFinancierosProgramasReforzamientoService {
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private RecursosFinancierosProgramasReforzamientoDAO recursosFinancierosProgramasReforzamientoDAO;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private DocumentService documentService;
	@EJB
	private ProgramasService programaService;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="folderTemplateRecursosFinancierosAPS")
	private String folderTemplateRecursosFinancierosAPS;
	@Resource(name="folderRecursosFinancierosAPS")
	private String folderRecursosFinancierosAPS;


	public Integer crearIntanciaProgramaAno(String username){
		System.out.println("username-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		return recursosFinancierosProgramasReforzamientoDAO.crearIntanciaProgramaAno(usuario);
	}

	public Integer cargarPlantilla(TipoDocumentosProcesos plantilla, File file){
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		String filename = null;
		switch (plantilla) {
		case PLANTILLAPROGRAMAAPSMUNICIPALES:
			filename = "plantillaProgramasAPSMunicipal.xlsx";
			break;
		case PLANTILLAPROGRAMAAPSMUNICIPALEXCEPCION:
			filename = "plantillaProgramasAPSMunicipalExcepcion.xlsx";
			break;	
		case PLANTILLAPROGRAMAAPSSERVICIO:
			filename = "plantillaProgramasAPSServicio.docx";
			break;	
		default:
			break;
		}
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		filename = tmpDir + File.separator + filename;
		String contentType = mimemap.getContentType(filename.toLowerCase());
		if(plantillaId == null){
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateRecursosFinancierosAPS);
				System.out.println("response upload template --->"+response);
				plantillaId = documentService.createTemplate(plantilla, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Integer referenciaDocumentoId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateRecursosFinancierosAPS);
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return plantillaId;
	}

	public Integer getIdPlantillaProgramas(TipoDocumentosProcesos tipoDocumentoProceso){
		Integer plantillaId = documentService.getPlantillaByType(tipoDocumentoProceso);
		if(plantillaId == null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator + "plantillaDesempenoDificil.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<CellExcelVO> header = new ArrayList<CellExcelVO>();
			header.add(new CellExcelVO("Servicios de Salud", 2, 2));
			header.add(new CellExcelVO("Comunas", 2, 2));
			header.add(new CellExcelVO("Programa", 1, 2));
			header.add(new CellExcelVO("Componente - SubComponente - Actividad", 1, 2));
			List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Comuna", 1, 2));
			subHeader.add(new CellExcelVO("Subtitulo 24", 2, 1));
			subHeader.add(new CellExcelVO("Tarifa", 1, 1));
			subHeader.add(new CellExcelVO("Cantidad", 1, 1));
			ProgramaAPSMunicipalesSheetExcel programaAPSMunicipalesSheetExcel = new ProgramaAPSMunicipalesSheetExcel(header, subHeader, null);
			generadorExcel.addSheet(programaAPSMunicipalesSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplateRecursosFinancierosAPS);
				System.out.println("response asignacionDesempenoDificilSheetExcel --->"+response);
				plantillaId = documentService.createTemplate(TipoDocumentosProcesos.PLANTILLAASIGNACIONDESEMPENODIFICIL, response.getNodeRef(), response.getFileName(), contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	private Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}
 
	public void cambiarEstadoPrograma(Integer idPrograma, EstadosProgramas estadoPrograma) {
		System.out.println("idPrograma-->" + idPrograma + " estadoPrograma.getId()-->"+estadoPrograma.getId());
		ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idPrograma);
		
		programaAno.setEstado(new EstadoPrograma(estadoPrograma.getId()));
		System.out.println("Cambia estado ok");
	}

	public List<ProgramaVO> getProgramas(String username) {
		List<ProgramaVO> programasVO = new ArrayList<ProgramaVO>();
		List<ProgramaAno> programas = programasDAO.getProgramasByUserAno(username, getAnoCurso());
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				programasVO.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return programasVO;
	}
	
	public ProgramaVO getProgramaById(Integer idPrograma) {
		ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idPrograma);
		return new ProgramaMapper().getBasic(programaAno);
	}

}
