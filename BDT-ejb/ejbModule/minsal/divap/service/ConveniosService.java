package minsal.divap.service;

import java.io.File;
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

import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.model.mappers.ConvenioComunaMapper;
import minsal.divap.model.mappers.ConvenioServicioMapper;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.ConvenioDocumentoVO;
import minsal.divap.vo.ConveniosVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ResolucionConveniosComunaVO;
import minsal.divap.vo.ResolucionConveniosServicioVO;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.ConvenioServicioComponente;
import cl.minsal.divap.model.DocumentoConvenio;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.TipoSubtitulo;



@Stateless
@LocalBean
public class ConveniosService {

	@EJB
	private CajaDAO cajaDAO;
	@EJB
	private ComponenteDAO componenteDAO;
	@EJB
	private TipoSubtituloDAO tipoSubtituloDAO;
	@EJB
	private EstablecimientosDAO establecimientosDAO;
	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private ComunaDAO comunaDAO;
	@EJB
	private DocumentDAO documentDAO;
	@EJB
	private ConveniosDAO conveniosDAO;
	@EJB
	private DocumentService documentService;
	@EJB
	private AlfrescoService alfrescoService;
	@Resource(name="folderConvenio")
	private String folderConvenio;

	public void moveToAlfresco(Integer idconvenio, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());

		BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderConvenio.replace("{ANO}", getAnoCurso().toString()));
		System.out.println("response upload template --->"+response);
		documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
		ConvenioComuna convenio = conveniosDAO.findById(idconvenio);
		documentService.createDocumentConvenio(convenio, tipoDocumento, referenciaDocumentoId);
	}


	public void moveToBd(Integer idconvenio,Integer referenciaDocumentoId,TipoDocumentosProcesos tipoDocumento) {
		ConvenioComuna convenio = conveniosDAO.findById(idconvenio);
		documentService.createDocumentConvenio(convenio, tipoDocumento, referenciaDocumentoId);
	}


	private Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}

	public void updateConvenioByIdAprobacion(int id,boolean apro) {
		this.conveniosDAO.updateConvenioByIdAprobacion(id,apro);
	}

	public void setConvenioById(int id,int numeroResolucion,int monto) {
		this.conveniosDAO.setConvenioById(id,numeroResolucion,monto);
	}

	public void setDocumentoConvenioById(String nodoref,String filename,int id) {
		conveniosDAO.setDocumentoConvenioById(nodoref,filename,id);
	}



	public ConveniosVO getConvenioById(int parseInt) {
		ConvenioComuna Convenios = this.conveniosDAO.getConveniosById(parseInt);
		ConveniosVO componentesConvenios = new ConveniosVO();
		componentesConvenios.setComunaNombre(Convenios.getIdComuna().getNombre());
		componentesConvenios.setProgramaNombre(Convenios.getIdPrograma().getPrograma().getNombre());
//		componentesConvenios.setTipoSubtituloNombreSubtitulo(Convenios.getIdTipoSubtitulo().getNombreSubtitulo());
//		componentesConvenios.setConvenioMonto(new Integer(Convenios.getMonto()));
		componentesConvenios.setIdConverio(Convenios.getIdConvenioComuna());
		componentesConvenios.setNumeroResolucion(Convenios.getNumeroResolucion());
//		componentesConvenios.setIdSubtitulo(Convenios.getIdTipoSubtitulo().getIdTipoSubtitulo());
		Collection<Componente>    con = Convenios.getIdPrograma().getPrograma().getComponentes();
		for (Componente conve : con){
			componentesConvenios.setComponente(conve.getNombre());
		}



		return componentesConvenios;
	}


	public List<ConvenioDocumentoVO> getDocumentById(int parseInt) {
		List<DocumentoConvenio> ConveniosDocumentos = this.documentDAO.getDocumentPopUpConvenio(parseInt);
		List<ConvenioDocumentoVO> documentosConvenio =  new ArrayList<ConvenioDocumentoVO>();
		for (DocumentoConvenio convedoc : ConveniosDocumentos){

			ConvenioDocumentoVO ConDocVO = new ConvenioDocumentoVO();
			ConDocVO.setNombreDocu(convedoc.getDocumento().getPath());
			ConDocVO.setId(convedoc.getDocumento().getId());
			documentosConvenio.add(ConDocVO);
		}
		return documentosConvenio;




	}



	public Integer  convenioSave(int idPrograma,int idEstable,int idComuna,int idSubti, int monto,int componenete,int numResolucion){
		ConvenioComuna con = new ConvenioComuna();
		ConvenioComuna cons = new ConvenioComuna();
		Date date = new Date();

		Componente compo = new Componente();
		compo = componenteDAO.getComponenteByID(componenete);
		ProgramaAno programaAno = programasDAO.getProgramasByIdProgramaAno(idPrograma);
		Comuna comuna =  new Comuna();
		comuna = comunaDAO.getComunaById(idComuna);
		TipoSubtitulo tiposub = new TipoSubtitulo();
		tiposub = tipoSubtituloDAO.getTipoSubtituloById(idSubti);


		con.setIdPrograma(programaAno);
		con.setIdComuna(comuna);
//		con.setIdTipoSubtitulo(tiposub);
//		con.setMonto(monto);
		con.setNumeroResolucion(numResolucion);
//		con.setComponente(compo);
		con.setFecha(date);
		con.setAprobacion(false);
		cons =conveniosDAO.save(con);

		return cons.getIdConvenioComuna();
	}


	public ReferenciaDocumento getReferenciaDocumentoByIdConvenio(Integer idConverio) {

		ReferenciaDocumento ref = new ReferenciaDocumento();

		ref = conveniosDAO.getNodeByIdConvenio(idConverio);


		return ref;
	}

	public List<ProgramaVO> getProgramasByUserAno(String username) {
		List<ProgramaAno> programas = this.programasDAO.getProgramasByUserAno(username, getAnoCurso());
		List<ProgramaVO> result = new ArrayList<ProgramaVO>();
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				result.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return result;
	}



	public List<ConveniosVO> getConvenioBySubComponenteComunaEstableAnoAprobacion(int sub,
			String componenteSeleccionado, String comunaSeleccionada,
			String establecimientoSeleccionado, int ano,Integer programa,boolean apro ) {

		List<Caja> caja =	this.cajaDAO.getByIDSubComponenteComunaProgramaAno(sub,componenteSeleccionado,  comunaSeleccionada, establecimientoSeleccionado, ano, programa);

		Long marcoPresupuestario=123L;
		for (Caja caj: caja){

			//marcoPresupuestario = caj.getMarcoPresupuestario();
		}

		Integer total=0;
		List<ConvenioComuna> Convenios = this.conveniosDAO.getByIDSubComponenteComunaProgramaAnoAprobacion(sub,componenteSeleccionado,  comunaSeleccionada, establecimientoSeleccionado, ano, programa,apro);
		List<ConveniosVO> componentesConvenios = new ArrayList<ConveniosVO>();


		for (ConvenioComuna conve : Convenios){
//			Integer aux = new Integer(conve.getMonto());
//
//			total = total + aux;
		}
		for (ConvenioComuna conve : Convenios){
			ConveniosVO ConVO = new ConveniosVO();
			ConVO.setComunaNombre(conve.getIdComuna().getNombre());
			ConVO.setProgramaNombre(conve.getIdPrograma().getPrograma().getNombre());
//			ConVO.setTipoSubtituloNombreSubtitulo(conve.getIdTipoSubtitulo().getNombreSubtitulo());
//			ConVO.setConvenioMonto(new Integer(conve.getMonto()));
			ConVO.setIdConverio(conve.getIdConvenioComuna());
			ConVO.setNumeroResolucion(conve.getNumeroResolucion());
//			ConVO.setIdSubtitulo(conve.getIdTipoSubtitulo().getIdTipoSubtitulo());
			ConVO.setFecha(conve.getFecha().toString());
			ConVO.setTotal(total);
			ConVO.setMarcoPresupuestario( Integer.valueOf(marcoPresupuestario.intValue()));
			componentesConvenios.add(ConVO);


		}
		return componentesConvenios;
	}

	public void cambiarEstadoPrograma(Integer programaSeleccionado, EstadosProgramas estadoPrograma) {
		System.out.println("programaSeleccionado-->" + programaSeleccionado + " estadoPrograma.getId()-->"+estadoPrograma.getId());
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(programaSeleccionado);
		programaAno.setEstadoConvenio(new EstadoPrograma(estadoPrograma.getId()));
		System.out.println("Cambia estado ok");
	}

	public void notificarServicioSalud(Integer programaSeleccionado) {
		System.out.println("ConveniosService::notificarServicioSalud->"+programaSeleccionado);		
	}


	public Integer generarResolucionDisponibilizarAlfresco(
			Integer programaSeleccionado) {
		System.out.println("ConveniosService::generarResolucionDisponibilizarAlfresco->"+programaSeleccionado);
		return 1;
	}

	public void administrarVersionesAlfresco(Integer programaSeleccionado) {
		System.out.println("ConveniosService::administrarVersionesAlfresco->"+programaSeleccionado);		
	}
	
	public List<ResolucionConveniosComunaVO> getResolucionConveniosMunicipal(Integer idServicio, Integer idProgramaAno, Integer componenteSeleccionado, Integer comunaSeleccionada) {
		List<ResolucionConveniosComunaVO> resolucionesConvenios = new ArrayList<ResolucionConveniosComunaVO>();
		List<ConvenioComuna> convenios = null;
		if(componenteSeleccionado == null && comunaSeleccionada == null){
			List<Comuna> comunas = comunaDAO.getComunasByServicio(idServicio);
			List<Integer> idComunas = new ArrayList<Integer>();
			if(comunas != null && comunas.size() > 0){
				for(Comuna comuna : comunas){
					idComunas.add(comuna.getId());
				}
			}
			convenios = this.conveniosDAO.getByProgramaAnoComunas(idProgramaAno, idComunas);
		}else{
			if(componenteSeleccionado != null && comunaSeleccionada != null){
				List<Integer> idComponentes = new ArrayList<Integer>();
				idComponentes.add(componenteSeleccionado);
				List<Integer> idComunas = new ArrayList<Integer>();
				idComunas.add(comunaSeleccionada);
				convenios = this.conveniosDAO.getByProgramaAnoComponentesComunas(idProgramaAno, idComponentes, idComunas);
			} else {
				if(componenteSeleccionado == null){
					List<Integer> idComunas = new ArrayList<Integer>();
					idComunas.add(comunaSeleccionada);
					convenios = this.conveniosDAO.getByProgramaAnoComunas(idProgramaAno, idComunas);
				}else{
					List<Integer> idComponentes = new ArrayList<Integer>();
					idComponentes.add(componenteSeleccionado);
					convenios = this.conveniosDAO.getByProgramaAnoComponentes(idProgramaAno, idComponentes);
				}
			}
		}
		if(convenios != null && convenios.size() > 0){
			for(ConvenioComuna convenioComuna : convenios){
				resolucionesConvenios.add(new ConvenioComunaMapper().getBasic(convenioComuna));
			}
		}
		return resolucionesConvenios;
	}

	public List<ResolucionConveniosServicioVO> getResolucionConveniosServicio(Integer idServicio, Integer idProgramaAno, Integer componenteSeleccionado, Integer establecimientoSeleccionado) {
		List<ResolucionConveniosServicioVO> resolucionesConvenios = new ArrayList<ResolucionConveniosServicioVO>();
		List<ConvenioServicio> convenios = null;
		if(componenteSeleccionado == null && establecimientoSeleccionado == null){
			List<Establecimiento> establecimientos = establecimientosDAO.getEstablecimientosByServicio(idServicio);
			List<Integer> idEstablecimientos = new ArrayList<Integer>();
			if(establecimientos != null && establecimientos.size() > 0){
				for(Establecimiento establecimiento : establecimientos){
					idEstablecimientos.add(establecimiento.getId());
				}
			}
			convenios = this.conveniosDAO.getByProgramaAnoEstablecimientos(idProgramaAno, idEstablecimientos);
		}else{
			if(componenteSeleccionado != null && establecimientoSeleccionado != null){
				List<Integer> idComponentes = new ArrayList<Integer>();
				idComponentes.add(componenteSeleccionado);
				List<Integer> idEstablecimientos = new ArrayList<Integer>();
				idEstablecimientos.add(establecimientoSeleccionado);
				convenios = this.conveniosDAO.getByProgramaAnoComponentesEstablecimientos(idProgramaAno, idComponentes, idEstablecimientos);
			} else {
				if(componenteSeleccionado == null){
					List<Integer> idEstablecimientos = new ArrayList<Integer>();
					idEstablecimientos.add(establecimientoSeleccionado);
					convenios = this.conveniosDAO.getByProgramaAnoEstablecimientos(idProgramaAno, idEstablecimientos);
				}else{
					List<Integer> idComponentes = new ArrayList<Integer>();
					idComponentes.add(componenteSeleccionado);
					convenios = this.conveniosDAO.getConvenioServicioByProgramaAnoComponentes(idProgramaAno, idComponentes);
				}
			}
		}
		if(convenios != null && convenios.size() > 0){
			for(ConvenioServicio convenioServicio : convenios){
				resolucionesConvenios.add(new ConvenioServicioMapper().getBasic(convenioServicio));
			}
		}
		return resolucionesConvenios;
	}
	
	private ConvenioComuna createConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idComuna){
		ConvenioComuna convenioComuna = new ConvenioComuna();
		Componente componente = componenteDAO.getComponenteByID(idComponente);
		Comuna comuna = comunaDAO.getComunaById(idComuna);
		convenioComuna.setIdComuna(comuna);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		convenioComuna.setIdPrograma(programaAno);
		TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloById(idSubtitulo);
		ConvenioComunaComponente convenioComunaComponente = new ConvenioComunaComponente();
		convenioComunaComponente.setComponente(componente);
		convenioComunaComponente.setSubtitulo(tipoSubtitulo);
		return convenioComuna;
	}
	
	private ConvenioServicio createConvenioServicio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idEstablecimiento) {
		ConvenioServicio convenioServicio = new ConvenioServicio();
		Componente componente = componenteDAO.getComponenteByID(idComponente);
		Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(idEstablecimiento);
		convenioServicio.setIdEstablecimiento(establecimiento);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		convenioServicio.setIdPrograma(programaAno);
		TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloById(idSubtitulo);
		ConvenioServicioComponente convenioServicioComponente = new ConvenioServicioComponente();
		convenioServicioComponente.setComponente(componente);
		convenioServicioComponente.setSubtitulo(tipoSubtitulo);
		return convenioServicio;
	}


	public void crearConveniosMunicipal(Integer idServicio, Integer idProgramaAno) {
		Map<Integer, List<Integer>> componentesBySubtitulo = new HashMap<Integer, List<Integer>>();
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		List<Comuna> comunas = comunaDAO.getComunasByServicio(idServicio);
		List<Integer> idComunas = new ArrayList<Integer>();
		if(comunas != null && comunas.size() > 0){
			for(Comuna comuna : comunas){
				idComunas.add(comuna.getId());
			}
		}
		for(Componente componente : programaAno.getPrograma().getComponentes()){
			for(ComponenteSubtitulo componenteSubtitulo : componente.getComponenteSubtitulos()){
				if(Subtitulo.SUBTITULO24.getId().equals(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo())){
					if(componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
						List<Integer> idComponentes = new ArrayList<Integer>();
						idComponentes.add(componente.getId());
						componentesBySubtitulo.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), idComponentes);
					}else{
						componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(componente.getId());
					}
				}
			}
		}
		for (Map.Entry<Integer, List<Integer>> entry : componentesBySubtitulo.entrySet()) {
		    Integer idSubtitulo = entry.getKey();
		    List<Integer> idComponentes = entry.getValue();
		    for(Integer idComponente : idComponentes){
		    	for(Integer idComuna : idComunas){
		    		ConvenioComuna convenioComuna = componenteDAO.getConvenioComunaByProgramaAnoComponenteSubtituloComuna(idProgramaAno, idComponente, idSubtitulo, idComuna);
		    		if(convenioComuna == null){
		    			convenioComuna = createConvenio(idProgramaAno, idComponente, idSubtitulo, idComuna);
						conveniosDAO.save(convenioComuna);
		    		}
		    	}
		    }
		}
		
	}

	public void crearConveniosServicios(Integer idServicio, Integer idProgramaAno) {
		Map<Integer, List<Integer>> componentesBySubtitulo = new HashMap<Integer, List<Integer>>();
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		List<Establecimiento> establecimientos = establecimientosDAO.getEstablecimientosByServicio(idServicio);
		List<Integer> idEstablecimientos = new ArrayList<Integer>();
		if(establecimientos != null && establecimientos.size() > 0){
			for(Establecimiento establecimiento : establecimientos){
				idEstablecimientos.add(establecimiento.getId());
			}
		}
		for(Componente componente : programaAno.getPrograma().getComponentes()){
			for(ComponenteSubtitulo componenteSubtitulo : componente.getComponenteSubtitulos()){
				if(!Subtitulo.SUBTITULO24.getId().equals(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo())){
					if(componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
						List<Integer> idComponentes = new ArrayList<Integer>();
						idComponentes.add(componente.getId());
						componentesBySubtitulo.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), idComponentes);
					}else{
						componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(componente.getId());
					}
				}
			}
		}
		for (Map.Entry<Integer, List<Integer>> entry : componentesBySubtitulo.entrySet()) {
		    Integer idSubtitulo = entry.getKey();
		    List<Integer> idComponentes = entry.getValue();
		    for(Integer idComponente : idComponentes){
		    	for(Integer idEstablecimiento : idEstablecimientos){
		    		ConvenioServicio convenioServicio = componenteDAO.getConvenioServicioByProgramaAnoComponenteSubtituloEstablecimiento(idProgramaAno, idComponente, idSubtitulo, idEstablecimiento);
		    		if(convenioServicio == null){
		    			convenioServicio = createConvenioServicio(idProgramaAno, idComponente, idSubtitulo, idEstablecimiento);
						conveniosDAO.save(convenioServicio);
		    		}
		    	}
		    }
		}
	}

}
