package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Email;



@Singleton
public class EmailDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public Email getEmailIdByValor(String email){
		try {
			TypedQuery<Email> query = this.em.createNamedQuery("Email.findByValor", Email.class);
			query.setParameter("valor", email.toLowerCase());
			List<Email> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public Email save(Email newMail) {
		this.em.persist(newMail);
		return newMail;
	}
	
	public Email getEmailIdById(Integer idEmail){
		try {
			TypedQuery<Email> query = this.em.createNamedQuery("Email.findByIdEmail", Email.class);
			query.setParameter("idEmail", idEmail);
			List<Email> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public List<Email> getAllEmail(){
		try {
			TypedQuery<Email> query = this.em.createNamedQuery("Email.findAll", Email.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	

}
