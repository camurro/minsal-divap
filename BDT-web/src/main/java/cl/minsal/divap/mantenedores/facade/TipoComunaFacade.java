/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.Persona;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoComuna;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.ComunaDAO;
import minsal.divap.vo.ServiciosVO;

/**
 *
 * @author francisco
 */
@Stateless
public class TipoComunaFacade extends AbstractFacade<TipoComuna> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private ComunaDAO comunaDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoComunaFacade() {
        super(TipoComuna.class);
    }
    
    public void edit(TipoComuna seleccionado){
    	TipoComuna tipoComuna = null;
    	if(seleccionado.getIdTipoComuna() == null){
    		tipoComuna = new TipoComuna();
    	}else{
    		tipoComuna = comunaDAO.getTipoComunaById(seleccionado.getIdTipoComuna());
    		tipoComuna.setDescripcion(seleccionado.getDescripcion().toUpperCase());
    		getEntityManager().merge(tipoComuna);
    	}
    }
    
    public void create(TipoComuna nuevo){
    	TipoComuna tipoComuna = new TipoComuna();
    	tipoComuna.setDescripcion(nuevo.getDescripcion().toUpperCase());
    	
		getEntityManager().persist(tipoComuna);
    }
    
    public void remove(TipoComuna seleccionado){
    	TipoComuna tipoComuna = comunaDAO.getTipoComunaById(seleccionado.getIdTipoComuna());
    	getEntityManager().remove(getEntityManager().merge(tipoComuna));
    }
    
}
