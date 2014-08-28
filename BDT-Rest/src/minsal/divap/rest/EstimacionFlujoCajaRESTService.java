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
import minsal.divap.service.EstimacionFlujoCajaService;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/proceso")
@RequestScoped
public class EstimacionFlujoCajaRESTService extends BaseRest{

	@GET
    @Path("/estimacionFlujoCaja/calcularPropuesta/{idLineaProgramatica}")
    @Produces("application/json")
    public Integer calcularPropuesta(@PathParam("idLineaProgramatica") Integer idLineaProgramatica){
		System.out.println("[Calcular Propuesta] -->"+idLineaProgramatica);
		
		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		Integer ind = estimacionFlujoCajaService.calcularPropuesta(idLineaProgramatica);
		//return distribucionInicialPercapitaService.valorizarDisponibilizarPlanillaTrabajo(idDistribucionInicialPercapita);
		
		return 1;
    }
	
	
	@GET
    @Path("/estimacionFlujoCaja/generarPlanillaPropuesta/{idLineaProgramatica}")
    @Produces("application/json")
    public Integer generarPlanillaPropuesta(@PathParam("idLineaProgramatica") Integer idLineaProgramatica){
		System.out.println("[Generar Planilla] -->"+idLineaProgramatica);
		return 1;
    }
	
	@GET
    @Path("/estimacionFlujoCaja/eliminarPlanillaPropuesta/{idLineaProgramatica}")
    @Produces("application/json")
    public Integer eliminarPlanillaPropuesta(@PathParam("idLineaProgramatica") Integer idLineaProgramatica){
		System.out.println("[Eliminar Planilla] -->"+idLineaProgramatica);
		return 1;
    }

	@GET
    @Path("/estimacionFlujoCaja/notificarUsuarioConsolidador/{idLineaProgramatica}")
    @Produces("application/json")
    public Integer notificarUsuarioConsolidador(@PathParam("idLineaProgramatica") Integer idLineaProgramatica){
		System.out.println("[Eliminar Planilla] -->"+idLineaProgramatica);
		return 1;
    }
	
	@GET
    @Path("/estimacionFlujoCaja/recalcularEstimacion/{flujo}")
    @Produces("application/json")
    public Integer recalcularEstimacion(@PathParam("flujo") Integer idLineaProgramatica){
		System.out.println("[Eliminar Planilla] -->"+idLineaProgramatica);
		return 1;
    }
	
	
	@GET
    @Path("/estimacionFlujoCaja/elaborarOrdinarioProgramacion/{flujo}")
    @Produces("application/json")
    public Integer elaborarOrdinarioProgramacion(@PathParam("flujo") Integer idLineaProgramatica){
		System.out.println("[Eliminar Planilla] -->"+idLineaProgramatica);
		return 1;
    }
	
	@GET
    @Path("/estimacionFlujoCaja/administrarVersiones/{flujo}")
    @Produces("application/json")
    public Integer administrarVersiones(@PathParam("flujo") Integer idLineaProgramatica){
		System.out.println("[Eliminar Planilla] -->"+idLineaProgramatica);

		EstimacionFlujoCajaService estimacionFlujoCajaService = getService(EstimacionFlujoCajaService.class);
		estimacionFlujoCajaService.elaborarOrdinarioProgramacionCaja(1);

		return 1;
    }
	
	
	
		
	@GET
    @Path("/estimacionFlujoCaja/enviarOrdinarioFONASA/{flujo}")
    @Produces("application/json")
    public Integer enviarOrdinarioFONASA(@PathParam("flujo") Integer idLineaProgramatica){
		System.out.println("[Eliminar Planilla] -->"+idLineaProgramatica);
		return 1;
    }
	
}
