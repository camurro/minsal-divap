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

	public ProgramaMunicipalCore save(ProgramaMunicipalCore programaMunicipalCore) {
		em.persist(programaMunicipalCore);
		return programaMunicipalCore;
	}

	public ProgramaMunicipalCoreComponente save(ProgramaMunicipalCoreComponente programaMunicipalCoreComponente) {
		em.persist(programaMunicipalCoreComponente);
		return programaMunicipalCoreComponente;
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
