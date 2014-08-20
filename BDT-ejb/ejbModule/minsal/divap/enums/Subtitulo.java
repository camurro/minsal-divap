package minsal.divap.enums;

public enum Subtitulo {
	SUBTITULO24(24), SUBTITULO21(21), SUBTITULO22(22), SUBTITULO29(29);
	
private int id;
	
	private Subtitulo(int id) { this.id = id; }

	public int getId()
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
