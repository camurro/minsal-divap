package minsal.divap.enums;


public enum TiposSubtitulos{
	Subtitulo21(1, "SUbtítulo 21"),  Subtitulo22(2,"SUbtítulo 22"), 
	Subtitulo24(3,"SUbtítulo 24"), Subtitulo29(4,"SUbtítulo 29");

	private Integer id;
	private String name;

	private TiposSubtitulos(Integer id, String name) {
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

	public static TiposSubtitulos getById(int id){
		for (TiposSubtitulos tipoComuna : TiposSubtitulos.values()) {
			if (tipoComuna.id == id)
				return tipoComuna;
		}
		throw new IllegalArgumentException("id de comuna no válido");
	}
}