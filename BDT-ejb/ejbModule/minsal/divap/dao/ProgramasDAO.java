
package minsal.divap.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import minsal.divap.enums.Subtitulo;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;



@Singleton
public class ProgramasDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public ProgramaAno getProgramaAnoByID(Integer idProgramaAno){
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByIdProgramaAno", ProgramaAno.class);
			query.setParameter("idProgramaAno", idProgramaAno);

			List <ProgramaAno> programas = query.getResultList(); 
			if(programas != null && programas.size() > 0){
				return programas.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Programa> getProgramasByUser(String username){
		try {
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findByUser", Programa.class);
			query.setParameter("usuario", username);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Programa> getComponenteByPrograma(int programaId){
		try {
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findComponentesByPrograma", Programa.class);
			query.setParameter("id", programaId);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public  Programa getProgramaPorID(int programaId){
		try {
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findById", Programa.class);
			query.setParameter("id", programaId);
			return query.getSingleResult(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaAno> getProgramasByUserAno(String username, Integer anoCurso) {
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByUserAno", ProgramaAno.class);
			query.setParameter("usuario", username);
			query.setParameter("ano", anoCurso);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public ProgramaAno getProgramasByIdProgramaAno(Integer idProgramaAno) {
		try {
			return em.find(ProgramaAno.class, idProgramaAno);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ProgramaAno getProgramaAnoByIDProgramaAno(Integer idPrograma, Integer ano) {
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByAnoIdPrograma", ProgramaAno.class);
			query.setParameter("idPrograma", idPrograma);
			query.setParameter("ano", ano);
			
			return query.getSingleResult(); 
			
		} catch (Exception e) {
			
		}
		return null;
	}

	public EstadoPrograma getEstadoProgramaByIdEstado(Integer idEstadoPrograma) {
		try {
			TypedQuery<EstadoPrograma> query = this.em.createNamedQuery("EstadoPrograma.findByIdEstadoPrograma", EstadoPrograma.class);
			query.setParameter("idEstadoPrograma", idEstadoPrograma);
			if (query.getSingleResult()!=null)
			return query.getSingleResult(); 
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

//	public void saveProgramaAnoByIDProgramaAno(Programa programa,
//			AnoEnCurso anoCurso, EstadoPrograma estadoPrograma) {
//		// TODO Auto-generated method stub
//		ProgramaAno programaAno = new ProgramaAno();
//		programaAno.setAno(anoCurso);
//		programaAno.setEstado(estadoPrograma);
//		programaAno.setEstadoFlujoCaja(estadoPrograma);
//		programaAno.setPrograma(programa);
//		//TODO: [ASAAVEDRA] Se debe crear el programa ano para una nueva estimacion de flujo de caja?.
//		//programaAno.setProgramasMunicipalesCore(programasMunicipalesCore);
//		//programaAno.setProgramasServiciosCore(programasServiciosCore);
//		this.em.persist(programaAno);
//	}

	public void guardarEstadoFlujoCaja(Integer idEstado, Integer idProgramaAno) {
		ProgramaAno programaAno = getProgramaAnoByID(idProgramaAno);
		EstadoPrograma estadoPrograma = new EstadoPrograma(idEstado);
		programaAno.setEstadoFlujoCaja(estadoPrograma);
		this.em.persist(programaAno);
	}

	
	public Integer saveProgramaAno(ProgramaAno programaAno, boolean detach) {
		if (detach)
			this.em.detach(programaAno);
	
			this.em.persist(programaAno);
			
			
		return programaAno.getIdProgramaAno();
		
	}

	public AnoEnCurso saveAnoCurso(AnoEnCurso anoCurso) {
		this.em.persist(anoCurso);
		return anoCurso;
	}

	public AnoEnCurso getAnoEnCursoById(int ano) {
		try {
			TypedQuery<AnoEnCurso> query = this.em.createNamedQuery("AnoEnCurso.findByAno", AnoEnCurso.class);
			query.setParameter("ano", ano);
			if (query.getSingleResult()!=null)
			return query.getSingleResult(); 
			
		} catch (Exception e) {
			
		}
		return null;
	}

	public Programa getProgramaById(Integer id){
		try {
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findById", Programa.class);
			query.setParameter("id", id);
			List<Programa> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaMunicipalCoreComponente> getProgramaMunicipales(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo) {
		try {
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByIdProgramaAnoIdComponenteIdSubtitulo", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaServicioCoreComponente> getProgramaServicios(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByIdProgramaAnoIdComponenteIdSubtitulo", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void eliminarPropuesta(Integer idProgramaAno) {
		Query queryCajaMonto = this.em.createNamedQuery("CajaMonto.deleteUsingIdProgramaAno");
		queryCajaMonto.setParameter("idProgramaAno", idProgramaAno);
		queryCajaMonto.executeUpdate();
		Query queryCaja = this.em.createNamedQuery("Caja.deleteUsingIdProgramaAno");
		queryCaja.setParameter("idProgramaAno", idProgramaAno);
		queryCaja.executeUpdate();
		Query queryMarco = this.em.createNamedQuery("MarcoPresupuestario.deleteUsingIdProgramaAno");
		queryMarco.setParameter("idProgramaAno", idProgramaAno);
		queryMarco.executeUpdate();
	}
	
	public ProgramaAno getProgramaAnoSiguiente(Integer idPrograma, Integer anoSiguiente) {
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByAnoIdPrograma", ProgramaAno.class);
			query.setParameter("idPrograma", idPrograma);
			query.setParameter("ano", anoSiguiente);
			List<ProgramaAno> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public ProgramaAno save(ProgramaAno programaAno) {
		this.em.persist(programaAno);
		return programaAno;
	}

	public List<ProgramaAno> getProgramasBySubtitulo(Integer anoCurso, Subtitulo subtitulo) {
		try {
			List<ProgramaAno> programasAno = null;
			TypedQuery<Componente> queryComponente = this.em.createNamedQuery("Componente.findByIdSubtitulo", Componente.class);
			queryComponente.setParameter("idTipoSubtitulo", subtitulo.getId());
			List<Componente> componentes =  queryComponente.getResultList();
			if(componentes != null && componentes.size() > 0){
				List<Integer> idComponentes = new ArrayList<Integer>();
				for(Componente componente : componentes){
					idComponentes.add(componente.getId());
				}
				TypedQuery<ProgramaAno> queryProgramas = this.em.createNamedQuery("ProgramaAno.findByAnoComponente", ProgramaAno.class);
				queryProgramas.setParameter("idComponentes", idComponentes);
				queryProgramas.setParameter("ano", anoCurso);
				programasAno = queryProgramas.getResultList();
			}
			return programasAno;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

