/**
 * 
 */
package cl.redhat.bandejaTareas.brms.client.artifacts;

/**
 * @author Jorge Morando
 * 
 */
public class Participant {

	public String type;
	public String idRef;
	public boolean isGroup;
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the idRef
	 */
	public String getIdRef() {
		return idRef;
	}
	/**
	 * @param idRef the idRef to set
	 */
	public void setIdRef(String idRef) {
		this.idRef = idRef;
	}
	/**
	 * @return the isGroup
	 */
	public boolean isGroup() {
		return isGroup;
	}
	/**
	 * @param isGroup the isGroup to set
	 */
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	
}
