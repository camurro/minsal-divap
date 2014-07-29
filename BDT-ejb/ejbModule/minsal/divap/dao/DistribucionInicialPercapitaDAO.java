package minsal.divap.dao;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.Usuario;



@Singleton
public class DistribucionInicialPercapitaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public DistribucionInicialPercapita findById(Integer idDistribucionInicialPercapita) {
		return this.em.find(DistribucionInicialPercapita.class, idDistribucionInicialPercapita);
	}

	public Integer crearIntanciaDistribucionInicialPercapita(Usuario usuario){
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

}
