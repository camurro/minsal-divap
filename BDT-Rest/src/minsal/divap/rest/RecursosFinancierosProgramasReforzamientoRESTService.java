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
import minsal.divap.service.OTService;
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
    @Path("/recursosFinancierosProgramasReforzamiento/cambiarEstadoPrograma/{idPrograma}/{estado}")
    @Produces("application/json")
    public void cambiarEstadoPrograma(@PathParam("idPrograma") Integer idPrograma, @PathParam("estado") String estado){
		System.out.println("cambiarEstadoPrograma-->"+idPrograma +" estado="+estado);
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(estado == null){
			throw new IllegalArgumentException("estado programa: "+ idPrograma + " no puede ser nulo");
		}
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		EstadosProgramas estadoPrograma = EstadosProgramas.getById(Integer.parseInt(estado));
		recursosFinancierosProgramasReforzamientoService.cambiarEstadoPrograma(idPrograma, estadoPrograma);
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/generarPlanillaValorizacion/{idPrograma}")
    @Produces("application/json")
    public void generarPlanillaValorizacion(@PathParam("idPrograma") Integer idPrograma){
		System.out.println("generarPlanillaValorizacion-->"+idPrograma );
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
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
    @Path("/recursosFinancierosProgramasReforzamiento/notificarGeneracionResoluciones/{idPrograma}")
    @Produces("application/json")
    public void notificarGeneracionResoluciones(@PathParam("idPrograma") Integer idPrograma){
		System.out.println("notificarGeneracionResoluciones-->"+idPrograma );
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/administrarVersionesFinales/{idPrograma}")
    @Produces("application/json")
    public void administrarVersionesFinales(@PathParam("idPrograma") Integer idPrograma){
		System.out.println("administrarVersionesFinales-->"+idPrograma );
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/enviarDocumentosServicioSalud/{idPrograma}/{tipoProgramaPxQ}/{idProceso}")
    @Produces("application/json")
    public void enviarDocumentosServicioSalud(@PathParam("idPrograma") Integer idPrograma,@PathParam("tipoProgramaPxQ") Boolean tipoProgramaPxQ,@PathParam("idProceso") Integer idProceso){
		System.out.println("enviarDocumentosServicioSalud-->"+idPrograma );
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		recursosFinancierosProgramasReforzamientoService.recursosFinancierosProgramasReforzamientoService(idPrograma, tipoProgramaPxQ, idProceso);
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/calcularActualDesdeHistorico/{idPrograma}/{tipoHistorico}")
    @Produces("application/json")
	public void calcularActualDesdeHistorico(@PathParam("idPrograma") Integer idPrograma, @PathParam("tipoHistorico") String tipoHistorico){
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		recursosFinancierosProgramasReforzamientoService.calcularActual(idPrograma, tipoHistorico);
	}
	
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/generarOrdinariosDistribucionRecursos/{idPrograma}")
    @Produces("application/json")
    public void generarOrdinariosDistribucionRecursos(@PathParam("idPrograma") Integer idPrograma){
		System.out.println("generarOrdinariosDistribucionRecursos-->"+idPrograma );
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		List<ResumenProgramaMixtoVO> resumen = recursosFinancierosProgramasReforzamientoService.getConsolidadoPrograma(idPrograma);
		recursosFinancierosProgramasReforzamientoService.elaborarOrdinarioProgramaReforzamiento(idPrograma,resumen);
		recursosFinancierosProgramasReforzamientoService.elaborarExcelOrdinario(idPrograma,resumen,TipoDocumentosProcesos.PLANTILLARESOLUCIONPROGRAMASAPS);
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/generarResolucionesDistribucionRecursos/{idPrograma}")
    @Produces("application/json")
    public void generarResolucionesDistribucionRecursos(@PathParam("idPrograma") Integer idPrograma){
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		System.out.println("generarResolucionesDistribucionRecursos-->"+idPrograma );
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		List<ResumenProgramaMixtoVO> resumen = recursosFinancierosProgramasReforzamientoService.getConsolidadoPrograma(idPrograma);
		recursosFinancierosProgramasReforzamientoService.elaborarResolucionProgramaReforzamiento(idPrograma,resumen);
		recursosFinancierosProgramasReforzamientoService.elaborarExcelResolucion(idPrograma,resumen,TipoDocumentosProcesos.PLANTILLARESOLUCIONPROGRAMASAPS);
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
    @Path("/recursosFinancierosProgramasReforzamiento/modificacionGenerarOrdinariosDistribucionRecursos/{idPrograma}/{listaServicios}")
    @Produces("application/json")
    public void modificacionGenerarOrdinariosDistribucionRecursos(@PathParam("idPrograma") Integer idPrograma,@PathParam("listaServicios") String listaServicios){
		System.out.println("generarOrdinariosDistribucionRecursos-->"+idPrograma );

		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		String[] servicios = listaServicios.split(",");
		List<Integer> listaServ = new ArrayList<Integer>();
		for(int i=0; i < servicios.length;i++){
			listaServ.add(Integer.parseInt(servicios[i]));
		}
		List<ResumenProgramaMixtoVO> resumen = recursosFinancierosProgramasReforzamientoService.getConsolidadoProgramaModificado(idPrograma);
		recursosFinancierosProgramasReforzamientoService.elaborarOrdinarioModificacionProgramaReforzamiento(idPrograma,resumen,listaServ);
		recursosFinancierosProgramasReforzamientoService.elaborarExcelOrdinarioModificado(idPrograma,resumen,TipoDocumentosProcesos.MODIFICACIONORDINARIOPROGRAMASAPS);
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/modificacionGenerarResolucionesDistribucionRecursos/{idPrograma}/{listaServicios}")
    @Produces("application/json")
    public void modificacionGenerarResolucionesDistribucionRecursos(@PathParam("idPrograma") Integer idPrograma,@PathParam("listaServicios") String listaServicios){
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		System.out.println("generarResolucionesDistribucionRecursos-->"+idPrograma );
		RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService = getService(RecursosFinancierosProgramasReforzamientoService.class);
		String[] servicios = listaServicios.split(",");
		List<Integer> listaServ = new ArrayList<Integer>();
		for(int i=0; i < servicios.length;i++){
			listaServ.add(Integer.parseInt(servicios[i]));
		}
		
		List<ResumenProgramaMixtoVO> resumen = recursosFinancierosProgramasReforzamientoService.getConsolidadoProgramaModificado(idPrograma);
		recursosFinancierosProgramasReforzamientoService.elaborarResolucionModificacionProgramaReforzamiento(idPrograma,resumen,listaServ);
		recursosFinancierosProgramasReforzamientoService.elaborarExcelResolucionModificado(idPrograma,resumen,TipoDocumentosProcesos.MODIFICACIONRESOLUCIONPROGRAMASAPS);
	
    }
	
	@GET
    @Path("/recursosFinancierosProgramasReforzamiento/enviarDocumentosModificacionServicioSalud/{idPrograma}/{tipoProgramaPxQ}/{listaServicios}/{idProcesoModificacion}")
    @Produces("application/json")
    public void enviarDocumentosModificacionServicioSalud(@PathParam("idPrograma") Integer idPrograma,@PathParam("tipoProgramaPxQ") Boolean tipoProgramaPxQ,@PathParam("listaServicios") String listaServicios,@PathParam("idProcesoModificacion") Integer idProcesoModificacion){
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
		recursosFinancierosProgramasReforzamientoService.recursosFinancierosProgramasReforzamientoModificacionService(idPrograma, tipoProgramaPxQ,listaServ,idProcesoModificacion);
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
