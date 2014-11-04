package minsal.divap.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.DocumentOtDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.dao.RebajaDAO;
import minsal.divap.dao.ReliquidacionDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.model.mappers.PercapitaReferenciaDocumentoMapper;
import minsal.divap.model.mappers.ReferenciaDocumentoMapper;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReferenciaDocumentoVO;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoConvenio;
import cl.minsal.divap.model.DocumentoDistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.DocumentoOt;
import cl.minsal.divap.model.DocumentoRebaja;
import cl.minsal.divap.model.DocumentoReliquidacion;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.OrdenTransferencia;
import cl.minsal.divap.model.Plantilla;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.Rebaja;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.Reliquidacion;
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
	private RebajaDAO rebajaDAO;
	@EJB
	private EstimacionFlujoCajaDAO estimacionFlujoCajaDAO;
	@EJB
	private ReliquidacionDAO reliquidacionDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private AlfrescoService alfrescoService;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="tmpDownloadDirectory")
	private String tmpDownloadDirectory;

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

	public void createTempFile(String folder, String fileName, byte[] contents) {
		System.out.println("Creando ARCHIVO " + fileName + " (" + contents.length + 
				"bytes)");
		BufferedOutputStream bs = null;
		File fileFolder = new File(folder);
		File file = new File(folder + File.separator + fileName);
		try {
			boolean parentDirs = false;
			if (!fileFolder.exists()) {
				parentDirs = fileFolder.mkdirs();
			}
			if(!parentDirs){
				return;
			}
			FileOutputStream fs = new FileOutputStream(file);
			bs = new BufferedOutputStream(fs);
			bs.write(contents);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bs != null) try { bs.close(); } catch (Exception e) {e.printStackTrace();}
		}
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
			System.out.println("getDocument doc.getNodeRef()->"+doc.getNodeRef());
			System.out.println("getDocument doc.getPath()->"+doc.getPath());
			String filename = ((doc.getNodeRef() != null) ? doc.getPath() : doc.getPath().substring((doc.getPath().lastIndexOf(File.separator) + 1)));
			filename = filename.replace(" ", "_");
			System.out.println("getDocument filename-->"+filename);
			documentoVO.setName(filename);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return documentoVO;
	}

	public DocumentoVO getDocument(Integer idServicio, List<Integer> idDocumentos){
		DocumentoVO documentoVO = null;
		if(idDocumentos == null || idDocumentos.size() == 0){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		Long millis = cal.getTimeInMillis();
		String tmpFolder = tmpDownloadDirectory + File.separator + millis.toString() + File.separator + idServicio;
		for(Integer idDocumento : idDocumentos){
			ReferenciaDocumento doc = this.fileDAO.findById(idDocumento);
			if(doc == null){
				continue;
			}
			try	{
				String key = ((doc.getNodeRef() == null) ? doc.getPath() : doc.getNodeRef().replace("workspace://SpacesStore/", ""));
				DocumentoVO documentoTmpVO = alfrescoService.download(key);
				String filename = ((doc.getNodeRef() != null) ? doc.getPath() : doc.getPath().substring((doc.getPath().lastIndexOf(File.separator) + 1)));
				filename = filename.replace(" ", "_");
				System.out.println("getDocument filename-->"+filename);
				createTempFile(tmpFolder, filename, documentoTmpVO.getContent());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		String destZipFile = tmpFolder + ".zip";
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String contentType = mimemap.getContentType(destZipFile.toLowerCase());
		try {
			zipFolder(tmpFolder, destZipFile);
			documentoVO = new DocumentoVO(idServicio + ".zip", contentType, getBytesFromFile(new File(destZipFile)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documentoVO;
	}

	private  byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		try{
			System.out.println("\nDEBUG: FileInputStream is " + file);
			// tamaño del archivo
			long length = file.length();
			System.out.println("DEBUG: largo de " + file + " es " + length + "\n");
			if (length > Integer.MAX_VALUE) {
				System.out.println("File es demasiado grande ṕara procesar");
				return null;
			}
			// Crear el arreglo de bytes para los datos
			byte[] bytes = new byte[(int)length];

			int offset = 0;
			int numRead = 0;
			while ( (offset < bytes.length)
					&&
					( (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) ) {

				offset += numRead;

			}
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
			return bytes;
		}finally{
			if(is != null){
				is.close();
			}
		}

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
	
	public Integer createTemplateProgramas(TipoDocumentosProcesos tipoDocumentoProceso,
			String nodeRef, String filename, String contenType, ProgramaVO programa) {
		long current = Calendar.getInstance().getTimeInMillis();
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		TipoDocumento tipoPlantilla = new TipoDocumento(tipoDocumentoProceso.getId());
		Plantilla plantilla = new Plantilla();
		plantilla.setDocumento(referenciaDocumento);
		plantilla.setTipoPlantilla(tipoPlantilla);
		plantilla.setFechaCreacion(new Date(current));
		plantilla.setFechaVigencia(null);
		
		Programa prog = new Programa();
		prog.setId(programa.getId());
		
		plantilla.setIdPrograma(prog);
		
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

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
	public List<ReferenciaDocumentoVO> getDocumentByTypesServicioDistribucionInicialPercapitaTransaccional(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		return getDocumentByTypesServicioDistribucionInicialPercapita(idDistribucionInicialPercapita, idServicio, tiposDocumentoProceso);
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

	public ReferenciaDocumentoSummaryVO getLastDocumentoSummaryByEstimacionFlujoCajaType(
			Integer idProgramaAno,
			TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = null;
		ReferenciaDocumento referenciaDocumento =  fileDAO.getLastDocumentByTypeEstimacionFlujoCaja(idProgramaAno, tipoDocumento);
		referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
		return referenciaDocumentoSummaryVO;
	}

	public ReferenciaDocumentoSummaryVO getLastDocumentoSummaryByEstimacionFlujoCajaTipoDocumento(TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = null;
		ReferenciaDocumento referenciaDocumento =  fileDAO.getLastDocumentByTipoDocumentoEstimacionFlujoCaja(tipoDocumento);
		referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
		return referenciaDocumentoSummaryVO;
	}

	public Integer crearDocumentoRebajaCalculada(Rebaja rebaja, TipoDocumentosProcesos tipoDocumentoProceso, String nodeRef,
			String fileName, String contenType) {

		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, fileName, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoRebaja documentoRebaja = new DocumentoRebaja();
		documentoRebaja.setTipoDocumento(new TipoDocumento(tipoDocumentoProceso.getId()));
		documentoRebaja.setDocumento(referenciaDocumento);
		documentoRebaja.setRebaja(rebaja);
		rebajaDAO.save(documentoRebaja);
		System.out.println("luego de aplicar insert del documento rebaja");
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

	public List<DocumentoEstimacionflujocaja> getDocumentEstimacionFlujoCajaByIDProgramaAnoTipoDocumento(
			ProgramaAno programaAno, TipoDocumento tipoDocumento) {
		return estimacionFlujoCajaDAO.getDocumentByIDProgramaAnoTipoDocumento(programaAno,tipoDocumento);

	}

	public void createDocumentOrdinarioProgramacionEstimacionFlujoCaja(
			ProgramaAno programaAno, TipoDocumentosProcesos tipoDocumento,
			Integer referenciaDocumentoId, boolean versionFinal) {

		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(versionFinal){
			referenciaDocumento.setDocumentoFinal(versionFinal);
		}
		DocumentoEstimacionflujocaja documentoEstimacionFlujoCaja = new DocumentoEstimacionflujocaja();


		documentoEstimacionFlujoCaja.setIdTipoDocumento(new TipoDocumento(tipoDocumento.getId()));
		documentoEstimacionFlujoCaja.setIdDocumento(referenciaDocumento);
		documentoEstimacionFlujoCaja.setIdProgramaAno(programaAno);
		estimacionFlujoCajaDAO.save(documentoEstimacionFlujoCaja);
		//distribucionInicialPercapitaDAO.save(documentoDistribucionInicialPercapita);
		System.out.println("luego de aplicar insert del documento percapita");

	}
	public Integer createDocumentPropuestaEstimacionFlujoCaja(ProgramaAno programaAno, TipoDocumento tipoDocumentoProceso,
			String nodeRef, String filename, String contenType) {



		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoEstimacionflujocaja documentoEstimacionFlujoCaja = new DocumentoEstimacionflujocaja();
		documentoEstimacionFlujoCaja.setIdProgramaAno(programaAno);
		documentoEstimacionFlujoCaja.setIdTipoDocumento(tipoDocumentoProceso);
		documentoEstimacionFlujoCaja.setIdDocumento(referenciaDocumento);
		estimacionFlujoCajaDAO.save(documentoEstimacionFlujoCaja);
		System.out.println("luego de aplicar insert del documento flujo caja");
		return referenciaDocumentoId;
	}

	public Integer createDocumentOrdinarioProgramacióndeCaja(TipoDocumento tipoDocumentoProceso,
			String nodeRef, String filename, String contenType) {


		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoEstimacionflujocaja documentoEstimacionFlujoCaja = new DocumentoEstimacionflujocaja();
		documentoEstimacionFlujoCaja.setIdProgramaAno(null);
		documentoEstimacionFlujoCaja.setIdTipoDocumento(tipoDocumentoProceso);
		documentoEstimacionFlujoCaja.setIdDocumento(referenciaDocumento);

		//documentoDistribucionInicialPercapita.setIdDocumento(referenciaDocumento);

		estimacionFlujoCajaDAO.save(documentoEstimacionFlujoCaja);
		System.out.println("luego de aplicar insert del documento percapita");
		return referenciaDocumentoId;
	}


	public Integer createDocumentPropuestaConsolidador(TipoDocumento tipoDocumentoProceso,
			String nodeRef, String filename, String contenType, Integer ano, Integer idMes ) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		AnoEnCurso anoEnCurso = new AnoEnCurso();
		anoEnCurso.setAno(ano);
		Mes mesEnCurso = new Mes();
		mesEnCurso.setIdMes(idMes);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoEstimacionflujocaja documentoEstimacionFlujoCaja = new DocumentoEstimacionflujocaja();
		documentoEstimacionFlujoCaja.setIdTipoDocumento(tipoDocumentoProceso);
		documentoEstimacionFlujoCaja.setIdDocumento(referenciaDocumento);
		documentoEstimacionFlujoCaja.setAno(anoEnCurso);
		documentoEstimacionFlujoCaja.setIdMes(mesEnCurso);
		//documentoDistribucionInicialPercapita.setIdDocumento(referenciaDocumento);

		estimacionFlujoCajaDAO.save(documentoEstimacionFlujoCaja);
		System.out.println("luego de aplicar insert del documento percapita");
		System.out.println("referenciaDocumentoId ---> "+referenciaDocumentoId);
		return referenciaDocumentoId;
	}

	public Integer createDocumentRebaja(Integer idProceso, Integer idComuna, TipoDocumentosProcesos tipoDocumentoProceso, String nodeRef,
			String filename, String contentType) {
		Rebaja Rebaja = rebajaDAO.findRebajaById(idProceso);
		Comuna comuna = servicioSaludDAO.getComunaById(idComuna);
		return createDocumentRebaja(Rebaja, comuna, tipoDocumentoProceso, nodeRef, filename, contentType);
	}

	private Integer createDocumentRebaja(Rebaja rebaja,Comuna comuna,
			TipoDocumentosProcesos tipoDocumentoProceso, String nodeRef,
			String filename, String contentType) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contentType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoRebaja documentoRebaja = new DocumentoRebaja();
		documentoRebaja.setTipoDocumento(new TipoDocumento(tipoDocumentoProceso.getId()));
		documentoRebaja.setComuna(comuna);
		documentoRebaja.setDocumento(referenciaDocumento);
		documentoRebaja.setRebaja(rebaja);
		rebajaDAO.save(documentoRebaja);
		System.out.println("luego de aplicar insert del documento rebaja");
		return referenciaDocumentoId;
	}

	public void createDocumentConvenio(ConvenioComuna convenio,TipoDocumentosProcesos tipoDocumento, Integer referenciaDocumentoId) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoConvenio documentoConvenio = new DocumentoConvenio();
		documentoConvenio.setConvenio(convenio);
		documentoConvenio.setDocumento(referenciaDocumento);
		fileDAO.save(documentoConvenio);
		System.out.println("luego de aplicar insert del documento convenio");
	}
	
	public Integer createDocumentReliquidacion(ProgramaAno programaAno, TipoDocumento tipoDocumentoProceso,
			String nodeRef, String filename, String contenType) {
		
		
		
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoEstimacionflujocaja documentoEstimacionFlujoCaja = new DocumentoEstimacionflujocaja();
		documentoEstimacionFlujoCaja.setIdProgramaAno(programaAno);
		documentoEstimacionFlujoCaja.setIdTipoDocumento(tipoDocumentoProceso);
		documentoEstimacionFlujoCaja.setIdDocumento(referenciaDocumento);
		estimacionFlujoCajaDAO.save(documentoEstimacionFlujoCaja);
		System.out.println("luego de aplicar insert del documento flujo caja");
		return referenciaDocumentoId;
	}
	
	public Integer createDocumentBaseReliquidacion(ProgramaAno programa, TipoDocumento tipoDocumentoProceso,
			String nodeRef, String filename, String contenType, Integer idReliquidacion, Mes mes) {
		
//		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
//		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
//		DocumentoEstimacionflujocaja documentoEstimacionFlujoCaja = new DocumentoEstimacionflujocaja();
//		documentoEstimacionFlujoCaja.setIdProgramaAno(programaAno);
//		documentoEstimacionFlujoCaja.setIdTipoDocumento(tipoDocumentoProceso);
//		documentoEstimacionFlujoCaja.setIdDocumento(referenciaDocumento);
		
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);	
		
		DocumentoReliquidacion documentoReliquidacion = new DocumentoReliquidacion();		
		documentoReliquidacion.setComuna(null);
		documentoReliquidacion.setServicio(null);
		
		
		//TODO agregar idProgramaAno a la tabla de TipoDocumento
		Reliquidacion reliquidacion = new Reliquidacion();
		reliquidacion.setIdReliquidacion(idReliquidacion);
		reliquidacion.setMes(mes);
		
		documentoReliquidacion.setIdReliquidacion(reliquidacion);
		documentoReliquidacion.setIdDocumento(referenciaDocumento);
		documentoReliquidacion.setIdTipoDocumento(tipoDocumentoProceso);
		reliquidacionDAO.save(documentoReliquidacion);
		return referenciaDocumentoId;
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
	
	public Integer getPlantillaByType(TipoDocumentosProcesos template, Integer idPrograma){
		return fileDAO.getPlantillaByType(template, idPrograma);
	}
	
	public Integer createTemplate(Programa programa, TipoDocumentosProcesos tipoDocumentoProceso,
			String nodeRef, String filename, String contenType) {
		long current = Calendar.getInstance().getTimeInMillis();
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		TipoDocumento tipoPlantilla = new TipoDocumento(tipoDocumentoProceso.getId());
		Plantilla plantilla = new Plantilla();
		plantilla.setDocumento(referenciaDocumento);
		plantilla.setTipoPlantilla(tipoPlantilla);
		plantilla.setFechaCreacion(new Date(current));
		plantilla.setIdPrograma(programa);
		plantilla.setFechaVigencia(null);
		fileDAO.save(plantilla);
		return referenciaDocumento.getId();
	}

	public Integer getPlantillaByTypeAndProgram(TipoDocumentosProcesos tipoDocumentoProceso,
			Integer programaSeleccionado) {
		return fileDAO.getPlantillaByTypeAndProgram(tipoDocumentoProceso,programaSeleccionado);
	}

	public void zipFolder(String srcFolder, String destZipFile) throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);

		addFolderToZip("", srcFolder, zip);
		zip.flush();
		zip.close();
	}

	private void addFileToZip(String path, String srcFile, ZipOutputStream zip)
			throws Exception {

		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			FileInputStream in = new FileInputStream(srcFile);
			try{
				byte[] buf = new byte[1024];
				int len;
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
				while ((len = in.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
			}finally{
				if(in != null){
					in.close();
				}
			}
		}
	}

	private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
			throws Exception {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}

}
