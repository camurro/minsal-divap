/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Dependencia;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.TipoComponente;
import cl.minsal.divap.model.TipoSubtitulo;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.vo.MantenedorComponenteVO;
import minsal.divap.vo.MantenedorTipoSubtituloVO;

/**
 *
 * @author francisco
 */
@Stateless
public class TipoSubtituloFacade extends AbstractFacade<TipoSubtitulo> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private TipoSubtituloDAO tipoSubtituloDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoSubtituloFacade() {
        super(TipoSubtitulo.class);
    }
    
    
    public void edit(MantenedorTipoSubtituloVO subtituloSeleccionado){
    	TipoSubtitulo tipoSubtitulo = null;
    	if(subtituloSeleccionado.getIdSubtitulo() == null){
    		tipoSubtitulo = new TipoSubtitulo();
    	}else{
    		tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloById(subtituloSeleccionado.getIdSubtitulo());
    		tipoSubtitulo.setNombreSubtitulo(subtituloSeleccionado.getNombreSubtitulo().toUpperCase());
    		Dependencia dependencia = tipoSubtituloDAO.getDependenciaById(subtituloSeleccionado.getIdDependencia());
    		tipoSubtitulo.setDependencia(dependencia);
    		tipoSubtitulo.setInflactor(subtituloSeleccionado.getInflactor());
    		getEntityManager().merge(tipoSubtitulo);
    	}
    }
    
    public void create(MantenedorTipoSubtituloVO mantenedorTipoSubtituloVO){
    	TipoSubtitulo tipoSubtitulo = new TipoSubtitulo();
    	tipoSubtitulo.setNombreSubtitulo(mantenedorTipoSubtituloVO.getNombreSubtitulo().toUpperCase());
		Dependencia dependencia = tipoSubtituloDAO.getDependenciaById(mantenedorTipoSubtituloVO.getIdDependencia());
		tipoSubtitulo.setDependencia(dependencia);
		tipoSubtitulo.setInflactor(mantenedorTipoSubtituloVO.getInflactor());
		getEntityManager().persist(tipoSubtitulo);
    }
    
    public void remove(MantenedorTipoSubtituloVO subtituloSeleccionado){
    	TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloById(subtituloSeleccionado.getIdSubtitulo());
    	getEntityManager().remove(getEntityManager().merge(tipoSubtitulo));
    }
    
}
