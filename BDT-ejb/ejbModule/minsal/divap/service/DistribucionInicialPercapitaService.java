package minsal.divap.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.UsuarioDAO;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class DistribucionInicialPercapitaService {
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	
	public Integer crearIntanciaDistribucionInicialPercapita(String username){
		System.out.println("username-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
	    return distribucionInicialPercapitaDAO.crearIntanciaDistribucionInicialPercapita(usuario);
	}
	
	
}
