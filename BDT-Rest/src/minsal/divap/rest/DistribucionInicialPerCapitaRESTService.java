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


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/proceso")
@RequestScoped
public class DistribucionInicialPerCapitaRESTService extends BaseRest{

	@GET
    @Path("/distribucionInicialPerCapita/valorizarPlanSalud/{idDistribucionInicialPercapita}/{ano}")
    @Produces("application/json")
    public Integer valorizarPlanSalud(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("valorizar Plan Salud-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("proceso: "+ ano + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		return distribucionInicialPercapitaService.valorizarDisponibilizarPlanillaTrabajo(idDistribucionInicialPercapita, ano);
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/creacionAntecedentesComuna/{usuario}/{idDistribucionInicialPercapita}/{ano}")
    public void creacionAntecedentesComuna(@PathParam("usuario") String usuario, @PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, 
    		@PathParam("ano") Integer ano){
		System.out.println("creacionAntecedentesComuna -->"+usuario);
		if(usuario == null){
			throw new IllegalArgumentException("usuario: "+ usuario + " no puede ser nulo");
		}
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("idDistribucionInicialPercapita: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		distribucionInicialPercapitaService.creacionAntecedentesComuna(usuario, idDistribucionInicialPercapita, ano);
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/subirAlfresco/{docId}")
    @Produces("application/json")
    public Integer subirAlfresco(@PathParam("docId") Integer docId){
		System.out.println("documento a subir-->"+docId);
		if(docId == null){
			throw new IllegalArgumentException("documento: "+ docId + " no puede ser nulo");
		}
		System.out.println("subirAlfresco");
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/elaborarOficioConsulta/{idDistribucionInicialPercapita}/{ano}")
    @Produces("application/json")
    public Integer elaborarOficioConsulta(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("elaborar oficio proceso-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		return distribucionInicialPercapitaService.crearOficioConsulta(idDistribucionInicialPercapita, ano);
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/elaborarBorradorDecretoAporteEstatal/{idDistribucionInicialPercapita}/{ano}")
    @Produces("application/json")
    public Integer elaborarBorradorDecretoAporteEstatal(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita,
    		@PathParam("ano") Integer ano){
		System.out.println("elaborar Borrador Decreto Aporte Estatal-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		return distribucionInicialPercapitaService.elaborarBorradorDecretoAporteEstatal(idDistribucionInicialPercapita, ano);
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/enviarDecretoAporteEstatal/{borradorAporteEstatalId}/{usuario}/{ano}")
    public void enviarDecretoAporteEstatal(@PathParam("borradorAporteEstatalId") Integer borradorAporteEstatalId, @PathParam("usuario") String usuario,
    		@PathParam("ano") Integer ano){
		System.out.println("enviar Decreto Aporte Estatal-->"+borradorAporteEstatalId);
		if(borradorAporteEstatalId == null){
			throw new IllegalArgumentException("el documento  borradorAporteEstata: "+ borradorAporteEstatalId + " no puede ser nulo");
		}
		if(usuario == null){
			throw new IllegalArgumentException("el usuario : "+ usuario + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("el ano : "+ ano + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		distribucionInicialPercapitaService.enviarDecretoAporteEstatal(borradorAporteEstatalId, usuario, ano);
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/elaborarDocumentoFormal/{idDistribucionInicialPercapita}/{ano}")
    public void elaborarDocumentoFormal(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("ano") Integer ano){
		System.out.println("elaborar Documento Formal-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		distribucionInicialPercapitaService.elaborarDocumentoFormal(idDistribucionInicialPercapita, ano);
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/notificarFinElaboracionResolucion/{idDistribucionInicialPercapita}/{usuario}/{ano}")
    public void notificarFinElaboracionResolucion(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, 
    		@PathParam("usuario") String usuario, @PathParam("ano") Integer ano){
		System.out.println("Notificar Fin Elaboracion Resolucion-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(usuario == null){
			throw new IllegalArgumentException("el usuario : "+ usuario + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("el ano : "+ ano + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		distribucionInicialPercapitaService.notificarFinElaboracionResolucion(idDistribucionInicialPercapita, usuario, ano);
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/administrarVersionesFinalesAlfresco/{idDistribucionInicialPercapita}/{ano}")
    @Produces("application/json")
    public void administrarVersionesFinalesAlfresco(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita,
    		@PathParam("ano") Integer ano){
		System.out.println("administrar Versiones Finales Alfresco-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		distribucionInicialPercapitaService.administrarVersionesFinalesAlfresco(idDistribucionInicialPercapita, ano);
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/enviarResolucionesServicioSalud/{idDistribucionInicialPercapita}/{ano}")
    @Produces("application/json")
    public void enviarResolucionesServicioSalud(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita,
    		@PathParam("ano") Integer ano){
		System.out.println("enviar Resoluciones Servicio Salud-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		distribucionInicialPercapitaService.enviarResolucionesServicioSalud(idDistribucionInicialPercapita, ano);
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/enviarConsultaRegional/{idDistribucionInicialPercapita}/{oficioConsultaId}/{ano}")
    @Produces("application/json")
    public void enviarConsultaRegional(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita, @PathParam("oficioConsultaId") Integer oficioConsultaId, 
    		@PathParam("ano") Integer ano){
		System.out.println("enviar Consulta Regional-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
		if(oficioConsultaId == null){
			throw new IllegalArgumentException("oficio consulta proceso percapita: "+ oficioConsultaId + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		distribucionInicialPercapitaService.enviarConsultaRegional(idDistribucionInicialPercapita, oficioConsultaId, ano);
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/instanciarProcesoInicialPerCapita/{usuarioId}/{ano}")
    @Produces("application/json")
    public Integer instanciarProcesoInicialPerCapita(@PathParam("usuarioId") String usuarioId, @PathParam("ano") Integer ano){
		System.out.println("instanciar Proceso Inicial PerCapita-->"+usuarioId);
		if(usuarioId == null){
			throw new IllegalArgumentException("usuarioId: "+ usuarioId + " no puede ser nulo");
		}
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		return distribucionInicialPercapitaService.crearIntanciaDistribucionInicialPercapita(usuarioId, ano);
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/cambiarEstadoPercapita/{ano}/{estado}")
    @Produces("application/json")
    public void cambiarEstadoPercapita(@PathParam("ano") Integer ano, @PathParam("estado") String estado){
		System.out.println("Cambiar estado programa percapita ano-->"+ano);
		if(ano == null){
			throw new IllegalArgumentException("ano: "+ ano + " no puede ser nulo");
		}
		if(estado == null){
			throw new IllegalArgumentException("estado: "+ estado + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		EstadosProgramas estadoPrograma = EstadosProgramas.getById(Integer.parseInt(estado));
		distribucionInicialPercapitaService.cambiarEstadoProgramaPercapita(ano, estadoPrograma);
    }
	
}
