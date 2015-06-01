package cl.minsal.divap.mantenedores.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.service.ReportesServices;
import cl.redhat.bandejaTareas.controller.BaseController;

@Named("menuMantenedoresController")
@ViewScoped
public class MenuMantenedoresController extends BaseController implements Serializable{

	private static final long serialVersionUID = -1278346523098543061L;
	private Integer mesCurso;
	private Integer anoCurso;
	private Boolean mostrarMenuComunasAnosiguiente;
	
	@EJB
	ReportesServices reportesService;
	
	@PostConstruct
	public void init(){
		anoCurso = reportesService.getAnoCurso();
		mesCurso = Integer.parseInt(reportesService.getMesCurso(true));
		mostrarMenuComunasAnosiguiente = false;
		if(mesCurso > 9 && mesCurso < 13){
			mostrarMenuComunasAnosiguiente = true;
		}
	}

	public Integer getMesCurso() {
		return mesCurso;
	}
	public void setMesCurso(Integer mesCurso) {
		this.mesCurso = mesCurso;
	}
	public Integer getAnoCurso() {
		return anoCurso;
	}
	public void setAnoCurso(Integer anoCurso) {
		this.anoCurso = anoCurso;
	}
	public Boolean getMostrarMenuComunasAnosiguiente() {
		return mostrarMenuComunasAnosiguiente;
	}
	public void setMostrarMenuComunasAnosiguiente(
			Boolean mostrarMenuComunasAnosiguiente) {
		this.mostrarMenuComunasAnosiguiente = mostrarMenuComunasAnosiguiente;
	}

}
