package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.Subtitulo;
import cl.minsal.divap.model.Remesa;


@Singleton
public class RemesaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	


	public Integer crearRemesa(Remesa remesa) {
	
		this.em.persist(remesa);
		return remesa.getIdremesa();
	}

	public Remesa findById(Integer id){
		return em.find(Remesa.class,id);
		
	}
	
	public Integer actualizarRemesa(Remesa remesa){
		try {
			
			Remesa remesaActualizar =  findById(remesa.getIdremesa());
			if(remesaActualizar!=null)
			{
			
				remesaActualizar.setValordia09(remesa.getValordia09());
				remesaActualizar.setValordia24(remesa.getValordia24());
				remesaActualizar.setValordia28(remesa.getValordia28());
				this.em.persist(remesaActualizar);
				
				return remesaActualizar.getIdremesa();
			}
			return null;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Remesa> getRemesasSummaryByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, Integer idServicio, List<Integer> idComponentes, Subtitulo subtitulo) {
		try {
			TypedQuery<Remesa> query = this.em.createNamedQuery("Remesa.findByIdProgramaAnoIdServicioIdSubtitulo", Remesa.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicioSalud", idServicio);
			query.setParameter("idTipoSubtitulo", subtitulo.getId());
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Remesa> getRemesasByComuna(Integer idComuna) {
		try {
			TypedQuery<Remesa> query = this.em.createNamedQuery("Remesa.findByComuna", Remesa.class);
			query.setParameter("idComuna", idComuna);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
