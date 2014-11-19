package minsal.divap.enums;


public enum TiposPrograma{
	ProgramaPxQ(1, "PxQ"),  ProgramaHistorico(2,"Historico"), ProgramaLey(3,"Ley");
	
	private Integer id;
	private String name;

	private TiposPrograma(Integer id, String name) {
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

	public static TiposPrograma getById(int id){
		for (TiposPrograma tipoComuna : TiposPrograma.values()) {
			if (tipoComuna.id == id)
				return tipoComuna;
		}
		throw new IllegalArgumentException("id de comuna no válido");
	}
}