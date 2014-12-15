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

import minsal.divap.service.ModificacionDistribucionInicialPercapitaService;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/proceso")
@RequestScoped
public class ModificacionDistribucionInicialPerCapitaRESTService extends BaseRest{
	@GET
	@Path("/modificacionDistribucionInicialPerCapita/valorizarPlanSalud/{idDistribucionInicialPercapita}")
	@Produces("application/json")
	public Integer valorizarPlanSalud(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("valorizar Plan Salud-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		return modificacionDistribucionInicialPercapitaService.valorizarDisponibilizarPlanillaTrabajo(idDistribucionInicialPercapita);
	}


	@GET
	@Path("/modificacionDistribucionInicialPerCapita/subirAlfresco/{docId}")
	@Produces("application/json")
	public Integer subirAlfresco(@PathParam("docId") Integer docId){
		System.out.println("documento a subir-->"+docId);
		if(docId == null){
			throw new IllegalArgumentException("documento: "+ docId + " no puede ser nulo");
		}
		System.out.println("valorizarPlanSalud");
		return 1;
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/elaborarOficioConsulta/{idDistribucionInicialPercapita}")
	@Produces("application/json")
	public Integer elaborarOficioConsulta(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("elaborar oficio proceso-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		return modificacionDistribucionInicialPercapitaService.crearOficioConsulta(idDistribucionInicialPercapita);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/elaborarBorradorDecretoAporteEstatal/{idDistribucionInicialPercapita}")
	@Produces("application/json")
	public Integer elaborarBorradorDecretoAporteEstatal(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("elaborar Borrador Decreto Aporte Estatal-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		return modificacionDistribucionInicialPercapitaService.elaborarBorradorDecretoAporteEstatal(idDistribucionInicialPercapita);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/enviarDecretoAporteEstatal/{borradorAporteEstatalId}/{usuario}")
	public void enviarDecretoAporteEstatal(@PathParam("borradorAporteEstatalId") Integer borradorAporteEstatalId, @PathParam("usuario") String usuario){
		System.out.println("enviar Decreto Aporte Estatal-->"+borradorAporteEstatalId);
		if(borradorAporteEstatalId == null){
			throw new IllegalArgumentException("el documento  borradorAporteEstata: "+ borradorAporteEstatalId + " no puede ser nulo");
		}
		if(usuario == null){
			throw new IllegalArgumentException("el usuario : "+ usuario + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.enviarDecretoAporteEstatal(borradorAporteEstatalId, usuario);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/elaborarDocumentoFormal/{idDistribucionInicialPercapita}")
	public void elaborarDocumentoFormal(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("elaborar Documento Formal-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.elaborarDocumentoFormal(idDistribucionInicialPercapita);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/notificarFinElaboracionResolucion/{idDistribucionInicialPercapita}/{usuario}")
	public void notificarFinElaboracionResolucion(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("usuario") String usuario){
		System.out.println("Notificar Fin Elaboracion Resolucion-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(usuario == null){
			throw new IllegalArgumentException("el usuario : "+ usuario + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.notificarFinElaboracionResolucion(idDistribucionInicialPercapita, usuario);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/administrarVersionesFinalesAlfresco/{idDistribucionInicialPercapita}")
	@Produces("application/json")
	public void administrarVersionesFinalesAlfresco(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("administrar Versiones Finales Alfresco-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.administrarVersionesFinalesAlfresco(idDistribucionInicialPercapita);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/enviarResolucionesServicioSalud/{idDistribucionInicialPercapita}")
	@Produces("application/json")
	public void enviarResolucionesServicioSalud(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("enviar Resoluciones Servicio Salud-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.enviarResolucionesServicioSalud(idDistribucionInicialPercapita);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/enviarConsultaRegional/{idDistribucionInicialPercapita}/{oficioConsultaId}")
	@Produces("application/json")
	public void enviarConsultaRegional(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("oficioConsultaId") Integer oficioConsultaId){
		System.out.println("enviar Consulta Regional-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(oficioConsultaId == null){
			throw new IllegalArgumentException("oficio consulta proceso percapita: "+ oficioConsultaId + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.enviarConsultaRegional(idDistribucionInicialPercapita, oficioConsultaId);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/instanciarProcesoModificacionInicialPerCapita/{usuarioId}")
	@Produces("application/json")
	public Integer instanciarProcesoModificacionInicialPerCapita(@PathParam("usuarioId") String usuarioId){
		System.out.println("instanciar Proceso Inicial PerCapita-->"+usuarioId);
		if(usuarioId == null){
			throw new IllegalArgumentException("usuarioId: "+ usuarioId + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		return modificacionDistribucionInicialPercapitaService.crearIntanciaModificacionDistribucionInicialPercapita(usuarioId);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/generarOrdinarioSolicitudAntecedentes/{idDistribucionInicialPercapita}")
	public void generarOrdinarioSolicitudAntecedentes(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("generarOrdinarioSolicitudAntecedentes-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("usuarioId: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.generarOrdinarioSolicitudAntecedentes(idDistribucionInicialPercapita);
	}
	
	@GET
	@Path("/modificacionDistribucionInicialPerCapita/enviarOrdinarioSolicitudAntecedentes/{idDistribucionInicialPercapita}")
	public void enviarOrdinarioSolicitudAntecedentes(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("enviarOrdinarioSolicitudAntecedentes-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("usuarioId: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.enviarOrdinarioSolicitudAntecedentes(idDistribucionInicialPercapita);
	}

}
