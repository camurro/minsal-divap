package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.TipoCumplimiento;



@Singleton
public class RebajaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public List<TipoCumplimiento> getAllTipoCumplimiento(){
		TypedQuery<TipoCumplimiento> query = this.em.createNamedQuery("TipoCumplimiento.findAll", TipoCumplimiento.class);
		return query.getResultList(); 
	}


}
