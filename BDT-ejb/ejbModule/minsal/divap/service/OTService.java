package minsal.divap.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.TratamientoOrdenDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionRecursosPercapitaSheetExcel;
import minsal.divap.model.mappers.AsignacionDistribucionPercapitaMapper;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.OTRevisarAntecedentesVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaServicioCore;
import cl.minsal.divap.model.Remesa;
import cl.minsal.divap.model.Seguimiento;

@Stateless
@LocalBean
public class OTService {
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
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
	private MesService mesService;
	@EJB
	private RemesaService remesaService;
	@EJB
	private ProgramasDAO programaDAO;
	
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

	
	/**
	 * METODO EL CUAL BUSCARA LOS DATOS CORRESPONDIENTE A LAS ACTIVIDADES:
	 * Revisar Antecedentes Línea Programática / Revisar Antecedentes Per Cápita / Revisar Antecedentes Leyes
	 */
	public List<AsignacionDistribucionPerCapitaVO> findDatos(Integer idPrograma) {
		
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = null;
		antecendentesComunaCalculado = tratamientoOrdenDAO.findAntecendentes(idPrograma);
		
		List<AsignacionDistribucionPerCapitaVO> antecedentesCalculados = new ArrayList<AsignacionDistribucionPerCapitaVO>();
		
		if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
			
			for(AntecendentesComunaCalculado antecendenteComunaCalculado : antecendentesComunaCalculado){
				
				AsignacionDistribucionPerCapitaVO asignacionDistribucionPerCapitaVO = new AsignacionDistribucionPercapitaMapper().getBasic(antecendenteComunaCalculado);
				
				if(asignacionDistribucionPerCapitaVO != null){
					
					antecedentesCalculados.add(asignacionDistribucionPerCapitaVO);
				}
			}
		}
		return antecedentesCalculados;
	}
	
	public Integer getIdPlantillaRecursosPerCapita(){
		Integer plantillaId =null;// documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPOBLACIONINSCRITA);
		if(plantillaId == null){
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
			AsignacionRecursosPercapitaSheetExcel asignacionRecursosPercapitaSheetExcel = new AsignacionRecursosPercapitaSheetExcel(headers, servicios);
			generadorExcel.addSheet( asignacionRecursosPercapitaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType,folderTemplateTo );//(folderTemplatePercapita);
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
	
	private Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}
	public Integer createSeguimientoOT(Integer idOT, TareasSeguimiento tareaSeguimiento, String subject, String body, String username, List<String> para, List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos){
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
		return  1;//distribucionInicialPercapitaDAO.createSeguimiento(idDistribucionInicialPercapita, seguimiento);
	}

	public List<SeguimientoVO> getBitacora(
			Integer idDistribucionInicialPercapita,
			TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacora(idDistribucionInicialPercapita, tareaSeguimiento);
	}

	
	
	///***** METODOS ACTIVIDAD REVISAR ANTECENDETES ******
	private void calcularDatosOTRevisarAntecedentesVO(OTRevisarAntecedentesVO otRevisarAntecedentesVO,Collection<Remesa>listaRemesa)
	{
		//MARCO PRESUPUESTARIO
		long marcoPresupuestario = 1000000;
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
			}
			
		}	

		long id=0;
		for (OTRevisarAntecedentesVO otRevisarAntecedentesVO : lista) {
			
			otRevisarAntecedentesVO.setId(id);
			id++;
		}
		
		return lista;
		
	}
	
	public List<OTRevisarAntecedentesVO> obtenerListaSubtitulo21VOPorPrograma(Integer idProgramaAno)
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
			
			Integer anio = 2014;// otRevisarAntecedentesVO.getAnio();

			for (Remesa remesa : listaRemesa) {
				
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
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesEnero(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(1),
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
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesFebrero(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(2),
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
				
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesMarzo(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(3),
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
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesAbril(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(4),
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
				
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesMayo(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(5),
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
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesJunio(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(6),
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
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesJulio(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(7),
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
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesAgosto(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(8),
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
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesSeptiembre(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(9),
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
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesOctubre(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(10),
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
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesNoviembre(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(11),
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
				
				Remesa remesa = new Remesa(otRevisarAntecedentesVO.getIdRemesaMesDiciembre(),programaAno,otRevisarAntecedentesVO.getServicioSalud(),2014,mesService.getMesPorID(12),
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
	
	///*****FIN METODOS ACTIVIDAD REVISAR ANTECENDETES ******

}
