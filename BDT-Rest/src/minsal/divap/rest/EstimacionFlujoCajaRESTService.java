package minsal.divap.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import minsal.divap.service.EstimacionFlujoCajaService;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * members table.
 */
@Path("/proceso")
@RequestScoped
public class EstimacionFlujoCajaRESTService extends BaseRest {

	@GET
	@Path("/estimacionFlujoCaja/calcularPropuesta/{idProgramaAno}/{iniciarFlujoCaja}")
	@Produces("application/json")
	public void calcularPropuesta(
			@PathParam("idProgramaAno") Integer idProgramaAno,
			@PathParam("iniciarFlujoCaja") Boolean iniciarFlujoCaja) {
		System.out.println("[CALCULAR PROPUESTA idProgramaAno] -->"
				+ idProgramaAno);
		System.out.println("[CALCULAR PROPUESTA iniciarFlujoCaja] -->"
				+ iniciarFlujoCaja);

		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		System.out.println("[FIN CALCULAR PROPUESTA]");
		estimacionFlujoCajaService.calcularPropuesta(idProgramaAno,
				iniciarFlujoCaja);
	}

	@GET
	@Path("/estimacionFlujoCaja/generarPlanillaPropuesta/{idProgramaAno}")
	@Produces("application/json")
	public Integer generarPlanillaPropuesta(
			@PathParam("idProgramaAno") Integer idProgramaAno) {
		System.out.println("[GENERAR PLANILLA PROPUESTA] -->" + idProgramaAno);
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		System.out.println("FIN GENERAR PLANILLA PROPUESTA");
		return estimacionFlujoCajaService
				.generarPlanillaPropuesta(idProgramaAno);
	}

	@GET
    @Path("/estimacionFlujoCaja/eliminarPlanillaPropuesta/{idProgramaAno}")
    @Produces("application/json")
    public void eliminarPlanillaPropuesta(@PathParam("idProgramaAno") Integer idProgramaAno){
		System.out.println("[Eliminar Planilla] -->"+idProgramaAno);
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.eliminarPlanillaPropuesta(idProgramaAno);
    }

	@GET
	@Path("/estimacionFlujoCaja/notificarUsuarioConsolidador/{idLineaProgramatica}/{usuario}")
	@Produces("application/json")
	public void notificarUsuarioConsolidador(
			@PathParam("idLineaProgramatica") Integer idLineaProgramatica,
			@PathParam("usuario") String usuario) {
		System.out.println("[Notificar a Usuario Consolidador] -->"
				+ idLineaProgramatica);
		System.out
				.println("[Notificar a Usuario Consolidador] usuario -->"
						+ usuario);
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.notificarUsuarioConsolidador(
				idLineaProgramatica, usuario);
	}

	@GET
	@Path("/estimacionFlujoCaja/recalcularEstimacion/{flujo}")
	@Produces("application/json")
	public Integer recalcularEstimacion(
			@PathParam("flujo") Integer idLineaProgramatica) {
		System.out.println("[Recalcular Estimacion] -->" + idLineaProgramatica);
		return 1;
	}

	@GET
	@Path("/estimacionFlujoCaja/elaborarOrdinarioProgramacion")
	@Produces("application/json")
	public Integer elaborarOrdinarioProgramacion(){
			EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		// Elaborar Ordinario Programacion
		
		return estimacionFlujoCajaService.elaborarOrdinarioProgramacionCaja();
	}

	@GET
	@Path("/estimacionFlujoCaja/administrarVersiones/{flujo}")
	@Produces("application/json")
	public Integer administrarVersiones(
			@PathParam("flujo") Integer idLineaProgramatica) {
		System.out.println("[Eliminar Planilla] -->" + idLineaProgramatica);

		System.out.println("[Eliminar Planilla] -->" + idLineaProgramatica);
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		return estimacionFlujoCajaService
				.eliminarOrdinarioFonasa(idLineaProgramatica);
		// return 1;
	}

	@GET
	@Path("/estimacionFlujoCaja/enviarOrdinarioFONASA/{flujo}")
	@Produces("application/json")
	public Integer enviarOrdinarioFONASA(
			@PathParam("flujo") Integer idLineaProgramatica) {
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		// Elaborar Ordinario Programacion
		return estimacionFlujoCajaService
				.enviarOrdinarioFONASA(idLineaProgramatica);
	}

}
