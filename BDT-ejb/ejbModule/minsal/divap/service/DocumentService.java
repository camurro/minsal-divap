package minsal.divap.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.DocumentOtDAO;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.model.mappers.PercapitaReferenciaDocumentoMapper;
import minsal.divap.model.mappers.ReferenciaDocumentoMapper;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReferenciaDocumentoVO;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoDistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoOt;
import cl.minsal.divap.model.OrdenTransferencia;
import cl.minsal.divap.model.Plantilla;
import cl.minsal.divap.model.Rebaja;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.TipoDocumento;

@Stateless
public class DocumentService {

	@EJB
	private DocumentDAO fileDAO;
	@EJB
	private DocumentOtDAO documentOtDAO;
	
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
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

	public Integer getPlantillaByType(TipoDocumentosProcesos template){
		return fileDAO.getPlantillaByType(template);
	}

	public Integer createDocument(String path, String filename, String contentType, boolean last){
		return fileDAO.createDocument(path, filename, contentType, last);
	}

	public Integer uploadTemporalFile(String fileName, byte[] contents) {
		System.out.println("SUBIENDO ARCHIVO " + fileName + " (" + contents.length + "bytes)");
		Integer docId = null;
		fileName = new Date().getTime() + "_" + fileName;
		BufferedOutputStream bs = null;
		try {
			FileOutputStream fs = new FileOutputStream(new File(tmpDir + File.separator + fileName));
			bs = new BufferedOutputStream(fs);
			bs.write(contents);
			ReferenciaDocumento doc = new ReferenciaDocumento();
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contentType = mimemap.getContentType(fileName.toLowerCase());
			doc.setContentType(contentType);
			doc.setFechaCreacion(new Date(Calendar.getInstance().getTimeInMillis()));
			doc.setPath(tmpDir + File.separator + fileName);
			this.fileDAO.save(doc);
			docId = Integer.valueOf(doc.getId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bs != null) try { bs.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return docId;
	}
	
	public File createTemporalFile(String fileName, byte[] contents) {
		System.out.println("Creando ARCHIVO " + fileName + " (" + contents.length + 
				"bytes)");
		fileName = new Date().getTime() + "_" + fileName;
		BufferedOutputStream bs = null;
		File file = new File(tmpDir + File.separator + fileName);
		try {
			FileOutputStream fs = new FileOutputStream(file);
			bs = new BufferedOutputStream(fs);
			bs.write(contents);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bs != null) try { bs.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return file;
	}

	public DocumentoVO getDocument(Integer documentoId){
		ReferenciaDocumento doc = this.fileDAO.findById(documentoId);
		DocumentoVO documentoVO = null;
		if(doc == null){
			return null;
		}
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
	
	public Integer createTemplate(TipoDocumentosProcesos tipoDocumentoProceso,
			String nodeRef, String filename, String contenType) {
		long current = Calendar.getInstance().getTimeInMillis();
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		TipoDocumento tipoPlantilla = new TipoDocumento(tipoDocumentoProceso.getId());
		Plantilla plantilla = new Plantilla();
		plantilla.setDocumento(referenciaDocumento);
		plantilla.setTipoPlantilla(tipoPlantilla);
		plantilla.setFechaCreacion(new Date(current));
		plantilla.setFechaVigencia(null);
		fileDAO.save(plantilla);
		return referenciaDocumento.getId();
	}
	
	public Integer updateDocumentTemplate(Integer referenciaDocumentoId, String nodeRef,
			String fileName, String contentType) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		referenciaDocumento.setNodeRef(nodeRef);
		referenciaDocumento.setPath(fileName);
		referenciaDocumento.setContentType(contentType);
		return referenciaDocumento.getId();
	}

	public Integer getDocumentoIdByPlantillaId(Integer plantillaId) {
		return fileDAO.getDocumentoIdByPlantillaId(plantillaId);
	} 
	
	public ReferenciaDocumentoSummaryVO getDocumentByPlantillaId(Integer plantillaId) {
		return new ReferenciaDocumentoMapper().getSummary(fileDAO.getDocumentByPlantillaId(plantillaId));
	} 
	
	public ReferenciaDocumentoSummaryVO getDocumentSummary(Integer referenciaDocumentoId) {
		return new ReferenciaDocumentoMapper().getSummary(fileDAO.findById(referenciaDocumentoId));
	} 
	
	public Integer createDocumentPercapita(
			Integer idDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumentoProceso,
			String nodeRef, String filename, String contenType) {
		  	DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
		  	return createDocumentPercapita(distribucionInicialPercapita, tipoDocumentoProceso, nodeRef, filename, contenType );
	}
	
	public Integer createDocumentPercapita(
			DistribucionInicialPercapita distribucionInicialPercapita, TipoDocumentosProcesos tipoDocumentoProceso,
			String nodeRef, String filename, String contenType) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoDistribucionInicialPercapita documentoDistribucionInicialPercapita = new DocumentoDistribucionInicialPercapita();
		documentoDistribucionInicialPercapita.setTipoDocumento(new TipoDocumento(tipoDocumentoProceso.getId()));
		documentoDistribucionInicialPercapita.setIdDocumento(referenciaDocumento);
		documentoDistribucionInicialPercapita.setIdDistribucionInicialPercapita(distribucionInicialPercapita);
		distribucionInicialPercapitaDAO.save(documentoDistribucionInicialPercapita);
		System.out.println("luego de aplicar insert del documento percapita");
		return referenciaDocumentoId;
	}
	
	public Integer createDocumentPercapita(
			Integer idDistribucionInicialPercapita, Integer idComuna, TipoDocumentosProcesos tipoDocumentoProceso,
			String nodeRef, String filename, String contenType) {
		  	DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
		  	Comuna comuna = antecedentesComunaDAO.findByComunaById(idComuna);
		  	return createDocumentPercapita(distribucionInicialPercapita, comuna, tipoDocumentoProceso, nodeRef, filename, contenType );
	}
 
	private Integer createDocumentPercapita(
			DistribucionInicialPercapita distribucionInicialPercapita,
			Comuna comuna, TipoDocumentosProcesos tipoDocumentoProceso,
			String nodeRef, String filename, String contenType) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoDistribucionInicialPercapita documentoDistribucionInicialPercapita = new DocumentoDistribucionInicialPercapita();
		documentoDistribucionInicialPercapita.setTipoDocumento(new TipoDocumento(tipoDocumentoProceso.getId()));
		documentoDistribucionInicialPercapita.setIdDocumento(referenciaDocumento);
		documentoDistribucionInicialPercapita.setComuna(comuna);
		documentoDistribucionInicialPercapita.setIdDistribucionInicialPercapita(distribucionInicialPercapita);
		distribucionInicialPercapitaDAO.save(documentoDistribucionInicialPercapita);
		System.out.println("luego de aplicar insert del documento percapita");
		return referenciaDocumentoId;
	}

	public ReferenciaDocumentoSummaryVO getDocumentByTypeDistribucionInicialPercapita(Integer idDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumentoProceso) {
		return new ReferenciaDocumentoMapper().getSummary(fileDAO.getDocumentByTypeDistribucionInicialPercapita(idDistribucionInicialPercapita, tipoDocumentoProceso));
	}
	
	public List<ReferenciaDocumentoVO> getDocumentByTypesServicioDistribucionInicialPercapita(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		List<ReferenciaDocumentoVO> referenciasDocumentoVO = new ArrayList<ReferenciaDocumentoVO>();
		List<DocumentoDistribucionInicialPercapita> referencias = fileDAO.getDocumentosByTypeServicioDistribucionInicialPercapita(idDistribucionInicialPercapita, idServicio, tiposDocumentoProceso);
		if(referencias != null && referencias.size() > 0){
			for(DocumentoDistribucionInicialPercapita referencia : referencias){
				referenciasDocumentoVO.add(new PercapitaReferenciaDocumentoMapper().getBasic(referencia));
			}
		}
		return referenciasDocumentoVO;
	}
	
	public ReferenciaDocumentoSummaryVO getDocumentById(Integer documentId) {
		return new ReferenciaDocumentoMapper().getSummary(fileDAO.findById(documentId));
	}
	
	public List<ReferenciaDocumentoSummaryVO> getReferenciasDocumentosById(List<Integer> docIds){
		 List<ReferenciaDocumentoSummaryVO> referenciasDocumentoSummary = new ArrayList<ReferenciaDocumentoSummaryVO>();
		 List<ReferenciaDocumento> referenciasDocumento = fileDAO.getReferenciasDocumentosById(docIds);
		 if(referenciasDocumento != null && referenciasDocumento.size() >0){
			 for(ReferenciaDocumento referenciaDocumento : referenciasDocumento){
				 referenciasDocumentoSummary.add(new ReferenciaDocumentoMapper().getSummary(referenciaDocumento));
			 }
		 }
		 return referenciasDocumentoSummary;
	}

	public void createDocumentPercapita(DistribucionInicialPercapita distribucionInicialPercapita,
			TipoDocumentosProcesos tipoDocumento, Integer referenciaDocumentoId, Boolean lastVersion) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(lastVersion != null){
			referenciaDocumento.setDocumentoFinal(lastVersion);
		}
		DocumentoDistribucionInicialPercapita documentoDistribucionInicialPercapita = new DocumentoDistribucionInicialPercapita();
		documentoDistribucionInicialPercapita.setTipoDocumento(new TipoDocumento(tipoDocumento.getId()));
		documentoDistribucionInicialPercapita.setIdDocumento(referenciaDocumento);
		documentoDistribucionInicialPercapita.setIdDistribucionInicialPercapita(distribucionInicialPercapita);
		distribucionInicialPercapitaDAO.save(documentoDistribucionInicialPercapita);
		System.out.println("luego de aplicar insert del documento percapita");
	}

	public ReferenciaDocumentoSummaryVO getLastDocumentoSummaryByDistribucionInicialPercapitaType(
			Integer idDistribucionInicialPercapita,
			TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = null;
		ReferenciaDocumento referenciaDocumento =  fileDAO.getLastDocumentByTypeDistribucionInicialPercapita(idDistribucionInicialPercapita, tipoDocumento);
		referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
		return referenciaDocumentoSummaryVO;
	}
	
	public Integer crearDocumentoRebajaCalculada(Rebaja rebaja, String nodeRef,
			String fileName, String contenType) {
		
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, fileName, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(referenciaDocumento.getRebajaCollection() == null ){
			referenciaDocumento.setRebajaCollection(new ArrayList<Rebaja>());
		}
		referenciaDocumento.getRebajaCollection().add(rebaja);
		return referenciaDocumentoId;
	}
	
	public Integer createDocumentOrdinarioOrdenTransferencia(OrdenTransferencia ordenTransferencia, TipoDocumento tipoDocumentoProceso,
            String nodeRef, String filename, String contenType) {
     Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
     ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);

     DocumentoOt documentoOt = new DocumentoOt();
     documentoOt.setIdOrdenTransferencia(ordenTransferencia);
	 documentoOt.setIdTipoDocumento(tipoDocumentoProceso);
     documentoOt.setIdDocumento(referenciaDocumento);
     documentOtDAO.save(documentoOt);
     System.out.println("luego de aplicar insert del documento percapita");
     return referenciaDocumentoId;
}
	
	public void createDocumentOrdinarioOrdenTransferencia(OrdenTransferencia ordenTransferencia,
			TipoDocumentosProcesos tipoDocumento, Integer referenciaDocumentoId, Boolean lastVersion) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(lastVersion != null){
			referenciaDocumento.setDocumentoFinal(lastVersion);
		}
		
		DocumentoOt documentoOt = new DocumentoOt();
	     documentoOt.setIdOrdenTransferencia(ordenTransferencia);
	     if(tipoDocumento!=null)
		 {
		 	documentoOt.setIdTipoDocumento(new TipoDocumento(tipoDocumento.getId()));
		 }
	     documentoOt.setIdDocumento(referenciaDocumento);
		
	     documentOtDAO.save(documentoOt);
		System.out.println("luego de aplicar insert del documento percapita");
	}
	
	public ReferenciaDocumentoSummaryVO getLastDocumentoSummaryByOrdinarioOrdenTransferenciaType(
			Integer idOrdenTransferencia,Integer idTipoDocumento) {
		
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = null;
		ReferenciaDocumento referenciaDocumento =  fileDAO.getLastDocumentByOrdinarioOrdenTransferencia(idOrdenTransferencia,idTipoDocumento);
		referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
		return referenciaDocumentoSummaryVO;
	}

}
