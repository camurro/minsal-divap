package minsal.divap.enums;


public enum TemplatesType{
	DESEMPENODIFICIL(1), RECURSOSPERCAPITA(2), BASECUMPLIMIENTO(3),REBAJACALCULADA(4)  ;

	private Integer id;
	
	private TemplatesType(int id) { this.id = id; }

	public Integer getId()
	{
		return this.id;
	}
	
	public static TemplatesType getById(int id){
        for (TemplatesType templatesType : TemplatesType.values()) {
            if (templatesType.id == id)
                return templatesType;
        }
        throw new IllegalArgumentException("id plantilla no válido");
    }
}