package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorComunaFinalVO implements Serializable{


	private static final long serialVersionUID = 7403543859974140531L;
	
	private String idClasificacion;
	private String clasificacion;
	private String idAsigZona;
	private Double asigZonaValor;
	private String idTramoPobreza;
	private Double tramoPobreza;
	private Integer idAntecedentesComuna;
	private Integer idComuna;
	private String nombreComuna;
	private String idServicio;
	private String nombreServicio;
	private Integer ano;
	private Integer nroResolucion;
	private Boolean comunaAuxiliar;
	
	public MantenedorComunaFinalVO() {
		super();
	}

	public MantenedorComunaFinalVO(String idClasificacion, String clasificacion, String idAsigZona, Double asigZonaValor, String idTramoPobreza, Double tramoPobreza, Integer idAntecedentesComuna, Integer idComuna, String nombreComuna, String idServicio, String nombreServicio, Integer ano, Integer nroResolucion, Boolean comunaAuxiliar) {
		super();
		
		 this.idClasificacion = idClasificacion;
		 this.clasificacion = clasificacion;
		 this.idAsigZona = idAsigZona;
		 this.asigZonaValor = asigZonaValor;
		 this.idTramoPobreza = idTramoPobreza;
		 this.tramoPobreza = tramoPobreza;
		 this.idAntecedentesComuna = idAntecedentesComuna;
		 this.idComuna = idComuna;
		 this.nombreComuna = nombreComuna;
		 this.idServicio = idServicio;
		 this.nombreServicio = nombreServicio;
		 this.ano = ano;
		 this.nroResolucion = nroResolucion;
		 this.comunaAuxiliar = comunaAuxiliar;
	}

	public String getIdClasificacion() {
		return idClasificacion;
	}

	public void setIdClasificacion(String idClasificacion) {
		this.idClasificacion = idClasificacion;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public String getIdAsigZona() {
		return idAsigZona;
	}

	public void setIdAsigZona(String idAsigZona) {
		this.idAsigZona = idAsigZona;
	}

	public Double getAsigZonaValor() {
		return asigZonaValor;
	}

	public void setAsigZonaValor(Double asigZonaValor) {
		this.asigZonaValor = asigZonaValor;
	}

	public String getIdTramoPobreza() {
		return idTramoPobreza;
	}

	public void setIdTramoPobreza(String idTramoPobreza) {
		this.idTramoPobreza = idTramoPobreza;
	}

	public Double getTramoPobreza() {
		return tramoPobreza;
	}

	public void setTramoPobreza(Double tramoPobreza) {
		this.tramoPobreza = tramoPobreza;
	}

	public Integer getIdAntecedentesComuna() {
		return idAntecedentesComuna;
	}

	public void setIdAntecedentesComuna(Integer idAntecedentesComuna) {
		this.idAntecedentesComuna = idAntecedentesComuna;
	}

	public Integer getIdComuna() {
		return idComuna;
	}

	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}

	public String getNombreComuna() {
		return nombreComuna;
	}

	public void setNombreComuna(String nombreComuna) {
		this.nombreComuna = nombreComuna;
	}

	public String getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(String idServicio) {
		this.idServicio = idServicio;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getNroResolucion() {
		return nroResolucion;
	}

	public void setNroResolucion(Integer nroResolucion) {
		this.nroResolucion = nroResolucion;
	}

	public Boolean getComunaAuxiliar() {
		return comunaAuxiliar;
	}

	public void setComunaAuxiliar(Boolean comunaAuxiliar) {
		this.comunaAuxiliar = comunaAuxiliar;
	}

	@Override
	public String toString() {
		return "MantenedorComunaFinalVO [idClasificacion=" + idClasificacion
				+ ", clasificacion=" + clasificacion + ", idAsigZona="
				+ idAsigZona + ", asigZonaValor=" + asigZonaValor
				+ ", idTramoPobreza=" + idTramoPobreza + ", tramoPobreza="
				+ tramoPobreza + ", idAntecedentesComuna="
				+ idAntecedentesComuna + ", idComuna=" + idComuna
				+ ", nombreComuna=" + nombreComuna + ", idServicio="
				+ idServicio + ", nombreServicio=" + nombreServicio + ", ano="
				+ ano + ", nroResolucion=" + nroResolucion
				+ ", comunaAuxiliar=" + comunaAuxiliar + "]";
	}
	
}
