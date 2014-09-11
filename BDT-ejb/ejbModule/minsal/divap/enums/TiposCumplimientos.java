package minsal.divap.enums;


public enum TiposCumplimientos{
	ACTIVIDADGENERAL(1, "Actividad General"),  CONTINUIDADATENCIONSALUD(2,"Continuidad de la Atención de Salud"),
	ACTIVIDADGARANTIASEXPLICITASSALUD(3,"Actividad con Garantías Explícitas en Salud");
	
	private Integer id;
	private String name;

	private TiposCumplimientos(Integer id, String name) {
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

	public static TiposCumplimientos getById(int id){
		for (TiposCumplimientos tipoComuna : TiposCumplimientos.values()) {
			if (tipoComuna.id == id)
				return tipoComuna;
		}
		throw new IllegalArgumentException("id de comuna no válido");
	}
}