package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.enums.DocumentType;
import minsal.divap.enums.ProcessDocument;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.PercapitaSheetExcel;
import minsal.divap.vo.PercapitaExcelVO;


@Stateless
public class DocumentService {

	@Resource(name="tmpDir")
	private String tmpDir;

	@Inject
	private AlfrescoService alfrescoService;

	public void createDocument(Integer processInstanceId, BusinessProcess proceso, DocumentType type, ProcessDocument processDocument){
		switch (proceso) {
		case PERCAPITA: 
			System.out.println("Documento proceso percapita");
			createDocumentPercapita(processInstanceId, type, processDocument);
			break;

		case REBAJAS: 
			System.out.println("Documento proceso rebajas");
			createDocumentRebaja(processInstanceId, type, processDocument);
			break;

		default: System.out.println("Proceso no soportado.");
		break;
		}
	}

	private void createDocumentPercapita(Integer processInstanceId, DocumentType type, ProcessDocument processDocument){
		switch (type) {
		case EXCEL: 
			System.out.println("Documento proceso percapita excel");
			createDocumentPercapitaExcel(processInstanceId, processDocument);
			break;

		case WORD: 
			System.out.println("Documento proceso percapita word");
			createDocumentPercapitaWord(processInstanceId, processDocument);
			break;

		default: System.out.println("Tipo documento no soportado.");
		break;
		}
	}

	private void createDocumentPercapitaExcel(Integer processInstanceId,
			ProcessDocument processDocument) {
		// TODO Auto-generated method stub

	}

	private void createDocumentPercapitaWord(Integer processInstanceId,
			ProcessDocument processDocument) {
		// TODO Auto-generated method stub

	}

	private void createDocumentRebaja(Integer processInstanceId, DocumentType type, ProcessDocument processDocument){
		switch (type) {
		case EXCEL: 
			System.out.println("Documento proceso rebaja excel");
			createDocumentRebajaExcel(processInstanceId, processDocument);
			break;

		case WORD: 
			System.out.println("Documento proceso rebaja word");
			createDocumentRebajaWord(processInstanceId, processDocument);
			break;

		default: System.out.println("Tipo documento no soportado.");
		break;
		}
	}

	private void createDocumentRebajaExcel(Integer processInstanceId,
			ProcessDocument processDocument) {
		switch (processDocument) {
		case RESULTADOREBAJAS: 
			System.out.println("Documento excel proceso rebaja RESULTADOREBAJAS");
			createDocumentResultadoRebaja(processInstanceId);
			break;

		case REBAJASCOMUNAS: 
			System.out.println("Documento excel proceso rebaja REBAJASCOMUNAS");
			createDocumentRebajaComuna(processInstanceId);
			break;

		default: System.out.println("documento para proceso rebaja no soportado.");
		break;
		}
	}
	
	private void createDocumentResultadoRebaja(Integer processInstanceId) {
		String fileName = tmpDir + "/rebaja.xlsx";
		GeneradorExcel generadorExcel = new GeneradorExcel(fileName);
		List<String> headers = new ArrayList<String>();
		headers.add("REGION");
		headers.add("SERVICIO");
		headers.add("COMUNA");
		headers.add("CLASIFICACION 2014");
		headers.add("Ref. Asig_Zon 2014");
		headers.add("Tramo Pobreza");
		headers.add("Per Cápita Basal 2014");
		headers.add("Pobreza 2014");
		headers.add("Ruralidad 2014");
		headers.add("Valor Ref- Asig. Zona 2014");
		headers.add("VALOR PER CAPITA 2014 ($/mes 2014)");
		headers.add("POBLACION AÑO 2014");
		headers.add("POBLACION MAYOR DE 65 AÑOS 2014");
		headers.add("PER CÁPITA AÑO 2014 (m$2014)");
		List<PercapitaExcelVO> items  = new ArrayList<PercapitaExcelVO>();
		PercapitaExcelVO percapitaExcelVO = new PercapitaExcelVO();
		percapitaExcelVO.setRegion(15);
		percapitaExcelVO.setServicio("ARICA");
		percapitaExcelVO.setComuna("ARICA");
		percapitaExcelVO.setClasificacion("URBANA")			;
		percapitaExcelVO.setAsignacionZona(40);
		percapitaExcelVO.setTramoPobreza(4);
		percapitaExcelVO.setPercapitaBasal(3794);
		percapitaExcelVO.setValorAsignacionZona(531);
		percapitaExcelVO.setValorPercapita(4325);
		percapitaExcelVO.setPoblacion(197251);
		percapitaExcelVO.setPoblacionMayor(22199);
		percapitaExcelVO.setPercapitaAno(16150874);
		items.add(percapitaExcelVO);
		PercapitaSheetExcel percapitaSheetExcel = new PercapitaSheetExcel(headers, items, 2, 1);
		generadorExcel.addSheet( percapitaSheetExcel, "Hoja 1");
		try {
			alfrescoService.uploadDocument(generadorExcel.saveExcel());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createDocumentRebajaComuna(Integer processInstanceId) {

	}


	private void createDocumentRebajaWord(Integer processInstanceId,
			ProcessDocument processDocument) {

	}

	public void delete(Integer docId) {
		System.out.println("buscando documento " + docId + " a eliminar en alfresco");
		String docAlfresco = "40c22b34-ae96-4a78-9793-eec13103ffbb";
		alfrescoService.delete(docAlfresco);
	}

}
