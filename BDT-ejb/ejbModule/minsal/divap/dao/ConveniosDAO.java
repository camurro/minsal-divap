package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.Subtitulo;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.ReferenciaDocumento;



@Singleton
public class ConveniosDAO {
	@PersistenceContext 
	private EntityManager emm;
	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public void setDocumentoConvenioById(String nodoref,String filename,Integer id){

		
	}
		

	
	
	public ConvenioComuna getConveniosById(Integer id){
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByConveniosById", ConvenioComuna.class);
			query.setParameter("id", id);
			List<ConvenioComuna> results = query.getResultList();
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
	
	public ConvenioComuna updateConvenioByIdAprobacion(Integer id,boolean apro){
		try {
			ConvenioComuna con= findById(id);
			if(con != null){
				con.setAprobacion(apro);
			}
		  return null;
		    } catch (Exception e) {
			throw new RuntimeException(e);
		    }
	}
	
	
	public ConvenioComuna setConvenioById(Integer id, Integer numeroResolucion, Integer monto){
		try {
			ConvenioComuna con = findById(id);
			if(con != null){
				con.setMonto(monto);
				con.setNumeroResolucion(numeroResolucion);
			}
		  return null;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	
	}
	
	public ConvenioComuna findById(Integer id){
		return em.find(ConvenioComuna.class,id);
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

	public List<ConvenioComuna> getByIDSubComponenteComunaProgramaAnoAprobacion(int sub,String componenteSeleccionado, String comunaSeleccionada,String establecimientoSeleccionado, Integer ano, Integer programa,boolean apro) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIDSubComponenteComunaProgramaAnoAprobacion", ConvenioComuna.class);
			query.setParameter("idPrograma", programa);
			query.setParameter("idComponente",Integer.parseInt(componenteSeleccionado));
			query.setParameter("idComuna", Integer.parseInt(comunaSeleccionada));
			query.setParameter("idEstablecimiento", Integer.parseInt(establecimientoSeleccionado));
			query.setParameter("ano", ano);
			query.setParameter("sub", sub);
			query.setParameter("apro", apro);
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
	
}
