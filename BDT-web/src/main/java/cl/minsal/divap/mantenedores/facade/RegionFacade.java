/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.Email;
import cl.minsal.divap.model.Persona;
import cl.minsal.divap.model.Region;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.EmailDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.vo.MantenedorRegionVO;
import minsal.divap.vo.PersonaVO;

/**
 *
 * @author francisco
 */
@Stateless
public class RegionFacade extends AbstractFacade<Region> {
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

    public RegionFacade() {
        super(Region.class);
    }
    
    public void edit(MantenedorRegionVO seleccionado){
    	Region region = null;
    	if(seleccionado.getIdRegion() == null){
    		region = new Region();
    	}else{
    		region = servicioSaludDAO.getRegionById(seleccionado.getIdRegion());
    		region.setNombre(seleccionado.getNombreRegion().toUpperCase());
    		
    		
    		Persona secretario = servicioSaludDAO.getPersonaById(seleccionado.getSecretarioRegional().getIdPersona());
    		secretario.setNombre(seleccionado.getSecretarioRegional().getNombre());
        	secretario.setApellidoPaterno(seleccionado.getSecretarioRegional().getApellidoPaterno());
        	secretario.setApellidoMaterno(seleccionado.getSecretarioRegional().getApellidoMaterno());
        	
        	Email emailDirector = emailDAO.getEmailIdById(seleccionado.getSecretarioRegional().getIdCorreo());
        	emailDirector.setValor(seleccionado.getSecretarioRegional().getCorreo());
        	getEntityManager().merge(emailDirector);
        	secretario.setEmail(emailDirector);
        	getEntityManager().merge(secretario);
        	
        	region.setSecretarioRegional(secretario);
    		
    		getEntityManager().merge(region);
    	}
    }
    
    public void create(MantenedorRegionVO nuevo){
    	Region region = new Region();
    	region.setNombre(nuevo.getNombreRegion().toUpperCase());
    	
    	
    	Persona secretario = new Persona();
    	secretario.setNombre(nuevo.getSecretarioRegional().getNombre());
    	secretario.setApellidoPaterno(nuevo.getSecretarioRegional().getApellidoPaterno());
    	secretario.setApellidoMaterno(nuevo.getSecretarioRegional().getApellidoMaterno());
		
		Email emailSecretario= new Email();
		emailSecretario.setValor(nuevo.getSecretarioRegional().getCorreo());
		getEntityManager().persist(emailSecretario);
		secretario.setEmail(emailSecretario);
		getEntityManager().persist(secretario);
		
    	
		region.setSecretarioRegional(secretario);
		getEntityManager().persist(region);
    }
    
    public void remove(MantenedorRegionVO seleccionado){
    	Region region = servicioSaludDAO.getRegionById(seleccionado.getIdRegion()); 
    	getEntityManager().remove(getEntityManager().merge(region));
    }
    
}
