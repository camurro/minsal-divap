package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.TratamientoOrdenService;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoOTSeleccionarLineaFinanciamientoController")
@ViewScoped
public class ProcesoOTSeleccionarLineaFinanciamientoController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	
	@EJB
	private TratamientoOrdenService tratamientoOrdenService;
	
	@EJB
	private ProgramasService programaService;
	
	private List<AsignacionDistribucionPerCapitaVO> antecendentesComunaCalculado;
	
	//ID Programa
	private Integer idLineaProgramatica;
	
	public void setIdLineaProgramatica(Integer idLineaProgramatica) {
		this.idLineaProgramatica = idLineaProgramatica;
	}
	
	@PostConstruct
	public void init() {
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		
		Programa programa = programaService.getProgramaPorID(idLineaProgramatica);//programaId)
		
		if(programa.getTipoPrograma().getId() == 1)//TIPO PERCAPITA
		{
			parameters.put("revisarPerCapita_", "si");
			parameters.put("revisarLeyes_", "no");
		}
		else if(programa.getTipoPrograma().getId() == 2)//TIPO LEYES
		{
			parameters.put("revisarLeyes_", "si");
		}
		else if(programa.getTipoPrograma().getId() == 3)//TIPO PROGRAMATICA
		{
			parameters.put("revisarLeyes_", "no");
			parameters.put("revisarPerCapita_", "no");
		}

		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "bandejaTareas";
		Long procId = iniciarProceso(BusinessProcess.OTPROFESIONAL);
		System.out.println("procId-->"+procId);
		if(procId == null){
			 success = null;
		}else{
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean().getUsername());
			if(task != null){
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if(taskDataVO != null){
					System.out.println("taskDataVO recuperada="+taskDataVO);
					setOnSession("taskDataSeleccionada", taskDataVO);
				}
			}
		}
		return success;
	}
	

	//TODO: [ASAAVEDRA] Verificar cuando los programas estan iniciados.
	
	//Obtiene la lista de programas del usuario
	public List<ProcesosProgramasPojo> getListadoProgramasServicio() {
		List<ProcesosProgramasPojo> listadoProgramasServicio = new ArrayList<ProcesosProgramasPojo>();
		List<ProgramaVO> programas = programaService
				.getProgramasByUser(getLoggedUsername());
		for (ProgramaVO programaVO : programas) {
			ProcesosProgramasPojo p2 = new ProcesosProgramasPojo();
			p2.setPrograma(programaVO.getNombre());
			p2.setDescripcion("descripcion");
			p2.setId(programaVO.getId());
			listadoProgramasServicio.add(p2);
		}
		return listadoProgramasServicio;
	}
	
	// Continua el proceso con el programa seleccionado.
		public void continuarProceso(Integer id) {

			setIdLineaProgramatica(id);
			super.enviar();
		}
}
