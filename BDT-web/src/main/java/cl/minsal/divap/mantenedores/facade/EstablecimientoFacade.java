/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.ServicioSalud;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.vo.MantenedorEstablecimientoVO;

/**
 *
 * @author francisco
 */
@Stateless
public class EstablecimientoFacade extends AbstractFacade<Establecimiento> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private ServicioSaludDAO servicioSaludDAO;
    @EJB
    private EstablecimientosDAO establecimientosDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstablecimientoFacade() {
        super(Establecimiento.class);
    }
    
    
    public void edit(MantenedorEstablecimientoVO establecimientoSeleccionado){
    	Establecimiento establecimiento= null;
    	if(establecimientoSeleccionado.getIdEstablecimiento() == null){
    		establecimiento = new Establecimiento();
    	}else{
    		establecimiento = establecimientosDAO.getEstablecimientoById(establecimientoSeleccionado.getIdEstablecimiento());
    		establecimiento.setNombre(establecimientoSeleccionado.getNombreEstablecimiento().toUpperCase());
    		ServicioSalud servicio = servicioSaludDAO.getServicioSaludById(establecimientoSeleccionado.getIdServicio());
    		establecimiento.setServicioSalud(servicio);
    		establecimiento.setCodigo(establecimientoSeleccionado.getCodigo());
    		establecimiento.setTipo(establecimientoSeleccionado.getTipo());
    		establecimiento.setAuxiliar(establecimientoSeleccionado.getEsAuxiliar());
    		getEntityManager().merge(establecimiento);
    	}
    }
    
    public void create(MantenedorEstablecimientoVO mantenedorEstablecimientoVO){
    	Establecimiento establecimiento = new Establecimiento();
    	establecimiento.setNombre(mantenedorEstablecimientoVO.getNombreEstablecimiento().toUpperCase());
		ServicioSalud servicio = servicioSaludDAO.getServicioSaludById(mantenedorEstablecimientoVO.getIdServicio());
		establecimiento.setServicioSalud(servicio);
		establecimiento.setCodigo(mantenedorEstablecimientoVO.getCodigo());
		establecimiento.setTipo(mantenedorEstablecimientoVO.getTipo());
		establecimiento.setAuxiliar(mantenedorEstablecimientoVO.getEsAuxiliar());
		getEntityManager().persist(establecimiento);
    }
    
    public void remove(MantenedorEstablecimientoVO establecimientoSeleccionado){
    	Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(establecimientoSeleccionado.getIdEstablecimiento());
    	getEntityManager().remove(getEntityManager().merge(establecimiento));
    }
    
    
    
}
