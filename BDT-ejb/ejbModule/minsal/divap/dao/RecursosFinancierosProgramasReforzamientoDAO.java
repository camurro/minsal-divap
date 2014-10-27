package minsal.divap.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.ProgramaServicioCore;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;
import cl.minsal.divap.model.Usuario;



@Singleton
public class RecursosFinancierosProgramasReforzamientoDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public ProgramaAno findById(Integer idProgramaAno) {
		return this.em.find(ProgramaAno.class, idProgramaAno);
	}

	public ProgramaAno update(ProgramaAno programaAno) {
		return em.merge(programaAno);
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

	/*public Integer createSeguimiento(Integer idProgramaAno,
			Seguimiento seguimiento) {
		ProgramaAno programaAno = findById(idProgramaAno);
		DistribucionInicialPercapitaSeguimiento distribucionInicialPercapitaSeguimiento = new DistribucionInicialPercapitaSeguimiento();
		distribucionInicialPercapitaSeguimiento.setDistribucionInicialPercapita(distribucionInicialPercapita);
		distribucionInicialPercapitaSeguimiento.setSeguimiento(seguimiento);
		this.em.persist(distribucionInicialPercapitaSeguimiento);
		return distribucionInicialPercapitaSeguimiento.getIdDistribucionInicialPercapitaSeguimiento();
	}*/

}
