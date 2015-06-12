package minsal.divap.enums;


public enum TipoComponenteEnum {
	PXQ(1, "PxQ"), 
	HISTORICO(2,"Histórico"), 
	LEY(3,"Ley");
	
	private Integer id;
	private String name;
	
	private TipoComponenteEnum(Integer id, String name){
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
