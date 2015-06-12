/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.Email;
import cl.minsal.divap.model.Persona;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.ServicioSalud;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.EmailDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.service.MantenedoresService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.MantenedorRegionVO;
import minsal.divap.vo.ServiciosMantenedorVO;
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
    @EJB
    private EmailDAO emailDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ServicioSaludFacade() {
        super(ServicioSalud.class);
    }
    
    public void edit(ServiciosMantenedorVO seleccionado){
    	ServicioSalud servicio = null;
    	if(seleccionado.getId_servicio() == null){
    		servicio = new ServicioSalud();
    	}else{
    		servicio = servicioSaludDAO.getServicioSaludById(seleccionado.getId_servicio());
    		servicio.setNombre(seleccionado.getNombre_servicio().toUpperCase());
    		
    		Region region = servicioSaludDAO.getRegionById(seleccionado.getRegion().getId());
    		servicio.setRegion(region);
    		
    		Persona director = servicioSaludDAO.getPersonaById(seleccionado.getDirector().getIdPersona());
    		director.setNombre(seleccionado.getDirector().getNombre());
    		director.setApellidoPaterno(seleccionado.getDirector().getApellidoPaterno());
    		director.setApellidoMaterno(seleccionado.getDirector().getApellidoMaterno());
    		
    		Email emailDirector = emailDAO.getEmailIdById(seleccionado.getDirector().getIdCorreo());
    		emailDirector.setValor(seleccionado.getDirector().getCorreo());
    		getEntityManager().merge(emailDirector);
    		director.setEmail(emailDirector);
    		getEntityManager().merge(director);
    		
    		
    		
    		
    		Persona encargadoFinanzasAps = servicioSaludDAO.getPersonaById(seleccionado.getEncargadoFinanzasAps().getIdPersona());
    		encargadoFinanzasAps.setNombre(seleccionado.getEncargadoFinanzasAps().getNombre());
    		encargadoFinanzasAps.setApellidoPaterno(seleccionado.getEncargadoFinanzasAps().getApellidoPaterno());
    		encargadoFinanzasAps.setApellidoMaterno(seleccionado.getEncargadoFinanzasAps().getApellidoMaterno());
    		
    		Email emailEncargadoFinanzasAps = emailDAO.getEmailIdById(seleccionado.getEncargadoFinanzasAps().getIdCorreo());
    		emailEncargadoFinanzasAps.setValor(seleccionado.getEncargadoFinanzasAps().getCorreo());
    		getEntityManager().merge(emailEncargadoFinanzasAps);
    		encargadoFinanzasAps.setEmail(emailEncargadoFinanzasAps);
    		getEntityManager().merge(encargadoFinanzasAps);
    		
    		
    		
    		
    		Persona encargadoAps = servicioSaludDAO.getPersonaById(seleccionado.getEncargadoAps().getIdPersona());
    		encargadoAps.setNombre(seleccionado.getEncargadoAps().getNombre());
    		encargadoAps.setApellidoPaterno(seleccionado.getEncargadoAps().getApellidoPaterno());
    		encargadoAps.setApellidoMaterno(seleccionado.getEncargadoAps().getApellidoMaterno());
    		
    		Email emailEncargadoAps = emailDAO.getEmailIdById(seleccionado.getEncargadoAps().getIdCorreo());
    		emailEncargadoAps.setValor(seleccionado.getEncargadoAps().getCorreo());
    		getEntityManager().merge(emailEncargadoAps);
    		encargadoAps.setEmail(emailEncargadoAps);
    		getEntityManager().merge(encargadoAps);
    		
    		
    		servicio.setDirector(director);
    		servicio.setEncargadoAps(encargadoAps);
    		servicio.setEncargadoFinanzasAps(encargadoFinanzasAps);
    		getEntityManager().merge(servicio);
    	}
    }
    
    public void create(ServiciosMantenedorVO nuevo){
    	ServicioSalud servicio = new ServicioSalud();
    	servicio.setNombre(nuevo.getNombre_servicio().toUpperCase());
		
		Region region = servicioSaludDAO.getRegionById(nuevo.getRegion().getId());
		servicio.setRegion(region);
		
		Persona director = new Persona();
		director.setNombre(nuevo.getDirector().getNombre());
		director.setApellidoPaterno(nuevo.getDirector().getApellidoPaterno());
		director.setApellidoMaterno(nuevo.getDirector().getApellidoMaterno());
		
		Email emailDirector = new Email();
		emailDirector.setValor(nuevo.getDirector().getCorreo());
		getEntityManager().persist(emailDirector);
		
		director.setEmail(emailDirector);
		getEntityManager().persist(director);
		servicio.setDirector(director);
		
		
		Persona encargadoFinanzasAps = new Persona();
		encargadoFinanzasAps.setNombre(nuevo.getEncargadoFinanzasAps().getNombre());
		encargadoFinanzasAps.setApellidoPaterno(nuevo.getEncargadoFinanzasAps().getApellidoPaterno());
		encargadoFinanzasAps.setApellidoMaterno(nuevo.getEncargadoFinanzasAps().getApellidoMaterno());
		
		Email emailEncargadoFinanzasAps = new Email();
		emailEncargadoFinanzasAps.setValor(nuevo.getEncargadoFinanzasAps().getCorreo());
		getEntityManager().persist(emailEncargadoFinanzasAps);
		
		encargadoFinanzasAps.setEmail(emailEncargadoFinanzasAps);
		getEntityManager().persist(encargadoFinanzasAps);
		servicio.setEncargadoFinanzasAps(encargadoFinanzasAps);
		
		
		Persona encargadoAps = new Persona();
		encargadoAps.setNombre(nuevo.getEncargadoAps().getNombre());
		encargadoAps.setApellidoPaterno(nuevo.getEncargadoAps().getApellidoPaterno());
		encargadoAps.setApellidoMaterno(nuevo.getEncargadoAps().getApellidoMaterno());
		
		Email emailEncargadoAps = new Email();
		emailEncargadoAps.setValor(nuevo.getEncargadoAps().getCorreo());
		getEntityManager().persist(emailEncargadoAps);
		
		encargadoAps.setEmail(emailEncargadoAps);
		getEntityManager().persist(encargadoAps);
		
		servicio.setEncargadoAps(encargadoAps);
    	
		getEntityManager().persist(servicio);
    }
    
    public void remove(ServiciosMantenedorVO seleccionado){
    	ServicioSalud servicio = servicioSaludDAO.getServicioSaludById(seleccionado.getId_servicio()); 
    	getEntityManager().remove(getEntityManager().merge(servicio));
    }
}
