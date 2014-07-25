package minsal.divap.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import cl.minsal.divap.model.Plantilla;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.TipoPlantilla;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.enums.TemplatesType;
import minsal.divap.vo.DocumentoVO;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;

@Stateless
public class DocumentService {

	@EJB
	private DocumentDAO fileDAO;
	@EJB
	private AlfrescoService alfrescoService;
	@Resource(name="tmpDir")
	private String tmpDir;

	public Integer createDocument(String path, String filename, String contentType){
		return fileDAO.createDocument(path, filename, contentType, false);
	}
	
	public Integer createDocumentAlfresco(String nodeRef, String filename, String contentType){
		return fileDAO.createDocumentAlfresco(nodeRef, filename, contentType, false);
	}

	public Integer getPlantillaByType(TemplatesType template){
		return fileDAO.getPlantillaByType(template);
	}

	public Integer createDocument(String path, String filename, String contentType, boolean last){
		return fileDAO.createDocument(path, filename, contentType, last);
	}

	public Integer uploadTemporalFile(String fileName, byte[] contents) {
		System.out.println("SUBIENDO ARCHIVO " + fileName + " (" + contents.length + 
				"bytes)");
		Integer docId = null;
		fileName = new Date().getTime() + "_" + fileName;
		String dir = "temp";
		BufferedOutputStream bs = null;
		try {
			FileOutputStream fs = new FileOutputStream(new File(tmpDir + File.separator + fileName));
			bs = new BufferedOutputStream(fs);
			bs.write(contents);
			ReferenciaDocumento doc = new ReferenciaDocumento();
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contentType = mimemap.getContentType(fileName.toLowerCase());
			doc.setContentType(contentType);
			doc.setPath(dir + "/" + fileName);
			this.fileDAO.save(doc);
			docId = Integer.valueOf(doc.getId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bs != null) try { bs.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return docId;
	}

	public DocumentoVO getDocument(Integer documentoId){
		ReferenciaDocumento doc = this.fileDAO.findById(documentoId);
		DocumentoVO documentoVO = null;
		try	{
			String key = ((doc.getNodeRef() == null) ? doc.getPath() : doc.getNodeRef().replace("workspace://SpacesStore/", ""));
			documentoVO = alfrescoService.download(key);
			documentoVO.setContentType(doc.getContentType());
			String filename = ((doc.getNodeRef() != null) ? doc.getPath() : doc.getPath().substring((doc.getPath().lastIndexOf(File.separator) + 1)));
			documentoVO.setName(filename);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return documentoVO;
	}

	public Integer createTemplate(TemplatesType templatesType,
			String nodeRef, String filename, String contenType) {
		long current = Calendar.getInstance().getTimeInMillis();
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		TipoPlantilla tipoPlantilla = new TipoPlantilla(templatesType.getId());
		Plantilla plantilla = new Plantilla();
		plantilla.setDocumento(referenciaDocumento);
		plantilla.setTipoPlantilla(tipoPlantilla);
		plantilla.setFechaCreacion(new Date(current));
		plantilla.setFechaVigencia(null);
		fileDAO.save(plantilla);
		return referenciaDocumento.getId();
	}

	public Integer getDocumentoIdByPlantillaId(Integer plantillaId) {
		return fileDAO.getDocumentoIdByPlantillaId(plantillaId);
	}

}
