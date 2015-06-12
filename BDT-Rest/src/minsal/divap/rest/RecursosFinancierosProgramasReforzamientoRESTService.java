/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package minsal.divap.rest;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.ResumenProgramaMixtoVO;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/proceso")
@RequestScoped
public class RecursosFinancierosProgramasReforzamientoRESTService extends BaseRest{

	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/instanciarProcesoDistribucion/{usuario}")
    @Produces("application/json")
    public Integer instanciarProcesoDistribucion(@PathParam("usuario") String usuario){
		System.out.println("Generar Instancia OT");
	
		if(usuario == null){
			throw new IllegalArgumentException("usuarioId: "+ usuario + " no puede ser nulo");
		}
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		return recursosFinancierosProgramasReforzamientoService.crearInstanciaDistribucion(usuario);
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/cambiarEstadoPrograma/{idPrograma}/{ano}/{estado}")
    @Produces("application/json")
    public void cambiarEstadoPrograma(@PathParam("idPrograma") Integer idPrograma, @PathParam("ano") Integer ano, @PathParam("estado") String estado){
		System.out.println("cambiarEstadoPrograma-->"+idPrograma +" estado="+estado);
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano del programa no puede ser nulo");
		}
		if(estado == null){
			throw new IllegalArgumentException("estado programa: "+ idPrograma + " no puede ser nulo");
		}
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		EstadosProgramas estadoPrograma = EstadosProgramas.getById(Integer.parseInt(estado));
		recursosFinancierosProgramasReforzamientoService.cambiarEstadoPrograma(idPrograma, ano, estadoPrograma);
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/generarPlanillaValorizacion/{idPrograma}/{ano}")
    @Produces("application/json")
    public void generarPlanillaValorizacion(@PathParam("idPrograma") Integer idPrograma, @PathParam("ano") Integer ano){
		System.out.println("generarPlanillaValorizacion-->"+idPrograma );
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano no puede ser nulo");
		}
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/valorizarMontosGenerarPlanillaTrabajo/{idPrograma}")
    @Produces("application/json")
    public void valorizarMontosGenerarPlanillaTrabajo(@PathParam("idPrograma") Integer idPrograma){
		System.out.println("valorizarMontosGenerarPlanillaTrabajo-->"+idPrograma );
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
    }
	
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/notificarGeneracionResoluciones/{idPrograma}/{ano}/{usuario}")
    @Produces("application/json")
    public void notificarGeneracionResoluciones(@PathParam("idPrograma") Integer idPrograma, @PathParam("ano") Integer ano, @PathParam("usuario") String usuario){
		System.out.println("notificarGeneracionResoluciones-->"+idPrograma );
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano no puede ser nulo");
		}
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		recursosFinancierosProgramasReforzamientoService.notificarGeneracionResoluciones(idPrograma, ano, usuario);
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/administrarVersionesFinales/{idPrograma}/{ano}/{idProceso}")
    @Produces("application/json")
    public void administrarVersionesFinales(@PathParam("idPrograma") Integer idPrograma, @PathParam("ano") Integer ano, @PathParam("idProceso") Integer idProceso){
		System.out.println("administrarVersionesFinales-->"+idPrograma );
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano no puede ser nulo");
		}
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		recursosFinancierosProgramasReforzamientoService.administrarVersionesFinales(idPrograma, ano, idProceso);
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/enviarDocumentosServicioSalud/{idPrograma}/{tipoProgramaPxQ}/{idProceso}/{ano}")
    @Produces("application/json")
    public void enviarDocumentosServicioSalud(@PathParam("idPrograma") Integer idPrograma,@PathParam("tipoProgramaPxQ") Boolean tipoProgramaPxQ, @PathParam("idProceso") Integer idProceso, @PathParam("ano") Integer ano){
		System.out.println("enviarDocumentosServicioSalud-->"+idPrograma );
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(tipoProgramaPxQ == null){
			throw new IllegalArgumentException("tipoProgramaPxQ no puede ser nulo");
		}
		if(idProceso == null){
			throw new IllegalArgumentException("idProceso no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano no puede ser nulo");
		}
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		recursosFinancierosProgramasReforzamientoService.recursosFinancierosProgramasReforzamientoService(idPrograma, tipoProgramaPxQ, idProceso, ano);
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/calcularActualDesdeHistorico/{idPrograma}/{tipoHistorico}/{ano}")
    @Produces("application/json")
	public void calcularActualDesdeHistorico(@PathParam("idPrograma") Integer idPrograma, @PathParam("tipoHistorico") String tipoHistorico, @PathParam("ano") Integer ano){
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		recursosFinancierosProgramasReforzamientoService.calcularActual(idPrograma, tipoHistorico, ano);
	}
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/generarOrdinariosDistribucionRecursos/{idPrograma}/{ano}/{idProceso}")
    @Produces("application/json")
    public void generarOrdinariosDistribucionRecursos(@PathParam("idPrograma") Integer idPrograma, @PathParam("ano") Integer ano, @PathParam("idProceso") Integer idProceso){
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano no puede ser nulo");
		}
		if(idProceso == null){
			throw new IllegalArgumentException("idProceso no puede ser nulo");
		}
		System.out.println("generarOrdinariosDistribucionRecursos-->"+idPrograma + " ano=" + ano + " idProceso=" + idProceso);
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		List<ResumenProgramaMixtoVO> resumen = recursosFinancierosProgramasReforzamientoService.getConsolidadoPrograma(idPrograma, ano);
		recursosFinancierosProgramasReforzamientoService.elaborarOrdinarioProgramaReforzamiento(idProceso, idPrograma, resumen, ano);
		recursosFinancierosProgramasReforzamientoService.elaborarExcelOrdinario(idProceso, idPrograma, resumen, TipoDocumentosProcesos.PLANTILLARESOLUCIONPROGRAMASAPS, ano);
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/generarResolucionesDistribucionRecursos/{idPrograma}/{ano}/{idProceso}")
    @Produces("application/json")
    public void generarResolucionesDistribucionRecursos(@PathParam("idPrograma") Integer idPrograma, @PathParam("ano") Integer ano, @PathParam("idProceso") Integer idProceso){
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano no puede ser nulo");
		}
		if(idProceso == null){
			throw new IllegalArgumentException("idProceso no puede ser nulo");
		}
		System.out.println("generarResolucionesDistribucionRecursos-->" + idPrograma + " ano=" + ano);
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		List<ResumenProgramaMixtoVO> resumen = recursosFinancierosProgramasReforzamientoService.getConsolidadoPrograma(idPrograma, ano);
		recursosFinancierosProgramasReforzamientoService.elaborarResolucionProgramaReforzamiento(idProceso, idPrograma, resumen, ano);
		recursosFinancierosProgramasReforzamientoService.elaborarExcelResolucion(idProceso, idPrograma, resumen, TipoDocumentosProcesos.PLANTILLARESOLUCIONPROGRAMASAPS, ano);
    }
	
	// Métodos flujo de modificación de asignación de recursos para programas APS

	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/modificacionCambiarEstadoPrograma/{idPrograma}/{estado}")
    @Produces("application/json")
    public void modificacionCambiarEstadoPrograma(@PathParam("idPrograma") Integer idPrograma, @PathParam("estado") String estado){
		System.out.println("modificacionCambiarEstadoPrograma-->"+idPrograma +" estadoMod="+estado);
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(estado == null){
			throw new IllegalArgumentException("estado programa: "+ idPrograma + " no puede ser nulo");
		}
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		EstadosProgramas estadoPrograma = EstadosProgramas.getById(Integer.parseInt(estado));
		recursosFinancierosProgramasReforzamientoService.modificacionCambiarEstadoPrograma(idPrograma, estadoPrograma);
    }
	
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/modificacionGenerarOrdinariosDistribucionRecursos/{idPrograma}/{listaServicios}/{ano}/{idProceso}")
    @Produces("application/json")
    public void modificacionGenerarOrdinariosDistribucionRecursos(@PathParam("idPrograma") Integer idPrograma, @PathParam("listaServicios") String listaServicios,
    		@PathParam("ano") Integer ano, @PathParam("idProceso") Integer idProceso){
		System.out.println("generarOrdinariosDistribucionRecursos idPrograma=" + idPrograma + " listaServicios=" + listaServicios + " ano=" + ano + " idProceso="+idProceso);
		
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(listaServicios == null){
			throw new IllegalArgumentException("listaServicio no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano no puede ser nulo");
		}
		if(idProceso == null){
			throw new IllegalArgumentException("idProceso no puede ser nulo");
		}
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		String[] servicios = listaServicios.split(",");
		List<Integer> listaServ = new ArrayList<Integer>();
		for(int i=0; i < servicios.length;i++){
			listaServ.add(Integer.parseInt(servicios[i]));
		}
		List<ResumenProgramaMixtoVO> resumen = recursosFinancierosProgramasReforzamientoService.getConsolidadoProgramaModificado(idPrograma, ano);
		recursosFinancierosProgramasReforzamientoService.elaborarOrdinarioModificacionProgramaReforzamiento(idProceso, idPrograma, resumen, listaServ, ano);
		recursosFinancierosProgramasReforzamientoService.elaborarExcelOrdinarioModificado(idProceso, idPrograma, resumen, TipoDocumentosProcesos.MODIFICACIONORDINARIOPROGRAMASAPS, ano);
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/modificacionGenerarResolucionesDistribucionRecursos/{idPrograma}/{listaServicios}/{ano}/{idProceso}")
    @Produces("application/json")
    public void modificacionGenerarResolucionesDistribucionRecursos(@PathParam("idPrograma") Integer idPrograma,@PathParam("listaServicios") String listaServicios,
    		@PathParam("ano") Integer ano, @PathParam("idProceso") Integer idProceso){
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(listaServicios == null){
			throw new IllegalArgumentException("listaServicio no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano no puede ser nulo");
		}
		if(idProceso == null){
			throw new IllegalArgumentException("idProceso no puede ser nulo");
		}
		System.out.println("generarResolucionesDistribucionRecursos-->"+idPrograma );
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		String[] servicios = listaServicios.split(",");
		List<Integer> listaServ = new ArrayList<Integer>();
		for(int i=0; i < servicios.length;i++){
			listaServ.add(Integer.parseInt(servicios[i]));
		}
		List<ResumenProgramaMixtoVO> resumen = recursosFinancierosProgramasReforzamientoService.getConsolidadoProgramaModificado(idPrograma, ano);
		recursosFinancierosProgramasReforzamientoService.elaborarResolucionModificacionProgramaReforzamiento(idProceso, idPrograma, resumen, listaServ, ano);
		recursosFinancierosProgramasReforzamientoService.elaborarExcelResolucionModificado(idProceso, idPrograma, resumen, TipoDocumentosProcesos.MODIFICACIONRESOLUCIONPROGRAMASAPS, ano);
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/enviarDocumentosModificacionServicioSalud/{idPrograma}/{tipoProgramaPxQ}/{listaServicios}/{idProcesoModificacion}/{ano}")
    @Produces("application/json")
    public void enviarDocumentosModificacionServicioSalud(@PathParam("idPrograma") Integer idPrograma,@PathParam("tipoProgramaPxQ") Boolean tipoProgramaPxQ, 
    		@PathParam("listaServicios") String listaServicios, @PathParam("idProcesoModificacion") Integer idProcesoModificacion, @PathParam("ano") Integer ano){
		System.out.println("enviarDocumentosServicioSalud-->"+idPrograma );
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		String[] servicios = listaServicios.split(",");
		List<Integer> listaServ = new ArrayList<Integer>();
		for(int i=0; i < servicios.length;i++){
			listaServ.add(Integer.parseInt(servicios[i]));
		}
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		recursosFinancierosProgramasReforzamientoService.recursosFinancierosProgramasReforzamientoModificacionService(idPrograma, tipoProgramaPxQ, listaServ, idProcesoModificacion, ano);
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/instanciarProcesoModificacion/{usuario}")
    @Produces("application/json")
    public Integer instanciarProcesoModificacion(@PathParam("usuario") String usuario){
		System.out.println("Generar Instancia OT");
	
		if(usuario == null){
			throw new IllegalArgumentException("usuarioId: "+ usuario + " no puede ser nulo");
		}
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		return recursosFinancierosProgramasReforzamientoService.crearInstanciaModificacion(usuario);
    }
	
}
