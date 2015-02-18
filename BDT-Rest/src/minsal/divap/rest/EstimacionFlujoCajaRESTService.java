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

	/**Profesional**/
	@GET
	@Path("/estimacionFlujoCaja/calcularPropuesta/{idProgramaAno}/{iniciarFlujoCaja}")
	@Produces("application/json")
	public void calcularPropuesta(
			@PathParam("idProgramaAno") Integer idProgramaAno,
			@PathParam("iniciarFlujoCaja") Boolean iniciarFlujoCaja) {
		System.out.println("[CALCULAR PROPUESTA idProgramaAno] -->"	+ idProgramaAno);
		System.out.println("[CALCULAR PROPUESTA iniciarFlujoCaja] -->" + iniciarFlujoCaja);
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.calcularPropuesta(idProgramaAno, iniciarFlujoCaja);
		System.out.println("[FIN CALCULAR PROPUESTA]");
	}

	/**Profesional**/
	@GET
	@Path("/estimacionFlujoCaja/generarPlanillaPropuesta/{idProgramaAno}")
	@Produces("application/json")
	public void generarPlanillaPropuesta(
			@PathParam("idProgramaAno") Integer idProgramaAno) {
		System.out.println("[GENERAR PLANILLA PROPUESTA] -->" + idProgramaAno);
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.generarPlanillaPropuesta(idProgramaAno);
	}

	/**Profesional**/
	@GET
    @Path("/estimacionFlujoCaja/eliminarPlanillaPropuesta/{idProgramaAno}")
    @Produces("application/json")
    public void eliminarPlanillaPropuesta(@PathParam("idProgramaAno") Integer idProgramaAno){
		System.out.println("[Eliminar Planilla] -->"+idProgramaAno);
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.eliminarPlanillaPropuesta(idProgramaAno);
    }

	/**Profesional**/
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
	
	/**Consolidador**/
	@GET
    @Path("/estimacionFlujoCaja/instanciarProcesoConsolidador/{usuarioId}")
    @Produces("application/json")
    public Integer instanciarProcesoConsolidador(@PathParam("usuarioId") String usuarioId){
		System.out.println("instanciar Proceso Consolidador-->"+usuarioId);
		if(usuarioId == null){
			throw new IllegalArgumentException("usuarioId: "+ usuarioId + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		return estimacionFlujoCajaService.crearIntanciaConsolidador(usuarioId);
    }
	
	/**Consolidador**/
	@GET
	@Path("/estimacionFlujoCaja/elaborarOrdinarioProgramacionPlanilla/{idProceso}")
    public void elaborarOrdinarioProgramacionPlanilla(@PathParam("idProceso") Integer idProceso){
		System.out.println("elaborarOrdinarioProgramacionPlanilla -->" + idProceso);
		if(idProceso == null){
			throw new IllegalArgumentException("idProceso: "+ idProceso + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.elaborarOrdinarioProgramacionPlanilla(idProceso);
    }
	
	/**Consolidador**/
	@GET
	@Path("/estimacionFlujoCaja/administrarVersionesFinalesConsolidador/{idProceso}") 
	@Produces("application/json")
	public void administrarVersionesFinalesConsolidador(@PathParam("idProceso") Integer idProceso){
		System.out.println("Proceso estimacionFlujoCaja administrarVersionesFinalesConsolidador-->"+idProceso);
		if(idProceso == null){
			throw new IllegalArgumentException("Id Proceso: "+ idProceso + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.administrarVersionesFinalesConsolidador(idProceso);
	}
	
	/**Consolidador**/
	@GET
	@Path("/estimacionFlujoCaja/enviarOrdinarioFonasaConsolidador/{idProceso}")
	@Produces("application/json")
	public void enviarOrdinarioFonasaConsolidador(@PathParam("idProceso") Integer idProceso){
		System.out.println("Proceso estimacionFlujoCaja enviarOrdinarioFonasaConsolidador-->"+idProceso);
		if(idProceso == null){
			throw new IllegalArgumentException("Id Proceso: "+ idProceso + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.enviarOrdinarioFonasaConsolidador(idProceso);
	}

}
