/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.AnoDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.vo.MantenedorCuotasVO;
import minsal.divap.vo.MantenedorProgramaVO;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
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
    		ProgramaAno programaAno = mantenedoresDAO.getProgramaAnoByIdPrograma(seleccionado.getIdPrograma());
    		
    		Usuario usuario = usuarioDAO.getUserByUsername(seleccionado.getNombreUsuario());
    		programa.setUsuario(usuario);
    		programa.setDescripcion(seleccionado.getDescripcion());
    		programa.setRevisaFonasa(seleccionado.getFonasa());
    		programa.setReliquidacion(seleccionado.getReliquidacion());
    		
    		getEntityManager().merge(programa);
    		
    		for(Cuota cuota : mantenedoresDAO.getCuotasByProgramaAno(programaAno.getIdProgramaAno())){
    			getEntityManager().remove(getEntityManager().merge(cuota));
    		}
    		
    		for(MantenedorCuotasVO mantenedorCuotasVO : seleccionado.getListaCuotas()){
    			   			
    			Cuota cuota = new Cuota();
    			cuota.setNumeroCuota(Short.parseShort(mantenedorCuotasVO.getNroCuota().toString()));
    			cuota.setPorcentaje(mantenedorCuotasVO.getPorcentaje_cuota());
    			cuota.setIdPrograma(programaAno);
    			cuota.setFechaPago(mantenedorCuotasVO.getFecha_cuota());
    			cuota.setMonto(mantenedorCuotasVO.getMonto_cuota());
    			Mes mes = mesDAO.getMesPorID(mantenedorCuotasVO.getMes());
    			cuota.setIdMes(mes);
    			getEntityManager().merge(cuota);
    		}
    		
    	}
    }
    
    public void create(MantenedorProgramaVO nuevo){
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
		AnoEnCurso ano = anoDAO.getAnoById(estimacionFlujoCajaService.getAnoCurso());
		programaAno.setAno(ano);
		
		for(MantenedorCuotasVO mantenedorCuotasVO : nuevo.getListaCuotas()){
			Cuota cuota = new Cuota();
			cuota.setNumeroCuota(Short.parseShort(mantenedorCuotasVO.getNroCuota().toString()));
			cuota.setPorcentaje(mantenedorCuotasVO.getPorcentaje_cuota());
			cuota.setIdPrograma(programaAno);
			cuota.setFechaPago(mantenedorCuotasVO.getFecha_cuota());
			cuota.setMonto(mantenedorCuotasVO.getMonto_cuota());
			Mes mes = null;
			if(mantenedorCuotasVO.getMes() != null){
				mes = mesDAO.getMesPorID(mantenedorCuotasVO.getMes());
				cuota.setIdMes(mes);
			}
			else{
				cuota.setIdMes(null);
			}
			getEntityManager().persist(cuota);
		} 
		
    }
    
    public void remove(MantenedorProgramaVO seleccionado){
    	Programa programa = programasDAO.getProgramaById(seleccionado.getIdPrograma());
    	ProgramaAno programaAno = mantenedoresDAO.getProgramaAnoByIdPrograma(seleccionado.getIdPrograma());
    	for(Cuota cuota : mantenedoresDAO.getCuotasByProgramaAno(programaAno.getIdProgramaAno())){
			getEntityManager().remove(getEntityManager().merge(cuota));
		}
    	
    	getEntityManager().remove(getEntityManager().merge(programa));
    }
}
