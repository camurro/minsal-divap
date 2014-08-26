package minsal.divap.dao;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.ProgramaAno;
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
