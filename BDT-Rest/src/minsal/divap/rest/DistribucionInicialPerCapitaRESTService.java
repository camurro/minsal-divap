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


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/proceso")
@RequestScoped
public class DistribucionInicialPerCapitaRESTService extends BaseRest{

	@GET
    @Path("/distribucionInicialPerCapita/valorizarPlanSalud")
    @Produces("application/json")
    public Integer valorizarPlanSalud(){
		System.out.println("valorizarPlanSalud");
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
    @Path("/distribucionInicialPerCapita/elaborarOficioConsulta/{procesoId}")
    @Produces("application/json")
    public Integer elaborarOficioConsulta(@PathParam("procesoId") Integer procesoId){
		System.out.println("elaborar oficio proceso-->"+procesoId);
		if(procesoId == null){
			throw new IllegalArgumentException("proceso: "+ procesoId + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/elaborarBorradorDecretoAporteEstatal/{procesoId}")
    @Produces("application/json")
    public Integer elaborarBorradorDecretoAporteEstatal(@PathParam("procesoId") Integer procesoId){
		System.out.println("elaborar Borrador Decreto Aporte Estatal-->"+procesoId);
		if(procesoId == null){
			throw new IllegalArgumentException("proceso: "+ procesoId + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/enviarDecretoAporteEstatal/{procesoId}")
    @Produces("application/json")
    public Integer enviarDecretoAporteEstatal(@PathParam("procesoId") Integer procesoId){
		System.out.println("enviar Decreto Aporte Estatal-->"+procesoId);
		if(procesoId == null){
			throw new IllegalArgumentException("proceso: "+ procesoId + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/elaborarDocumentoFormal/{procesoId}")
    @Produces("application/json")
    public Integer elaborarDocumentoFormal(@PathParam("procesoId") Integer procesoId){
		System.out.println("elaborar Documento Formal-->"+procesoId);
		if(procesoId == null){
			throw new IllegalArgumentException("proceso: "+ procesoId + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/notificarFinElaboracionResolucion/{procesoId}")
    @Produces("application/json")
    public Integer notificarFinElaboracionResolucion(@PathParam("procesoId") Integer procesoId){
		System.out.println("Notificar Fin Elaboracion Resolucion-->"+procesoId);
		if(procesoId == null){
			throw new IllegalArgumentException("proceso: "+ procesoId + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/administrarVersionesFinalesAlfresco/{procesoId}")
    @Produces("application/json")
    public Integer administrarVersionesFinalesAlfresco(@PathParam("procesoId") Integer procesoId){
		System.out.println("administrar Versiones Finales Alfresco-->"+procesoId);
		if(procesoId == null){
			throw new IllegalArgumentException("proceso: "+ procesoId + " no puede ser nulo");
		}
        return 1;
    }
	
	@GET
    @Path("/distribucionInicialPerCapita/enviarResolucionesServicioSalud/{procesoId}")
    @Produces("application/json")
    public Integer enviarResolucionesServicioSalud(@PathParam("procesoId") Integer procesoId){
		System.out.println("enviar Resoluciones Servicio Salud-->"+procesoId);
		if(procesoId == null){
			throw new IllegalArgumentException("proceso: "+ procesoId + " no puede ser nulo");
		}
        return 1;
    }
	
	
}
