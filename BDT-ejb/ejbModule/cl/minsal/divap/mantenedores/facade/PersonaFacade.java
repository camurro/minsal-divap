/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Email;
import cl.minsal.divap.model.Persona;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.TipoComponente;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.EmailDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.vo.MantenedorComponenteVO;
import minsal.divap.vo.PersonaVO;

/**
 *
 * @author francisco
 */
@Stateless
public class PersonaFacade extends AbstractFacade<Persona> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private ServicioSaludDAO servicioSaludDAO;
    @EJB
    private EmailDAO emailDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PersonaFacade() {
        super(Persona.class);
    }
    
    public void edit(PersonaVO seleccionado){
    	Persona persona = null;
    	if(seleccionado.getIdPersona() == null){
    		persona = new Persona();
    	}else{
    		persona = servicioSaludDAO.getPersonaById(seleccionado.getIdPersona());
    		persona.setNombre(seleccionado.getNombre().toUpperCase());
    		persona.setApellidoPaterno(seleccionado.getApellidoPaterno().toUpperCase());
    		persona.setApellidoMaterno(seleccionado.getApellidoMaterno().toUpperCase());
    		Email email = emailDAO.getEmailIdByValor(seleccionado.getCorreo());
    		persona.setEmail(email);
    		getEntityManager().merge(persona);
    	}
    }
    
    public void create(PersonaVO nuevo){
    	Persona persona = new Persona();
    	persona.setNombre(nuevo.getNombre().toUpperCase());
		persona.setApellidoPaterno(nuevo.getApellidoPaterno().toUpperCase());
		persona.setApellidoMaterno(nuevo.getApellidoMaterno().toUpperCase());
		Email email = emailDAO.getEmailIdByValor(nuevo.getCorreo());
		persona.setEmail(email);
		getEntityManager().persist(persona);
    }
    
    public void remove(PersonaVO seleccionado){
    	Persona persona = servicioSaludDAO.getPersonaById(seleccionado.getIdPersona()); 
    	getEntityManager().remove(getEntityManager().merge(persona));
    }
    
}
