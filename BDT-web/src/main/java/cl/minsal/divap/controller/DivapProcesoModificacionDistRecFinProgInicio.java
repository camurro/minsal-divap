package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("divapProcesoModificacionDistRecFinProgInicio")
@ViewScoped
public class DivapProcesoModificacionDistRecFinProgInicio extends AbstractTaskMBean
implements Serializable {

	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 6167176749346305150L;

	@Override
	public String toString() {
		return "DivapModificacionProcesoDistRecFinProgInicio [validarMontosDistribucion= ]";
	}

	@PostConstruct
	public void init() {
		if(sessionExpired()){
			return;
		}
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"
				+ getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "divapProcesoModificacionDistRecFinProgProgramasServicio";
		Long procId = iniciarProceso(BusinessProcess.MODIFICACIONRECURSOSFINANCIEROSAPS);
		System.out.println("procId-->" + procId);
		if (procId == null) {
			success = null;
		} else {
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean()
					.getUsername());
			if (task != null) {
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if (taskDataVO != null) {
					System.out.println("taskDataVO recuperada=" + taskDataVO);
					setOnSession("taskDataSeleccionada", taskDataVO);
				}
			}
		}
		return success;
	}

}
