/**
 * Response package for BRMS Rest Connectivity Utility
 */
package cl.redhat.bandejaTareas.bpms.client.artifacts.responses;

import java.util.List;

import cl.redhat.bandejaTareas.bpms.client.artifacts.ProcessInstance;

/**
 * GET Process Instances Response
 * @author Jorge Morando
 *
 */
public class GETProcessInstancesResponse extends BRMSClientWSResponse {

	List<ProcessInstance> instances;
	
	int totalCount;
	
	/**
	 * @return the definitions
	 */
	public List<ProcessInstance> getInstances() {
		return instances;
	}

	/**
	 * @param definitions the definitions to set
	 */
	public void setDefinitions(List<ProcessInstance> instances) {
		this.instances = instances;
	}
	
	/**
	 * @return the totalCount
	 */
	public int getTotalCount(){
		return totalCount;
	}
	
	/**
	 * @param total
	 */
	public void setTotalCount(int total) {
		this.totalCount = total;
	}
	
	@Override
	public String toString(){
		return instances.toString();
	}
	
}
