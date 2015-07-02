/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.ProgramasReforzamientoSeguimiento;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author francisco
 */
@Stateless
public class ProgramasReforzamientoSeguimientoFacade extends AbstractFacade<ProgramasReforzamientoSeguimiento> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgramasReforzamientoSeguimientoFacade() {
        super(ProgramasReforzamientoSeguimiento.class);
    }
    
}
