
package cl.redhat.bandejaTareas.util;

import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

public class MatchViewTask {

	enum ViewTask{
		// Mapero pÃ¡ginas Per Capita
		SUBIRDOCUMENTOS("minsal_divap.DistribucionInicialPerCapita.subirdocumentos", "divapProcesoAsignacionPerCapitaCargarValorizacion"),
		VALIDARRESULTADOSVALORIZACION("minsal_divap.DistribucionInicialPerCapita.validarresultadosvalorizacion", "divapProcesoAsignacionPerCapitaValidarMontosDistribucion"),
		HACERSEGUIMIENTOOFICIO("minsal_divap.DistribucionInicialPerCapita.hacerseguimientooficiosconsultaregional", "divapProcesoAsignacionPerCapitaSeguimiento"),
		HACERSEGUIMIENTODECRETO("minsal_divap.DistribucionInicialPerCapita.hacerseguimientodecreto/subirultimaversion", "divapProcesoAsignacionPerCapitaSeguimiento"),
		HACERSEGUIMIENTORESOLUCIONES("minsal_divap.DistribucionInicialPerCapita.hacerseguimientoresoluciones", "divapProcesoAsignacionPerCapitaSeguimiento"),
		HACERSEGUIMIENTOTOMARAZON("minsal_divap.DistribucionInicialPerCapita.hacerseguimientotomaderazon", "divapProcesoAsignacionPerCapitaSeguimiento"),
		SUBIRDOCUMENTOSFINALES("minsal_divap.DistribucionInicialPerCapita.subirdocumentostotalmentetramitados(decretosyresoluciones)", "divapProcesoAsignacionPerCapitaSubirDocumentosTotalmenteTramitados"),

		// Mapeo pÃ¡ginas de Rebaja
		REBAJASUBIRDOCUMENTOS("minsal.divap.procesos.Rebaja.subirinformaciondecumplimientoporcomuna", "divapProcesoRebajaCargarInformacionCumplimiento"),
		REBAJAREVISIONMONTOS("minsal.divap.procesos.Rebaja.revisionyvalidaciondelosmontosderebaja","divapProcesoRebajaRevisionValidacionRebaja"),
		REBAJARESOLUCIONES("minsal.divap.procesos.Rebaja.seguimientoderesolucionesderebaja","divapProcesoRebajaSeguimientoTratamientoRevision"),
														 
		// Mapeo pÃ¡ginas de ReliquidaciÃ³n
		RELIQUIDACIONPROGRAMAS("minsal.divap.procesos.Reliquidacion.seleccionarprograma","divapProcesoReliqProgramas"),
		RELIQUIDACIONARCHIVOS("minsal.divap.procesos.Reliquidacion.descargarysubirplanillacumplimiento","divapProcesoReliqPlanillas"),		
		RELIQUIDACIONMUNICIPAL("minsal.divap.procesos.Reliquidacion.validacionmontosreliquidacionmunicipal","divapProcesoReliqMunicipal"),
		RELIQUIDACIONSERVICIO("minsal.divap.procesos.Reliquidacion.validacionmontosreliquidacionservicio","divapProcesoReliqServicio"),
		RELIQUIDACIONMIXTO("minsal.divap.procesos.Reliquidacion.validacionmontosreliquidacionmixto","divapProcesoReliqServicio"),
		//Validacion Montos Reliquidacion

		
		// Mapeo paginas de Tramitacion Orden Transferencia Profesional
		OTSELECCIONARLINEAFINANCIAMIENTO("minsal_divap.TramitacionOrdenTransferenciaProfesional.seleccionarlineadefinanciamiento","divapProcesoOTLineas"),
		OTREVISARANTECEDENTESPROGRAMATICA("minsal_divap.TramitacionOrdenTransferenciaProfesional.revisarantecedentesprograma","divapProcesoOTRevisarAntecedentesProfesional"),

		// Mapeo paginas de Tramitacion Orden Transferencia Consolidador
		TRAMITACIONORDENREVISARCONSOLIDACION("minsal_divap.TramitacionOrdenTransferenciaConsolidador.revisarconsolidaciondeordenesdetransferencia","divapProcesoOTConsolidador"),
		TRAMITACIONORDENHACERSEGUIMIENTO("minsal_divap.TramitacionOrdenTransferenciaConsolidador.hacerseguimientoordinariosdeordenesdetransferencia","divapProcesoOTSeguimiento"),
		REVISARENVIODOCUMENTOS("minsal_divap.TramitacionOrdenTransferenciaConsolidador.verificarenviodedocumentos","divapProcesoOTEnviarDocumentos"),

		
		//Mapeo páginas de Distribución de Recursos Financieros para Programas de Reforzamiento APS 
		SELECCIONARPROGRAMA("minsal_divap.DistribucionRecursosProgramasReforzamiento.seleccionarprograma","divapProcesoDistRecFinProgProgramasServicio"),
		DESCARGARSUBIRVALORIZACIONMUNICIPAL("minsal_divap.DistribucionRecursosProgramasReforzamiento.descargar/subirplanillasvalorizacion(programapxqcondependenciamunicipal)","divapProcesoDistRecFinProgSubirPlanillasMunicipal"),
		DESCARGARSUBIRVALORIZACIONSERVICIO("minsal_divap.DistribucionRecursosProgramasReforzamiento.descargar/subirplanillasvalorizacion(programapxqcondependenciadeserviciodesalud)","divapProcesoDistRecFinProgSubirPlanillasServicioSalud"),
		DESCARGARSUBIRVALORIZACIONMIXTO("minsal_divap.DistribucionRecursosProgramasReforzamiento.descargar/subirplanillasvalorizacion(programapxqcondependenciadeserviciodesaludymunicipal)","divapProcesoDistRecFinProgSubirPlanillasMixto"),
		REVISARVALIDARMONTOSPxQMUNICIPAL("minsal_divap.DistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programapxqcondependenciamunicipal)","divapProcesoDistRecFinProgMunicipal"),
		REVISARVALIDARMONTOSPxQSERVICIO("minsal_divap.DistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programapxqcondependenciadeserviciodesalud)","divapProcesoDistRecFinProgServicioSalud"),
		REVISARVALIDARMONTOSPxQMIXTO("minsal_divap.DistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programapxqcondependenciadeserviciodesaludymunicipal)","divapProcesoDistRecFinProgMixto"),
		REVISARVALIDARMONTOSHISTORICOMUNICIPAL("minsal_divap.DistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programavaloreshistoricoscondependenciamunicipal)","divapProcesoDistRecFinProgValoresHistoricosMunicipal"),
		REVISARVALIDARMONTOSHISTORICOSERVICIO("minsal_divap.DistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programavaloreshistoricoscondependenciadeserviciodesalud)","divapProcesoDistRecFinProgValoresHistoricosServicioSalud"),
		REVISARVALIDARMONTOSHISTORICOMIXTOL("minsal_divap.DistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programavaloreshistoricoscondependenciadeserviciodesaludymunicipal)","divapProcesoDistRecFinProgValoresHistoricosMixto"),
		HACERSEGUIMIENTO("minsal_divap.DistribucionRecursosProgramasReforzamiento.hacerseguimientoadocumentos", "divapProcesoDistRecFinProgSeguimientoDocumentacion"),
		REVISARRESUMENESDOCUMENTOS("minsal_divap.DistribucionRecursosProgramasReforzamiento.revisarresumenenviodocumentos", "divapProcesoDistRecFinProgEnviarDocumentos"),
		DESCARGARSUBIRVALORIZACIONLEY("minsal_divap.DistribucionRecursosProgramasReforzamiento.descargar/subirplanillaley","divapProcesoDistRecFinProgSubirPlanillasLey"),
		REVISARVALIDARMONTOSLEY("minsal_divap.DistribucionRecursosProgramasReforzamiento.revisaryvalidarmontosley","divapProcesoDistRecFinProgLey"),
		
		//Mapeo páginas de Modificación Distribución de Recursos Financieros para Programas de Reforzamiento APS 
		MODSELECCIONARPROGRAMA("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.seleccionarprograma","divapProcesoModificacionDistRecFinProgProgramasServicio"),
		MODDESCARGARSUBIRVALORIZACIONMUNICIPAL("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.descargar/subirplanillasvalorizacion(programapxqcondependenciamunicipal)","divapProcesoModificacionDistRecFinProgSubirPlanillasMunicipal"),
		MODDESCARGARSUBIRVALORIZACIONSERVICIO("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.descargar/subirplanillasvalorizacion(programapxqcondependenciadeserviciodesalud)","divapProcesoModificacionDistRecFinProgSubirPlanillasServicioSalud"),
		MODDESCARGARSUBIRVALORIZACIONMIXTO("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.descargar/subirplanillasvalorizacion(programapxqcondependenciadeserviciodesaludymunicipal)","divapProcesoModificacionDistRecFinProgSubirPlanillasMixto"),
		MODREVISARVALIDARMONTOSPxQMUNICIPAL("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programapxqcondependenciamunicipal)","divapProcesoModificacionDistRecFinProgMunicipal"),
		MODREVISARVALIDARMONTOSPxQSERVICIO("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programapxqcondependenciadeserviciodesalud)","divapProcesoModificacionDistRecFinProgServicioSalud"),
		MODREVISARVALIDARMONTOSPxQMIXTO("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programapxqcondependenciadeserviciodesaludymunicipal)","divapProcesoModificacionDistRecFinProgMixto"),
		MODREVISARVALIDARMONTOSHISTORICOMUNICIPAL("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programavaloreshistoricoscondependenciamunicipal)","divapProcesoModificacionDistRecFinProgValoresHistoricosMunicipal"),
		MODREVISARVALIDARMONTOSHISTORICOSERVICIO("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programavaloreshistoricoscondependenciadeserviciodesalud)","divapProcesoModificacionDistRecFinProgValoresHistoricosServicioSalud"),
		MODREVISARVALIDARMONTOSHISTORICOMIXTOL("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.revisaryvalidarmontos(programavaloreshistoricoscondependenciadeserviciodesaludymunicipal)","divapProcesoModificacionDistRecFinProgValoresHistoricosMixto"),
		MODHACERSEGUIMIENTO("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.hacerseguimientoadocumentos", "divapProcesoModificacionDistRecFinProgSeguimientoDocumentacion"),
		MODREVISARRESUMENESDOCUMENTOS("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.revisarresumenenviodocumentos", "divapProcesoModificacionDistRecFinProgEnviarDocumentos"),
		MODDESCARGARSUBIRVALORIZACIONLEY("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.descargar/subirplanillaley","divapProcesoModificacionDistRecFinProgSubirPlanillasLey"),
		MODREVISARVALIDARMONTOSLEY("minsal_divap.ModificacionDistribucionRecursosProgramasReforzamiento.revisaryvalidarmontosley","divapProcesoModificacionDistRecFinProgLey"),
		
		
		PRUEBA("minsal_divap.prueba.primeratarea","divapPrueba"),
		PRUEBA2("minsal_divap.prueba.segundatarea","divapPrueba"),
		ESTIMACIONFLUJOCAJASELECCIONARLINEAPROGRAMATICA("minsal_divap.EstimacionFlujoCaja.seleccionarlineaprogramatica","divapProcesoProgProgramas"),
		ESTIMACIONFLUJOCAJAREVISARVALIDARMONITOREO("minsal_divap.EstimacionFlujoCaja.revisaryvalidarmonitoreo","divapProcesoProgMonitoreo"),
		ESTIMACIONFLUJOCAJAREVISARCONSOLIDACION("minsal_divap.EstimacionFlujoConsolidador.revisarconsolidaciondeestimaciondeflujocaja","divapProcesoProgConsolidado"),
		ESTIMACIONFLUJOCAJACONSOLIDACIONHACERSEGUIMIENTO("minsal_divap.EstimacionFlujoConsolidador.hacerseguimientoordinario","divapProcesoFlujoCajaSeguimiento"),
		ESTIMACIONFLUJOCAJACONSOLIDACIONREVISARENVIODOCUMENTOS("minsal_divap.EstimacionFlujoConsolidador.verificarenviocorreo","divapProcesoFlujoCajaEnviarDocumentos"),
		
		//Mapeo páginas de  Gestión de Información de Programas y Leyes para Transferencis(Convenios)
		CONVENIOSELECCIONARLINEAPROGRAMATICA("minsal_divap.ProcesoConvenio.seleccionarprograma", "divapProcesoGITSubir"),
		CONVENIOVERIFICARCONVENIOSINFORMADOS("minsal_divap.ProcesoConvenio.verificarconveniosinformadosporprograma", "divapProcesoGITVerificar"),
		CONVENIOHACERSEGUIMIENTORESOLUCIONES("minsal_divap.ProcesoConvenio.hacerseguimientoderesoluciones", "divapProcesoGITSeguimiento"), 
		CONVENIORESUMENDOCUMENTOSENVIADOS("minsal_divap.ProcesoConvenio.revisarresumendocumentos", "divapProcesoGITResumenDocumentos"),
																																											
		MODHACERSEGUIMIENTOSOLICITUDANTECEDENTES("minsal_divap.ModificacionDistribucionInicialPerCapita.hacerseguimientoordinariosdesolicituddeantecedentespoblacion", "divapProcesoModificacionAsignacionPerCapitaAntecedenteSeguimiento"),
		MODSUBIRDOCUMENTOS("minsal_divap.ModificacionDistribucionInicialPerCapita.subirdocumentos", "divapProcesoModificacionAsignacionPerCapitaCargarValorizacion"),
		MODVALIDARRESULTADOSVALORIZACION("minsal_divap.ModificacionDistribucionInicialPerCapita.validarresultadosvalorizacion", "divapProcesoModificacionAsignacionPerCapitaValidarMontosDistribucion"),
		MODHACERSEGUIMIENTOOFICIO("minsal_divap.ModificacionDistribucionInicialPerCapita.hacerseguimientooficiosconsultaregional", "divapProcesoModificacionAsignacionPerCapitaSeguimiento"),
		MODHACERSEGUIMIENTODECRETO("minsal_divap.ModificacionDistribucionInicialPerCapita.hacerseguimientodecreto/subirultimaversion", "divapProcesoModificacionAsignacionPerCapitaSeguimiento"),
		MODHACERSEGUIMIENTORESOLUCIONES("minsal_divap.ModificacionDistribucionInicialPerCapita.hacerseguimientoresoluciones", "divapProcesoModificacionAsignacionPerCapitaSeguimiento"),
		MODHACERSEGUIMIENTOTOMARAZON("minsal_divap.ModificacionDistribucionInicialPerCapita.hacerseguimientotomaderazon", "divapProcesoModificacionAsignacionPerCapitaSeguimiento"),
		MODSUBIRDOCUMENTOSFINALES("minsal_divap.ModificacionDistribucionInicialPerCapita.subirdocumentostotalmentetramitados(decretosyresoluciones)", "divapProcesoModificacionAsignacionPerCapitaSubirDocumentosTotalmenteTramitados");

		
		
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
			throw new IllegalArgumentException("nombre de tarea no vÃ¡lido");
		}
	}

	public static String matchView(TaskDataVO taskDataVO){
		return matchView(taskDataVO.getTask());
	}
	
	public static String matchView(TaskVO task)	{
		String process = task.getProcessId();
		System.out.println("original name-->"+task.getName());

		/*for (int i=0; i < task.getName().length(); i++){
			System.out.println("character-->"+unicodeEscaped(task.getName().charAt(i)) + " " + task.getName().charAt(i));
		}*/

		String taskName = task.getName().replaceAll(" ", "");
		String result = process + "." + taskName.toLowerCase();
		result = result.replaceAll("Ã±", "n");
		result = result.replaceAll("Ã¡", "a");
		result = result.replaceAll("Ã©", "e");
		result = result.replaceAll("Ã­", "i");
		result = result.replaceAll("Ã³", "o");
		result = result.replaceAll("Ãº", "u");
		result = result.replace("\\u00E1", "a");
		result = result.replace("\\u00E9", "e");
		result = result.replace("\\u00ED", "i");
		result = result.replace("\\u00F3", "o");
		result = result.replace("\\u00FA", "u");
		result = result.replaceAll("[Ã Ã¤]","a");
		result = result.replaceAll("[Ã¨Ã«]","e");
		result = result.replaceAll("[Ã¬Ã¯]","i");
		result = result.replaceAll("[Ã²Ã¶]","o");
		result = result.replaceAll("[Ã¹u]","u");
		result = result.replaceAll("[Ã�Ã€Ã„]","a");
		result = result.replaceAll("[Ã‰ÃˆÃ‹]","e");
		result = result.replaceAll("[Ã�ÃŒÃ�]","i");
		result = result.replaceAll("[Ã“Ã’Ã–]","o");
		result = result.replaceAll("[ÃšÃ™Ãœ]","u");
		result = result.replaceAll("&#243;","o");
		result = result.replaceAll("Ã‘","n");
		result = result.replaceAll("Ã§Ã‡","c");
		result = result.replaceAll("['\"~Â°]", "");
		System.out.println("result-->"+result);
		return ViewTask.getByKey(result).getView();
	}

	public static String unicodeEscaped(char ch) {
		String returnStr;
		//String uniTemplate = "\u0000";
		final String charEsc = "\\u";

		if (ch < 0x10) {
			returnStr = "000" + Integer.toHexString(ch);
		}
		else if (ch < 0x100) {
			returnStr = "00" + Integer.toHexString(ch);
		}
		else if (ch < 0x1000) {
			returnStr = "0" + Integer.toHexString(ch);
		}
		else
			returnStr = "" + Integer.toHexString(ch);

		return charEsc + returnStr;
	}
}


