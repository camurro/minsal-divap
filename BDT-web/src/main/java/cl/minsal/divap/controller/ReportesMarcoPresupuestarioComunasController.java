package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
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

import cl.minsal.divap.pojo.HistoricoProgramaPojo;
import cl.minsal.divap.pojo.MarcoPresupuestarioComunaPojo;
import cl.minsal.divap.pojo.ReportePerCapitaPojo;
import cl.minsal.divap.pojo.ReporteRebajaPojo;
import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named ( "reportesMarcoPresupuestarioComunasController" )
@ViewScoped public class ReportesMarcoPresupuestarioComunasController extends BaseController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4093661401216674878L;
	@Inject private transient Logger log;
	@Inject private BandejaProperties bandejaProperties;
	@Inject FacesContext facesContext;
	
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	private List<MarcoPresupuestarioComunaPojo> listMarcoPresupuestarioComuna = new ArrayList<MarcoPresupuestarioComunaPojo>();
	
	public List<MarcoPresupuestarioComunaPojo> getListMarcoPresupuestarioComuna() {
		return listMarcoPresupuestarioComuna;
	}
	
	public void setListMarcoPresupuestarioComuna( List<MarcoPresupuestarioComunaPojo> listMarcoPresupuestarioComuna ) {
		this.listMarcoPresupuestarioComuna = listMarcoPresupuestarioComuna;
	}

	
	private List<ReportePerCapitaPojo> listPoblacioNPerCapita = new ArrayList<ReportePerCapitaPojo>();
	
	public List<ReportePerCapitaPojo> getListPoblacioNPerCapita() {
		return listPoblacioNPerCapita;
	}

	public void setListPoblacioNPerCapita( List<ReportePerCapitaPojo> listPoblacioNPerCapita ) {
		this.listPoblacioNPerCapita = listPoblacioNPerCapita;
	}
	
	private List<HistoricoProgramaPojo> listHistoricoPrograma = new ArrayList<HistoricoProgramaPojo>();
	
	

	public List<HistoricoProgramaPojo> getListHistoricoPrograma() {
		return listHistoricoPrograma;
	}

	public void setListHistoricoPrograma( List<HistoricoProgramaPojo> listHistoricoPrograma ) {
		this.listHistoricoPrograma = listHistoricoPrograma;
	}
	
	private List<ReporteRebajaPojo> listReporteRebaja = new ArrayList<ReporteRebajaPojo>();
	
	

	public List<ReporteRebajaPojo> getListReporteRebaja() {
		return listReporteRebaja;
	}

	public void setListReporteRebaja( List<ReporteRebajaPojo> listReporteRebaja ) {
		this.listReporteRebaja = listReporteRebaja;
	}

	@PostConstruct public void init() {
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		Random rnd = new Random();
		
		////MARCO PRESUPUESTARIO POR COMUNA
		MarcoPresupuestarioComunaPojo mpc = new MarcoPresupuestarioComunaPojo();
		mpc.setPrograma("VIDA SANA: INTERVENCIÓN EN  FACTORES DE RIESGO DE ENFERMEDADES CRÓNICAS ASOCIADAS A LA MALNUTRICIÓN EN NIÑOS, NIÑAS, ADOLESCENTES, ADULTOS Y MUJERES POSTPARTO");
		mpc.setMarco2014(rnd.nextInt(999999999));
		mpc.setRemesas2014(mpc.getMarco2014()-rnd.nextInt(99999999));
		mpc.setConvenio2014(mpc.getMarco2014()-rnd.nextInt(99999999));
		mpc.setPorcentajeRemesa2014((float) mpc.getRemesas2014()/mpc.getMarco2014());
		
		mpc.setServicio("Talcahuano");
		mpc.setComuna("Alto Hospicio");
		mpc.setPorcentajeConvenio2014((float) mpc.getConvenio2014()/mpc.getMarco2014());
		mpc.setConvenioPendiente(mpc.getMarco2014()-mpc.getConvenio2014());
		
		if(rnd.nextBoolean()){
			mpc.setObservacion("Existen recursos no distribuidos en este programa.");
		}else{
			mpc.setObservacion("");
		}
		listMarcoPresupuestarioComuna.add(mpc);
		
		mpc = new MarcoPresupuestarioComunaPojo();
		mpc.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		mpc.setMarco2014(rnd.nextInt(999999999));
		mpc.setRemesas2014(mpc.getMarco2014()-rnd.nextInt(99999999));
		mpc.setConvenio2014(mpc.getMarco2014()-rnd.nextInt(99999999));
		mpc.setPorcentajeRemesa2014((float) mpc.getRemesas2014()/mpc.getMarco2014());
		
		mpc.setServicio("Metropolitano Oriente");
		mpc.setComuna("Macul");
		mpc.setPorcentajeConvenio2014((float) mpc.getConvenio2014()/mpc.getMarco2014());
		mpc.setConvenioPendiente(mpc.getMarco2014()-mpc.getConvenio2014());
		
		
		if(rnd.nextBoolean()){
			mpc.setObservacion("Existen recursos no distribuidos en este programa.");
		}else{
			mpc.setObservacion("");
		}
		listMarcoPresupuestarioComuna.add(mpc);
		
		mpc = new MarcoPresupuestarioComunaPojo();
		mpc.setPrograma("PILOTO VIDA SANA: ALCOHOL");
		mpc.setMarco2014(rnd.nextInt(999999999));
		mpc.setRemesas2014(mpc.getMarco2014()-rnd.nextInt(99999999));
		mpc.setConvenio2014(mpc.getMarco2014()-rnd.nextInt(99999999));
		mpc.setPorcentajeRemesa2014((float) mpc.getRemesas2014()/mpc.getMarco2014());
		
		mpc.setServicio("Metropolitano Sur");
		mpc.setComuna("La Reina");
		mpc.setPorcentajeConvenio2014((float) mpc.getConvenio2014()/mpc.getMarco2014());
		mpc.setConvenioPendiente(mpc.getMarco2014()-mpc.getConvenio2014());
		
		
		if(rnd.nextBoolean()){
			mpc.setObservacion("Existen recursos no distribuidos en este programa.");
		}else{
			mpc.setObservacion("");
		}
		listMarcoPresupuestarioComuna.add(mpc);
		
		mpc = new MarcoPresupuestarioComunaPojo();
		mpc.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		mpc.setMarco2014(rnd.nextInt(999999999));
		mpc.setRemesas2014(mpc.getMarco2014()-rnd.nextInt(99999999));
		mpc.setConvenio2014(mpc.getMarco2014()-rnd.nextInt(99999999));
		mpc.setPorcentajeRemesa2014((float) mpc.getRemesas2014()/mpc.getMarco2014());
		
		mpc.setServicio("Metropolitano Norte");
		mpc.setComuna("La Dehesa");
		mpc.setPorcentajeConvenio2014((float) mpc.getConvenio2014()/mpc.getMarco2014());
		mpc.setConvenioPendiente(mpc.getMarco2014()-mpc.getConvenio2014());
		
		if(rnd.nextBoolean()){
			mpc.setObservacion("Existen recursos no distribuidos en este programa.");
		}else{
			mpc.setObservacion("");
		}
		listMarcoPresupuestarioComuna.add(mpc);
		
		mpc = new MarcoPresupuestarioComunaPojo();
		mpc.setPrograma("CAPACITACIÓN Y FORMACIÓN ATENCIÓN PRIMARIA EN LA RED ASISTENCIAL");
		mpc.setMarco2014(rnd.nextInt(999999999));
		mpc.setRemesas2014(mpc.getMarco2014()-rnd.nextInt(99999999));
		mpc.setConvenio2014(mpc.getMarco2014()-rnd.nextInt(99999999));
		mpc.setPorcentajeRemesa2014((float) mpc.getRemesas2014()/mpc.getMarco2014());
		
		mpc.setServicio("Metropolitano Centro");
		mpc.setComuna("Santiago Centro");
		mpc.setPorcentajeConvenio2014((float) mpc.getConvenio2014()/mpc.getMarco2014());
		mpc.setConvenioPendiente(mpc.getMarco2014()-mpc.getConvenio2014());
		
		if(rnd.nextBoolean()){
			mpc.setObservacion("Existen recursos no distribuidos en este programa.");
		}else{
			mpc.setObservacion("");
		}
		listMarcoPresupuestarioComuna.add(mpc);
		
		/////////POBLACION PER CAPITA
		ReportePerCapitaPojo rpc;
		
		rpc = new ReportePerCapitaPojo();
		rpc.setRegion("Talcahuano");
		rpc.setServicio("Talcahuano");
		rpc.setComuna("Alto Hospicio");
		rpc.setClasificacion("");
		
		listPoblacioNPerCapita.add(rpc);
		
		rpc = new ReportePerCapitaPojo();
		rpc.setRegion("Metropolitana");
		rpc.setServicio("Metropolitano Oriente");
		rpc.setComuna("Macul");
		rpc.setClasificacion("");
		
		listPoblacioNPerCapita.add(rpc);
		
		rpc = new ReportePerCapitaPojo();
		rpc.setRegion("Metropolitana");
		rpc.setServicio("Metropolitano Sur");
		rpc.setComuna("La Reina");
		rpc.setClasificacion("");
		
		listPoblacioNPerCapita.add(rpc);
		
		rpc = new ReportePerCapitaPojo();
		rpc.setRegion("Metropolitana");
		rpc.setServicio("Metropolitano Norte");
		rpc.setComuna("La Dehesa");
		rpc.setClasificacion("");
		
		listPoblacioNPerCapita.add(rpc);
		
		rpc = new ReportePerCapitaPojo();
		rpc.setRegion("Metropolitana");
		rpc.setServicio("Metropolitano Centro");
		rpc.setComuna("Santiago Centro");
		rpc.setClasificacion("");
		
		listPoblacioNPerCapita.add(rpc);
		
		
		//////HISTORICO PROGRAMA
		
		HistoricoProgramaPojo hpp = new HistoricoProgramaPojo();
		
		hpp = new HistoricoProgramaPojo();
		hpp.setRegion("Talcahuano");
		hpp.setServicio("Talcahuano");
		hpp.setComuna("Alto Hospicio");
		
		listHistoricoPrograma.add(hpp);
		
		hpp = new HistoricoProgramaPojo();
		hpp.setRegion("Metropolitana");
		hpp.setServicio("Metropolitano Oriente");
		hpp.setComuna("Macul");
		
		listHistoricoPrograma.add(hpp);
		
		hpp = new HistoricoProgramaPojo();
		hpp.setRegion("Metropolitana");
		hpp.setServicio("Metropolitano Sur");
		hpp.setComuna("La Reina");
		
		listHistoricoPrograma.add(hpp);
		
		hpp = new HistoricoProgramaPojo();
		hpp.setRegion("Metropolitana");
		hpp.setServicio("Metropolitano Norte");
		hpp.setComuna("La Dehesa");
		
		listHistoricoPrograma.add(hpp);
		
		hpp = new HistoricoProgramaPojo();
		hpp.setRegion("Metropolitana");
		hpp.setServicio("Metropolitano Centro");
		hpp.setComuna("Santiago Centro");
		
		listHistoricoPrograma.add(hpp);
		
/////////REBAJA
		ReporteRebajaPojo rrp;
		
		rrp = new ReporteRebajaPojo();
		rrp.setRegion("Talcahuano");
		rrp.setServicio("Talcahuano");
		rrp.setComuna("Alto Hospicio");
		
		listReporteRebaja.add(rrp);
		
		rrp = new ReporteRebajaPojo();
		rrp.setRegion("Metropolitana");
		rrp.setServicio("Metropolitano Oriente");
		rrp.setComuna("Macul");
		
		listReporteRebaja.add(rrp);
		
		rrp = new ReporteRebajaPojo();
		rrp.setRegion("Metropolitana");
		rrp.setServicio("Metropolitano Sur");
		rrp.setComuna("La Reina");
		
		listReporteRebaja.add(rrp);
		
		rrp = new ReporteRebajaPojo();
		rrp.setRegion("Metropolitana");
		rrp.setServicio("Metropolitano Norte");
		rrp.setComuna("La Dehesa");
		
		listReporteRebaja.add(rrp);
		
		rrp = new ReporteRebajaPojo();
		rrp.setRegion("Metropolitana");
		rrp.setServicio("Metropolitano Centro");
		rrp.setComuna("Santiago Centro");
		
		listReporteRebaja.add(rrp);
		

	}
}
