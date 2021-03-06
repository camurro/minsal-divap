package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.AnoDAO;
import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RebajaDAO;
import minsal.divap.dao.RemesasDAO;
import minsal.divap.dao.RolDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.EstadoUsuarioEnum;
import minsal.divap.enums.TipoComponenteEnum;
import minsal.divap.model.mappers.PersonaMapper;
import minsal.divap.model.mappers.ServicioMapper;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.DependenciaVO;
import minsal.divap.vo.FactorRefAsigZonaVO;
import minsal.divap.vo.FactorTramoPobrezaVO;
import minsal.divap.vo.FechaRemesaVO;
import minsal.divap.vo.MantenedorAnoVO;
import minsal.divap.vo.MantenedorComponenteVO;
import minsal.divap.vo.MantenedorComunaFinalVO;
import minsal.divap.vo.MantenedorCumplimientoVO;
import minsal.divap.vo.MantenedorCuotasVO;
import minsal.divap.vo.MantenedorEstablecimientoVO;
import minsal.divap.vo.MantenedorEstadoProgramaVO;
import minsal.divap.vo.MantenedorFactorRefAsigZonaVO;
import minsal.divap.vo.MantenedorFactorTramoPobrezaVO;
import minsal.divap.vo.MantenedorProgramaVO;
import minsal.divap.vo.MantenedorRegionVO;
import minsal.divap.vo.MantenedorTipoSubtituloVO;
import minsal.divap.vo.MantenedorUsuarioVO;
import minsal.divap.vo.PersonaMantenedorVO;
import minsal.divap.vo.PersonaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.RegionSummaryVO;
import minsal.divap.vo.RolVO;
import minsal.divap.vo.ServiciosMantenedorVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.vo.TipoComponenteVO;
import minsal.divap.vo.TipoComunaVO;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.Dependencia;
import cl.minsal.divap.model.DetalleRemesas;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.FactorRefAsigZona;
import cl.minsal.divap.model.FactorTramoPobreza;
import cl.minsal.divap.model.FechaRemesa;
import cl.minsal.divap.model.Persona;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaComponente;
import cl.minsal.divap.model.ProgramaFechaRemesa;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.Rol;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoComponente;
import cl.minsal.divap.model.TipoComuna;
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
	private ProgramasService programasService;
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
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private CajaDAO cajaDAO;
	@EJB
	private EstimacionFlujoCajaDAO estimacionFlujoCajaDAO;
	@EJB
	private ComponenteService componenteService;
	@EJB
	private RemesasDAO remesasDAO;
	
	
	private final double EPSILON = 0.0000001;

	private boolean equals(double a, double b) {
		return equals(a, b, EPSILON);
	}

	private boolean equals(double a, double b, double epsilon) {
		return a == b ? true : Math.abs(a - b) < epsilon;
	}

	private boolean greaterThan(double a, double b) {
		return greaterThan(a, b, EPSILON);
	}

	private boolean greaterThan(double a, double b, double epsilon) {
		return a - b > epsilon;
	}

	private boolean lessThan(double a, double b) {
		return lessThan(a, b, EPSILON);
	}

	private boolean lessThan(double a, double b, double epsilon) {
		return b - a > epsilon;
	}

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

	public List<PersonaVO> getAllPersonas() {
		List<Persona> personas = servicioSaludDAO.getAllPersonas();
		List<PersonaVO> personasVO = new ArrayList<PersonaVO>();
		if (personas != null && personas.size() > 0) {
			for (Persona p : personas) {
				PersonaVO personaVO = new PersonaMapper().getBasic(p);
				personasVO.add(personaVO);

			}
		}
		return personasVO;
	}

	public List<MantenedorRegionVO> getAllMantenedorRegionesVO() {
		List<Region> regiones = servicioSaludDAO.getAllRegion();
		List<MantenedorRegionVO> mantenedoresRegionVO = new ArrayList<MantenedorRegionVO>();
		if (regiones != null && regiones.size() > 0) {
			for (Region region : regiones) {
				MantenedorRegionVO mantenedorRegionVO = new MantenedorRegionVO();
				Boolean puedeEliminarse = false;
					
				if (region.getServicioSaluds() == null || region.getServicioSaluds().size() == 0) {
					puedeEliminarse = true;
				}
				
				mantenedorRegionVO.setPuedeEliminarse(puedeEliminarse);
				mantenedorRegionVO.setIdRegion(region.getId());
				mantenedorRegionVO.setNombreRegion(region.getNombre());
				PersonaMantenedorVO secretarioRegional = new PersonaMantenedorVO();
				secretarioRegional.setNombre(region.getSecretarioRegional().getNombre());
				secretarioRegional.setIdPersona(region.getSecretarioRegional().getIdPersona());
				secretarioRegional.setApellidoPaterno(region.getSecretarioRegional().getApellidoPaterno());
				secretarioRegional.setApellidoMaterno(region.getSecretarioRegional().getApellidoMaterno());
				secretarioRegional.setIdCorreo(region.getSecretarioRegional().getEmail().getIdEmail());
				secretarioRegional.setCorreo(region.getSecretarioRegional().getEmail().getValor());
				mantenedorRegionVO.setSecretarioRegional(secretarioRegional);
				mantenedoresRegionVO.add(mantenedorRegionVO);
			}
		}
		return mantenedoresRegionVO;
	}

//	public MantenedorRegionVO getMantenedorRegioVOById(Integer idRegion) {
//		Region region = servicioSaludDAO.getRegionById(idRegion);
//		MantenedorRegionVO mantenedorRegionVO = new MantenedorRegionVO();
//		// mantenedorRegionVO.setIdRegion(region.getId());
//		mantenedorRegionVO.setNombreRegion(region.getNombre());
//		mantenedorRegionVO.setIdSecretarioRegional(region
//				.getSecretarioRegional().getIdPersona());
//		mantenedorRegionVO.setSecretarioRegional(region.getSecretarioRegional()
//				.getNombre()
//				+ " "
//				+ region.getSecretarioRegional().getApellidoPaterno());
//		return mantenedorRegionVO;
//	}

	public List<String> getSubtitulosNombres() {
		List<TipoSubtitulo> tipoSubtitulos = this.tipoSubtituloDAO
				.getTipoSubtituloAll();
		List<String> nombreSubtitulos = new ArrayList<String>();
		if (tipoSubtitulos != null && tipoSubtitulos.size() > 0) {
			for (TipoSubtitulo tipoSubtitulo : tipoSubtitulos) {
				nombreSubtitulos.add(tipoSubtitulo.getNombreSubtitulo());
			}
		}
		return nombreSubtitulos;

	}

	public List<String> getNombreSubtitulosByComponente(Integer idComponente) {
		List<ComponenteSubtitulo> componenteSubtitulos = this.tipoSubtituloDAO
				.getByIdComponente(idComponente);
		List<String> nombreSubtitulos = new ArrayList<String>();
		for (ComponenteSubtitulo componenteSubtitulo : componenteSubtitulos) {
			String nombre = null;
			nombre = componenteSubtitulo.getSubtitulo().getNombreSubtitulo();
			nombreSubtitulos.add(nombre);
		}
		return nombreSubtitulos;

	}

	public List<String> getNombreSubtitulosFaltantesComponente(
			Integer idComponente) {
		List<TipoSubtitulo> tipoSubtitulos = this.tipoSubtituloDAO
				.getTipoSubtituloAll();
		List<String> nombresFinal = new ArrayList<String>();
		for (TipoSubtitulo tipoSubtitulo : tipoSubtitulos) {
			nombresFinal.add(tipoSubtitulo.getNombreSubtitulo());
		}
		List<ComponenteSubtitulo> componenteSubtitulos = this.tipoSubtituloDAO
				.getByIdComponente(idComponente);
		List<String> nombreBorrar = new ArrayList<String>();
		for (ComponenteSubtitulo componenteSubtitulo : componenteSubtitulos) {
			String nombre = null;
			nombre = componenteSubtitulo.getSubtitulo().getNombreSubtitulo();
			nombreBorrar.add(nombre);
		}
		for (int i = 0; i < nombresFinal.size(); i++) {

			for (int j = 0; j < nombreBorrar.size(); j++) {
				if (nombresFinal.get(i).equalsIgnoreCase(nombreBorrar.get(j))) {
					nombresFinal.remove(i);
				}
			}
		}
		return nombresFinal;
	}

	public List<MantenedorEstadoProgramaVO> getMantenedorEstadoProgramaAll() {
		List<EstadoPrograma> estadoProgramas = programasDAO
				.getAllEstadoProgramas();
		List<MantenedorEstadoProgramaVO> resultado = new ArrayList<MantenedorEstadoProgramaVO>();
		for (EstadoPrograma estadoPrograma : estadoProgramas) {
			MantenedorEstadoProgramaVO mantenedorEstadoProgramaVO = new MantenedorEstadoProgramaVO();
			mantenedorEstadoProgramaVO.setId_estado_programa(estadoPrograma
					.getIdEstadoPrograma());
			mantenedorEstadoProgramaVO.setNombre_estado(estadoPrograma
					.getNombreEstado());
			resultado.add(mantenedorEstadoProgramaVO);
		}
		return resultado;
	}

	public MantenedorEstadoProgramaVO getMantenedorEstadoProgramaById(
			Integer idEstadoPrograma) {
		EstadoPrograma estadoPrograma = programasDAO
				.getEstadoProgramaById(idEstadoPrograma);
		MantenedorEstadoProgramaVO mantenedorEstadoProgramaVO = new MantenedorEstadoProgramaVO();
		mantenedorEstadoProgramaVO.setId_estado_programa(estadoPrograma
				.getIdEstadoPrograma());
		mantenedorEstadoProgramaVO.setNombre_estado(estadoPrograma
				.getNombreEstado());
		return mantenedorEstadoProgramaVO;

	}

	public List<MantenedorFactorRefAsigZonaVO> getMantenedorFactorRefAsigZonaAll() {
		List<MantenedorFactorRefAsigZonaVO> resultado = new ArrayList<MantenedorFactorRefAsigZonaVO>();
		List<FactorRefAsigZona> factorRefAsigZonas = mantenedoresDAO
				.getFactorRefAsigZonaAllOrderDesde();
		for (FactorRefAsigZona factorRefAsigZona : factorRefAsigZonas) {

			Boolean puedeEliminarse = false;
			MantenedorFactorRefAsigZonaVO mantenedorFactorRefAsigZonaVO = new MantenedorFactorRefAsigZonaVO();
			if (factorRefAsigZona.getAntecendentesComunaCollection() == null
					|| factorRefAsigZona.getAntecendentesComunaCollection()
							.size() == 0) {
				puedeEliminarse = true;
			}

			mantenedorFactorRefAsigZonaVO
					.setIdFactorRefAsigZona(factorRefAsigZona
							.getIdFactorRefAsigZona());
			mantenedorFactorRefAsigZonaVO.setZonaDesde(factorRefAsigZona
					.getZonaDesde());
			mantenedorFactorRefAsigZonaVO
					.setValor(factorRefAsigZona.getValor());
			mantenedorFactorRefAsigZonaVO.setZonaHasta(factorRefAsigZona
					.getZonaHasta());
			mantenedorFactorRefAsigZonaVO.setPuedeEliminarse(puedeEliminarse);
			resultado.add(mantenedorFactorRefAsigZonaVO);
		}
		return resultado;
	}

	public List<MantenedorFactorTramoPobrezaVO> getMantenedorFactorTramoPobrezaAll() {
		List<MantenedorFactorTramoPobrezaVO> resultado = new ArrayList<MantenedorFactorTramoPobrezaVO>();
		List<FactorTramoPobreza> factorTramoPobrezas = mantenedoresDAO
				.getFactorTramoPobrezaAll();
		for (FactorTramoPobreza factorTramoPobreza : factorTramoPobrezas) {
			Boolean puedeEliminarse = false;

			if (factorTramoPobreza.getAntecendentesComunaCollection() == null
					|| factorTramoPobreza.getAntecendentesComunaCollection()
							.size() == 0) {
				puedeEliminarse = true;
			}
			
			MantenedorFactorTramoPobrezaVO mantenedorFactorTramoPobrezaVO = new MantenedorFactorTramoPobrezaVO();
			mantenedorFactorTramoPobrezaVO.setPuedeEliminarse(puedeEliminarse);
			
			mantenedorFactorTramoPobrezaVO.setIdFactorTramoPobreza(factorTramoPobreza.getIdFactorTramoPobreza());
			mantenedorFactorTramoPobrezaVO.setValor(factorTramoPobreza.getValor());
			resultado.add(mantenedorFactorTramoPobrezaVO);
		}
		return resultado;
	}

	public List<MantenedorUsuarioVO> getMantenedorUsuarioAll() {
		List<MantenedorUsuarioVO> resultado = new ArrayList<MantenedorUsuarioVO>();
		List<Usuario> usuarios = usuarioDAO.getAllUserActivos(EstadoUsuarioEnum.ELIMINADO.getId());
		for (Usuario usuario : usuarios) {
			Boolean puedeBorrarse = false;
			if(usuario.getProgramas() != null && usuario.getProgramas().size() > 0){
				puedeBorrarse = false;
			}else{
				puedeBorrarse = true;
			}
			
			MantenedorUsuarioVO mantenedorUsuarioVO = new MantenedorUsuarioVO();
			
			if(usuario.getServicio() != null){
				mantenedorUsuarioVO.setIdServicioSalud(usuario.getServicio().getId().toString());
				mantenedorUsuarioVO.setServicioSalud(usuario.getServicio().getNombre());
			}else{
				mantenedorUsuarioVO.setIdServicioSalud("");
				mantenedorUsuarioVO.setServicioSalud("");
			}
			
			mantenedorUsuarioVO.setUsername(usuario.getUsername());
			mantenedorUsuarioVO.setNombre(usuario.getNombre());
			mantenedorUsuarioVO.setApellido(usuario.getApellido());
			mantenedorUsuarioVO.setPuedeEliminarse(puedeBorrarse);
			
			if(usuario.getEmail() != null){
				mantenedorUsuarioVO.setIdEmail(usuario.getEmail().getIdEmail());
				mantenedorUsuarioVO.setEmail(usuario.getEmail().getValor());
			}

			List<String> nombreRoles = getRolesPorUsuario(usuario.getUsername());
			List<String> nombreRolesFaltantes = getRolesFaltantesUsuario(usuario.getUsername());

			mantenedorUsuarioVO.setNombreRoles(nombreRoles);
			mantenedorUsuarioVO.setNombreRolesFaltantes(nombreRolesFaltantes);

			resultado.add(mantenedorUsuarioVO);
		}
		return resultado;
	}

	public List<MantenedorAnoVO> getMantenedorAnoAll() {
		List<MantenedorAnoVO> resultado = new ArrayList<MantenedorAnoVO>();
		List<AnoEnCurso> anos = anoDAO.getAllAnosOrdenados();
		for (AnoEnCurso ano : anos) {
			
			Boolean puedeEliminarse = false;
			
			if(ano.getAntecendentesComunas() == null || ano.getAntecendentesComunas().size() == 0){
				puedeEliminarse = true;
			}
			
			MantenedorAnoVO mantenedorAnoVO = new MantenedorAnoVO();
			mantenedorAnoVO.setPuedeEliminarse(puedeEliminarse);
			mantenedorAnoVO.setAno(ano.getAno());
			mantenedorAnoVO.setMontoPercapitalBasal(ano
					.getMontoPercapitalBasal());
			mantenedorAnoVO.setAsignacionAdultoMayor(ano
					.getAsignacionAdultoMayor());
			mantenedorAnoVO.setInflactor(ano.getInflactor());
			resultado.add(mantenedorAnoVO);
		}

		return resultado;
	}

	public List<String> getRolesPorUsuario(String username) {
		Usuario usuario = usuarioDAO.getUserByUsername(username);
		List<String> nombreRoles = new ArrayList<String>();
		for (Rol rol : usuario.getRols()) {
			nombreRoles.add(rol.getNombre());
		}
		return nombreRoles;
	}

	public List<String> getRolesFaltantesUsuario(String username) {
		Usuario usuario = usuarioDAO.getUserByUsername(username);
		List<String> rolesBorrar = getRolesPorUsuario(username);
		List<String> nombreRolesFinal = new ArrayList<String>();
		List<Rol> roles = rolDAO.getRoles();
		for (Rol rol : roles) {
			nombreRolesFinal.add(rol.getNombre());
		}

		for (int i = 0; i < nombreRolesFinal.size(); i++) {
			for (int j = 0; j < rolesBorrar.size(); j++) {
				if (rolesBorrar.get(j).equals(nombreRolesFinal.get(i))) {
					nombreRolesFinal.remove(i);
				}
			}
		}
		for (String nombre : nombreRolesFinal) {
			System.out.println("roles que no tiene el usuario --> " + nombre);
		}

		return nombreRolesFinal;
	}

	public List<String> getNombreRolesAll() {
		List<String> nombreRolesFinal = new ArrayList<String>();
		List<Rol> roles = rolDAO.getRoles();
		for (Rol rol : roles) {
			nombreRolesFinal.add(rol.getNombre());
		}
		return nombreRolesFinal;
	}

	public List<MantenedorProgramaVO> getAllMantenedorProgramaVO(Integer ano) {
		List<MantenedorProgramaVO> resultado = new ArrayList<MantenedorProgramaVO>();

		for (ProgramaAno programa : programasDAO.getProgramasByAno(ano)) {
			

			MantenedorProgramaVO mantenedorProgramaVO = new MantenedorProgramaVO();
			mantenedorProgramaVO.setIdPrograma(programa.getPrograma().getId());
			mantenedorProgramaVO.setIdProgramaAno(programa.getIdProgramaAno());
			mantenedorProgramaVO.setNombrePrograma(programa.getPrograma().getNombre());
			mantenedorProgramaVO.setNombreUsuario(programa.getPrograma().getUsuario().getUsername());
			mantenedorProgramaVO.setCuotas(programa.getPrograma().getCantidadCuotas());
			mantenedorProgramaVO.setDescripcion(programa.getPrograma().getDescripcion());
			mantenedorProgramaVO.setFonasa(programa.getPrograma().getRevisaFonasa());
			mantenedorProgramaVO.setAno(ano);
			
			
			if(programa.getPrograma().getNombre().equalsIgnoreCase("NUEVO")){
				System.out.println("haga algo");
			}
			
			
			List<String> diaFechasRemesas = new ArrayList<String>();
			List<String> diaFechasRemesasFaltantes = new ArrayList<String>();
			List<String> diaFechasRemesasActuales = new ArrayList<String>();
			
			ProgramaVO programaVO = programasService.getProgramaAno(programa.getIdProgramaAno());
			List<ProgramaFechaRemesa> programaFechaRemsas = new ArrayList<ProgramaFechaRemesa>();
			
			programaFechaRemsas = programasDAO.findRemesasByPrograma(programaVO.getId());
			if(programaFechaRemsas != null || programaFechaRemsas.size() > 0){
				for(ProgramaFechaRemesa programaFechaRemesa : programaFechaRemsas){
					diaFechasRemesas.add(programaFechaRemesa.getFechaRemesa().getDia().getId().toString());
					diaFechasRemesasActuales.add(programaFechaRemesa.getFechaRemesa().getDia().getId().toString());
				}
			}
			List<FechaRemesa> fechasRemesasAll = mantenedoresDAO.getAllFechasRemesas();
			for(FechaRemesa fechaRemesaCompleta : fechasRemesasAll){
				diaFechasRemesasFaltantes.add(fechaRemesaCompleta.getDia().getDia().toString());
			}
			
			for(int i = 0; i < diaFechasRemesasFaltantes.size(); i++){
				for(int j = 0; j < diaFechasRemesas.size(); j++){
					if(diaFechasRemesasFaltantes.get(i).equals(diaFechasRemesas.get(j))){
						diaFechasRemesasFaltantes.remove(i);
					}
				}
			}
			mantenedorProgramaVO.setDiaPagoRemesasFaltantes(diaFechasRemesasFaltantes);

			mantenedorProgramaVO.setReliquidacion(programa.getPrograma()
					.isReliquidacion());
			
			List<String> nombreComponentes = new ArrayList<String>();
			if(programa.getProgramaComponentes() == null){
				continue;
			}
			Integer tipoPrograma = TipoComponenteEnum.PXQ.getId();
			
			Boolean puedeEditarseComponentes = true;
			
			for(ProgramaComponente programaComponente : programa.getProgramaComponentes()){
				if(programaComponente.getComponente().getTipoComponente().getId().equals(TipoComponenteEnum.LEY.getId())){
					tipoPrograma = TipoComponenteEnum.LEY.getId();
					puedeEditarseComponentes = false;
					break;
				}
				if(programaComponente.getComponente().getTipoComponente().getId().equals(TipoComponenteEnum.HISTORICO.getId())){
					tipoPrograma = TipoComponenteEnum.HISTORICO.getId();
					break;
				}
				
			}
			List<String> nombreComponentesActuales = new ArrayList<String>();
			for(ProgramaComponente programaComponente : programa.getProgramaComponentes()){
				nombreComponentes.add(programaComponente.getComponente().getNombre().toUpperCase());
				nombreComponentesActuales.add(programaComponente.getComponente().getNombre().toUpperCase());
			}
			
			List<String> componentesByTipoComponenteAll = componenteService.getNombreComponenteByIdTipoComponente(tipoPrograma);
			
			List<String> nombreComponentesFaltantes = this.getComponentesFaltantesEntreDosListas(componentesByTipoComponenteAll, nombreComponentes);
			mantenedorProgramaVO.setComponentesFaltantes(nombreComponentesFaltantes);
			
			
			mantenedorProgramaVO.setPuedeEditarseComponentes(puedeEditarseComponentes);
			mantenedorProgramaVO.setComponentes(nombreComponentes);
			mantenedorProgramaVO.setComponentesActuales(nombreComponentesActuales);
			mantenedorProgramaVO.setIdTipoPrograma(tipoPrograma);
			String dependencia = null;
			
			
			if (programaVO.getDependenciaMunicipal()) {
				dependencia = "Municipal";
				if (programaVO.getDependenciaServicio()) {
					dependencia = "Mixto";
				}
			} else {
				dependencia = "Servicio";
			}
			mantenedorProgramaVO.setDependencia(dependencia);

			mantenedorProgramaVO.setIdEstadoPrograma(programa.getEstado().getIdEstadoPrograma());
			mantenedorProgramaVO.setEstadoPrograma(programa.getEstado().getNombreEstado());
			mantenedorProgramaVO.setIdEstadoFlujoCaja(programa.getEstadoFlujoCaja().getIdEstadoPrograma());
			mantenedorProgramaVO.setEstadoFlujoCaja(programa.getEstadoFlujoCaja().getNombreEstado());
			mantenedorProgramaVO.setIdEstadoreliquidacion(programa.getEstadoreliquidacion().getIdEstadoPrograma());
			mantenedorProgramaVO.setEstadoreliquidacion(programa.getEstadoreliquidacion().getNombreEstado());
			mantenedorProgramaVO.setIdEstadoConvenio(programa.getEstadoConvenio().getIdEstadoPrograma());
			mantenedorProgramaVO.setEstadoConvenio(programa.getEstadoConvenio().getNombreEstado());
			mantenedorProgramaVO.setIdEstadoOT(programa.getEstadoOT().getIdEstadoPrograma());
			mantenedorProgramaVO.setEstadoOT(programa.getEstadoOT().getNombreEstado());
			mantenedorProgramaVO.setIdEstadoModificacionAPS(programa.getEstadoModificacionAPS().getIdEstadoPrograma());
			mantenedorProgramaVO.setEstadoModificacionAPS(programa.getEstadoModificacionAPS().getNombreEstado());
			mantenedorProgramaVO.setDiaPagoRemesas(diaFechasRemesas);
			mantenedorProgramaVO.setDiaPagoRemesasActuales(diaFechasRemesasActuales);
			
			List<Cuota> cuotas = estimacionFlujoCajaDAO.getCuotasByProgramaAno(programa.getIdProgramaAno());
			List<MantenedorCuotasVO> mantenedorCuotasVO = new ArrayList<MantenedorCuotasVO>();
			List<MantenedorCuotasVO> mantenedorCuotasVOActuales = new ArrayList<MantenedorCuotasVO>();
			for(Cuota cuota : cuotas){
				Boolean puedeEliminarse = false;
				
				DetalleRemesas detalleRemesaByCuota = remesasDAO.findDetalleRemesaByIdCuota(cuota.getId());
				if(detalleRemesaByCuota == null){
					puedeEliminarse = true;
				}
				
				MantenedorCuotasVO cuotaVO = new MantenedorCuotasVO();
				cuotaVO.setPuedeEliminarse(puedeEliminarse);
				cuotaVO.setIdCuota(cuota.getId());
				cuotaVO.setNroCuota((int)cuota.getNumeroCuota());
				if(cuota.getFechaPago() != null){
					cuotaVO.setFecha_cuota(cuota.getFechaPago());
				}
				if(cuota.getIdMes() != null){
					cuotaVO.setMes(cuota.getIdMes().getIdMes());
				}
				cuotaVO.setPorcentajeCuota(cuota.getPorcentaje());
				mantenedorCuotasVO.add(cuotaVO);
				mantenedorCuotasVOActuales.add(new MantenedorCuotasVO(cuotaVO));
			}
			mantenedorProgramaVO.setListaCuotas(mantenedorCuotasVO);
			mantenedorProgramaVO.setListaCuotasActuales(mantenedorCuotasVOActuales);
			resultado.add(mantenedorProgramaVO);
		}

		return resultado;
	}


	public List<String> getComponentesFaltantesEntreDosListas(List<String> listaFinal, List<String> listaNombres){
		List<String> listaResultado = new ArrayList<String>();
		
		for(int i = 0; i < listaFinal.size(); i++){
			String componenteDesdeTodos = listaFinal.get(i);
			for(int j = 0; j < listaNombres.size(); j++){
				String componenteDesdePrograma = listaNombres.get(j);
				if(componenteDesdeTodos.equalsIgnoreCase(componenteDesdePrograma)){
					listaFinal.remove(i);
					i--;
				}
			}
		}
		listaResultado = listaFinal;
		return listaResultado;
	}
	
	public List<MantenedorCumplimientoVO> getMantenedorCumplimientoVOAll() {
		List<MantenedorCumplimientoVO> resultado = new ArrayList<MantenedorCumplimientoVO>();
		List<Cumplimiento> cumplimientos = mantenedoresDAO.getCumplimientoAll();
		for (Cumplimiento cumplimiento : cumplimientos) {
			MantenedorCumplimientoVO mantenedorCumplimientoVO = new MantenedorCumplimientoVO();

			mantenedorCumplimientoVO.setIdCumplimiento(cumplimiento
					.getIdCumplimiento());

			Tramo tramo = mantenedoresDAO.getTramoById(cumplimiento.getTramo()
					.getIdTramo());
			mantenedorCumplimientoVO.setIdTramo(tramo.getIdTramo());
			mantenedorCumplimientoVO.setTramo(tramo.getTramo());

			TipoCumplimiento tipoCumplimiento = mantenedoresDAO
					.getTipoCumplimientoById(cumplimiento.getTipoCumplimiento()
							.getIdTipoCumplimiento());
			mantenedorCumplimientoVO.setIdTipoCumplimiento(tipoCumplimiento
					.getIdTipoCumplimiento());
			mantenedorCumplimientoVO.setTipoCumplimiento(tipoCumplimiento
					.getDescripcion());

			mantenedorCumplimientoVO.setRebaja(cumplimiento.getRebaja());
			mantenedorCumplimientoVO.setPorcentajeDesde(cumplimiento
					.getPorcentajeDesde());
			mantenedorCumplimientoVO.setPorcentajeHasta(cumplimiento
					.getPorcentajeHasta());
			resultado.add(mantenedorCumplimientoVO);
		}

		return resultado;

	}

	public List<MantenedorComunaFinalVO> getAntedentesComunaMantenedor(
			Integer ano) {
		List<MantenedorComunaFinalVO> resultado = new ArrayList<MantenedorComunaFinalVO>();
		List<ServicioSalud> servicios = new ArrayList<ServicioSalud>();
		servicios = servicioSaludDAO.getServiciosOrderId();
		for (ServicioSalud servicio : servicios) {

			List<AntecendentesComuna> antecedentesComunaByServicio = antecedentesComunaDAO
					.getAntecedentesComunaByServicioAno(servicio.getId(), ano);
			if (antecedentesComunaByServicio != null
					&& antecedentesComunaByServicio.size() > 0) {
				for (AntecendentesComuna antecedenteComuna : antecedentesComunaByServicio) {

					MantenedorComunaFinalVO mantenedorComunaFinalVO = new MantenedorComunaFinalVO();
					mantenedorComunaFinalVO.setMostrarBotonesEditViewDelete(false);

					Boolean puedeEliminarse = ((antecedenteComuna
							.getAntecendentesComunaCalculadoCollection() == null || antecedenteComuna
							.getAntecendentesComunaCalculadoCollection().size() == 0) ? true
							: false);

					if (puedeEliminarse) {
						puedeEliminarse = ((antecedenteComuna.getIdComuna()
								.getProgramaMunicipalCores() == null || antecedenteComuna
								.getIdComuna().getProgramaMunicipalCores()
								.size() == 0) ? true : false);
					}

					
					mantenedorComunaFinalVO.setAno(ano);
					mantenedorComunaFinalVO.setPuedeEliminarse(puedeEliminarse);
					if (antecedenteComuna.getClasificacion() != null) {
						mantenedorComunaFinalVO.setIdClasificacion(antecedenteComuna.getClasificacion().getIdTipoComuna().toString());
						mantenedorComunaFinalVO.setClasificacion(antecedenteComuna.getClasificacion().getDescripcion());
					}else{
						mantenedorComunaFinalVO.setIdClasificacion("");
						mantenedorComunaFinalVO.setClasificacion("");
					}

					if (antecedenteComuna.getAsignacionZona() != null) {
						mantenedorComunaFinalVO.setIdAsigZona(antecedenteComuna.getAsignacionZona().getIdFactorRefAsigZona().toString());
						mantenedorComunaFinalVO.setAsigZonaValor(antecedenteComuna.getAsignacionZona().getValor());
					}else{
						mantenedorComunaFinalVO.setIdAsigZona("No asignada");
					}
					if (antecedenteComuna.getTramoPobreza() != null) {
						mantenedorComunaFinalVO.setIdTramoPobreza(antecedenteComuna.getTramoPobreza().getIdFactorTramoPobreza().toString());
						mantenedorComunaFinalVO.setTramoPobreza(antecedenteComuna.getTramoPobreza().getValor());
					}else{
						mantenedorComunaFinalVO.setIdTramoPobreza("No asignado");
					}
					
					
					mantenedorComunaFinalVO
							.setIdAntecedentesComuna(antecedenteComuna
									.getIdAntecedentesComuna());
					mantenedorComunaFinalVO.setIdComuna(antecedenteComuna
							.getIdComuna().getId());
					mantenedorComunaFinalVO.setNombreComuna(antecedenteComuna
							.getIdComuna().getNombre());
					mantenedorComunaFinalVO.setIdServicio(antecedenteComuna
							.getIdComuna().getServicioSalud().getId()
							.toString());
					mantenedorComunaFinalVO.setNombreServicio(antecedenteComuna
							.getIdComuna().getServicioSalud().getNombre());
					mantenedorComunaFinalVO.setComunaAuxiliar(false);
					resultado.add(mantenedorComunaFinalVO);

				}
				List<Comuna> comunasAuxiliaresServicio = comunaDAO
						.getComunaServicioAuxiliares(servicio.getId());
				for (Comuna comunaAuxiliarServicio : comunasAuxiliaresServicio) {
					Boolean puedeEliminarse = ((comunaAuxiliarServicio
							.getProgramaMunicipalCores() == null || comunaAuxiliarServicio
							.getProgramaMunicipalCores().size() == 0) ? true
							: false);

					MantenedorComunaFinalVO mantenedorComunaAuxiliar = new MantenedorComunaFinalVO();
					mantenedorComunaAuxiliar
							.setPuedeEliminarse(puedeEliminarse);
					mantenedorComunaAuxiliar.setAno(ano);
					mantenedorComunaAuxiliar.setIdServicio(servicio.getId()
							.toString());
					mantenedorComunaAuxiliar.setNombreServicio(servicio
							.getNombre());
					mantenedorComunaAuxiliar.setIdComuna(comunaAuxiliarServicio
							.getId());
					mantenedorComunaAuxiliar
							.setNombreComuna(comunaAuxiliarServicio.getNombre());
					mantenedorComunaAuxiliar.setAno(ano);

					mantenedorComunaAuxiliar.setIdClasificacion("");
					mantenedorComunaAuxiliar.setClasificacion("No aplica");
					mantenedorComunaAuxiliar.setIdAsigZona("");
					mantenedorComunaAuxiliar.setAsigZonaValor(0.0);
					mantenedorComunaAuxiliar.setIdAsigZona("");
					mantenedorComunaAuxiliar.setAsigZonaValor(0.0);
					mantenedorComunaAuxiliar.setIdAntecedentesComuna(-1);
					mantenedorComunaAuxiliar.setComunaAuxiliar(true);

					resultado.add(mantenedorComunaAuxiliar);
				}
			}
		}

		return resultado;

	}

	public void copyAntedentesComunaAnoActualToAnoSiguiente(Integer ano) {
		for (ServicioSalud servicio : servicioSaludDAO.getServiciosOrderId()) {
			// Obtengo los del año actual
			List<AntecendentesComuna> antecedentesComunaByServicio = antecedentesComunaDAO
					.getAntecedentesComunaByServicioAno(servicio.getId(),
							(ano - 1));
			if (antecedentesComunaByServicio != null
					&& antecedentesComunaByServicio.size() > 0) {
				for (AntecendentesComuna antecedenteComuna : antecedentesComunaByServicio) {
					AntecendentesComuna antecedenteComunaAnoSiguiente = new AntecendentesComuna();

					AnoEnCurso anoSiguiente = new AnoEnCurso();
					anoSiguiente.setAno(ano);
					antecedenteComunaAnoSiguiente
							.setAnoAnoEnCurso(anoSiguiente);

					antecedenteComunaAnoSiguiente.setIdComuna(antecedenteComuna
							.getIdComuna());

					if (antecedenteComuna.getClasificacion() != null) {
						antecedenteComunaAnoSiguiente
								.setClasificacion(antecedenteComuna
										.getClasificacion());
					}
					if (antecedenteComuna.getAsignacionZona() != null) {
						antecedenteComunaAnoSiguiente
								.setAsignacionZona(antecedenteComuna
										.getAsignacionZona());
					}
					if (antecedenteComuna.getTramoPobreza() != null) {
						antecedenteComunaAnoSiguiente
								.setTramoPobreza(antecedenteComuna
										.getTramoPobreza());
					}
					antecedenteComunaAnoSiguiente.setNumeroResolucion(null);
					antecedentesComunaDAO.save(antecedenteComunaAnoSiguiente);
				}
			}
		}
	}

	public List<FactorRefAsigZonaVO> getAllFactorRefAsigZonaVO() {
		List<FactorRefAsigZonaVO> resultado = new ArrayList<FactorRefAsigZonaVO>();
		List<FactorRefAsigZona> listRefAsigZona = mantenedoresDAO.getFactorRefAsigZonaAllOrderDesde();
		for (FactorRefAsigZona asig : listRefAsigZona) {
			FactorRefAsigZonaVO vo = new FactorRefAsigZonaVO();
			vo.setIdFactorRefAsigZona(asig.getIdFactorRefAsigZona());
			vo.setValor(asig.getValor());
			vo.setZonaDesde(asig.getZonaDesde());
			vo.setZonaHasta(asig.getZonaHasta());
			resultado.add(vo);
		}
		return resultado;
	}

	public List<FactorTramoPobrezaVO> getAllFactorTramoPobreza() {
		List<FactorTramoPobrezaVO> resultado = new ArrayList<FactorTramoPobrezaVO>();
		List<FactorTramoPobreza> listTramoPobreza = antecedentesComunaDAO
				.findAllTramoPobreza();
		for (FactorTramoPobreza pobreza : listTramoPobreza) {
			FactorTramoPobrezaVO vo = new FactorTramoPobrezaVO();
			vo.setIdFactorTramoPobreza(pobreza.getIdFactorTramoPobreza());
			vo.setValor(pobreza.getValor());
			resultado.add(vo);
		}
		return resultado;

	}

	public List<TipoComunaVO> getAllTipoComunas() {
		List<TipoComunaVO> resultado = new ArrayList<TipoComunaVO>();
		List<TipoComuna> comunas = antecedentesComunaDAO.findAllTipoComunas();
		for (TipoComuna tipoComuna : comunas) {
			TipoComunaVO vo = new TipoComunaVO();
			vo.setIdTipoComuna(tipoComuna.getIdTipoComuna());
			vo.setDescripcion(tipoComuna.getDescripcion());
			resultado.add(vo);
		}
		return resultado;
	}

	public List<String> getNombresComponentes() {
		List<String> nombreComponentes = new ArrayList<String>();
		List<Componente> componentes = this.componenteDAO.getComponentes();
		if (componentes != null && componentes.size() > 0) {
			for (Componente componente : componentes) {
				nombreComponentes.add(componente.getNombre());
			}
		}
		return nombreComponentes;
	}

	public List<FechaRemesaVO> getDiasFechaRemesas() {
		List<FechaRemesaVO> diasFechaRemesas = new ArrayList<FechaRemesaVO>();
		List<FechaRemesa> fechaRemesas = mantenedoresDAO.getAllFechasRemesas();
		for (FechaRemesa fechaRemesa : fechaRemesas) {
			FechaRemesaVO fechaRemesaVO = new FechaRemesaVO();
			fechaRemesaVO.setId(fechaRemesa.getId());
			fechaRemesaVO.setDia(fechaRemesa.getDia().getDia());
			diasFechaRemesas.add(fechaRemesaVO);
		}
		return diasFechaRemesas;
	}
	
	public List<String> getDiasNumeroFechaRemesas() {
		List<String> diasFechaRemesas = new ArrayList<String>();
		List<FechaRemesa> fechaRemesas = mantenedoresDAO.getAllFechasRemesas();
		for (FechaRemesa fechaRemesa : fechaRemesas) {
			diasFechaRemesas.add(fechaRemesa.getDia().getDia().toString());
		}
		return diasFechaRemesas;
	}

	public List<TipoComponenteVO> getTiposComponente() {
		List<TipoComponenteVO> resultado = new ArrayList<TipoComponenteVO>();
		List<TipoComponente> componentes = componenteDAO.getAllTipoComponente();
		for (TipoComponente tipoComponente : componentes) {
			TipoComponenteVO tipoComponenteVO = new TipoComponenteVO();
			tipoComponenteVO.setId(tipoComponente.getId());
			tipoComponenteVO.setNombre(tipoComponente.getNombre());
			resultado.add(tipoComponenteVO);
		}
		return resultado;
	}

	public TipoComponenteVO getTipoComponenteById(Integer idTipoComponente) {
		TipoComponente tipoComponente = componenteDAO
				.getTipoComponenteById(idTipoComponente);
		TipoComponenteVO tipoComponenteVO = new TipoComponenteVO();
		tipoComponenteVO.setId(tipoComponente.getId());
		tipoComponenteVO.setNombre(tipoComponente.getNombre());

		return tipoComponenteVO;
	}
	

//	public String puedeInsertarseTramoAsigZona(List<MantenedorFactorRefAsigZonaVO> listado, MantenedorFactorRefAsigZonaVO nuevo) {
//		String puedeInsertarse = "NO";
//		
//		int count1 = 0;
//		for (MantenedorFactorRefAsigZonaVO existente : listado) {
//			if(count1 == (listado.size() - 1)){
//				// es el ultimo
//				if(existente.getZonaHasta() == null){
//					//tiene un valor, puede compararse
//					if ((lessThan(existente.getZonaDesde(), nuevo.getZonaDesde()) && (lessThan(nuevo.getZonaDesde(), existente.getZonaHasta())))) {
//						puedeInsertarse = "NO";
//						break;
//					} else {
//						puedeInsertarse = "SI";
//					}
//				}else{
//					//no tiene un valor, no puede compararse
//					if(lessThan(existente.getZonaDesde(), nuevo.getZonaDesde())){
//						puedeInsertarse = "NO";
//						break;
//					}else{
//						puedeInsertarse = "SI";
//					}
//				}
//				
//			}else{
//				//no es el ultimo
//				if ((lessThan(existente.getZonaDesde(), nuevo.getZonaDesde()) && (lessThan(
//						nuevo.getZonaDesde(), existente.getZonaHasta())))) {
//					puedeInsertarse = "NO";
//					break;
//				} else {
//					puedeInsertarse = "SI";
//				}
//			}
//			count1 ++;
//		}
//		int count = 0;
//		for (MantenedorFactorRefAsigZonaVO existente : listado) {
//			if (count == (listado.size() - 1)) {
//				// es el ultimo
//				puedeInsertarse = "SI y agregar desde al último tramo";
//			} else {
//				if ((lessThan(existente.getZonaDesde(), nuevo.getZonaHasta()) && (lessThan(
//						nuevo.getZonaHasta(), existente.getZonaHasta())))) {
//					puedeInsertarse = "NO";
//					break;
//				} else {
//					puedeInsertarse = "SI";
//				}
//			}
//			count ++;
//		}
//
//		return puedeInsertarse;
//
//	}
	
	
	public String puedeInsertarseTramoAsigZona(List<MantenedorFactorRefAsigZonaVO> listado, MantenedorFactorRefAsigZonaVO nuevo) {
		String puedeInsertarse = "NO";
		Double ultimoHasta = listado.get(listado.size() - 1).getZonaHasta();
		if(ultimoHasta != null){
			System.out.println("existe ultimoHasta --> "+ultimoHasta);
			if(lessThan(ultimoHasta, nuevo.getZonaDesde())){
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nSE CUMPLE LA CONDICION\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				puedeInsertarse = "SI";
			}else{
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nNOO  SE CUMPLE LA CONDICION\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				puedeInsertarse = "NO";
			}
		}else{
			System.out.println("NO existe ultimoHasta");
			Double ultimoDesde = listado.get(listado.size() - 1).getZonaDesde();
			System.out.println("ultimoDesde --> "+ultimoDesde);
			if(lessThan(ultimoDesde, (nuevo.getZonaDesde() - 1))){
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nSE CUMPLE LA CONDICION\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				puedeInsertarse = "SI y agregar desde al último tramo";
			}else{
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nNOO  SE CUMPLE LA CONDICION\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				puedeInsertarse = "NO";
			}
		}
		return puedeInsertarse;
	}
	
	
	public List<ServiciosMantenedorVO> getServiciosMantenedorOrderId() {
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		List<ServiciosMantenedorVO> serviciosVO = new ArrayList<ServiciosMantenedorVO>();
		if (servicios != null && servicios.size() > 0) {
			for (ServicioSalud servicioSalud : servicios) {
				
				
				
				ServiciosMantenedorVO servicioVO = new ServiciosMantenedorVO();
				Boolean puedeEliminarse = false;
				
				if(servicioSalud.getComunas() == null || servicioSalud.getComunas().size() == 0){
					puedeEliminarse = true;
				}
				
				if(puedeEliminarse){
					if(servicioSalud.getEstablecimientos() == null || servicioSalud.getEstablecimientos().size() == 0){
						puedeEliminarse = true;
					}
				}
				
				servicioVO.setPuedeEliminarse(puedeEliminarse);
				servicioVO.setId_servicio(servicioSalud.getId());
				servicioVO.setNombre_servicio(servicioSalud.getNombre());
				RegionSummaryVO region = new RegionSummaryVO();
				region.setId(servicioSalud.getRegion().getId());
				region.setNombre(servicioSalud.getRegion().getNombre());
				
				
				servicioVO.setRegion(region);
				
				PersonaMantenedorVO personaDirectorVO = new PersonaMantenedorVO();
				personaDirectorVO.setIdPersona(servicioSalud.getDirector().getIdPersona());
				personaDirectorVO.setNombre(servicioSalud.getDirector().getNombre());
				personaDirectorVO.setApellidoPaterno(servicioSalud.getDirector().getApellidoPaterno());
				personaDirectorVO.setApellidoMaterno(servicioSalud.getDirector().getApellidoMaterno());
				personaDirectorVO.setCorreo(servicioSalud.getDirector().getEmail().getValor());
				personaDirectorVO.setIdCorreo(servicioSalud.getDirector().getEmail().getIdEmail());
				servicioVO.setDirector(personaDirectorVO);
				
				PersonaMantenedorVO personaEncargadoApsVO = new PersonaMantenedorVO();
				personaEncargadoApsVO.setIdPersona(servicioSalud.getEncargadoAps().getIdPersona());
				personaEncargadoApsVO.setNombre(servicioSalud.getEncargadoAps().getNombre());
				personaEncargadoApsVO.setApellidoPaterno(servicioSalud.getEncargadoAps().getApellidoPaterno());
				personaEncargadoApsVO.setApellidoMaterno(servicioSalud.getEncargadoAps().getApellidoMaterno());
				personaEncargadoApsVO.setCorreo(servicioSalud.getEncargadoAps().getEmail().getValor());
				personaEncargadoApsVO.setIdCorreo(servicioSalud.getEncargadoAps().getEmail().getIdEmail());
				servicioVO.setEncargadoAps(personaEncargadoApsVO);
				
				
				PersonaMantenedorVO personaEncargadoFinanzasApsVO = new PersonaMantenedorVO();
				personaEncargadoFinanzasApsVO.setIdPersona(servicioSalud.getEncargadoFinanzasAps().getIdPersona());
				personaEncargadoFinanzasApsVO.setNombre(servicioSalud.getEncargadoFinanzasAps().getNombre());
				personaEncargadoFinanzasApsVO.setApellidoPaterno(servicioSalud.getEncargadoFinanzasAps().getApellidoPaterno());
				personaEncargadoFinanzasApsVO.setApellidoMaterno(servicioSalud.getEncargadoFinanzasAps().getApellidoMaterno());
				personaEncargadoFinanzasApsVO.setCorreo(servicioSalud.getEncargadoFinanzasAps().getEmail().getValor());
				personaEncargadoFinanzasApsVO.setIdCorreo(servicioSalud.getEncargadoFinanzasAps().getEmail().getIdEmail());
				servicioVO.setEncargadoFinanzasAps(personaEncargadoFinanzasApsVO);
				serviciosVO.add(servicioVO);
				
			}
		}
		return serviciosVO;
	}
	
	public List<TipoComunaVO> getTipoComunas(){
		List<TipoComunaVO> resultado = new ArrayList<TipoComunaVO>();
		List<TipoComuna> tipoComunas = comunaDAO.getAllTipoComuna();
		for(TipoComuna tipoComuna : tipoComunas){
			
			Boolean puedeEliminarse = false;
			if(tipoComuna.getAntecendentesComunaCollection() == null || tipoComuna.getAntecendentesComunaCollection().size() == 0){
				puedeEliminarse = true;
			}
			
			TipoComunaVO tipoComunaVO = new TipoComunaVO();
			tipoComunaVO.setPuedeEliminarse(puedeEliminarse);
			tipoComunaVO.setIdTipoComuna(tipoComuna.getIdTipoComuna());
			tipoComunaVO.setDescripcion(tipoComuna.getDescripcion());
			resultado.add(tipoComunaVO);
		}
		return resultado;
	}
	
	public List<RolVO> getAllRolesVO(){
		List<RolVO> resultado = new ArrayList<RolVO>();
		List<Rol> roles = rolDAO.getRoles();
		for(Rol rol : roles){
			Boolean puedeEliminarse = false;
			if(rol.getUsuarios() == null || rol.getUsuarios().size() == 0){
				puedeEliminarse = true;
			}
			RolVO vo = new RolVO();
			vo.setPuedeBorrarse(puedeEliminarse);
			vo.setNombre(rol.getNombre());
			resultado.add(vo);
		}
		return resultado;
	}
	
	public List<MantenedorComponenteVO> getAllMantenedorComponenteVO(){
		List<MantenedorComponenteVO> resultado = new ArrayList<MantenedorComponenteVO>();
		List<Componente> componentes = componenteDAO.getComponentes();
		
			for(Componente componente : componentes){
				if(componente.getId() < 0){
					continue;
				}
				
				Boolean puedeBorrarse = false;
				if(componente.getProgramaComponentes() == null || componente.getProgramaComponentes().size() == 0){
					puedeBorrarse = true;
				}
				if(puedeBorrarse){
					if(componente.getComponenteSubtitulosComponente() == null || componente.getComponenteSubtitulosComponente().size() == 0){
						puedeBorrarse = true;
					}else{
						puedeBorrarse = false;
					}
				}
				
				MantenedorComponenteVO mantenedorComponenteVO = new MantenedorComponenteVO();
				mantenedorComponenteVO.setPuedeEliminarse(puedeBorrarse);
				mantenedorComponenteVO.setIdComponente(componente.getId());
				mantenedorComponenteVO.setNombreComponente(componente.getNombre());
				mantenedorComponenteVO.setIdTipoComponente(componente.getTipoComponente().getId());
				mantenedorComponenteVO.setNombreTipoComponente(componente.getTipoComponente().getNombre());
				mantenedorComponenteVO.setPeso(componente.getPeso());
				mantenedorComponenteVO.setNombreSubtitulos(this.getNombreSubtitulosByComponente(componente.getId()));
				mantenedorComponenteVO.setNombreSubtitulosFaltantes(this.getNombreSubtitulosFaltantesComponente(componente.getId()));
				resultado.add(mantenedorComponenteVO);
			}
		return resultado;
	}
	
	public List<MantenedorEstablecimientoVO> getAllMantenedorEstablecimientoVO(){
		List<MantenedorEstablecimientoVO> resultado = new ArrayList<MantenedorEstablecimientoVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for(ServicioSalud servicio : servicios){
			for(Establecimiento establecimiento : servicio.getEstablecimientos()){
				
				Boolean puedeEliminarse = false;
				if(programasDAO.getProgramaServicioCoreByIdEstablecimiento(establecimiento.getId()) == null || 
					programasDAO.getProgramaServicioCoreByIdEstablecimiento(establecimiento.getId()).size() == 0){
					puedeEliminarse = true;
				}
				
				MantenedorEstablecimientoVO mantenedorEstablecimientoVO = new MantenedorEstablecimientoVO();
				mantenedorEstablecimientoVO.setPuedeEliminarse(puedeEliminarse);
				mantenedorEstablecimientoVO.setEsAuxiliar(establecimiento.isAuxiliar());
    			mantenedorEstablecimientoVO.setIdEstablecimiento(establecimiento.getId());
    			mantenedorEstablecimientoVO.setNombreEstablecimiento(establecimiento.getNombre());
    			mantenedorEstablecimientoVO.setIdServicio(servicio.getId());
    			mantenedorEstablecimientoVO.setNombreServicio(servicio.getNombre());
    			mantenedorEstablecimientoVO.setCodigo(establecimiento.getCodigo());
    			mantenedorEstablecimientoVO.setTipo(establecimiento.getTipo());
    			resultado.add(mantenedorEstablecimientoVO);
			}
		}
		
		return resultado;
	}
	
	public List<MantenedorTipoSubtituloVO> getAllMantenedorSubtitulos(){
		List<MantenedorTipoSubtituloVO> resultado = new ArrayList<MantenedorTipoSubtituloVO>();
		List<TipoSubtitulo> getTipoSubtitulos = tipoSubtituloDAO.getTipoSubtituloAll();
		for(TipoSubtitulo tipoSub : getTipoSubtitulos){
			Boolean puedeEliminarse = false;
			if(tipoSub.getProgramaMunicipalCoreComponentes() == null || tipoSub.getProgramaMunicipalCoreComponentes().size() == 0){
				puedeEliminarse = true;
			}
			if(puedeEliminarse){
				if(tipoSub.getProgramaServicioCoreComponentes() == null || tipoSub.getProgramaServicioCoreComponentes().size() == 0){
					puedeEliminarse = true;
				}
			}
			if(puedeEliminarse){
				List<Caja> listaCaja = cajaDAO.getByIdSubtitulo(tipoSub.getIdTipoSubtitulo());
				if(listaCaja == null || listaCaja.size() == 0){
					puedeEliminarse = true;
				}else{
					puedeEliminarse = false;
				}
			}
			
			MantenedorTipoSubtituloVO mantenedorTipoSubtituloVO = new MantenedorTipoSubtituloVO();
			mantenedorTipoSubtituloVO.setPuedeEliminarse(puedeEliminarse);
			mantenedorTipoSubtituloVO.setIdSubtitulo(tipoSub.getIdTipoSubtitulo());
			mantenedorTipoSubtituloVO.setNombreSubtitulo(tipoSub.getNombreSubtitulo());
			mantenedorTipoSubtituloVO.setIdDependencia(tipoSub.getDependencia().getIdDependenciaPrograma());
			mantenedorTipoSubtituloVO.setNombreDependencia(tipoSub.getDependencia().getNombre());
			mantenedorTipoSubtituloVO.setInflactor(tipoSub.getInflactor());
			resultado.add(mantenedorTipoSubtituloVO);
		}
		return resultado;
	}
	
	public List<DependenciaVO> getAllDependenciaVO(){
		List<DependenciaVO> resultado = new ArrayList<DependenciaVO>();
		List<Dependencia> dependencias = tipoSubtituloDAO.getDependenciaAll();
		for(Dependencia dependencia : dependencias){
			DependenciaVO dependenciaVO = new DependenciaVO();
			dependenciaVO.setId(dependencia.getIdDependenciaPrograma());
			dependenciaVO.setNombre(dependencia.getNombre());
			resultado.add(dependenciaVO);
		}
		
		
		return resultado;
	}

}
