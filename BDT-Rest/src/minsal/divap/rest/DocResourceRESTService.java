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

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.enums.DocumentType;
import minsal.divap.enums.ProcessDocument;
import minsal.divap.exception.DocNotFoundException;
import minsal.divap.service.DocumentService;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/documentos")
@RequestScoped
public class DocResourceRESTService extends BaseRest{
	@Inject
	private Logger log;

	@Inject
	private Validator validator;


	@DELETE
    @Path("/doc/delete/{docId}")
    @Produces("application/json")
    public void deleteDoc(
            @PathParam("docId") Integer docId)
            throws DocNotFoundException {
		System.out.println("documento a borrar-->"+docId);
		if(docId == null){
			throw new IllegalArgumentException("documento: "+ docId + " no puede ser nulo");
		}
		DocumentService documentService = getService(DocumentService.class);
		documentService.delete(docId);
    }
	
	@POST
	//@GET
    @Path("/rebaja/create/excel/{processInstanceId}/{docId}")
    @Produces("application/json")
    public Integer rebajaCreateExcel(
            @PathParam("processInstanceId") Integer processInstanceId, @PathParam("docId") Integer docId){
		if(processInstanceId == null){
			throw new IllegalArgumentException("instancia de proceso: "+ processInstanceId + " no puede ser nulo");
		}
		if(docId == null){
			throw new IllegalArgumentException("documento: "+ docId + " no puede ser nulo");
		}
		System.out.println("processInstanceId " + processInstanceId +  " documento a crear-->"+docId);
		BusinessProcess proceso = BusinessProcess.REBAJA;
		DocumentType type =  DocumentType.EXCEL;
		ProcessDocument processDocument = ProcessDocument.getById(docId);
		DocumentService documentService = getService(DocumentService.class);
		documentService.createDocument(processInstanceId, proceso, type, processDocument);
        return 1;
    }
	
	@POST
    @Path("/rebaja/create/doc/{processInstanceId}/{docId}")
    @Produces("application/json")
    public Integer rebajaCreateDoc(
            @PathParam("processInstanceId") Integer processInstanceId, @PathParam("docId") Integer docId)
            throws DocNotFoundException {
		if(processInstanceId == null && processInstanceId == null){
			throw new DocNotFoundException("documento: "+ docId + " no existe");
		}
		System.out.println("processInstanceId " + processInstanceId +  " documento a crear-->"+docId);
		BusinessProcess proceso = BusinessProcess.REBAJA;
		DocumentType type =  DocumentType.WORD;
		ProcessDocument processDocument = ProcessDocument.getById(docId);
		DocumentService documentService = getService(DocumentService.class);
		documentService.createDocument(processInstanceId, proceso, type, processDocument);
        return 1;
    }
	
	@POST
//	@GET
    @Path("/percapita/create/excel/{processInstanceId}/{docId}")
    @Produces("application/json")
    public Integer percapitaCreateExcel(
            @PathParam("processInstanceId") Integer processInstanceId, @PathParam("docId") Integer docId){
		if(processInstanceId == null){
			throw new IllegalArgumentException("instancia de proceso: "+ processInstanceId + " no puede ser nulo");
		}
		if(docId == null){
			throw new IllegalArgumentException("documento: "+ docId + " no puede ser nulo");
		}
		System.out.println("processInstanceId " + processInstanceId +  " documento a crear-->"+docId);
		BusinessProcess proceso = BusinessProcess.PERCAPITA;
		DocumentType type =  DocumentType.EXCEL;
		ProcessDocument processDocument = ProcessDocument.getById(docId);
		DocumentService documentService = getService(DocumentService.class);
		documentService.createDocument(processInstanceId, proceso, type, processDocument);
        return 1;
    }
	
	@POST
    @Path("/percapita/create/doc/{processInstanceId}/{docId}")
    @Produces("application/json")
    public Integer percapitaCreateDoc(
            @PathParam("processInstanceId") Integer processInstanceId, @PathParam("docId") Integer docId)
            throws DocNotFoundException {
		if(processInstanceId == null && processInstanceId == null){
			throw new DocNotFoundException("documento: "+ docId + " no existe");
		}
		System.out.println("processInstanceId " + processInstanceId +  " documento a crear-->"+docId);
		BusinessProcess proceso = BusinessProcess.PERCAPITA;
		DocumentType type =  DocumentType.WORD;
		ProcessDocument processDocument = ProcessDocument.getById(docId);
		DocumentService documentService = getService(DocumentService.class);
		documentService.createDocument(processInstanceId, proceso, type, processDocument);
        return 1;
    }
	
	@GET
    @Path("/create/doc/{docId}")
    @Produces("application/json")
    public void getDoc(
            @PathParam("docId") Integer docId)
            throws DocNotFoundException {
        throw new DocNotFoundException("documento: "+ docId + " no existe");
    }
	
	@GET
    @Path("/percapita/create/oficioConsulta/{processInstanceId}")
    @Produces("application/json")
    public Integer percapitaCrearOficioConsulta(
            @PathParam("processInstanceId") Integer processInstanceId){
		if(processInstanceId == null){
			throw new IllegalArgumentException("instancia de proceso: "+ processInstanceId + " no puede ser nulo");
		}
		System.out.println("processInstanceId " + processInstanceId );
		BusinessProcess proceso = BusinessProcess.PERCAPITA;
		DocumentType type =  DocumentType.EXCEL;
		ProcessDocument processDocument = ProcessDocument.OFICIOCONSULTA;
		DocumentService documentService = getService(DocumentService.class);
		return documentService.createDocument(processInstanceId, proceso, type, processDocument);
    }
	
	@GET
    @Path("/test/mensaje/{mensaje}")
    @Produces("application/json")
    public String testMensaje(
            @PathParam("mensaje") String mensaje){
		if(mensaje == null){
			throw new IllegalArgumentException("instancia de proceso: "+ mensaje + " no puede ser nulo");
		}
		System.out.println("mensaje " + mensaje );
		return "hola "+ mensaje;
    }
	
	/*@Path("{proceso}","{documento}")
	@GET
	@Produces("application/json")
	public Response convertFtoCfromInput(@PathParam("proceso") String proceso, @PathParam("documento") String documento) throws JSONException {

		JSONObject jsonObject = new JSONObject();
		float celsius;
		celsius =  (f - 32)*5/9; 
		jsonObject.put("F Value", f); 
		jsonObject.put("C Value", celsius);

		String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		return Response.status(200).entity(result).build();
	}*/

}
