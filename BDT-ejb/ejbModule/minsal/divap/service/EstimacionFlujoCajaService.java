package minsal.divap.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import cl.minsal.divap.model.Usuario;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.enums.Subtitulo;

@Stateless
@LocalBean
public class EstimacionFlujoCajaService {
	@EJB
	private EstimacionFlujoCajaDAO estimacionFlujoCajaDAO;
	
	public Integer calcularPropuesta(Integer idPrograma){
		//System.out.println("username-->"+username);
		//Obtengo la lista de datos para realizar los calculos correspondientes.
		int subtitulo = 0;
		if (subtitulo == 24) //Percapita
		{
		
			
			
		}
//			
//		
//		
//		estimacionFlujoCajaDAO.calcularPropuesta();
	return 1;
	}

}
