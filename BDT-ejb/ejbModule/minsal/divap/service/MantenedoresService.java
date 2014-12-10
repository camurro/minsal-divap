package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.AnoDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RebajaDAO;
import minsal.divap.dao.RolDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.model.mappers.PersonaMapper;
import minsal.divap.model.mappers.ServicioMapper;
import minsal.divap.vo.MantenedorAnoVO;
import minsal.divap.vo.MantenedorCumplimientoVO;
import minsal.divap.vo.MantenedorEstadoProgramaVO;
import minsal.divap.vo.MantenedorFactorRefAsigZonaVO;
import minsal.divap.vo.MantenedorFactorTramoPobrezaVO;
import minsal.divap.vo.MantenedorProgramaVO;
import minsal.divap.vo.MantenedorRegionVO;
import minsal.divap.vo.MantenedorUsuarioVO;
import minsal.divap.vo.PersonaVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.FactorRefAsigZona;
import cl.minsal.divap.model.FactorTramoPobreza;
import cl.minsal.divap.model.Persona;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.Rol;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoCumplimiento;
import cl.minsal.divap.model.TipoSubtitulo;
import cl.minsal.divap.model.Tramo;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class MantenedoresService {

	@EJB
	private ComunaDAO comunaDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private TipoSubtituloDAO tipoSubtituloDAO;
	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private MantenedoresDAO mantenedoresDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private AnoDAO anoDAO;
	@EJB
	private RolDAO rolDAO;
	@EJB
	private ComponenteDAO componenteDAO;
	@EJB
	private RebajaDAO rebajaDAO;
	
	public List<ServiciosVO> getServiciosOrderId() {
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		List<ServiciosVO> serviciosVO = new ArrayList<ServiciosVO>();
		if (servicios != null && servicios.size() > 0) {
			for (ServicioSalud servicioSalud : servicios) {
				ServiciosVO servicioVO = new ServicioMapper()
						.getBasic(servicioSalud);
				serviciosVO.add(servicioVO);
			}
		}
		return serviciosVO;
	}
	
	
	public List<PersonaVO> getAllPersonas(){
		List<Persona> personas = servicioSaludDAO.getAllPersonas();
		List<PersonaVO> personasVO = new ArrayList<PersonaVO>();
		if(personas != null && personas.size() > 0){
			for(Persona p : personas){
				PersonaVO personaVO = new PersonaMapper().getBasic(p);
				personasVO.add(personaVO);
				
			}
		}
		return personasVO;
	}
	
	public List<MantenedorRegionVO> getAllMantenedorRegionesVO(){
		List<Region> regiones = servicioSaludDAO.getAllRegion();
		List<MantenedorRegionVO> mantenedoresRegionVO = new ArrayList<MantenedorRegionVO>();
		if(regiones != null && regiones.size() > 0){
			for(Region region : regiones){
				MantenedorRegionVO mantenedorRegionVO = new MantenedorRegionVO();
				mantenedorRegionVO.setIdRegion(region.getId());
				mantenedorRegionVO.setNombreRegion(region.getNombre());
				mantenedorRegionVO.setIdSecretarioRegional(region.getSecretarioRegional().getIdPersona());
				mantenedorRegionVO.setSecretarioRegional(region.getSecretarioRegional().getNombre()+" "+region.getSecretarioRegional().getApellidoPaterno());
				mantenedoresRegionVO.add(mantenedorRegionVO);
			}
		}
		return mantenedoresRegionVO;
	}
	
	public MantenedorRegionVO getMantenedorRegioVOById(Integer idRegion){
		Region region = servicioSaludDAO.getRegionById(idRegion);
		MantenedorRegionVO mantenedorRegionVO = new MantenedorRegionVO();
//		mantenedorRegionVO.setIdRegion(region.getId());
		mantenedorRegionVO.setNombreRegion(region.getNombre());
		mantenedorRegionVO.setIdSecretarioRegional(region.getSecretarioRegional().getIdPersona());
		mantenedorRegionVO.setSecretarioRegional(region.getSecretarioRegional().getNombre()+" "+region.getSecretarioRegional().getApellidoPaterno());
		return mantenedorRegionVO;
	}
	
	public List<String> getSubtitulosNombres(){
		List<TipoSubtitulo> tipoSubtitulos = this.tipoSubtituloDAO.getTipoSubtituloAll();
		List<String> nombreSubtitulos = new ArrayList<String>();
		if(tipoSubtitulos != null && tipoSubtitulos.size() > 0){
			for(TipoSubtitulo tipoSubtitulo : tipoSubtitulos){
				nombreSubtitulos.add(tipoSubtitulo.getNombreSubtitulo());
			}
		}
		return nombreSubtitulos;
		
	}
	
	public List<String> getNombreSubtitulosByComponente(Integer idComponente){
		List<ComponenteSubtitulo> componenteSubtitulos = this.tipoSubtituloDAO.getByIdComponente(idComponente);
		List<String> nombreSubtitulos = new ArrayList<String>();
		for(ComponenteSubtitulo componenteSubtitulo : componenteSubtitulos){
			System.out.println("subtitulo que posee el componente --> "+componenteSubtitulo.getSubtitulo().getNombreSubtitulo());
			String nombre = null;
			nombre = componenteSubtitulo.getSubtitulo().getNombreSubtitulo();
			nombreSubtitulos.add(nombre);
		}
		return nombreSubtitulos;
		
	}
	
	public List<String> getNombreSubtitulosFaltantesComponente(Integer idComponente){
		List<TipoSubtitulo> tipoSubtitulos = this.tipoSubtituloDAO.getTipoSubtituloAll();
		List<String> nombresFinal = new ArrayList<String>();
		for(TipoSubtitulo tipoSubtitulo : tipoSubtitulos){
			nombresFinal.add(tipoSubtitulo.getNombreSubtitulo());
		}
		List<ComponenteSubtitulo> componenteSubtitulos = this.tipoSubtituloDAO.getByIdComponente(idComponente);
		List<String> nombreBorrar = new ArrayList<String>();
		for(ComponenteSubtitulo componenteSubtitulo : componenteSubtitulos){
			System.out.println("subtitulo que posee el componente --> "+componenteSubtitulo.getSubtitulo().getNombreSubtitulo());
			String nombre = null;
			nombre = componenteSubtitulo.getSubtitulo().getNombreSubtitulo();
			nombreBorrar.add(nombre);
		}
		for(int i= 0; i< nombresFinal.size(); i++){
			
			for(int j=0; j<nombreBorrar.size(); j++){
				if(nombresFinal.get(i).equalsIgnoreCase(nombreBorrar.get(j))){
					nombresFinal.remove(i);
				}
			}
		}
		return nombresFinal;
	}
	
	public List<MantenedorEstadoProgramaVO> getMantenedorEstadoProgramaAll(){
		List<EstadoPrograma> estadoProgramas = programasDAO.getAllEstadoProgramas();
		List<MantenedorEstadoProgramaVO> resultado = new ArrayList<MantenedorEstadoProgramaVO>();
		for(EstadoPrograma estadoPrograma : estadoProgramas){
			MantenedorEstadoProgramaVO mantenedorEstadoProgramaVO = new MantenedorEstadoProgramaVO();
			mantenedorEstadoProgramaVO.setId_estado_programa(estadoPrograma.getIdEstadoPrograma());
			mantenedorEstadoProgramaVO.setNombre_estado(estadoPrograma.getNombreEstado());
			resultado.add(mantenedorEstadoProgramaVO);
		}
		return resultado;
	}
	
	public MantenedorEstadoProgramaVO getMantenedorEstadoProgramaById(Integer idEstadoPrograma){
		EstadoPrograma estadoPrograma = programasDAO.getEstadoProgramaById(idEstadoPrograma);
		MantenedorEstadoProgramaVO mantenedorEstadoProgramaVO = new MantenedorEstadoProgramaVO();
		mantenedorEstadoProgramaVO.setId_estado_programa(estadoPrograma.getIdEstadoPrograma());
		mantenedorEstadoProgramaVO.setNombre_estado(estadoPrograma.getNombreEstado());
		return mantenedorEstadoProgramaVO;
		
	}
	
	public List<MantenedorFactorRefAsigZonaVO> getMantenedorFactorRefAsigZonaAll(){
		List<MantenedorFactorRefAsigZonaVO> resultado = new ArrayList<MantenedorFactorRefAsigZonaVO>();
		List <FactorRefAsigZona> factorRefAsigZonas = mantenedoresDAO.getFactorRefAsigZonaAll();
		for(FactorRefAsigZona factorRefAsigZona : factorRefAsigZonas){
			MantenedorFactorRefAsigZonaVO mantenedorFactorRefAsigZonaVO = new MantenedorFactorRefAsigZonaVO();
			mantenedorFactorRefAsigZonaVO.setIdFactorRefAsigZona(factorRefAsigZona.getIdFactorRefAsigZona());
			mantenedorFactorRefAsigZonaVO.setZona(factorRefAsigZona.getZona());
			mantenedorFactorRefAsigZonaVO.setValor(factorRefAsigZona.getValor());
			resultado.add(mantenedorFactorRefAsigZonaVO);
		}
		return resultado;
	}
	
	public List<MantenedorFactorTramoPobrezaVO> getMantenedorFactorTramoPobrezaAll(){
		List<MantenedorFactorTramoPobrezaVO> resultado = new ArrayList<MantenedorFactorTramoPobrezaVO>();
		List<FactorTramoPobreza> factorTramoPobrezas = mantenedoresDAO.getFactorTramoPobrezaAll();
		for(FactorTramoPobreza factorTramoPobreza : factorTramoPobrezas){
			MantenedorFactorTramoPobrezaVO mantenedorFactorTramoPobrezaVO = new MantenedorFactorTramoPobrezaVO();
			mantenedorFactorTramoPobrezaVO.setIdFactorTramoPobreza(factorTramoPobreza.getIdFactorTramoPobreza());
			mantenedorFactorTramoPobrezaVO.setValor(factorTramoPobreza.getValor());
			resultado.add(mantenedorFactorTramoPobrezaVO);
		}
		return resultado;
	}
	
	public List<MantenedorUsuarioVO> getMantenedorUsuarioAll(){
		List<MantenedorUsuarioVO> resultado = new ArrayList<MantenedorUsuarioVO>();
		List<Usuario> usuarios = usuarioDAO.getUserAll();
		for(Usuario usuario : usuarios){
			MantenedorUsuarioVO mantenedorUsuarioVO = new MantenedorUsuarioVO();
			mantenedorUsuarioVO.setUsername(usuario.getUsername());
			mantenedorUsuarioVO.setNombre(usuario.getNombre());
			mantenedorUsuarioVO.setApellido(usuario.getApellido());
			mantenedorUsuarioVO.setIdEmail(usuario.getEmail().getIdEmail());
			mantenedorUsuarioVO.setEmail(usuario.getEmail().getValor());
			mantenedorUsuarioVO.setIdServicio(usuario.getServicio().getId());
			mantenedorUsuarioVO.setNombreServicio(usuario.getServicio().getNombre());
			
			List<String> nombreRoles = getRolesPorUsuario(usuario.getUsername());
			List<String> nombreRolesFaltantes = getRolesFaltantesUsuario(usuario.getUsername());
			
			mantenedorUsuarioVO.setNombreRoles(nombreRoles);
			mantenedorUsuarioVO.setNombreRolesFaltantes(nombreRolesFaltantes);
			
			resultado.add(mantenedorUsuarioVO);
		}
		return resultado;
	}
	
	public List<MantenedorAnoVO> getMantenedorAnoAll(){
		List<MantenedorAnoVO> resultado = new ArrayList<MantenedorAnoVO>();
		List<AnoEnCurso> anos = anoDAO.getAllAnosOrdenados();
		for(AnoEnCurso ano : anos){
			MantenedorAnoVO mantenedorAnoVO = new MantenedorAnoVO();
			mantenedorAnoVO.setAno(ano.getAno());
			mantenedorAnoVO.setMontoPercapitalBasal(ano.getMontoPercapitalBasal());
			mantenedorAnoVO.setAsignacionAdultoMayor(ano.getAsignacionAdultoMayor());
			mantenedorAnoVO.setInflactor(ano.getInflactor());
			mantenedorAnoVO.setInflactorMarcoPresupuestario(ano.getInflactorMarcoPresupuestario());
			resultado.add(mantenedorAnoVO);
		}
		
		return resultado;
	}
	
	public List<String> getRolesPorUsuario(String username){
		Usuario usuario = usuarioDAO.getUserByUsername(username);
		List<String> nombreRoles = new ArrayList<String>();
		for(Rol rol : usuario.getRols()){
			nombreRoles.add(rol.getNombre());
		}
		return nombreRoles;
	}
	
	public List<String> getRolesFaltantesUsuario(String username){
		Usuario usuario = usuarioDAO.getUserByUsername(username);
		List<String> rolesBorrar = getRolesPorUsuario(username);
		List<String> nombreRolesFinal = new ArrayList<String>();
		List<Rol> roles = rolDAO.getRoles();
		for(Rol rol : roles){
			nombreRolesFinal.add(rol.getNombre());
		}
		
		for(int i=0;i<nombreRolesFinal.size();i++){
			for(int j = 0; j < rolesBorrar.size(); j++){
				if(rolesBorrar.get(j).equals(nombreRolesFinal.get(i))){
					nombreRolesFinal.remove(i);
				}
			}
		}
		for(String nombre : nombreRolesFinal){
			System.out.println("roles que no tiene el usuario --> "+nombre);
		}
		
		return nombreRolesFinal;
	}
	
	public List<String> getNombreRolesAll(){
		List<String> nombreRolesFinal = new ArrayList<String>();
		List<Rol> roles = rolDAO.getRoles();
		for(Rol rol : roles){
			nombreRolesFinal.add(rol.getNombre());
		}
		return nombreRolesFinal;
	}
	
	public List<MantenedorProgramaVO> getAllMantenedorProgramaVO(){
		List<MantenedorProgramaVO> resultado = new ArrayList<MantenedorProgramaVO>();
		List<Programa> programas = programasDAO.getAllProgramas();
		
		for(Programa programa : programas){
			ProgramaAno programaAno = mantenedoresDAO.getProgramaAnoByIdPrograma(programa.getId());
			if(programaAno == null){
				programaAno = new ProgramaAno();
			}
			
			MantenedorProgramaVO mantenedorProgramaVO = new MantenedorProgramaVO();
			mantenedorProgramaVO.setIdPrograma(programa.getId());
			mantenedorProgramaVO.setIdProgramaAno(programaAno.getIdProgramaAno());
			mantenedorProgramaVO.setNombrePrograma(programa.getNombre());
			mantenedorProgramaVO.setNombreUsuario(programa.getUsuario().getUsername());
			mantenedorProgramaVO.setCuotas(programa.getCantidadCuotas());
			mantenedorProgramaVO.setDescripcion(programa.getDescripcion());
			mantenedorProgramaVO.setReliquidacion(programa.isReliquidacion());
			mantenedorProgramaVO.setFonasa(programa.getRevisaFonasa());
			resultado.add(mantenedorProgramaVO);
		}
		
		return resultado;
	}
	
	
	public List<String> getNombresComponentesProgramaAll(){
		List<String> resultado = new ArrayList<String>();
		
		List<Componente> componentes = componenteDAO.getComponentes();
		for(Componente componente : componentes){
			resultado.add(componente.getNombre());
		}
		
		return resultado;
	}
	
	public List<String> getNombresComponentesByPrograma(Integer idPrograma){
		List<String> resultado = new ArrayList<String>();
		Programa programa = programasDAO.getProgramaById(idPrograma);
		for(Componente componente : programa.getComponentes()){
			resultado.add(componente.getNombre());
		}
		return resultado;
	}
	
	public List<String> getNombresComponentesFaltantesByPrograma(Integer idPrograma){
		List<String> resultado = new ArrayList<String>();
		Programa programa = programasDAO.getProgramaById(idPrograma);
		List<String> componentesBorrar = new ArrayList<String>();
		
		for(Componente componente : programa.getComponentes()){
			componentesBorrar.add(componente.getNombre());
		}
		
		for(Componente componente : componenteDAO.getComponentes()){
			resultado.add(componente.getNombre());
		}
		
		for(int i=0; i<resultado.size(); i++){
			for(int j=0; j<componentesBorrar.size(); j++){
				if(resultado.get(i).equalsIgnoreCase(componentesBorrar.get(j))){
					resultado.remove(i);
				}
			}
		}
		
		return resultado;
	}
	
	public List<MantenedorCumplimientoVO> getMantenedorCumplimientoVOAll(){
		List<MantenedorCumplimientoVO> resultado = new ArrayList<MantenedorCumplimientoVO>();
		List<Cumplimiento> cumplimientos = mantenedoresDAO.getCumplimientoAll();
		for(Cumplimiento cumplimiento : cumplimientos){
			MantenedorCumplimientoVO mantenedorCumplimientoVO = new MantenedorCumplimientoVO();
			
			mantenedorCumplimientoVO.setIdCumplimiento(cumplimiento.getIdCumplimiento());
			
			Tramo tramo = mantenedoresDAO.getTramoById(cumplimiento.getTramo().getIdTramo());
			mantenedorCumplimientoVO.setIdTramo(tramo.getIdTramo());
			mantenedorCumplimientoVO.setTramo(tramo.getTramo());
			
			TipoCumplimiento tipoCumplimiento = mantenedoresDAO.getTipoCumplimientoById(cumplimiento.getTipoCumplimiento().getIdTipoCumplimiento());
			mantenedorCumplimientoVO.setIdTipoCumplimiento(tipoCumplimiento.getIdTipoCumplimiento());
			mantenedorCumplimientoVO.setTipoCumplimiento(tipoCumplimiento.getDescripcion());
			
			mantenedorCumplimientoVO.setRebaja(cumplimiento.getRebaja());
			mantenedorCumplimientoVO.setPorcentajeDesde(cumplimiento.getPorcentajeDesde());
			mantenedorCumplimientoVO.setPorcentajeHasta(cumplimiento.getPorcentajeHasta());
			resultado.add(mantenedorCumplimientoVO);
		}
		
		
		return resultado;
		
	}
	
	
	
}


