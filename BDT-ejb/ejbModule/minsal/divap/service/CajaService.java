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
import minsal.divap.model.mappers.CajaMapper;
import minsal.divap.vo.CajaVO;
import minsal.divap.vo.EmailVO;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import cl.minsal.divap.model.Caja;

@Stateless
public class CajaService {

	@EJB
	private CajaDAO cajaDAO;

	public List<Caja> getByProgramaAnoSubtitulo(Integer idPrograma,
			Integer ano, Integer subtitulo) {

		List<Caja> lstCaja = cajaDAO.getByIDProgramaAnoSubtitulo(idPrograma,
				ano, subtitulo);

		return lstCaja;
	}

	public List<CajaVO> getByProgramaAnoSubtituloVO(Integer idPrograma,
			Integer ano, Integer subtitulo) {

		CajaMapper cajaMapper = new CajaMapper();
		List<Caja> lstCaja = cajaDAO.getByIDProgramaAnoSubtitulo(idPrograma,
				ano, subtitulo);
		List<CajaVO> lstCajaVO = new ArrayList<CajaVO>();

		// TODO:Implementar en el Mapper que devuelva la lista.
		for (Caja caja : lstCaja) {
			lstCajaVO.add(cajaMapper.getBasic(caja));
		}

		return lstCajaVO;
	}

	public void save(List<CajaVO> cajaVO) {

		// Crear los objetos de negocio.
		for (CajaVO cajaVO2 : cajaVO) {
			Caja caja = cajaDAO.getByID(cajaVO2.getId());
			caja.setId(cajaVO2.getId());

			caja.setEnero(cajaVO2.getEnero());

			caja.setFebrero(cajaVO2.getFebrero());

			caja.setMarzo(cajaVO2.getMarzo());

			caja.setAbril(cajaVO2.getAbril());

			caja.setMayo(cajaVO2.getMayo());

			caja.setJunio(cajaVO2.getJunio());

			caja.setJulio(cajaVO2.getJulio());

			caja.setAgosto(cajaVO2.getAgosto());

			caja.setSeptiembre(cajaVO2.getSeptiembre());

			caja.setOctubre(cajaVO2.getOctubre());

			caja.setNoviembre(cajaVO2.getNoviembre());

			caja.setDiciembre(cajaVO2.getDiciembre());
			
			caja.setTotal(cajaVO2.getTotal());
			
			cajaDAO.save(caja);

		}

	}

}
