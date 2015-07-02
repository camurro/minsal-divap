/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.ComunaCumplimientoRebaja;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author francisco
 */
@Stateless
public class ComunaCumplimientoRebajaFacade extends AbstractFacade<ComunaCumplimientoRebaja> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComunaCumplimientoRebajaFacade() {
        super(ComunaCumplimientoRebaja.class);
    }
    
}
