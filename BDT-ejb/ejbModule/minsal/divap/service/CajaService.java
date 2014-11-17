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
import minsal.divap.model.mappers.ServicioMapper;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.RegionVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.ServicioSalud;

@Stateless
@LocalBean
public class CajaService {
	
}