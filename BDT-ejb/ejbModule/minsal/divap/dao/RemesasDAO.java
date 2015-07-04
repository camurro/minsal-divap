package minsal.divap.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import minsal.divap.enums.TipoDocumentosProcesos;
import cl.minsal.divap.model.DetalleRemesas;
import cl.minsal.divap.model.DocumentoRemesas;
import cl.minsal.divap.model.FechaRemesa;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.RemesaConvenios;
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

	public List<DetalleRemesas> getRemesasPagadasByProgramaAnoComponenteEstablecimientoSubtitulo(Integer idProgramaAno, Integer idComponente, Integer idEstablecimiento, Integer idTipoSubtitulo){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasByProgramaAnoComponenteEstablecimientoSubtituloPagada", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	

	public List<DetalleRemesas> getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo(Integer idProgramaAno, Integer idComponente, Integer idComuna, Integer idTipoSubtitulo){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPendientesByProgramaAnoComponenteEstablecimientoSubtitulo(Integer idProgramaAno, Integer idComponente, Integer idEstablecimiento, Integer idTipoSubtitulo){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasByProgramaAnoComponenteEstablecimientoSubtituloPagada", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(false));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPendientesByProgramaAnoComponenteComunaSubtitulo(Integer idProgramaAno, Integer idComponente, Integer idComuna, Integer idTipoSubtitulo){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasByProgramaAnoComponenteComunaSubtituloPagada", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(false));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPendientesByProgramaAnoComponenteComunaSubtituloDiaMes(Integer idProgramaAno, Integer idComponente, Integer idComuna, Integer idTipoSubtitulo, Integer dia, Integer mes){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasByProgramaAnoComponenteComunaSubtituloDiaMesPagada", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("idDia", dia);
			query.setParameter("idMes", mes);
			query.setParameter("remesaPagada", new Boolean(false));
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
			Integer idProgramaAno, Integer idEstablecimiento, Integer idTipoSubtitulo) {
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
			Integer mesCurso, Integer idProgramaAno, Integer servicioSeleccionado, Integer idTipoSubtitulo, Boolean revisarConsolidador) {
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
	
	public List<DetalleRemesas> getRemesasMesActualByMesProgramaAnoServicioSubtituloConsolidador(
			Integer mesCurso, Integer idProgramaAno, Integer servicioSeleccionado, Integer idTipoSubtitulo, Boolean revisarConsolidador) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtituloConsolidador", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMesDesde", mesCurso);
			query.setParameter("idMesHasta", mesCurso+1);
			query.setParameter("idServicio", servicioSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("revisarConsolidador", revisarConsolidador);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasByProgramaAnoMesEstablecimientoSubtitulo(
			Integer idProgramaAno, Integer idMes, Integer componenteSeleccionado, Integer idSubtitulo, String codigoEstablecimiento) {
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

	public List<DetalleRemesas> getRemesasPagadasComunaddd(Integer idProgramaAno,
			Integer idComuna, Integer idSubtitulo) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasComuna", DetalleRemesas.class);
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
			Integer mesCurso, Integer idProgramaAno, Integer servicioSeleccionado, Integer idTipoSubtitulo, Integer programa) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipal", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMesDesde", mesCurso);
			query.setParameter("idMesHasta", mesCurso+1);
			query.setParameter("idServicio", servicioSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			//query.setParameter("programa", programa);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipalConsolidador(
			Integer mesCurso, Integer idProgramaAno, Integer servicioSeleccionado, Integer idTipoSubtitulo, Integer programa, Boolean revisarConsolidador) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipalConsolidador", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMesDesde", mesCurso);
			query.setParameter("idMesHasta", mesCurso+1);
			query.setParameter("idServicio", servicioSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("revisarConsolidador", revisarConsolidador);
			//query.setParameter("programa", programa);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasServicioAprobadosConsolidadorDiaMesProgramaAnoServicio(Integer dia, Integer mesCurso, Integer idProgramaAno, Integer idServicio) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasServicioAprobadasConsolidadorByDiaMesProgramaAnoServicio", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mesCurso);
			query.setParameter("idDia", dia);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasMesActualByMesProgramaAnoServicioSubtitulo1(Integer mesCurso, Integer idProgramaAno, Integer idServicio, Integer idTipoSubtitulo, Boolean remesaPagada) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtitulo1", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mesCurso);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", remesaPagada);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasMesActualByDiaMesProgramaAnoServicioSubtitulo1(Integer dia, Integer mesCurso, Integer idProgramaAno, Integer idServicio, Integer idTipoSubtitulo, Boolean remesaPagada) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByDiaMesProgramaAnoServicioSubtitulo1", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mesCurso);
			query.setParameter("idDia", dia);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", remesaPagada);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasComunaAprobadosConsolidadorDiaMesProgramaAnoServicio(Integer dia, Integer mesCurso, Integer idProgramaAno, Integer idServicio) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasComunaAprobadosConsolidadorDiaMesProgramaAnoServicio", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mesCurso);
			query.setParameter("idDia", dia);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasMesActualByDiaMesProgramaAnoServicioSubtitulo2(Integer dia, Integer mesCurso, Integer idProgramaAno, Integer idServicio, Integer idTipoSubtitulo, Boolean remesaPagada) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasMesActualByDiaMesProgramaAnoServicioSubtitulo2", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mesCurso);
			query.setParameter("idDia", dia);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", remesaPagada);
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

	public Integer getIdDocumentoRemesa(Integer idProcesoOT, TipoDocumentosProcesos idTipoDocumento) {
		try {
			TypedQuery<DocumentoRemesas> query = this.em.createNamedQuery("DocumentoRemesas.findByTipoDocumentoAndRemesa", DocumentoRemesas.class);
			query.setParameter("idRemesa", idProcesoOT);
			query.setParameter("idTipoDocumento", idTipoDocumento.getId());
			List<DocumentoRemesas> result = query.getResultList();
			if(result != null && result.size() > 0){
				return result.get(0).getDocumento().getId();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	public Integer getDocumentoFinalRemesa(Integer idProcesoOT, TipoDocumentosProcesos idTipoDocumento) {
		try {
			TypedQuery<DocumentoRemesas> query = this.em.createNamedQuery("DocumentoRemesas.findByTipoDocumentoFinalAndRemesa", DocumentoRemesas.class);
			query.setParameter("idRemesa", idProcesoOT);
			query.setParameter("idTipoDocumento", idTipoDocumento.getId());
			List<DocumentoRemesas> result = query.getResultList();
			if(result != null && result.size() > 0){
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

	public List<ReporteEmailsRemesas> getReporteCorreosByidRemesa(Integer idProcesoOT) {
		try {
			TypedQuery<ReporteEmailsRemesas> query = this.em.createNamedQuery("ReporteEmailsRemesas.findByIdRemesa", ReporteEmailsRemesas.class);
			query.setParameter("idRemesa", idProcesoOT);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<ReporteEmailsRemesas> getReporteCorreosByRemesaServicio(Integer idProcesoOT, Integer idServicio) {
		try {
			TypedQuery<ReporteEmailsRemesas> query = this.em.createNamedQuery("ReporteEmailsRemesas.findByIdRemesaIdServicio", ReporteEmailsRemesas.class);
			query.setParameter("idRemesa", idProcesoOT);
			query.setParameter("idServicio", idServicio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Long getRemesasPagadasComunaPrograma(Integer idProgramaAno, Integer idComuna, Integer idMes){
		try{
			Query query = this.em.createNamedQuery("DetalleRemesas.groupMontoRemesaProgramaComuna");
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("mes", idMes);
			Object[] results = (Object[]) query.getSingleResult();
			if(results != null && results.length > 1){
				return ((Number)results[1]).longValue();
			}
			return 0L;
		}catch (NoResultException noResultException) {
			return 0L;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public Long getRemesasPagadasComunaProgramaSubtitulo(Integer idProgramaAno, Integer idSubtitulo, Integer idComuna, Integer idMes){
		try{
			Query query = this.em.createNamedQuery("DetalleRemesas.groupMontoRemesaProgramaSubtituloComuna");
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("mes", idMes);
			query.setParameter("idSubtitulo", idSubtitulo);
			Object[] results = (Object[]) query.getSingleResult();
			if(results != null && results.length > 1){
				return ((Number)results[1]).longValue();
			}
			return 0L;
		}catch (NoResultException noResultException) {
			return 0L;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public Long getRemesasPagadasComunaProgramaComponenteSubtitulo(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idComuna, Integer idMes){
		try{
			Query query = this.em.createNamedQuery("DetalleRemesas.groupMontoRemesaProgramaComponenteSubtituloComuna");
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idComuna", idComuna);
			query.setParameter("mes", idMes);
			query.setParameter("idSubtitulo", idSubtitulo);
			Object[] results = (Object[]) query.getSingleResult();
			if(results != null && results.length > 1){
				return ((Number)results[1]).longValue();
			}
			return 0L;
		}catch (NoResultException noResultException) {
			return 0L;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public Long getRemesasPagadasEstablecimientoProgramaSubtitulo(Integer idProgramaAno, Integer idSubtitulo, Integer idEstablecimiento, Integer idMes){
		try{
			Query query = this.em.createNamedQuery("DetalleRemesas.groupMontoRemesaProgramaSubtituloEstablecimiento");
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("mes", idMes);
			query.setParameter("idSubtitulo", idSubtitulo);
			Object[] results = (Object[]) query.getSingleResult();
			if(results != null && results.length > 1){
				return ((Number)results[1]).longValue();
			}
			return 0L;
		}catch (NoResultException noResultException) {
			return 0L;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public Long getRemesasPagadasEstablecimientoProgramaComponenteSubtituloDiaMes(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idEstablecimiento, Integer idDia, Integer idMes){
		try{
			Query query = this.em.createNamedQuery("DetalleRemesas.groupMontoRemesaProgramaComponenteSubtituloEstablecimientoDiaMes");
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("mes", idMes);
			query.setParameter("dia", idDia);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idSubtitulo", idSubtitulo);
			Object[] results = (Object[]) query.getSingleResult();
			if(results != null && results.length > 1){
				return ((Number)results[1]).longValue();
			}
			return 0L;
		}catch (NoResultException noResultException) {
			return 0L;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public Long getRemesasPagadasComunaProgramaComponenteSubtituloDiaMes(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idComuna, Integer idDia, Integer idMes){
		try{
			Query query = this.em.createNamedQuery("DetalleRemesas.groupMontoRemesaProgramaComponenteSubtituloComunaDiaMes");
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("mes", idMes);
			query.setParameter("dia", idDia);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idSubtitulo", idSubtitulo);
			Object[] results = (Object[]) query.getSingleResult();
			if(results != null && results.length > 1){
				return ((Number)results[1]).longValue();
			}
			return 0L;
		}catch (NoResultException noResultException) {
			return 0L;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public Long getRemesasPagadasEstablecimientoProgramaComponenteSubtitulo(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idEstablecimiento, Integer idMes){
		try{
			Query query = this.em.createNamedQuery("DetalleRemesas.groupMontoRemesaProgramaComponenteSubtituloEstablecimiento");
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("mes", idMes);
			query.setParameter("idSubtitulo", idSubtitulo);
			Object[] results = (Object[]) query.getSingleResult();
			if(results != null && results.length > 1){
				return ((Number)results[1]).longValue();
			}
			return 0L;
		}catch (NoResultException noResultException) {
			return 0L;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasComunaLaFecha(Integer idProgramaAno, Integer idComuna, Integer idTipoSubtitulo, Integer mes){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasComunaLaFecha", DetalleRemesas.class);
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

	public Long getRemesasPagadasEstablecimientoPrograma(Integer idProgramaAno, Integer idEstablecimiento, Integer idMes){
		try{
			Query query = this.em.createNamedQuery("DetalleRemesas.groupMontoRemesaProgramaEstablecimiento");
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("mes", idMes);
			Object[] results = (Object[]) query.getSingleResult();
			if(results != null && results.length > 1){
				return ((Number)results[1]).longValue();
			}
			return 0L;
		}catch (NoResultException noResultException) {
			return 0L;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public Long getRemesasNoPagadasEstablecimientoProgramaSubtitulo(Integer idProgramaAno, Integer idSubtitulo, Integer idEstablecimiento, Integer idMes){
		try{
			Query query = this.em.createNamedQuery("DetalleRemesas.groupMontoRemesaNoPagadasProgramaSubtituloEstablecimiento");
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("mes", idMes);
			Object[] results = (Object[]) query.getSingleResult();
			if(results != null && results.length > 1){
				return ((Number)results[1]).longValue();
			}
			return 0L;
		}catch (NoResultException noResultException) {
			return 0L;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public Long getRemesasNoPagadasComunaProgramaSubtituloMesHasta(Integer idProgramaAno, Integer idSubtitulo, Integer idComuna, Integer idMes){
		try{
			Query query = this.em.createNamedQuery("DetalleRemesas.groupMontoRemesaNoPagadasProgramaSubtituloComunaMesHasta");
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("idComuna", idComuna);
			query.setParameter("mes", idMes);
			query.setParameter("idSubtitulo", idSubtitulo);
			Object[] results = (Object[]) query.getSingleResult();
			if(results != null && results.length > 1){
				return ((Number)results[1]).longValue();
			}
			return 0L;
		}catch (NoResultException noResultException) {
			return 0L;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasPagadasComunasProgramaAnoServicioSubtituloMesHasta(Integer idProgramaAno, Integer idServicio, Integer idTipoSubtitulo, Integer mes){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasComunasProgramaAnoServicioSubtituloMesHasta", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mes);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPagadasComunasProgramaAnoServicioComponenteSubtituloMesHasta(Integer idProgramaAno, Integer idServicio, Integer idComponente, Integer idTipoSubtitulo, Integer mes){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasComunasProgramaAnoServicioComponenteSubtituloMesHasta", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mes);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesHasta(Integer idProgramaAno, Integer idServicio, Integer idTipoSubtitulo, Integer mes){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesHasta",DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mes);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}

	}

	public List<DetalleRemesas> getRemesasPagadasEstablecimientosProgramaAnoServicioComponenteSubtituloMesHasta(Integer idProgramaAno, Integer idServicio, Integer idComponente, Integer idTipoSubtitulo, Integer mes){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasEstablecimientosProgramaAnoServicioComponenteSubtituloMesHasta", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mes);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			query.setParameter("idComponente", idComponente);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPagadasEstablecimientosProgramaAnoEstablecimientoComponenteSubtituloMesHasta(Integer idProgramaAno, Integer idEstablecimiento, Integer idComponente, Integer idTipoSubtitulo, Integer mes){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasEstablecimientosProgramaAnoEstablecimientoComponenteSubtituloMesHasta", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mes);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			query.setParameter("idComponente", idComponente);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPagadasComunasProgramaAnoServicioSubtituloMesActual(Integer idProgramaAno, Integer idServicio, Integer idTipoSubtitulo, Integer mes){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasComunasProgramaAnoServicioSubtituloMesActual", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mes);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesActual(Integer idProgramaAno, Integer idServicio, Integer idTipoSubtitulo, Integer mes){
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesActual", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mes);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasComunaSubtituloCuota(Integer idProgramaAno, Integer idComuna, Integer idSubtitulo, Integer cuota) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasComunaSubtituloCuota", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("numeroCuota", cuota);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasNoPagadasComunaSubtituloCuota(Integer idProgramaAno, Integer idComuna, Integer idSubtitulo, Integer numeroCuota) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasNoPagadasComunaSubtituloCuota", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("numeroCuota", numeroCuota);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getDetalleRemesaByProgramaAnoComunaSubtitulo(Integer idProgramaAno, Integer idComuna, Integer idSubtitulo) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasComunaSubtitulo", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getDetalleRemesaByProgramaAnoComponenteEstablecimientoSubtitulo(Integer idProgramaAno, Integer idComponente, Integer idEstablecimiento, Integer idSubtitulo) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasProgramaAnoComponenteSubtituloEstablecimiento", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public List<DetalleRemesas> getRemesasPorPagarDiaMesActual(Integer idDia, Integer idMes) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPorPagarDiaMesActual", DetalleRemesas.class);
			query.setParameter("remesaPagada", Boolean.FALSE);
			query.setParameter("idMes", idMes);
			query.setParameter("idDia", idDia);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPagadasComunasProcesoServicioProgramaSubtituloDiaMesActual(Integer idProceso, Integer servicio, Integer programaAno, Integer subtitulo, Integer idDia, Integer idMes) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasComunasProcesoServicioProgramaSubtituloDiaMes", DetalleRemesas.class);
			query.setParameter("idMes", idMes);
			query.setParameter("idDia", idDia);
			query.setParameter("idProceso", idProceso);
			query.setParameter("idServicio", servicio);
			query.setParameter("idProgramaAno", programaAno);
			query.setParameter("idSubtitulo", subtitulo);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPagadasComunasProcesoComunaProgramaSubtituloDiaMesActual(Integer idProceso, Integer comuna, Integer programaAno, Integer subtitulo, Integer idDia, Integer idMes) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasComunasProcesoComunaProgramaSubtituloDiaMes", DetalleRemesas.class);
			query.setParameter("idMes", idMes);
			query.setParameter("idDia", idDia);
			query.setParameter("idProceso", idProceso);
			query.setParameter("idComuna", comuna);
			query.setParameter("idProgramaAno", programaAno);
			query.setParameter("idSubtitulo", subtitulo);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPagadasEstablecimientosProcesoServicioProgramaSubtituloDiaMesActual(Integer idProceso, Integer servicio, Integer programaAno, Integer subtitulo, Integer idDia, Integer idMes) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasEstablecimientosProcesoServicioProgramaSubtituloDiaMes", DetalleRemesas.class);
			query.setParameter("idProceso", idProceso);
			query.setParameter("idServicio", servicio);
			query.setParameter("idProgramaAno", programaAno);
			query.setParameter("idSubtitulo", subtitulo);
			query.setParameter("idMes", idMes);
			query.setParameter("idDia", idDia);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPagadasEstablecimientosProcesoEstablecimientoProgramaSubtituloDiaMesActual(Integer idProceso, Integer establecimiento, Integer programaAno, Integer subtitulo, Integer idDia, Integer idMes) {
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasEstablecimientosProcesoEstablecimientoProgramaSubtituloDiaMes", DetalleRemesas.class);
			query.setParameter("idProceso", idProceso);
			query.setParameter("idEstablecimiento", establecimiento);
			query.setParameter("idProgramaAno", programaAno);
			query.setParameter("idSubtitulo", subtitulo);
			query.setParameter("idMes", idMes);
			query.setParameter("idDia", idDia);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPendientesConsolidadorProgramaAnoComponenteSubtituloEstablecimiento(Integer idProgramaAno, Integer idComponente, Integer idTipoSubtitulo, Integer idEstablecimiento) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPendientesConsolidadorProgramaAnoComponenteSubtituloEstablecimiento", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<DetalleRemesas> getRemesasPendientesConsolidadorProgramaAnoComponenteSubtituloComuna(Integer idProgramaAno, Integer idComponente, Integer idTipoSubtitulo, Integer idComuna) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPendientesConsolidadorProgramaAnoComponenteSubtituloComuna", DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("idComuna", idComuna);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public RemesaConvenios save(RemesaConvenios remesaConvenios) {
		em.persist(remesaConvenios);
		return remesaConvenios;
	}

	public List<DocumentoRemesas> getByIdOTTipoNotFinal(Integer idProcesoOT, TipoDocumentosProcesos tipoDocumento) {
		try {
			TypedQuery<DocumentoRemesas> query = this.em.createNamedQuery("DocumentoRemesas.findByIdOTTipoNotFinal", DocumentoRemesas.class);
			query.setParameter("idRemesa", idProcesoOT);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer deleteDocumento(Integer idDocumento) {
		Query query = this.em.createNamedQuery("ReferenciaDocumento.deleteUsingId");
		query.setParameter("idDocumento", idDocumento);
		return query.executeUpdate();
	}

	public Integer deleteDocumentoRemesa(Integer idDocumentoRemesas) {
		List<Integer> idDocumentosRemesas = new ArrayList<Integer>();
		idDocumentosRemesas.add(idDocumentoRemesas);
		return deleteDocumentoRemesa(idDocumentosRemesas);
	}
	
	public Integer deleteDocumentoRemesa(List<Integer> idDocumentosRemesas) {
		Query query = this.em.createNamedQuery("DocumentoRemesas.deleteUsingIds");
		query.setParameter("idDocumentosRemesas", idDocumentosRemesas);
		return query.executeUpdate();
	}

	public List<DetalleRemesas> getRemesasRechazadasByIdProcesoRemesa(Integer idProcesoOT) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasRechazadasByIdProceso", DetalleRemesas.class);
			query.setParameter("idProceso", idProcesoOT);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<FechaRemesa> getFechasRemesasOrderByDia(){
		try {
			TypedQuery<FechaRemesa> query = this.em.createNamedQuery("FechaRemesa.findAllOrderByDia", FechaRemesa.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<FechaRemesa> getFechasRemesasByProgramaOrderByDia(Integer idPrograma){
		try {
			TypedQuery<FechaRemesa> query = this.em.createNamedQuery("FechaRemesa.findByProgramaOrderByDia", FechaRemesa.class);
			query.setParameter("idPrograma", idPrograma);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaAno> findProgramaAnoByRemesa(Integer idProcesoOT) {
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("DetalleRemesas.getProgramaAnoByRemesaPagada", ProgramaAno.class);
			query.setParameter("idProceso", idProcesoOT);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public DetalleRemesas findDetalleRemesaByIdCuota(Integer idCuota) {
		try {
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.findByIdCuota", DetalleRemesas.class);
			query.setParameter("idCuota", idCuota);
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
}
