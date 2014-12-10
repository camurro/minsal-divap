/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import java.util.List;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoComponente;
import cl.minsal.divap.model.TipoSubtitulo;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.vo.MantenedorComponenteVO;
import minsal.divap.vo.MantenedorEstablecimientoVO;

/**
 *
 * @author francisco
 */
@Stateless
public class ComponenteFacade extends AbstractFacade<Componente> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private ProgramasDAO programasDAO;
    @EJB
    private ComponenteDAO componenteDAO;
    @EJB
    private TipoSubtituloDAO tipoSubtituloDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComponenteFacade() {
        super(Componente.class);
    }
    
    public void edit(MantenedorComponenteVO componenteSeleccionado){
    	Componente componente = null;
    	if(componenteSeleccionado.getIdComponente() == null){
    		componente = new Componente();
    	}else{
    		componente = componenteDAO.getComponenteByID(componenteSeleccionado.getIdComponente());
    		componente.setNombre(componenteSeleccionado.getNombreComponente());
    		Programa programa = programasDAO.getProgramaById(componenteSeleccionado.getIdPrograma());
    		componente.setIdPrograma(programa);
    		TipoComponente tipoComponente = componenteDAO.getTipoComponenteById(componenteSeleccionado.getIdTipoComponente());
    		componente.setTipoComponente(tipoComponente);
    		getEntityManager().merge(componente);
    		
    		List<String> nombreSubtitulos = componenteSeleccionado.getNombreSubtitulos();
    		List <ComponenteSubtitulo> componenteSubtitulos = tipoSubtituloDAO.getByIdComponente(componenteSeleccionado.getIdComponente());
    		for(ComponenteSubtitulo componenteSubtitulo : componenteSubtitulos){
    			getEntityManager().remove(getEntityManager().merge(componenteSubtitulo));
    		}
    		for(String nombreSubtitulo : nombreSubtitulos){
    			TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloByName(nombreSubtitulo);
    			ComponenteSubtitulo newComponenteSubtitulo = new ComponenteSubtitulo();
    			newComponenteSubtitulo.setComponente(componente);
    			newComponenteSubtitulo.setSubtitulo(tipoSubtitulo);
//    			newComponenteSubtitulo.setPesoSubtitulo(Long.parseLong(componenteSeleccionado.getPeso().toString()));
    			getEntityManager().persist(newComponenteSubtitulo);
    		}
    	}
    }
    
    public void create(MantenedorComponenteVO mantenedorComponenteVO){
    	Componente componente = new Componente();
    	
		componente.setNombre(mantenedorComponenteVO.getNombreComponente());
		Programa programa = programasDAO.getProgramaById(mantenedorComponenteVO.getIdPrograma());
		componente.setIdPrograma(programa);
		TipoComponente tipoComponente = componenteDAO.getTipoComponenteById(mantenedorComponenteVO.getIdTipoComponente());
		componente.setTipoComponente(tipoComponente);
		
		getEntityManager().persist(componente);
		
		List<String> nombreSubtitulos = mantenedorComponenteVO.getNombreSubtitulos();
		for(String nombreSubtitulo : nombreSubtitulos){
			TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloByName(nombreSubtitulo);
			ComponenteSubtitulo componenteSubtitulo = new ComponenteSubtitulo();
			componenteSubtitulo.setComponente(componente);
			componenteSubtitulo.setSubtitulo(tipoSubtitulo);
			
			//TODO peso subtitulo, ¿Donde debe ir???
//			componenteSubtitulo.setPesoSubtitulo(Long.parseLong(mantenedorComponenteVO.getPeso().toString()));
			getEntityManager().persist(componenteSubtitulo);
		}
    }
    
    public void remove(MantenedorComponenteVO componenteSeleccionado){
    	Componente componente = componenteDAO.getComponenteByID(componenteSeleccionado.getIdComponente());
    	getEntityManager().remove(getEntityManager().merge(componente));
    	List <ComponenteSubtitulo> componenteSubtitulos = tipoSubtituloDAO.getByIdComponente(componenteSeleccionado.getIdComponente());
		for(ComponenteSubtitulo componenteSubtitulo : componenteSubtitulos){
			getEntityManager().remove(getEntityManager().merge(componenteSubtitulo));
		}
    }
    
}
