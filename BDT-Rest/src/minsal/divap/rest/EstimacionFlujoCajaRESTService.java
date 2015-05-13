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
	@Path("/estimacionFlujoCaja/calcularPropuesta/{idPrograma}/{ano}/{iniciarFlujoCaja}")
	@Produces("application/json")
	public void calcularPropuesta(@PathParam("idPrograma") Integer idPrograma,@PathParam("ano") Integer ano, @PathParam("iniciarFlujoCaja") Boolean iniciarFlujoCaja) {
		System.out.println("[CALCULAR PROPUESTA idPrograma] -->"	+ idPrograma);
		System.out.println("[CALCULAR PROPUESTA ano] -->"	+ ano);
		System.out.println("[CALCULAR PROPUESTA iniciarFlujoCaja] -->" + iniciarFlujoCaja);
		if(idPrograma == null){
			throw new IllegalArgumentException("idPrograma: "+ idPrograma + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		if(iniciarFlujoCaja == null){
			throw new IllegalArgumentException("iniciarFlujoCaja: "+ iniciarFlujoCaja + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.calcularPropuesta(idPrograma, ano, iniciarFlujoCaja);
		System.out.println("[FIN CALCULAR PROPUESTA]");
	}

	/**Profesional**/
	@GET
	@Path("/estimacionFlujoCaja/generarPlanillaPropuesta/{idPrograma}/{ano}")
	@Produces("application/json")
	public void generarPlanillaPropuesta(@PathParam("idPrograma") Integer idPrograma, @PathParam("ano") Integer ano) {
		System.out.println("[GENERAR PLANILLA PROPUESTA] idPrograma-->" + idPrograma);
		System.out.println("[GENERAR PLANILLA PROPUESTA] ano-->" + ano);
		if(idPrograma == null){
			throw new IllegalArgumentException("idPrograma: "+ idPrograma + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.generarPlanillaPropuesta(idPrograma, ano);
	}

	/**Profesional**/
	@GET
    @Path("/estimacionFlujoCaja/eliminarPlanillaPropuesta/{idPrograma}/{ano}")
    @Produces("application/json")
    public void eliminarPlanillaPropuesta(@PathParam("idPrograma") Integer idPrograma, @PathParam("ano") Integer ano){
		System.out.println("[Eliminar Planilla] idPrograma-->"+idPrograma);
		System.out.println("[Eliminar Planilla] ano-->"+ano);
		if(idPrograma == null){
			throw new IllegalArgumentException("idPrograma: "+ idPrograma + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.eliminarPlanillaPropuesta(idPrograma, ano);
    }

	/**Profesional**/
	@GET
	@Path("/estimacionFlujoCaja/notificarUsuarioConsolidador/{idPrograma}/{ano}/{usuario}")
	@Produces("application/json")
	public void notificarUsuarioConsolidador(@PathParam("idPrograma") Integer idPrograma, @PathParam("ano") Integer ano, @PathParam("usuario") String usuario) {
		System.out.println("[Notificar a Usuario Consolidador] idPrograma-->"	+ idPrograma);
		System.out.println("[Notificar a Usuario Consolidador] ano-->"	+ ano);
		System.out.println("[Notificar a Usuario Consolidador] usuario -->"	+ usuario);
		if(idPrograma == null){
			throw new IllegalArgumentException("idPrograma: "+ idPrograma + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		if(usuario == null){
			throw new IllegalArgumentException("usuario: "+ usuario + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.notificarUsuarioConsolidador(idPrograma, ano, usuario);
	}

	@GET
	@Path("/estimacionFlujoCaja/elaborarOrdinarioProgramacion/{ano}")
	@Produces("application/json")
	public Integer elaborarOrdinarioProgramacion(@PathParam("ano") Integer ano){
			EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		// Elaborar Ordinario Programacion
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		return estimacionFlujoCajaService.elaborarOrdinarioProgramacionCaja(ano);
	}

	@GET
	@Path("/estimacionFlujoCaja/administrarVersiones/{idPrograma}/{ano}")
	@Produces("application/json")
	public Integer administrarVersiones(@PathParam("idPrograma") Integer idPrograma, @PathParam("ano") Integer ano) {
		System.out.println("[Eliminar Planilla] idPrograma-->" + idPrograma);
		System.out.println("[Eliminar Planilla] ano-->" + ano);
		if(idPrograma == null){
			throw new IllegalArgumentException("idPrograma: "+ idPrograma + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		return estimacionFlujoCajaService.eliminarOrdinarioFonasa(idPrograma, ano);
	}

	@GET
	@Path("/estimacionFlujoCaja/enviarOrdinarioFONASA/{idPrograma}/{ano}")
	@Produces("application/json")
	public Integer enviarOrdinarioFONASA(@PathParam("idPrograma") Integer idPrograma, @PathParam("ano") Integer ano) {
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		// Elaborar Ordinario Programacion
		if(idPrograma == null){
			throw new IllegalArgumentException("idPrograma: "+ idPrograma + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		return estimacionFlujoCajaService.enviarOrdinarioFONASA(idPrograma, ano);
	}
	
	/**Consolidador**/
	@GET
    @Path("/estimacionFlujoCaja/instanciarProcesoConsolidador/{usuarioId}/{ano}")
    @Produces("application/json")
    public Integer instanciarProcesoConsolidador(@PathParam("usuarioId") String usuarioId, @PathParam("ano") Integer ano){
		System.out.println("instanciar Proceso Consolidador usuarioId-->"+usuarioId);
		System.out.println("instanciar Proceso Consolidador ano-->"+ano);
		if(usuarioId == null){
			throw new IllegalArgumentException("usuarioId: "+ usuarioId + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		return estimacionFlujoCajaService.crearIntanciaConsolidador(usuarioId, ano);
    }
	
	/**Consolidador**/
	@GET
	@Path("/estimacionFlujoCaja/elaborarOrdinarioProgramacionPlanilla/{idProceso}/{ano}")
    public void elaborarOrdinarioProgramacionPlanilla(@PathParam("idProceso") Integer idProceso, @PathParam("ano") Integer ano){
		System.out.println("elaborarOrdinarioProgramacionPlanilla idProceso -->" + idProceso);
		System.out.println("elaborarOrdinarioProgramacionPlanilla ano -->" + ano);
		if(idProceso == null){
			throw new IllegalArgumentException("idProceso: "+ idProceso + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.elaborarOrdinarioProgramacionPlanilla(idProceso, ano);
    }
	
	/**Consolidador**/
	@GET
	@Path("/estimacionFlujoCaja/administrarVersionesFinalesConsolidador/{idProceso}/{ano}") 
	@Produces("application/json")
	public void administrarVersionesFinalesConsolidador(@PathParam("idProceso") Integer idProceso, @PathParam("ano") Integer ano){
		System.out.println("Proceso estimacionFlujoCaja administrarVersionesFinalesConsolidador idProceso-->"+idProceso);
		System.out.println("Proceso estimacionFlujoCaja administrarVersionesFinalesConsolidador ano-->"+ano);
		if(idProceso == null){
			throw new IllegalArgumentException("Id Proceso: "+ idProceso + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.administrarVersionesFinalesConsolidador(idProceso, ano);
	}
	
	/**Consolidador**/
	@GET
	@Path("/estimacionFlujoCaja/enviarOrdinarioFonasaConsolidador/{idProceso}/{ano}")
	@Produces("application/json")
	public void enviarOrdinarioFonasaConsolidador(@PathParam("idProceso") Integer idProceso, @PathParam("ano") Integer ano){
		System.out.println("Proceso estimacionFlujoCaja enviarOrdinarioFonasaConsolidador idProceso-->"+idProceso);
		System.out.println("Proceso estimacionFlujoCaja enviarOrdinarioFonasaConsolidador ano-->"+ano);
		if(idProceso == null){
			throw new IllegalArgumentException("Id Proceso: "+ idProceso + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.enviarOrdinarioFonasaConsolidador(idProceso, ano);
	}

}
