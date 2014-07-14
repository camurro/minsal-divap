/**
 * Response package for BRMS Rest Connectivity Utility
 */
package cl.redhat.bandejaTareas.bpms.client.artifacts.responses;


/**
 * Abstract BRMS REST Client Utility WS response 
 * @author Jorge Morando
 *
 */
public abstract class BRMSClientWSResponse {

	String status = "success";
	
	String message = "";
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
}
