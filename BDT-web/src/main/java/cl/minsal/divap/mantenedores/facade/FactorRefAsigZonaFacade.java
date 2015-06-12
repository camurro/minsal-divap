/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import cl.minsal.divap.model.FactorRefAsigZona;
import cl.minsal.divap.model.Persona;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.ServicioSalud;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.vo.MantenedorFactorRefAsigZonaVO;
import minsal.divap.vo.ServiciosVO;

/**
 * 
 * @author francisco
 */
@Stateless
public class FactorRefAsigZonaFacade extends AbstractFacade<FactorRefAsigZona> {
	@PersistenceContext(unitName = "BDT-JPA")
	private EntityManager em;

	@EJB
	private MantenedoresDAO mantenedoresDAO;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public FactorRefAsigZonaFacade() {
		super(FactorRefAsigZona.class);
	}

	public void edit(MantenedorFactorRefAsigZonaVO seleccionado) {
		FactorRefAsigZona factorRefAsigZona = null;
		if (seleccionado.getIdFactorRefAsigZona() == null) {
			factorRefAsigZona = new FactorRefAsigZona();
		} else {
			factorRefAsigZona = mantenedoresDAO
					.getFactorRefAsigZonaById(seleccionado
							.getIdFactorRefAsigZona());
			factorRefAsigZona.setZonaDesde(seleccionado.getZonaDesde());
			factorRefAsigZona.setValor(seleccionado.getValor());
			factorRefAsigZona.setZonaHasta(seleccionado.getZonaHasta());
			getEntityManager().merge(factorRefAsigZona);
		}
	}

	public void create(MantenedorFactorRefAsigZonaVO nuevo) {
		FactorRefAsigZona factorRefAsigZona = new FactorRefAsigZona();
		factorRefAsigZona.setZonaDesde(nuevo.getZonaDesde());
		factorRefAsigZona.setValor(nuevo.getValor());
		factorRefAsigZona.setZonaHasta(nuevo.getZonaHasta());
		getEntityManager().persist(factorRefAsigZona);

	}

	public void createAndChangeUltimo(MantenedorFactorRefAsigZonaVO nuevo,
			String resp, MantenedorFactorRefAsigZonaVO ultimo) {
		FactorRefAsigZona factorRefAsigZonaUltimo = mantenedoresDAO
				.getFactorRefAsigZonaById(ultimo.getIdFactorRefAsigZona());
		factorRefAsigZonaUltimo.setZonaHasta(nuevo.getZonaDesde() - 1);
		getEntityManager().merge(factorRefAsigZonaUltimo);

		FactorRefAsigZona factorRefAsigZona = new FactorRefAsigZona();
		factorRefAsigZona.setZonaDesde(nuevo.getZonaDesde());
		factorRefAsigZona.setValor(nuevo.getValor());
		factorRefAsigZona.setZonaHasta(nuevo.getZonaHasta());
		getEntityManager().persist(factorRefAsigZona);

	}

	public void remove(MantenedorFactorRefAsigZonaVO seleccionado) {
		FactorRefAsigZona factorRefAsigZona = mantenedoresDAO
				.getFactorRefAsigZonaById(seleccionado.getIdFactorRefAsigZona());
		getEntityManager().remove(factorRefAsigZona);
	}

}
