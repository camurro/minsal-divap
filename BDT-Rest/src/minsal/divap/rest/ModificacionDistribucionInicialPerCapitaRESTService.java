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
	@Path("/modificacionDistribucionInicialPerCapita/valorizarPlanSalud/{idDistribucionInicialPercapita}/{ano}")
	@Produces("application/json")
	public Integer valorizarPlanSalud(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("valorizar Plan Salud idDistribucionInicialPercapita-->" + idDistribucionInicialPercapita);
		System.out.println("valorizar Plan Salud ano-->" + ano);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		return modificacionDistribucionInicialPercapitaService.valorizarDisponibilizarPlanillaTrabajo(idDistribucionInicialPercapita, ano);
	}
	
	@GET
	@Path("/modificacionDistribucionInicialPerCapita/habilitarModificacionPercapita/{idDistribucionInicialPercapita}/{ano}")
	@Produces("application/json")
	public void habilitarModificacionPercapita(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("habilitarModificacionPercapita idDistribucionInicialPercapita-->" + idDistribucionInicialPercapita);
		System.out.println("habilitarModificacionPercapita ano-->" + ano);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.habilitarModificacionPercapita(idDistribucionInicialPercapita, ano);
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
	@Path("/modificacionDistribucionInicialPerCapita/elaborarOficioConsulta/{idDistribucionInicialPercapita}/{ano}")
	@Produces("application/json")
	public Integer elaborarOficioConsulta(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("elaborar oficio proceso idDistribucionInicialPercapita-->"+idDistribucionInicialPercapita);
		System.out.println("elaborar oficio proceso ano-->"+ano);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("proceso ano: "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		return modificacionDistribucionInicialPercapitaService.crearOficioConsulta(idDistribucionInicialPercapita, ano);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/elaborarBorradorDecretoAporteEstatal/{idDistribucionInicialPercapita}/{ano}")
	@Produces("application/json")
	public Integer elaborarBorradorDecretoAporteEstatal(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("elaborar Borrador Decreto Aporte Estatal idDistribucionInicialPercapita-->"+idDistribucionInicialPercapita);
		System.out.println("elaborar Borrador Decreto Aporte Estatal ano-->"+ano);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		return modificacionDistribucionInicialPercapitaService.elaborarBorradorDecretoAporteEstatal(idDistribucionInicialPercapita, ano);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/enviarDecretoAporteEstatal/{borradorAporteEstatalId}/{usuario}/{ano}")
	public void enviarDecretoAporteEstatal(@PathParam("borradorAporteEstatalId") Integer borradorAporteEstatalId, @PathParam("usuario") String usuario, @PathParam("ano") Integer ano){
		System.out.println("enviar Decreto Aporte Estatal borradorAporteEstatalId-->"+borradorAporteEstatalId);
		System.out.println("enviar Decreto Aporte Estatal usuario-->"+usuario);
		System.out.println("enviar Decreto Aporte Estatal ano-->"+ano);
		if(borradorAporteEstatalId == null){
			throw new IllegalArgumentException("el documento  borradorAporteEstata: "+ borradorAporteEstatalId + " no puede ser nulo");
		}
		if(usuario == null){
			throw new IllegalArgumentException("el usuario : "+ usuario + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("el ano : "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.enviarDecretoAporteEstatal(borradorAporteEstatalId, usuario, ano);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/elaborarDocumentoFormal/{idDistribucionInicialPercapita}/{ano}")
	public void elaborarDocumentoFormal(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("elaborar Documento Formal idDistribucionInicialPercapita-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.elaborarDocumentoFormal(idDistribucionInicialPercapita, ano);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/notificarFinElaboracionResolucion/{idDistribucionInicialPercapita}/{usuario}/{ano}")
	public void notificarFinElaboracionResolucion(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("usuario") String usuario, @PathParam("ano") Integer ano){
		System.out.println("Notificar Fin Elaboracion idDistribucionInicialPercapita-->"+idDistribucionInicialPercapita);
		System.out.println("Notificar Fin Elaboracion usuario-->"+usuario);
		System.out.println("Notificar Fin Elaboracion ano-->"+ano);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(usuario == null){
			throw new IllegalArgumentException("el usuario : "+ usuario + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("el ano : "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.notificarFinElaboracionResolucion(idDistribucionInicialPercapita, usuario, ano);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/administrarVersionesFinalesAlfresco/{idDistribucionInicialPercapita}/{ano}")
	@Produces("application/json")
	public void administrarVersionesFinalesAlfresco(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("administrar Versiones Finales Alfresco idDistribucionInicialPercapita-->"+idDistribucionInicialPercapita);
		System.out.println("administrar Versiones Finales Alfresco ano-->"+ano);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.administrarVersionesFinalesAlfresco(idDistribucionInicialPercapita, ano);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/enviarResolucionesServicioSalud/{idDistribucionInicialPercapita}/{ano}")
	@Produces("application/json")
	public void enviarResolucionesServicioSalud(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("enviar Resoluciones Servicio Salud idDistribucionInicialPercapita-->"+idDistribucionInicialPercapita);
		System.out.println("enviar Resoluciones Servicio Salud ano-->"+ano);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.enviarResolucionesServicioSalud(idDistribucionInicialPercapita, ano);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/enviarConsultaRegional/{idDistribucionInicialPercapita}/{oficioConsultaId}/{ano}")
	@Produces("application/json")
	public void enviarConsultaRegional(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("oficioConsultaId") Integer oficioConsultaId,
			@PathParam("ano") Integer ano){
		System.out.println("enviar Consulta Regional idDistribucionInicialPercapita-->"+idDistribucionInicialPercapita);
		System.out.println("enviar Consulta Regional oficioConsultaId-->"+oficioConsultaId);
		System.out.println("enviar Consulta Regional ano-->"+ano);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(oficioConsultaId == null){
			throw new IllegalArgumentException("oficio consulta proceso percapita: "+ oficioConsultaId + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("oficio consulta proceso ano: "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.enviarConsultaRegional(idDistribucionInicialPercapita, oficioConsultaId, ano);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/instanciarProcesoModificacionInicialPerCapita/{usuarioId}/{ano}")
	@Produces("application/json")
	public Integer instanciarProcesoModificacionInicialPerCapita(@PathParam("usuarioId") String usuarioId, @PathParam("ano") Integer ano){
		System.out.println("instanciar Proceso Inicial PerCapita-->"+usuarioId);
		if(usuarioId == null){
			throw new IllegalArgumentException("usuarioId: "+ usuarioId + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		return modificacionDistribucionInicialPercapitaService.crearIntanciaModificacionDistribucionInicialPercapita(usuarioId, ano);
	}

	@GET
	@Path("/modificacionDistribucionInicialPerCapita/generarOrdinarioSolicitudAntecedentes/{idDistribucionInicialPercapita}/{ano}")
	public void generarOrdinarioSolicitudAntecedentes(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("generarOrdinarioSolicitudAntecedentes-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("usuarioId: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.generarOrdinarioSolicitudAntecedentes(idDistribucionInicialPercapita, ano);
	}
	
	@GET
	@Path("/modificacionDistribucionInicialPerCapita/enviarOrdinarioSolicitudAntecedentes/{idDistribucionInicialPercapita}/{ano}")
	public void enviarOrdinarioSolicitudAntecedentes(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("enviarOrdinarioSolicitudAntecedentes idDistribucionInicialPercapita-->"+idDistribucionInicialPercapita);
		System.out.println("enviarOrdinarioSolicitudAntecedentes ano-->"+ano);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("usuarioId: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService = getService(ModificacionDistribucionInicialPercapitaService.class);
		modificacionDistribucionInicialPercapitaService.enviarOrdinarioSolicitudAntecedentes(idDistribucionInicialPercapita, ano);
	}

}
