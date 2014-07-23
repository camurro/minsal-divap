package cl.redhat.bandejaTareas.util;

import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

public class MatchViewTask {
	
	enum ViewTask{
		SUBIRDOCUMENTOS("minsal_divap.DistribucionInicialPerCapita.subirdocumentos", "divapProcesoAsignacionPerCapitaCargarValorizacion"),
		
		// Mapeo páginas de Rebaja
		REBAJASUBIRDOCUMENTOS("minsal.divap.procesos.Rebaja.subirinformaciondecumplimientoporcomuna", "divapProcesoRebajaCargarInformacionCumplimiento"),
		REBAJAREVISIONMONTOS("minsal.divap.procesos.Rebaja.revisionyvalidaciondelosmontosderebaja","divapProcesoRebajaRevisionValidacionRebaja"),
		REBAJARESOLUCIONES("minsal.divap.procesos.Rebaja.seguimientoderesoluciones","divapProcesoRebajaSeguimientoTratamientoRevision"),
		
		// Mapeo páginas de Reliquidación
		RELIQUIDACIONPROGRAMAS("minsal.divap.procesos.Reliquidacion.seleccionarprograma","divapProcesoReliqProgramas"),
		RELIQUIDACIONARCHIVOS("minsal.divap.procesos.Reliquidacion.descargarysubirplanillacumplimiento","divapProcesoReliqPlanillas"),		
		RELIQUIDACIONMUNICIPAL("minsal.divap.procesos.Reliquidacion.validacionmontosreliquidacionmunicipal","divapProcesoReliqMunicipal"),
		RELIQUIDACIONSERVICIO("minsal.divap.procesos.Reliquidacion.validacionmontosreliquidacionservicio","divapProcesoReliqServicio");

		
		private String key;
		private String view;

		private ViewTask(String key, String view) {
			this.key = key; 
			this.view = view;
		}

		public String getKey() {
			return key;
		}
		
		public String getView() {
			return view;
		}

		public static ViewTask getByKey(String key){
			for (ViewTask viewTask : ViewTask.values()) {
				if(viewTask.getKey().equals(key)){
					return viewTask;
				}
			}
			throw new IllegalArgumentException("nombre de tarea no válido");
		}
	}
	
	public static String matchView(TaskDataVO taskDataVO){
		return matchView(taskDataVO.getTask());
	}
	
	public static String matchView(TaskVO task)	{
		String process = task.getProcessId();
		String taskName = task.getName().replaceAll(" ", "");
		String result = process + "." + taskName.toLowerCase();
		result = result.replaceAll("ñ", "n");
		result = result.replaceAll("á", "a");
		result = result.replaceAll("é", "e");
		result = result.replaceAll("í", "i");
		result = result.replaceAll("ó", "o");
		result = result.replaceAll("ú", "u");
		result = result.replaceAll("['\"~°]", "");
		System.out.println("result-->"+result);
		return ViewTask.getByKey(result).getView();
	}
}
