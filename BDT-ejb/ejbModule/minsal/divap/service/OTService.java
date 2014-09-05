package minsal.divap.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.MesDAO;
import minsal.divap.dao.OrdenTransferenciaDAO;
import minsal.divap.dao.OtSeguimientoDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RemesaDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.TratamientoOrdenDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.doc.GeneradorWordOrdinarioOrdenTransferencia;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionRecursosPercapitaSheetExcel;
import minsal.divap.excel.impl.OTResumenOrdenFormatoFonasaSheetExcel;
import minsal.divap.excel.interfaces.ExcelDinamicoVO;
import minsal.divap.model.mappers.ServicioSaludMapper;
import minsal.divap.util.Util;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.OTResumenConsolidadoFonasaProgramasVO;
import minsal.divap.vo.OTResumenConsolidadoFonasaVO;
import minsal.divap.vo.OTResumenTotalVO;
import minsal.divap.vo.OTRevisarAntecedentesVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cl.minsal.divap.model.DocumentoOt;
import cl.minsal.divap.model.OrdenTransferencia;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaServicioCore;
import cl.minsal.divap.model.Remesa;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.TipoDocumento;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class OTService {
	@EJB
	private OtSeguimientoDAO otSeguimientoDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private TratamientoOrdenDAO tratamientoOrdenDAO;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private DocumentService documentService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private SeguimientoService seguimientoService;
	@EJB
	private EmailService emailService;
	@EJB
	private MesDAO mesDAO;
	@EJB
	private ProgramasDAO programaDAO;
	@EJB
	private TipoSubtituloDAO tipoSubtituloDAO;
	
	@EJB
	private OrdenTransferenciaDAO ordenTransferenciaDAO;
	
	@EJB
	private RemesaDAO remesaDAO;
	
	private boolean esTipoMixto;
	private boolean esTipoServicio;
	private boolean esTipoMunicipal;
	
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="tmpDirDoc")
	private String tmpDirDoc;
	@Resource(name="folderTemplateOT")
	private String folderTemplateTo;
	@Resource(name="folderOT")
	private String folderOT;

	
	public Integer getIdPlantillaOTFonasa(Integer idOrdenTransferencia,List<ColumnaVO> columnsProgramas,
										  List<List<OTResumenConsolidadoFonasaProgramasVO>>listaResumenProgramasVO,
										  List<List<OTResumenConsolidadoFonasaVO>> listaResumenesConsolidadoFonasaVO){
		Integer plantillaId = null;
		if(plantillaId == null){
			
			 String[][] lista = new String[1000][columnsProgramas.size() + 9];
			int fila = 0;
			for (OTResumenConsolidadoFonasaVO otResumenConsolidadoFonasaVO : listaResumenesConsolidadoFonasaVO.get(0)) {
				
				int columnaServicio = 0;
	
				lista[fila][columnaServicio]=otResumenConsolidadoFonasaVO.getServicioSaludVO().getNombre_servicio();
				
				int columnaPrograma = 1;
				for (ColumnaVO columna : columnsProgramas) {
					
					if( listaResumenProgramasVO.get(0).size() ==0)
					{
						lista[fila][columnaPrograma] = "0";
					}
					else
					{
						for (OTResumenConsolidadoFonasaProgramasVO otResumenConsolidadoFonasaProgramasVO : listaResumenProgramasVO.get(0)) {
		
							if(otResumenConsolidadoFonasaVO.getServicioSaludVO()==otResumenConsolidadoFonasaProgramasVO.getServicioVO() && 
							  columna.getNombreColumna() == otResumenConsolidadoFonasaProgramasVO.getNombre())
							{
								lista[fila][columnaPrograma]=String.valueOf(otResumenConsolidadoFonasaProgramasVO.getMonto());
							}
							else
							{
								lista[fila][columnaPrograma]="";
							}
					}
						
				}
					
				columnaPrograma++;
					
			}
				columnaServicio = columnaPrograma;
				lista[fila][columnaServicio++]=String.valueOf(otResumenConsolidadoFonasaVO.getOtroRefMunicipal());
				lista[fila][columnaServicio++]=String.valueOf(otResumenConsolidadoFonasaVO.getLeyes());
				lista[fila][columnaServicio++]=String.valueOf(otResumenConsolidadoFonasaVO.getTotalPercapita());
				lista[fila][columnaServicio++]=String.valueOf(otResumenConsolidadoFonasaVO.getRebajaIncumplimiento());
				lista[fila][columnaServicio++]=String.valueOf(otResumenConsolidadoFonasaVO.getDesRetirtoLeyes());
				lista[fila][columnaServicio++]=String.valueOf(otResumenConsolidadoFonasaVO.getTotalRefMunicipal());
				lista[fila][columnaServicio++]=String.valueOf(otResumenConsolidadoFonasaVO.getMarcoPresupuestario());
				lista[fila][columnaServicio++]=String.valueOf(otResumenConsolidadoFonasaVO.getTotal());	

				fila++;
			}
		
		
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator + "archivoResumenConsolidadoFonasa.xlsx";//"plantillaPercapita.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();

			headers.add("SERVICIO");
			//DINAMICO
			for (ColumnaVO columna : columnsProgramas) {
				
				headers.add(columna.getNombreColumna());
			}
			headers.add("OTROS");
			headers.add("LEYES");
			headers.add("TOTAL PERCAPITA");
			headers.add("REBAJA");
			headers.add("DESCUENTO LEYES");
			headers.add("TOTAL REF");
			headers.add("MARCO");
			headers.add("TOTAL");
			
			List<ExcelDinamicoVO> lstDatos = new ArrayList<ExcelDinamicoVO>();

            for (int x = 0; x < lista.length; x++)
            {
               ExcelDinamicoVO filaArreglo = new ExcelDinamicoVO();
               List<Object> colArreglo = new ArrayList<Object>();
               
                for (int y = 0; y < lista[x].length; y++)
                {
                	colArreglo.add(lista[x][y]); 
                }
                
                filaArreglo.setListaColumnas(colArreglo);
                lstDatos.add(filaArreglo);
            }
            
            OTResumenOrdenFormatoFonasaSheetExcel otResumenOrdenFormatoFonasaSheetExcel = new OTResumenOrdenFormatoFonasaSheetExcel(headers, lstDatos);

			generadorExcel.addSheet(otResumenOrdenFormatoFonasaSheetExcel, "Hoja 1");
			try {
				
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType,folderOT);
				System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
				
				  OrdenTransferencia ordenTransferencia = ordenTransferenciaDAO.findById(idOrdenTransferencia);

				  TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.RESUMENCONSOLIDADOFONASA.getId());
                  plantillaId = documentService.createDocumentOrdinarioOrdenTransferencia(ordenTransferencia,
                		  																  tipoDocumento, response.getNodeRef(),
                		  																  response.getFileName(), contenType);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		return plantillaId;
	}
	
	
	
	
	private Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}
	public Integer createSeguimientoOT(Integer idOrdenTransferencia, TareasSeguimiento tareaSeguimiento, String subject, String body, String username, List<String> para, List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos){
		String from = usuarioDAO.getEmailByUsername(username);
		if(from == null){
			throw new RuntimeException("Usuario no tiene un email valido");
		}
		List<ReferenciaDocumentoSummaryVO> documentosTmp = new ArrayList<ReferenciaDocumentoSummaryVO>();
		if(documentos != null && documentos.size() > 0){
			for(Integer referenciaDocumentoId : documentos){
				MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = documentService.getDocumentSummary(referenciaDocumentoId);
				documentosTmp.add(referenciaDocumentoSummaryVO);
				String contenType= mimemap.getContentType(referenciaDocumentoSummaryVO.getPath().toLowerCase());
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderOT.replace("{ANO}", getAnoCurso().toString()));
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		return  otSeguimientoDAO.createSeguimiento(idOrdenTransferencia, seguimiento);
	}

	public List<SeguimientoVO> getBitacora(
			Integer idOrdenTransferencia,
			TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacoraProcesoOT(idOrdenTransferencia, tareaSeguimiento);
	}


	///***** METODOS ACTIVIDAD REVISAR ANTECENDETES ******
	private void calcularDatosOTRevisarAntecedentesVO(OTRevisarAntecedentesVO otRevisarAntecedentesVO,Collection<Remesa>listaRemesa)
	{
		//MARCO PRESUPUESTARIO
		long marcoPresupuestario = 200000;
		otRevisarAntecedentesVO.setMarcoPresupuestarioMonto(marcoPresupuestario);
		
		//ACUMULADO
		long acumulado = 0;
		for (Remesa remesa : listaRemesa) {
			acumulado = acumulado + remesa.getValorDia09() +remesa.getValorDia09()+remesa.getValorDia24()+remesa.getValorDia28();
		}
		otRevisarAntecedentesVO.setAcumuladoFechaMonto(acumulado);
		
		//CONVENIO
		long convenio = 3000;
		otRevisarAntecedentesVO.setConvenioMonto(convenio);

		//DIFERENCIA (MARCO -TOTAL(ACUMULADO+CONVENIO))
		long total =acumulado+convenio;
		total = marcoPresupuestario - total;
		otRevisarAntecedentesVO.setDiferenciaMarcoContraTotalMonto(total);
		
		//*********PORCENTAJES*************
		
		//ACUMULADO 
		float totalAcumuladoPorcentaje = ((float)acumulado* (float)100)/(float)marcoPresupuestario;
		otRevisarAntecedentesVO.setAcumuladoFechaPorcentaje(String.valueOf(totalAcumuladoPorcentaje)+"%");
		
		//TOTAL ACUMULADO 
		float convenioPorcentaje  = ((float)convenio* (float)100)/(float)marcoPresupuestario;
		otRevisarAntecedentesVO.setConvenioPorcentaje(String.valueOf(convenioPorcentaje)+"%");
		
		//DIFERENCIA (MARCO - TOTAL)
		float totalPorcentaje  = ((float)total* (float)100)/(float)marcoPresupuestario;
		otRevisarAntecedentesVO.setDiferenciaMarcoContraTotalPorcentaje(String.valueOf(totalPorcentaje)+"%");		
	}

	public List<OTRevisarAntecedentesVO> crearListaServicioPorPrograma(ProgramaAno programaAno)
	{
		List<OTRevisarAntecedentesVO> lista = new ArrayList<OTRevisarAntecedentesVO>();
		
		if(programaAno.getProgramasServiciosCore().size()>0 && programaAno.getProgramasMunicipalesCore().size()>0) //mixto
		{
			esTipoMixto = true;
			
		}
		else if(programaAno.getProgramasServiciosCore().size()>0) //servicio
		{
			esTipoServicio = true;
			
			for (ProgramaServicioCore servicioCore : programaAno.getProgramasServiciosCore()) {
				
				OTRevisarAntecedentesVO otRevisarAntecedentesVO = new OTRevisarAntecedentesVO();
				otRevisarAntecedentesVO.setServicioSalud(servicioCore.getServicio());
				otRevisarAntecedentesVO.setIdServicioSalud(servicioCore.getServicio().getId());
				
				otRevisarAntecedentesVO.setEstablecimiento(servicioCore.getEstablecimiento());
				otRevisarAntecedentesVO.setIdEstablecimiento(servicioCore.getEstablecimiento().getId());
				
				//SE CALCLULA DATOS : MARCO PRESUPUESTARIO-ACUMULADO-CONVENIO.
				calcularDatosOTRevisarAntecedentesVO(otRevisarAntecedentesVO,servicioCore.getServicio().getRemesaCollection());
				lista.add(otRevisarAntecedentesVO);
			}
			
		}
		else if(programaAno.getProgramasMunicipalesCore().size()>0) //municipal
		{
			esTipoMunicipal = true;
			
				for (ProgramaMunicipalCore municipalCore : programaAno.getProgramasMunicipalesCore()) {
				
				OTRevisarAntecedentesVO otRevisarAntecedentesVO = new OTRevisarAntecedentesVO();
				otRevisarAntecedentesVO.setServicioSalud(municipalCore.getComuna().getServicioSalud());
				otRevisarAntecedentesVO.setIdServicioSalud(municipalCore.getComuna().getServicioSalud().getId());
				
				otRevisarAntecedentesVO.setComuna(municipalCore.getComuna());
				otRevisarAntecedentesVO.setIdComuna(municipalCore.getComuna().getId());
				
				//SE CALCLULA DATOS : MARCO PRESUPUESTARIO-ACUMULADO-CONVENIO.
				calcularDatosOTRevisarAntecedentesVO(otRevisarAntecedentesVO,municipalCore.getComuna().getRemesaCollection());
				lista.add(otRevisarAntecedentesVO);
			}
			
		}	

		long id=0;
		for (OTRevisarAntecedentesVO otRevisarAntecedentesVO : lista) {
			
			otRevisarAntecedentesVO.setId(id);
			otRevisarAntecedentesVO.setProgramaAno(programaAno);
			id++;
		}
		
		return lista;
		
	}
	
	public List<OTRevisarAntecedentesVO> obtenerListaSubtituloVOPorPrograma(Integer idProgramaAno,Integer idTipoSubtitulo)
	{
		
		ProgramaAno programaAno = programaDAO.getProgramasByIdProgramaAno(idProgramaAno);
		List<OTRevisarAntecedentesVO> listaVO = crearListaServicioPorPrograma(programaAno);
		
		Collection<Remesa>listaRemesa =  new ArrayList<Remesa>();
		if(programaAno.getRemesaCollection()!=null)
			listaRemesa = programaAno.getRemesaCollection();

		for (OTRevisarAntecedentesVO otRevisarAntecedentesVO : listaVO) {

			Integer idServicioSalud = otRevisarAntecedentesVO.getIdServicioSalud();
			Integer idEstablecimiento =0;
			if(otRevisarAntecedentesVO.getIdEstablecimiento()!=null)
				idEstablecimiento= otRevisarAntecedentesVO.getIdEstablecimiento();
			
			Integer idComuna=0;
			if(otRevisarAntecedentesVO.getIdComuna()!=null)
				idComuna= otRevisarAntecedentesVO.getIdComuna();
			
			Integer anio = Util.obtenerAno(new Date());

			for (Remesa remesa : listaRemesa) {
				
				if(remesa.getIdtiposubtitulo().getIdTipoSubtitulo().intValue() == idTipoSubtitulo)
				{
					//datos remesa
					Integer idServicioSaludRemesa = remesa.getIdServicioSalud().getId();
					Integer idEstablecimientoRemesa =0;
					if(remesa.getIdEstablecimiento()!=null)
						idEstablecimientoRemesa= remesa.getIdEstablecimiento().getId();
					
					Integer idComunaRemesa=0;
					if(remesa.getIdComuna()!=null)
						idComunaRemesa= remesa.getIdComuna().getId();
					
					if(anio.intValue() == remesa.getAnio().intValue() )
					{
						if(idServicioSalud == idServicioSaludRemesa)
						{
							if(esTipoServicio)//TIPO SERVICIO
							{
								if(true)//POR ESTABLECIMIENTO
								{
									if(idEstablecimiento== idEstablecimientoRemesa)
									{
										cargarVOPorRemesa(otRevisarAntecedentesVO,remesa);
									}
								}
								else //POR SERVICIO
								{
									cargarVOPorRemesa(otRevisarAntecedentesVO,remesa);
								}
							}
							else if(esTipoMunicipal)//TIPO MUNCIPAL
							{
								if(idComuna== idComunaRemesa)
								{
									cargarVOPorRemesa(otRevisarAntecedentesVO,remesa);
								}
							}
						}
					}
				}
			}			
		}
		
		return listaVO;
		
	}

	private void cargarVOPorRemesa(OTRevisarAntecedentesVO otRevisarAntecedentesVO, Remesa remesa)
	{
		
		Long valorDia09 = Long.valueOf(String.valueOf(remesa.getValorDia09()));
		Long valorDia24 =  Long.valueOf(String.valueOf(remesa.getValorDia24()));
		Long valorDia28 =  Long.valueOf(String.valueOf(remesa.getValorDia28()));
			
		//ENERO
		if(remesa.getIdMes().getIdMes() == 1)
		{
			otRevisarAntecedentesVO.setEneroRemesa09(valorDia09);
			otRevisarAntecedentesVO.setEneroRemesa24(valorDia24);
			otRevisarAntecedentesVO.setEneroRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesEnero(remesa.getIdRemesa());
		}
		
		//FEBRERO
		if(remesa.getIdMes().getIdMes() == 2)
		{
			otRevisarAntecedentesVO.setFebreroRemesa09(valorDia09);
			otRevisarAntecedentesVO.setFebreroRemesa24(valorDia24);
			otRevisarAntecedentesVO.setFebreroRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesFebrero(remesa.getIdRemesa());
		}
		//MARZO
		if(remesa.getIdMes().getIdMes() == 3)
		{
			otRevisarAntecedentesVO.setMarzoRemesa09(valorDia09);
			otRevisarAntecedentesVO.setMarzoRemesa24(valorDia24);
			otRevisarAntecedentesVO.setMarzoRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesMarzo(remesa.getIdRemesa());
		}
		
		
		//ABRIL
		if(remesa.getIdMes().getIdMes() == 4)
		{
			otRevisarAntecedentesVO.setAbrilRemesa09(valorDia09);
			otRevisarAntecedentesVO.setAbrilRemesa24(valorDia24);
			otRevisarAntecedentesVO.setAbrilRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesAbril(remesa.getIdRemesa());
		}
		
		//MAYO
		if(remesa.getIdMes().getIdMes() == 5)
		{
			otRevisarAntecedentesVO.setMayoRemesa09(valorDia09);
			otRevisarAntecedentesVO.setMayoRemesa24(valorDia24);
			otRevisarAntecedentesVO.setMayoRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesMayo(remesa.getIdRemesa());
		}
		
		//JUNIO
		if(remesa.getIdMes().getIdMes() == 6)
		{
			otRevisarAntecedentesVO.setJunioRemesa09(valorDia09);
			otRevisarAntecedentesVO.setJunioRemesa24(valorDia24);
			otRevisarAntecedentesVO.setJunioRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesJunio(remesa.getIdRemesa());
		}
		
		//JULIO
		if(remesa.getIdMes().getIdMes() == 7)
		{
			otRevisarAntecedentesVO.setJulioRemesa09(valorDia09);
			otRevisarAntecedentesVO.setJulioRemesa24(valorDia24);
			otRevisarAntecedentesVO.setJulioRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesJulio(remesa.getIdRemesa());
		}
		
		//AGOSTO
		if(remesa.getIdMes().getIdMes() == 8)
		{
			otRevisarAntecedentesVO.setAgostoRemesa09(valorDia09);
			otRevisarAntecedentesVO.setAgostoRemesa24(valorDia24);
			otRevisarAntecedentesVO.setAgostoRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesAgosto(remesa.getIdRemesa());
		}
		
		//SEPTIEMBRE
		if(remesa.getIdMes().getIdMes() == 9)
		{
			otRevisarAntecedentesVO.setSeptiembreRemesa09(valorDia09);
			otRevisarAntecedentesVO.setSeptiembreRemesa24(valorDia24);
			otRevisarAntecedentesVO.setSeptiembreRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesSeptiembre(remesa.getIdRemesa());
		}
		
		//OCTUBRE
		if(remesa.getIdMes().getIdMes() == 10)
		{
			otRevisarAntecedentesVO.setOctubreRemesa09(valorDia09);
			otRevisarAntecedentesVO.setOctubreRemesa24(valorDia24);
			otRevisarAntecedentesVO.setOctubreRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesOctubre(remesa.getIdRemesa());
		}

		//NOVIEMBRE
		if(remesa.getIdMes().getIdMes() == 11)
		{
			otRevisarAntecedentesVO.setNoviembreRemesa09(valorDia09);
			otRevisarAntecedentesVO.setNoviembreRemesa24(valorDia24);
			otRevisarAntecedentesVO.setNoviembreRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesNoviembre(remesa.getIdRemesa());
		}
		
		//DICIEMBRE
		if(remesa.getIdMes().getIdMes() == 12)
		{
			otRevisarAntecedentesVO.setDiciembreRemesa09(valorDia09);
			otRevisarAntecedentesVO.setDiciembreRemesa24(valorDia24);
			otRevisarAntecedentesVO.setDiciembreRemesa28(valorDia28);
			otRevisarAntecedentesVO.setIdRemesaMesDiciembre(remesa.getIdRemesa());
		}
	}

	public List<Remesa> obtenerListaRemesaPorListaOTRevisarAntecedentesVO(List<OTRevisarAntecedentesVO> listaVO,Integer idProgramaAno)
	{
		List<Remesa> listaRemesa= new ArrayList<Remesa>();
		ProgramaAno programaAno = programaDAO.getProgramasByIdProgramaAno(idProgramaAno);
		
		for (OTRevisarAntecedentesVO otRevisarAntecedentesVO : listaVO) {

			//ENERO
			if(String.valueOf(otRevisarAntecedentesVO.getEneroRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getEneroRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getEneroRemesa28()) != "0")
			{
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesEnero(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(1),
						(int)(long)otRevisarAntecedentesVO.getEneroRemesa09(),
						(int)(long) otRevisarAntecedentesVO.getEneroRemesa24(),
						(int)(long) otRevisarAntecedentesVO.getEneroRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
				
				listaRemesa.add(remesa);
				
			}
			
			//Febrero
			if(String.valueOf(otRevisarAntecedentesVO.getFebreroRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getFebreroRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getFebreroRemesa28()) != "0")
			{
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesFebrero(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(2),
						(int)(long)otRevisarAntecedentesVO.getFebreroRemesa09(),
						(int)(long) otRevisarAntecedentesVO.getFebreroRemesa24(),
						(int)(long) otRevisarAntecedentesVO.getFebreroRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
				
				listaRemesa.add(remesa);
				
			}
			
			//Marzo
			if(String.valueOf(otRevisarAntecedentesVO.getMarzoRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getMarzoRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getMarzoRemesa28()) != "0")
			{
				
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesMarzo(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(3),
						(int)(long)otRevisarAntecedentesVO.getMarzoRemesa09(),
						(int)(long) otRevisarAntecedentesVO.getMarzoRemesa24(),
						(int)(long) otRevisarAntecedentesVO.getMarzoRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
				
				listaRemesa.add(remesa);
				
			}
			
			//Abril
			if(String.valueOf(otRevisarAntecedentesVO.getAbrilRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getAbrilRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getAbrilRemesa28()) != "0")
			{
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesAbril(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(4),
						(int)(long)otRevisarAntecedentesVO.getAbrilRemesa09(),
						(int)(long) otRevisarAntecedentesVO.getAbrilRemesa24(),
						(int)(long) otRevisarAntecedentesVO.getAbrilRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
				
				listaRemesa.add(remesa);
				
			}
			
			
			//mayo
			if(String.valueOf(otRevisarAntecedentesVO.getMayoRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getMayoRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getMayoRemesa28()) != "0")
			{
				
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesMayo(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(5),
						(int)(long)otRevisarAntecedentesVO.getMayoRemesa09(),
						(int)(long) otRevisarAntecedentesVO.getMayoRemesa24(),
						(int)(long) otRevisarAntecedentesVO.getMayoRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
				
				listaRemesa.add(remesa);
				
			}
			
			//Junio
			if(String.valueOf(otRevisarAntecedentesVO.getJunioRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getJunioRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getJunioRemesa28()) != "0")
			{
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesJunio(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(6),
						(int)(long)otRevisarAntecedentesVO.getJunioRemesa09(),
						(int)(long) otRevisarAntecedentesVO.getJunioRemesa24(),
						(int)(long) otRevisarAntecedentesVO.getJunioRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
				
				listaRemesa.add(remesa);
				
			}
			
			
			//Julio
			if(String.valueOf(otRevisarAntecedentesVO.getJulioRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getJulioRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getJulioRemesa28()) != "0")
			{
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesJulio(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(7),
						(int)(long)otRevisarAntecedentesVO.getJulioRemesa09(),
						(int)(long) otRevisarAntecedentesVO.getJulioRemesa24(),
						(int)(long) otRevisarAntecedentesVO.getJulioRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
				
				listaRemesa.add(remesa);
				
			}
			
			
			//Agosto
			if(String.valueOf(otRevisarAntecedentesVO.getAgostoRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getAgostoRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getAgostoRemesa28()) != "0")
			{
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesAgosto(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(8),
						(int)(long)otRevisarAntecedentesVO.getAgostoRemesa09(),
						(int)(long) otRevisarAntecedentesVO.getAgostoRemesa24(),
						(int)(long) otRevisarAntecedentesVO.getAgostoRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
				
				listaRemesa.add(remesa);
				
			}
			
			//Septiembre
			if(String.valueOf(otRevisarAntecedentesVO.getSeptiembreRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getSeptiembreRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getSeptiembreRemesa28()) != "0")
			{
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesSeptiembre(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(9),
						(int)(long)otRevisarAntecedentesVO.getSeptiembreRemesa09(),
						(int)(long) otRevisarAntecedentesVO.getSeptiembreRemesa24(),
						(int)(long) otRevisarAntecedentesVO.getSeptiembreRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
				
				listaRemesa.add(remesa);
				
			}
			
			//Octubre
			if(String.valueOf(otRevisarAntecedentesVO.getOctubreRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getOctubreRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getOctubreRemesa28()) != "0")
			{
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesOctubre(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(10),
						otRevisarAntecedentesVO.getOctubreRemesa09(),
						otRevisarAntecedentesVO.getOctubreRemesa24(),
						otRevisarAntecedentesVO.getOctubreRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
				
				listaRemesa.add(remesa);
				
			}
			
			//Noviembre
			if(String.valueOf(otRevisarAntecedentesVO.getNoviembreRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getNoviembreRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getNoviembreRemesa28()) != "0")
			{
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesNoviembre(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(11),
						(int)(long)otRevisarAntecedentesVO.getNoviembreRemesa09(),
						(int)(long) otRevisarAntecedentesVO.getNoviembreRemesa24(),
						(int)(long) otRevisarAntecedentesVO.getNoviembreRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
				
				listaRemesa.add(remesa);
				
			}
			//Diciembre
			if(String.valueOf(otRevisarAntecedentesVO.getDiciembreRemesa09()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getDiciembreRemesa24()) != "0"||
			   String.valueOf(otRevisarAntecedentesVO.getDiciembreRemesa28()) != "0")
			{
				
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesDiciembre(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesDAO.getMesPorID(12),
						(int)(long)otRevisarAntecedentesVO.getDiciembreRemesa09(),
						(int)(long) otRevisarAntecedentesVO.getDiciembreRemesa24(),
						(int)(long) otRevisarAntecedentesVO.getDiciembreRemesa28());
				
				if(esTipoServicio )//SERVICIO
					remesa.setIdEstablecimiento(otRevisarAntecedentesVO.getEstablecimiento());
				if(esTipoMunicipal)//MUNICIPAL
					remesa.setIdComuna(otRevisarAntecedentesVO.getComuna());
					
				
				listaRemesa.add(remesa);
				
			}
		}
		
		
		return listaRemesa;
	}

	public void guardarDatosProcesoRevisarOT(List<OTRevisarAntecedentesVO> lista,Integer idProgramaAno,Integer idMesActual,Integer idMesSiguiente,Integer idTipoSubtitulo) {

		List<Remesa> listaRemesa =obtenerListaRemesaPorListaOTRevisarAntecedentesVO(lista,idProgramaAno);
		for (Remesa remesa : listaRemesa) {
			if(remesa.getIdMes().getIdMes().intValue() == idMesActual ||
			  remesa.getIdMes().getIdMes().intValue() == idMesSiguiente	)
			{
				
				if(remesa.getIdRemesa()!= null && remesa.getIdRemesa().intValue() > 0)
				{
					remesaDAO.actualizarRemesa(remesa);
				}
				else
				{
					remesa.setIdtiposubtitulo(tipoSubtituloDAO.getTipoSubtituloPorID(idTipoSubtitulo));
					remesaDAO.crearRemesa(remesa);
				}
			}
		}
	}

	///*****FIN METODOS ACTIVIDAD REVISAR ANTECENDETES ******
	
	
	///***** METODOS ACTIVIDAD REVISAR ANTECENDETES CONSOLIDADOR ******

	//METODO REPARAR PROGRAMA
	public void repararPrograma(Integer idProgramaAno,Integer idMesActual,Integer idMesSiguiente,
								String user,String observacion)
	{
		 ProgramaAno programaAno = programaDAO.getProgramasByIdProgramaAno(idProgramaAno);
		 
		 String correoResponsablePrograma = programaAno.getPrograma().getUsuario().getEmail().getValor();
		 String para= correoResponsablePrograma;
		 String subject = "Reparo Programa "+programaAno.getPrograma().getNombre();
		 emailService.sendMail(para, subject, observacion,null); 		
		 
		 for (Remesa remesa : programaAno.getRemesaCollection()) {
			
			 if(remesa.getIdMes().getIdMes() == idMesActual || remesa.getIdMes().getIdMes() == idMesSiguiente )
			 {
				 //CAMBIAR ESTADO REMESA
			 }
		} 
	}
	
	//METODOS PESTAÑA RESUMEN CONSOLIDADO
	public List<OTResumenConsolidadoFonasaVO> obtenerListaResumenConsolidadoFonasaVOPorUsuario(String username,Integer idTipoSubtitulo)
	{
		List<OTResumenConsolidadoFonasaVO> listaOTResumenConsolidadoFonasaVO = new ArrayList<OTResumenConsolidadoFonasaVO>();
		List<OTResumenConsolidadoFonasaProgramasVO>listaResumenProgramasVO = new ArrayList<OTResumenConsolidadoFonasaProgramasVO>();
	
		//LISTA DE PROGRAMA 
		List<ProgramaAno> listaProgramaAno = programaDAO.getProgramasByUserAno(username, Util.obtenerAno(new Date()));
		
		//SE AGREGAN TODOS LOS SERVICIOS DE LOS PROGRAMAS DEL USUARIO
		List<ServiciosVO> listaServiciosVO = new ArrayList<ServiciosVO>();
		
		for (ProgramaAno programaAno : listaProgramaAno) {
			
			if(programaAno.getProgramasServiciosCore().size()>0) //servicio
			{
				for (ProgramaServicioCore servicioCore : programaAno.getProgramasServiciosCore()) {

					ServiciosVO servicioVO= new ServicioSaludMapper().getBasic(servicioCore.getServicio());
					
					OTResumenConsolidadoFonasaProgramasVO otResumenConsolidadoFonasaProgramasVO =  new OTResumenConsolidadoFonasaProgramasVO();
					otResumenConsolidadoFonasaProgramasVO.setIdPrograma(programaAno.getPrograma().getId());
					otResumenConsolidadoFonasaProgramasVO.setNombre(programaAno.getPrograma().getNombre());
					otResumenConsolidadoFonasaProgramasVO.setRevisaFonasa(programaAno.getPrograma().getRevisaFonasa());
					otResumenConsolidadoFonasaProgramasVO.setServicioVO(servicioVO);
					
					long totalRemesaPorPrograma = 0;
					for (Remesa remesa : servicioCore.getServicio().getRemesaCollection()) {
						if(remesa.getIdtiposubtitulo().getIdTipoSubtitulo().intValue() == idTipoSubtitulo)
						{
							totalRemesaPorPrograma = totalRemesaPorPrograma + remesa.getValorDia09() + remesa.getValorDia24() + remesa.getValorDia28();
						}
					}
					
					otResumenConsolidadoFonasaProgramasVO.setMonto(totalRemesaPorPrograma);
					listaResumenProgramasVO.add(otResumenConsolidadoFonasaProgramasVO);
					
					if(!listaServiciosVO.contains(servicioCore.getServicio()))
					{
						listaServiciosVO.add(servicioVO);
					}
				}
			}
			else if(programaAno.getProgramasMunicipalesCore().size()>0) //municipal
			{
					for (ProgramaMunicipalCore municipalCore : programaAno.getProgramasMunicipalesCore()) {
						
						ServiciosVO servicioVO= new ServicioSaludMapper().getBasic(municipalCore.getComuna().getServicioSalud());
						
						OTResumenConsolidadoFonasaProgramasVO otResumenConsolidadoFonasaProgramasVO =  new OTResumenConsolidadoFonasaProgramasVO();
						otResumenConsolidadoFonasaProgramasVO.setIdPrograma(programaAno.getPrograma().getId());
						otResumenConsolidadoFonasaProgramasVO.setNombre(programaAno.getPrograma().getNombre());
						otResumenConsolidadoFonasaProgramasVO.setRevisaFonasa(programaAno.getPrograma().getRevisaFonasa());
						otResumenConsolidadoFonasaProgramasVO.setServicioVO(servicioVO);
						
						long totalRemesaPorPrograma = 0;
						for (Remesa remesa : municipalCore.getComuna().getServicioSalud().getRemesaCollection()) {
							if(remesa.getIdtiposubtitulo().getIdTipoSubtitulo().intValue() == idTipoSubtitulo)
							{
								totalRemesaPorPrograma = totalRemesaPorPrograma + remesa.getValorDia09() + remesa.getValorDia24() + remesa.getValorDia28();
							}
						}
						
						otResumenConsolidadoFonasaProgramasVO.setMonto(totalRemesaPorPrograma);
						listaResumenProgramasVO.add(otResumenConsolidadoFonasaProgramasVO);
						
						if(!listaServiciosVO.contains(servicioVO))
						{
							listaServiciosVO.add(servicioVO);
						}
				}
			}	
		}

		for (ServiciosVO serviciosVO : listaServiciosVO) {
			
			OTResumenConsolidadoFonasaVO otResumenConsolidadoFonasaVO = new OTResumenConsolidadoFonasaVO();
			otResumenConsolidadoFonasaVO.setListaOTResumenConsolidadoFonasaProgramasVO(new ArrayList<OTResumenConsolidadoFonasaProgramasVO>());
			otResumenConsolidadoFonasaVO.setServicioSaludVO(serviciosVO);
			otResumenConsolidadoFonasaVO.setLeyes(1000);
			otResumenConsolidadoFonasaVO.setTotalPercapita(2000);
			otResumenConsolidadoFonasaVO.setRebajaIncumplimiento(3000);
			otResumenConsolidadoFonasaVO.setDesRetirtoLeyes(4000);
			otResumenConsolidadoFonasaVO.setTotalRefMunicipal(5000);
			otResumenConsolidadoFonasaVO.setMarcoPresupuestario(6000);
			otResumenConsolidadoFonasaVO.setTotal(7000000);
			
			long montoOtroRefMunicipal = 0;
			for (OTResumenConsolidadoFonasaProgramasVO otResumenConsolidadoFonasaProgramasVO : listaResumenProgramasVO) {
				
				if(serviciosVO.getId_servicio() == otResumenConsolidadoFonasaProgramasVO.getServicioVO().getId_servicio())
				{
					if(otResumenConsolidadoFonasaProgramasVO.isRevisaFonasa())
					{
						otResumenConsolidadoFonasaVO.getListaOTResumenConsolidadoFonasaProgramasVO().add(otResumenConsolidadoFonasaProgramasVO);
					}
					else
					{
						montoOtroRefMunicipal  = montoOtroRefMunicipal +otResumenConsolidadoFonasaProgramasVO.getMonto();
					}	
				}
			}
			
			otResumenConsolidadoFonasaVO.setOtroRefMunicipal(montoOtroRefMunicipal);
			listaOTResumenConsolidadoFonasaVO.add(otResumenConsolidadoFonasaVO);
		}
		
		return listaOTResumenConsolidadoFonasaVO;
	}

	//METODOS PESTAÑA RESUMEN TOTALES
	public List<OTResumenTotalVO> obtenerListaResumenTotalPorIDProgramaAno(Integer idProgramaAno)
	{
		List<OTResumenTotalVO>listaOTResumenTotalVO= new ArrayList<OTResumenTotalVO>();
	
		// PROGRAMA AÑO
		ProgramaAno programaAno = programaDAO.getProgramasByIdProgramaAno(idProgramaAno);

		if(programaAno.getProgramasServiciosCore().size()>0) //servicio
		{
			for (ProgramaServicioCore servicioCore : programaAno.getProgramasServiciosCore()) {

				ServiciosVO servicioVO= new ServicioSaludMapper().getBasic(servicioCore.getServicio());
				
				OTResumenTotalVO otResumenTotalVO =  new OTResumenTotalVO();
				otResumenTotalVO.setServicioSaludVO(servicioVO);
				otResumenTotalVO.setIdProgramaAno(idProgramaAno);
				
				long totalRemesaSubtitulo21 = 0;
				long totalRemesaSubtitulo22 = 0;
				long totalRemesaSubtitulo29 = 0;
				long totalRemesaSubtitulo24 = 0;
				for (Remesa remesa : servicioCore.getServicio().getRemesaCollection()) {
					if(remesa.getIdtiposubtitulo().getIdTipoSubtitulo().intValue() == Subtitulo.SUBTITULO21.getId())
					{
						totalRemesaSubtitulo21 = totalRemesaSubtitulo21 + remesa.getValorDia09() + remesa.getValorDia24() + remesa.getValorDia28();
					}
					else if(remesa.getIdtiposubtitulo().getIdTipoSubtitulo().intValue() == Subtitulo.SUBTITULO22.getId())
					{
						totalRemesaSubtitulo22 = totalRemesaSubtitulo22 + remesa.getValorDia09() + remesa.getValorDia24() + remesa.getValorDia28();
					}
					else if(remesa.getIdtiposubtitulo().getIdTipoSubtitulo().intValue() == Subtitulo.SUBTITULO29.getId())
					{
						totalRemesaSubtitulo29 = totalRemesaSubtitulo29 + remesa.getValorDia09() + remesa.getValorDia24() + remesa.getValorDia28();
					}
					else if(remesa.getIdtiposubtitulo().getIdTipoSubtitulo().intValue() == Subtitulo.SUBTITULO24.getId())
					{
						totalRemesaSubtitulo24 = totalRemesaSubtitulo24 + remesa.getValorDia09() + remesa.getValorDia24() + remesa.getValorDia28();
					}
				}
				
				otResumenTotalVO.setSubtitulo21Total(totalRemesaSubtitulo21);
				otResumenTotalVO.setSubtitulo22Total(totalRemesaSubtitulo22);
				otResumenTotalVO.setSubtitulo29Total(totalRemesaSubtitulo29);
				otResumenTotalVO.setSubtitulo24Total(totalRemesaSubtitulo24);
				listaOTResumenTotalVO.add(otResumenTotalVO);

			}
		}
		else if(programaAno.getProgramasMunicipalesCore().size()>0) //municipal
		{
				for (ProgramaMunicipalCore municipalCore : programaAno.getProgramasMunicipalesCore()) {
					
					ServiciosVO servicioVO= new ServicioSaludMapper().getBasic(municipalCore.getComuna().getServicioSalud());

					OTResumenTotalVO otResumenTotalVO =  new OTResumenTotalVO();
					otResumenTotalVO.setServicioSaludVO(servicioVO);
					otResumenTotalVO.setIdProgramaAno(idProgramaAno);
					
					long totalRemesaSubtitulo21 = 0;
					long totalRemesaSubtitulo22 = 0;
					long totalRemesaSubtitulo29 = 0;
					long totalRemesaSubtitulo24 = 0;
					
					for (Remesa remesa : municipalCore.getComuna().getServicioSalud().getRemesaCollection()) {
						if(remesa.getIdtiposubtitulo().getIdTipoSubtitulo().intValue() == Subtitulo.SUBTITULO21.getId())
						{
							totalRemesaSubtitulo21 = totalRemesaSubtitulo21 + remesa.getValorDia09() + remesa.getValorDia24() + remesa.getValorDia28();
						}
						else if(remesa.getIdtiposubtitulo().getIdTipoSubtitulo().intValue() == Subtitulo.SUBTITULO22.getId())
						{
							totalRemesaSubtitulo22 = totalRemesaSubtitulo21 + remesa.getValorDia09() + remesa.getValorDia24() + remesa.getValorDia28();
						}
						else if(remesa.getIdtiposubtitulo().getIdTipoSubtitulo().intValue() == Subtitulo.SUBTITULO29.getId())
						{
							totalRemesaSubtitulo29 = totalRemesaSubtitulo21 + remesa.getValorDia09() + remesa.getValorDia24() + remesa.getValorDia28();
						}
						else if(remesa.getIdtiposubtitulo().getIdTipoSubtitulo().intValue() == Subtitulo.SUBTITULO24.getId())
						{
							totalRemesaSubtitulo24 = totalRemesaSubtitulo21 + remesa.getValorDia09() + remesa.getValorDia24() + remesa.getValorDia28();
						}
					}
					
					otResumenTotalVO.setSubtitulo21Total(totalRemesaSubtitulo21);
					otResumenTotalVO.setSubtitulo22Total(totalRemesaSubtitulo22);
					otResumenTotalVO.setSubtitulo29Total(totalRemesaSubtitulo29);
					otResumenTotalVO.setSubtitulo24Total(totalRemesaSubtitulo24);
					listaOTResumenTotalVO.add(otResumenTotalVO);
			}
		}
				
		return listaOTResumenTotalVO;
	}
	
	public Integer crearObjetoOrdenTransferencia(String username) {
		
		OrdenTransferencia ordenTransferencia = new OrdenTransferencia(); 
		ordenTransferencia.setFechaCreacion(new Date());
		ordenTransferencia.setUsuario(usuarioDAO.getUserByUsername(username));
		ordenTransferenciaDAO.save(ordenTransferencia);
		return ordenTransferencia.getIdOrdenTransferencia();
	}
	

	//METODO ACTIVIDAD GENERACION ORDINARIOS ORDENES DE TRANSFERENCIA
	public Integer crearDocumentoOrdinarioOrdenTransferencia(Integer idOrdenTransferencia) {
			
			System.out.println("DistribucionInicialPercapitaService --- > crearOficioConsulta");
			Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAORDINARIOOREDENTRANSFERENCIA);

            if (plantillaId == null) {
                   throw new RuntimeException(
                                 "No se puede crear Borrador Decreto Aporte Estatal, la plantilla no esta cargada");
            }
            try {
                   ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryBorradorAporteEstatalVO = documentService
                                 .getDocumentByPlantillaId(plantillaId);
                   
                   DocumentoVO documentoOrdinarioOrdenTransferenciaConsolidadoVO = documentService.getDocument(referenciaDocumentoSummaryBorradorAporteEstatalVO.getId());
                   
                   String templateOrdinarioOrdenTransferenciaConsolidado = tmpDirDoc + File.separator + documentoOrdinarioOrdenTransferenciaConsolidadoVO.getName();
                   templateOrdinarioOrdenTransferenciaConsolidado = templateOrdinarioOrdenTransferenciaConsolidado.replace(" ", "");
                   String filenameOrdinarioOrdenTransferenciaConsolidado = tmpDirDoc + File.separator + new Date().getTime() + "_"+ "OrdinarioOrdenTransferenciaConsolidado.docx";
                   filenameOrdinarioOrdenTransferenciaConsolidado = filenameOrdinarioOrdenTransferenciaConsolidado.replaceAll("","");
                   System.out.println("filenameOrdinarioOrdenTransferenciaConsolidado filename-->"
                                 + filenameOrdinarioOrdenTransferenciaConsolidado);
                   System.out.println("templateOrdinarioOrdenTransferenciaConsolidado template-->"
                                 + templateOrdinarioOrdenTransferenciaConsolidado);
                   GeneradorWord generadorWordPlantillaOrdinarioOrdenTransferenciaConsolidado = new GeneradorWord(
                		   filenameOrdinarioOrdenTransferenciaConsolidado);
                   
                   MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
                   String contentType = mimemap.getContentType(filenameOrdinarioOrdenTransferenciaConsolidado.toLowerCase());
                   System.out.println("contenTypeBorradorAporteEstatal->"
                                 + contentType);
                   generadorWordPlantillaOrdinarioOrdenTransferenciaConsolidado.saveContent(documentoOrdinarioOrdenTransferenciaConsolidadoVO.getContent(), XWPFDocument.class);

                   Map<String, Object> parametersOrdinarioOrdenTransferencia = new HashMap<String, Object>();
                   parametersOrdinarioOrdenTransferencia.put("{ano}", Util.obtenerAno(new Date()));
                   parametersOrdinarioOrdenTransferencia.put("{mes}", Util.obtenerNombreMes(Util.obtenerMes(new Date())));
                   parametersOrdinarioOrdenTransferencia.put("{fecha}", Util.getFechaDia());
                   
                   GeneradorWordOrdinarioOrdenTransferencia generadorWordOrdinarioOrdenTransferencia = new GeneradorWordOrdinarioOrdenTransferencia(
                		   filenameOrdinarioOrdenTransferenciaConsolidado,
                                 templateOrdinarioOrdenTransferenciaConsolidado);
                   
                   generadorWordOrdinarioOrdenTransferencia.replaceValues(
                		   parametersOrdinarioOrdenTransferencia, null, XWPFDocument.class);
                   BodyVO response = alfrescoService.uploadDocument(new File(
                		   filenameOrdinarioOrdenTransferenciaConsolidado), contentType,
                                 folderOT.replace("{ANO}", getAnoCurso()
                                              .toString()));
                   System.out.println("response responseBorradorAporteEstatal --->"
                                 + response);

                   OrdenTransferencia ordenTransferencia = ordenTransferenciaDAO.findById(idOrdenTransferencia);
                   TipoDocumento tipoDocumento = new TipoDocumento(
                                 TipoDocumentosProcesos.PLANTILLAORDINARIOOREDENTRANSFERENCIA.getId());
                   
                   plantillaId = documentService.createDocumentOrdinarioOrdenTransferencia(ordenTransferencia,
                		   					tipoDocumento, response.getNodeRef(),
                                              response.getFileName(), contentType);

            } catch (IOException e) {
                   e.printStackTrace();
            } catch (InvalidFormatException e) {
                   e.printStackTrace();
            }
            return plantillaId;
      }
	
	public ReferenciaDocumentoSummaryVO getLastDocumentoSummaryByOrdinarioOrdenTransferenciaType(Integer idOrdenTransferencia,Integer idTipoDocumento){
		return documentService.getLastDocumentoSummaryByOrdinarioOrdenTransferenciaType(idOrdenTransferencia,idTipoDocumento);
	}
	
	public Integer administracionVersionAlfresco(Integer idOrdenTransferencia)
	{
		 OrdenTransferencia ordenTransferencia = ordenTransferenciaDAO.findById(idOrdenTransferencia);
		 List<DocumentoOt> lstDocumentoOt = (List<DocumentoOt>)ordenTransferencia.getDocumentoOtCollection();
		
		//RESUMEN CONSOLIDADO FONASA
		ReferenciaDocumentoSummaryVO referenciaDocumentoResumenConsolidadoFonasa =  getLastDocumentoSummaryByOrdinarioOrdenTransferenciaType(idOrdenTransferencia,
																					TipoDocumentosProcesos.RESUMENCONSOLIDADOFONASA.getId());
		
		//ORDINARIO OT
		ReferenciaDocumentoSummaryVO referenciaDocumentoOrdinarioOT =  getLastDocumentoSummaryByOrdinarioOrdenTransferenciaType(idOrdenTransferencia,
 																					TipoDocumentosProcesos.PLANTILLAORDINARIOOREDENTRANSFERENCIA.getId());
        
        String keyUltimoDocResumenConsolidado = ((referenciaDocumentoResumenConsolidadoFonasa.getNodeRef() == null) ? referenciaDocumentoResumenConsolidadoFonasa.getPath() : referenciaDocumentoResumenConsolidadoFonasa.getNodeRef().replace("workspace://SpacesStore/", ""));
        String keyUltimoDocOrdinarioOT= ((referenciaDocumentoOrdinarioOT.getNodeRef() == null) ? referenciaDocumentoOrdinarioOT.getPath() : referenciaDocumentoOrdinarioOT.getNodeRef().replace("workspace://SpacesStore/", ""));
        
        for (DocumentoOt documentoOt : lstDocumentoOt) {

                String key = ((documentoOt.getIdDocumento().getNodeRef() == null) ? documentoOt.getIdDocumento().getPath() : documentoOt.getIdDocumento().getNodeRef()
                                .replace("workspace://SpacesStore/", ""));
                            
                //eliminacion documentos Resumen Consolidado Fonasa
                if(documentoOt.getIdTipoDocumento().getIdTipoDocumento() == TipoDocumentosProcesos.RESUMENCONSOLIDADOFONASA.getId())
                {
	                if (!key.equals(keyUltimoDocResumenConsolidado)){
	                        alfrescoService.delete(key);
	                }
                }
                
                //eliminacion documentos Ordinario OT
                if(documentoOt.getIdTipoDocumento().getIdTipoDocumento() == TipoDocumentosProcesos.PLANTILLAORDINARIOOREDENTRANSFERENCIA.getId())
                {
	                if (!key.equals(keyUltimoDocOrdinarioOT)){
	                        alfrescoService.delete(key);
	                }
                }
        }

        return 0;
	}

	
	public void enviarOrdinarioOTServicioSalud(Integer idOrdenTransferencia, String username) {

		System.out.println("enviarDecretoAporteEstatal--> "+idOrdenTransferencia+" username="+username);
		
		//RESUMEN CONSOLIDADO FONASA
		ReferenciaDocumentoSummaryVO referenciaDocumentoResumenConsolidadoFonasa =  getLastDocumentoSummaryByOrdinarioOrdenTransferenciaType(idOrdenTransferencia,
																					TipoDocumentosProcesos.RESUMENCONSOLIDADOFONASA.getId());
		
		DocumentoVO documentoResumenConsolidadoFonasa= documentService.getDocument(referenciaDocumentoResumenConsolidadoFonasa.getId());
		File file = documentService.createTemporalFile(documentoResumenConsolidadoFonasa.getName(),documentoResumenConsolidadoFonasa.getContent());
		
		String fileNameResumenConsolidadoFonasa = tmpDirDoc + File.separator + file.getName();
		
		//ORDINARIO OT
		ReferenciaDocumentoSummaryVO referenciaDocumentoOrdinarioOT =  getLastDocumentoSummaryByOrdinarioOrdenTransferenciaType(idOrdenTransferencia,
 																	   TipoDocumentosProcesos.PLANTILLAORDINARIOOREDENTRANSFERENCIA.getId());
		
		DocumentoVO documentoOrdinarioOT = documentService.getDocument(referenciaDocumentoOrdinarioOT.getId());
		
		String fileNameOrdinarioOT= tmpDirDoc + File.separator + documentoOrdinarioOT.getName();

		try {
			
			Usuario usuario = usuarioDAO.getUserByUsername(username);
			
			List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
						//SE ADJUNTA Resumen Consolidado Fonasa
			EmailService.Adjunto adjuntoResumenConsolidadoFonasa = new EmailService.Adjunto();
			adjuntoResumenConsolidadoFonasa.setDescripcion("Resumen Consolidado Fonasa");
			adjuntoResumenConsolidadoFonasa.setName(documentoResumenConsolidadoFonasa.getName());
			adjuntoResumenConsolidadoFonasa.setUrl((new File(fileNameResumenConsolidadoFonasa)).toURI().toURL());
			adjuntos.add(adjuntoResumenConsolidadoFonasa);
			
			//SE ADJUNTA Ordinario Orden Trasnferencia
			EmailService.Adjunto adjuntoOrdinarioOT = new EmailService.Adjunto();
			adjuntoOrdinarioOT.setDescripcion("Ordinario Orden Transferencia");
			adjuntoOrdinarioOT.setName(documentoOrdinarioOT.getName());
			adjuntoOrdinarioOT.setUrl((new File(fileNameOrdinarioOT)).toURI().toURL());
			adjuntos.add(adjuntoOrdinarioOT);
			
			emailService.sendMail(usuario.getEmail().getValor(), "Envio Ordinarios a Fonasa y Servicios de Salud", "Estimado " + username + ": <br /> <p> Se Adjunta Documentos</p>", adjuntos);
			System.out.println("enviarDecretoAporteEstatal se ejecuto correctamente");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Integer cargarDocumentoResumenConsolidado(Integer idOrdenTransferencia) {
		// TODO Auto-generated method stub
		 Integer idDocumento = 0;
		  OrdenTransferencia ordenTransferencia = ordenTransferenciaDAO.findById(idOrdenTransferencia);

		  for (DocumentoOt documentoOt : ordenTransferencia.getDocumentoOtCollection()) {
			
			  if(documentoOt.getIdTipoDocumento().getIdTipoDocumento() == TipoDocumentosProcesos.RESUMENCONSOLIDADOFONASA.getId())
			  {
				  idDocumento = documentoOt.getIdDocumento().getId();
			  }
		}
		  return idDocumento;
	}
	
	public Integer cargarDocumentoOrdinarioOrdenTransferencia(Integer idOrdenTransferencia) {
		// TODO Auto-generated method stub
		 Integer idDocumento = 0;
		  OrdenTransferencia ordenTransferencia = ordenTransferenciaDAO.findById(idOrdenTransferencia);

		  for (DocumentoOt documentoOt : ordenTransferencia.getDocumentoOtCollection()) {

				  if(documentoOt.getIdTipoDocumento().getIdTipoDocumento() == TipoDocumentosProcesos.PLANTILLAORDINARIOOREDENTRANSFERENCIA.getId())
				  {
					  idDocumento = documentoOt.getIdDocumento().getId();
				  }
			  
		}
		  return idDocumento;
	}
	
	public void moveToAlfresco(Integer idOrdenTransferencia, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Boolean lastVersion ) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderOT.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			
			 OrdenTransferencia ordenTransferencia = ordenTransferenciaDAO.findById(idOrdenTransferencia);
			documentService.createDocumentOrdinarioOrdenTransferencia(ordenTransferencia, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}

}