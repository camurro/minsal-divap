package minsal.divap.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.DocumentOtDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RebajaDAO;
import minsal.divap.dao.RecursosFinancierosProgramasReforzamientoDAO;
import minsal.divap.dao.ReliquidacionDAO;
import minsal.divap.dao.RemesasDAO;
import minsal.divap.dao.ReportesDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.model.mappers.ModificacionPercapitaReferenciaDocumentoMapper;
import minsal.divap.model.mappers.PercapitaReferenciaDocumentoMapper;
import minsal.divap.model.mappers.ReferenciaDocumentoMapper;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReferenciaDocumentoVO;
import minsal.divap.vo.ServiciosSummaryVO;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Convenio;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoConvenio;
import cl.minsal.divap.model.DocumentoConvenioComuna;
import cl.minsal.divap.model.DocumentoConvenioServicio;
import cl.minsal.divap.model.DocumentoDistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.DocumentoModificacionPercapita;
import cl.minsal.divap.model.DocumentoOt;
import cl.minsal.divap.model.DocumentoProgramasReforzamiento;
import cl.minsal.divap.model.DocumentoRebaja;
import cl.minsal.divap.model.DocumentoReliquidacion;
import cl.minsal.divap.model.DocumentoRemesas;
import cl.minsal.divap.model.DocumentoReportes;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ModificacionDistribucionInicialPercapita;
import cl.minsal.divap.model.OrdenTransferencia;
import cl.minsal.divap.model.Plantilla;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.Rebaja;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.Reliquidacion;
import cl.minsal.divap.model.ServicioSalud;
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
	private RemesasDAO remesasDAO;
	@EJB
	private EstimacionFlujoCajaDAO estimacionFlujoCajaDAO;
	@EJB
	private ReliquidacionDAO reliquidacionDAO;
	@EJB
	private RecursosFinancierosProgramasReforzamientoDAO programasReforzamientoDAO;
	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private ConveniosDAO conveniosDAO; 
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private ReportesDAO reportesDAO;
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
		System.out.println("Creando ARCHIVO " + fileName + " (" + contents.length + "bytes) en carpeta->"+folder);
		BufferedOutputStream bs = null;
		File fileFolder = new File(folder);
		File file = new File(folder + File.separator + fileName);
		try {
			boolean parentDirs = true;
			if (!fileFolder.exists()) {
				System.out.println("Creando carpeta padre");
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

	public DocumentoVO getDocument(String servicio, List<Integer> idDocumentos){
		DocumentoVO documentoVO = null;
		if(idDocumentos == null || idDocumentos.size() == 0){
			return null;
		}
		System.out.println("archivos para comprimir idDocumentos.size()-->"+idDocumentos.size());
		Calendar cal = Calendar.getInstance();
		Long millis = cal.getTimeInMillis();
		String tmpFolder = tmpDownloadDirectory + File.separator + millis.toString() + File.separator + servicio;
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
			documentoVO = new DocumentoVO(servicio + ".zip", contentType, getBytesFromFile(new File(destZipFile)));
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
	}
	
	public void createDocumentPercapita(DistribucionInicialPercapita distribucionInicialPercapita,Integer idServicio,
			TipoDocumentosProcesos tipoDocumento, Integer referenciaDocumentoId, Boolean lastVersion) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(lastVersion != null){
			referenciaDocumento.setDocumentoFinal(lastVersion);
		}
		DocumentoDistribucionInicialPercapita documentoDistribucionInicialPercapita = new DocumentoDistribucionInicialPercapita();
		if(idServicio != null){
			ServicioSalud servicio = servicioSaludDAO.getById(idServicio);
			documentoDistribucionInicialPercapita.setServicio(servicio);
		}
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

	public Integer createDocumentProgramasReforzamiento(TipoDocumentosProcesos tipoDocumentoProceso,
			String nodeRef, String filename, String contenType, Integer idProgramaAno) {


		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);

		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);

		DocumentoProgramasReforzamiento documentoProgramasReforzamiento = new DocumentoProgramasReforzamiento();
		documentoProgramasReforzamiento.setIdProgramaAno(programaAno);
		documentoProgramasReforzamiento.setIdTipoDocumento(new TipoDocumento(tipoDocumentoProceso.getId()));
		documentoProgramasReforzamiento.setIdDocumento(referenciaDocumento);

		//documentoDistribucionInicialPercapita.setIdDocumento(referenciaDocumento);

		programasReforzamientoDAO.save(documentoProgramasReforzamiento);
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

	public void createDocumentConvenioComuna(ConvenioComuna convenio,TipoDocumentosProcesos tipoDocumento, Integer referenciaDocumentoId) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoConvenioComuna documentoConvenioComuna = new DocumentoConvenioComuna();
		documentoConvenioComuna.setConvenio(convenio);
		documentoConvenioComuna.setDocumento(referenciaDocumento);
		fileDAO.save(documentoConvenioComuna);
		System.out.println("luego de aplicar insert del documento convenio comuna");
	}

	public void createDocumentConvenioServicio(ConvenioServicio convenio,TipoDocumentosProcesos tipoDocumento, Integer referenciaDocumentoId) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoConvenioServicio documentoConvenioServicio = new DocumentoConvenioServicio();
		documentoConvenioServicio.setConvenio(convenio);
		documentoConvenioServicio.setDocumento(referenciaDocumento);
		fileDAO.save(documentoConvenioServicio);
		System.out.println("luego de aplicar insert del documento convenio servicio");
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

	public Integer createDocumentBaseReliquidacion(TipoDocumentosProcesos tipoDocumento,
			String nodeRef, String filename, String contenType, Integer idReliquidacion) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		TipoDocumento tipoDocumentoProceso = new TipoDocumento(tipoDocumento.getId());
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);	
		DocumentoReliquidacion documentoReliquidacion = new DocumentoReliquidacion();
		Reliquidacion reliquidacion = reliquidacionDAO.getReliquidacionById(idReliquidacion);
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

	public ReferenciaDocumentoSummaryVO getDocumentByTypeAnoReportes(TipoDocumentosProcesos tipoDocumentoProceso, Integer ano) {
		return new ReferenciaDocumentoMapper().getSummary(fileDAO.getDocumentByTypeAnoReportes(tipoDocumentoProceso, ano));
	}
	public ReferenciaDocumentoSummaryVO getLastDocumentoSummaryByResolucionAPSType(
			Integer idProgramaAno,
			TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = null;
		ReferenciaDocumento referenciaDocumento =  fileDAO.getLastDocumentoSummaryByResolucionAPSType(idProgramaAno, tipoDocumento);
		referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
		return referenciaDocumentoSummaryVO;
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
			System.out.println("addFolderToZip fileName-->"+fileName);
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}
	
	
	
	public Integer createDocumentReportes(TipoDocumento tipoDocumentoProceso,
			String nodeRef, String filename, String contenType, Integer ano, Integer idMes ) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		AnoEnCurso anoEnCurso = new AnoEnCurso();
		anoEnCurso.setAno(ano);
		Mes mesEnCurso = new Mes();
		mesEnCurso.setIdMes(idMes);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoReportes documentoReportes = new DocumentoReportes();
		documentoReportes.setAno(anoEnCurso);
		documentoReportes.setTipoDocumento(tipoDocumentoProceso);
		documentoReportes.setDocumento(referenciaDocumento);
		reportesDAO.save(documentoReportes);
		System.out.println("referenciaDocumentoId ---> "+referenciaDocumentoId);
		return referenciaDocumentoId;
	}

	public Integer createDocumentReportePoblacionPercapita(TipoDocumento tipoDocumentoProceso,
			String nodeRef, String filename, String contenType, Integer ano, Integer idMes ) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		AnoEnCurso anoEnCurso = new AnoEnCurso();
		anoEnCurso.setAno(ano);
		Mes mesEnCurso = new Mes();
		mesEnCurso.setIdMes(idMes);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoReportes documentoReportes = new DocumentoReportes();
		documentoReportes.setAno(anoEnCurso);
		documentoReportes.setTipoDocumento(tipoDocumentoProceso);
		documentoReportes.setDocumento(referenciaDocumento);
		reportesDAO.save(documentoReportes);
		System.out.println("referenciaDocumentoId ---> "+referenciaDocumentoId);
		return referenciaDocumentoId;
	}

	public Integer createDocumentReporteRebaja(TipoDocumento tipoDocumentoProceso,
			String nodeRef, String filename, String contenType, Integer ano, Integer idMes ) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		AnoEnCurso anoEnCurso = new AnoEnCurso();
		anoEnCurso.setAno(ano);
		Mes mesEnCurso = new Mes();
		mesEnCurso.setIdMes(idMes);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoReportes documentoReportes = new DocumentoReportes();
		documentoReportes.setAno(anoEnCurso);
		documentoReportes.setTipoDocumento(tipoDocumentoProceso);
		documentoReportes.setDocumento(referenciaDocumento);
		reportesDAO.save(documentoReportes);
		System.out.println("referenciaDocumentoId ---> "+referenciaDocumentoId);
		return referenciaDocumentoId;
	}
	

	public void createDocumentReliquidacion(Reliquidacion reliquidacion, TipoDocumentosProcesos tipoDocumento, Integer referenciaDocumentoId, Boolean lastVersion) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(lastVersion != null){
			referenciaDocumento.setDocumentoFinal(lastVersion);
		}
		DocumentoReliquidacion documentoReliquidacion = new DocumentoReliquidacion();
		documentoReliquidacion.setIdTipoDocumento(new TipoDocumento(tipoDocumento.getId()));
		documentoReliquidacion.setIdDocumento(referenciaDocumento);
		documentoReliquidacion.setIdReliquidacion(reliquidacion);
		reliquidacionDAO.save(documentoReliquidacion);
		System.out.println("luego de aplicar insert del documento reliquidacion");
	}


	public Integer createDocumentProgramasReforzamiento(TipoDocumentosProcesos tipoDocumentoProceso, String nodeRef,
			String filename, String contentType, Integer idProxAno,
			Integer idServicio) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contentType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProxAno);
		DocumentoProgramasReforzamiento documentoProgramasReforzamiento = new DocumentoProgramasReforzamiento();
		documentoProgramasReforzamiento.setIdProgramaAno(programaAno);
		documentoProgramasReforzamiento.setIdTipoDocumento(new TipoDocumento(tipoDocumentoProceso.getId()));
		documentoProgramasReforzamiento.setIdDocumento(referenciaDocumento);
		documentoProgramasReforzamiento.setIdServicio(servicioSaludDAO.getById(idServicio));
		programasReforzamientoDAO.save(documentoProgramasReforzamiento);
		return referenciaDocumentoId;
	}

	public Integer getIdDocumentoFromPlantilla(
			TipoDocumentosProcesos plantilla) {
		Integer plantillaId = getPlantillaByType(plantilla);
		return getDocumentoIdByPlantillaId(plantillaId);
	}

	public Integer createDocumentConvenio(Integer idConvenio, Integer idServicio, TipoDocumentosProcesos tipoDocumentoProceso, String nodeRef,
			String filename, String contentType) {
		Convenio convenio = conveniosDAO.findById(idConvenio);
		ServicioSalud servicio = null;
		if(idServicio != null){
			servicio = servicioSaludDAO.getServicioSaludById(idServicio);
		}
		return createDocumentConvenio(convenio, servicio, tipoDocumentoProceso, nodeRef, filename, contentType, null);
	}

	public Integer createDocumentConvenio(Convenio convenio, ServicioSalud servicio, 
			TipoDocumentosProcesos tipoDocumento, String nodeRef,
			String filename, String contentType, Boolean lastVersion) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contentType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(lastVersion != null){
			referenciaDocumento.setDocumentoFinal(lastVersion);
		}
		DocumentoConvenio documentoConvenio = new DocumentoConvenio();
		documentoConvenio.setTipoDocumento(new TipoDocumento(tipoDocumento.getId()));
		documentoConvenio.setDocumento(referenciaDocumento);
		documentoConvenio.setConvenio(convenio);
		documentoConvenio.setServicio(servicio);
		conveniosDAO.save(documentoConvenio);
		System.out.println("luego de aplicar insert del documento convenio");
		return referenciaDocumentoId;
	}

	public void createDocumentConvenio(Convenio convenio, TipoDocumentosProcesos tipoDocumento, Integer referenciaDocumentoId, Boolean lastVersion) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(lastVersion != null){
			referenciaDocumento.setDocumentoFinal(lastVersion);
		}
		DocumentoConvenio documentoConvenio = new DocumentoConvenio();
		documentoConvenio.setTipoDocumento(new TipoDocumento(tipoDocumento.getId()));
		documentoConvenio.setDocumento(referenciaDocumento);
		documentoConvenio.setConvenio(convenio);
		conveniosDAO.save(documentoConvenio);
		System.out.println("luego de aplicar insert del documento convenio");
	}

	public ReferenciaDocumentoSummaryVO getLastDocumentoSummaryConvenioByTypeType(Integer idConvenio, TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumento referenciaDocumento =  fileDAO.getLastDocumentByTypeConvenio(idConvenio, tipoDocumento);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
		return referenciaDocumentoSummaryVO;
	}

	public List<ReferenciaDocumentoSummaryVO> getVersionFinalConvenioByType(Integer idConvenio, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = new ArrayList<ReferenciaDocumentoSummaryVO>();
		List<ReferenciaDocumento> referenciaDocumentos =  fileDAO.getVersionFinalConvenioByType(idConvenio, tipoDocumento);
		if(referenciaDocumentos != null && referenciaDocumentos.size() > 0){
			for(ReferenciaDocumento referenciaDocumento : referenciaDocumentos){
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
				versionesFinales.add(referenciaDocumentoSummaryVO);
			}
		}
		return versionesFinales;
	}

	public void createDocumentModificacionPercapita(ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento, Integer referenciaDocumentoId, Boolean lastVersion) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(lastVersion != null){
			referenciaDocumento.setDocumentoFinal(lastVersion);
		}
		DocumentoModificacionPercapita documentoModificacionPercapita = new DocumentoModificacionPercapita();
		documentoModificacionPercapita.setTipoDocumento(new TipoDocumento(tipoDocumento.getId()));
		documentoModificacionPercapita.setDocumento(referenciaDocumento);
		documentoModificacionPercapita.setModificacionPercapita(modificacionDistribucionInicialPercapita);
		conveniosDAO.save(documentoModificacionPercapita);
		System.out.println("luego de aplicar insert del documento convenio");
	}
	
	public void createDocumentModificacionPercapita(ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos tipoDocumento, Integer referenciaDocumentoId, Boolean lastVersion) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(lastVersion != null){
			referenciaDocumento.setDocumentoFinal(lastVersion);
		}
		DocumentoModificacionPercapita documentoModificacionPercapita = new DocumentoModificacionPercapita();
		documentoModificacionPercapita.setTipoDocumento(new TipoDocumento(tipoDocumento.getId()));
		documentoModificacionPercapita.setDocumento(referenciaDocumento);
		documentoModificacionPercapita.setModificacionPercapita(modificacionDistribucionInicialPercapita);
		if(idServicio != null){
			ServicioSalud servicio = servicioSaludDAO.getServicioSaludById(idServicio);
			documentoModificacionPercapita.setServicio(servicio);
		}
		conveniosDAO.save(documentoModificacionPercapita);
		System.out.println("luego de aplicar insert del documento convenio");
	}

	public Integer createDocumentModificacionPercapita(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos tipoDocumentoProceso, String nodeRef,
			String filename, String contentType) {
		ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita = distribucionInicialPercapitaDAO.findModificacionDistribucionInicialById(idDistribucionInicialPercapita);
		ServicioSalud servicio = null;
		if(idServicio != null){
			servicio = servicioSaludDAO.getServicioSaludById(idServicio);
		}
		return createDocumentModificacionPercapita(modificacionDistribucionInicialPercapita, servicio, tipoDocumentoProceso, nodeRef, filename, contentType, null);
	}

	public Integer createDocumentModificacionPercapita(ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita, ServicioSalud servicio, 
			TipoDocumentosProcesos tipoDocumento, String nodeRef,
			String filename, String contentType, Boolean lastVersion) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contentType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(lastVersion != null){
			referenciaDocumento.setDocumentoFinal(lastVersion);
		}
		DocumentoModificacionPercapita documentoModificacionPercapita = new DocumentoModificacionPercapita();
		documentoModificacionPercapita.setTipoDocumento(new TipoDocumento(tipoDocumento.getId()));
		documentoModificacionPercapita.setDocumento(referenciaDocumento);
		documentoModificacionPercapita.setModificacionPercapita(modificacionDistribucionInicialPercapita);
		documentoModificacionPercapita.setServicio(servicio);
		distribucionInicialPercapitaDAO.save(documentoModificacionPercapita);
		System.out.println("luego de aplicar insert del documento modificacionDistribucionInicialPercapita");
		return referenciaDocumentoId;
	}

	public Integer createDocumentModificacionPercapita(ModificacionDistribucionInicialPercapita modificacionPercapita, TipoDocumentosProcesos tipoDocumentoProceso,
			String nodeRef, String filename, String contenType) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoModificacionPercapita documentoModificacionPercapita = new DocumentoModificacionPercapita();
		documentoModificacionPercapita.setTipoDocumento(new TipoDocumento(tipoDocumentoProceso.getId()));
		documentoModificacionPercapita.setDocumento(referenciaDocumento);
		documentoModificacionPercapita.setModificacionPercapita(modificacionPercapita);
		distribucionInicialPercapitaDAO.save(documentoModificacionPercapita);
		System.out.println("luego de aplicar insert del documento modificacion percapita");
		return referenciaDocumentoId;
	}

	public Integer createDocumentModificacionPercapita(ModificacionDistribucionInicialPercapita modificacionPercapita, Integer idComuna, TipoDocumentosProcesos tipoDocumento,
			String nodeRef, String fileName, String contentType) {
		Comuna comuna = antecedentesComunaDAO.findByComunaById(idComuna);
		return createDocumentModificacionPercapita(modificacionPercapita, comuna, tipoDocumento, nodeRef, fileName, contentType );
	}

	private Integer createDocumentModificacionPercapita(
			ModificacionDistribucionInicialPercapita modificacionPercapita,
			Comuna comuna, TipoDocumentosProcesos tipoDocumentoProceso,
			String nodeRef, String filename, String contenType) {
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, filename, contenType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		DocumentoModificacionPercapita documentoModificacionPercapita = new DocumentoModificacionPercapita();
		documentoModificacionPercapita.setTipoDocumento(new TipoDocumento(tipoDocumentoProceso.getId()));
		documentoModificacionPercapita.setDocumento(referenciaDocumento);
		documentoModificacionPercapita.setComuna(comuna);
		documentoModificacionPercapita.setModificacionPercapita(modificacionPercapita);
		distribucionInicialPercapitaDAO.save(documentoModificacionPercapita);
		System.out.println("luego de aplicar insert del documento modificacion percapita");
		return referenciaDocumentoId;
	}

	public List<ReferenciaDocumentoSummaryVO> getVersionFinalModificacionDistribucionInicialByType(Integer idModificacionPercapita, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = new ArrayList<ReferenciaDocumentoSummaryVO>();
		List<ReferenciaDocumento> referenciaDocumentos =  fileDAO.getVersionFinalModificacionDistribucionInicialByType(idModificacionPercapita, tipoDocumento);
		if(referenciaDocumentos != null && referenciaDocumentos.size() > 0){
			for(ReferenciaDocumento referenciaDocumento : referenciaDocumentos){
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
				versionesFinales.add(referenciaDocumentoSummaryVO);
			}
		}
		return versionesFinales;
	}

	public ReferenciaDocumentoSummaryVO getLastDocumentSummaryModificacionPercapitaByType(Integer idModificacionDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumento referenciaDocumento =  fileDAO.getLastDocumentSummaryModificacionPercapitaByType(idModificacionDistribucionInicialPercapita, tipoDocumento);
		return new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
	}

	public List<ReferenciaDocumentoSummaryVO> getVersionFinalModificacionPercapitaByType(Integer idDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = new ArrayList<ReferenciaDocumentoSummaryVO>();
		List<ReferenciaDocumento> referenciaDocumentos =  fileDAO.getVersionFinalModificacionPercapitaByType(idDistribucionInicialPercapita, tipoDocumento);
		if(referenciaDocumentos != null && referenciaDocumentos.size() > 0){
			for(ReferenciaDocumento referenciaDocumento : referenciaDocumentos){
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
				versionesFinales.add(referenciaDocumentoSummaryVO);
			}
		}
		return versionesFinales;
	}

	public List<ReferenciaDocumentoVO> getDocumentByTypesServicioModificacionPercapita(Integer idModificacionPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		System.out.println("idDistribucionInicialPercapita->"+idModificacionPercapita);
		for(TipoDocumentosProcesos tipoDocumentoProceso : tiposDocumentoProceso){
			System.out.println("tipoDocumentoProceso->"+tipoDocumentoProceso.getId());
		}
		System.out.println("idServicio->"+idServicio);
		List<ReferenciaDocumentoVO> referenciasDocumentoVO = new ArrayList<ReferenciaDocumentoVO>();
		List<DocumentoModificacionPercapita> referencias = fileDAO.getDocumentosByTypeServicioModificacionPercapita(idModificacionPercapita, idServicio, tiposDocumentoProceso);
		if(referencias != null && referencias.size() > 0){
			for(DocumentoModificacionPercapita referencia : referencias){
				referenciasDocumentoVO.add(new ModificacionPercapitaReferenciaDocumentoMapper().getBasic(referencia));
			}
		}
		return referenciasDocumentoVO;
	}

	public List<ServiciosSummaryVO> getDocumentResolucionByTypesServicioModificacionPercapita(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		System.out.println("idDistribucionInicialPercapita->"+idDistribucionInicialPercapita);
		for(TipoDocumentosProcesos tipoDocumentoProceso : tiposDocumentoProceso){
			System.out.println("tipoDocumentoProceso->"+tipoDocumentoProceso.getId());
		}
		System.out.println("idServicio->"+idServicio);
		List<ServiciosSummaryVO> serviciosResoluciones = new ArrayList<ServiciosSummaryVO>();
		List<DocumentoModificacionPercapita> referencias = fileDAO.getDocumentosByTypeServicioModificacionPercapita(idDistribucionInicialPercapita, idServicio, tiposDocumentoProceso);
		if(referencias != null && referencias.size() > 0){
			for(DocumentoModificacionPercapita referencia : referencias){
				if(referencia.getComuna() != null && referencia.getComuna().getServicioSalud() != null){
					ServiciosSummaryVO serviciosSummaryVO = new ServiciosSummaryVO();
					serviciosSummaryVO.setId_servicio(referencia.getComuna().getServicioSalud().getId());
					serviciosSummaryVO.setNombre_servicio(referencia.getComuna().getServicioSalud().getNombre());
					if(!serviciosResoluciones.contains(serviciosSummaryVO)){
						serviciosResoluciones.add(serviciosSummaryVO);
					}
				}
			}
		}
		return serviciosResoluciones;
	}

	public List<Integer> getDocumentosByModificacionPercapitaServicioTypes(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		System.out.println("idDistribucionInicialPercapita->"+idDistribucionInicialPercapita);
		for(TipoDocumentosProcesos tipoDocumentoProceso : tiposDocumentoProceso){
			System.out.println("tipoDocumentoProceso->"+tipoDocumentoProceso.getId());
		}
		System.out.println("idServicio->"+idServicio);
		List<Integer> documentos = new ArrayList<Integer>();
		DocumentoModificacionPercapita referencia = fileDAO.getLastDocumentosByTypeServicioModificacionPercapita(idDistribucionInicialPercapita, idServicio, tiposDocumentoProceso);
		if(referencia == null){
			List<DocumentoModificacionPercapita> referencias = fileDAO.getDocumentosByTypeServicioModificacionPercapita(idDistribucionInicialPercapita, idServicio, tiposDocumentoProceso);
			if(referencias != null && referencias.size() > 0){
				for(DocumentoModificacionPercapita referenciaComuna : referencias){
					documentos.add(referenciaComuna.getDocumento().getId());
				}
			}
		}else{
			documentos.add(referencia.getDocumento().getId());
		}
		return documentos;
	}
	public Integer createDocumentRemesas(
			TipoDocumentosProcesos tipoDocumentoProceso,
			String nodeRef, String fileName, String contentType,
			String idProcesoOT) {
		
		Integer referenciaDocumentoId = createDocumentAlfresco(nodeRef, fileName, contentType);
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		
		DocumentoRemesas documentoRemesas = new DocumentoRemesas();
		documentoRemesas.setTipoDocumento(new TipoDocumento(tipoDocumentoProceso.getId()));
		documentoRemesas.setDocumento(referenciaDocumento);
		documentoRemesas.setRemesa(remesasDAO.findById(Integer.parseInt(idProcesoOT)));
		
		remesasDAO.save(documentoRemesas);
		System.out.println("luego de aplicar insert del documento OT");
		return referenciaDocumentoId;
	}
	
	public List<Integer> getDocumentosByDistribucionInicialPercapitaServicioTypes(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		System.out.println("idDistribucionInicialPercapita->"+idDistribucionInicialPercapita);
		for(TipoDocumentosProcesos tipoDocumentoProceso : tiposDocumentoProceso){
			System.out.println("tipoDocumentoProceso->"+tipoDocumentoProceso.getId());
		}
		System.out.println("idServicio->"+idServicio);
		List<Integer> documentos = new ArrayList<Integer>();
		DocumentoDistribucionInicialPercapita referencia = fileDAO.getLastDocumentosByTypeServicioDistribucionInicialPercapita(idDistribucionInicialPercapita, idServicio, tiposDocumentoProceso);
		if(referencia == null){
			List<DocumentoDistribucionInicialPercapita> referencias = fileDAO.getDocumentosByTypeServicioDistribucionInicialPercapita(idDistribucionInicialPercapita, idServicio, tiposDocumentoProceso);
			if(referencias != null && referencias.size() > 0){
				for(DocumentoDistribucionInicialPercapita referenciaComuna : referencias){
					documentos.add(referenciaComuna.getIdDocumento().getId());
				}
			}
		}else{
			documentos.add(referencia.getIdDocumento().getId());
		}
		return documentos;
	}

	public List<ServiciosSummaryVO> getDocumentByResolucionTypesServicioDistribucionInicialPercapita(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos[] tiposDocumentoProceso) {
		System.out.println("idDistribucionInicialPercapita->"+idDistribucionInicialPercapita);
		for(TipoDocumentosProcesos tipoDocumentoProceso : tiposDocumentoProceso){
			System.out.println("tipoDocumentoProceso->"+tipoDocumentoProceso.getId());
		}
		System.out.println("idServicio->"+idServicio);
		List<ServiciosSummaryVO> serviciosResoluciones = new ArrayList<ServiciosSummaryVO>();
		List<DocumentoDistribucionInicialPercapita> referencias = fileDAO.getDocumentosByTypeServicioDistribucionInicialPercapita(idDistribucionInicialPercapita, idServicio, tiposDocumentoProceso);
		if(referencias != null && referencias.size() > 0){
			for(DocumentoDistribucionInicialPercapita referencia : referencias){
				if(referencia.getComuna() != null && referencia.getComuna().getServicioSalud() != null){
					ServiciosSummaryVO serviciosSummaryVO = new ServiciosSummaryVO();
					serviciosSummaryVO.setId_servicio(referencia.getComuna().getServicioSalud().getId());
					serviciosSummaryVO.setNombre_servicio(referencia.getComuna().getServicioSalud().getNombre());
					if(!serviciosResoluciones.contains(serviciosSummaryVO)){
						serviciosResoluciones.add(serviciosSummaryVO);
					}
				}
			}
		}
		return serviciosResoluciones;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
	public List<ReferenciaDocumentoVO> getDocumentByTypesServicioModificacionPercapitaTransaccional(Integer idModificacionPercapita, Integer idServicio, TipoDocumentosProcesos... tipoDocumento) {
		return getDocumentByTypesServicioModificacionPercapita(idModificacionPercapita, idServicio, tipoDocumento);
	}

	public List<ReferenciaDocumentoSummaryVO> getVersionFinalByServicioModificacionPercapitaTypes(Integer idModificacionPercapita, Integer idServicio, TipoDocumentosProcesos... tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = new ArrayList<ReferenciaDocumentoSummaryVO>();
		List<ReferenciaDocumento> referenciaDocumentos =  fileDAO.getVersionFinalByServicioModificacionPercapitaTypes(idModificacionPercapita, idServicio, tipoDocumento);
		if(referenciaDocumentos != null && referenciaDocumentos.size() > 0){
			for(ReferenciaDocumento referenciaDocumento : referenciaDocumentos){
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
				versionesFinales.add(referenciaDocumentoSummaryVO);
			}
		}
		return versionesFinales;
	}

	public List<ReferenciaDocumentoSummaryVO> getVersionFinalByServicioDistribucionInicialPercapitaTypes(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = new ArrayList<ReferenciaDocumentoSummaryVO>();
		List<ReferenciaDocumento> referenciaDocumentos =  fileDAO.getVersionFinalByServicioDistribucionInicialPercapitaTypes(idDistribucionInicialPercapita, idServicio, tipoDocumento);
		if(referenciaDocumentos != null && referenciaDocumentos.size() > 0){
			for(ReferenciaDocumento referenciaDocumento : referenciaDocumentos){
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
				versionesFinales.add(referenciaDocumentoSummaryVO);
			}
		}
		return versionesFinales;
	}

	public List<ReferenciaDocumentoSummaryVO> getVersionFinalModificacionPercapitaByType(Integer idModificacionPercapita, Integer idServicio, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = new ArrayList<ReferenciaDocumentoSummaryVO>();
		List<ReferenciaDocumento> referenciaDocumentos =  fileDAO.getVersionFinalModificacionPercapitaByType(idModificacionPercapita, idServicio, tipoDocumento);
		if(referenciaDocumentos != null && referenciaDocumentos.size() > 0){
			for(ReferenciaDocumento referenciaDocumento : referenciaDocumentos){
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
				versionesFinales.add(referenciaDocumentoSummaryVO);
			}
		}
		return versionesFinales;
	}

	public List<ReferenciaDocumentoSummaryVO> getVersionFinalDistribucionInicialPercapitaByType(Integer idDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = new ArrayList<ReferenciaDocumentoSummaryVO>();
		List<ReferenciaDocumento> referenciaDocumentos =  fileDAO.getVersionFinalDistribucionInicialPercapitaByType(idDistribucionInicialPercapita, tipoDocumento);
		if(referenciaDocumentos != null && referenciaDocumentos.size() > 0){
			for(ReferenciaDocumento referenciaDocumento : referenciaDocumentos){
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
				versionesFinales.add(referenciaDocumentoSummaryVO);
			}
		}
		return versionesFinales;
	}

	public List<ReferenciaDocumentoSummaryVO> getVersionFinalDistribucionInicialPercapitaByType(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = new ArrayList<ReferenciaDocumentoSummaryVO>();
		List<ReferenciaDocumento> referenciaDocumentos =  fileDAO.getVersionFinalDistribucionInicialPercapitaByType(idDistribucionInicialPercapita, idServicio, tipoDocumento);
		if(referenciaDocumentos != null && referenciaDocumentos.size() > 0){
			for(ReferenciaDocumento referenciaDocumento : referenciaDocumentos){
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
				versionesFinales.add(referenciaDocumentoSummaryVO);
			}
		}
		return versionesFinales;
	}

	public List<ServiciosSummaryVO> getDocumentResolucionByTypesServicioDistribucionInicialPercapita(Integer idDistribucionInicialPercapita, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		System.out.println("idDistribucionInicialPercapita->"+idDistribucionInicialPercapita);
		for(TipoDocumentosProcesos tipoDocumentoProceso : tiposDocumentoProceso){
			System.out.println("tipoDocumentoProceso->"+tipoDocumentoProceso.getId());
		}
		System.out.println("idServicio->"+idServicio);
		List<ServiciosSummaryVO> serviciosResoluciones = new ArrayList<ServiciosSummaryVO>();
		List<DocumentoDistribucionInicialPercapita> referencias = fileDAO.getDocumentosByTypeServicioDistribucionInicialPercapita(idDistribucionInicialPercapita, idServicio, tiposDocumentoProceso);
		if(referencias != null && referencias.size() > 0){
			for(DocumentoDistribucionInicialPercapita referencia : referencias){
				if(referencia.getComuna() != null && referencia.getComuna().getServicioSalud() != null){
					ServiciosSummaryVO serviciosSummaryVO = new ServiciosSummaryVO();
					serviciosSummaryVO.setId_servicio(referencia.getComuna().getServicioSalud().getId());
					serviciosSummaryVO.setNombre_servicio(referencia.getComuna().getServicioSalud().getNombre());
					if(!serviciosResoluciones.contains(serviciosSummaryVO)){
						serviciosResoluciones.add(serviciosSummaryVO);
					}
				}
			}
		}
		return serviciosResoluciones;
	}
	
	public List<Integer> getDocumentosByRebajaServicioTypes(Integer idRebaja, Integer idServicio, TipoDocumentosProcesos... tiposDocumentoProceso) {
		System.out.println("idRebaja->"+idRebaja);
		for(TipoDocumentosProcesos tipoDocumentoProceso : tiposDocumentoProceso){
			System.out.println("tipoDocumentoProceso->"+tipoDocumentoProceso.getId());
		}
		System.out.println("idServicio->"+idServicio);
		List<Integer> documentos = new ArrayList<Integer>();
		DocumentoRebaja referencia = fileDAO.getLastDocumentosByTypeServicioRebaja(idRebaja, idServicio, tiposDocumentoProceso);
		if(referencia == null){
			List<DocumentoRebaja> referencias = fileDAO.getDocumentosByTypeServicioRebaja(idRebaja, idServicio, tiposDocumentoProceso);
			if(referencias != null && referencias.size() > 0){
				for(DocumentoRebaja referenciaComuna : referencias){
					documentos.add(referenciaComuna.getDocumento().getId());
				}
			}
		}else{
			documentos.add(referencia.getDocumento().getId());
		}
		return documentos;
	}

	public void createDocumentRebaja(Rebaja rebaja, Integer idServicio,	TipoDocumentosProcesos tipoDocumento, Integer referenciaDocumentoId, Boolean lastVersion) {
		ReferenciaDocumento referenciaDocumento = fileDAO.findById(referenciaDocumentoId);
		if(lastVersion != null){
			referenciaDocumento.setDocumentoFinal(lastVersion);
		}
		DocumentoRebaja documentoRebaja = new DocumentoRebaja();
		if(idServicio != null){
			ServicioSalud servicio = servicioSaludDAO.getById(idServicio);
			documentoRebaja.setServicio(servicio);
		}
		documentoRebaja.setTipoDocumento(new TipoDocumento(tipoDocumento.getId()));
		documentoRebaja.setDocumento(referenciaDocumento);
		documentoRebaja.setRebaja(rebaja);
		rebajaDAO.save(documentoRebaja);
		System.out.println("luego de aplicar insert del documento rebaja");
	}

	public List<ServiciosSummaryVO> getDocumentByResolucionTypesServicioRebaja(Integer idProcesoRebaja, Integer idServicio, TipoDocumentosProcesos ... tiposDocumentoProceso) {
		System.out.println("idProcesoRebaja->"+idProcesoRebaja);
		for(TipoDocumentosProcesos tipoDocumentoProceso : tiposDocumentoProceso){
			System.out.println("tipoDocumentoProceso->"+tipoDocumentoProceso.getId());
		}
		System.out.println("idServicio->"+idServicio);
		List<ServiciosSummaryVO> serviciosResoluciones = new ArrayList<ServiciosSummaryVO>();
		List<DocumentoRebaja> referencias = fileDAO.getDocumentosByTypeServicioRebaja(idProcesoRebaja, idServicio, tiposDocumentoProceso);
		if(referencias != null && referencias.size() > 0){
			for(DocumentoRebaja referencia : referencias){
				if(referencia.getComuna() != null && referencia.getComuna().getServicioSalud() != null){
					ServiciosSummaryVO serviciosSummaryVO = new ServiciosSummaryVO();
					serviciosSummaryVO.setId_servicio(referencia.getComuna().getServicioSalud().getId());
					serviciosSummaryVO.setNombre_servicio(referencia.getComuna().getServicioSalud().getNombre());
					if(!serviciosResoluciones.contains(serviciosSummaryVO)){
						serviciosResoluciones.add(serviciosSummaryVO);
					}
				}
			}
		}
		return serviciosResoluciones;
	}

	public List<ReferenciaDocumentoSummaryVO> getVersionFinalRebajaByType(Integer idProcesoRebaja, Integer idServicio, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = new ArrayList<ReferenciaDocumentoSummaryVO>();
		List<ReferenciaDocumento> referenciaDocumentos =  fileDAO.getVersionFinalRebajaByType(idProcesoRebaja, idServicio, tipoDocumento);
		if(referenciaDocumentos != null && referenciaDocumentos.size() > 0){
			for(ReferenciaDocumento referenciaDocumento : referenciaDocumentos){
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
				versionesFinales.add(referenciaDocumentoSummaryVO);
			}
		}
		return versionesFinales;
	}

	public List<ReferenciaDocumentoSummaryVO> getVersionFinalRebajaByType( Integer idProcesoRebaja, Integer idServicio, TipoDocumentosProcesos ... tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = new ArrayList<ReferenciaDocumentoSummaryVO>();
		List<ReferenciaDocumento> referenciaDocumentos =  fileDAO.getVersionFinalByServicioRebajaTypes(idProcesoRebaja, idServicio, tipoDocumento);
		if(referenciaDocumentos != null && referenciaDocumentos.size() > 0){
			for(ReferenciaDocumento referenciaDocumento : referenciaDocumentos){
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = new ReferenciaDocumentoMapper().getSummary(referenciaDocumento);
				versionesFinales.add(referenciaDocumentoSummaryVO);
			}
		}
		return versionesFinales;
	}

}
