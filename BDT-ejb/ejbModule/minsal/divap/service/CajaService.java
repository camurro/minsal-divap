package minsal.divap.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import minsal.divap.dao.CajaDAO;
import cl.minsal.divap.model.Caja;

@Stateless
public class CajaService {

	@EJB
	private CajaDAO cajaDAO;

	public List<Caja> getByProgramaAnoSubtitulo(Integer idPrograma, Integer ano, Integer subtitulo)
	{
	
		List<Caja> lstCaja = cajaDAO.getByIDProgramaAnoSubtitulo(idPrograma, ano, subtitulo);
		
		return lstCaja;
	}
	

}
