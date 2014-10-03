package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComponenteSummaryVO implements Serializable{


	private static final long serialVersionUID = -1926890921516075410L;

	private String nombre;
	private List<SubtituloSummaryVO> subtitulos = new ArrayList<SubtituloSummaryVO>();
	
	public ComponenteSummaryVO(String nombre) {
		super();
		this.nombre = nombre;
	}
	
	public ComponenteSummaryVO(String nombre, List<SubtituloSummaryVO> subtitulos) {
		super();
		this.nombre = nombre;
		this.subtitulos = subtitulos;
	}
	
	public ComponenteSummaryVO(String nombre, SubtituloSummaryVO subtitulo) {
		super();
		this.nombre = nombre;
		this.subtitulos.add(subtitulo);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<SubtituloSummaryVO> getSubtitulos() {
		return subtitulos;
	}

	public void setSubtitulos(List<SubtituloSummaryVO> subtitulos) {
		this.subtitulos = subtitulos;
	}

	@Override
	public String toString() {
		return "ComponenteSummaryVO [nombre=" + nombre + ", subtitulos="
				+ subtitulos + "]";
	}
	
}