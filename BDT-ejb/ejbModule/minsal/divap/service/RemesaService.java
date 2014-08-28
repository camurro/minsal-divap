package minsal.divap.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.RemesaDAO;
import cl.minsal.divap.model.Remesa;

@Stateless
@LocalBean
public class RemesaService {
	@EJB
	private RemesaDAO remesaDAO;


	public Integer crearRemesa(Remesa remesa) {
		return this.remesaDAO.crearRemesa(remesa);
	}
	
	public void crearRemesa(List<Remesa> listaRemesa) {
	for (Remesa remesa : listaRemesa) {
		
			this.remesaDAO.crearRemesa(remesa);
		}
	}

	public Integer actualizarRemesa(Remesa remesa) {
		return this.remesaDAO.actualizarRemesa(remesa);

	}
	
	public void actualizarRemesa(List<Remesa> listaRemesa) {

		for (Remesa remesa : listaRemesa) {
			
			this.remesaDAO.actualizarRemesa(remesa);
		}

	}

	
	public void guardarDatosProcesoRevisarOT(List<Remesa> listaRemesa) {

		for (Remesa remesa : listaRemesa) {
			
			if(remesa.getIdRemesa()!= null && remesa.getIdRemesa().intValue() > 0)
			{
				this.remesaDAO.actualizarRemesa(remesa);
			}
			else
			{
				this.remesaDAO.crearRemesa(remesa);
			}
		}
	}
	
	
	

}
