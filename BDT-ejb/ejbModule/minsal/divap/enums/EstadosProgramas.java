package minsal.divap.enums;


public enum EstadosProgramas{
	SININICIAR(1, "Sin Iniciar"),  ENCURSO(2,"En Curso"), 
	FINALIZADO(3, "Finalizado");

	private Integer id;
	private String name;

	private EstadosProgramas(Integer id, String name) {
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

	public static EstadosProgramas getById(int id){
		for (EstadosProgramas tipoComuna : EstadosProgramas.values()) {
			if (tipoComuna.id == id)
				return tipoComuna;
		}
		throw new IllegalArgumentException("id de comuna no válido");
	}
}