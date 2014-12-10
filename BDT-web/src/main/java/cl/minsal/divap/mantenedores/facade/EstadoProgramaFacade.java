/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Persona;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.ServicioSalud;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.ProgramasDAO;
import minsal.divap.vo.MantenedorEstadoProgramaVO;
import minsal.divap.vo.ServiciosVO;

/**
 *
 * @author francisco
 */
@Stateless
public class EstadoProgramaFacade extends AbstractFacade<EstadoPrograma> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private ProgramasDAO programasDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoProgramaFacade() {
        super(EstadoPrograma.class);
    }
    
    public void edit(MantenedorEstadoProgramaVO seleccionado){
    	EstadoPrograma estadoPrograma = null;
    	if(seleccionado.getId_estado_programa() == null){
    		estadoPrograma = new EstadoPrograma();
    	}else{
    		estadoPrograma = programasDAO.getEstadoProgramaById(seleccionado.getId_estado_programa());
    		estadoPrograma.setNombreEstado(seleccionado.getNombre_estado());
    		getEntityManager().merge(estadoPrograma);
    	}
    }
    
    public void create(MantenedorEstadoProgramaVO nuevo){
    	EstadoPrograma estadoPrograma = new EstadoPrograma();
    	estadoPrograma.setNombreEstado(nuevo.getNombre_estado());
		getEntityManager().persist(estadoPrograma);
    }
    
    public void remove(MantenedorEstadoProgramaVO seleccionado){
    	EstadoPrograma estadoPrograma = programasDAO.getEstadoProgramaById(seleccionado.getId_estado_programa());
    	getEntityManager().remove(getEntityManager().merge(estadoPrograma));
    }
    
}
