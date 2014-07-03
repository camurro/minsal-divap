/**
 * Response package for BRMS Rest Connectivity Utility
 */
package cl.redhat.bandejaTareas.brms.client.artifacts.responses;

import java.util.List;

import cl.redhat.bandejaTareas.brms.client.artifacts.HumanTask;

/**
 * GET Unassigned Tasks potentially assignable to a user by its groups.
 * @author Jorge Morando
 *
 */
public class GETUnassignedTasksResponse extends BRMSClientWSResponse {

	private List<HumanTask> tasks;
	
	private int totalCount;
	
	/**
	 * @return the definitions
	 */
	public List<HumanTask> getTasks() {
		return tasks;
	}

	/**
	 * @param definitions the definitions to set
	 */
	public void setTasks(List<HumanTask> tasks) {
		this.tasks = tasks;
	}
	
	

	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString(){
		return tasks.toString();
	}
	
}
