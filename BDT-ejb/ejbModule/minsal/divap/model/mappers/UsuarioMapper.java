package minsal.divap.model.mappers;

import java.util.HashSet;
import java.util.Set;

import minsal.divap.vo.ServiciosSummaryVO;
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
		return new UsuarioSummaryVO(usuario.getNombre(), usuario.getApellido(), ((usuario.getEmail() != null) ? usuario.getEmail().getValor() : null));
	}

	@Override
	public UsuarioVO getBasic(Usuario usuario) {
		if (usuario == null)
			return null;
		UsuarioVO usuarioVO = new UsuarioVO(usuario.getUsername(), usuario.getPassword(), usuario.getNombre(), usuario.getApellido(), ((usuario.getEmail() != null) ? usuario.getEmail().getValor() : null));
		if ((usuario.getRols() != null) && (usuario.getRols().size() > 0)) {
			Set<String> roles = new HashSet<String> ();
			for (Rol rol : usuario.getRols()) {
				roles.add(rol.getNombre());
			}
			usuarioVO.setRoles(roles);
		}
		if(usuario.getServicio() != null){
			ServiciosSummaryVO serviciosSummaryVO = new ServicioMapper().getSummary(usuario.getServicio());
			usuarioVO.setServicio(serviciosSummaryVO);
		}
		return usuarioVO;
	}

	@Override
	public Object getData(Usuario usuario) {
		throw new NotImplementedException();
	}

}
