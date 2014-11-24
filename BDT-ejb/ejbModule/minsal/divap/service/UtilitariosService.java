package minsal.divap.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.UtilitariosDAO;
import minsal.divap.enums.TipoComuna;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.MesVO;
import minsal.divap.vo.RegionVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.ServicioSalud;

@Stateless
@LocalBean
public class UtilitariosService {
	
	@EJB
	private UtilitariosDAO utilitariosDAO;
	
	public List<RegionVO> getAllRegion(){
		List<Region> regiones = utilitariosDAO.getAllRegion();
		List<RegionVO> regionesVO = new ArrayList<RegionVO>();
		for(Region region:regiones){
			RegionVO regionVO = new RegionVO();
			regionVO.setIdRegion(region.getId());
			regionVO.setDescRegion(region.getNombre());
			regionesVO.add(regionVO);
		}
		return regionesVO;
	}
	
	public List<ServiciosVO> getAllServicios(){
		List<ServicioSalud> servicios = utilitariosDAO.getServicios();
		List<ServiciosVO> serviciosVO = new ArrayList<ServiciosVO>();
		for(ServicioSalud servicio:servicios){
			ServiciosVO servicioVO = new ServiciosVO();
			servicioVO.setId_servicio(servicio.getId());
			servicioVO.setNombre_servicio(servicio.getNombre());
			serviciosVO.add(servicioVO);
		}
		return serviciosVO;
	}
	
	public List<ServiciosVO> getServiciosByRegion(Integer idRegion){
		List<ServicioSalud> servicios = utilitariosDAO.getServiciosByRegion(idRegion);
		List<ServiciosVO> serviciosVO = new ArrayList<ServiciosVO>();
		for(ServicioSalud servicio:servicios){
			ServiciosVO servicioVO = new ServiciosVO();
			servicioVO.setId_servicio(servicio.getId());
			servicioVO.setNombre_servicio(servicio.getNombre());
			serviciosVO.add(servicioVO);
		}
		return serviciosVO;
	}
	
	public List<ComunaVO> getComunasByServicio(Integer idServicio){
		List<Comuna> comunas = utilitariosDAO.getComunasByServicio(idServicio);
		List<ComunaVO> comunasVO = new ArrayList<ComunaVO>();
		for(Comuna comuna : comunas){
			ComunaVO comunaVO = new ComunaVO();
			comunaVO.setIdComuna(comuna.getId());
			comunaVO.setDescComuna(comuna.getNombre());
			comunasVO.add(comunaVO);
		}
		return comunasVO;
	}
	
		
}
