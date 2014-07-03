package cl.redhat.bandejaTareas.util;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;

@SessionScoped
@Stateful
@Named("userUtil")
@Default
public class UserUtil implements Serializable {

	private static final long serialVersionUID = -191132953360318549L;

	private String username;
	
	private String password;
	
	private String token;
	
	private Date fechaToken;
	
	private Long userId;
	
	private boolean admin;
	private boolean centralizador;
	private boolean oirs;
	private boolean espec;
	
	public void inicializar(String _username, String _password,	String _token,	Date _fechaToken){
		setUsername(_username);
		setPassword(_password);
		setToken(_token);
		setFechaToken(_fechaToken);
	}

	public void logout(){
		this.username = null;
		this.password = null;
		this.token = null;
		this.fechaToken = null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getFechaToken() {
		return fechaToken;
	}

	public void setFechaToken(Date fechaToken) {
		this.fechaToken = fechaToken;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public boolean isLogged(){
		return username != null && password != null; 	
	}

	public void setRoles(boolean isAdmin,boolean isCentr,boolean isEspec){
		this.oirs = false;
		this.admin = isAdmin;
		this.centralizador = isCentr;
		this.espec = isEspec;
		if(!admin && !espec && !centralizador)
			this.oirs = true;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isCentralizador() {
		return centralizador;
	}

	public boolean isAdmin() {
		return admin;
	}

	public boolean isOirs() {
		return oirs;
	}

	public boolean isEspec() {
		return espec;
	}

}
