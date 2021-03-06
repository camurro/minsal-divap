package minsal.divap.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import minsal.divap.enums.EstadosConvenios;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import cl.minsal.divap.model.Convenio;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;
import cl.minsal.divap.model.ConvenioSeguimiento;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.ConvenioServicioComponente;
import cl.minsal.divap.model.DocumentoConvenio;
import cl.minsal.divap.model.DocumentoConvenioComuna;
import cl.minsal.divap.model.DocumentoConvenioServicio;
import cl.minsal.divap.model.DocumentoModificacionPercapita;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;
import cl.minsal.divap.model.ReporteEmailsConvenio;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;
import cl.minsal.divap.model.ReporteEmailsEnviados;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.Usuario;



@Singleton
public class ConveniosDAO {
	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public ConvenioComuna getConvenioComunaById(Integer id){
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdConvenioComuna", ConvenioComuna.class);
			query.setParameter("idConvenioComuna", id);
			List<ConvenioComuna> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ConvenioComuna getConvenioComunaByNumeroResolucion(Integer numeroResolucion){
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByNumeroResolucion", ConvenioComuna.class);
			query.setParameter("numeroResolucion", numeroResolucion);
			List<ConvenioComuna> results = query.getResultList();
			if (results.size() > 0)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ConvenioServicio getConvenioServicioById(Integer id){
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdConvenioServicio", ConvenioServicio.class);
			query.setParameter("idConvenioServicio", id);
			List<ConvenioServicio> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ConvenioServicio getConvenioServicioByNumeroResolucion(Integer numeroResolucion){
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByNumeroResolucion", ConvenioServicio.class);
			query.setParameter("numeroResolucion", numeroResolucion);
			List<ConvenioServicio> results = query.getResultList();
			if (results.size() > 0)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ConvenioServicioComponente getConvenioServicioComponenteById(Integer idConvenioServicioComponente){
		try {
			TypedQuery<ConvenioServicioComponente> query = this.em.createNamedQuery("ConvenioServicioComponente.findByIdConvenioServicioComponente", ConvenioServicioComponente.class);
			query.setParameter("idConvenioServicioComponente", idConvenioServicioComponente);
			List<ConvenioServicioComponente> results = query.getResultList();
			if (results != null && results.size() > 0)
				return results.get(0);
			return null;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ConvenioServicio findByIdConvenio(Integer idConvenio){
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdConvenio", ConvenioServicio.class);
			query.setParameter("idConvenio", idConvenio);
			List<ConvenioServicio> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public ConvenioComuna save(ConvenioComuna convenio) {
		this.em.persist(convenio);
		return convenio;
	}

	public ConvenioServicio save(ConvenioServicio convenio) {
		this.em.persist(convenio);
		return convenio;
	}

	public ReferenciaDocumento getNodeByIdConvenio(Integer idConverio) {
		try {
			TypedQuery<ReferenciaDocumento> query = this.em.createNamedQuery("DocumentoConvenio.findDocumentoByConvenioID", ReferenciaDocumento.class);
			query.setParameter("idConvenio", idConverio);
			List<ReferenciaDocumento> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComuna> getByIDSubComponenteComunaProgramaAnoEstadoConvenio(int sub,String componenteSeleccionado, String comunaSeleccionada,String establecimientoSeleccionado, Integer ano, Integer programa, Integer idEstadoConvenio) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIDSubComponenteComunaProgramaAnoEstadoConvenio", ConvenioComuna.class);
			query.setParameter("idPrograma", programa);
			query.setParameter("idComponente",Integer.parseInt(componenteSeleccionado));
			query.setParameter("idComuna", Integer.parseInt(comunaSeleccionada));
			query.setParameter("idEstablecimiento", Integer.parseInt(establecimientoSeleccionado));
			query.setParameter("ano", ano);
			query.setParameter("sub", sub);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComuna> getConveniosSummaryByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, Integer idServicio, List<Integer> idComponentes, Subtitulo subtitulo) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdServicioIdComponenteIdSubtitulo", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idTipoSubtitulo", subtitulo.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComuna> getByProgramaAnoComponentesComunas(Integer idProgramaAno, List<Integer> idComponentes, List<Integer> idComunas) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdComponentesIdComunas", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idComunas", idComunas);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioServicio> getByProgramaAnoComponentesEstablecimientos(Integer idProgramaAno, List<Integer> idComponentes, List<Integer> idEstablecimientos) {
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdProgramaAnoIdComponentesIdEstablecimientos", ConvenioServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idEstablecimientos", idEstablecimientos);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComuna> getByProgramaAnoComunas(Integer idProgramaAno, List<Integer> idComunas) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdComunas", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComunas", idComunas);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComuna> getByProgramaAnoComponentes(Integer idProgramaAno, List<Integer> idComponentes) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdComponentes", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponentes", idComponentes);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComuna> getConveniosByProgramaAnoComponenteComuna(Integer idProgramaAno, Integer idComponente, Integer idComuna) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdComponenteIdComuna", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idComuna", idComuna);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public List<ConvenioServicio> getByProgramaAnoEstablecimientos(Integer idProgramaAno, List<Integer> idEstablecimientos) {
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdProgramaAnoIdEstablecimientos", ConvenioServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimientos", idEstablecimientos);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioServicio> getConvenioServicioByProgramaAnoComponentes(Integer idProgramaAno, List<Integer> idComponentes) {
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdProgramaAnoIdComponentes", ConvenioServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponentes", idComponentes);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setDocumentoConvenioById(String nodoref, String filename, int id) {

	}

	public List<ConvenioComuna> getConveniosComunaByProgramaAnoServicioEstadoConvenio(Integer idProgramaAno, Integer idServicio, Integer idEstadoConvenio) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdServicioIdEstadoConvenio", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioServicio> getConveniosServicioByProgramaAnoServicioEstadoConvenio(Integer idProgramaAno, Integer idServicio, Integer idEstadoConvenio) {
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdProgramaAnoIdServicioIdEstadoConvenio", ConvenioServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComuna> getConvenioComunaByProgramaAnoServicioComponente(Integer idProgramaAno, Integer idServicio, Integer idComponente) {
		List<Integer> idComponentes = new ArrayList<Integer>();
		idComponentes.add(idComponente);
		return getConvenioComunaByProgramaAnoServicioComponentes(idProgramaAno, idServicio, idComponentes);
	}

	public List<ConvenioComuna> getConvenioComunaByProgramaAnoServicioComponentes(Integer idProgramaAno, Integer idServicio, List<Integer> idComponentes) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdServicioIdComponentes", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponentes", idComponentes);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioServicio> getConvenioServicioByProgramaAnoServicioComponente(Integer idProgramaAno, Integer idServicio, Integer idComponente) {
		List<Integer> idComponentes = new ArrayList<Integer>();
		idComponentes.add(idComponente);
		return getConvenioServicioByProgramaAnoServicioComponentes(idProgramaAno, idServicio, idComponentes);
	}

	private List<ConvenioServicio> getConvenioServicioByProgramaAnoServicioComponentes(Integer idProgramaAno, Integer idServicio, List<Integer> idComponentes) {
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdProgramaAnoIdServicioIdComponentes", ConvenioServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponentes", idComponentes);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ConvenioComuna getConvenioComunaByIdComunaIdProgramaAno(Integer idComuna, Integer idProgramaAno) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdComunaIdProgramaAno", ConvenioComuna.class);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idProgramaAno", idProgramaAno);
			List<ConvenioComuna> result = query.getResultList();
			if(result != null && result.size() > 0 ){
				return result.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer crearInstanciaConvenio(Usuario usuario, Mes mesEnCurso) {
		try {
			long current = Calendar.getInstance().getTimeInMillis();
			Convenio dto = new Convenio();
			dto.setUsuario(usuario);
			dto.setMes(mesEnCurso);
			dto.setFechaCreacion(new Date(current));
			this.em.persist(dto);
			return dto.getIdConvenio();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Convenio getConvenioById(Integer idConvenio) {
		try {
			TypedQuery<Convenio> query = this.em.createNamedQuery("Convenio.findByIdConvenio", Convenio.class);
			query.setParameter("idConvenio", idConvenio);
			List<Convenio> results = query.getResultList(); 
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComunaComponente> getConveniosComunaComponenteByProgramaAnoComponenteSubtituloServicioEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idServicio, Integer idEstadoConvenio) {
		try {
			System.out.println("idProgramaAno="+idProgramaAno);
			System.out.println("idComponente="+idComponente);
			System.out.println("idSubtitulo="+idSubtitulo);
			System.out.println("idServicio="+idServicio);
			System.out.println("idEstadoConvenio="+idEstadoConvenio);
			TypedQuery<ConvenioComunaComponente> query = this.em.createNamedQuery("ConvenioComunaComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdServicioIdEstadoConvenio", ConvenioComunaComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Convenio findById(Integer idConvenio) {
		try {
			TypedQuery<Convenio> query = this.em.createNamedQuery("Convenio.findByIdConvenio", Convenio.class);
			query.setParameter("idConvenio", idConvenio);
			List<Convenio> results =  query.getResultList(); 
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ConvenioComunaComponente getByIdConvenioComunaIdSubtituloIdComponente(Integer idConvenioComuna, Integer idSubtitulo, Integer idComponente){
		try {
			TypedQuery<ConvenioComunaComponente> query = this.em.createNamedQuery("ConvenioComunaComponente.findByIdConvenioComunaIdSubtituloIdComponente", ConvenioComunaComponente.class);
			query.setParameter("idConvenioComuna", idConvenioComuna);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("idComponente", idComponente);
			List<ConvenioComunaComponente> result = query.getResultList();
			if(result != null && result.size() >0 ){
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ConvenioComuna findConvenioComunaById(Integer idConvenioComuna) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdConvenioComuna", ConvenioComuna.class);
			query.setParameter("idConvenioComuna", idConvenioComuna);
			List<ConvenioComuna> results =  query.getResultList(); 
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ConvenioServicio findConvenioServicioById(Integer idConvenioServicio) {
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdConvenioServicio", ConvenioServicio.class);
			query.setParameter("findByIdConvenioServicio", idConvenioServicio);
			List<ConvenioServicio> results =  query.getResultList(); 
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int getCountConveniosServicioPendientes(Integer idProgramaAno, Integer idEstadoConvenio) {
		try {
			TypedQuery<Number> query = this.em.createNamedQuery("ConvenioServicio.countConvenioServicioByIdProgramaAnoIdEstadoConvenio", Number.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return ((Number)query.getSingleResult()).intValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getCountConveniosComunaPendientes(Integer idProgramaAno, Integer idEstadoConvenio) {
		try {
			TypedQuery<Number> query = this.em.createNamedQuery("ConvenioComuna.countConvenioComunaByIdProgramaAnoIdEstadoConvenio", Number.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return ((Number)query.getSingleResult()).intValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ConvenioServicioComponente getConveniosServicioComponenteById(Integer idConvenioServicioComponente) {
		try {
			TypedQuery<ConvenioServicioComponente> query = this.em.createNamedQuery("ConvenioServicioComponente.findByIdConvenioServicioComponente", ConvenioServicioComponente.class);
			query.setParameter("idConvenioServicioComponente", idConvenioServicioComponente);
			List<ConvenioServicioComponente> results =  query.getResultList(); 
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	// WHERE    and c.convenioServicio.idEstablecimiento.id = :idEstablecimiento
	public ConvenioServicioComponente getConvenioServicioComponenteByIdSubtituloIdComponente(Integer idProgramaAno, Integer idComponente, Integer idTipoSubtitulo, Integer idEstablecimiento){
		try {
			TypedQuery<ConvenioServicioComponente> query = this.em.createNamedQuery("ConvenioServicioComponente.findByIdProgramaAnoIdComponenteSubtituloEstablecimiento", ConvenioServicioComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			
			List<ConvenioServicioComponente> result = query.getResultList();
			if(result != null && result.size() >0 ){
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ConvenioComunaComponente getConveniosComunaComponenteById(Integer idConvenioComunaComponente) {
		try {
			TypedQuery<ConvenioComunaComponente> query = this.em.createNamedQuery("ConvenioComunaComponente.findByIdConvenioComunaComponente", ConvenioComunaComponente.class);
			query.setParameter("idConvenioComunaComponente", idConvenioComunaComponente);
			List<ConvenioComunaComponente> results =  query.getResultList(); 
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioServicio> getConveniosServicioPendientes(Integer idProgramaAno, Integer idEstadoConvenio) {
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findConvenioServicioByIdProgramaAnoIdEstadoConvenio", ConvenioServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<ConvenioComuna> getConveniosComunaPendientes(Integer idProgramaAno, Integer idEstadoConvenio) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findConvenioComunaByIdProgramaAnoIdEstadoConvenio", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<ConvenioServicioComponente> getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idServicio, Integer idEstadoConvenio) {
		try {
			TypedQuery<ConvenioServicioComponente> query = this.em.createNamedQuery("ConvenioServicioComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdServicioIdEstadoConvenio", ConvenioServicioComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioServicioComponente> getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idEstablecimiento, Integer idEstadoConvenio) {
		try {
			TypedQuery<ConvenioServicioComponente> query = this.em.createNamedQuery("ConvenioServicioComponente.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio", ConvenioServicioComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public ConvenioServicio getConvenio(Integer idProgramaAno, int idEstablecimiento,
			Integer idConvenio) {
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findConvenioByProgramaAnoEstablecimientoConvenio", ConvenioServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idConvenio", idConvenio);
			List<ConvenioServicio> result = query.getResultList();
			if(result.size()>0){
				return result.get(0);
			}else
				return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComunaComponente> getConveniosComunaComponenteByProgramaAnoComponenteServicioEstadoConvenio(
			Integer idProgramaAno, Integer idComponente,
			Integer idServicio, Integer idEstadoConvenio) {
		
		try {
			TypedQuery<ConvenioComunaComponente> query = this.em.createNamedQuery("ConvenioComunaComponente.findByIdProgramaAnoIdComponenteIdServicioIdEstadoConvenio", ConvenioComunaComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComunaComponente> getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
			Integer idProgramaAno, Integer componenteSeleccionado, Integer idTipoSubtitulo,
			Integer idComuna, Integer idEstadoConvenio) {
		
		try {
			TypedQuery<ConvenioComunaComponente> query = this.em.createNamedQuery("ConvenioComunaComponente.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio", ConvenioComunaComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", componenteSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	public ConvenioComuna getConvenioComunaByIdConvenio(Integer idConvenio) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.getConvenioComunaByIdConvenio", ConvenioComuna.class);
			query.setParameter("idConvenio", idConvenio);
			List<ConvenioComuna> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer createSeguimiento(Integer idConvenio, Seguimiento seguimiento) {
		Convenio convenio = findById(idConvenio);
		ConvenioSeguimiento convenioSeguimiento = new ConvenioSeguimiento();
		convenioSeguimiento.setConvenio(convenio);
		convenioSeguimiento.setSeguimiento(seguimiento);
		this.em.persist(convenioSeguimiento);
		return convenioSeguimiento.getIdConvenioSeguimiento();
	}

	public DocumentoConvenio save(DocumentoConvenio documentoConvenio) {
		em.persist(documentoConvenio);
		return documentoConvenio;
	}

	public List<DocumentoConvenio> getByIdConvenioTipo(Integer idConvenio, TipoDocumentosProcesos tipoDocumentoProceso) {
		try{
			TypedQuery<DocumentoConvenio> query = this.em.createNamedQuery("DocumentoConvenio.findByIdConvenioTipo", DocumentoConvenio.class);
			query.setParameter("idConvenio", idConvenio);
			query.setParameter("idTipoDocumento", tipoDocumentoProceso.getId());
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<DocumentoConvenio> getByIdConvenioTipoNotFinal(Integer idConvenio, TipoDocumentosProcesos tipoDocumentoProceso) {
		try{
			TypedQuery<DocumentoConvenio> query = this.em.createNamedQuery("DocumentoConvenio.findByIdConvenioTipoNotFinal", DocumentoConvenio.class);
			query.setParameter("idConvenio", idConvenio);
			query.setParameter("idTipoDocumento", tipoDocumentoProceso.getId());
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer deleteDocumentoConvenio(Integer idDocumentoConvenio) {
		List<Integer> idDocumentosConvenio = new ArrayList<Integer>();
		idDocumentosConvenio.add(idDocumentoConvenio);
		return deleteDocumentoConvenio(idDocumentosConvenio);
	}
	
	public Integer deleteDocumentoConvenio(List<Integer> idDocumentosConvenio) {
		Query query = this.em.createNamedQuery("DocumentoConvenio.deleteUsingIds");
		query.setParameter("idDocumentosConvenio", idDocumentosConvenio);
		return query.executeUpdate();
	}

	public Integer deleteDocumento(Integer idDocumento) {
		Query query = this.em.createNamedQuery("ReferenciaDocumento.deleteUsingId");
		query.setParameter("idDocumento", idDocumento);
		return query.executeUpdate();
	}

	public ReporteEmailsEnviados save(ReporteEmailsEnviados reporteEmailsEnviados) {
		em.persist(reporteEmailsEnviados);
		return reporteEmailsEnviados;
	}

	public ReporteEmailsAdjuntos save(ReporteEmailsAdjuntos reporteEmailsAdjuntos) {
		em.persist(reporteEmailsAdjuntos);
		return reporteEmailsAdjuntos;
	}

	public ReporteEmailsConvenio save(ReporteEmailsConvenio reporteEmailsConvenio) {
		em.persist(reporteEmailsConvenio);
		return reporteEmailsConvenio;
	}

	public ReporteEmailsDestinatarios save(ReporteEmailsDestinatarios destinatarioConCopiaEncargadoFinanzas) {
		em.persist(destinatarioConCopiaEncargadoFinanzas);
		return destinatarioConCopiaEncargadoFinanzas;
	}

	public List<ReporteEmailsConvenio> getReporteCorreosByConvenio(Integer idConvenio) {
		try{
			TypedQuery<ReporteEmailsConvenio> query = this.em.createNamedQuery("ReporteEmailsConvenio.findByIdConvenio", ReporteEmailsConvenio.class);
			query.setParameter("idConvenio", idConvenio);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ConvenioComunaComponente> getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idComuna,
			Integer idEstadoConvenio) {
		try {
			TypedQuery<ConvenioComunaComponente> query = this.em.createNamedQuery("ConvenioComunaComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdComunaIdIdEstadoConvenio", ConvenioComunaComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioServicioComponente> getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idEstablecimiento,
			Integer idEstadoConvenio) {
		try {
			TypedQuery<ConvenioServicioComponente> query = this.em.createNamedQuery("ConvenioServicioComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdEstablecimientoIdEstadoConvenio", ConvenioServicioComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ConvenioServicioComponente save(ConvenioServicioComponente convenioServicioComponente) {
		em.persist(convenioServicioComponente);
		return convenioServicioComponente;
	}

	public ConvenioComunaComponente save(ConvenioComunaComponente convenioComunaComponente) {
		em.persist(convenioComunaComponente);
		return convenioComunaComponente;
	}
	
	public DocumentoConvenioServicio save(DocumentoConvenioServicio documentoConvenioServicio) {
		em.persist(documentoConvenioServicio);
		return documentoConvenioServicio;
	}

	public DocumentoConvenioComuna save(DocumentoConvenioComuna documentoConvenioComuna) {
		em.persist(documentoConvenioComuna);
		return documentoConvenioComuna;
	}

	public DocumentoModificacionPercapita save(DocumentoModificacionPercapita documentoModificacionPercapita) {
		em.persist(documentoModificacionPercapita);
		return documentoModificacionPercapita;
	}
	
	public List<ConvenioServicio> getConveniosServicioByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, 
			Integer idEstablecimiento, Integer idEstadoConvenio) {
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdProgramaAnoIdComponenteIdSubtituloIdEstablecimientoIdEstadoConvenio", ConvenioServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<ConvenioComuna> getConveniosComunaByProgramaAnoComponenteSubtituloComunaEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo,
			Integer idComuna, Integer idEstadoConvenio) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdComponenteIdSubtituloIdComunaIdEstadoConvenio", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idEstadoConvenio", idEstadoConvenio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<ConvenioServicio> getConveniosServicioByProgramaAnoComponenteSubtituloServicioEstadosConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, 
			Integer idServicio, Integer ... idEstadosConvenio) {
		try {
			List<Integer> estadosConvenios = new ArrayList<Integer>();
			for(Integer estado : idEstadosConvenio){
				estadosConvenios.add(estado);
			}
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdProgramaAnoIdComponenteIdSubtituloIdServicioIdEstadosConvenio", ConvenioServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idEstadosConvenio", estadosConvenios);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComuna> getConveniosComunaByProgramaAnoComponenteSubtituloServicioEstadosConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo,
			Integer idServicio, Integer ... idEstadosConvenio) {
		try {
			List<Integer> estadosConvenios = new ArrayList<Integer>();
			for(Integer estado : idEstadosConvenio){
				estadosConvenios.add(estado);
			}
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdComponenteIdSubtituloIdServicioIdEstadosConvenio", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idEstadosConvenio", estadosConvenios);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioServicioComponente> getConvenioServicioComponenteByIdConvenioIdProgramaAnoEstado(Integer idConvenio, Integer idProgramaAno, EstadosConvenios estadoConvenio) {
		try {
			TypedQuery<ConvenioServicioComponente> query = this.em.createNamedQuery("ConvenioServicioComponente.findByIdConvenioIdProgramaAnoIdEstadoConvenio", ConvenioServicioComponente.class);
			query.setParameter("idConvenio", idConvenio);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstadoConvenio", estadoConvenio.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<ConvenioComunaComponente> getConvenioComunaComponenteByIdConvenioIdProgramaAnoEstado(Integer idConvenio, Integer idProgramaAno, EstadosConvenios estadoConvenio) {
		try {
			TypedQuery<ConvenioComunaComponente> query = this.em.createNamedQuery("ConvenioComunaComponente.findByIdConvenioIdProgramaAnoIdEstadoConvenio", ConvenioComunaComponente.class);
			query.setParameter("idConvenio", idConvenio);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstadoConvenio", estadoConvenio.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioServicio> getConvenioServicioByProgramaAnoEstablecimientoEstado(Integer idProgramaAno, Integer idEstablecimiento, EstadosConvenios estadoConvenio) {
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdProgramaAnoIdEstablecimientoIdEstadoConvenio", ConvenioServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idEstadoConvenio", estadoConvenio.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ConvenioComuna> getConvenioComunaByProgramaAnoComunaEstado(Integer idProgramaAno, Integer idComuna, EstadosConvenios estadoConvenio) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdComunaIdEstadoConvenio", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idEstadoConvenio", estadoConvenio.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ConvenioComunaComponente getConvenioComunaComponenteById(Integer idConvenioComunaComponente) {
		try {
			TypedQuery<ConvenioComunaComponente> query = this.em.createNamedQuery("ConvenioComunaComponente.findByIdConvenioComunaComponente", ConvenioComunaComponente.class);
			query.setParameter("idConvenioComunaComponente", idConvenioComunaComponente);
			List<ConvenioComunaComponente> results = query.getResultList();
			if (results != null && results.size() > 0)
				return results.get(0);
			return null;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
