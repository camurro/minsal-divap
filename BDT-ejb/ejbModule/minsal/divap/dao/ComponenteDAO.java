package minsal.divap.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.vo.ComponentesVO;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
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
			query1.setParameter("id", programaId);
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
				   Integer aux= componentesPrograma.get(i).getId();
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
	
	
	public List<Componente> getComponenteByPrograma(Integer programaId){
		try {
			TypedQuery<Componente> query = this.em.createNamedQuery("Componente.findByPrograma", Componente.class);
			query.setParameter("id", programaId);
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

	
}
