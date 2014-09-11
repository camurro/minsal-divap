package minsal.divap.enums;


public enum TareasSeguimiento{
	HACERSEGUIMIENTOOFICIO(1, "Hacer seguimiento oficio"), 
	HACERSEGUIMIENTODECRETO(2, "Hacer Seguimiento Decreto"),

	HACERSEGUIMIENTOTOMARAZON(3, "Hacer Seguimiento Toma de Razon"), 
	HACERSEGUIMIENTORESOLUCIONES(4,"Hacer Seguimiento Resoluciones"),
	HACERSEGUIMIENTOESTIMACIONFLUJOCAJA(5,"Hacer Seguimiento Ordinario"),

	
	HACERSEGUIMIENTOOT(5,"Hacer Seguimiento Ordinarios de Ordenes de Transferencia"),
	HACERSEGUIMIENTORESOLUCIONREABAJA(10,"Hacer Seguimiento de Resoluciones de Rebaja");

	
	private Integer id;
	private String name;

	private TareasSeguimiento(Integer id, String name) {
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

	public static TareasSeguimiento getById(int id){
		for (TareasSeguimiento tareasSeguimiento : TareasSeguimiento.values()) {
			if (tareasSeguimiento.id == id)
				return tareasSeguimiento;
		}
		throw new IllegalArgumentException("id de tarea seguimiento no válido");
	}
}