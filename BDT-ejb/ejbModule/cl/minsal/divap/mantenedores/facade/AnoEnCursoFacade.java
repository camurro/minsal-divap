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

import minsal.divap.dao.AnoDAO;
import minsal.divap.vo.MantenedorAnoVO;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.EstadoPrograma;

/**
 *
 * @author francisco
 */
@Stateless
public class AnoEnCursoFacade extends AbstractFacade<AnoEnCurso> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private AnoDAO anoDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AnoEnCursoFacade() {
        super(AnoEnCurso.class);
    }
    
    public void edit(MantenedorAnoVO seleccionado){
    	AnoEnCurso anoEnCurso = null;
    	if(seleccionado.getAno()== null){
    		anoEnCurso = new AnoEnCurso();
    	}else{
    		anoEnCurso = anoDAO.getAnoById(seleccionado.getAno());
    		anoEnCurso.setMontoPercapitalBasal(seleccionado.getMontoPercapitalBasal());
    		anoEnCurso.setAsignacionAdultoMayor(seleccionado.getAsignacionAdultoMayor());
    		anoEnCurso.setInflactor(seleccionado.getInflactor());
    		anoEnCurso.setInflactorMarcoPresupuestario(1.0);
    		getEntityManager().merge(anoEnCurso);
    	}
    }
    
    public void create(MantenedorAnoVO nuevo){
    	AnoEnCurso anoEnCurso = new AnoEnCurso();
    	anoEnCurso.setAno(nuevo.getAno());
    	anoEnCurso.setMontoPercapitalBasal(nuevo.getMontoPercapitalBasal());
		anoEnCurso.setAsignacionAdultoMayor(nuevo.getAsignacionAdultoMayor());
		anoEnCurso.setInflactor(nuevo.getInflactor());
		anoEnCurso.setInflactorMarcoPresupuestario(1.0);
		getEntityManager().persist(anoEnCurso);
    }
    
    public void remove(MantenedorAnoVO seleccionado){
    	AnoEnCurso anoEnCurso = anoDAO.getAnoById(seleccionado.getAno());
    	getEntityManager().remove(getEntityManager().merge(anoEnCurso));
    }
    
}
