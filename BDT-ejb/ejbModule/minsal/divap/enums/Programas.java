package minsal.divap.enums;


public enum Programas{
	PERCAPITA(-1, "PERCAPITA"), DESEMPENODIFICIAL(-2,"DESEMPEÑO DIFICIL"), REBAJAIAAPS(-3,"REBAJAIAAPS");
	private Integer id;
	private String name;

	private Programas(Integer id, String name) {
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

	public static Programas getById(int id){
		for (Programas tipoComuna : Programas.values()) {
			if (tipoComuna.id == id)
				return tipoComuna;
		}
		throw new IllegalArgumentException("id de programa no válido");
	}
}