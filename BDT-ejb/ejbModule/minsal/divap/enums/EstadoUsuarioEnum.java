package minsal.divap.enums;

public enum EstadoUsuarioEnum {
	ACTIVO(1, "activo"), 
	BLOQUEADO(2,"bloqueado"), 
	ELIMINADO(3,"eliminado"),
	CLAVETEMPORAL(4, "clave temporal");
	
	private Integer id;
	private String name;
	
	private EstadoUsuarioEnum(Integer id, String name){
		this.id = id; 
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
