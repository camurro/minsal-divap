package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.Subtitulo;
import cl.minsal.divap.model.Convenio;
import cl.minsal.divap.model.ReferenciaDocumento;



@Singleton
public class ConveniosDAO {
	@PersistenceContext 
	private EntityManager emm;
	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	

	

	public List<Convenio> getConveniosForSubGridMuni(Integer programaID,Integer componenteID, Integer comunaID, Integer establecimientoID, Integer muni1, Integer muni3 ){
		try {
			TypedQuery<Convenio> query = this.em.createNamedQuery("Convenio.findByComunaEstablecimientoProgramaComponenteMuniORDERMontoDesc", Convenio.class);
			query.setParameter("idPrograma", programaID);
			query.setParameter("idComponente", componenteID);
			query.setParameter("idComuna", comunaID);
			query.setParameter("idEstablecimiento", establecimientoID);
			query.setParameter("muni1", muni1);
			query.setParameter("muni3", muni3);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	public List<Convenio> getConveniosForGridMuniServi(Integer programaID,Integer componenteID, Integer comunaID, Integer establecimientoID, Integer muni1, Integer muni3 ){
		try {
			TypedQuery<Convenio> query = this.em.createNamedQuery("Convenio.findByComunaEstablecimientoProgramaComponenteMuni", Convenio.class);
			query.setParameter("idPrograma", programaID);
			query.setParameter("idComponente", componenteID);
			query.setParameter("idComuna", comunaID);
			query.setParameter("idEstablecimiento", establecimientoID);
			query.setParameter("muni1", muni1);
			query.setParameter("muni3", muni3);
			
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	
	
	
	public void setDocumentoConvenioById(String nodoref,String filename,Integer id){

		
	}
		

	
	
	public Convenio getConveniosById(Integer id){
		try {
			TypedQuery<Convenio> query = this.em.createNamedQuery("Convenio.findByConveniosById", Convenio.class);
			query.setParameter("id", id);
			List<Convenio> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Convenio save(Convenio Convenio) {
		this.em.persist(Convenio);
		return Convenio;
	}
	
	
	public Convenio updateConvenioByIdAprobacion(Integer id,boolean apro){
		try {
			Convenio con= findById(id);
			if(con != null){
				con.setAprobacion(apro);
			}
		  return null;
		    } catch (Exception e) {
			throw new RuntimeException(e);
		    }
	}
	
	
	public Convenio setConvenioById(Integer id, Integer numeroResolucion, Integer monto){
		try {
			Convenio con = findById(id);
			if(con != null){
				con.setMonto(monto);
				con.setNumeroResolucion(numeroResolucion);
			}
		  return null;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	
	}
	
	public Convenio findById(Integer id){
		return em.find(Convenio.class,id);
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


	public List<Convenio> getByIDSubComponenteComunaProgramaAno(int sub,String componenteSeleccionado, String comunaSeleccionada,String establecimientoSeleccionado, Integer ano, Integer programa) {
		try {
			TypedQuery<Convenio> query = this.em.createNamedQuery("Convenio.findByIDSubComponenteComunaProgramaAno", Convenio.class);
			query.setParameter("idPrograma", programa);
			query.setParameter("idComponente",Integer.parseInt(componenteSeleccionado));
			query.setParameter("idComuna", Integer.parseInt(comunaSeleccionada));
			query.setParameter("idEstablecimiento", Integer.parseInt(establecimientoSeleccionado));
			query.setParameter("ano", ano);
			query.setParameter("sub", sub);
			
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Convenio> getByIDSubComponenteComunaProgramaAnoAprobacion(int sub,String componenteSeleccionado, String comunaSeleccionada,String establecimientoSeleccionado, Integer ano, Integer programa,boolean apro) {
		try {
			TypedQuery<Convenio> query = this.em.createNamedQuery("Convenio.findByIDSubComponenteComunaProgramaAnoAprobacion", Convenio.class);
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

	public List<Convenio> getConveniosSummaryByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, Integer idServicio, List<Integer> idComponentes, Subtitulo subtitulo) {
		try {
			TypedQuery<Convenio> query = this.em.createNamedQuery("Convenio.findByIdProgramaAnoIdServicioIdComponenteIdSubtitulo", Convenio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idTipoSubtitulo", subtitulo.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
