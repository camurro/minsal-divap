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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import minsal.divap.service.EmailService;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/notificacion")
@RequestScoped
public class NotificacionRESTService extends BaseRest{
	@Inject
	private Logger log;

	@Inject
	private Validator validator;

	

	@POST
	@Path("/percapita/error/excel/{to}/{subject}/{body}")
	@Produces("application/json")
	public String notificacionCorreo(
			@PathParam("to") String to, @PathParam("subject") String subject, @PathParam("body") String body){
		if(to == null){
			throw new IllegalArgumentException("El correo: "+ to + " no puede ser nulo");
		}
		if(subject == null){
			throw new IllegalArgumentException("El asunto: "+ subject + " no puede ser nulo");
		}
		if(body == null){
			throw new IllegalArgumentException("El contenido: "+ body + " no puede ser nulo");
		}
		System.out.println("para " + to + " asunto " + subject +  " contenido "+body);
		List<String> recipients = new ArrayList<String>();
		recipients.add(to);
		EmailService emailService = getService(EmailService.class);
		emailService.sendMail(recipients, subject, body);
		return "ok";
	}



	@GET
	@Path("/percapita/error/excel/{to}/{subject}")
	@Produces("application/json")
	public void notificacionDummy(
			@PathParam("to") String to, @PathParam("subject") String subject){
		if(to == null){
			throw new IllegalArgumentException("El correo: "+ to + " no puede ser nulo");
		}
		if(subject == null){
			throw new IllegalArgumentException("El asunto: "+ subject + " no puede ser nulo");
		}
		System.out.println("para " + to + " asunto " + subject );
	}

}
