/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.vo.MantenedorCumplimientoVO;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.FactorTramoPobreza;
import cl.minsal.divap.model.TipoCumplimiento;
import cl.minsal.divap.model.Tramo;

/**
 *
 * @author francisco
 */
@Stateless
public class CumplimientoFacade extends AbstractFacade<Cumplimiento> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private MantenedoresDAO mantenedoresDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CumplimientoFacade() {
        super(Cumplimiento.class);
    }
    
    public void edit(MantenedorCumplimientoVO seleccionado){
    	Cumplimiento cumplimiento = null;
    	
    	if(seleccionado.getIdCumplimiento() == null){
    		cumplimiento = new Cumplimiento();
    	}else{
    		cumplimiento = mantenedoresDAO.getCumplimientoById(seleccionado.getIdCumplimiento());
    		cumplimiento.setPorcentajeDesde(seleccionado.getPorcentajeDesde());
    		cumplimiento.setPorcentajeHasta(seleccionado.getPorcentajeHasta());
    		
    		TipoCumplimiento tipoCumplimiento = mantenedoresDAO.getTipoCumplimientoById(seleccionado.getIdTipoCumplimiento());
    		Tramo tramo = mantenedoresDAO.getTramoById(seleccionado.getIdTramo());
    		cumplimiento.setTramo(tramo);
    		cumplimiento.setTipoCumplimiento(tipoCumplimiento);
    		cumplimiento.setRebaja(seleccionado.getRebaja());
    		
    		getEntityManager().merge(cumplimiento);
    	}
    }
    
    public void create(MantenedorCumplimientoVO nuevo){
    	Cumplimiento cumplimiento = new Cumplimiento();
		cumplimiento.setPorcentajeDesde(nuevo.getPorcentajeDesde());
		cumplimiento.setPorcentajeHasta(nuevo.getPorcentajeHasta());
		
		TipoCumplimiento tipoCumplimiento = mantenedoresDAO.getTipoCumplimientoById(nuevo.getIdTipoCumplimiento());
		
		Tramo tramo = mantenedoresDAO.getTramoById(nuevo.getIdTramo());
		
		
		cumplimiento.setTramo(tramo);
		cumplimiento.setTipoCumplimiento(tipoCumplimiento);
		cumplimiento.setRebaja(nuevo.getRebaja());
		getEntityManager().persist(cumplimiento);
    }
    
    public void remove(MantenedorCumplimientoVO seleccionado){
    	Cumplimiento cumplimiento = mantenedoresDAO.getCumplimientoById(seleccionado.getIdCumplimiento());
    	getEntityManager().remove(getEntityManager().merge(cumplimiento));
    }
    
}
