package minsal.divap.enums;

public enum Subtitulo {
	SUBTITULO24(3,"SUbtítulo 24"), SUBTITULO21(1, "SUbtítulo 21"), SUBTITULO22(2,"SUbtítulo 22"), SUBTITULO29(4,"SUbtítulo 29");
	private Integer id;

	private Subtitulo(Integer id, String name) {
		this.id = id; 
	}

	public Integer getId()
	{
		return this.id;
	}

	public static Subtitulo getById(int id){
		for (Subtitulo subtitulo : Subtitulo.values()) {
			if (subtitulo.id == id)
				return subtitulo;
		}
		throw new IllegalArgumentException("id de documento no válido");
	}
}
