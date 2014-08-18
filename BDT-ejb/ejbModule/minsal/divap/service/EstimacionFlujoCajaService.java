package minsal.divap.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import cl.minsal.divap.model.Usuario;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;

@Stateless
@LocalBean
public class EstimacionFlujoCajaService {
	@EJB
	private EstimacionFlujoCajaDAO estimacionFlujoCajaDAO;
	
	public Integer calcularPropuesta(String username){
		System.out.println("username-->"+username);
		estimacionFlujoCajaDAO.calcularPropuesta();
	return 1;
	}

}
