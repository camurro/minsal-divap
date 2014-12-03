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
	@Path("/convenio/crearInstanciaConvenio/{usuario}")
	@Produces("application/json")
	public Integer crearInstanciaConvenio(@PathParam("usuario") String usuario){
		System.out.println("crearInstanciaReliquidacion con usuario->"+usuario);
		if(usuario == null){
			throw new IllegalArgumentException("usuario: "+ usuario + " no puede ser nulo");
		}
		ConveniosService conveniosService = getService(ConveniosService.class);
		return conveniosService.crearInstanciaConvenio(usuario);
	}

	@GET
	@Path("/convenio/cambiarEstadoPrograma/{programaSeleccionado}/{estado}")
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
	@Path("/convenio/convenioPorPrograma/{programaSeleccionado}/{idConvenio}")
	public void convenioPorPrograma(@PathParam("programaSeleccionado") Integer programaSeleccionado, @PathParam("idConvenio") Integer idConvenio){
		System.out.println("Cambiar estado-->"+programaSeleccionado);
		if(programaSeleccionado == null){
			throw new IllegalArgumentException("programaSeleccionado: "+ programaSeleccionado + " no puede ser nulo");
		}
		if(idConvenio == null){
			throw new IllegalArgumentException("idConvenio: "+ idConvenio + " no puede ser nulo");
		}
		ConveniosService conveniosService = getService(ConveniosService.class);
		conveniosService.convenioPorPrograma(programaSeleccionado, idConvenio);
	}	

	@GET
	@Path("/convenio/notificarServicioSalud/{programaSeleccionado}/{servicioSeleccionado}/{idConvenio}")
	public void notificarServicioSalud(@PathParam("programaSeleccionado") Integer programaSeleccionado, @PathParam("servicioSeleccionado") Integer servicioSeleccionado, @PathParam("idConvenio") Integer idConvenio){
		System.out.println("notificarServicioSalud programaSeleccionado-->"+programaSeleccionado);
		System.out.println("notificarServicioSalud servicioSeleccionado-->"+servicioSeleccionado);
		System.out.println("notificarServicioSalud idConvenio-->"+idConvenio);
		if(programaSeleccionado == null){
			throw new IllegalArgumentException("programaSeleccionado: "+ programaSeleccionado + " no puede ser nulo");
		}
		if(servicioSeleccionado == null){
			throw new IllegalArgumentException("servicioSeleccionado: "+ servicioSeleccionado + " no puede ser nulo");
		}
		if(idConvenio == null){
			throw new IllegalArgumentException("idConvenio: "+ idConvenio + " no puede ser nulo");
		}
		ConveniosService conveniosService = getService(ConveniosService.class);
		conveniosService.notificarServicioSalud(programaSeleccionado, servicioSeleccionado, idConvenio);
	}

	@GET
	@Path("/convenio/generarResolucionDisponibilizarAlfresco/{programaSeleccionado}/{idConvenio}")
	public Integer generarResolucionDisponibilizarAlfresco(@PathParam("programaSeleccionado") Integer programaSeleccionado, @PathParam("idConvenio") Integer idConvenio){
		System.out.println("generarResolucionDisponibilizarAlfresco-->"+programaSeleccionado);
		if(programaSeleccionado == null){
			throw new IllegalArgumentException("programaSeleccionado: "+ programaSeleccionado + " no puede ser nulo");
		}
		if(idConvenio == null){
			throw new IllegalArgumentException("idConvenio: "+ idConvenio + " no puede ser nulo");
		}
		ConveniosService conveniosService = getService(ConveniosService.class);
		return conveniosService.generarResolucionDisponibilizarAlfresco(programaSeleccionado, idConvenio);
	}

	@GET
	@Path("/convenio/administrarVersionesAlfresco/{programaSeleccionado}/{idConvenio}")
	public void administrarVersionesAlfresco(@PathParam("programaSeleccionado") Integer programaSeleccionado, @PathParam("idConvenio") Integer idConvenio){
		System.out.println("administrarVersionesAlfresco-->"+programaSeleccionado);
		if(programaSeleccionado == null){
			throw new IllegalArgumentException("programaSeleccionado: "+ programaSeleccionado + " no puede ser nulo");
		}
		if(idConvenio == null){
			throw new IllegalArgumentException("idConvenio: "+ idConvenio + " no puede ser nulo");
		}
		ConveniosService conveniosService = getService(ConveniosService.class);
		conveniosService.administrarVersionesAlfresco(programaSeleccionado, idConvenio);
	}
	
	@GET
	@Path("/convenio/notificarPorCorreo/{programaSeleccionado}/{idConvenio}")
	public void notificarPorCorreo(@PathParam("programaSeleccionado") Integer programaSeleccionado, @PathParam("idConvenio") Integer idConvenio){
		System.out.println("notificarPorCorreo-->"+programaSeleccionado);
		if(programaSeleccionado == null){
			throw new IllegalArgumentException("programaSeleccionado: "+ programaSeleccionado + " no puede ser nulo");
		}
		if(idConvenio == null){
			throw new IllegalArgumentException("idConvenio: "+ idConvenio + " no puede ser nulo");
		}
		ConveniosService conveniosService = getService(ConveniosService.class);
		conveniosService.notificarPorCorreo(programaSeleccionado, idConvenio);
	}

}
