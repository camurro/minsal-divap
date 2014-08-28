package cl.minsal.divap.pojo;

public class ProcesosProgramasPojo {
	private String programa;
	private String descripcion;
	private Double progreso;
	private Boolean editar;
	private Boolean terminar;
	private String estado;
	private String url;
	private Integer id;
	private Integer estadoFlujoCaja;
	
	
	public Integer getEstadoFlujoCaja() {
		return estadoFlujoCaja;
	}

	public void setEstadoFlujoCaja(Integer estadoFlujoCaja) {
		this.estadoFlujoCaja = estadoFlujoCaja;
	}

	public String getPrograma() {
		return programa;
	}
	
	public void setPrograma( String programa ) {
		this.programa = programa;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion( String descripcion ) {
		this.descripcion = descripcion;
	}
	
	public Double getProgreso() {
		return progreso;
	}
	
	public void setProgreso( Double progreso ) {
		this.progreso = progreso;
	}
	
	public Boolean getEditar() {
		return editar;
	}
	
	public void setEditar( Boolean editar ) {
		this.editar = editar;
	}
	
	public Boolean getTerminar() {
		return terminar;
	}
	
	public void setTerminar( Boolean terminar ) {
		this.terminar = terminar;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado( String estado ) {
		this.estado = estado;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl( String url ) {
		this.url = url;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
