package minsal.divap.model.mappers;

import minsal.divap.vo.UsuarioSummaryVO;
import minsal.divap.vo.UsuarioVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
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
		return new UsuarioVO(usuario.getUsername(), usuario.getPassword(), usuario.getNombre(), usuario.getApellido(), usuario.getEmail());
	}

	@Override
	public Object getData(Usuario usuario) {
		throw new NotImplementedException();
	}

}
