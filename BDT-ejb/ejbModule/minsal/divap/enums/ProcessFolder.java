package minsal.divap.enums;


public enum ProcessFolder{

	ESTIMACIONFLUJOCAJA(0, "ESTIMACIONFLUJOCAJA"), 
	PERCAPITA(1,"PERCAPITA"), 
	REBAJA(2,"REBAJA"), 
	RECURSOSFINANCIEROSPROGRAMASREFORZAMIENTOAPS(3,"RECURSOSFINANCIEROSPROGRAMASREFORZAMIENTOAPS"), 
	RELIQUIDACION(4,"RELIQUIDACION");
	private int id;
	private String name;

	private ProcessFolder(int id, String name) {
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

	public static ProcessFolder getById(int id){
		for (ProcessFolder process : ProcessFolder.values()) {
			if (process.id == id)
				return process;
		}
		throw new IllegalArgumentException("id de proceso no válido");
	}
}
