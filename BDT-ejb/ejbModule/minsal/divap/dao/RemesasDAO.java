package minsal.divap.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.TipoDocumentosProcesos;
import cl.minsal.divap.model.DetalleRemesas;
import cl.minsal.divap.model.DocumentoRemesas;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.Remesas;
import cl.minsal.divap.model.RemesasSeguimiento;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;
import cl.minsal.divap.model.ReporteEmailsEnviados;
import cl.minsal.divap.model.ReporteEmailsRemesas;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.Usuario;


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

	public Integer crearInstanciaOT(Usuario usuario, Mes mes, Date date) {
		Remesas remesas = new Remesas();
		remesas.setFechaCreacion(date);
		remesas.setMes(mes);
		remesas.setUsuario(usuario);
		em.persist(remesas);
		return remesas.getIdRemesa();
	}

	public Integer createSeguimiento(Integer idProcesoOT, Seguimiento seguimiento) {
		Remesas remesa = findById(idProcesoOT);
		RemesasSeguimiento remesasSeguimiento = new RemesasSeguimiento();
		remesasSeguimiento.setInstanciaRemesa(remesa);
		remesasSeguimiento.setSeguimiento(seguimiento);
		this.em.persist(remesasSeguimiento);
		return remesasSeguimiento.getId();
	}

	public Remesas findById(Integer idRemesa) {
		try {
			TypedQuery<Remesas> query = this.em.createNamedQuery("Remesas.findByIdRemesa", Remesas.class);
			query.setParameter("idRemesa", idRemesa);
			List<Remesas> result = query.getResultList();
			if(result!=null && result.size() > 0){
				return result.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public void save(DocumentoRemesas documentoRemesas) {
		em.persist(documentoRemesas);

	}

	public Integer getIdDocumentoRemesa(Integer idProcesoOT,
			TipoDocumentosProcesos idTipoDocumento) {
		try {
			TypedQuery<DocumentoRemesas> query = this.em.createNamedQuery("DocumentoRemesas.findByTipoDocumentoAndRemesa", DocumentoRemesas.class);
			query.setParameter("idRemesa", idProcesoOT);
			query.setParameter("idTipoDocumento", idTipoDocumento.getId());
			List<DocumentoRemesas> result = query.getResultList();
			if(result!=null && result.size() > 0){
				return result.get(0).getDocumento().getId();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public void save(ReporteEmailsEnviados reporteEmailsEnviados) {
		em.persist(reporteEmailsEnviados);

	}

	public void save(ReporteEmailsDestinatarios destinatarioPara) {
		em.persist(destinatarioPara);

	}

	public void save(ReporteEmailsAdjuntos reporteEmailsAdjuntos) {
		em.persist(reporteEmailsAdjuntos);

	}

	public void save(ReporteEmailsRemesas reporteEmailsRemesas) {
		em.persist(reporteEmailsRemesas);

	}

	public List<ReporteEmailsRemesas> getReporteCorreosByidRemesa(
			Integer idProcesoOT) {
		try {
			TypedQuery<ReporteEmailsRemesas> query = this.em.createNamedQuery("ReporteEmailsRemesas.findByIdRemesa", ReporteEmailsRemesas.class);
			query.setParameter("idRemesa", idProcesoOT);
			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	public List<DetalleRemesas> getRemesasPagadasComunaPrograma(Integer idProgramaAno, Integer idComuna, Integer idMes){
		
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasComunaProgramaMesActual",DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("remesaPagada", new Boolean(true));
			query.setParameter("idMes", idMes);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		
	}

	public List<DetalleRemesas> getRemesasComunaLaFecha(Integer idProgramaAno, Integer idComuna, Integer idTipoSubtitulo, Integer mes){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasComunaLaFecha",DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mes);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getDetalleRemesaEstimadaByProgramaAnoEstablecimientoSubtitulo(Integer idProgramaAno, Integer idEstablecimiento, Integer idSubtitulo) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getDetalleRemesaEstimadaByProgramaAnoEstablecimientoSubtitulo", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getDetalleRemesaEstimadaByProgramaAnoComunaSubtitulo(Integer idProgramaAno, Integer idComuna, Integer idSubtitulo) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getDetalleRemesaEstimadaByProgramaAnoComunaSubtitulo", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}
