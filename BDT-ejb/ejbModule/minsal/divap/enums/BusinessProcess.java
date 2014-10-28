package minsal.divap.enums;


public enum BusinessProcess{

	PERCAPITA(0, "minsal_divap.DistribucionInicialPerCapita"), 
	REBAJA(1,"minsal.divap.procesos.Rebaja"), 

	
	TRAMITACIONORDEN(5,"minsal_divap.TramitacionOrdenTransferenciaConsolidador"),
	
	RELIQUIDACION(2,"minsal.divap.procesos.Reliquidacion"), 
	RECURSOSFINANCIEROSAPS(3,"minsal_divap.DistribucionRecursosProgramasReforzamiento"),
    Prueba(4,"minsal_divap.prueba"),
    ESTIMACIONFLUJOCAJA(5,"minsal_divap.EstimacionFlujoCaja"),
    ESTIMACIONFLUJOCAJACONSOLIDADOR(6,"minsal_divap.EstimacionFlujoConsolidador"),
	OTPROFESIONAL(7,"minsal_divap.TramitacionOrdenTransferenciaProfesional"),
	OTCONSOLIDADOR(8,"minsal_divap.TramitacionOrdenTransferenciaConsolidador"),
	CONVENIOS(9,"minsal_divap.ProcesoConvenio");

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
