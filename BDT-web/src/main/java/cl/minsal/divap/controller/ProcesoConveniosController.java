package cl.minsal.divap.controller;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.AlfrescoService;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.ComunaService;
import minsal.divap.service.ConveniosService;
import minsal.divap.service.EstablecimientosService;
import minsal.divap.service.ProgramasService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.ConvenioDocumentoVO;
import minsal.divap.vo.ConveniosVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.ProgramaVO;

import org.primefaces.model.UploadedFile;

import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.pojo.IngresoResolucionPojo;
import cl.redhat.bandejaTareas.controller.BaseController;

@Named ( "procesoConveniosController" ) 
@ViewScoped 
public class ProcesoConveniosController extends BaseController implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	private List<ProgramaVO> programas;
	private List<ComunaVO> comunas;
	private List<EstablecimientoVO> establecimientos;
	private List<ComponentesVO> componentes;
	private List<ConvenioDocumentoVO> documentos;
	private List<ConveniosVO> convenios;
	private ConveniosVO convenio;
	private ProgramaVO programa;
	private String convenioSeleccionado;
	private String programaSeleccionado;
	private String comunaSeleccionada;
	private String componenteSeleccionado;
	private String establecimientoSeleccionado;
	private String documentoSeleccionado;
	private File file;
	private UploadedFile plantillaFile;
	
	
	@EJB
	private ProgramasService programasService;
	@EJB
	private ComponenteService componenteService;
	@EJB
	private ComunaService comunaService;
	@EJB
	private EstablecimientosService establecimientosService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private ConveniosService aPSConveniosService;


	
	
	
	List<IngresoResolucionPojo> listResoluciones = new ArrayList<IngresoResolucionPojo>();

	public void downloadDocAlfresco(String id ){
		
		
		String mensaje="";	
	try{
		mensaje = "Archivo Descargado";
		FacesMessage msg = new FacesMessage(mensaje);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		ReferenciaDocumento ref = new ReferenciaDocumento();
		ref =aPSConveniosService.getReferenciaDocumentoByIdConvenio(Integer.parseInt(id));
		System.out.println("felipe :"+ref.getId());
		Integer docDownload = Integer.valueOf(ref.getId());
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
	}catch(Exception e) {
		mensaje ="EL convenio no  conteine archivo relacionado";
		FacesMessage msg = new FacesMessage(mensaje);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		throw new RuntimeException(e);
	}
				
	}
	
	
		public void uploadPopup(){
			String mensaje="";	
				
			if(documentoSeleccionado=="" && plantillaFile != null){
				 String filename = plantillaFile.getFileName();
					byte[] contentAttachedFile = plantillaFile.getContents();
					Integer docAttachedFile = persistFile(filename,	contentAttachedFile);
					
				try{
					 mensaje="El Archivo  fue cargado con �xito.";	
					
					//if(getComunaSeleccionada() != null && getProgramaSeleccionado() != null && getComponenteSeleccionado() != null  && getEstablecimientoSeleccionado() != null){
						Integer idconvenioNew=	aPSConveniosService.convenioSave(Integer.parseInt(getProgramaSeleccionado()),
						Integer.parseInt(getEstablecimientoSeleccionado()),
						Integer.parseInt(getComunaSeleccionada()),
						Integer.parseInt(convenio.getIdSubtitulo().toString()),
						Integer.parseInt(convenio.getConvenioMonto().toString()),
						Integer.parseInt(getComponenteSeleccionado()),
						Integer.parseInt(convenio.getNumeroResolucion().toString())
						);
				aPSConveniosService.moveToAlfresco(idconvenioNew, docAttachedFile, TipoDocumentosProcesos.CONVENIOS);
					//aPSConveniosService.setConvenioById(convenio.getIdConverio(),convenio.getNumeroResolucion(),convenio.getConvenioMonto());
				//convenio.getTipoSubtituloNombreSubtitulo();,
					FacesMessage msg = new FacesMessage(mensaje);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					
				} catch (Exception e) {
					mensaje ="Los Archivos no fueron cargados.";
					FacesMessage msg = new FacesMessage(mensaje);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					
					throw new RuntimeException(e);
					
				}
			}else if(documentoSeleccionado !="" && plantillaFile == null){
				try{
					mensaje = "Los datos fueron actualizados correctamente.";
					
					Integer idconvenioNew=	aPSConveniosService.convenioSave(Integer.parseInt(getProgramaSeleccionado()),
							Integer.parseInt(getEstablecimientoSeleccionado()),
							Integer.parseInt(getComunaSeleccionada()),
							Integer.parseInt(convenio.getIdSubtitulo().toString()),
							Integer.parseInt(convenio.getConvenioMonto().toString()),
							Integer.parseInt(getComponenteSeleccionado()),
							Integer.parseInt(convenio.getNumeroResolucion().toString())
							);
					aPSConveniosService.moveToBd(idconvenioNew,Integer.parseInt(getDocumentoSeleccionado()), TipoDocumentosProcesos.CONVENIOS );
					//actualiza convenio en la bd
					//aPSConveniosService.setConvenioById(convenio.getIdConverio(),convenio.getNumeroResolucion(),convenio.getConvenioMonto());
					FacesMessage msg = new FacesMessage(mensaje);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					
				} catch (Exception e) {
					
					mensaje ="Los Archivos no fueron actualizados.";
					FacesMessage msg = new FacesMessage(mensaje);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					throw new RuntimeException(e);
					
				}
				
				
			}
			
		
	}
	

	public File getFile() {
		
		
		return file;
	}


	public void setFile(File file) {
		
		this.file = file;
	}





	
	public void cargaConvenio(String pramConvenio){
		convenio = aPSConveniosService.getConvenioById(Integer.parseInt(pramConvenio));
		}
	
	


	public List<IngresoResolucionPojo> getListResoluciones() {
		return listResoluciones;
	}

	public void setListResoluciones( List<IngresoResolucionPojo> listResoluciones ) {
		this.listResoluciones = listResoluciones;
	}

	

	public List<ConveniosVO> getListResolucionesMuni() {
	
		
		Integer muni1 = 1;// representa a munisipal
		Integer muni3 = 3;// representa a mixtos
		if(getComunaSeleccionada() != null && getProgramaSeleccionado() != null && getComponenteSeleccionado() != null  && getEstablecimientoSeleccionado() != null){
			convenios = aPSConveniosService.getConveniosForSubGridMuniServi(Integer.parseInt(programaSeleccionado),Integer.parseInt(componenteSeleccionado),Integer.parseInt(comunaSeleccionada),Integer.parseInt(establecimientoSeleccionado),muni1,muni3);
					
			
		}else{
			convenios = new ArrayList<ConveniosVO>();
		}
			
		
		
		return convenios;
	}

	public List<ConveniosVO> getListResolucionesServi() {
	
		
		Integer muni2 = 2;// representa a Servicios
		Integer muni3 = 3;// representa a mixtos
		if(getComunaSeleccionada() != null && getProgramaSeleccionado() != null && getComponenteSeleccionado() != null  && getEstablecimientoSeleccionado() != null){
			convenios = aPSConveniosService.getConveniosForSubGridMuniServi(Integer.parseInt(programaSeleccionado),Integer.parseInt(componenteSeleccionado),Integer.parseInt(comunaSeleccionada),Integer.parseInt(establecimientoSeleccionado),muni2,muni3);
					
			
		}else{
			convenios = new ArrayList<ConveniosVO>();
		}
			
		
		
		return convenios;
	}

	
	
	
	public List<ConveniosVO> getConveniosMuni() {
		Integer muni1 = 1;//1 representa a munisipal
		Integer muni3 = 3;// representa a mixtos
		if(getComunaSeleccionada() != null && getProgramaSeleccionado() != null && getComponenteSeleccionado() != null  && getEstablecimientoSeleccionado() != null){
			convenios = aPSConveniosService.getConveniosForGridMuniServi(Integer.parseInt(programaSeleccionado),Integer.parseInt(componenteSeleccionado),Integer.parseInt(comunaSeleccionada),Integer.parseInt(establecimientoSeleccionado),muni1,muni3);
					
			
		}else{
			convenios = new ArrayList<ConveniosVO>();
		}
			
		
		return convenios;
	}


	
	public List<ConveniosVO> getConveniosServi() {
	
		
		Integer muni2 = 2;//1 representa a Servicios
		Integer muni3 = 3;// representa a mixtos
		if(getComunaSeleccionada() != null && getProgramaSeleccionado() != null && getComponenteSeleccionado() != null  && getEstablecimientoSeleccionado() != null){
			convenios = aPSConveniosService.getConveniosForGridMuniServi(Integer.parseInt(programaSeleccionado),Integer.parseInt(componenteSeleccionado),Integer.parseInt(comunaSeleccionada),Integer.parseInt(establecimientoSeleccionado),muni2,muni3);
					
			
		}else{
			convenios = new ArrayList<ConveniosVO>();
		}
			
		
		return convenios;
	}



	public List<EstablecimientoVO> getEstablecimientos() {
		
		if(getComunaSeleccionada() != null){
			establecimientos = establecimientosService.getEstablecimientosByComuna(Integer.parseInt(comunaSeleccionada));
					
			
		}else{
			establecimientos = new ArrayList<EstablecimientoVO>();
		}
				
		
		return establecimientos;
	}



	public List<ComunaVO> getComunas() {
		if(comunas == null){
			comunas = comunaService.getComunas();
		}
		
	
		return comunas;
	}

	public List<ConvenioDocumentoVO> getDocumentos() {
		

		
		if(convenio != null && convenio.getIdConverio()!=null){
			documentos = aPSConveniosService.getDocumentById(convenio.getIdConverio());
		}else{
			documentos = new ArrayList<ConvenioDocumentoVO>();
		}
		
		
		return documentos;
	}


	
	
	public List<ComponentesVO> getComponentes() {
		
		if(getProgramaSeleccionado() != null){
			componentes = componenteService.getComponenteByPrograma(Integer.parseInt(programaSeleccionado));
			
		}else{
			componentes = new ArrayList<ComponentesVO>();
		}
				
		return componentes;
	}
	
	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			programas = programasService.getProgramasByUser(getLoggedUsername());
		}
		return programas;
	}
	
	
	
	
	@PostConstruct public void init() {
		/*if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}*/
		Random rnd = new Random();
		
		IngresoResolucionPojo irp = new IngresoResolucionPojo();
		irp.setComuna("Macul");
		irp.setEstablecimiento("Hospital Cordillera");
		irp.setFecha(new Date());
		irp.setComponente("Especialidades Ambulatorias");
		irp.setSubtitulo(21);
		irp.setMonto(Long.valueOf(rnd.nextInt(999999)));
		irp.setArchivo("ruta:/archivo/alfresco");
		irp.setResolucion(String.valueOf(rnd.nextInt(99999)));
		
		listResoluciones.add(irp);
		
		irp = new IngresoResolucionPojo();
		irp.setComuna("Macul");
		irp.setEstablecimiento("Hospital Cordillera");
		irp.setComponente("Especialidades Ambulatorias");
		irp.setSubtitulo(22);
		
		listResoluciones.add(irp);
		
		irp = new IngresoResolucionPojo();
		irp.setComuna("Macul");
		irp.setEstablecimiento("Hospital Cordillera");
		irp.setComponente("Especialidades Ambulatorias");
		irp.setSubtitulo(24);
		
		listResoluciones.add(irp);
		
		irp = new IngresoResolucionPojo();
		irp.setComuna("Macul");
		irp.setEstablecimiento("Hospital Cordillera");
		irp.setComponente("Especialidades Ambulatorias");
		irp.setSubtitulo(29);
		
		listResoluciones.add(irp);
		
		irp = new IngresoResolucionPojo();
		irp.setComuna("Macul");
		irp.setEstablecimiento("Hospital Cordillera");
		irp.setFecha(new Date());
		irp.setComponente("Procedimientos Cutáneos Quirúrgicos de Baja");
		irp.setSubtitulo(21);
		irp.setMonto(Long.valueOf(rnd.nextInt(999999)));
		irp.setArchivo("ruta:/archivo/alfresco");
		irp.setResolucion(String.valueOf(rnd.nextInt(99999)));
		
		listResoluciones.add(irp);
		
		irp = new IngresoResolucionPojo();
		irp.setComuna("Macul");
		irp.setEstablecimiento("Hospital Cordillera");
		irp.setFecha(new Date());
		irp.setComponente("Procedimientos Cutáneos Quirúrgicos de Baja");
		irp.setSubtitulo(22);
		irp.setMonto(Long.valueOf(rnd.nextInt(999999)));
		irp.setArchivo("ruta:/archivo/alfresco");
		irp.setResolucion(String.valueOf(rnd.nextInt(99999)));
		
		listResoluciones.add(irp);
		
		irp = new IngresoResolucionPojo();
		irp.setComuna("Macul");
		irp.setEstablecimiento("Hospital Cordillera");
		irp.setFecha(new Date());
		irp.setComponente("Procedimientos Cutáneos Quirúrgicos de Baja");
		irp.setSubtitulo(24);
		irp.setMonto(Long.valueOf(Long.valueOf(rnd.nextInt(999999))));
		irp.setArchivo("ruta:/archivo/alfresco");
		irp.setResolucion(String.valueOf(rnd.nextInt(99999)));
		
		listResoluciones.add(irp);
		
		irp = new IngresoResolucionPojo();
		irp.setComuna("Macul");
		irp.setEstablecimiento("Hospital Cordillera");
		irp.setFecha(new Date());
		irp.setComponente("Procedimientos Cutáneos Quirúrgicos de Baja");
		irp.setSubtitulo(29);
		irp.setMonto(Long.valueOf(rnd.nextInt(999999)));
		irp.setArchivo("ruta:/archivo/alfresco");
		irp.setResolucion(String.valueOf(rnd.nextInt(99999)));
		
		listResoluciones.add(irp);
	}

	
	


	public String getComponenteSeleccionado() {
		return componenteSeleccionado;
	}

	public void setComponenteSeleccionado(String componenteSeleccionado) {
		this.componenteSeleccionado = componenteSeleccionado;
	}

	public String getEstablecimientoSeleccionado() {
		return establecimientoSeleccionado;
	}

	public void setEstablecimientoSeleccionado(String establecimientoSeleccionado) {
		this.establecimientoSeleccionado = establecimientoSeleccionado;
	}

	public void setConvenios(List<ConveniosVO> convenios) {
		this.convenios = convenios;
	}

	
	public void setEstablecimientos(List<EstablecimientoVO> establecimientos) {
		this.establecimientos = establecimientos;
	}


	public String getComunaSeleccionada() {
		return comunaSeleccionada;
	}

	public void setComunaSeleccionada(String comunaSeleccionada) {
		this.comunaSeleccionada = comunaSeleccionada;
	}

	public void setComponentes(List<ComponentesVO> componentes) {
		this.componentes = componentes;
	}

	public void setProgramas(List<ProgramaVO> programas) {
		this.programas = programas;
	}

	public String getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(String programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public String getConvenioSeleccionado() {
		
		return convenioSeleccionado;
	}

	public void setConvenioSeleccionado(String convenioSeleccionado) {
		
		this.convenioSeleccionado = convenioSeleccionado;
	}

	public ConveniosVO getConvenio() {
		return convenio;
	}

	public void setConvenio(ConveniosVO convenio) {
	
		this.convenio = convenio;
	}
	
	public ProgramasService getProgramasService() {
		return programasService;
	}


	public void setProgramasService(ProgramasService programasService) {
		this.programasService = programasService;
	}


	public ComponenteService getComponenteService() {
		return componenteService;
	}


	public void setComponenteService(ComponenteService componenteService) {
		this.componenteService = componenteService;
	}


	public ComunaService getComunaService() {
		return comunaService;
	}


	public void setComunaService(ComunaService comunaService) {
		this.comunaService = comunaService;
	}


	public EstablecimientosService getEstablecimientosService() {
		return establecimientosService;
	}


	public void setEstablecimientosService(
			EstablecimientosService establecimientosService) {
		this.establecimientosService = establecimientosService;
	}


	public ConveniosService getaPSConveniosService() {
		return aPSConveniosService;
	}


	public void setaPSConveniosService(ConveniosService aPSConveniosService) {
		this.aPSConveniosService = aPSConveniosService;
	}




	public AlfrescoService getAlfrescoService() {
		return alfrescoService;
	}


	public void setAlfrescoService(AlfrescoService alfrescoService) {
		this.alfrescoService = alfrescoService;
	}







	public UploadedFile getPlantillaFile() {
		return plantillaFile;
	}



	public void setPlantillaFile(UploadedFile plantillaFile) {
		this.plantillaFile = plantillaFile;
	}



	public void setDocumentos(List<ConvenioDocumentoVO> documentos) {
		this.documentos = documentos;
	}


	public String getDocumentoSeleccionado() {
		return documentoSeleccionado;
	}


	public void setDocumentoSeleccionado(String documentoSeleccionado) {
		this.documentoSeleccionado = documentoSeleccionado;
	}


	public ProgramaVO getPrograma() {
		return programa;
	}


	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}



	
	
}
