/**
 * Response package for BRMS Rest Connectivity Utility
 */
package cl.redhat.bandejaTareas.bpms.client.artifacts.responses;

import java.util.List;

import cl.redhat.bandejaTareas.bpms.client.artifacts.RoleCheck;

/**
 * GET Role Check Response
 * @author Jorge Morando
 *
 */
public class GETRoleCheckResponse extends BRMSClientWSResponse {

	List<RoleCheck> roles;
	
	/**
	 * @return the roles
	 */
	public List<RoleCheck> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 */
	public void setRoles(List<RoleCheck> roles) {
		this.roles = roles;
	}

	@Override
	public String toString(){
		return roles.toString();
	}
	
}
