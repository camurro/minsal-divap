package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.List;

import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import cl.redhat.bandejaTareas.controller.BaseController;

public class ReporteMetaDesempenoController extends BaseController implements Serializable{

	private static final long serialVersionUID = -5594583943620049546L;
	
	private List<ServiciosVO> servicios;
	private List<ProgramaVO> programas;

}
