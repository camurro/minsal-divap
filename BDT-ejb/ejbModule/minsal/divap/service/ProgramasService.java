
package minsal.divap.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.model.mappers.ComponenteMapper;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.DependenciaVO;
import minsal.divap.vo.ProgramaAPSServicioResumenVO;
import minsal.divap.vo.ProgramaAPSVO;
import minsal.divap.vo.ProgramaFonasaVO;
import minsal.divap.vo.ProgramaMunicipalHistoricoVO;
import minsal.divap.vo.ProgramaMunicipalVO;
import minsal.divap.vo.ProgramaServicioHistoricoVO;
import minsal.divap.vo.ProgramaServicioVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ResumenProgramaServiciosVO;
import minsal.divap.vo.ResumenProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloProgramasVO;
import minsal.divap.vo.SubtituloVO;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.ProgramaServicioCore;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;
import cl.minsal.divap.model.TipoComponente;
import cl.minsal.divap.model.TipoSubtitulo;

@Stateless
@LocalBean
public class ProgramasService {
	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private ComponenteDAO componenteDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB ConveniosDAO conveniosDAO;
	@EJB ComunaDAO comunaDAO;
	
	
	

	private ServicioSaludDAO serviciosDAO;
	@EJB
	private TipoSubtituloDAO tipoSubtituloDAO;
	@EJB
	private RecursosFinancierosProgramasReforzamientoService reforzamientoService;
	
	public Programa getProgramasByID(Integer idPrograma) {
		return this.programasDAO.getProgramaPorID(idPrograma);
	}
	
	public ProgramaVO getProgramaAno(Integer idProgramaAno) {
		ProgramaVO programa = new ProgramaMapper().getBasic(this.programasDAO.getProgramaAnoByID(idProgramaAno));
		return programa;
	}
	
	
	public ServiciosVO getServiciosProgramaAno(Integer idProgramaAno) {
		//ServiciosVO servicio = new ServicioMapper().getBasic(this.serviciosDAO.get)
		//.getBasic(this.programasDAO.getProgramaAnoByID(idProgramaAno));
		return null;
	}
	
	
	public List<ProgramaVO> getProgramasByUser(String username) {
		List<ProgramaAno> programas = this.programasDAO.getProgramasByUserAno(username, getAnoCurso());
		List<ProgramaVO> result = new ArrayList<ProgramaVO>();
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				result.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return result;
	}
	
	public List<ProgramaVO> getProgramasByUserAno(String username, Integer ano) {
		List<ProgramaAno> programas = this.programasDAO.getProgramasByUserAno(username, ano);
		List<ProgramaVO> result = new ArrayList<ProgramaVO>();
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				result.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return result;
	}
	
	public Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}
	
	public List<ComponentesVO> getComponenteByPrograma(int programaId) {
		List<Programa> programa = this.programasDAO.getComponenteByPrograma(programaId);
		List<ComponentesVO> componentesPrograma = new ArrayList<ComponentesVO>();
		for (Programa prog : programa){
			for (Componente componente : prog.getComponentes()) {
				ComponentesVO comVO = new ComponentesVO();
				comVO.setId(componente.getId());
				comVO.setNombre(componente.getNombre());
				comVO.setPeso(componente.getPeso());
				
				List<SubtituloVO> subsVO = new ArrayList<SubtituloVO>();
				Iterator<ComponenteSubtitulo> iter = componente.getComponenteSubtitulos().iterator();
				 while(iter.hasNext()) {
					 SubtituloVO sub = new SubtituloVO();
					 ComponenteSubtitulo element = iter.next();
			         sub.setId(element.getSubtitulo().getIdTipoSubtitulo());
			         subsVO.add(sub);
			      }
				
				comVO.setSubtitulos(subsVO);
				
				componentesPrograma.add(comVO);
			}
		}
		return componentesPrograma;
	}

	public Programa getProgramaPorID(int programaId) {
		return this.programasDAO.getProgramaPorID(programaId);
	}
	
	public ProgramaVO getProgramaAnoPorID(int programaAnoId) {
		ProgramaAno programa = this.programasDAO.getProgramasByIdProgramaAno(programaAnoId);
		return new ProgramaMapper().getBasic(programa);
	}
	
	public void guardarEstadoFlujoCaja(Integer idEstado, Integer idProgramaAno) {
		this.programasDAO.guardarEstadoFlujoCaja(idEstado,idProgramaAno);
	}
	
	public void cambiarEstadoProgramaAno(Integer idProgramaAno, EstadosProgramas encurso) {
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		EstadoPrograma estadoPrograma = new EstadoPrograma(encurso.getId());
		programaAno.setEstadoFlujoCaja(estadoPrograma);
		this.programasDAO.saveProgramaAno(programaAno, false);
	}
	
	public List<ProgramaMunicipalVO> findByServicioComponente(Integer idComponente, Integer idServicio, Integer idProgramaAno){
		List<ProgramaMunicipalCoreComponente> result = programasDAO.findByServicioComponente(idComponente, idServicio,idProgramaAno);
		List<ProgramaMunicipalVO> listaProgramaMunicipalVO = new ArrayList<ProgramaMunicipalVO>();
		for (ProgramaMunicipalCoreComponente programaMunicipalCore : result) {
			ProgramaMunicipalVO prog = new ProgramaMunicipalVO();
			prog.setIdComuna(programaMunicipalCore.getProgramaMunicipalCore().getComuna().getId());
			prog.setNombreComuna(programaMunicipalCore.getProgramaMunicipalCore().getComuna().getNombre());
			prog.setPrecio(programaMunicipalCore.getMonto());
			prog.setCantidad(programaMunicipalCore.getCantidad());
			prog.setTotal(programaMunicipalCore.getTarifa());
			prog.setIdComponente(idComponente);
						
			prog.setIdProgramaMunicipalCore(programaMunicipalCore.getProgramaMunicipalCore().getIdProgramaMunicipalCore());
			listaProgramaMunicipalVO.add(prog);
		}
		
		return listaProgramaMunicipalVO;
		
	}
	
	public List<ProgramaServicioVO> findByServicioComponenteServicios(
			Integer idComponente, Integer idServicio) {
		List<ProgramaServicioCoreComponente> result = programasDAO.findByServicioComponenteServicios(idComponente, idServicio);
		List<ProgramaServicioVO> listaProgramaServicioVO = new ArrayList<ProgramaServicioVO>();
		
		for (ProgramaServicioCoreComponente programaServicio : result) {
			ProgramaServicioVO prog = new ProgramaServicioVO();
			prog.setIdEstablecimiento(programaServicio.getProgramaServicioCore1().getEstablecimiento().getId());
			prog.setNombreEstablecimiento(programaServicio.getProgramaServicioCore1().getEstablecimiento().getNombre());
			prog.setIdServicioCore(programaServicio.getProgramaServicioCore1().getIdProgramaServicioCore());
			prog.setIdComponente(programaServicio.getServicioCoreComponente().getId());
			
			SubtituloProgramasVO subProg = new SubtituloProgramasVO();
			subProg.setId(programaServicio.getSubtitulo().getIdTipoSubtitulo());
			subProg.setNombre(programaServicio.getSubtitulo().getNombreSubtitulo());
			subProg.setCantidad(programaServicio.getCantidad());
			subProg.setMonto(programaServicio.getMonto());
			subProg.setTotal(programaServicio.getTarifa());
			
			int posicion = listaProgramaServicioVO.indexOf(prog);
			if(posicion != -1){
				prog = listaProgramaServicioVO.get(posicion);
				
				if(subProg.getId()==1){
					prog.setSubtitulo21(subProg);
				}
				if(subProg.getId()==2){	
					prog.setSubtitulo22(subProg);	
							}
				if(subProg.getId()==4){
					prog.setSubtitulo29(subProg);
				}				
			}else{
				if(subProg.getId()==1){
					prog.setSubtitulo21(subProg);
				}
				if(subProg.getId()==2){
					prog.setSubtitulo22(subProg);	
							}
				if(subProg.getId()==4){
					prog.setSubtitulo29(subProg);
				}
				listaProgramaServicioVO.add(prog);
			}
		}
		return listaProgramaServicioVO;
		
	}
	
	public void guardarProgramaReforzamiento(List<ProgramaMunicipalVO> detalleComunas) {
		
		for(ProgramaMunicipalVO detalle : detalleComunas){
			ProgramaMunicipalCoreComponente programaMunicipalCoreComponente = programasDAO.getProgramaMunicipalCoreComponente(detalle.getIdProgramaMunicipalCore(),detalle.getIdComponente());
			programaMunicipalCoreComponente.setCantidad(detalle.getCantidad());
			programaMunicipalCoreComponente.setMonto(detalle.getPrecio());
			programaMunicipalCoreComponente.setTarifa(detalle.getTotal());
			
		}

	}
	
	public void guardarProgramaHistoricoMunicipal(List<ProgramaMunicipalHistoricoVO> detalleComunas) {
		
		for(ProgramaMunicipalHistoricoVO detalle : detalleComunas){
			ProgramaMunicipalCoreComponente programaMunicipalCoreComponente = programasDAO.getProgramaMunicipalCoreComponente(detalle.getIdProgramaMunicipalCore(),detalle.getIdComponente());
			programaMunicipalCoreComponente.setTarifa(detalle.getTotalAnoActual());
			
		}

	}
	
	public void guardarProgramaHistoricoServicio(List<ProgramaServicioHistoricoVO> detalleEstablecimiento) {
		
		for(ProgramaServicioHistoricoVO detalle : detalleEstablecimiento){
			
			List<ProgramaServicioCoreComponente> programaServicioCoreComponente = programasDAO.getProgramaServicioCoreComponente(detalle.getIdProgramaServicioCore(),detalle.getIdComponente());
			
			for(ProgramaServicioCoreComponente coreComponente : programaServicioCoreComponente){
				if(coreComponente.getSubtitulo().getIdTipoSubtitulo()==1){
					if(detalle.getSubtitulo21()!=null){
						coreComponente.setTarifa(detalle.getSubtitulo21().getTotalFuturo());
					}
				}
				if(coreComponente.getSubtitulo().getIdTipoSubtitulo()==2){
					if(detalle.getSubtitulo22()!=null){
						coreComponente.setTarifa(detalle.getSubtitulo22().getTotalFuturo());
					}
				}
				if(coreComponente.getSubtitulo().getIdTipoSubtitulo()==4){
					if(detalle.getSubtitulo29()!=null){
						coreComponente.setTarifa(detalle.getSubtitulo29().getTotalFuturo());
					}
				}
			}
		}

	}
	
	public void guardarProgramaReforzamientoServicios(
			List<ProgramaServicioVO> listadoEstablecimientos) {
		
		for(ProgramaServicioVO detalle: listadoEstablecimientos){
			
			if(detalle.getSubtitulo21()!=null){
				ProgramaServicioCoreComponente programaServicioCoreComponenteS21 = programasDAO.getProgramaServicioCoreComponenteEstablecimiento(detalle.getIdComponente(), detalle.getIdEstablecimiento(), detalle.getSubtitulo21().getId());
				System.out.println(programaServicioCoreComponenteS21.getProgramaServicioCore1().getEstablecimiento().getNombre());
				System.out.println("cantidad antes:"+programaServicioCoreComponenteS21.getCantidad());
				System.out.println("monto antes:"+programaServicioCoreComponenteS21.getMonto());
				programaServicioCoreComponenteS21.setCantidad(detalle.getSubtitulo21().getCantidad());
				programaServicioCoreComponenteS21.setMonto(detalle.getSubtitulo21().getMonto());
				programaServicioCoreComponenteS21.setTarifa(detalle.getSubtitulo21().getTotal());
				System.out.println("cantidad despues:"+programaServicioCoreComponenteS21.getCantidad());
				System.out.println("monto despues:"+programaServicioCoreComponenteS21.getMonto());
			}
			
			if(detalle.getSubtitulo22()!=null){
				ProgramaServicioCoreComponente programaServicioCoreComponenteS22 = programasDAO.getProgramaServicioCoreComponenteEstablecimiento(detalle.getIdComponente(), detalle.getIdEstablecimiento(), detalle.getSubtitulo22().getId());
				System.out.println(programaServicioCoreComponenteS22.getProgramaServicioCore1().getEstablecimiento().getNombre());
				System.out.println("cantidad antes:"+programaServicioCoreComponenteS22.getCantidad());
				System.out.println("monto antes:"+programaServicioCoreComponenteS22.getMonto());
				programaServicioCoreComponenteS22.setCantidad(detalle.getSubtitulo22().getCantidad());
				programaServicioCoreComponenteS22.setMonto(detalle.getSubtitulo22().getMonto());
				programaServicioCoreComponenteS22.setTarifa(detalle.getSubtitulo22().getTotal());
				System.out.println("cantidad despues:"+programaServicioCoreComponenteS22.getCantidad());
				System.out.println("monto despues:"+programaServicioCoreComponenteS22.getMonto());
			}
			
			if(detalle.getSubtitulo29()!=null){
				ProgramaServicioCoreComponente programaServicioCoreComponenteS29 = programasDAO.getProgramaServicioCoreComponenteEstablecimiento(detalle.getIdComponente(), detalle.getIdEstablecimiento(), detalle.getSubtitulo29().getId());
				System.out.println(programaServicioCoreComponenteS29.getProgramaServicioCore1().getEstablecimiento().getNombre());
				System.out.println("cantidad antes:"+programaServicioCoreComponenteS29.getCantidad());
				System.out.println("monto antes:"+programaServicioCoreComponenteS29.getMonto());
				programaServicioCoreComponenteS29.setCantidad(detalle.getSubtitulo29().getCantidad());
				programaServicioCoreComponenteS29.setMonto(detalle.getSubtitulo29().getMonto());
				programaServicioCoreComponenteS29.setTarifa(detalle.getSubtitulo29().getTotal());
				System.out.println("cantidad despues:"+programaServicioCoreComponenteS29.getCantidad());
				System.out.println("monto despues:"+programaServicioCoreComponenteS29.getMonto());
			}
		
		}
		
	}
	
	public List<ResumenProgramaVO> getResumenMunicipal(Integer idProgramaAno, Integer idSubtitulo){
		List<Object[]> result = programasDAO.getResumenMunicipal(idProgramaAno, idSubtitulo);
		List<ResumenProgramaVO> salida =  new ArrayList<ResumenProgramaVO>();
		if(result != null && result.size() > 0){
			for(Object[] resumen : result){
				ResumenProgramaVO r =  new ResumenProgramaVO();
				Integer idServicio = ((Number)(resumen[0])).intValue();
				String nombreServicio = (String) (resumen[1]);
				Long totalS24 = ((Number)(resumen[2])).longValue();
				r.setIdServicio(idServicio);
				r.setNombreServicio(nombreServicio);
				r.setTotalS24(totalS24);
				salida.add(r);
			}
		}
		return salida;
	}
	
	public List<ResumenProgramaServiciosVO> getResumenServicio(Integer idProgramaAno, Integer id){
		List<ResumenProgramaServiciosVO> salida =  new ArrayList<ResumenProgramaServiciosVO>();
		List<ComponentesVO> componentes = getComponenteByPrograma(id);
		
		for(ComponentesVO compo : componentes){
			
			for(SubtituloVO sub : compo.getSubtitulos()){
				System.out.println("Subtitulo-->" + sub.getId());
				List<Object[]> result = programasDAO.getResumenServicio(idProgramaAno, sub.getId());
				
				if(result != null && result.size() > 0){
					for(Object[] resumen : result){
						ResumenProgramaServiciosVO r =  new ResumenProgramaServiciosVO();
						Integer idServicio = ((Number)(resumen[0])).intValue();
						String nombreServicio = (String) (resumen[1]);
						
						r.setIdServicio(idServicio);
						r.setNombreServicio(nombreServicio);
						
						int posicion = salida.indexOf(r);
						if(posicion != -1){
							r = salida.get(posicion);
							
							if(sub.getId()==1){
								Long totalS21 = ((Number)(resumen[2])).longValue();
								r.setTotalS21(totalS21);
							}
							if(sub.getId()==2){
								Long totalS22 = ((Number)(resumen[2])).longValue();
								r.setTotalS22(totalS22);
							}
							if(sub.getId()==4){
								Long totalS29 = ((Number)(resumen[2])).longValue();
								r.setTotalS29(totalS29);
							}
							
						}else{
							if(sub.getId()==1){
								Long totalS21 = ((Number)(resumen[2])).longValue();
								r.setTotalS21(totalS21);
							}
							if(sub.getId()==2){
								Long totalS22 = ((Number)(resumen[2])).longValue();
								r.setTotalS22(totalS22);
							}
							if(sub.getId()==4){
								Long totalS29 = ((Number)(resumen[2])).longValue();
								r.setTotalS29(totalS29);
							}
							salida.add(r);
						}
					}
				}
			}
		}
		
		return salida;
	}

	public int getIdProgramaAnoAnterior(Integer programaSeleccionado,
			int anoAnterior) {
		Integer id = programasDAO.getIdProgramaAnoAnterior(programaSeleccionado,anoAnterior);
		System.out.println(id);
		if(id == null){
			Programa actual = getProgramaPorID(programaSeleccionado);
			ProgramaAno programaAnoSiguiente = new ProgramaAno();
			AnoEnCurso anoSiguienteDTO = programasDAO.getAnoEnCursoById(getAnoCurso()+1);
			if(anoSiguienteDTO == null) {
				AnoEnCurso anoActualDTO = programasDAO.getAnoEnCursoById(getAnoCurso());
				anoSiguienteDTO = new AnoEnCurso();
				anoSiguienteDTO.setAno(getAnoCurso()+1);
				anoSiguienteDTO.setMontoPercapitalBasal(anoActualDTO.getMontoPercapitalBasal());
				anoSiguienteDTO.setAsignacionAdultoMayor(anoActualDTO.getAsignacionAdultoMayor());
				anoSiguienteDTO.setInflactor(anoActualDTO.getInflactor());
				anoSiguienteDTO.setInflactorMarcoPresupuestario(anoActualDTO.getInflactorMarcoPresupuestario());
				programasDAO.saveAnoCurso(anoSiguienteDTO);
			}
			programaAnoSiguiente.setAno(anoSiguienteDTO);
			programaAnoSiguiente.setEstado(new EstadoPrograma(1));
			programaAnoSiguiente.setEstadoFlujoCaja(new EstadoPrograma(1));
			programaAnoSiguiente.setPrograma(actual);
			programaAnoSiguiente.setEstadoConvenio(new EstadoPrograma(1));
			programaAnoSiguiente.setEstadoreliquidacion(new EstadoPrograma(1));
			programaAnoSiguiente.setEstadoOT(new EstadoPrograma(1));
			programasDAO.save(programaAnoSiguiente);
			id= programaAnoSiguiente.getIdProgramaAno();
		}
		return id;
	}


	
	public List<ProgramaMunicipalHistoricoVO> getHistoricoMunicipal(int programaSeleccionado, int idComponente, int idServicio) {
		
		int IdProgramaAno = getIdProgramaAnoAnterior(programaSeleccionado, reforzamientoService.getAnoCurso());
		int IdProgramaAnoSiguiente = getIdProgramaAnoAnterior(programaSeleccionado, reforzamientoService.getAnoCurso()+1);
		
		
		List<ProgramaMunicipalCoreComponente> actual = programasDAO.getHistoricoMunicipal(IdProgramaAno,idComponente, idServicio);
		List<ProgramaMunicipalCoreComponente> siguiente = programasDAO.getHistoricoMunicipal(IdProgramaAnoSiguiente,idComponente, idServicio);
		
		List<ProgramaMunicipalHistoricoVO> listaProgramaMunicipalVO = new ArrayList<ProgramaMunicipalHistoricoVO>();
		for (int i=0; i < actual.size();i++) {
			ProgramaMunicipalHistoricoVO prog = new ProgramaMunicipalHistoricoVO();
			prog.setIdComuna(actual.get(i).getProgramaMunicipalCore().getComuna().getId());
			prog.setNombreComuna(actual.get(i).getProgramaMunicipalCore().getComuna().getNombre());
			prog.setTotalAnoAnterior(actual.get(i).getTarifa());
			prog.setTotalAnoActual(siguiente.get(i).getTarifa());
			prog.setIdComponente(idComponente);
									
			prog.setIdProgramaMunicipalCore(siguiente.get(i).getProgramaMunicipalCore().getIdProgramaMunicipalCore());
			listaProgramaMunicipalVO.add(prog);
		}
		
		
		return listaProgramaMunicipalVO;
	}
	
	public List<ProgramaServicioHistoricoVO> getHistoricoServicio(int programaSeleccionado, int idComponente, int idServicio) {
		
		int IdProgramaAno = getIdProgramaAnoAnterior(programaSeleccionado, reforzamientoService.getAnoCurso());
		int IdProgramaAnoSiguiente = getIdProgramaAnoAnterior(programaSeleccionado, reforzamientoService.getAnoCurso()+1);
		
		
		List<ProgramaServicioCoreComponente> actual = programasDAO.getHistoricoServicio(IdProgramaAno,idComponente, idServicio);
		List<ProgramaServicioCoreComponente> siguiente = programasDAO.getHistoricoServicio(IdProgramaAnoSiguiente,idComponente, idServicio);
		
		List<ProgramaServicioHistoricoVO> listaProgramaServicioVO = new ArrayList<ProgramaServicioHistoricoVO>();
		for (int i=0; i < actual.size();i++) {
			ProgramaServicioHistoricoVO prog = new ProgramaServicioHistoricoVO();
			prog.setIdEstablecimiento(actual.get(i).getProgramaServicioCore1().getEstablecimiento().getId());
			prog.setNombreEstablecimiento(actual.get(i).getProgramaServicioCore1().getEstablecimiento().getNombre());
			
			prog.setTotalAnoAnterior(actual.get(i).getTarifa());
			prog.setTotalAnoActual(siguiente.get(i).getTarifa());
			prog.setIdComponente(idComponente);
									
			prog.setIdProgramaServicioCore(siguiente.get(i).getProgramaServicioCore1().getIdProgramaServicioCore());
			
			int posicion = listaProgramaServicioVO.indexOf(prog);
			if(posicion != -1){
				if(actual.get(i).getSubtitulo().getIdTipoSubtitulo()==1){
					SubtituloProgramasVO subVO = new SubtituloProgramasVO();
					subVO.setTotal(actual.get(i).getTarifa());
					subVO.setTotalFuturo(siguiente.get(i).getTarifa());
					subVO.setId(siguiente.get(i).getSubtitulo().getIdTipoSubtitulo());
					listaProgramaServicioVO.get(posicion).setSubtitulo21(subVO);
				}
				if(actual.get(i).getSubtitulo().getIdTipoSubtitulo()==2){
					SubtituloProgramasVO subVO = new SubtituloProgramasVO();
					subVO.setTotal(actual.get(i).getTarifa());
					subVO.setTotalFuturo(siguiente.get(i).getTarifa());
					subVO.setId(siguiente.get(i).getSubtitulo().getIdTipoSubtitulo());
					listaProgramaServicioVO.get(posicion).setSubtitulo22(subVO);
				}
				if(actual.get(i).getSubtitulo().getIdTipoSubtitulo()==4){
					SubtituloProgramasVO subVO = new SubtituloProgramasVO();
					subVO.setTotal(actual.get(i).getTarifa());
					subVO.setTotalFuturo(siguiente.get(i).getTarifa());
					subVO.setId(siguiente.get(i).getSubtitulo().getIdTipoSubtitulo());
					listaProgramaServicioVO.get(posicion).setSubtitulo29(subVO);
				}
			}else{
				if(actual.get(i).getSubtitulo().getIdTipoSubtitulo()==1){
					SubtituloProgramasVO subVO = new SubtituloProgramasVO();
					subVO.setTotal(actual.get(i).getTarifa());
					subVO.setTotalFuturo(siguiente.get(i).getTarifa());
					subVO.setId(siguiente.get(i).getSubtitulo().getIdTipoSubtitulo());
					prog.setSubtitulo21(subVO);
				}
				if(actual.get(i).getSubtitulo().getIdTipoSubtitulo()==2){
					SubtituloProgramasVO subVO = new SubtituloProgramasVO();
					subVO.setTotal(actual.get(i).getTarifa());
					subVO.setTotalFuturo(siguiente.get(i).getTarifa());
					subVO.setId(siguiente.get(i).getSubtitulo().getIdTipoSubtitulo());
					prog.setSubtitulo22(subVO);
				}
				if(actual.get(i).getSubtitulo().getIdTipoSubtitulo()==4){
					SubtituloProgramasVO subVO = new SubtituloProgramasVO();
					subVO.setTotal(actual.get(i).getTarifa());
					subVO.setTotalFuturo(siguiente.get(i).getTarifa());
					subVO.setId(siguiente.get(i).getSubtitulo().getIdTipoSubtitulo());
					prog.setSubtitulo29(subVO);
				}
				listaProgramaServicioVO.add(prog);
			}
		}
		return listaProgramaServicioVO;
	}


	public List<ProgramaVO> getProgramasBySubtitulo(Subtitulo subtitulo) {
		System.out.println("carga programas del subtitulo --> "+subtitulo.getNombre());
		Integer anoCurso = getAnoCurso();
		List<ProgramaVO> programas = new ArrayList<ProgramaVO>();
		List<ProgramaAno> programasAno = programasDAO.getProgramasBySubtitulo(anoCurso, subtitulo);
		if(programasAno != null && programasAno.size() > 0){
			for(ProgramaAno programaAno : programasAno){
				ProgramaVO programaVO = new ProgramaMapper().getBasic(programaAno);
				if(!programas.contains(programaVO)){
					programas.add(programaVO);
				}
			}
		}
		return programas;
	}
	
	
	public Integer evaluarAnoSiguiente(Integer programaSeleccionado, ProgramaVO programa) {
		ProgramaAno programaAnoActual = programasDAO.getProgramaAnoByID(programaSeleccionado);
		Integer anoSiguiente = getAnoCurso() + 1;
		ProgramaAno programaAnoSiguiente = programasDAO.getProgramaAnoSiguiente(programaAnoActual.getPrograma().getId(), anoSiguiente);

		if(programaAnoSiguiente == null){
			//crear 
			programaAnoSiguiente = new ProgramaAno();
			AnoEnCurso anoSiguienteDTO = programasDAO.getAnoEnCursoById(anoSiguiente);
			if(anoSiguienteDTO == null) {
				AnoEnCurso anoActualDTO = programasDAO.getAnoEnCursoById(getAnoCurso());
				anoSiguienteDTO = new AnoEnCurso();
				anoSiguienteDTO.setAno(anoSiguiente);
				anoSiguienteDTO.setMontoPercapitalBasal(anoActualDTO.getMontoPercapitalBasal());
				anoSiguienteDTO.setAsignacionAdultoMayor(anoActualDTO.getAsignacionAdultoMayor());
				anoSiguienteDTO.setInflactor(anoActualDTO.getInflactor());
				anoSiguienteDTO.setInflactorMarcoPresupuestario(anoActualDTO.getInflactorMarcoPresupuestario());
				programasDAO.saveAnoCurso(anoSiguienteDTO);
			}
			programaAnoSiguiente.setAno(anoSiguienteDTO);
			programaAnoSiguiente.setEstado(new EstadoPrograma(1));
			programaAnoSiguiente.setEstadoFlujoCaja(new EstadoPrograma(1));
			programaAnoSiguiente.setPrograma(programaAnoActual.getPrograma());
			programaAnoSiguiente.setEstadoConvenio(new EstadoPrograma(1));
			programaAnoSiguiente.setEstadoreliquidacion(new EstadoPrograma(1));
			programaAnoSiguiente.setEstadoOT(new EstadoPrograma(1));
			programasDAO.save(programaAnoSiguiente);
		} 
		
		
			
		if(programa.getDependenciaMunicipal()){
			List<ProgramaMunicipalCore> municipalCore =  programasDAO.getProgramaMunicipalCoreById(programaAnoSiguiente.getIdProgramaAno());
			if(municipalCore.size()==0){
				for(ProgramaMunicipalCore coreMun : programaAnoActual.getProgramasMunicipalesCore()){
					ProgramaMunicipalCore nuevoCore = new ProgramaMunicipalCore();
					nuevoCore.setComuna(coreMun.getComuna());
					nuevoCore.setProgramaAnoMunicipal(programaAnoSiguiente);
					programasDAO.save(nuevoCore);
				}			
			}
		}
		if(programa.getDependenciaServicio()){
			List<ProgramaServicioCore> servicioCore =  programasDAO.getProgramaServicioCoreById(programaAnoSiguiente.getIdProgramaAno());
			if(servicioCore.size()==0){
				for(ProgramaServicioCore coreServ : programaAnoActual.getProgramasServiciosCore()){
					ProgramaServicioCore nuevoCore = new ProgramaServicioCore();
					nuevoCore.setEstablecimiento(coreServ.getEstablecimiento());
					nuevoCore.setProgramaAnoServicio(programaAnoSiguiente);
					nuevoCore.setServicio(coreServ.getServicio());
					programasDAO.save(nuevoCore);
				}
			}
		}
		
		return programaAnoSiguiente.getIdProgramaAno();
		
	}

	public int getProgramaAnoSiguiente(Integer idPrograma,
			Integer anoSiguiente) {
		ProgramaAno prog = programasDAO.getProgramaAnoSiguiente(idPrograma, anoSiguiente);
		return prog.getIdProgramaAno();
		
	}
	
	public List<ProgramaAPSVO> getProgramaMunicipalesResumen(Integer idProgramaAno, List<Integer> idComponentes, Integer idSubtitulo) {
		List<ProgramaAPSVO> programaVO =  new ArrayList<ProgramaAPSVO>();
		List<ProgramaMunicipalCoreComponente> coreComponentesMunicipal = programasDAO.getProgramaMunicipales(idProgramaAno, idComponentes, idSubtitulo);
		for(ProgramaMunicipalCoreComponente detalle : coreComponentesMunicipal){
			ProgramaAPSVO prog = new ProgramaAPSVO();
			prog.setCantidad(detalle.getCantidad());
			prog.setComuna(detalle.getProgramaMunicipalCore().getComuna().getNombre());
			prog.setIdComuna(detalle.getProgramaMunicipalCore().getComuna().getId());
			prog.setIdServicioSalud(detalle.getProgramaMunicipalCore().getComuna().getServicioSalud().getId());
			prog.setServicioSalud(detalle.getProgramaMunicipalCore().getComuna().getServicioSalud().getNombre());
			prog.setTarifa(detalle.getMonto());
			prog.setTotal(detalle.getTarifa());
			prog.setIdPrograma(detalle.getProgramaMunicipalCore().getProgramaAnoMunicipal().getIdProgramaAno());
			prog.setIdComponente(detalle.getMunicipalCoreComponente().getId());
			programaVO.add(prog);
			
		}
		return programaVO;
	}
	
	public List<ProgramaAPSVO> getProgramaMunicipalesResumenDetalle(Integer idProgramaAno,
			List<Integer> idComponentes, Integer idServicio) {
		List<ProgramaAPSVO> programaVO =  new ArrayList<ProgramaAPSVO>();
		List<ProgramaMunicipalCoreComponente> coreComponentesMunicipal = programasDAO.getProgramaMunicipalesDetalle(idProgramaAno, idComponentes,idServicio);
		for(ProgramaMunicipalCoreComponente detalle : coreComponentesMunicipal){
			ProgramaAPSVO prog = new ProgramaAPSVO();
			prog.setCantidad(detalle.getCantidad());
			prog.setComuna(detalle.getProgramaMunicipalCore().getComuna().getNombre());
			prog.setIdComuna(detalle.getProgramaMunicipalCore().getComuna().getId());
			prog.setIdServicioSalud(detalle.getProgramaMunicipalCore().getComuna().getServicioSalud().getId());
			prog.setServicioSalud(detalle.getProgramaMunicipalCore().getComuna().getServicioSalud().getNombre());
			prog.setTarifa(detalle.getMonto());
			prog.setTotal(detalle.getTarifa());
			prog.setIdComponente(detalle.getMunicipalCoreComponente().getId());
			programaVO.add(prog);
			
		}
		return programaVO;
	}
	
	
	public List<ProgramaAPSServicioResumenVO> getProgramaServicios(Integer idProgramaAno) {
		List<ProgramaAPSServicioResumenVO> programaVO =  new ArrayList<ProgramaAPSServicioResumenVO>();
		List<ProgramaServicioCoreComponente> coreComponenteServicio = programasDAO.getProgramaServicios(idProgramaAno);
		
		for(ProgramaServicioCoreComponente detalle : coreComponenteServicio){
			ProgramaAPSServicioResumenVO prog = new ProgramaAPSServicioResumenVO();
			
			prog.setEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getNombre());
			prog.setIdEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getId());
			prog.setServicioSalud(detalle.getProgramaServicioCore1().getServicio().getNombre());
			prog.setIdServicioSalud(detalle.getProgramaServicioCore1().getServicio().getId());
			prog.setCodigoEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getCodigo());
			
			int posicion = programaVO.indexOf(prog);
			if(posicion != -1){
				prog = programaVO.get(posicion);
				
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==1){
					prog.setCantidadS21(detalle.getCantidad());
					prog.setTarifaS21(detalle.getMonto());
					prog.setTotalS21(detalle.getTarifa());
				}
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==2){
					prog.setCantidadS22(detalle.getCantidad());
					prog.setTarifaS22(detalle.getMonto());
					prog.setTotalS22(detalle.getTarifa());
				}
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==4){
					prog.setCantidadS29(detalle.getCantidad());
					prog.setTarifaS29(detalle.getMonto());
					prog.setTotalS29(detalle.getTarifa());
				}
			}else{
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==1){
					prog.setCantidadS21(detalle.getCantidad());
					prog.setTarifaS21(detalle.getMonto());
					prog.setTotalS21(detalle.getTarifa());
				}
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==2){
					prog.setCantidadS22(detalle.getCantidad());
					prog.setTarifaS22(detalle.getMonto());
					prog.setTotalS22(detalle.getTarifa());
				}
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==4){
					prog.setCantidadS29(detalle.getCantidad());
					prog.setTarifaS29(detalle.getMonto());
					prog.setTotalS29(detalle.getTarifa());
				}
				programaVO.add(prog);
			}
			
		}
		
		return programaVO;
	}
	
	
	public List<ProgramaAPSServicioResumenVO> getProgramaServiciosResumen(
			Integer idProxAno, Integer idServicio, List<Integer> idComponentesServicio) {
		List<ProgramaAPSServicioResumenVO> programaVO =  new ArrayList<ProgramaAPSServicioResumenVO>();
		List<ProgramaServicioCoreComponente> coreComponenteServicio = programasDAO.getProgramaServiciosResumen(idProxAno, idServicio,idComponentesServicio);
		
		
		for(ProgramaServicioCoreComponente detalle : coreComponenteServicio){
		
			ProgramaAPSServicioResumenVO prog = new ProgramaAPSServicioResumenVO();
			
			prog.setEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getNombre());
			prog.setIdEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getId());
			prog.setServicioSalud(detalle.getProgramaServicioCore1().getServicio().getNombre());
			prog.setIdServicioSalud(detalle.getProgramaServicioCore1().getServicio().getId());
			prog.setCodigoEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getCodigo());
			
			TipoSubtitulo tipoSub = tipoSubtituloDAO.getTipoSubtituloPorID(detalle.getSubtitulo().getIdTipoSubtitulo());
			
			//Si no existe
			if(programaVO.indexOf(prog) == -1){
				List<SubtituloVO> subtitulos = new ArrayList<SubtituloVO>();
				List<ComponentesVO> componentes = new ArrayList<ComponentesVO>();
				
				DependenciaVO dependenciaVO = new DependenciaVO();
				dependenciaVO.setId(tipoSub.getDependencia().getIdDependenciaPrograma());
				dependenciaVO.setNombre(tipoSub.getDependencia().getNombre());
				SubtituloVO subVO = new SubtituloVO();
				subVO.setDependencia(dependenciaVO);
				subVO.setId(detalle.getSubtitulo().getIdTipoSubtitulo());
				subVO.setNombre(tipoSub.getNombreSubtitulo());
				subVO.setTarifa(detalle.getMonto());
				subVO.setCantidad(detalle.getCantidad());
				subVO.setTotal(detalle.getTarifa());
				
				subtitulos.add(subVO);
			
				ComponentesVO compoVO = new ComponentesVO();
				compoVO.setId(detalle.getServicioCoreComponente().getId());
				compoVO.setNombre(detalle.getServicioCoreComponente().getNombre());
				compoVO.setSubtitulos(subtitulos);
				
				componentes.add(compoVO);
				
				prog.setComponentes(componentes);
				programaVO.add(prog);
				
			}else{
				DependenciaVO dependenciaVO = new DependenciaVO();
				dependenciaVO.setId(tipoSub.getDependencia().getIdDependenciaPrograma());
				dependenciaVO.setNombre(tipoSub.getDependencia().getNombre());
				
				SubtituloVO subVO = new SubtituloVO();
				subVO.setDependencia(dependenciaVO);
				subVO.setId(detalle.getSubtitulo().getIdTipoSubtitulo());
				subVO.setNombre(tipoSub.getNombreSubtitulo());
				subVO.setTarifa(detalle.getMonto());
				subVO.setCantidad(detalle.getCantidad());
				subVO.setTotal(detalle.getTarifa());
				
				ComponentesVO compoVO = new ComponentesVO();
				compoVO.setId(detalle.getServicioCoreComponente().getId());
				compoVO.setNombre(detalle.getServicioCoreComponente().getNombre());
				
				List<ComponentesVO> componentes = programaVO.get(programaVO.indexOf(prog)).getComponentes();
				
				//Si el componente no existe en el programa
				if(componentes.indexOf(compoVO) == -1){
					List<SubtituloVO> subs = new ArrayList<SubtituloVO>();
					subs.add(subVO);
					compoVO.setSubtitulos(subs);
					
					componentes.add(compoVO);
					programaVO.get(programaVO.indexOf(prog)).setComponentes(componentes);
				}else{
					List<SubtituloVO> subs = componentes.get(componentes.indexOf(compoVO)).getSubtitulos();
					subs.add(subVO);
					
					componentes.get(componentes.indexOf(compoVO)).setSubtitulos(subs);
					programaVO.get(programaVO.indexOf(prog)).setComponentes(componentes);
					
				}
				
			}
			
		}
		return programaVO;
	}
	
	public List<ProgramaAPSServicioResumenVO> getProgramaServicios(Integer idProgramaAno, Integer idServicio) {
		List<ProgramaAPSServicioResumenVO> programaVO =  new ArrayList<ProgramaAPSServicioResumenVO>();
		List<ProgramaServicioCoreComponente> coreComponenteServicio = programasDAO.getProgramaServicios(idProgramaAno, idServicio);
		
		for(ProgramaServicioCoreComponente detalle : coreComponenteServicio){
			ProgramaAPSServicioResumenVO prog = new ProgramaAPSServicioResumenVO();
			
			prog.setEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getNombre());
			prog.setIdEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getId());
			prog.setServicioSalud(detalle.getProgramaServicioCore1().getServicio().getNombre());
			prog.setIdServicioSalud(detalle.getProgramaServicioCore1().getServicio().getId());
			prog.setCodigoEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getCodigo());
			
			int posicion = programaVO.indexOf(prog);
			if(posicion != -1){
				prog = programaVO.get(posicion);
				
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==1){
					prog.setCantidadS21(detalle.getCantidad());
					prog.setTarifaS21(detalle.getMonto());
					prog.setTotalS21(detalle.getTarifa());
				}
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==2){
					prog.setCantidadS22(detalle.getCantidad());
					prog.setTarifaS22(detalle.getMonto());
					prog.setTotalS22(detalle.getTarifa());
				}
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==4){
					prog.setCantidadS29(detalle.getCantidad());
					prog.setTarifaS29(detalle.getMonto());
					prog.setTotalS29(detalle.getTarifa());
				}
			}else{
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==1){
					prog.setCantidadS21(detalle.getCantidad());
					prog.setTarifaS21(detalle.getMonto());
					prog.setTotalS21(detalle.getTarifa());
				}
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==2){
					prog.setCantidadS22(detalle.getCantidad());
					prog.setTarifaS22(detalle.getMonto());
					prog.setTotalS22(detalle.getTarifa());
				}
				if(detalle.getSubtitulo().getIdTipoSubtitulo()==4){
					prog.setCantidadS29(detalle.getCantidad());
					prog.setTarifaS29(detalle.getMonto());
					prog.setTotalS29(detalle.getTarifa());
				}
				programaVO.add(prog);
			}
			
		}
		
		return programaVO;
	}

	public List<ComponentesVO> getComponenteByProgramaSubtitulos(Integer programa, Subtitulo... subtitulos) {
		List<ComponentesVO> componentesVO = new ArrayList<ComponentesVO>();
		List<Componente> componentes = componenteDAO.getComponentesByIdProgramaIdSubtitulos(programa, subtitulos);
		if(componentes != null && componentes.size() > 0){
			for(Componente componente : componentes){
				ComponentesVO componenteVO = new ComponenteMapper().getBasic(componente);
				if(!componentesVO.contains(componenteVO)){
					componentesVO.add(componenteVO);
				}
			}
		}
		return componentesVO;
	}

	public List<ProgramaAPSServicioResumenVO> getProgramaServiciosResumen(
			Integer idProxAno, List<Integer> idComponentesServicio) {
		List<ProgramaAPSServicioResumenVO> programaVO =  new ArrayList<ProgramaAPSServicioResumenVO>();
		List<ProgramaServicioCoreComponente> coreComponenteServicio = programasDAO.getProgramaServiciosResumen(idProxAno,idComponentesServicio);
		
		
		for(ProgramaServicioCoreComponente detalle : coreComponenteServicio){
		
			ProgramaAPSServicioResumenVO prog = new ProgramaAPSServicioResumenVO();
			
			prog.setEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getNombre());
			prog.setIdEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getId());
			prog.setServicioSalud(detalle.getProgramaServicioCore1().getServicio().getNombre());
			prog.setIdServicioSalud(detalle.getProgramaServicioCore1().getServicio().getId());
			prog.setCodigoEstablecimiento(detalle.getProgramaServicioCore1().getEstablecimiento().getCodigo());
			
			TipoSubtitulo tipoSub = tipoSubtituloDAO.getTipoSubtituloPorID(detalle.getSubtitulo().getIdTipoSubtitulo());
			
			//Si no existe
			if(programaVO.indexOf(prog) == -1){
				List<SubtituloVO> subtitulos = new ArrayList<SubtituloVO>();
				List<ComponentesVO> componentes = new ArrayList<ComponentesVO>();
				
				DependenciaVO dependenciaVO = new DependenciaVO();
				dependenciaVO.setId(tipoSub.getDependencia().getIdDependenciaPrograma());
				dependenciaVO.setNombre(tipoSub.getDependencia().getNombre());
				SubtituloVO subVO = new SubtituloVO();
				subVO.setDependencia(dependenciaVO);
				subVO.setId(detalle.getSubtitulo().getIdTipoSubtitulo());
				subVO.setNombre(tipoSub.getNombreSubtitulo());
				subVO.setTarifa(detalle.getMonto());
				subVO.setCantidad(detalle.getCantidad());
				subVO.setTotal(detalle.getTarifa());
				
				subtitulos.add(subVO);
			
				ComponentesVO compoVO = new ComponentesVO();
				compoVO.setId(detalle.getServicioCoreComponente().getId());
				compoVO.setNombre(detalle.getServicioCoreComponente().getNombre());
				compoVO.setSubtitulos(subtitulos);
				
				componentes.add(compoVO);
				
				prog.setComponentes(componentes);
				programaVO.add(prog);
				
			}else{
				DependenciaVO dependenciaVO = new DependenciaVO();
				dependenciaVO.setId(tipoSub.getDependencia().getIdDependenciaPrograma());
				dependenciaVO.setNombre(tipoSub.getDependencia().getNombre());
				
				SubtituloVO subVO = new SubtituloVO();
				subVO.setDependencia(dependenciaVO);
				subVO.setId(detalle.getSubtitulo().getIdTipoSubtitulo());
				subVO.setNombre(tipoSub.getNombreSubtitulo());
				subVO.setTarifa(detalle.getMonto());
				subVO.setCantidad(detalle.getCantidad());
				subVO.setTotal(detalle.getTarifa());
				
				ComponentesVO compoVO = new ComponentesVO();
				compoVO.setId(detalle.getServicioCoreComponente().getId());
				compoVO.setNombre(detalle.getServicioCoreComponente().getNombre());
				
				List<ComponentesVO> componentes = programaVO.get(programaVO.indexOf(prog)).getComponentes();
				
				//Si el componente no existe en el programa
				if(componentes.indexOf(compoVO) == -1){
					List<SubtituloVO> subs = new ArrayList<SubtituloVO>();
					subs.add(subVO);
					compoVO.setSubtitulos(subs);
					
					componentes.add(compoVO);
					programaVO.get(programaVO.indexOf(prog)).setComponentes(componentes);
				}else{
					List<SubtituloVO> subs = componentes.get(componentes.indexOf(compoVO)).getSubtitulos();
					subs.add(subVO);
					
					componentes.get(componentes.indexOf(compoVO)).setSubtitulos(subs);
					programaVO.get(programaVO.indexOf(prog)).setComponentes(componentes);
					
				}
				
			}
			
		}
		return programaVO;
	}

	public List<ProgramaFonasaVO> getProgramasFonasa(boolean revisaFonasa) {
		List<ProgramaFonasaVO> resultado = new ArrayList<ProgramaFonasaVO>();
		List<Programa> programas = 	programasDAO.getProgramasFonasa(revisaFonasa);
		
		for(Programa programa : programas){
			ProgramaFonasaVO fonasa = new ProgramaFonasaVO();
			fonasa.setIdPrograma(programa.getId());
			fonasa.setNombrePrograma(programa.getNombre());
			resultado.add(fonasa);
		}
		return resultado;
		
		
	}

	
	
}
