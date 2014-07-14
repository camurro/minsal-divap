package minsal.divap.enums;


public enum ProcessDocument{
	RESULTADOREBAJAS(1), REBAJASCOMUNAS(2),  OFICIOCONSULTA(3);

	private int id;
	
	private ProcessDocument(int id) { this.id = id; }

	public int getId()
	{
		return this.id;
	}
	
	public static ProcessDocument getById(int id){
        for (ProcessDocument processDocument : ProcessDocument.values()) {
            if (processDocument.id == id)
                return processDocument;
        }
        throw new IllegalArgumentException("id de documento no válido");
    }
}