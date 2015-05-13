
package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

	public List<Caja> getConvenioRemesaByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo) {
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByProgramaAnoComponentesSubtitulo", Caja.class);
			query.setParameter("idSubtitulo", subtitulo.getId());
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

	public List<Caja> getCajaByProgramaAnoSubtitulo(Integer valorComboPrograma, Subtitulo subtitulo) {
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByProgramaAnoSubtitulo", Caja.class);
			query.setParameter("idTipoSubtitulo", subtitulo.getId());
			query.setParameter("idProgramaAno", valorComboPrograma);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<CajaMonto> getCajaMontoReparosByProgramaAno(Integer idProgramaAno) {
		try {
			TypedQuery<CajaMonto> query = this.em.createNamedQuery("CajaMonto.findByProgramaAnoReparo", CajaMonto.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("reparos", new Boolean(true));
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int updateMarcoReparos(Integer idProgramaAno) {
		Query queryUpdate = this.em.createNamedQuery("MarcoPresupuestario.updateMarcoProgramaAnoReparos");
		queryUpdate.setParameter("idProgramaAno", idProgramaAno);
		return queryUpdate.executeUpdate();
	}
	
	public int updateCajaMontoReparos(Integer idProgramaAno) {
		Query queryUpdate = this.em.createNamedQuery("CajaMonto.updateCajaMontoReparos");
		queryUpdate.setParameter("idProgramaAno", idProgramaAno);
		return queryUpdate.executeUpdate();
	}
	
	public Caja getByServicioEstablecimientoProgramaAnoComponenteSubtitulo(Integer idServicio, Integer idEstablecimiento, Integer idProgramaAno, Integer idComponente, Subtitulo subtitulo) {
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByServicioEstablecimientoProgramaAnoComponenteSubtitulo", Caja.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", subtitulo.getId());
			List<Caja> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Caja getByServicioComunaProgramaAnoComponenteSubtitulo(Integer idServicio, Integer idComuna, Integer idProgramaAno, Integer idComponente, Subtitulo subtitulo) {
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByServicioComunaProgramaAnoComponenteSubtitulo", Caja.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", subtitulo.getId());
			List<Caja> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public CajaMonto getCajaMontoByCajaMes(Integer idCaja, Integer idMes) {
		try {
			TypedQuery<CajaMonto> query = this.em.createNamedQuery("CajaMonto.findByCajaMes", CajaMonto.class);
			query.setParameter("mes", idMes);
			query.setParameter("caja", idCaja);
			List<CajaMonto> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer eliminarPropuesta(Integer idProgramaAno) {
		try {
			Integer countDeleted = 0;
			TypedQuery<Number> queryCajaMontoCount = this.em.createNamedQuery("CajaMonto.countByIdProgramaAno", Number.class);
			queryCajaMontoCount.setParameter("idProgramaAno", idProgramaAno);
			int count =  ((Number)queryCajaMontoCount.getSingleResult()).intValue();
			if(count > 0){
				Query queryCajaMonto = this.em.createNamedQuery("CajaMonto.deleteUsingIdProgramaAno");
				queryCajaMonto.setParameter("idProgramaAno", idProgramaAno);
				queryCajaMonto.executeUpdate();
			}
			TypedQuery<Number> queryCajaCount = this.em.createNamedQuery("Caja.countByIdProgramaAno", Number.class);
			queryCajaCount.setParameter("idProgramaAno", idProgramaAno);
			count =  ((Number)queryCajaCount.getSingleResult()).intValue();
			if(count > 0){
				Query queryCaja = this.em.createNamedQuery("Caja.deleteUsingIdProgramaAno");
				queryCaja.setParameter("idProgramaAno", idProgramaAno);
				countDeleted = queryCaja.executeUpdate();
			}
			return countDeleted;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Caja> getCajasByProgramaAno(Integer idProgramaAno) {
		try {
			TypedQuery<Caja> query = this.em.createNamedQuery("Caja.findByIdProgramaAno", Caja.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int eliminarCajaMontosById(Integer idCaja) {
		try {
			Query queryCajaMonto = this.em.createNamedQuery("CajaMonto.deleteUsingIdCaja");
			queryCajaMonto.setParameter("idCaja", idCaja);
			return queryCajaMonto.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int eliminarCajasById(List<Integer> idCajas) {
		try {
			Query queryCaja = this.em.createNamedQuery("Caja.deleteUsingIds");
			queryCaja.setParameter("idCajas", idCajas);
			return queryCaja.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	public CajaMonto getCajaMontoByServicioEstablecimientoProgramaComponenteSubtituloMes(Integer idServicio, Integer idEstablecimiento, Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer mes) {
		try {
			TypedQuery<CajaMonto> query = this.em.createNamedQuery("CajaMonto.findByServicioEstablecimientoProgramaComponenteSubtituloMes", CajaMonto.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("mes", mes);
			List<CajaMonto> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public CajaMonto getCajaMontoByServicioComunaProgramaComponenteSubtituloMes(Integer idServicio, Integer idComuna, Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer mes) {
		try {
			TypedQuery<CajaMonto> query = this.em.createNamedQuery("CajaMonto.findByServicioComunaProgramaComponenteSubtituloMes", CajaMonto.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idSubtitulo", idSubtitulo);
			query.setParameter("mes", mes);
			List<CajaMonto> results = query.getResultList();
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<CajaMonto> getByProgramaAnoServicioSubtituloMes(Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo, Integer idMes) {
		try {
			TypedQuery<CajaMonto> query = this.em.createNamedQuery("CajaMonto.findByProgramaServicioSubtituloMes", CajaMonto.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idSubtitulo", subtitulo.getId());
			query.setParameter("mes", idMes);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<CajaMonto> getByProgramaAnoServicioEstablecimientoSubtituloMes(Integer idProgramaAno, Integer idServicio, Integer idEstablecimiento, Subtitulo subtitulo, Integer idMes) {
		try {
			TypedQuery<CajaMonto> query = this.em.createNamedQuery("CajaMonto.findByProgramaServicioSubtituloMes", CajaMonto.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idSubtitulo", subtitulo.getId());
			query.setParameter("mes", idMes);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

