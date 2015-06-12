package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class MantenedorUsuarioVO implements Serializable{

	private static final long serialVersionUID = 3825890642994254818L;
	
	private String username;
	private String nombre;
	private String apellido;
	private String password;
	private Integer idEmail;
	private String email;
	private List<String> nombreRoles;
	private List<String> nombreRolesFaltantes;
	private Boolean puedeEliminarse;
	
	public MantenedorUsuarioVO(){
		
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getNombreRoles() {
		return nombreRoles;
	}

	public void setNombreRoles(List<String> nombreRoles) {
		this.nombreRoles = nombreRoles;
	}

	public List<String> getNombreRolesFaltantes() {
		return nombreRolesFaltantes;
	}

	public void setNombreRolesFaltantes(List<String> nombreRolesFaltantes) {
		this.nombreRolesFaltantes = nombreRolesFaltantes;
	}

	public Integer getIdEmail() {
		return idEmail;
	}

	public void setIdEmail(Integer idEmail) {
		this.idEmail = idEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getPuedeEliminarse() {
		return puedeEliminarse;
	}

	public void setPuedeEliminarse(Boolean puedeEliminarse) {
		this.puedeEliminarse = puedeEliminarse;
	}

	@Override
	public String toString() {
		return "MantenedorUsuarioVO [username=" + username + ", nombre="
				+ nombre + ", apellido=" + apellido + ", password=" + password
				+ ", idEmail=" + idEmail + ", email=" + email
				+ ", nombreRoles=" + nombreRoles + ", nombreRolesFaltantes="
				+ nombreRolesFaltantes + ", puedeEliminarse=" + puedeEliminarse
				+ "]";
	}

}
