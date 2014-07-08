package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import cl.minsal.divap.pojo.ComponentePojo;
import cl.minsal.divap.pojo.MonitoreoPojo;
import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.redhat.bandejaTareas.controller.BaseController;

@Named ( "procesoProgramacionController" ) @ViewScoped public class ProcesoProgramacionController extends BaseController implements
				Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;
	@Inject FacesContext facesContext;
	
	@PostConstruct public void init() {
		log.info("procesoProgramacionController tocado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		
		generaProgramas();
		generaServicios();
	}
	
	List<ProcesosProgramasPojo> listadoProgramasServicio;
	
	public List<ProcesosProgramasPojo> getListadoProgramasServicio() {
		return listadoProgramasServicio;
	}
	
	public void setListadoProgramasServicio( List<ProcesosProgramasPojo> listadoProgramasServicio ) {
		this.listadoProgramasServicio = listadoProgramasServicio;
	}
	
	public void generaProgramas() {
		listadoProgramasServicio = new ArrayList<ProcesosProgramasPojo>();
		
		ProcesosProgramasPojo p2;
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("VIDA SANA: INTERVENCIÓN EN  FACTORES DE RIESGO DE ENFERMEDADES CRÓNICAS ASOCIADAS A LA MALNUTRICIÓN EN NIÑOS, NIÑAS, ADOLESCENTES, ADULTOS Y MUJERES POSTPARTO");
		p2.setDescripcion("Descripción del Programa de Vida Sana (Municipal)");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(true);
		p2.setProgreso(0.55D);
		p2.setEstado("green");
		p2.setTerminar(true);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		p2.setDescripcion("Descripción del Programa de Apoyo a las acciones en el nivel primario de Salud (Servicio).");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(false);
		p2.setProgreso(1D);
		p2.setEstado("green");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("PILOTO VIDA SANA: ALCOHOL");
		p2.setDescripcion("Descripción del Programa de Vida Sana con Alcohol (Mixto).");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(true);
		p2.setProgreso(0D);
		p2.setEstado("red");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		p2.setDescripcion("Descripción del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Históricos Municipal).");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(true);
		p2.setProgreso(0.75D);
		p2.setEstado("green");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("CAPACITACIÓN Y FORMACIÓN ATENCIÓN PRIMARIA EN LA RED ASISTENCIAL");
		p2.setDescripcion("Descripción del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Históricos Servicio Salud).");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(true);
		p2.setProgreso(0.75D);
		p2.setEstado("red");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
	}
	
	List<MonitoreoPojo> listadoServicios;
	
	public List<MonitoreoPojo> getListadoServicios() {
		return listadoServicios;
	}
	
	public void setListadoServicios( List<MonitoreoPojo> listadoServicios ) {
		this.listadoServicios = listadoServicios;
	}
	
	public void generaServicios(){
		MonitoreoPojo p;
		listadoServicios = new ArrayList<MonitoreoPojo>();
		ComponentePojo c = new ComponentePojo();
		c.setComponenteNombre("Emergencia Dental");
		c.setPesoComponente(0.3f);
		
		p = new MonitoreoPojo();
		p.setEstablecimiento("Centro Comunitario de Salud Familiar Cerro Esmeralda");
		p.setServicio("Metropolitano Oriente");
		p.setComuna("Macul");
		p.setComponente(c);
		listadoServicios.add(p);
		
		p = new MonitoreoPojo();
		p.setEstablecimiento("Centro Comunitario de Salud Familiar El Boro");
		p.setServicio("Iquique");
		p.setComuna("La Reina");
		p.setComponente(c);
		listadoServicios.add(p);
		
		p = new MonitoreoPojo();
		p.setEstablecimiento("Clínica Dental Móvil Simple. Pat. RW6740 (Iquique)");
		p.setServicio("Metropolitano Sur");
		p.setComuna("Puente Alto");
		p.setComponente(c);
		listadoServicios.add(p);
		
		p = new MonitoreoPojo();
		p.setEstablecimiento("COSAM Dr. Jorge Seguel Cáceres");
		p.setServicio("Alto Hospicio");
		p.setComuna("Santiago");
		p.setComponente(c);
		listadoServicios.add(p);
		
		p = new MonitoreoPojo();
		p.setEstablecimiento("COSAM Salvador Allende	");
		p.setServicio("Bio Bio");
		p.setComuna("Providencia");
		p.setComponente(c);
		listadoServicios.add(p);
	}
}
