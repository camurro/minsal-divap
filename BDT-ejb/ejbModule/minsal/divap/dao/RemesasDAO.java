package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.DetalleRemesas;


@Singleton
public class RemesasDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public List<DetalleRemesas> getRemesasPagadasComunaLaFecha(Integer idProgramaAno, Integer idComuna, Integer idTipoSubtitulo, Integer mes){
		
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasComunaLaFecha",DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mes);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		
	}

	public List<DetalleRemesas> getRemesasPagadasEstablecimiento(Integer idProgramaAno, Integer idEstablecimiento, Integer idTipoSubtitulo){
		
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasEstablecimiento",DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		
	}
	
	public List<DetalleRemesas> findDetalleRemesaByComuna(Integer idComuna, Integer idProgramaAno, Integer mes, Integer idTipoSubtitulo) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.findDetalleRemesaByComuna",DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idMes", mes);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			return query.getResultList();
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}


	public DetalleRemesas save(DetalleRemesas detalleRemesas) {
		em.persist(detalleRemesas);
		return detalleRemesas;
		
	}


	public List<DetalleRemesas> getRemesasByProgramaAnoComponenteEstablecimientoSubtitulo(
			Integer idProgramaAno, Integer idEstablecimiento,
			Integer idTipoSubtitulo) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getDetalleRemesasByProgramaAnoEstablecimientoSubtitulo", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasMesActualByMesProgramaAnoServicioSubtitulo(
			Integer mesCurso, Integer idProgramaAno, Integer servicioSeleccionado, Integer idTipoSubtitulo) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtitulo", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMesDesde", mesCurso);
			query.setParameter("idMesHasta", mesCurso+1);
			query.setParameter("idServicio", servicioSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasByProgramaAnoMesEstablecimientoSubtitulo(
			Integer idProgramaAno, Integer idMes,
			Integer componenteSeleccionado, Integer idSubtitulo,
			String codigoEstablecimiento) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasByProgramaAnoMesEstablecimientoSubtitulo", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", idMes);
			query.setParameter("idComponente",componenteSeleccionado);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("codEstablecimiento", codigoEstablecimiento);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	public DetalleRemesas findDetalleRemesaById(Integer idDetalleRemesa) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.findByIdDetalleRemesa", DetalleRemesas.class);
			query.setParameter("idDetalleRemesa", idDetalleRemesa);
			List<DetalleRemesas> result =  query.getResultList(); 
			if(result.size() > 0){
				return result.get(0);
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void remove(DetalleRemesas remesa) {
		em.remove(remesa);
	}

	public List<DetalleRemesas> getRemesasPagadasComuna(Integer idProgramaAno,
			Integer idComuna, Integer idSubtitulo) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasComuna",DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}


	public List<DetalleRemesas> getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipal(
			Integer mesCurso, Integer idProgramaAno, Integer servicioSeleccionado, Integer idTipoSubtitulo) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipal", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMesDesde", mesCurso);
			query.setParameter("idMesHasta", mesCurso+1);
			query.setParameter("idServicio", servicioSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasMesActualByMesProgramaAnoServicio1(
			Integer mesCurso, Integer idProgramaAno, Integer idServicio) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicio1", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mesCurso);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasMesActualByMesProgramaAnoServicioSubtitulo1(
			Integer mesCurso, Integer idProgramaAno, Integer idServicio, Integer idTipoSubtitulo) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtitulo1", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mesCurso);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public List<DetalleRemesas> getRemesasMesActualByMesProgramaAnoServicio2(
			Integer mesCurso, Integer idProgramaAno, Integer idServicio) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicio2", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mesCurso);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasMesActualByMesProgramaAnoServicioSubtitulo2(
			Integer mesCurso, Integer idProgramaAno, Integer idServicio, Integer idTipoSubtitulo) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtitulo2", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mesCurso);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
