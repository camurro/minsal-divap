package minsal.divap.model.mappers;

import java.util.HashSet;
import java.util.Set;

import minsal.divap.vo.UsuarioSummaryVO;
import minsal.divap.vo.UsuarioVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Rol;
import cl.minsal.divap.model.Usuario;

public class UsuarioMapper implements Mapper<Usuario>{

	@Override
	public UsuarioSummaryVO getSummary(Usuario usuario) {
		if (usuario == null)
		      return null;
		return new UsuarioSummaryVO(usuario.getNombre(), usuario.getApellido(), usuario.getEmail());
	}

	@Override
	public UsuarioVO getBasic(Usuario usuario) {
		if (usuario == null)
		      return null;
		UsuarioVO usuarioVO = new UsuarioVO(usuario.getUsername(), usuario.getPassword(), usuario.getNombre(), usuario.getApellido(), usuario.getEmail());
		if ((usuario.getRols() != null) && (usuario.getRols().size() > 0)) {
		      Set<String> roles = new HashSet<String> ();
		      for (Rol rol : usuario.getRols()) {
		        roles.add(rol.getNombre());
		      }
		      usuarioVO.setRoles(roles);
		    }
		return usuarioVO;
	}

	@Override
	public Object getData(Usuario usuario) {
		throw new NotImplementedException();
	}

}
