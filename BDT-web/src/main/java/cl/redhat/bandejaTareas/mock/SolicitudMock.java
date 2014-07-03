package cl.redhat.bandejaTareas.mock;

public class SolicitudMock {
	
	
	private Long idSolicitud;
	private String firstname;
	private String lastname;
	private String createDate;
	private String dueDate;
	private String  state;
	
	public SolicitudMock() {

	}
	
	public SolicitudMock(Long idSolicitud, String firstname, String lastname,
			String createDate, String dueDate, String state) {
		super();
		this.idSolicitud = idSolicitud;
		this.firstname = firstname;
		this.lastname = lastname;
		this.createDate = createDate;
		this.dueDate = dueDate;
		this.state = state;
	}

	
	public Long getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(Long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

}
