/**
 * Response package for BRMS Rest Connectivity Utility
 */
package cl.redhat.bandejaTareas.brms.client.artifacts.responses;

import java.util.Map;

/**
 * GET Unassigned Tasks potentially assignable to a user by its groups.
 * @author Jorge Morando
 *
 */
//@JacksonXmlRootElement("dataset")
public class GETDatasetInstanceResponse extends BRMSClientWSResponse {

	private Map<String, String> dataset;

	public Map<String, String> getDataset() {
		return dataset;
	}

	public void setDataset(Map<String, String> dataset) {
		this.dataset = dataset;
	}
}
