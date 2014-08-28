package minsal.divap.service;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.vo.EmailVO;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

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
