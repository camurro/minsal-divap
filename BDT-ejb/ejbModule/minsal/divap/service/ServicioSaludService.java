package minsal.divap.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.enums.TipoComuna;
import minsal.divap.model.mappers.PersonaMapper;
import minsal.divap.model.mappers.RegionMapper;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.RegionVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.ServicioSalud;

@Stateless
@LocalBean
public class ServicioSaludService {
	@EJB
	private ServicioSaludDAO servicioSaludDAO;

	public List<BaseVO> getAllServicios() {
		List<ServicioSalud> serviciosSalud = this.servicioSaludDAO.getServicios();
		List<BaseVO> result = new ArrayList<BaseVO>();
		if(serviciosSalud != null){
			for (ServicioSalud servicioSalud : serviciosSalud){
				for (Comuna comuna : servicioSalud.getComunas()){
					BaseVO baseVO = new BaseVO();
					baseVO.setRegion(((servicioSalud.getRegion() != null)?servicioSalud.getRegion().getId():null));
					baseVO.setServicio(((servicioSalud.getNombre() != null)?servicioSalud.getNombre():null));
					baseVO.setComuna(((comuna != null)?comuna.getNombre():null));
					result.add(baseVO);
				}
			}
		}
		return result;
	}


	public List<ServiciosVO> getAllServiciosVO() {
		List<ServicioSalud> serviciosSalud = this.servicioSaludDAO.getServicios();
		List<ServiciosVO> result = new ArrayList<ServiciosVO>();
		if(serviciosSalud != null){
			for (ServicioSalud servicioSalud : serviciosSalud){
				ServiciosVO servicioVO = new ServiciosVO(servicioSalud.getId(), servicioSalud.getNombre());
				servicioVO.setDirector(new PersonaMapper().getBasic(servicioSalud.getDirector()));
				servicioVO.setEncargadoAps(new PersonaMapper().getBasic(servicioSalud.getEncargadoAps()));
				servicioVO.setEncargadoFinanzasAps(new PersonaMapper().getBasic(servicioSalud.getEncargadoFinanzasAps()));
				result.add(servicioVO);
			}
		}
		return result;
	}


	public List<BaseVO> getAllServiciosComunasCFUrbanaRural() {
		List<AntecendentesComuna> antecendentesComunas = this.servicioSaludDAO.getAntecedentesComunaPercapita(getAnoCurso(), new TipoComuna[] { TipoComuna.RURAL, TipoComuna.URBANA, TipoComuna.COSTOFIJO});
		List<BaseVO> result = new ArrayList<BaseVO>();
		if(antecendentesComunas != null && antecendentesComunas.size() > 0){
			for (AntecendentesComuna antecendentesComuna : antecendentesComunas){
				BaseVO baseVO = new BaseVO();
				baseVO.setRegion(((antecendentesComuna.getIdComuna().getServicioSalud().getRegion()!= null)?antecendentesComuna.getIdComuna().getServicioSalud().getRegion().getId():null));
				baseVO.setServicio(((antecendentesComuna.getIdComuna().getServicioSalud().getNombre()!= null)?antecendentesComuna.getIdComuna().getServicioSalud().getNombre():null));
				baseVO.setComuna(((antecendentesComuna.getIdComuna()!= null)?antecendentesComuna.getIdComuna().getNombre():null));
				result.add(baseVO);
			}
		}
		return result;
	}
	
	public ServicioSalud getServicioSaludPorID(int idServicioSalud) {
		ServicioSalud servicioSalud = this.servicioSaludDAO.getServicioSaludPorID(idServicioSalud);
		return servicioSalud;
	}
	
	public List<RegionVO> getAllRegionesVO() {
		List<Region> regiones = this.servicioSaludDAO.getAllRegion();
		List<RegionVO> result = new ArrayList<RegionVO>();
		if(regiones != null && regiones.size() >0){
			for (Region region : regiones){
				result.add(new RegionMapper().getBasic(region));
			}
		}
		return result;
	}

	private Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}

}
