/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.Persona;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.ServicioSalud;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.service.MantenedoresService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.MantenedorRegionVO;
import minsal.divap.vo.ServiciosVO;

/**
 *
 * @author francisco
 */
@Stateless
public class ServicioSaludFacade extends AbstractFacade<ServicioSalud> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private ServicioSaludService servicioSaludService;
    @EJB
    private MantenedoresService mantenedoresService;
    @EJB
    private ServicioSaludDAO servicioSaludDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ServicioSaludFacade() {
        super(ServicioSalud.class);
    }
    
    public void edit(ServiciosVO seleccionado){
    	ServicioSalud servicio = null;
    	if(seleccionado.getId_servicio() == null){
    		servicio = new ServicioSalud();
    	}else{
    		servicio = servicioSaludDAO.getServicioSaludById(seleccionado.getId_servicio());
    		servicio.setNombre(seleccionado.getNombre_servicio().toUpperCase());
    		
    		Region region = servicioSaludDAO.getRegionById(seleccionado.getRegion().getId());
    		servicio.setRegion(region);
    		
    		Persona director = servicioSaludDAO.getPersonaById(seleccionado.getDirector().getIdPersona());
    		Persona encargadoAps = servicioSaludDAO.getPersonaById(seleccionado.getEncargadoAps().getIdPersona());
    		Persona encargadoFinanzasAps = servicioSaludDAO.getPersonaById(seleccionado.getEncargadoFinanzasAps().getIdPersona());
    		
    		servicio.setDirector(director);
    		servicio.setEncargadoAps(encargadoAps);
    		servicio.setEncargadoFinanzasAps(encargadoFinanzasAps);
    		getEntityManager().merge(servicio);
    	}
    }
    
    public void create(ServiciosVO nuevo){
    	ServicioSalud servicio = new ServicioSalud();
    	servicio.setNombre(nuevo.getNombre_servicio().toUpperCase());
		
		Region region = servicioSaludDAO.getRegionById(nuevo.getRegion().getId());
		servicio.setRegion(region);
		
		Persona director = servicioSaludDAO.getPersonaById(nuevo.getDirector().getIdPersona());
		Persona encargadoAps = servicioSaludDAO.getPersonaById(nuevo.getEncargadoAps().getIdPersona());
		Persona encargadoFinanzasAps = servicioSaludDAO.getPersonaById(nuevo.getEncargadoFinanzasAps().getIdPersona());
		
		servicio.setDirector(director);
		servicio.setEncargadoAps(encargadoAps);
		servicio.setEncargadoFinanzasAps(encargadoFinanzasAps);
    	
		getEntityManager().persist(servicio);
    }
    
    public void remove(ServiciosVO seleccionado){
    	ServicioSalud servicio = servicioSaludDAO.getServicioSaludById(seleccionado.getId_servicio()); 
    	getEntityManager().remove(getEntityManager().merge(servicio));
    }
}
