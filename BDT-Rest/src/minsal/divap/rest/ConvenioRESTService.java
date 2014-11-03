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
import minsal.divap.service.ConveniosService;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/proceso")
@RequestScoped
public class ConvenioRESTService extends BaseRest{
	
	@GET
    @Path("/convenio/cambiarEstadoPrograma/{programaSeleccionado}/{estado}")
    @Produces("application/json")
    public void cambiarEstadoPrograma(@PathParam("programaSeleccionado") Integer programaSeleccionado, @PathParam("estado") String estado){
		System.out.println("Cambiar estado-->"+programaSeleccionado);
		if(programaSeleccionado == null){
			throw new IllegalArgumentException("programaSeleccionado: "+ programaSeleccionado + " no puede ser nulo");
		}
		if(estado == null){
			throw new IllegalArgumentException("estado: "+ estado + " no puede ser nulo");
		}
		ConveniosService conveniosService = getService(ConveniosService.class);
		EstadosProgramas estadoPrograma = EstadosProgramas.getById(Integer.parseInt(estado));
		conveniosService.cambiarEstadoPrograma(programaSeleccionado, estadoPrograma);
    }
	
	@GET
    @Path("/convenio/notificarServicioSalud/{programaSeleccionado}")
    @Produces("application/json")
    public void notificarServicioSalud(@PathParam("programaSeleccionado") Integer programaSeleccionado){
		System.out.println("notificarServicioSalud-->"+programaSeleccionado);
		if(programaSeleccionado == null){
			throw new IllegalArgumentException("programaSeleccionado: "+ programaSeleccionado + " no puede ser nulo");
		}
		ConveniosService conveniosService = getService(ConveniosService.class);
		conveniosService.notificarServicioSalud(programaSeleccionado);
    }
	
	@GET
    @Path("/convenio/generarResolucionDisponibilizarAlfresco/{programaSeleccionado}")
    @Produces("application/json")
    public Integer generarResolucionDisponibilizarAlfresco(@PathParam("programaSeleccionado") Integer programaSeleccionado){
		System.out.println("generarResolucionDisponibilizarAlfresco-->"+programaSeleccionado);
		if(programaSeleccionado == null){
			throw new IllegalArgumentException("programaSeleccionado: "+ programaSeleccionado + " no puede ser nulo");
		}
		ConveniosService conveniosService = getService(ConveniosService.class);
		return conveniosService.generarResolucionDisponibilizarAlfresco(programaSeleccionado);
    }
	
	@GET
    @Path("/convenio/administrarVersionesAlfresco/{programaSeleccionado}")
    @Produces("application/json")
    public void administrarVersionesAlfresco(@PathParam("programaSeleccionado") Integer programaSeleccionado){
		System.out.println("administrarVersionesAlfresco-->"+programaSeleccionado);
		if(programaSeleccionado == null){
			throw new IllegalArgumentException("programaSeleccionado: "+ programaSeleccionado + " no puede ser nulo");
		}
		ConveniosService conveniosService = getService(ConveniosService.class);
		conveniosService.administrarVersionesAlfresco(programaSeleccionado);
    }
	
}
