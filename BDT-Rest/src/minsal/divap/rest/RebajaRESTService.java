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

import minsal.divap.service.RebajaService;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/proceso")
@RequestScoped
public class RebajaRESTService extends BaseRest{

	@GET
	@Path("/rebaja/calcularRebaja/{idMes}/{idProceso}/{ano}")
	@Produces("application/json")
	public Integer calcularRebaja(@PathParam("idMes") Integer idMes,@PathParam("idProceso") Integer idProceso, @PathParam("ano") Integer ano){
		System.out.println("Mes de rebaja>"+idMes);
		if(idMes == null){
			throw new IllegalArgumentException("Id Mes: "+ idMes + " no puede ser nulo");
		}
		if(idProceso == null){
			throw new IllegalArgumentException("Id Proceso: "+ idProceso + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("Id ano: "+ ano + " no puede ser nulo");
		}
		RebajaService rebajaService = getService(RebajaService.class);
		return rebajaService.calculaRebajaMes(idMes, idProceso, ano);
	}	
	
	@GET
	@Path("/rebaja/instanciarProcesoRebaja/{usuarioId}/{ano}")
	@Produces("application/json")
	public Integer instanciarProcesoRebaja(@PathParam("usuarioId") String usuarioId, @PathParam("ano") Integer ano){
		System.out.println("instanciar Proceso Rebaja-->"+usuarioId);
		if(usuarioId == null){
			throw new IllegalArgumentException("usuarioId: "+ usuarioId + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("Id ano: "+ ano + " no puede ser nulo");
		}
		RebajaService rebajaService = getService(RebajaService.class);
		return rebajaService.crearIntanciaRebaja(usuarioId, ano);
	}
	
	@GET
	@Path("/rebaja/elaborarResolucionRebaja/{idProceso}/{ano}") 
	@Produces("application/json")
	public Integer elaborarResolucionRebaja(@PathParam("idProceso") Integer idProceso, @PathParam("ano") Integer ano){
		System.out.println("Proceso Rebaja elaborarResolucionRebaja-->"+idProceso);
		if(idProceso == null){
			throw new IllegalArgumentException("Id Proceso: "+ idProceso + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("Id ano: "+ ano + " no puede ser nulo");
		}
		RebajaService rebajaService = getService(RebajaService.class);
		return rebajaService.elaborarResolucionRebaja(idProceso, ano);
	}
	
	@GET
	@Path("/rebaja/administrarVersionesFinalesAlfresco/{idProceso}/{ano}") 
	@Produces("application/json")
	public void administrarVersionesFinalesAlfresco(@PathParam("idProceso") Integer idProceso, @PathParam("ano") Integer ano){
		System.out.println("Proceso Rebaja administrarVersionesFinalesAlfresco-->"+idProceso);
		if(idProceso == null){
			throw new IllegalArgumentException("Id Proceso: "+ idProceso + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("Id ano: "+ ano + " no puede ser nulo");
		}
		RebajaService rebajaService = getService(RebajaService.class);
		rebajaService.administrarVersionesFinalesAlfresco(idProceso, ano);
	}
	
	@GET
	@Path("/rebaja/enviarResolucionesServicioSalud/{idProceso}/{ano}")
	@Produces("application/json")
	public void enviarResolucionesServicioSalud(@PathParam("idProceso") Integer idProceso, @PathParam("ano") Integer ano){
		System.out.println("Proceso Rebaja enviarResolucionesServicioSalud-->"+idProceso);
		if(idProceso == null){
			throw new IllegalArgumentException("Id Proceso: "+ idProceso + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("Id ano: "+ ano + " no puede ser nulo");
		}
		RebajaService rebajaService = getService(RebajaService.class);
		rebajaService.enviarResolucionesServicioSalud(idProceso, ano);
	}

}
