package minsal.divap.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import minsal.divap.enums.TipoDocumentosProcesos;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoProgramasReforzamiento;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.ProgramaServicioCore;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;
import cl.minsal.divap.model.ProgramasReforzamiento;
import cl.minsal.divap.model.ProgramasReforzamientoSeguimiento;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;
import cl.minsal.divap.model.ReporteEmailsEnviados;
import cl.minsal.divap.model.ReporteEmailsProgramasReforzamiento;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.Usuario;



@Singleton
public class RecursosFinancierosProgramasReforzamientoDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public ProgramaAno findById(Integer idProgramaAno) {
		return this.em.find(ProgramaAno.class, idProgramaAno);
	}
	
	public ProgramasReforzamiento findByIdInstancia(Integer idProgramasReforzamiento) {
		try {
			TypedQuery<ProgramasReforzamiento> query = this.em.createNamedQuery("ProgramasReforzamiento.findByIdProgramasReforzamiento", ProgramasReforzamiento.class);
			query.setParameter("idProgramasReforzamiento", idProgramasReforzamiento);
			List<ProgramasReforzamiento> result = query.getResultList();
			if(result!=null && result.size() > 0){
				return result.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	

	public ProgramaAno update(ProgramaAno programaAno) {
		return em.merge(programaAno);
	}

	public Integer save(ReporteEmailsProgramasReforzamiento reporteEmailsProgramasReforzamiento) {
		em.persist(reporteEmailsProgramasReforzamiento);
		return reporteEmailsProgramasReforzamiento.getIdReporteEmailsProgramasReforzamiento();
	}
	
	public Integer save(ProgramaAno programaAno) {
		em.persist(programaAno);
		return programaAno.getIdProgramaAno();
	}

	public Integer crearIntanciaProgramaAno(Usuario usuario){
		try {
			long current = Calendar.getInstance().getTimeInMillis();
			DistribucionInicialPercapita dto = new DistribucionInicialPercapita();
			dto.setUsuario(usuario);
			dto.setFechaCreacion(new Date(current));
			this.em.persist(dto);
			return dto.getIdDistribucionInicialPercapita();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ProgramaMunicipalCore> getProgramasCoreByProgramaAno(Integer idProgramaAno) {
		List<ProgramaMunicipalCore> results = null;
		try {
			TypedQuery<ProgramaMunicipalCore> query = this.em.createNamedQuery("ProgramaMunicipalCore.findByProgramaAno", ProgramaMunicipalCore.class);
			query.setParameter("programaAno", idProgramaAno);
			results = query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}
	
	public List<ProgramaServicioCore> getProgramaServicioCoreByProgramaAno(Integer idProgramaAno) {
		List<ProgramaServicioCore> results = null;
		try {
			TypedQuery<ProgramaServicioCore> query = this.em.createNamedQuery("ProgramaServicioCore.findByProgramaAno", ProgramaServicioCore.class);
			query.setParameter("programaAno", idProgramaAno);
			results = query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}

	public Integer deleteProgramasMunicipalCore(List<Integer> idsProgramasCore) {
		System.out.println("Inicio deleteProgramasMunicipalCore" + idsProgramasCore);
		Query queryProgramaMunicipalCoreComponente = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.deleteByProgramasMunicipalCore");
		queryProgramaMunicipalCoreComponente.setParameter("programasMunicipalCore", idsProgramasCore);
		Integer programasMunicipalCoreDelete = queryProgramaMunicipalCoreComponente.executeUpdate();
		Query queryProgramaMunicipalCore = this.em.createNamedQuery("ProgramaMunicipalCore.deleteByProgramasMunicipalCore");
		queryProgramaMunicipalCore.setParameter("programasMunicipalCore", idsProgramasCore);
		programasMunicipalCoreDelete = queryProgramaMunicipalCore.executeUpdate();
		System.out.println("Fin deleteProgramasMunicipalCore programasMunicipalCoreDelete " + programasMunicipalCoreDelete);
		return programasMunicipalCoreDelete;
	}
	
	public Integer deleteProgramasServiciosCore(List<Integer> idsProgramasCore) {
		System.out.println("Inicio deleteProgramasServiciosCore" + idsProgramasCore);
		Query queryProgramaServiciosCoreComponente = this.em.createNamedQuery("ProgramaServicioCoreComponente.deleteByProgramasServicioCore");
		queryProgramaServiciosCoreComponente.setParameter("programasServicioCore", idsProgramasCore);
		Integer programasServiciosCoreDelete = queryProgramaServiciosCoreComponente.executeUpdate();
		
		Query queryProgramaServicioCore = this.em.createNamedQuery("ProgramaServicioCore.deleteByProgramasServicioCore");
		queryProgramaServicioCore.setParameter("programasServicioCore", idsProgramasCore);
		programasServiciosCoreDelete = queryProgramaServicioCore.executeUpdate();
		System.out.println("Fin deleteProgramasServiciosCore programasServiciosCoreDelete " + programasServiciosCoreDelete);
		return programasServiciosCoreDelete;
	}
	

	public ProgramaMunicipalCore save(ProgramaMunicipalCore programaMunicipalCore) {
		em.persist(programaMunicipalCore);
		return programaMunicipalCore;
	}

	public ProgramaMunicipalCoreComponente save(ProgramaMunicipalCoreComponente programaMunicipalCoreComponente) {
		em.persist(programaMunicipalCoreComponente);
		return programaMunicipalCoreComponente;
	}

	public ProgramaServicioCore save(ProgramaServicioCore programaServicioCore) {
		em.persist(programaServicioCore);
		return programaServicioCore;
		
	}

	public ProgramaServicioCoreComponente save(
			ProgramaServicioCoreComponente programaServicioCoreComponente) {
		em.persist(programaServicioCoreComponente);
		return programaServicioCoreComponente;
		
	}

	public DocumentoProgramasReforzamiento save(
			DocumentoProgramasReforzamiento documentoProgramasReforzamiento) {
		em.persist(documentoProgramasReforzamiento);
		return documentoProgramasReforzamiento;
		
	}

	public List<ProgramaMunicipalCoreComponente> getProgramasCoreComponenteByServicioProgramaAnoComponentesSubtitulo(Integer idServicio, Integer idProgramaAno, 
			List<Integer> idComponentes, Integer idSubtitulo) {
		try {
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByServicoProgramaAnoComponentesSubtitulo", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaServicioCoreComponente> getProgramasServicioCoreComponenteByServicioProgramaAnoComponentesSubtitulo(
			Integer idServicio , Integer idProgramaAno, List<Integer> idComponentes, Integer idSubtitulo) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByServicoProgramaAnoComponentesSubtitulo", ProgramaServicioCoreComponente.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	public Integer createSeguimiento(ProgramaAno programaAno, Seguimiento seguimiento) {
		// TODO Auto-generated method stub
		ProgramasReforzamientoSeguimiento programasReforzamientoSeguimiento = new ProgramasReforzamientoSeguimiento();
		programasReforzamientoSeguimiento.setIdProgramaAno(programaAno);
		programasReforzamientoSeguimiento.setSeguimiento(seguimiento);
		this.em.persist(programasReforzamientoSeguimiento);
		return programasReforzamientoSeguimiento.getId();
	}

	public List<DocumentoProgramasReforzamiento> getByIdTipo(
			Integer idProgramaSiguiente,
			TipoDocumentosProcesos resolucionprogramasaps) {
		try {
			TypedQuery<DocumentoProgramasReforzamiento> query = this.em.createNamedQuery("DocumentoProgramasReforzamiento.findByProgramaAnoTipoDocumento", DocumentoProgramasReforzamiento.class);
			query.setParameter("idProgramaAno", idProgramaSiguiente);
			query.setParameter("idTipoDocumento", resolucionprogramasaps.getId());
			return query.getResultList(); 
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ReporteEmailsEnviados save(ReporteEmailsEnviados reporteEnviados) {
		em.persist(reporteEnviados);
		return reporteEnviados;
	}

	public ReporteEmailsAdjuntos save(ReporteEmailsAdjuntos adjuntoMail) {
		em.persist(adjuntoMail);
		return adjuntoMail;
		
	}

	public ReporteEmailsDestinatarios save(ReporteEmailsDestinatarios destinatario) {
		em.persist(destinatario);
		return destinatario;
		
	}

	public List<ReporteEmailsEnviados> getReporteCorreosByIdPrograma(Integer idProgramaAno,
			boolean modifica, Integer idServicio) {
		
		try {
			TypedQuery<ReporteEmailsEnviados> query = this.em.createNamedQuery("ReporteEmailsEnviados.getReporteCorreosByIdProgramaAndidServicio", ReporteEmailsEnviados.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("modifica", modifica);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public List<ReporteEmailsProgramasReforzamiento> getReporteCorreosProgramasReforzamiento(Integer idReporteEmailsProgramasReforzamiento){
		try {
			TypedQuery<ReporteEmailsProgramasReforzamiento> query = this.em.createNamedQuery("ReporteEmailsProgramasReforzamiento.findByIdReporteEmailsProgramasReforzamiento", ReporteEmailsProgramasReforzamiento.class);
			query.setParameter("idReporteEmailsProgramasReforzamiento", idReporteEmailsProgramasReforzamiento);
			return query.getResultList(); 
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}

	public List<DocumentoProgramasReforzamiento> getByIdTipo(
			Integer idProgramaSiguiente, TipoDocumentosProcesos tipoDocumento,
			Integer idServicio) {
		try {
			TypedQuery<DocumentoProgramasReforzamiento> query = this.em.createNamedQuery("DocumentoProgramasReforzamiento.findByProgramaAnoTipoDocumentoIdServicio", DocumentoProgramasReforzamiento.class);
			query.setParameter("idProgramaAno", idProgramaSiguiente);
			query.setParameter("idTipoDocumento", tipoDocumento.getId());
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteDocumentoProgramaAPS(Integer id) {
		Query queryDeleteDocumento = this.em.createNamedQuery("DocumentoProgramasReforzamiento.deleteById");
		queryDeleteDocumento.setParameter("id", id);
		queryDeleteDocumento.executeUpdate();
	}

	public Integer crearInstanciaModificacion(Usuario usuario, Mes mes,
			Date date) {
		ProgramasReforzamiento reforzamiento = new ProgramasReforzamiento();
		reforzamiento.setFechaCreacion(date);
		reforzamiento.setMes(mes);
		reforzamiento.setUsuario(usuario);
		em.persist(reforzamiento);
		return reforzamiento.getIdProgramasReforzamiento();
	}

}
