package minsal.divap.enums;


public enum TipoComuna{
	RURAL(1, "RURAL"), COSTOFIJO(2,"COSTO FIJO"), 
	URBANA(3,"URBANA");

	private Integer id;
	private String name;

	private TipoComuna(Integer id, String name) {
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

	public static TipoComuna getById(int id){
		for (TipoComuna tipoComuna : TipoComuna.values()) {
			if (tipoComuna.id == id)
				return tipoComuna;
		}
		throw new IllegalArgumentException("id de comuna no válido");
	}
}