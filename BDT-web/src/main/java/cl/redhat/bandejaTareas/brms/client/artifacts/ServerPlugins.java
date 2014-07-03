/**
 * Artifact package for BRMS Rest Connectivity Utility
 */
package cl.redhat.bandejaTareas.brms.client.artifacts;

/**
 * @author Jorge Morando
 *
 */
public class ServerPlugins {
	
	private String type;
	
	private boolean available;

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
	 * @return the available
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	@Override
	public String toString(){
		return "{type:"+getType()+", available:"+isAvailable()+"}";
	}
	
}
