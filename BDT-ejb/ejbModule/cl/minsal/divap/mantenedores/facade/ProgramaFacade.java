/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.AnoDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.vo.MantenedorCuotasVO;
import minsal.divap.vo.MantenedorProgramaVO;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.FechaRemesa;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaComponente;
import cl.minsal.divap.model.ProgramaFechaRemesa;
import cl.minsal.divap.model.Usuario;

/**
 *
 * @author francisco
 */
@Stateless
public class ProgramaFacade extends AbstractFacade<Programa> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private ProgramasDAO programasDAO;
    @EJB
    private UsuarioDAO usuarioDAO;
    @EJB
    private ComponenteDAO componenteDAO;
    @EJB
    private MantenedoresDAO mantenedoresDAO;
    @EJB
    private EstimacionFlujoCajaDAO estimacionFlujoCajaDAO;
    @EJB
    private MesDAO mesDAO;
    @EJB
    private AnoDAO anoDAO;
    @EJB
    private EstimacionFlujoCajaService estimacionFlujoCajaService;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgramaFacade() {
        super(Programa.class);
    }
    
    public void edit(MantenedorProgramaVO seleccionado){
    	Programa programa = null;
    	if(seleccionado.getIdPrograma() == null){
    		programa = new Programa();
    	}else{
    		programa = programasDAO.getProgramaById(seleccionado.getIdPrograma());
    		programa.setNombre(seleccionado.getNombrePrograma().toUpperCase());
    		programa.setCantidadCuotas(seleccionado.getListaCuotas().size());
    		ProgramaAno programaAno = mantenedoresDAO.getProgramaAnoByIdPrograma(seleccionado.getIdPrograma(), seleccionado.getAno());
    		
    		Usuario usuario = usuarioDAO.getUserByUsername(seleccionado.getNombreUsuario());
    		programa.setUsuario(usuario);
    		programa.setDescripcion(seleccionado.getDescripcion());
    		programa.setRevisaFonasa(seleccionado.getFonasa());
    		programa.setReliquidacion(seleccionado.getReliquidacion());
    		getEntityManager().merge(programa);
    		
    		HashMap<String, Boolean> hashMapReiniciarProcesos = seleccionado.getReiniciarProcesos();
    		
    		EstadoPrograma estadoPrograma = programasDAO.getEstadoProgramaByIdEstado(1);
    		
    		if(hashMapReiniciarProcesos.get("reiniciarEstimacionFlujoCaja")){
    			programaAno.setEstadoFlujoCaja(estadoPrograma);
    		}
    		if(hashMapReiniciarProcesos.get("reiniciarConvenio")){
    			programaAno.setEstadoConvenio(estadoPrograma);
    		}
    		if(hashMapReiniciarProcesos.get("reiniciarReliquidacion")){
    			programaAno.setEstadoreliquidacion(estadoPrograma);
    		}
    		if(hashMapReiniciarProcesos.get("reiniciarOT")){
    			programaAno.setEstadoOT(estadoPrograma);
    		}
    		if(hashMapReiniciarProcesos.get("reiniciarDistribucionAps")){
    			programaAno.setEstado(estadoPrograma);
    		}
    		if(hashMapReiniciarProcesos.get("reiniciarModificacionAps")){
    			programaAno.setEstadoModificacionAPS(estadoPrograma);
    		}
    		
    		List<String> componentesNuevo = new ArrayList<String>();
    		List<String> componentesEliminar = new ArrayList<String>();
    		for(String componente : seleccionado.getComponentesActuales()){
    			if(!seleccionado.getComponentes().contains(componente)){
    				componentesEliminar.add(componente);
    			}
    		}
    		
    		for(String componente : seleccionado.getComponentes()){
    			if(!seleccionado.getComponentesActuales().contains(componente)){
    				componentesNuevo.add(componente);
    			}
    		}
    		
    		getEntityManager().merge(programaAno);
    		for(String nombreComponente : componentesNuevo){
    			Componente componente = componenteDAO.getComponenteByNombre(nombreComponente);
    			ProgramaComponente programaComponente = new ProgramaComponente();
    			programaComponente.setComponente(componente);
    			programaComponente.setPrograma(programaAno);
    			getEntityManager().persist(programaComponente);
    		}
    		
    		for(String nombreComponente : componentesEliminar){
    			ProgramaComponente programaComponente = programasDAO.getProgramaComponenteByIdProgramaNombreComponente(programaAno.getIdProgramaAno(), nombreComponente);
    			getEntityManager().remove(getEntityManager().merge(programaComponente));
    		}
    		
    		List<String> diaRemesasNuevos = new ArrayList<String>();
    		List<String> diaRemesasBorrar = new ArrayList<String>();
    		
    		for(String diaRemesa : seleccionado.getDiaPagoRemesasActuales()){
    			if(!seleccionado.getDiaPagoRemesas().contains(diaRemesa)){
    				diaRemesasBorrar.add(diaRemesa);
    			}
    		}
    		for(String diaRemesa : seleccionado.getDiaPagoRemesas()){
    			if(!seleccionado.getDiaPagoRemesasActuales().contains(diaRemesa)){
    				diaRemesasNuevos.add(diaRemesa);
    			}
    		}
    		
    		for(String diaRemesa : diaRemesasNuevos){
    			FechaRemesa fechaRemesa = mantenedoresDAO.getFechaRemesaByDia(Integer.parseInt(diaRemesa));
    			ProgramaFechaRemesa programaFechaRemesa = new ProgramaFechaRemesa();
    			programaFechaRemesa.setFechaRemesa(fechaRemesa);
    			programaFechaRemesa.setPrograma(programa);
    			getEntityManager().persist(programaFechaRemesa);
    		}
    		for(String diaRemesa : diaRemesasBorrar){
    			FechaRemesa fechaRemesa = mantenedoresDAO.getFechaRemesaByDia(Integer.parseInt(diaRemesa));
    			ProgramaFechaRemesa programFechaRemesa = programasDAO.findRemesaByIdProgramaIdFechaRemesa(programa.getId(), fechaRemesa.getId());
    			getEntityManager().remove(getEntityManager().merge(programFechaRemesa));
    		}
    		
    		for(Cuota cuota : mantenedoresDAO.getCuotasByProgramaAno(programaAno.getIdProgramaAno())){
    			getEntityManager().remove(getEntityManager().merge(cuota));
    		}
    		
    		List<MantenedorCuotasVO> cuotasNuevas = new ArrayList<MantenedorCuotasVO>();
    		List<MantenedorCuotasVO> cuotasEliminar = new ArrayList<MantenedorCuotasVO>();
    		List<MantenedorCuotasVO> mantenedorCuotasActuales = new ArrayList<MantenedorCuotasVO>();
    		for(Cuota cuota : estimacionFlujoCajaDAO.getCuotasByProgramaAno(programaAno.getIdProgramaAno())){
    			MantenedorCuotasVO cuotaVO = new MantenedorCuotasVO();
				cuotaVO.setIdCuota(cuota.getId());
				cuotaVO.setNroCuota((int)cuota.getNumeroCuota());
				if(cuota.getFechaPago() != null){
					cuotaVO.setFecha_cuota(cuota.getFechaPago());
				}
				if(cuota.getIdMes() != null){
					cuotaVO.setMes(cuota.getIdMes().getIdMes());
				}
				cuotaVO.setPorcentajeCuota(cuota.getPorcentaje());
				mantenedorCuotasActuales.add(cuotaVO);
    		}
    		
    		
    		
    		for(MantenedorCuotasVO cuotaVO : mantenedorCuotasActuales){
    			if(!seleccionado.getListaCuotas().contains(cuotaVO)){
    				cuotasEliminar.add(cuotaVO);
    			}
    		}
    		for(MantenedorCuotasVO cuotaVO : seleccionado.getListaCuotas()){
    			if(cuotaVO.getIdCuota() == null){
    				cuotasNuevas.add(cuotaVO);
    			}
    		}
    		
    		
    		for(MantenedorCuotasVO mantenedorCuotasVO : cuotasNuevas){
    			Cuota cuota = new Cuota();
    			cuota.setNumeroCuota((short)mantenedorCuotasVO.getNroCuota().intValue());
    			cuota.setPorcentaje(mantenedorCuotasVO.getPorcentajeCuota());
    			cuota.setIdPrograma(programaAno);
    			cuota.setFechaPago(mantenedorCuotasVO.getFecha_cuota());
    			Mes mes = null;
    			if(mantenedorCuotasVO.getMes() != null){
    				mes = mesDAO.getMesPorID(Integer.parseInt(mantenedorCuotasVO.getMes().toString()));
    				cuota.setIdMes(mes);
    			}
    			else{
    				cuota.setIdMes(null);
    			}
    			getEntityManager().persist(cuota);
    		} 
    		
    		for(MantenedorCuotasVO mantenedorCuotasVO : cuotasEliminar){
    			System.out.println("mantenedorCuotasVO-->"+mantenedorCuotasVO.getIdCuota());
    			Cuota cuota = mantenedoresDAO.getCuotaByIdProgramaAnoNroCuota(programaAno.getIdProgramaAno(), (short)mantenedorCuotasVO.getIdCuota().intValue());
    			if(cuota != null){
    				getEntityManager().remove(cuota);
    			}
    		}
    	}
    }
    
    public void create(MantenedorProgramaVO nuevo, Integer anoFinal){
    	Programa programa = new Programa();
    	ProgramaAno programaAno = new ProgramaAno();
    	
    	programa.setNombre(nuevo.getNombrePrograma().toUpperCase());
		programa.setCantidadCuotas(nuevo.getCuotas());
		
		Usuario usuario = usuarioDAO.getUserByUsername(nuevo.getNombreUsuario());
		programa.setUsuario(usuario);
		programa.setDescripcion(nuevo.getDescripcion());
		programa.setRevisaFonasa(nuevo.getFonasa());
		programa.setReliquidacion(nuevo.getReliquidacion());
		programa.setCantidadCuotas(nuevo.getListaCuotas().size());
		getEntityManager().persist(programa);
		
		programaAno.setPrograma(programa);
		AnoEnCurso ano = anoDAO.getAnoById(anoFinal);
		programaAno.setAno(ano);
		EstadoPrograma estadoPrograma = programasDAO.getEstadoProgramaByIdEstado(1);
		programaAno.setEstado(estadoPrograma);
		programaAno.setEstadoConvenio(estadoPrograma);
		programaAno.setEstadoFlujoCaja(estadoPrograma);
		programaAno.setEstadoModificacionAPS(estadoPrograma);
		programaAno.setEstadoOT(estadoPrograma);
		programaAno.setEstadoreliquidacion(estadoPrograma);
		
		getEntityManager().persist(programaAno);
		
		
		for(String nombreComponente : nuevo.getComponentes()){
			Componente componente = componenteDAO.getComponenteByNombre(nombreComponente);
			ProgramaComponente programaComponente = new ProgramaComponente();
			programaComponente.setComponente(componente);
			programaComponente.setPrograma(programaAno);
			getEntityManager().persist(programaComponente);
		}
		
		for(String diaRemesa : nuevo.getDiaPagoRemesas()){
			System.out.println("diaRemesa --> "+diaRemesa);
			FechaRemesa fechaRemesa = mantenedoresDAO.getFechaRemesaByDia(Integer.parseInt(diaRemesa));
			ProgramaFechaRemesa programaFechaRemesa = new ProgramaFechaRemesa();
			programaFechaRemesa.setFechaRemesa(fechaRemesa);
			programaFechaRemesa.setPrograma(programa);
			getEntityManager().persist(programaFechaRemesa);
		}
		int i = 0;
		for(MantenedorCuotasVO mantenedorCuotasVO : nuevo.getListaCuotas()){
			Cuota cuota = new Cuota();
			cuota.setNumeroCuota((short)i);
			cuota.setPorcentaje(mantenedorCuotasVO.getPorcentajeCuota());
			cuota.setIdPrograma(programaAno);
			cuota.setFechaPago(mantenedorCuotasVO.getFecha_cuota());
			Mes mes = null;
			if(mantenedorCuotasVO.getMes() != null){
				mes = mesDAO.getMesPorID(Integer.parseInt(mantenedorCuotasVO.getMes().toString()));
				cuota.setIdMes(mes);
			}
			else{
				cuota.setIdMes(null);
			}
			getEntityManager().persist(cuota);
			i++;
		} 
		
    }
    
    public void remove(MantenedorProgramaVO seleccionado){
    	Programa programa = programasDAO.getProgramaById(seleccionado.getIdPrograma());
    	ProgramaAno programaAno = mantenedoresDAO.getProgramaAnoByIdPrograma(seleccionado.getIdPrograma(), seleccionado.getAno());
    	for(Cuota cuota : mantenedoresDAO.getCuotasByProgramaAno(programaAno.getIdProgramaAno())){
			getEntityManager().remove(getEntityManager().merge(cuota));
		}
    	
    	getEntityManager().remove(getEntityManager().merge(programa));
    }
}
