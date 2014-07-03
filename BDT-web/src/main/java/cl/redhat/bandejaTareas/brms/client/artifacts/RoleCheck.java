/**
 * 
 */
package cl.redhat.bandejaTareas.brms.client.artifacts;

/**
 * @author Jorge Morando
 * 
 */
public class RoleCheck {

	public String role;
	public Boolean assigned;
	
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @return the assigned
	 */
	public boolean getAssigned() {
		return assigned;
	}
	/**
	 * @param assigned the assigned to set
	 */
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	
}
