package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import cl.minsal.divap.pojo.IngresoResolucionPojo;
import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named ( "procesoConveniosController" ) @ViewScoped public class ProcesoConveniosController extends BaseController implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;
	@Inject private BandejaProperties bandejaProperties;
	@Inject FacesContext facesContext;
	
	List<IngresoResolucionPojo> listResoluciones = new ArrayList<IngresoResolucionPojo>();

	public List<IngresoResolucionPojo> getListResoluciones() {
		return listResoluciones;
	}

	public void setListResoluciones( List<IngresoResolucionPojo> listResoluciones ) {
		this.listResoluciones = listResoluciones;
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
}
