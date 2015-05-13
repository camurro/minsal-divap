package minsal.divap.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.Subtitulo;
import minsal.divap.vo.ComponentesVO;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.TipoComponente;

@Singleton
public class ComponenteDAO {
	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public List<Componente> getComponentes(){
		try {
			TypedQuery<Componente> query = this.em.createNamedQuery("Componente.findAll", Componente.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Componente> getComponenteByProgramaSubtitulo(Integer programaId, Integer sub ){
		try {
			//toma todos los componenetes por programa 
			TypedQuery<Componente> query1 = this.em.createNamedQuery("Componente.findByPrograma", Componente.class);
			query1.setParameter("idPrograma", programaId);
			List<Componente> conpo = query1.getResultList();
			
			List<ComponentesVO> componentesPrograma = new ArrayList<ComponentesVO>();
			List<ComponentesVO> componentesPrograma2 = new ArrayList<ComponentesVO>();
		
			for (Componente componente : conpo){
					ComponentesVO comVO = new ComponentesVO();
					comVO.setId(componente.getId());
					comVO.setNombre(componente.getNombre());
					componentesPrograma.add(comVO);
				}
			
			//busca cada  componente seleccionado anterior mente si es o no del subtitulo entregado y retorna los id de los componentes que corresponden por subtitulo
			 for (int i = 0; i < componentesPrograma.toArray().length ; i++){
				 TypedQuery<ComponenteSubtitulo> query = this.em.createNamedQuery("ComponenteSubtitulo.findByProgramaSub", ComponenteSubtitulo.class);
				    Integer aux = componentesPrograma.get(i).getId();
				 	query.setParameter("componente", aux );
					query.setParameter("subtitulo", sub);
					List<ComponenteSubtitulo> conpo2 = query.getResultList();
					
				
					for (ComponenteSubtitulo componente2 : conpo2){
						ComponentesVO comVO2 = new ComponentesVO();
						comVO2.setId(componente2.getComponente().getId());
						comVO2.setNombre(componente2.getComponente().getNombre());
						componentesPrograma2.add(comVO2);
					}
			 }
			 
			List<Componente> con = null ;
			
//busca por cada id de componente seleccionado anterior mente  el componente y lo retorna como una lista de componente
			List<Componente> componentesPrograma3 = new ArrayList<Componente>();
			for (int i = 0; i < componentesPrograma2.toArray().length ; i++){
				  TypedQuery<Componente> query3 = this.em.createNamedQuery("Componente.findByPrograma", Componente.class);
					query3.setParameter("id", componentesPrograma2.get(i).getId());
					 con = query3.getResultList();
							for (Componente componente : con){
								Componente comVO3 = new Componente();
								comVO3.setId(componente.getId());
								comVO3.setNombre(componente.getNombre());
								componentesPrograma3.add(comVO3);
						}
			 
			  }
			  return componentesPrograma3;
			 
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Componente> getComponenteByPrograma(Integer idPrograma){
		try {
			TypedQuery<Componente> query = this.em.createNamedQuery("Componente.findByPrograma", Componente.class);
			query.setParameter("idPrograma", idPrograma);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Componente getComponenteByID(Integer Id){
		try {
			TypedQuery<Componente> query = this.em.createNamedQuery("Componente.findById", Componente.class);
			query.setParameter("id", Id);
			List<Componente> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Componente getComponenteByNombre(String componente) {
		try{
			TypedQuery<Componente> query = this.em.createNamedQuery("Componente.findByNombre", Componente.class);
			query.setParameter("nombre", componente.toLowerCase());
			List<Componente> results = query.getResultList();
			if (results.size() > 0)
				return results.get(0);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public List<Componente> getComponentesByIdProgramaIdSubtitulos(Integer idPrograma, Subtitulo... subtitulos) {
		try {
			List<Integer> idSubtitulos = new ArrayList<Integer>();
			for(Subtitulo subtitulo : subtitulos){
				idSubtitulos.add(subtitulo.getId());
			}
			TypedQuery<Componente> query = this.em.createNamedQuery("Componente.findByIdProgramaIdSubtitulos", Componente.class);
			query.setParameter("idTipoSubtitulos", idSubtitulos);
			query.setParameter("idPrograma", idPrograma);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ConvenioComuna getConvenioComunaByProgramaAnoComponenteSubtituloComuna(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idComuna) {
		try {
			TypedQuery<ConvenioComuna> query = this.em.createNamedQuery("ConvenioComuna.findByIdProgramaAnoIdComponenteIdSubtituloIdComuna", ConvenioComuna.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("idComuna", idComuna);
			List<ConvenioComuna> results = query.getResultList();
			if (results.size() > 0)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ConvenioServicio getConvenioServicioByProgramaAnoComponenteSubtituloEstablecimiento(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idEstablecimiento) {
		try {
			TypedQuery<ConvenioServicio> query = this.em.createNamedQuery("ConvenioServicio.findByIdProgramaAnoIdComponenteIdSubtituloIdEstablecimiento", ConvenioServicio.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			List<ConvenioServicio> results = query.getResultList();
			if (results.size() > 0)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<TipoComponente> getAllTipoComponente(){
		try {
			TypedQuery<TipoComponente> query = this.em.createNamedQuery("TipoComponente.findAll" , TipoComponente.class);
			List<TipoComponente> result = query.getResultList();
			return query.getResultList();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public TipoComponente getTipoComponenteById(Integer idTipoComponente){
		try {
			TypedQuery<TipoComponente> query = this.em.createNamedQuery("TipoComponente.findById" , TipoComponente.class);
			query.setParameter("idTipoComponente", idTipoComponente);
			List<TipoComponente> result = query.getResultList();
			if (result.size() > 0)
				return result.get(0);
			return null;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
