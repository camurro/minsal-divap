package minsal.divap.enums;

public enum Subtitulo {
	SUBTITULO24(3,"Subtítulo 24"), SUBTITULO21(1, "Subtítulo 21"), SUBTITULO22(2,"Subtítulo 22"), SUBTITULO29(4,"Subtítulo 29");
	private Integer id;
	private String nombre;

	private Subtitulo(Integer id, String nombre) {
		this.id = id; 
		this.nombre = nombre;
	}

	public Integer getId(){
		return this.id;
	}

	public String getNombre() {
		return nombre;
	}

	public static Subtitulo getById(int id){
		for (Subtitulo subtitulo : Subtitulo.values()) {
			if (subtitulo.id == id)
				return subtitulo;
		}
		throw new IllegalArgumentException("id de documento no válido");
	}
}
