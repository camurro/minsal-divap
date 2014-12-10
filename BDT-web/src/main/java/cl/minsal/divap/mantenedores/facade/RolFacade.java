/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.Rol;
import cl.minsal.divap.model.TipoComponente;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.RolDAO;
import minsal.divap.vo.MantenedorComponenteVO;
import minsal.divap.vo.MantenedorRolVO;

/**
 *
 * @author francisco
 */
@Stateless
public class RolFacade extends AbstractFacade<Rol> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private RolDAO rolDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolFacade() {
        super(Rol.class);
    }
    
    
    public void edit(Rol seleccionado){
    	Rol rol = null;
    	if(seleccionado.getNombre() == null){
    		rol = new Rol();
    	}else{
    		rol = rolDAO.getRolByNombre(seleccionado.getNombre());
    		rol.setNombre(seleccionado.getNombre());
    		getEntityManager().merge(rol);
    	}
    }
    
    public void create(Rol newRol){
    	Rol rol = new Rol();
    	rol.setNombre(newRol.getNombre());
		getEntityManager().persist(rol);
    }
    
    public void remove(Rol seleccionado){
    	Rol rol = rolDAO.getRolByNombre(seleccionado.getNombre());
    	getEntityManager().remove(getEntityManager().merge(rol));
    }
    
}
