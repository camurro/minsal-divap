package minsal.divap.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import cl.minsal.divap.model.Usuario;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionRecursosPercapitaSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSheetExcel;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;

@Stateless
@LocalBean
public class EstimacionFlujoCajaService {
	@EJB
	private EstimacionFlujoCajaDAO estimacionFlujoCajaDAO;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private DocumentService documentService;
	@EJB
	private ServicioSaludService servicioSaludService;
	
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="folderTemplatePercapita")
	private String folderTemplatePercapita;
	
	public Integer calcularPropuesta(Integer idPrograma){
		//System.out.println("username-->"+username);
		//Obtengo la lista de datos para realizar los calculos correspondientes.
		int subtitulo = 0;
		
		switch (subtitulo) {
		case 21:
		case 22:
		case 24:
		case 29:
			if (subtitulo == 24){ 
				
			//PERCAPITA
			Double marcoPresupuestario = (Double) 0.0; //Obtener cuota desde la BD
			Double cuota = marcoPresupuestario / 12;
			
			//LEYES
			//Distribucion Mensual (Igual que Percapita)
			//Segun Demanda
			}
			
			//TODO: Explicacion del algoritmo Programas de Reoforzamiento
			
			
			
			break;
		default:
			break;
		}
		if (subtitulo == 24) //Percapita
		{
			//Cuota n [mes de programaci�n a diciembre] de un SS = MP[actualizado] / 12
		
			//Se guarda el valor para todos los programas.
			
		}
		else if (subtitulo == 21 || subtitulo == 22 || subtitulo ==24 || subtitulo == 29)
		{
		
		}
//			21, 22, 24, 29)
//		
//		
//		estimacionFlujoCajaDAO.calcularPropuesta();
	return 1;
	}

	public Integer getIdPlantillaProgramacion() {
		// TODO Auto-generated method stub
		Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPROGRAMACION);
		if(plantillaId == null){
			//TODO: Implementar la creacion del excel.
			List<BaseVO> servicios = servicioSaludService.getAllServicios();

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator + "plantillaPercapita.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();
			headers.add("REGION");
			headers.add("SERVICIO");
			headers.add("COMUNA");
			headers.add("POBLACION");
			headers.add("POBLACION MAYOR DE 65 AÑOS");
			EstimacionFlujoCajaSheetExcel estimacionFlujoCajaSheetExcel = new EstimacionFlujoCajaSheetExcel(headers, servicios);
			generadorExcel.addSheet( estimacionFlujoCajaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplatePercapita);
				System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
				plantillaId = documentService.createTemplate(TipoDocumentosProcesos.PLANTILLAPOBLACIONINSCRITA, response.getNodeRef(), response.getFileName(), contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	public Integer getIdPlantillaPropuesta() {
		// TODO Auto-generated method stub
		Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPROPUESTA);
		if(plantillaId == null){
			//TODO: Implementar la creacion del excel.
			List<BaseVO> servicios = servicioSaludService.getAllServicios();

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator + "plantillaPercapita.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();
			headers.add("REGION");
			headers.add("SERVICIO");
			headers.add("COMUNA");
			headers.add("POBLACION");
			headers.add("POBLACION MAYOR DE 65 AÑOS");
			EstimacionFlujoCajaSheetExcel estimacionFlujoCajaSheetExcel = new EstimacionFlujoCajaSheetExcel(headers, servicios);
			generadorExcel.addSheet( estimacionFlujoCajaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplatePercapita);
				System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
				plantillaId = documentService.createTemplate(TipoDocumentosProcesos.PLANTILLAPOBLACIONINSCRITA, response.getNodeRef(), response.getFileName(), contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

}
