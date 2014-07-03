package minsal.divap.vo;

import java.io.Serializable;

public class UsuarioVO extends UsuarioSummaryVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4283196708102961934L;
	
	private String username;
	private String password;
	
	public UsuarioVO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	
	public UsuarioVO(String username, String password, String nombre, String apellido, String email) {
		super(nombre, apellido, email);
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
