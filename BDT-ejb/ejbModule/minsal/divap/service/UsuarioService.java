package minsal.divap.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.UsuarioDAO;
import minsal.divap.model.mappers.UsuarioMapper;
import minsal.divap.vo.UsuarioSummaryVO;
import minsal.divap.vo.UsuarioVO;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class UsuarioService {
	@EJB
	private UsuarioDAO usuarioDAO;
	
	public UsuarioVO getUserByUsername(String username){
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
	    return new UsuarioMapper().getBasic(usuario);
	}
	
	public UsuarioSummaryVO getUserSummaryByUsername(String username){
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
	    return new UsuarioMapper().getSummary(usuario);
	}
	
}
