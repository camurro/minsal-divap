package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import cl.minsal.divap.pojo.ComunaPojo;
import cl.minsal.divap.pojo.EnvioServiciosPojo;
import cl.minsal.divap.pojo.EstablecimientoPojo;
import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.minsal.divap.pojo.ProgramasPojo;
import cl.minsal.divap.pojo.ValorHistoricoPojo;
import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named ( "procesoDistRecFinController" ) @ViewScoped public class ProcesoDistRecFinController extends BaseController implements
				Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;
	@Inject private BandejaProperties bandejaProperties;
	@Inject FacesContext facesContext;
	
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	private List<ProgramasPojo> listadoProgramas;
	
	public List<ProgramasPojo> getListadoProgramas() {
		return listadoProgramas;
	}
	
	public void setListadoProgramas( List<ProgramasPojo> listadoProgramas ) {
		this.listadoProgramas = listadoProgramas;
	}
	
	private List<ProcesosProgramasPojo> listadoProgramasServicio;
	
	public List<ProcesosProgramasPojo> getListadoProgramasServicio() {
		return listadoProgramasServicio;
	}
	
	public void setListadoProgramasServicio( List<ProcesosProgramasPojo> listadoProgramasServicio ) {
		this.listadoProgramasServicio = listadoProgramasServicio;
	}
	
	private List<EnvioServiciosPojo> listadoEnvioServicios;
	
	public List<EnvioServiciosPojo> getListadoEnvioServicios() {
		return listadoEnvioServicios;
	}
	
	public void setListadoEnvioServicios( List<EnvioServiciosPojo> listadoEnvioServicios ) {
		this.listadoEnvioServicios = listadoEnvioServicios;
	}
	
	private List<EstablecimientoPojo> listadoEstablecimientos;
	
	public List<EstablecimientoPojo> getListadoEstablecimientos() {
		return listadoEstablecimientos;
	}
	
	public void setListadoEstablecimientos( List<EstablecimientoPojo> listadoEstablecimientos ) {
		this.listadoEstablecimientos = listadoEstablecimientos;
	}
	
	private List<ComunaPojo> listadoComunas;
	
	public List<ComunaPojo> getListadoComunas() {
		return listadoComunas;
	}
	
	public void setListadoComunas( List<ComunaPojo> listadoComunas ) {
		this.listadoComunas = listadoComunas;
	}
	
	private List<ValorHistoricoPojo> listadoValorHistorico;
	
	public List<ValorHistoricoPojo> getListadoValorHistorico() {
		return listadoValorHistorico;
	}
	
	public void setListadoValorHistorico( List<ValorHistoricoPojo> listadoValorHistorico ) {
		this.listadoValorHistorico = listadoValorHistorico;
	}
	
	private Long totalS21;
	private Long totalS22;
	private Long totalS24;
	private Long totalS29;
	
	
	
	public Long getTotalS21() {
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS21();
		}
		return suma;
	}

	public void setTotalS21( Long totalS21 ) {
		this.totalS21 = totalS21;
	}

	public Long getTotalS22() {
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS22();
		}
		return suma;
	}

	public void setTotalS22( Long totalS22 ) {
		this.totalS22 = totalS22;
	}

	public Long getTotalS24() {
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS24();
		}
		return suma;
	}

	public void setTotalS24( Long totalS24 ) {
		this.totalS24 = totalS24;
	}

	public Long getTotalS29() {
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS29();
		}
		return suma;
	}

	public void setTotalS29( Long totalS29 ) {
		this.totalS29 = totalS29;
	}

	@PostConstruct public void init() {
		// log.info("ProcesosPrincipalController Alcanzado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		
		ProgramasPojo p;
		listadoProgramas = new ArrayList<ProgramasPojo>();
		Random rnd = new Random();
		
		p = new ProgramasPojo();
		p.setCodServicio("00001");
		p.setGloServicio("Talcahuano");
		p.setComuna("Iquique");
		p.setS21((long) rnd.nextInt(9999999));
		p.setS22((long) rnd.nextInt(9999999));
		p.setS24((long) rnd.nextInt(9999999));
		p.setS29((long) rnd.nextInt(9999999));
		p.setReliquidacion(0f);
		p.setCumplimiento(0.55f);
		p.setAvance(1f);
		p.setEstado("green");
		
		listadoProgramas.add(p);
		
		p = new ProgramasPojo();
		p.setCodServicio("00002");
		p.setGloServicio("Concepción");
		p.setComuna("Alto Hospicio");
		p.setS21((long) rnd.nextInt(9999999));
		p.setS22((long) rnd.nextInt(9999999));
		p.setS24((long) rnd.nextInt(9999999));
		p.setS29((long) rnd.nextInt(9999999));
		p.setReliquidacion(1f);
		p.setCumplimiento(0f);
		p.setAvance(0.4f);
		p.setEstado("yellow");
		
		listadoProgramas.add(p);
		
		p = new ProgramasPojo();
		p.setCodServicio("00003");
		p.setGloServicio("Metropolitano Sur");
		p.setComuna("Pozo Almonte");
		p.setS21(0L);
		p.setS22(0L);
		p.setS24(0L);
		p.setS29(0L);
		p.setReliquidacion(0.5f);
		p.setCumplimiento(0.5f);
		p.setAvance(0);
		p.setEstado("red");
		
		listadoProgramas.add(p);
		
		p = new ProgramasPojo();
		p.setCodServicio("00004");
		p.setGloServicio("Bio Bio");
		p.setComuna("Macul");
		p.setS21((long) rnd.nextInt(9999999));
		p.setS22((long) rnd.nextInt(9999999));
		p.setS24((long) rnd.nextInt(9999999));
		p.setS29((long) rnd.nextInt(9999999));
		p.setReliquidacion(0.75f);
		p.setCumplimiento(0.31f);
		p.setAvance(1f);
		p.setEstado("green");
		
		listadoProgramas.add(p);
		
		p = new ProgramasPojo();
		p.setCodServicio("00005");
		p.setGloServicio("Iquique");
		p.setComuna("Camiña");
		p.setS21((long) rnd.nextInt(9999999));
		p.setS22((long) rnd.nextInt(9999999));
		p.setS24((long) rnd.nextInt(9999999));
		p.setS29((long) rnd.nextInt(9999999));
		p.setReliquidacion(0.27f);
		p.setCumplimiento(0.65f);
		p.setAvance(1f);
		p.setEstado("green");
		
		listadoProgramas.add(p);
		
		listadoProgramasServicio = new ArrayList<ProcesosProgramasPojo>();
		
		ProcesosProgramasPojo p2;
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("VIDA SANA: INTERVENCIÓN EN  FACTORES DE RIESGO DE ENFERMEDADES CRÓNICAS ASOCIADAS A LA MALNUTRICIÓN EN NIÑOS, NIÑAS, ADOLESCENTES, ADULTOS Y MUJERES POSTPARTO");
		p2.setDescripcion("Descripción del Programa de Vida Sana (Programa Municipal)");
		p2.setUrl("divapProcesoDistRecFinProgSubirPlanillasMunicipal");
		p2.setEditar(true);
		p2.setProgreso(0.55D);
		p2.setEstado("yellow");
		p2.setTerminar(true);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		p2.setDescripcion("Descripción del Programa de Apoyo a las acciones en el nivel primario de Salud (Programa Servicio Salud).");
		p2.setUrl("divapProcesoDistRecFinProgSubirPlanillasServicioSalud");
		p2.setEditar(false);
		p2.setProgreso(1D);
		p2.setEstado("green");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("PILOTO VIDA SANA: ALCOHOL");
		p2.setDescripcion("Descripción del Programa de Vida Sana con Alcohol (Programa Mixto).");
		p2.setUrl("divapProcesoDistRecFinProgSubirPlanillasMixto");
		p2.setEditar(true);
		p2.setProgreso(0D);
		p2.setEstado("red");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		p2.setDescripcion("Descripción del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Históricos Municipal).");
		p2.setUrl("divapProcesoDistRecFinProgValoresHistoricosMunicipal");
		p2.setEditar(true);
		p2.setProgreso(0.75D);
		p2.setEstado("yellow");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("CAPACITACIÓN Y FORMACIÓN ATENCIÓN PRIMARIA EN LA RED ASISTENCIAL");
		p2.setDescripcion("Descripción del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Históricos Servicio Salud).");
		p2.setUrl("divapProcesoDistRecFinProgValoresHistoricosServicioSalud");
		p2.setEditar(true);
		p2.setProgreso(0.75D);
		p2.setEstado("yellow");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		listadoEnvioServicios = new ArrayList<EnvioServiciosPojo>();
		EnvioServiciosPojo e;
		
		e = new EnvioServiciosPojo();
		e.setServicioSalud("Metropolitano Oriente");
		e.setEnviado("15 Abril 2014 16:39");
		listadoEnvioServicios.add(e);
		
		e = new EnvioServiciosPojo();
		e.setServicioSalud("Talcahuano");
		e.setEnviado("15 Abril 2014 16:39");
		listadoEnvioServicios.add(e);
		
		e = new EnvioServiciosPojo();
		e.setServicioSalud("Bio Bio");
		e.setEnviado("15 Abril 2014 16:39");
		listadoEnvioServicios.add(e);
		
		e = new EnvioServiciosPojo();
		e.setServicioSalud("Iquique");
		e.setEnviado("15 Abril 2014 16:39");
		listadoEnvioServicios.add(e);
		
		e = new EnvioServiciosPojo();
		e.setServicioSalud("Alto Hospicio");
		e.setEnviado("15 Abril 2014 16:39");
		listadoEnvioServicios.add(e);
		
		// LISTADO ESTABLECIMIENTOS
		listadoEstablecimientos = new ArrayList<EstablecimientoPojo>();
		listadoComunas = new ArrayList<ComunaPojo>();
		EstablecimientoPojo est;
		ComunaPojo com;
		
		est = new EstablecimientoPojo();
		est.setNombreOficial("Centro Comunitario de Salud Familiar Cerro Esmeralda");
		com = new ComunaPojo();
		com.setNombreComuna("Iquique");
		est.setpS21((long) rnd.nextInt(99999));
		est.setpS22((long) rnd.nextInt(99999));
		est.setpS24((long) rnd.nextInt(99999));
		est.setpS29((long) rnd.nextInt(99999));
		est.setqS21((long) rnd.nextInt(999));
		est.setqS22((long) rnd.nextInt(999));
		est.setqS24((long) rnd.nextInt(999));
		est.setqS29((long) rnd.nextInt(999));
		est.setReliquidacion(0f);
		est.setCumplimiento(0.65f);
		est.setComuna(com);
		
		listadoEstablecimientos.add(est);
		
		est = new EstablecimientoPojo();
		est.setNombreOficial("Centro Comunitario de Salud Familiar El Boro");
		com = new ComunaPojo();
		com.setNombreComuna("Alto Hospicio");
		est.setpS21((long) rnd.nextInt(99999));
		est.setpS22((long) rnd.nextInt(99999));
		est.setpS24((long) rnd.nextInt(99999));
		est.setpS29((long) rnd.nextInt(99999));
		est.setqS21((long) rnd.nextInt(999));
		est.setqS22((long) rnd.nextInt(999));
		est.setqS24((long) rnd.nextInt(999));
		est.setqS29((long) rnd.nextInt(999));
		est.setReliquidacion(0.8f);
		est.setCumplimiento(0.35f);
		est.setComuna(com);
		
		listadoEstablecimientos.add(est);
		
		est = new EstablecimientoPojo();
		est.setNombreOficial("Clínica Dental Móvil Simple. Pat. RW6740 (Iquique)");
		com = new ComunaPojo();
		com.setNombreComuna("Pozo Almonte");
		est.setpS21((long) rnd.nextInt(99999));
		est.setpS22((long) rnd.nextInt(99999));
		est.setpS24((long) rnd.nextInt(99999));
		est.setpS29((long) rnd.nextInt(99999));
		est.setqS21((long) rnd.nextInt(999));
		est.setqS22((long) rnd.nextInt(999));
		est.setqS24((long) rnd.nextInt(999));
		est.setqS29((long) rnd.nextInt(999));
		est.setReliquidacion(1f);
		est.setCumplimiento(0f);
		est.setComuna(com);
		
		listadoEstablecimientos.add(est);
		
		est = new EstablecimientoPojo();
		est.setNombreOficial("COSAM Dr. Jorge Seguel Cáceres");
		com = new ComunaPojo();
		com.setNombreComuna("Macul");
		est.setpS21((long) rnd.nextInt(99999));
		est.setpS22((long) rnd.nextInt(99999));
		est.setpS24((long) rnd.nextInt(99999));
		est.setpS29((long) rnd.nextInt(99999));
		est.setqS21((long) rnd.nextInt(999));
		est.setqS22((long) rnd.nextInt(999));
		est.setqS24((long) rnd.nextInt(999));
		est.setqS29((long) rnd.nextInt(999));
		est.setReliquidacion(0.5f);
		est.setCumplimiento(0.43f);
		est.setComuna(com);
		
		listadoEstablecimientos.add(est);
		
		est = new EstablecimientoPojo();
		est.setNombreOficial("COSAM Salvador Allende");
		com = new ComunaPojo();
		com.setNombreComuna("Camiña");
		est.setpS21((long) rnd.nextInt(99999));
		est.setpS22((long) rnd.nextInt(99999));
		est.setpS24((long) rnd.nextInt(99999));
		est.setpS29((long) rnd.nextInt(99999));
		est.setqS21((long) rnd.nextInt(999));
		est.setqS22((long) rnd.nextInt(999));
		est.setqS24((long) rnd.nextInt(999));
		est.setqS29((long) rnd.nextInt(999));
		est.setReliquidacion(0f);
		est.setCumplimiento(1f);
		est.setComuna(com);
		
		listadoEstablecimientos.add(est);
		
		com = new ComunaPojo();
		com.setNombreComuna("Iquique");
		
		listadoComunas.add(com);
		
		com = new ComunaPojo();
		com.setNombreComuna("Alto Hospicio");
		
		listadoComunas.add(com);
		
		com = new ComunaPojo();
		com.setNombreComuna("Pozo Almonte");
		
		listadoComunas.add(com);
		
		com = new ComunaPojo();
		com.setNombreComuna("Camiña");
		
		listadoComunas.add(com);
		
		com = new ComunaPojo();
		com.setNombreComuna("Colchane");
		
		listadoComunas.add(com);
		
		listadoValorHistorico = new ArrayList<ValorHistoricoPojo>();
		ValorHistoricoPojo valorHistorico = new ValorHistoricoPojo();
		valorHistorico.setComuna("Iquique");//alto hospicio, pozo almonte, macul, cami�a
		valorHistorico.setServicioSalud("Metropolitano Oriente");
		valorHistorico.setInflactorS21((double) (1.3));
		valorHistorico.setValorHistoricoS21((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS22((double) (1.35));
		valorHistorico.setValorHistoricoS22((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS24((double) (1.4));
		valorHistorico.setValorHistoricoS24((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS29((double) (1.45));
		valorHistorico.setValorHistoricoS29((long) rnd.nextInt(99999));
		listadoValorHistorico.add(valorHistorico);
		
		valorHistorico = new ValorHistoricoPojo();
		valorHistorico.setComuna("Alto Hospicio");//alto hospicio, pozo almonte, macul, cami�a
		valorHistorico.setServicioSalud("Concepcion");
		valorHistorico.setInflactorS21((double) (1.3));
		valorHistorico.setValorHistoricoS21((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS22((double) (1.35));
		valorHistorico.setValorHistoricoS22((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS24((double) (1.4));
		valorHistorico.setValorHistoricoS24((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS29((double) (1.45));
		valorHistorico.setValorHistoricoS29((long) rnd.nextInt(99999));
		listadoValorHistorico.add(valorHistorico);

		valorHistorico = new ValorHistoricoPojo();
		valorHistorico.setComuna("Pozo Almonte");//alto hospicio, pozo almonte, macul, cami�a
		valorHistorico.setServicioSalud("Talcahuano");
		valorHistorico.setInflactorS21((double) (1.3));
		valorHistorico.setValorHistoricoS21((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS22((double) (1.35));
		valorHistorico.setValorHistoricoS22((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS24((double) (1.4));
		valorHistorico.setValorHistoricoS24((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS29((double) (1.45));
		valorHistorico.setValorHistoricoS29((long) rnd.nextInt(99999));
		listadoValorHistorico.add(valorHistorico);

		valorHistorico = new ValorHistoricoPojo();
		valorHistorico.setComuna("Macul");//alto hospicio, pozo almonte, macul, cami�a
		valorHistorico.setServicioSalud("Iquique");
		valorHistorico.setInflactorS21((double) (1.3));
		valorHistorico.setValorHistoricoS21((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS22((double) (1.35));
		valorHistorico.setValorHistoricoS22((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS24((double) (1.4));
		valorHistorico.setValorHistoricoS24((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS29((double) (1.45));
		valorHistorico.setValorHistoricoS29((long) rnd.nextInt(99999));
		listadoValorHistorico.add(valorHistorico);

		valorHistorico = new ValorHistoricoPojo();
		valorHistorico.setComuna("Camiña");//alto hospicio, pozo almonte, macul, cami�a
		valorHistorico.setServicioSalud("Bio Bio");
		valorHistorico.setInflactorS21((double) (1.3));
		valorHistorico.setValorHistoricoS21((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS22((double) (1.35));
		valorHistorico.setValorHistoricoS22((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS24((double) (1.4));
		valorHistorico.setValorHistoricoS24((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS29((double) (1.45));
		valorHistorico.setValorHistoricoS29((long) rnd.nextInt(99999));
		listadoValorHistorico.add(valorHistorico);
	}
	
	public Long getTotalServicio(){
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS21()+e.gettS22()+e.gettS29();
		}
		return suma;
	}
	
	public Long getTotalMunicipal(){
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS24();
		}
		return suma;
	}
}
