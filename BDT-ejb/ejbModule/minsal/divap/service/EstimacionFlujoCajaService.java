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
		
		switch (subtitulo) {
		case 21:
		case 22:
		case 24:
		case 29:
			if (subtitulo == 24){ 
				
			//PERCAPITA
			Double marcoPresupuestario = (Double) 0.0; //Obtener cuota desde la BD
			Double cuota = marcoPresupuestario / 12;
			
			//LEYES
			//Distribucion Mensual (Igual que Percapita)
			//Segun Demanda
			}
			
			//TODO: Explicacion del algoritmo Programas de Reoforzamiento
			
			
			
			break;
		default:
			break;
		}
		if (subtitulo == 24) //Percapita
		{
			//Cuota n [mes de programación a diciembre] de un SS = MP[actualizado] / 12
		
			//Se guarda el valor para todos los programas.
			
		}
		else if (subtitulo == 21 || subtitulo == 22 || subtitulo ==24 || subtitulo == 29)
		{
		
		}
//			21, 22, 24, 29)
//		
//		
//		estimacionFlujoCajaDAO.calcularPropuesta();
	return 1;
	}

}
