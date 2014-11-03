package minsal.divap.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.CumplimientoPrograma;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.DocumentoReliquidacion;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.Reliquidacion;
import cl.minsal.divap.model.ReliquidacionComuna;
import cl.minsal.divap.model.Usuario;

@Singleton
public class ReliquidacionDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	@EJB
	private ProgramasDAO programaDAO;
	
	
	public Integer save(DocumentoReliquidacion documentoReliquidacion){
		em.persist(documentoReliquidacion);
		return documentoReliquidacion.getIdDocumentoReliquidacion();
	}


	public Integer crearInstanciaReliquidacion(Usuario usuario,
			Mes mesEnCurso) {
		
		String nombreUsuario = usuario.getUsername();
		try {
			long current = Calendar.getInstance().getTimeInMillis();
			Reliquidacion dto = new Reliquidacion();
			dto.setUsuario(nombreUsuario);
			dto.setMes(mesEnCurso);
			dto.setFechaCreacion(new Date(current));
			this.em.persist(dto);
			return dto.getIdReliquidacion();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public List<CumplimientoPrograma> getCumplimientoProgramaByPrograma(Integer idPrograma) {
		try{
			TypedQuery<CumplimientoPrograma> query = this.em.createNamedQuery("CumplimientoPrograma.findByProgramaByIdPrograma", CumplimientoPrograma.class);
			query.setParameter("idPrograma", idPrograma);
			return  query.getResultList();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public ReliquidacionComuna save(ReliquidacionComuna reliquidacionComuna) {
		em.persist(reliquidacionComuna);
		em.flush();
		return reliquidacionComuna;
	}


	public Reliquidacion getReliquidacionById(Integer idReliquidacion) {
		try{
			TypedQuery<Reliquidacion> query = this.em.createNamedQuery("Reliquidacion.findByIdReliquidacion", Reliquidacion.class);
			query.setParameter("idReliquidacion", idReliquidacion);
			List<Reliquidacion> results = query.getResultList();
			if(results != null && results.size()>0) {
				return results.get(0);
			}
			return null;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Cuota getCuotaByIdProgramaAnoNroCuota(Integer idProgramaAno, Short numeroCuota){
		try{
			TypedQuery<Cuota> query = this.em.createNamedQuery("Cuota.findByIdProgramaAnoNroCuota", Cuota.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("numeroCuota", numeroCuota);
			List<Cuota> results = query.getResultList();
			if(results != null && results.size()>0) {
				return results.get(0);
			}
			return null;
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}		
		
	}
	
	public ReliquidacionComuna getReliquidacionComunaByReliquidacionProgramaComponenteServicioComuna(Integer idPrograma, Integer idComponente, Integer idServicio, Integer idComuna, Integer idReliquidacion){
		try{
			TypedQuery<ReliquidacionComuna> query = this.em.createNamedQuery("ReliquidacionComuna.findByReliquidacionProgramaComponenteServicioComuna", ReliquidacionComuna.class);
			query.setParameter("idPrograma", idPrograma);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idReliquidacion", idReliquidacion);
			List<ReliquidacionComuna> results = query.getResultList();
			if(results != null && results.size()>0) {
				return results.get(0);
			}else{
				System.out.println("no se encuentra");
				return null;
			}

		}
		catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}
	
}
