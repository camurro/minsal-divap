
package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.jbpm.services.task.commands.GetPotentialOwnersForTaskCommand;

import minsal.divap.enums.EstadosProgramas;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;



@Singleton
public class ProgramasDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

//	@PersistenceUnit(unitName="persistenceUnitSqlServer")
//	private EntityManagerFactory emf;
////	
//	@Autowired
//	private JtaTransactionManager transactionManagerSqlServer;
	
	
	
	public ProgramaAno getProgramaAnoByID(Integer idProgramaAno){
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByIdProgramaAno", ProgramaAno.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			List <ProgramaAno> programasAno = query.getResultList(); 
			for (ProgramaAno programaAno : programasAno) {
				return programaAno;
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
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findProgramaPorID", Programa.class);
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
	
	
	public Programa getProgramaByID(Integer idPrograma){
		try {
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findById", Programa.class);
			query.setParameter("id", idPrograma);
			if (query.getSingleResult()!=null)
			return query.getSingleResult(); 
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public ProgramaAno getProgramaAnoByIDProgramaAno(Integer idPrograma, Integer ano) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		ProgramaAno programaAno = getProgramaAnoByID(idProgramaAno);
		EstadoPrograma estadoPrograma = new EstadoPrograma(idEstado);
		programaAno.setEstadoFlujoCaja(estadoPrograma);
		this.em.persist(programaAno);
	}

	
	public Integer saveProgramaAno(ProgramaAno programaAno, boolean detach) {
		// TODO Auto-generated method stub
	
		
		if (detach)
			this.em.detach(programaAno);
	
			this.em.persist(programaAno);
			
			
		return programaAno.getIdProgramaAno();
		
		
		
		
	}

	public AnoEnCurso saveAnoCurso(AnoEnCurso anoCurso) {
		// TODO Auto-generated method stub
		this.em.persist(anoCurso);
		return anoCurso;
	}

	public AnoEnCurso getAnoEnCursoById(int ano) {
		// TODO Auto-generated method stub
		try {
			TypedQuery<AnoEnCurso> query = this.em.createNamedQuery("AnoEnCurso.findByAno", AnoEnCurso.class);
			query.setParameter("ano", ano);
			if (query.getSingleResult()!=null)
			return query.getSingleResult(); 
			
		} catch (Exception e) {
			
		}
		return null;
	}
	

}

