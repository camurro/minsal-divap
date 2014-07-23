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
    @Path("/distribucionInicialPerCapita/valorizarPlanSalud/{idDistribucionInicialPercapita}")
    @Produces("application/json")
    public Integer valorizarPlanSalud(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("valorizar Plan Salud-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/subirAlfresco/{docId}")
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
    @Path("/distribucionInicialPerCapita/elaborarOficioConsulta/{idDistribucionInicialPercapita}")
    @Produces("application/json")
    public Integer elaborarOficioConsulta(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("elaborar oficio proceso-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/elaborarBorradorDecretoAporteEstatal/{idDistribucionInicialPercapita}")
    @Produces("application/json")
    public Integer elaborarBorradorDecretoAporteEstatal(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("elaborar Borrador Decreto Aporte Estatal-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/enviarDecretoAporteEstatal/{idDistribucionInicialPercapita}")
    @Produces("application/json")
    public Integer enviarDecretoAporteEstatal(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("enviar Decreto Aporte Estatal-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/elaborarDocumentoFormal/{idDistribucionInicialPercapita}")
    @Produces("application/json")
    public Integer elaborarDocumentoFormal(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("elaborar Documento Formal-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/notificarFinElaboracionResolucion/{idDistribucionInicialPercapita}")
    @Produces("application/json")
    public Integer notificarFinElaboracionResolucion(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("Notificar Fin Elaboracion Resolucion-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/administrarVersionesFinalesAlfresco/{idDistribucionInicialPercapita}")
    @Produces("application/json")
    public Integer administrarVersionesFinalesAlfresco(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("administrar Versiones Finales Alfresco-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/enviarResolucionesServicioSalud/{idDistribucionInicialPercapita}")
    @Produces("application/json")
    public Integer enviarResolucionesServicioSalud(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("enviar Resoluciones Servicio Salud-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/enviarConsultaRegional/{idDistribucionInicialPercapita}")
    @Produces("application/json")
    public Integer enviarConsultaRegional(@PathParam("idDistribucionInicialPercapita") Integer idDistribucionInicialPercapita){
		System.out.println("enviar Consulta Regional-->"+idDistribucionInicialPercapita);
		if(idDistribucionInicialPercapita == null){
			throw new IllegalArgumentException("proceso: "+ idDistribucionInicialPercapita + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/instanciarProcesoInicialPerCapita/{usuarioId}")
    @Produces("application/json")
    public Integer instanciarProcesoInicialPerCapita(@PathParam("usuarioId") String usuarioId){
		System.out.println("instanciar Proceso Inicial PerCapita-->"+usuarioId);
		if(usuarioId == null){
			throw new IllegalArgumentException("usuarioId: "+ usuarioId + " no puede ser nulo");
		}
		DistribucionInicialPercapitaService distribucionInicialPercapitaService = getService(DistribucionInicialPercapitaService.class);
		return distribucionInicialPercapitaService.crearIntanciaDistribucionInicialPercapita(usuarioId);
    }
	
	
}
