package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ProgramaMunicipalSummaryVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5435842805479614786L;
	
	private String nombre;
	private List<ComponenteSummaryVO> componentes = new ArrayList<ComponenteSummaryVO>();
	
	public ProgramaMunicipalSummaryVO(String nombre) {
		super();
		this.nombre = nombre;
	}
	
	public ProgramaMunicipalSummaryVO(String nombre, ComponenteSummaryVO componente) {
		super();
		this.nombre = nombre;
		this.componentes.add(componente);
	}
	
	public ProgramaMunicipalSummaryVO(String nombre, List<ComponenteSummaryVO> componentes) {
		super();
		this.nombre = nombre;
		this.componentes = componentes;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<ComponenteSummaryVO> getComponentes() {
		return componentes;
	}
	
	public void setComponentes(List<ComponenteSummaryVO> componentes) {
		this.componentes = componentes;
	}

	@Override
	public String toString() {
		return "ProgramaMunicipalSummaryVO [nombre=" + nombre
				+ ", componentes=" + componentes + "]";
	}

}
