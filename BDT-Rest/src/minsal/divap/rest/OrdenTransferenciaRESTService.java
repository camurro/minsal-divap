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

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.OTService;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/proceso")
@RequestScoped
public class OrdenTransferenciaRESTService extends BaseRest{

	@GET
    @Path("/ordenesDeTransferencia/cambiarEstadoPrograma/{idPrograma}/{estado}")
    @Produces("application/json")
    public void cambiarEstadoPrograma(@PathParam("idPrograma") Integer idPrograma, @PathParam("estado") String estado){
		System.out.println("cambiarEstadoPrograma-->"+idPrograma +" estado="+estado);
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(estado == null){
			throw new IllegalArgumentException("estado programa: "+ idPrograma + " no puede ser nulo");
		}
		OTService ordenTransferenciaService = getService(OTService.class);
		EstadosProgramas estadoPrograma = EstadosProgramas.getById(Integer.parseInt(estado));
		ordenTransferenciaService.cambiarEstadoPrograma(idPrograma, estadoPrograma);
    }
	
	@GET
    @Path("/ordenesDeTransferencia/generarOficioOrdenTransferencia/{idProcesoOT}/{ano}")
    @Produces("application/json")
    public void generarOficioOrdenTransferencia(@PathParam("idProcesoOT") String idProcesoOT, @PathParam("ano") Integer ano){
		System.out.println("Generar Oficio Orden de Transferencia");
		if(idProcesoOT == null){
			throw new IllegalArgumentException("idProcesoOT: "+ idProcesoOT + " no puede ser nulo");
		}		
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		OTService ordenTransferenciaService = getService(OTService.class);
		ordenTransferenciaService.pagarOrdenesTransferenciayConvenios();
		Long totalFinal = ordenTransferenciaService.generarExcelFonasaOT(TipoDocumentosProcesos.RESUMENCONSOLIDADOFONASA, idProcesoOT, ano);
		ordenTransferenciaService.generarOficiosTransferencia(TipoDocumentosProcesos.PLANTILLAORDINARIOOREDENTRANSFERENCIA, idProcesoOT, totalFinal, ano);
    }	
	
	@GET
    @Path("/ordenesDeTransferencia/administrarVersionesAlfresco/{idProcesoOT}")
    @Produces("application/json")
    public void administrarVersionesAlfresco(@PathParam("idProcesoOT") Integer idProcesoOT){
		System.out.println("administrarVersionesAlfresco Orden de Transferencia idProcesoOT= "+idProcesoOT);
		OTService ordenTransferenciaService = getService(OTService.class);
		ordenTransferenciaService.administrarVersionesAlfresco(idProcesoOT);
    }	
	
	@GET
    @Path("/ordenesDeTransferencia/instanciarProcesoOT/{usuario}")
    @Produces("application/json")
    public Integer instanciarProcesoOT(@PathParam("usuario") String usuario){
		System.out.println("Generar Instancia OT");
		if(usuario == null){
			throw new IllegalArgumentException("usuarioId: "+ usuario + " no puede ser nulo");
		}
		OTService ordenTransferenciaService = getService(OTService.class);
		return ordenTransferenciaService.crearInstanciaOT(usuario);
    }

	@GET
    @Path("/ordenesDeTransferencia/enviarOrdinarioFonasa/{idProcesoOT}")
    @Produces("application/json")
    public void enviarOrdinarioFonasa(@PathParam("idProcesoOT") String idProcesoOT){
		System.out.println("Enviando documentos OT a FONASA");
		if(idProcesoOT == null){
			throw new IllegalArgumentException("idProcesoOT: "+ idProcesoOT + " no puede ser nulo");
		}
		OTService ordenTransferenciaService = getService(OTService.class);
		ordenTransferenciaService.enviarDocumentosFonasa(idProcesoOT);
    }
	
	@GET
    @Path("/ordenesDeTransferencia/enviarOrdinarioServicioSalud/{idProcesoOT}")
    @Produces("application/json")
    public void enviarOrdinarioServicioSalud(@PathParam("idProcesoOT") String idProcesoOT){
		System.out.println("Enviando documentos OT a ServicioSalud");
		if(idProcesoOT == null){
			throw new IllegalArgumentException("idProcesoOT: "+ idProcesoOT + " no puede ser nulo");
		}
		OTService ordenTransferenciaService = getService(OTService.class);
		ordenTransferenciaService.enviarDocumentosServicioSalud(idProcesoOT);
    }

	@GET
    @Path("/ordenesDeTransferencia/reestablecerProgramas/{estado}")
    @Produces("application/json")
    public void reestablecerProgramas(@PathParam("estado") String estado){
		System.out.println("Reestableciendo estado de los programas para OT");
		if(estado == null){
			throw new IllegalArgumentException("estado: "+ estado + " no puede ser nulo");
		}
		OTService ordenTransferenciaService = getService(OTService.class);
		ordenTransferenciaService.reestablecerProgramas(Integer.parseInt(estado));
    }
			
}
