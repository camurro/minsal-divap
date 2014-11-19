
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
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.ProgramaMunicipalHistoricoVO;
import minsal.divap.vo.ProgramaMunicipalVO;
import minsal.divap.vo.ProgramaServicioVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaVO;
import minsal.divap.vo.ResumenProgramaServiciosVO;
import minsal.divap.vo.ResumenProgramaVO;
import minsal.divap.vo.ServiciosSummaryVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloProgramasVO;
import minsal.divap.vo.SubtituloVO;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;
import cl.minsal.divap.model.ServicioSalud;

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
	
	private Integer getAnoCurso() {
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
	
	public List<ProgramaMunicipalVO> findByServicioComponente(Integer idComponente, Integer idServicio){
		List<ProgramaMunicipalCoreComponente> result = programasDAO.findByServicioComponente(idComponente, idServicio);
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
				Integer totalS24 = ((Number)(resumen[2])).intValue();
				r.setIdServicio(idServicio);
				r.setNombreServicio(nombreServicio);
				r.setTotalS24(totalS24);
				salida.add(r);
			}
		}
		return salida;
	}
	
	public List<ResumenProgramaServiciosVO> getResumenServicio(Integer idProgramaAno){
		List<ResumenProgramaServiciosVO> salida =  new ArrayList<ResumenProgramaServiciosVO>();
		List<ComponentesVO> componentes = getComponenteByPrograma(idProgramaAno);
		
		for(ComponentesVO compo : componentes){
			
			for(SubtituloVO sub : compo.getSubtitulos()){
				
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
								Integer totalS21 = ((Number)(resumen[2])).intValue();
								r.setTotalS21(totalS21);
							}
							if(sub.getId()==2){
								Integer totalS22 = ((Number)(resumen[2])).intValue();
								r.setTotalS22(totalS22);
							}
							if(sub.getId()==4){
								Integer totalS29 = ((Number)(resumen[2])).intValue();
								r.setTotalS29(totalS29);
							}
							
						}else{
							if(sub.getId()==1){
								Integer totalS21 = ((Number)(resumen[2])).intValue();
								r.setTotalS21(totalS21);
							}
							if(sub.getId()==2){
								Integer totalS22 = ((Number)(resumen[2])).intValue();
								r.setTotalS22(totalS22);
							}
							if(sub.getId()==4){
								Integer totalS29 = ((Number)(resumen[2])).intValue();
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
		return programasDAO.getIdProgramaAnoAnterior(programaSeleccionado,anoAnterior);
	}

	public List<ProgramaMunicipalHistoricoVO> getHistoricoMunicipal(
			int idProgramaAnoAnterior, Integer idComponente, Integer idServicio) {
		
		List<ProgramaMunicipalCoreComponente> result = programasDAO.getHistoricoMunicipal(idProgramaAnoAnterior,idComponente, idServicio);
		List<ProgramaMunicipalHistoricoVO> listaProgramaMunicipalVO = new ArrayList<ProgramaMunicipalHistoricoVO>();
		for (ProgramaMunicipalCoreComponente programaMunicipalCore : result) {
			ProgramaMunicipalHistoricoVO prog = new ProgramaMunicipalHistoricoVO();
			prog.setIdComuna(programaMunicipalCore.getProgramaMunicipalCore().getComuna().getId());
			prog.setNombreComuna(programaMunicipalCore.getProgramaMunicipalCore().getComuna().getNombre());
			prog.setTotalAnoAnterior(programaMunicipalCore.getTarifa());
			prog.setIdComponente(idComponente);
			
			
						
			prog.setIdProgramaMunicipalCore(programaMunicipalCore.getProgramaMunicipalCore().getIdProgramaMunicipalCore());
			listaProgramaMunicipalVO.add(prog);
		}
		
		
		return listaProgramaMunicipalVO;
	}


	public List<ProgramaVO> getProgramasBySubtitulo(Integer anoCurso, Subtitulo subtitulo) {
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
	
	
}
