/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.mantenedores.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.security.auth.spi.Util;

import minsal.divap.dao.EmailDAO;
import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.dao.RolDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.EstadoUsuarioEnum;
import minsal.divap.service.EmailService;
import minsal.divap.vo.MantenedorEstadoProgramaVO;
import minsal.divap.vo.MantenedorUsuarioVO;
import cl.minsal.divap.model.Email;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.EstadoUsuario;
import cl.minsal.divap.model.Rol;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.Usuario;
import cl.minsal.util.PasswordHelper;

/**
 *
 * @author francisco
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName="BDT-JPA")
    private EntityManager em;
    
    @EJB
    private UsuarioDAO usuarioDAO;
    @EJB
    private EmailDAO emailDAO;
    @EJB
    private ServicioSaludDAO servicioSaludDAO;
    @EJB
    private RolDAO rolDAO;
    @EJB
    private EmailService emailService;
    @EJB
    private MantenedoresDAO mantenedoresDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    public void edit(MantenedorUsuarioVO seleccionado){
    	Usuario usuario = null;
    	
    	if(seleccionado.getUsername() == null){
    		usuario = new Usuario();
    	}else{
    		usuario = usuarioDAO.getUserByUsername(seleccionado.getUsername());
    		usuario.setNombre(seleccionado.getNombre());
    		usuario.setApellido(seleccionado.getApellido());
//    		usuario.setPassword("minsal2014");
    		Email email = emailDAO.getEmailIdById(seleccionado.getIdEmail());
    		usuario.setEmail(email);
    		List<Rol> rolesUsuario = new ArrayList<Rol>();
    		for(String nombreRol : seleccionado.getNombreRoles()){
    			System.out.println("nombreRol a agregar --> "+nombreRol);
    			Rol rol = rolDAO.getRolByNombre(nombreRol);
    			rolesUsuario.add(rol);
    		}
    		usuario.setRols(rolesUsuario);
    		getEntityManager().merge(usuario);
    	}
    }
    
    public void create(MantenedorUsuarioVO nuevo){
    	
    	Usuario usuario = new Usuario();
    	usuario.setUsername(nuevo.getUsername());
    	usuario.setNombre(nuevo.getNombre());
		usuario.setApellido(nuevo.getApellido());
		EstadoUsuario estado = mantenedoresDAO.getEstadoUsuarioById(EstadoUsuarioEnum.ACTIVO.getId());
		usuario.setEstado(estado);
		
		PasswordHelper validacionPassword = new PasswordHelper();
		String passwordNoEncriptada = validacionPassword.generarPassword();
		
		usuario.setPassword(this.generate(passwordNoEncriptada));
		
		Email email = new Email();
		email.setValor(nuevo.getEmail());
		getEntityManager().persist(email);
		
		usuario.setEmail(email);
		
		List<Rol> rolesUsuario = new ArrayList<Rol>();
		for(String nombreRol : nuevo.getNombreRoles()){
			Rol rol = rolDAO.getRolByNombre(nombreRol);
			rolesUsuario.add(rol);
		}
		usuario.setRols(rolesUsuario);
		getEntityManager().persist(usuario);
		
		List<String> to = new ArrayList<String>();
		to.add(email.getValor());
		
		emailService.sendMail(to, null, null, "Usuario creado", "El usuario "+nuevo.getUsername()+" ha sido creado con la password temporal: "+passwordNoEncriptada);
		
		
    }
    
    public void remove(MantenedorUsuarioVO seleccionado){
    	Usuario usuario = usuarioDAO.getUserByUsername(seleccionado.getUsername());
    	EstadoUsuario estadoUsuario = mantenedoresDAO.getEstadoUsuarioById(EstadoUsuarioEnum.ELIMINADO.getId());
    	usuario.setEstado(estadoUsuario);
    	getEntityManager().merge(usuario);
    }
    
    public String generate(String password) {
        return Util.createPasswordHash("SHA-256", "BASE64", null, null,password);
      }
    
}
