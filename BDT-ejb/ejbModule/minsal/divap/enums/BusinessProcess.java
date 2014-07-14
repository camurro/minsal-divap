package minsal.divap.enums;


public enum BusinessProcess{
	PERCAPITA(0, "minsal_divap.DistribucionIncialPerCapita"), REBAJAS(1,"minsal_divap.rebaja");

	private int id;
	private String name;

	private BusinessProcess(int id, String name) {
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

	public static BusinessProcess getById(int id){
		for (BusinessProcess process : BusinessProcess.values()) {
			if (process.id == id)
				return process;
		}
		throw new IllegalArgumentException("id de proceso no válido");
	}
}