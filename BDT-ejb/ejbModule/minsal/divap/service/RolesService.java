package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.RolDAO;
import cl.minsal.divap.model.Rol;

@Stateless
@LocalBean
public class RolesService {
	@EJB
	private RolDAO rolDAO;

	public List<String> getAllRoles() {
		List<Rol> roles = this.rolDAO.getRoles();
		ArrayList<String> result = new ArrayList<String>(roles.size());
		for (Rol rol : roles){
			result.add(rol.getNombre());
		}
		return result;
	}

}
