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
    		Persona persona = servicioSaludDAO.getPersonaById(seleccionado.getIdSecretarioRegional());
    		region.setSecretarioRegional(persona);
    		getEntityManager().merge(region);
    	}
    }
    
    public void create(MantenedorRegionVO nuevo){
    	Region region = new Region();
    	region.setNombre(nuevo.getNombreRegion().toUpperCase());
    	System.out.println("nuevo.getIdSecretarioRegional() --> "+nuevo.getIdSecretarioRegional());
    	Persona persona = servicioSaludDAO.getPersonaById(nuevo.getIdSecretarioRegional());
		region.setSecretarioRegional(persona);
		getEntityManager().persist(region);
    }
    
    public void remove(MantenedorRegionVO seleccionado){
    	Region region = servicioSaludDAO.getRegionById(seleccionado.getIdRegion()); 
    	getEntityManager().remove(getEntityManager().merge(region));
    }
    
}
