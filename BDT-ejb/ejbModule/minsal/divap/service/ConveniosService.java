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

import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.ReferenciaDocumentoDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.ConvenioDocumentoVO;
import minsal.divap.vo.ConveniosVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Convenio;
import cl.minsal.divap.model.DocumentoConvenio;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.Programa;
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
		Convenio convenio = conveniosDAO.findById(idconvenio);
		documentService.createDocumentConvenio(convenio, tipoDocumento, referenciaDocumentoId);
	}


	public void moveToBd(Integer idconvenio,Integer referenciaDocumentoId,TipoDocumentosProcesos tipoDocumento) {
		Convenio convenio = conveniosDAO.findById(idconvenio);
		documentService.createDocumentConvenio(convenio, tipoDocumento, referenciaDocumentoId);
	}


	private Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}



	public List<ConveniosVO> getConveniosForSubGridMuniServi(int programaID,int componenteID ,int comunaID, int establecimientoID, int muni1, int muni3) {
		List<Convenio> Convenios = this.conveniosDAO.getConveniosForSubGridMuni(programaID,componenteID,comunaID,establecimientoID, muni1,  muni3);
		List<ConveniosVO> componentesConvenios = new ArrayList<ConveniosVO>();

		for (Convenio conve : Convenios){
			ConveniosVO ConVO = new ConveniosVO();
			ConVO.setComunaNombre(conve.getIdComuna().getNombre());
			ConVO.setEstablecimientoNombre(conve.getIdEstablecimiento().getNombre());
			ConVO.setProgramaNombre(conve.getIdPrograma().getNombre());
			ConVO.setTipoSubtituloNombreSubtitulo(conve.getIdTipoSubtitulo().getNombreSubtitulo());
			ConVO.setConvenioMonto(new Integer(conve.getMonto()));
			ConVO.setIdConverio(conve.getIdConvenio());
			ConVO.setNumeroResolucion(conve.getNumeroResolucion());
			ConVO.setIdSubtitulo(conve.getIdTipoSubtitulo().getIdTipoSubtitulo());
			ConVO.setFecha(conve.getFecha().toString());
			componentesConvenios.add(ConVO);
		}
		return componentesConvenios;
	}

	public List<ConveniosVO> getConveniosForGridMuniServi(int programaID,int componenteID ,int comunaID, int establecimientoID, int muni1, int muni3) {
		List<Convenio> Convenios = this.conveniosDAO.getConveniosForGridMuniServi(programaID,componenteID,comunaID,establecimientoID, muni1,  muni3);
		List<ConveniosVO> componentesConvenios = new ArrayList<ConveniosVO>();

		for (Convenio conve : Convenios){
			ConveniosVO ConVO = new ConveniosVO();
			ConVO.setComunaNombre(conve.getIdComuna().getNombre());
			ConVO.setEstablecimientoNombre(conve.getIdEstablecimiento().getNombre());
			ConVO.setProgramaNombre(conve.getIdPrograma().getNombre());
			ConVO.setTipoSubtituloNombreSubtitulo(conve.getIdTipoSubtitulo().getNombreSubtitulo());
			ConVO.setConvenioMonto(new Integer(conve.getMonto()));
			ConVO.setIdConverio(conve.getIdConvenio());
			ConVO.setNumeroResolucion(conve.getNumeroResolucion());
			ConVO.setIdSubtitulo(conve.getIdTipoSubtitulo().getIdTipoSubtitulo());
			componentesConvenios.add(ConVO);
		}
		return componentesConvenios;
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
		Convenio Convenios = this.conveniosDAO.getConveniosById(parseInt);
		ConveniosVO componentesConvenios = new ConveniosVO();
		componentesConvenios.setComunaNombre(Convenios.getIdComuna().getNombre());
		componentesConvenios.setEstablecimientoNombre(Convenios.getIdEstablecimiento().getNombre());
		componentesConvenios.setProgramaNombre(Convenios.getIdPrograma().getNombre());
		componentesConvenios.setTipoSubtituloNombreSubtitulo(Convenios.getIdTipoSubtitulo().getNombreSubtitulo());
		componentesConvenios.setConvenioMonto(new Integer(Convenios.getMonto()));
		componentesConvenios.setIdConverio(Convenios.getIdConvenio());
		componentesConvenios.setNumeroResolucion(Convenios.getNumeroResolucion());
		componentesConvenios.setIdSubtitulo(Convenios.getIdTipoSubtitulo().getIdTipoSubtitulo());
		Collection<Componente>    con = Convenios.getIdPrograma().getComponentes();
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
		Convenio con = new Convenio();
		Convenio cons = new Convenio();
		Date date = new Date();

		Componente compo = new Componente();
		compo = componenteDAO.getComponenteByID(componenete);
		Programa programa =  new Programa();
		programa = programasDAO.getProgramaById(idPrograma);
		Establecimiento establecimiento =  new Establecimiento();
		establecimiento = establecimientosDAO.getEstablecimientoById(idEstable);
		Comuna comuna =  new Comuna();
		comuna = comunaDAO.getComunaById(idComuna);
		TipoSubtitulo tiposub = new TipoSubtitulo();
		tiposub = tipoSubtituloDAO.getTipoSubtituloById(idSubti);


		con.setIdPrograma(programa);
		con.setIdComuna(comuna);
		con.setIdEstablecimiento(establecimiento);
		con.setIdTipoSubtitulo(tiposub);
		con.setMonto((short) monto);
		con.setNumeroResolucion(numResolucion);
		con.setComponente(compo);
		con.setFecha(date);
		con.setAprobacion(false);
		cons =conveniosDAO.save(con);

		return cons.getIdConvenio();
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

			//marcoPresupuestario = caja.getMarcoPresupuestario();
		}

		Integer total=0;
		List<Convenio> Convenios = this.conveniosDAO.getByIDSubComponenteComunaProgramaAnoAprobacion(sub,componenteSeleccionado,  comunaSeleccionada, establecimientoSeleccionado, ano, programa,apro);
		List<ConveniosVO> componentesConvenios = new ArrayList<ConveniosVO>();


		for (Convenio conve : Convenios){
			Integer aux = new Integer(conve.getMonto());

			total = total + aux;
		}
		for (Convenio conve : Convenios){
			ConveniosVO ConVO = new ConveniosVO();
			ConVO.setComunaNombre(conve.getIdComuna().getNombre());
			ConVO.setEstablecimientoNombre(conve.getIdEstablecimiento().getNombre());
			ConVO.setProgramaNombre(conve.getIdPrograma().getNombre());
			ConVO.setTipoSubtituloNombreSubtitulo(conve.getIdTipoSubtitulo().getNombreSubtitulo());
			ConVO.setConvenioMonto(new Integer(conve.getMonto()));
			ConVO.setIdConverio(conve.getIdConvenio());
			ConVO.setNumeroResolucion(conve.getNumeroResolucion());
			ConVO.setIdSubtitulo(conve.getIdTipoSubtitulo().getIdTipoSubtitulo());
			ConVO.setFecha(conve.getFecha().toString());
			ConVO.setTotal(total);
			ConVO.setMarcoPresupuestario( Integer.valueOf(marcoPresupuestario.intValue()));
			componentesConvenios.add(ConVO);


		}
		return componentesConvenios;
	}




}
