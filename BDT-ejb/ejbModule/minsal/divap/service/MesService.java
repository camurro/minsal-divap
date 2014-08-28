package minsal.divap.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.MesDAO;
import cl.minsal.divap.model.Mes;

@Stateless
@LocalBean
public class MesService {
	@EJB
	private MesDAO mesDAO;


	public Mes getMesPorID(int idMes) {
		Mes mes = this.mesDAO.getMesPorID(idMes);
		
		return mes;
	}

}
