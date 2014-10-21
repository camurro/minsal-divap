
package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.Subtitulo;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.CajaMonto;
import cl.minsal.divap.model.MarcoPresupuestario;
import cl.minsal.divap.model.MontoMes;



@Singleton
public class CajaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public List<Caja> getByIDPrograma(Integer idPrograma){
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByIdPrograma", Caja.class);
			query.setParameter("idPrograma",idPrograma);
			List<Caja> results = query.getResultList();
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public List<Caja> getByIDProgramaAno(Integer idProgramaAno){
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByIdProgramaAno", Caja.class);
			query.setParameter("idProgramaAno",idProgramaAno);
			List<Caja> results = query.getResultList();
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public List<Caja> getByIdProgramaAnoIdServicio(Integer idProgramaAno, Integer idServicio){
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByIdProgramaAnoIdServicio", Caja.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//Verificar que devuelve
	//TODO: Guardar la lista
	public List<Caja> save(List<Caja> caja) {

		for (Caja caja2 : caja) {
			this.em.detach(caja2);
			this.em.persist(caja2);
			
		}
		return caja;
	}
	
	public boolean save(Caja caja) {

		this.em.persist(caja);
		return true;
	}

	public List<Caja> getByIDProgramaAnoSubtitulo(Integer idPrograma, Integer ano, Integer subtitulo) {
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByIdProgramaAnoSubtitulo", Caja.class);
			query.setParameter("idPrograma",idPrograma);
			query.setParameter("ano",ano);
			query.setParameter("idSubtitulo",subtitulo);
			List<Caja> results = query.getResultList();
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public Caja getByID(Integer id) {
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findById", Caja.class);
			query.setParameter("id",id);
			
			if (query.getSingleResult()!=null);
			return query.getSingleResult();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Caja> getByIDSubComponenteComunaProgramaAno(int sub, String componenteSeleccionado, String comunaSeleccionada,
						String establecimientoSeleccionado, int ano, Integer programa) {
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByByIDSubComponenteComunaProgramaAno", Caja.class);
			query.setParameter("idComuna",Integer.parseInt(comunaSeleccionada));
			query.setParameter("idPrograma",programa);
			query.setParameter("ano",ano);
			query.setParameter("idSubtitulo",sub);
			query.setParameter("idComponente",Integer.parseInt(componenteSeleccionado));
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public MarcoPresupuestario save(MarcoPresupuestario marcoPresupuestario) {
		this.em.persist(marcoPresupuestario);
		return marcoPresupuestario;
	}

	public CajaMonto save(CajaMonto cajaMontoActual) {
		this.em.persist(cajaMontoActual);
		return cajaMontoActual;
	}

	public MontoMes save(MontoMes montoMesActual) {
		this.em.persist(montoMesActual);
		return montoMesActual;
	}

	public MarcoPresupuestario getMarcoPresupuestarioByProgramaAnoServicio(Integer idProgramaAno, Integer idServicio) {
		try {
			TypedQuery<MarcoPresupuestario> query = this.em.createNamedQuery("MarcoPresupuestario.findByProgramaAnoServicio", MarcoPresupuestario.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicioSalud", idServicio);
			List<MarcoPresupuestario> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Caja> getMonitoreoByProgramaAnoComponenteSubtituloServicio(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo,
			Integer idServicio) {
		
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByProgramaAnoComponenteSubtituloServicio", Caja.class);
			query.setParameter("idTipoSubtitulo", subtitulo.getId());
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Caja> getMonitoreoByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo) {
		System.out.println("idProgramaAno --> "+idProgramaAno);
		System.out.println("subtitulo --> "+subtitulo);
		System.out.println("idComponente[0] --> "+idComponentes.get(0));
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByProgramaAnoComponenteSubtitulo", Caja.class);
			query.setParameter("idTipoSubtitulo", subtitulo.getId());
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idProgramaAno", idProgramaAno);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Caja> getConvenioRemesaByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo) {
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByProgramaAnoComponenteSubtitulo", Caja.class);
			query.setParameter("idTipoSubtitulo", subtitulo.getId());
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idProgramaAno", idProgramaAno);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Caja> getByProgramaAnoServicioSubtitulo(Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo) {
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByProgramaAnoServicioSubtitulo", Caja.class);
			query.setParameter("idTipoSubtitulo", subtitulo.getId());
			query.setParameter("idServicio", idServicio);
			query.setParameter("idProgramaAno", idProgramaAno);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	

	

}

