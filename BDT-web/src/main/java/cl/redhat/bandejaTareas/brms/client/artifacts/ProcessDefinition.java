/**
 * Artifact package for BRMS Rest Connectivity Utility
 */
package cl.redhat.bandejaTareas.brms.client.artifacts;

/**
 * @author Jorge Morando
 *
 */
public class ProcessDefinition {
	
	private String id;
	
	private String name;
	
	private long version;
	
	private String packageName;
	
	private String deploymentId;
	
	private boolean suspended;
	
	private String diagramUrl;
	
	private String key
	;
	private String description;
	
	private String formUrl;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the deploymentId
	 */
	public String getDeploymentId() {
		return deploymentId;
	}

	/**
	 * @param deploymentId the deploymentId to set
	 */
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	/**
	 * @return the suspended
	 */
	public boolean isSuspended() {
		return suspended;
	}

	/**
	 * @param suspended the suspended to set
	 */
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	/**
	 * @return the diagramUrl
	 */
	public String getDiagramUrl() {
		return diagramUrl;
	}

	/**
	 * @param diagramUrl the diagramUrl to set
	 */
	public void setDiagramUrl(String diagramUrl) {
		this.diagramUrl = diagramUrl;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the formUrl
	 */
	public String getFormUrl() {
		return formUrl;
	}

	/**
	 * @param formUrl the formUrl to set
	 */
	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		sb.append("id:");
		sb.append(id);
		sb.append(",");
		sb.append("name:");
		sb.append(name);
		sb.append(",");
		sb.append("version:");
		sb.append(version);
		sb.append(",");
		sb.append("packageName:");
		sb.append(packageName);
		sb.append(",");
		sb.append("deploymentId:");
		sb.append(deploymentId);
		sb.append(",");
		sb.append("diagramUrl:");
		sb.append(diagramUrl);
		sb.append("}");
		
		return sb.toString();
	}
	
}
