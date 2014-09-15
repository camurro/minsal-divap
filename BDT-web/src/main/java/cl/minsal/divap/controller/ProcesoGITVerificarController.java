package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.CajaService;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.ComunaService;
import minsal.divap.service.ConveniosService;
import minsal.divap.service.EstablecimientosService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.SubtituloService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.ConveniosVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
@Named ( "procesoGITVerificarController" ) 
@ViewScoped 
public class ProcesoGITVerificarController extends AbstractTaskMBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9029285397119804637L;


	private ProgramaVO programaVO;
	
	private List<ComponentesVO> componentes;
	private List<SubtituloVO>  subtitulos;
	private List<ConveniosVO> convenios;
	private Integer programaSeleccionado; // viene desde elBPM SUITE llenado en init
	private boolean sub21;
	private boolean sub22;
	private boolean sub24;
	private boolean sub29;
	private boolean rectificar_;
	private String componenteSeleccionado;
	private String comunaSeleccionada;
	private String establecimientoSeleccionado;
	
	private String componenteSeleccionado22;
	private String comunaSeleccionada22;
	private String establecimientoSeleccionado22;
	
	private String componenteSeleccionado24;
	private String comunaSeleccionada24;
	private String establecimientoSeleccionado24;
	
	private String componenteSeleccionado29;
	private String comunaSeleccionada29;
	private String establecimientoSeleccionado29;
	
	private List<ComunaVO> comunas;
	private List<EstablecimientoVO> establecimientos;
	
	@EJB
	private ConveniosService aPSConveniosService;
	@EJB
	private ProgramasService programaService;
	@EJB
	private ComponenteService componenteService;
	@EJB
	private ComunaService comunaService;
	@EJB
	private EstablecimientosService establecimientosService;
	@EJB
	private SubtituloService subtituloService;
	
	
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new  HashMap<String, Object>();
		parameters.put("rectificar_", isRectificar_() );
		parameters.put("retiro_", true );
		parameters.put("contenido_", "ESTO_ES_UN_CONTENIDO" );
		
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String  informarReparos(boolean sino){
	  setRectificar_(sino);
	  setTarget( "bandejaTareas.jsf");
	 
	  return  enviar();
	}
	
	@Override
	public String enviar(){
		

		return super.enviar();
	}
	
	
	
	@PostConstruct public void init() {
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			 programaSeleccionado = (Integer) getTaskDataVO()
					.getData().get("_programaSeleccionado");
			System.out.println("programaSeleccionado --->"
					+ programaSeleccionado);
			programaVO = programaService.getProgramaAnoPorID(programaSeleccionado);
		}
		
		Programa pro = new Programa();
		pro = programaService.getProgramasByID(programaVO.getId());
		subtitulos =	subtituloService.getSubtituloByDependenciaPrograma(pro.getDependencia().getIdDependenciaPrograma());
		 setSub21(false);
		 setSub22(false);
		 setSub24(false);
		 setSub29(false);
		 
		 //muestra los tab 
		for (int i = 0; i < subtitulos.size(); i++){
		 		switch (subtitulos.get(i).getId()) {
			  	case 1: setSub21(true); 
			  				break;
		        case 2: setSub22(true);
		        			break;
			    case 3: setSub24(true);
			    			break;
			    case 4: setSub29(true);
			    			break;
				default: break;
		      }
			   
		}
	
	
	}

	public void updatefila(int idConvenio){
	
		aPSConveniosService.updateConvenioByIdAprobacion(idConvenio, true);
		
		
	}
	
	
	
	public List<ConveniosVO> llenaTabs(int sub){
	
		  switch (sub) {
          case 1: convenios = listResoluciones(sub,componenteSeleccionado,comunaSeleccionada,establecimientoSeleccionado);
          				
          				return convenios;
          case 2:  
          case 3: 
          case 4: convenios = listResoluciones(sub,componenteSeleccionado29,comunaSeleccionada29,establecimientoSeleccionado29);
                    return convenios;
                 
          default: break;
      }
		  return convenios;
	}
	
	public List<ConveniosVO> listResoluciones(int sub, String componente,String comuna, String establecimiento) {
	
		
		
		int ano = Calendar.getInstance().get(Calendar.YEAR);
		
		if(comuna != null  && componente != null  && establecimiento != null){
			if(comuna !=""  && componente != ""  && establecimiento != ""){
			convenios =	aPSConveniosService.getConvenioBySubComponenteComunaEstableAnoAprobacion(sub, componente, comuna,establecimiento,ano,programaVO.getId(),false);
				}else{
					convenios = new ArrayList<ConveniosVO>();
				}
			}else{
			convenios = new ArrayList<ConveniosVO>();
		}
			
		
		return convenios;
	}
	
	

	
	public List<ComponentesVO> getComponentesBySubtitulo(int sub) {
			
		if(getProgramaSeleccionado() != null){
			
			componentes = componenteService.getComponenteByProgramaSubtitulo(programaVO.getId(),sub);
		
		}else{
			componentes = new ArrayList<ComponentesVO>();
		}
		return componentes;
     }
	
	public List<ComunaVO> getComunas() {
		if(comunas == null){
			comunas = comunaService.getComunas();
		}
		
	
		return comunas;
	}
	
	public List<EstablecimientoVO> llenaCboEstablecimientos(int sub){
		
		switch (sub) {
        case 1: establecimientos = getEstablecimientos(comunaSeleccionada);
        		  return establecimientos;
        case 2:  establecimientos = getEstablecimientos(comunaSeleccionada22);
		  			return establecimientos;
        case 3: establecimientos = getEstablecimientos(comunaSeleccionada24);
        		  return establecimientos;
        case 4: establecimientos = getEstablecimientos(comunaSeleccionada29);
                  return establecimientos;
               
        default: break;
    }
		  return establecimientos;
	}
	
public List<EstablecimientoVO> getEstablecimientos(String comuna) {
		
		if(comuna != null ){
			if(comuna != ""){
				establecimientos = establecimientosService.getEstablecimientosByComuna(Integer.parseInt(comuna));
						
			}else{
				establecimientos = new ArrayList<EstablecimientoVO>();
			}
					
		}else{
			
		}
			
		return establecimientos;
	}
	
	
	
	
	
	public void downloadDocAlfresco(String id ){
		String mensaje="";	
	try{
		mensaje = "Archivo Descargado";
		FacesMessage msg = new FacesMessage(mensaje);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		ReferenciaDocumento ref = new ReferenciaDocumento();
		ref =aPSConveniosService.getReferenciaDocumentoByIdConvenio(Integer.parseInt(id));
		
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
	

	
	
	
	public List<ConveniosVO> getConvenios() {
		return convenios;
	}

	public void setConvenios(List<ConveniosVO> convenios) {
		this.convenios = convenios;
	}


	
	
	
	
	

	
	
	
	
	
	public List<ComponentesVO> getComponentes() {
		if(getProgramaSeleccionado() != null){
			componentes = componenteService.getComponenteByPrograma(programaVO.getId());
		}else{
			componentes = new ArrayList<ComponentesVO>();
		}
		return componentes;
	}
		
	public ProgramaVO getProgramaVO() {
	return programaVO;
	}

	public void setProgramaVO(ProgramaVO programaVO) {
	this.programaVO = programaVO;
	}

	public void setComponentes(List<ComponentesVO> componentes) {
		this.componentes = componentes;
	}

	public Integer getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(Integer programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public String getComponenteSeleccionado() {
		return componenteSeleccionado;
	}

	public void setComponenteSeleccionado(String componenteSeleccionado) {
		this.componenteSeleccionado = componenteSeleccionado;
	}

	

	public void setComunas(List<ComunaVO> comunas) {
		this.comunas = comunas;
	}

	public String getComunaSeleccionada() {
		return comunaSeleccionada;
	}

	public void setComunaSeleccionada(String comunaSeleccionada) {
		this.comunaSeleccionada = comunaSeleccionada;
	}


	public void setEstablecimientos(List<EstablecimientoVO> establecimientos) {
		this.establecimientos = establecimientos;
	}

	public String getEstablecimientoSeleccionado() {
		return establecimientoSeleccionado;
	}

	public void setEstablecimientoSeleccionado(String establecimientoSeleccionado) {
		this.establecimientoSeleccionado = establecimientoSeleccionado;
	}



	

	public String getComponenteSeleccionado29() {
		return componenteSeleccionado29;
	}

	public void setComponenteSeleccionado29(String componenteSeleccionado29) {
		this.componenteSeleccionado29 = componenteSeleccionado29;
	}

	public String getComunaSeleccionada29() {
		return comunaSeleccionada29;
	}

	public void setComunaSeleccionada29(String comunaSeleccionada29) {
		this.comunaSeleccionada29 = comunaSeleccionada29;
	}

	public String getEstablecimientoSeleccionado29() {
		return establecimientoSeleccionado29;
	}

	public void setEstablecimientoSeleccionado29(
			String establecimientoSeleccionado29) {
		this.establecimientoSeleccionado29 = establecimientoSeleccionado29;
	}

	public List<SubtituloVO> getSubtitulos() {
		return subtitulos;
	}

	public void setSubtitulos(List<SubtituloVO> subtitulos) {
		this.subtitulos = subtitulos;
	}

	public boolean isSub21() {
		return sub21;
	}

	public void setSub21(boolean sub21) {
		this.sub21 = sub21;
	}

	public boolean isSub22() {
		return sub22;
	}

	public void setSub22(boolean sub22) {
		this.sub22 = sub22;
	}

	public boolean isSub24() {
		return sub24;
	}

	public void setSub24(boolean sub24) {
		this.sub24 = sub24;
	}

	public boolean isSub29() {
		return sub29;
	}

	public void setSub29(boolean sub29) {
		this.sub29 = sub29;
	}

	public String getComponenteSeleccionado22() {
		return componenteSeleccionado22;
	}

	public void setComponenteSeleccionado22(String componenteSeleccionado22) {
		this.componenteSeleccionado22 = componenteSeleccionado22;
	}

	public String getComunaSeleccionada22() {
		return comunaSeleccionada22;
	}

	public void setComunaSeleccionada22(String comunaSeleccionada22) {
		this.comunaSeleccionada22 = comunaSeleccionada22;
	}

	public String getEstablecimientoSeleccionado22() {
		return establecimientoSeleccionado22;
	}

	public void setEstablecimientoSeleccionado22(
			String establecimientoSeleccionado22) {
		this.establecimientoSeleccionado22 = establecimientoSeleccionado22;
	}

	public String getComponenteSeleccionado24() {
		return componenteSeleccionado24;
	}

	public void setComponenteSeleccionado24(String componenteSeleccionado24) {
		this.componenteSeleccionado24 = componenteSeleccionado24;
	}

	public String getComunaSeleccionada24() {
		return comunaSeleccionada24;
	}

	public void setComunaSeleccionada24(String comunaSeleccionada24) {
		this.comunaSeleccionada24 = comunaSeleccionada24;
	}

	public String getEstablecimientoSeleccionado24() {
		return establecimientoSeleccionado24;
	}

	public void setEstablecimientoSeleccionado24(
			String establecimientoSeleccionado24) {
		this.establecimientoSeleccionado24 = establecimientoSeleccionado24;
	}

	public boolean isRectificar_() {
		return rectificar_;
	}

	public void setRectificar_(boolean rectificar_) {
		this.rectificar_ = rectificar_;
	}


	
	
	




	
}
