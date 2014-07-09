/**
 * Artifact package for BRMS Rest Connectivity Utility
 */
package cl.redhat.bandejaTareas.bpms.client.artifacts;

import java.util.Date;

/**
 * @author Jorge Morando
 * 
 */
public class ProcessInstance {

	private Long id;

	private String definitionId;

	private Date startDate;

	private boolean suspended;

	private ProcessInstanceToken rootToken;

	private String key;
	
	private Date endDate;
	
	private String endResult;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the definitionId
	 */
	public String getDefinitionId() {
		return definitionId;
	}

	/**
	 * @param definitionId
	 *            the definitionId to set
	 */
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the suspended
	 */
	public boolean isSuspended() {
		return suspended;
	}

	/**
	 * @param suspended
	 *            the suspended to set
	 */
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	/**
	 * @return the rootToken
	 */
	public ProcessInstanceToken getRootToken() {
		return rootToken;
	}

	/**
	 * @param rootToken
	 *            the rootToken to set
	 */
	public void setRootToken(ProcessInstanceToken rootToken) {
		this.rootToken = rootToken;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the endResult
	 */
	public String getEndResult() {
		return endResult;
	}

	/**
	 * @param endResult the endResult to set
	 */
	public void setEndResult(String endResult) {
		this.endResult = endResult;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{");
		sb.append("id:");
		sb.append(id);
		sb.append(",");
		sb.append("definitionId:");
		sb.append(definitionId);
		sb.append(",");
		sb.append("startDate:");
		sb.append(startDate);
		sb.append(",");
		sb.append("endDate:");
		sb.append(endDate);
		sb.append(",");
		sb.append("endResult:");
		sb.append(endResult);
		sb.append(",");
		sb.append("isSuspended:");
		sb.append(suspended);
		sb.append(",");
		sb.append("rootToken:");
		sb.append(rootToken);
		sb.append("}");

		return sb.toString();
	}

}
