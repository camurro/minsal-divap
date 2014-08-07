package minsal.divap.enums;


public enum TiposDestinatarios{
	PARA(1, "Para"), CC(2, "Cc"), CCO(3, "Cco");

	private int id;
	private String name;

	private TiposDestinatarios(int id, String name) {
		this.id = id; 
		this.name = name;
	}

	public int getId()
	{
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static TiposDestinatarios getById(int id){
		for (TiposDestinatarios tiposDestinatarios : TiposDestinatarios.values()) {
			if (tiposDestinatarios.id == id)
				return tiposDestinatarios;
		}
		throw new IllegalArgumentException("id de tipos destinatarios no válido");
	}
}