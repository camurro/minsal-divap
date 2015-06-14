package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.FactorTramoPobreza;
import cl.minsal.divap.model.Usuario;



@Singleton
public class UsuarioDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public Usuario getUserByUsername(String username){
		try {
			TypedQuery<Usuario> query = this.em
					.createQuery(
							"from Usuario u where u.username =?", 
							Usuario.class);
			query.setParameter(1, username);
			List<Usuario> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public String getEmailByUsername(String username) {
		Usuario usuario = getUserByUsername(username);
		if(usuario != null && usuario.getEmail() != null){
			return usuario.getEmail().getValor();
		}
		return null;
	}
	
	public List<Usuario> getUserAll(){
		try {
			TypedQuery<Usuario> query = this.em.createNamedQuery("Usuario.findAll", Usuario.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Usuario> getAllUserActivos(Integer idEstadoUsuario){
		try {
			TypedQuery<Usuario> query = this.em.createNamedQuery("Usuario.findActivos", Usuario.class);
			query.setParameter("idEstadoUsuario", idEstadoUsuario);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
