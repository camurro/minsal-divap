package minsal.divap.enums;


public enum Instituciones{
	FONASA(1, "Para");

	private int id;
	private String name;

	private Instituciones(int id, String name) {
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

	public static Instituciones getById(int id){
		for (Instituciones tiposDestinatarios : Instituciones.values()) {
			if (tiposDestinatarios.id == id)
				return tiposDestinatarios;
		}
		throw new IllegalArgumentException("id de tipos destinatarios no válido");
	}
}