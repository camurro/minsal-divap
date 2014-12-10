/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.FactorTramoPobreza;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.vo.MantenedorEstadoProgramaVO;
import minsal.divap.vo.MantenedorFactorTramoPobrezaVO;

/**
 *
 * @author francisco
 */
@Stateless
public class FactorTramoPobrezaFacade extends AbstractFacade<FactorTramoPobreza> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private MantenedoresDAO mantenedoresDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FactorTramoPobrezaFacade() {
        super(FactorTramoPobreza.class);
    }
    
    public void edit(MantenedorFactorTramoPobrezaVO seleccionado){
    	FactorTramoPobreza factorTramoPobreza = null;
    	
    	if(seleccionado.getIdFactorTramoPobreza() == null){
    		factorTramoPobreza = new FactorTramoPobreza();
    	}else{
    		factorTramoPobreza = mantenedoresDAO.getFactorTramoPobrezaById(seleccionado.getIdFactorTramoPobreza());
    		factorTramoPobreza.setValor(seleccionado.getValor());
    		getEntityManager().merge(factorTramoPobreza);
    	}
    }
    
    public void create(MantenedorFactorTramoPobrezaVO nuevo){
    	FactorTramoPobreza factorTramoPobreza = new FactorTramoPobreza();
    	factorTramoPobreza.setValor(nuevo.getValor());
		getEntityManager().persist(factorTramoPobreza);
    }
    
    public void remove(MantenedorFactorTramoPobrezaVO seleccionado){
    	FactorTramoPobreza factorTramoPobreza = mantenedoresDAO.getFactorTramoPobrezaById(seleccionado.getIdFactorTramoPobreza());
    	getEntityManager().remove(getEntityManager().merge(factorTramoPobreza));
    }
}
