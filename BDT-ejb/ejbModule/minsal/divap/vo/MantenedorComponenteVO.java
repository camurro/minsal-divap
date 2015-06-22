package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class MantenedorComponenteVO implements Serializable{

	
	private static final long serialVersionUID = -5618971000476522859L;
	
	private Integer idComponente;
	private String nombreComponente;
	private Integer idTipoComponente;
	private String nombreTipoComponente;
	private Float peso;
	private List<SubtituloVO> nombreSubtitulos;
	private List<SubtituloVO> nombreSubtitulosFaltantes;
	private Boolean puedeEliminarse;
	
	public MantenedorComponenteVO(){
		
	}
	

	public Integer getIdComponente() {
		return idComponente;
	}

	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}

	public String getNombreComponente() {
		return nombreComponente;
	}

	public void setNombreComponente(String nombreComponente) {
		this.nombreComponente = nombreComponente;
	}

	public Integer getIdTipoComponente() {
		return idTipoComponente;
	}

	public void setIdTipoComponente(Integer idTipoComponente) {
		this.idTipoComponente = idTipoComponente;
	}

	public String getNombreTipoComponente() {
		return nombreTipoComponente;
	}

	public void setNombreTipoComponente(String nombreTipoComponente) {
		this.nombreTipoComponente = nombreTipoComponente;
	}

	public Float getPeso() {
		return peso;
	}

	public void setPeso(Float peso) {
		this.peso = peso;
	}
	
	public List<SubtituloVO> getNombreSubtitulos() {
		return nombreSubtitulos;
	}

	public void setNombreSubtitulos(List<SubtituloVO> nombreSubtitulos) {
		this.nombreSubtitulos = nombreSubtitulos;
	}

	
	public List<SubtituloVO> getNombreSubtitulosFaltantes() {
		return nombreSubtitulosFaltantes;
	}

	public void setNombreSubtitulosFaltantes(List<SubtituloVO> nombreSubtitulosFaltantes) {
		this.nombreSubtitulosFaltantes = nombreSubtitulosFaltantes;
	}

	public Boolean getPuedeEliminarse() {
		return puedeEliminarse;
	}

	public void setPuedeEliminarse(Boolean puedeEliminarse) {
		this.puedeEliminarse = puedeEliminarse;
	}


	@Override
	public String toString() {
		return "MantenedorComponenteVO [idComponente=" + idComponente
				+ ", nombreComponente=" + nombreComponente
				+ ", idTipoComponente=" + idTipoComponente
				+ ", nombreTipoComponente=" + nombreTipoComponente + ", peso="
				+ peso + ", nombreSubtitulos=" + nombreSubtitulos
				+ ", nombreSubtitulosFaltantes=" + nombreSubtitulosFaltantes
				+ ", puedeEliminarse=" + puedeEliminarse + "]";
	}

}
