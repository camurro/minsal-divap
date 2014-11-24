package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.Subtitulo;
import cl.minsal.divap.model.DetalleRemesas;
import cl.minsal.divap.model.Remesa;


@Singleton
public class RemesasDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	
	public List<DetalleRemesas> getRemesasPagadasComunaLaFecha(Integer idProgramaAno, Integer idComuna, Integer idTipoSubtitulo, Integer mes){
		
		try{
			TypedQuery<DetalleRemesas> query = this.em.createNamedQuery("DetalleRemesas.getRemesasPagadasComunaLaFecha",DetalleRemesas.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idMes", mes);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			query.setParameter("remesaPagada", new Boolean(true));
			return query.getResultList();
		}catch(Exception e){
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

}
