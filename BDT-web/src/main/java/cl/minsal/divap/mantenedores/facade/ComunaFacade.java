/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.FactorRefAsigZona;
import cl.minsal.divap.model.FactorTramoPobreza;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoComuna;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.vo.MantenedorComunaFinalVO;
import minsal.divap.vo.MantenedorComunaVO;

/**
 *
 * @author francisco
 */
@Stateless
public class ComunaFacade extends AbstractFacade<Comuna> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private ComunaDAO comunaDAO;
    @EJB
    private AntecedentesComunaDAO antecedentesComunaDAO;
    @EJB
    private ServicioSaludDAO servicioSaludDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComunaFacade() {
        super(Comuna.class);
    }

	public void edit(MantenedorComunaFinalVO comunaSeleccionada, Integer anoFinal) {
		AntecendentesComuna antecedentesComuna = null;
		
		if(comunaSeleccionada.getIdAntecedentesComuna() == null ){
			antecedentesComuna = new AntecendentesComuna();
		}else{
			antecedentesComuna = antecedentesComunaDAO.getAntecendentesComunaByComunaAno(comunaSeleccionada.getIdComuna(), comunaSeleccionada.getAno());
		}
		AnoEnCurso ano = new AnoEnCurso();
		ano.setAno(comunaSeleccionada.getAno());
		antecedentesComuna.setAnoAnoEnCurso(ano);
		
		antecedentesComuna.setAsignacionZona(new FactorRefAsigZona(comunaSeleccionada.getIdAsigZona(), comunaSeleccionada.getAsigZonaValor()));
		antecedentesComuna.setTramoPobreza(new FactorTramoPobreza(comunaSeleccionada.getIdTramoPobreza(), comunaSeleccionada.getTramoPobreza()));
		
		Comuna comuna = comunaDAO.getComunaById(comunaSeleccionada.getIdComuna());
		antecedentesComuna.setIdComuna(comuna);
		
		antecedentesComuna.setClasificacion(new TipoComuna(comunaSeleccionada.getIdClasificacion(), comunaSeleccionada.getClasificacion()));
		
		getEntityManager().merge(antecedentesComuna);
		
	}
	

	public void remove(MantenedorComunaFinalVO comunaSeleccionada) {
		AntecendentesComuna antecedentesComuna = null;
		antecedentesComuna = antecedentesComunaDAO.getAntecendentesComunaByComunaAno(comunaSeleccionada.getIdComuna(), comunaSeleccionada.getAno());
		getEntityManager().remove(getEntityManager().merge(antecedentesComuna));
		
		Comuna comuna = comunaDAO.getComunaById(comunaSeleccionada.getIdComuna());
		getEntityManager().remove(getEntityManager().merge(comuna));
	}
	
	public void create(MantenedorComunaFinalVO comunaNueva, Integer anoFinal) {
		AntecendentesComuna antecedentesComuna = null;
		if(comunaNueva.getIdAntecedentesComuna() == null ){
			antecedentesComuna = new AntecendentesComuna();
		}else{
			antecedentesComuna = antecedentesComunaDAO.getAntecendentesComunaByComunaAno(comunaNueva.getIdComuna(), comunaNueva.getAno());
		}
		AnoEnCurso ano = new AnoEnCurso();
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		ano.setAno(anoFinal);
		antecedentesComuna.setAnoAnoEnCurso(ano);
		
		FactorRefAsigZona factorRefAsigZona = antecedentesComunaDAO.findFactorRefAsigZonaById(comunaNueva.getIdAsigZona());
		antecedentesComuna.setAsignacionZona(factorRefAsigZona);
		
		FactorTramoPobreza factorTramoPobreza = antecedentesComunaDAO.findFactorTramoPobrezaById(comunaNueva.getIdTramoPobreza());
		antecedentesComuna.setTramoPobreza(factorTramoPobreza);
		
//		Comuna comuna = comunaDAO.getComunaById(comunaNueva.getIdComuna());
		Comuna comuna = new Comuna();
		comuna.setNombre(comunaNueva.getNombreComuna());
		//TODO ver este tema
		comuna.setAuxiliar(false);
		ServicioSalud servicio = servicioSaludDAO.getById(comunaNueva.getIdServicio());
		comuna.setServicioSalud(servicio);
		getEntityManager().persist(comuna);
		
		antecedentesComuna.setIdComuna(comuna);
		
		TipoComuna tipoComuna = antecedentesComunaDAO.findTipoComunaById(comunaNueva.getIdClasificacion());
		antecedentesComuna.setClasificacion(tipoComuna);
		
        getEntityManager().persist(antecedentesComuna);
    }
    
}
