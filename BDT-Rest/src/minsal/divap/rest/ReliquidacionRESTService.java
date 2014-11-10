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
import minsal.divap.service.DistribucionInicialPercapitaService;
import minsal.divap.service.ReliquidacionService;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/proceso")
@RequestScoped
public class ReliquidacionRESTService extends BaseRest{

	@GET
    @Path("/reliquidacion/elaborarDocumentoFormal/{idDistribucionInicialPercapita}")
    public void elaborarDocumentoFormal(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("elaborar Documento Formal-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		distribucionInicialPercapitaService.elaborarDocumentoFormal(idDistribucionInicialPercapita);
    }
	
	@GET
    @Path("/reliquidacion/cambiarEstadoPrograma/{idPrograma}/{estado}")
    @Produces("application/json")
    public void cambiarEstadoPrograma(@PathParam("idPrograma") Integer idPrograma, @PathParam("estado") String estado){
		System.out.println("cambiarEstadoPrograma-->"+idPrograma +" estado="+estado);
		if(idPrograma == null){
			throw new IllegalArgumentException("id del programa no puede ser nulo");
		}
		if(estado == null){
			throw new IllegalArgumentException("estado programa: "+ idPrograma + " no puede ser nulo");
		}
		ReliquidacionService reliquidacionService = getService(ReliquidacionService.class);
		EstadosProgramas estadoPrograma = EstadosProgramas.getById(Integer.parseInt(estado));
		reliquidacionService.cambiarEstadoPrograma(idPrograma, estadoPrograma);
    }
	
	@GET
    @Path("/reliquidacion/crearInstanciaReliquidacion/{usuario}")
	@Produces("application/json")
    public Integer crearInstanciaReliquidacion(@PathParam("usuario") String usuario){
		System.out.println("crearInstanciaReliquidacion con usuario->"+usuario);
		if(usuario == null){
			throw new IllegalArgumentException("usuario: "+ usuario + " no puede ser nulo");
		}
		ReliquidacionService reliquidacionService = getService(ReliquidacionService.class);
		return reliquidacionService.crearInstanciaReliquidacion(usuario);
    }
	
	@GET
    @Path("/reliquidacion/calcularReliquidacion/{idProgramaAno}/{idReliquidacion}")
	@Produces("application/json")
    public String calcularReliquidacion(@PathParam("idProgramaAno") Integer idProgramaAno, @PathParam("idReliquidacion") Integer idReliquidacion){
		System.out.println("llega al REST de valorizarMontos -->"+idProgramaAno+"  --> "+idReliquidacion);
		if(idProgramaAno == null){
			throw new IllegalArgumentException("proceso: "+ idProgramaAno + " no puede ser nulo");
		}
		ReliquidacionService reliquidacionService = getService(ReliquidacionService.class);
		return reliquidacionService.valorizarMontosReliquidacion(idProgramaAno, idReliquidacion);
    }

	
	@GET
    @Path("/reliquidacion/elaborarPlantillasBaseReliquidacion/{idProgramaAno}/{idReliquidacion}")
	@Produces("application/json")
    public String elaborarPlantillasBaseReliquidacion(@PathParam("idProgramaAno") Integer idProgramaAno, @PathParam("idReliquidacion") Integer idReliquidacion){
		System.out.println("elaborar PlanillasBaseReliquidacion -->"+idProgramaAno);
		if(idProgramaAno == null){
			throw new IllegalArgumentException("proceso: "+ idProgramaAno + " no puede ser nulo");
		}
		if(idReliquidacion == null){
			throw new IllegalArgumentException("proceso: "+ idReliquidacion + " no puede ser nulo");
		}
		ReliquidacionService reliquidacionService = getService(ReliquidacionService.class);
		return reliquidacionService.elaborarPlantillasBaseReliquidacion(idProgramaAno, idReliquidacion);
    }
		
}
