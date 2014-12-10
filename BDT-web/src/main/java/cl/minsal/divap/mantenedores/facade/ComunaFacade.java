/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ServicioSalud;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.vo.MantenedorComunaVO;

/**
 *
 * @author francisco
 */
@Stateless
public class ComunaFacade extends AbstractFacade<Comuna> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private ComunaDAO comunaDAO;
    @EJB
    private ServicioSaludDAO servicioSaludDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComunaFacade() {
        super(Comuna.class);
    }

	public void edit(MantenedorComunaVO comunaSeleccionada) {
		Comuna comuna = null;
		if(comunaSeleccionada.getIdComuna() == null){
			 comuna = new Comuna();
		}else{
			comuna = comunaDAO.getComunaById(comunaSeleccionada.getIdComuna());
		}
		comuna.setNombre(comunaSeleccionada.getNombreComuna().toUpperCase());
		ServicioSalud servicio = servicioSaludDAO.getServicioSaludById(comunaSeleccionada.getIdServicio());
		comuna.setServicioSalud(servicio);
		getEntityManager().merge(comuna);
		
	}
	
	private Comuna convertComunaVOtoEntity(MantenedorComunaVO mantenedorComunaVO){
		Comuna comuna = new Comuna();
		comuna.setNombre(mantenedorComunaVO.getNombreComuna().toUpperCase());
		ServicioSalud servicio = servicioSaludDAO.getServicioSaludById(mantenedorComunaVO.getIdServicio());
		comuna.setServicioSalud(servicio);
		
		return comuna;
	}

	public void remove(MantenedorComunaVO comunaSeleccionada) {
		Comuna comuna = comunaDAO.getComunaById(comunaSeleccionada.getIdComuna());
		getEntityManager().remove(getEntityManager().merge(comuna));
	}
	
	public void create(MantenedorComunaVO comunaSeleccionada) {
		Comuna comuna = convertComunaVOtoEntity(comunaSeleccionada);
        getEntityManager().persist(comuna);
    }
    
}
