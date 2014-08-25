package minsal.divap.enums;


public enum DependenciasPrograma{
	ProgramaMunicipal(1, "Programa Municipal"),  ProgramaServicios(2,"Programa Servicio"), 
	ProgramaMixto(3,"Programa Mixto");

	private Integer id;
	private String name;

	private DependenciasPrograma(Integer id, String name) {
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

	public static DependenciasPrograma getById(int id){
		for (DependenciasPrograma tipoComuna : DependenciasPrograma.values()) {
			if (tipoComuna.id == id)
				return tipoComuna;
		}
		throw new IllegalArgumentException("id de comuna no válido");
	}
}