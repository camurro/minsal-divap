package minsal.divap.enums;


public enum EstadosConvenios{
	INGRESADO(1, "Ingresado"),  APROBADO(2,"Aprobado"), 
	RECHAZADO(3, "Rechazado");

	private Integer id;
	private String name;

	private EstadosConvenios(Integer id, String name) {
		this.id = id; 
		this.name = name;
	}

	public Integer getId()
	{
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static EstadosConvenios getById(int id){
		for (EstadosConvenios tipoComuna : EstadosConvenios.values()) {
			if (tipoComuna.id == id)
				return tipoComuna;
		}
		throw new IllegalArgumentException("id de comuna no válido");
	}
}