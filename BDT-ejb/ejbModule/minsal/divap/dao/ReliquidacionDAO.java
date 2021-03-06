package minsal.divap.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.CumplimientoPrograma;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.DocumentoReliquidacion;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.Reliquidacion;
import cl.minsal.divap.model.ReliquidacionComuna;
import cl.minsal.divap.model.ReliquidacionComunaComponente;
import cl.minsal.divap.model.ReliquidacionServicio;
import cl.minsal.divap.model.ReliquidacionServicioComponente;
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


	public Integer crearInstanciaReliquidacion(Usuario usuario, Mes mesEnCurso) {
		try {
			long current = Calendar.getInstance().getTimeInMillis();
			Reliquidacion dto = new Reliquidacion();
			dto.setUsuario(usuario);
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

	public List<ReliquidacionComuna> getReliquidacionComunaByProgramaAnoServicioComponentesReliquidacion(Integer idProgramaAno, Integer idServicio, List<Integer> idComponentes, Integer idReliquidacion){
		try{
			TypedQuery<ReliquidacionComuna> query = this.em.createNamedQuery("ReliquidacionComuna.findByIdProgramaAnoIdServicioIdComponentesIdReliquidacion", ReliquidacionComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idReliquidacion", idReliquidacion);
			return query.getResultList();
		}
		catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}

	public List<ReliquidacionServicio> getReliquidacionServicioByProgramaAnoServicioReliquidacion(Integer idProgramaAno, Integer idServicio, Integer idReliquidacion){
		try{
			TypedQuery<ReliquidacionServicio> query = this.em.createNamedQuery("ReliquidacionServicio.findByIdProgramaAnoIdServicioIdReliquidacion", ReliquidacionServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idReliquidacion", idReliquidacion);
			return query.getResultList();
		}
		catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}

	public ReliquidacionServicio save(ReliquidacionServicio reliquidacionServicio) {
		em.persist(reliquidacionServicio);
		return reliquidacionServicio;
	}

	public int deleteReliquidacionComuna(Integer idProgramaAno) {
		Query queryDeleteReliquidacionComunaComponente = this.em.createNamedQuery("ReliquidacionComunaComponente.deleteByIdProgramaAno");
		queryDeleteReliquidacionComunaComponente.setParameter("idProgramaAno", idProgramaAno);
		queryDeleteReliquidacionComunaComponente.executeUpdate();

		/*Query queryDeleteReliquidacionComuna = this.em.createNamedQuery("ReliquidacionComuna.deleteByIdProgramaAno");
		queryDeleteReliquidacionComuna.setParameter("idProgramaAno", idProgramaAno);
		return queryDeleteReliquidacionComuna.executeUpdate();*/
		return 0;
	}

	public int deleteReliquidacionServicio(Integer idProgramaAno) {
		Query queryDeleteReliquidacionServicioComponente = this.em.createNamedQuery("ReliquidacionServicioComponente.deleteByIdProgramaAno");
		queryDeleteReliquidacionServicioComponente.setParameter("idProgramaAno", idProgramaAno);
		queryDeleteReliquidacionServicioComponente.executeUpdate();

		Query queryDeleteReliquidacionServicio = this.em.createNamedQuery("ReliquidacionServicio.deleteByIdProgramaAno");
		queryDeleteReliquidacionServicio.setParameter("idProgramaAno", idProgramaAno);
		return queryDeleteReliquidacionServicio.executeUpdate();
	}

	public List<Cuota> getCuotasByProgramaAno(Integer idProgramaAno) {
		try{
			TypedQuery<Cuota> query = this.em.createNamedQuery("Cuota.findByIdProgramaAno", Cuota.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			return query.getResultList();
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}	
	}

	public ReliquidacionComuna getReliquidacionComunaByProgramaAnoComunaComponenteReliquidacion(Integer idProgramaAno, Integer idComuna, Integer idComponente, Integer idReliquidacion) {
		try{
			TypedQuery<ReliquidacionComuna> query = this.em.createNamedQuery("ReliquidacionComuna.findByIdProgramaAnoIdComunaIdComponenteIdReliquidacion", ReliquidacionComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idReliquidacion", idReliquidacion);
			List<ReliquidacionComuna> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<ReliquidacionComuna> getReliquidacionComunaByProgramaAnoServicioComponenteReliquidacion(Integer idProgramaAno, Integer idServicio, Integer idComponente, Integer idReliquidacion) {
		try{
			List<Integer> idComponentes = new ArrayList<Integer>();
			TypedQuery<ReliquidacionComuna> query = this.em.createNamedQuery("ReliquidacionComuna.findByIdProgramaAnoIdServicioIdComponentesIdReliquidacion", ReliquidacionComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idReliquidacion", idReliquidacion);
			return query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public ReliquidacionComuna getReliquidacionComunaByProgramaAnoComunaComponenteSubtituloReliquidacion(Integer idProgramaAno, Integer idComuna, Integer idComponente, Integer idSubtitulo, Integer idReliquidacion) {
		try{
			TypedQuery<ReliquidacionComuna> query = this.em.createNamedQuery("ReliquidacionComuna.findByIdProgramaAnoIdComunaIdComponenteIdSubtituloIdReliquidacion", ReliquidacionComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("idReliquidacion", idReliquidacion);
			List<ReliquidacionComuna> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	public ReliquidacionServicio getReliquidacionServicioByProgramaAnoEstablecimientoComponenteSubtituloReliquidacion(Integer idProgramaAno, Integer idEstablecimiento, Integer idComponente, Integer idSubtitulo, Integer idReliquidacion) {
		try{
			TypedQuery<ReliquidacionServicio> query = this.em.createNamedQuery("ReliquidacionServicio.findByIdProgramaAnoIdEstablecimientoIdComponenteIdSubtituloIdReliquidacion", ReliquidacionServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("idReliquidacion", idReliquidacion);
			List<ReliquidacionServicio> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	public ConvenioComuna getConvenioByProgramaAnoComunaMes(Integer idProgramaAno, Integer idComuna, Integer mesActual) {
		try{
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdComunaIdMes", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idMes", mesActual);
			List<ConvenioComuna> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	public List<ReliquidacionServicio> getReliquidacionServicioByProgramaAnoServicioComponente(Integer idProgramaAno, Integer idServicio, Integer idComponente){
		try{
			TypedQuery<ReliquidacionServicio> query = this.em.createNamedQuery("ReliquidacionServicio.findByIdProgramaAnoIdServicioIdComponente", ReliquidacionServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponente", idComponente);
			return query.getResultList();
		}
		catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}

	public ReliquidacionComunaComponente save(ReliquidacionComunaComponente reliquidacionComunaComponente) {
		em.persist(reliquidacionComunaComponente);
		return reliquidacionComunaComponente;
	}

	public ReliquidacionServicioComponente save(ReliquidacionServicioComponente reliquidacionServicioComponente) {
		em.persist(reliquidacionServicioComponente);
		return reliquidacionServicioComponente;
	}

	public List<ReliquidacionComuna> findByIdProgramaAno(Integer idProgramaAno) {
		try{
			TypedQuery<ReliquidacionComuna> query = this.em.createNamedQuery("ReliquidacionComuna.findByIdProgramaAno", ReliquidacionComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			return query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public int deleteByIdReliquidacionComuna(Integer idReliquidacionComuna) {
		Query queryDeleteReliquidacionComunaComponente = this.em.createNamedQuery("ReliquidacionComunaComponente.deleteByIdReliquidacionComuna");
		queryDeleteReliquidacionComunaComponente.setParameter("idReliquidacionComuna", idReliquidacionComuna);
		queryDeleteReliquidacionComunaComponente.executeUpdate();

		Query queryDeleteReliquidacionComuna = this.em.createNamedQuery("ReliquidacionComuna.deleteById");
		queryDeleteReliquidacionComuna.setParameter("idReliquidacionComuna", idReliquidacionComuna);
		return queryDeleteReliquidacionComuna.executeUpdate();
	}

	public List<ReliquidacionServicio> findByReliquidacionServicioIdProgramaAno(Integer idProgramaAno) {
		try{
			TypedQuery<ReliquidacionServicio> query = this.em.createNamedQuery("ReliquidacionServicio.findByIdProgramaAno", ReliquidacionServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			return query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public int deleteByIdReliquidacionServicio(Integer idReliquidacionServicio) {
		Query queryDeleteReliquidacionServicioComponente = this.em.createNamedQuery("ReliquidacionServicioComponente.deleteByIdReliquidacionServicio");
		queryDeleteReliquidacionServicioComponente.setParameter("idReliquidacionServicio", idReliquidacionServicio);
		queryDeleteReliquidacionServicioComponente.executeUpdate();


		Query queryDeleteReliquidacionServicio = this.em.createNamedQuery("ReliquidacionServicio.deleteById");
		queryDeleteReliquidacionServicio.setParameter("idReliquidacionServicio", idReliquidacionServicio);
		return queryDeleteReliquidacionServicio.executeUpdate();
	}


	public ReliquidacionComunaComponente getReliquidacionComunaComponenteByProgramaAnoComunaComponenteSubtituloReliquidacion(Integer idProgramaAno, Integer idComuna, Integer idComponente, Integer idSubtitulo, Integer idReliquidacion) {
		try{

			TypedQuery<ReliquidacionComunaComponente> query = this.em.createNamedQuery("ReliquidacionComunaComponente.findByIdProgramaAnoIdComunaIdComponenteIdSubtituloIdReliquidacion", ReliquidacionComunaComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("idReliquidacion", idReliquidacion);
			List<ReliquidacionComunaComponente> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	public ReliquidacionServicioComponente getReliquidacionServicioComponenteByProgramaAnoEstablecimientoComponenteSubtituloReliquidacion(Integer idProgramaAno, Integer idEstablecimiento, Integer idComponente, Integer idSubtitulo, Integer idReliquidacion) {
		try{
			TypedQuery<ReliquidacionServicioComponente> query = this.em.createNamedQuery("ReliquidacionServicioComponente.findByIdProgramaAnoIdEstablecimientoIdComponenteIdSubtituloIdReliquidacion", ReliquidacionServicioComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("idReliquidacion", idReliquidacion);
			List<ReliquidacionServicioComponente> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Reliquidacion getReliquidacionByProgramaAno(Integer idProgramaAno){
		try{
			TypedQuery<Reliquidacion> query = this.em.createNamedQuery("Reliquidacion.findByIdProgramaAno", Reliquidacion.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			List<Reliquidacion> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
