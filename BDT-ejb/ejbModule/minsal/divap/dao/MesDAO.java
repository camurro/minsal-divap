package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Mes;
@Singleton
public class MesDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;


	public  Mes getMesPorID(int idMes){
		try {
			TypedQuery<Mes> query = this.em.createNamedQuery("Mes.findByIdMes", Mes.class);
			query.setParameter("idMes", idMes);
			return query.getSingleResult(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public List<Mes> getAll(){
		try {
			TypedQuery<Mes> query = this.em.createNamedQuery("Mes.findAll", Mes.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
