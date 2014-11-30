package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Comuna;



@Singleton
public class AntecedentesComunaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public AntecendentesComuna findById(Integer documentId) {
		return this.em.find(AntecendentesComuna.class, documentId);
	}

	public Integer createAntecendentesComunaCalcuado(AntecendentesComunaCalculado antecendentesComunaCalculado) {
		em.persist(antecendentesComunaCalculado);;
		return antecendentesComunaCalculado.getIdAntecendentesComunaCalculado();
	}

	public Comuna findByComunaServicio(String servicio,	String comuna, Integer anoCurso) {
		Comuna entity = null;
		try {
			System.out.println("servicio->"+servicio+" comuna->"+comuna+" anoCurso->"+anoCurso);
			TypedQuery<Comuna> query = this.em.createNamedQuery("Comuna.findByComunaServicioAno", Comuna.class);
			query.setParameter("comuna", comuna);
			query.setParameter("servicio", servicio);
			query.setParameter("anoCurso", anoCurso);
			List<Comuna> comunas = query.getResultList(); 
			if(comunas != null && comunas.size() > 0){
				entity = comunas.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return entity;
	}
	
	public Comuna findByComunaById(Integer idComuna) {
		Comuna entity = null;
		try {
			System.out.println("idComuna->"+idComuna);
			TypedQuery<Comuna> query = this.em.createNamedQuery("Comuna.findById", Comuna.class);
			query.setParameter("id", idComuna);
			List<Comuna> comunas = query.getResultList(); 
			if(comunas != null && comunas.size() > 0){
				entity = comunas.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return entity;
	}

	public AntecendentesComunaCalculado findByAntecedentesDistrinbucionInicial(
			Integer idAntecedentesComuna, Integer idDistribucionInicialPercapita) {
		AntecendentesComunaCalculado entity = null;
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByAntecedentesDistrinbucionInicial", AntecendentesComunaCalculado.class);
			query.setParameter("idAntecendentesComuna", idAntecedentesComuna);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			List<AntecendentesComunaCalculado> antecendentesComunaCalculado = query.getResultList(); 
			if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
				entity = antecendentesComunaCalculado.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return entity;
	}

	public List<AntecendentesComunaCalculado> findAntecedentesComunaCalculadosByDistribucionInicialPercapita(
			Integer idDistribucionInicialPercapita) {
		List<AntecendentesComunaCalculado> results = null;
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByDistribucionInicialPercapita", AntecendentesComunaCalculado.class);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			results = query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}
	
	public List<AntecendentesComunaCalculado> findAntecedentesServicioCalculadosByDistribucionInicialPercapita(
			Integer idDistribucionInicialPercapita) {
		List<AntecendentesComunaCalculado> results = null;
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByServicioDistribucionInicialPercapita", AntecendentesComunaCalculado.class);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			results = query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> groupPercapitaServicioByDistribucionInicialPercapita(Integer idDistribucionInicialPercapita) {
		List<Object[]> results = null;
		try {
			Query queryGroupPercapitaServicioByDistribucionInicialPercapita = this.em.createNamedQuery("AntecendentesComuna.groupPercapitaServicio");
			queryGroupPercapitaServicioByDistribucionInicialPercapita.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			results = queryGroupPercapitaServicioByDistribucionInicialPercapita.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}

	public Double findPerCapitaCostoFijoByServicioComunaAnoAnterior(
			Integer comuna, Integer servicio, Integer anoAnterior) {
		Double result = null;
		try {
			System.out.println("comuna="+comuna+" servicio="+servicio+" anoAnterior="+anoAnterior);
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByComunaServicioAnoCurso", AntecendentesComunaCalculado.class);
			query.setParameter("idComuna", comuna);
			query.setParameter("idServicio", servicio);
			query.setParameter("anoEnCurso", anoAnterior);
			List<AntecendentesComunaCalculado> results = query.getResultList(); 
			if(results != null && results.size() > 0){
				result = results.get(0).getValorPerCapitaComunalMes();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public AntecendentesComuna findAntecendentesComunaByComunaServicioAno(
			String servicio, String comuna, Integer anoCurso) {
		AntecendentesComuna result = null;
		try {
			TypedQuery<AntecendentesComuna> query = this.em.createNamedQuery("AntecendentesComuna.findAntecendentesComunaByComunaServicioAno", AntecendentesComuna.class);
			query.setParameter("nombreComuna", comuna);
			query.setParameter("nombreServicio", servicio);
			query.setParameter("anoEnCurso", anoCurso);
			List<AntecendentesComuna> results = query.getResultList(); 
			if(results != null && results.size() > 0){
				result = results.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<AntecendentesComunaCalculado> findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapita(
			Integer servicio, Integer comuna, Integer idDistribucionInicialPercapita) {
		List<AntecendentesComunaCalculado> results = null;
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByComunaServicioDistribucionInicialPercapita", AntecendentesComunaCalculado.class);
			query.setParameter("idComuna", comuna);
			query.setParameter("idServicio", servicio);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			results = query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}
	
	public List<AntecendentesComunaCalculado> findAntecendentesComunaCalculadoByServicioDistribucionInicialPercapita(
			Integer servicio, Integer idDistribucionInicialPercapita) {
		List<AntecendentesComunaCalculado> results = null;
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByServicioDistribucionInicialPercapita", AntecendentesComunaCalculado.class);
			query.setParameter("idServicio", servicio);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			results = query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}

	public List<AntecendentesComuna> findAntecendentesComunaByidDistribucionInicialPercapita(Integer idDistribucionInicialPercapita) {
		List<AntecendentesComuna> results = null;
		try {
			TypedQuery<AntecendentesComuna> query = this.em.createNamedQuery("AntecendentesComuna.findByDistribucionInicialPercapita", AntecendentesComuna.class);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			results = query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}
	
	public List<AntecendentesComunaCalculado> findAntecendentesComunaCalculadoByComunasDistribucionInicialPercapita(
			 List<Integer> comunas, Integer idDistribucionInicialPercapita) {
		List<AntecendentesComunaCalculado> results = null;
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByComunasDistribucionInicialPercapita", AntecendentesComunaCalculado.class);
			query.setParameter("comunas", comunas);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			results = query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> groupDesempenoDificilServicioByDistribucionInicialPercapita(Integer idDistribucionInicialPercapita) {
		List<Object[]> results = null;
		try {
			Query queryGroupDesempenoDificilServicioByDistribucionInicialPercapita = this.em.createNamedQuery("AntecendentesComuna.groupDesempenoDificilServicio");
			queryGroupDesempenoDificilServicioByDistribucionInicialPercapita.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			results = queryGroupDesempenoDificilServicioByDistribucionInicialPercapita.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}

	public List<AntecendentesComunaCalculado> findAntecedentesComunaCalculadosByDistribucionInicialPercapitaServicio(
			Integer idDistribucionInicialPercapita, Integer idServicio) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByDistribucionInicialPercapitaServicio", AntecendentesComunaCalculado.class);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public AntecendentesComunaCalculado findByIdAntecedentesComuna(
			Integer idAntecedentesComuna) {
		AntecendentesComunaCalculado entity = null;
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByIdAntecedentesComuna", AntecendentesComunaCalculado.class);
			query.setParameter("idAntecendentesComuna", idAntecedentesComuna);
			List<AntecendentesComunaCalculado> antecendentesComunaCalculado = query.getResultList(); 
			if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
				entity = antecendentesComunaCalculado.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return entity;
	}

	public AntecendentesComunaCalculado findByComunaAno(Integer idComuna, Integer ano) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByIdComunaAnoAprobado", AntecendentesComunaCalculado.class);
			query.setParameter("idComuna", idComuna);
			query.setParameter("ano", ano);
			query.setParameter("aprobado", new Boolean(false));
			List<AntecendentesComunaCalculado> antecendentesComunaCalculado = query.getResultList(); 
			if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
				return antecendentesComunaCalculado.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public AntecendentesComuna getAntecendentesComunaByComunaAno(Integer idComuna, Integer ano) {
		AntecendentesComuna result = null;
		try {
			TypedQuery<AntecendentesComuna> query = this.em.createNamedQuery("AntecendentesComuna.findAntecendentesComunaByIdComunaIdAno", AntecendentesComuna.class);
			query.setParameter("idComuna", idComuna);
			query.setParameter("ano", ano);
			List<AntecendentesComuna> results = query.getResultList(); 
			if(results != null && results.size() > 0){
				result = results.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public AntecendentesComuna save(AntecendentesComuna antecendentesComuna) {
		em.persist(antecendentesComuna);
		return antecendentesComuna;
	}

	public AntecendentesComunaCalculado findByComunaAnoTipoComuna(Integer idComuna,
			Integer anoCurso, List<Integer> tiposComuna) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByIdComunaAnoTiposComuna", AntecendentesComunaCalculado.class);
			query.setParameter("idComuna", idComuna);
			query.setParameter("ano", anoCurso);
			query.setParameter("tiposComuna", tiposComuna);
			List<AntecendentesComunaCalculado> antecendentesComunaCalculado = query.getResultList(); 
			if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
				return antecendentesComunaCalculado.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object getPerCapitaBasalByIdServicio(Integer idServicio) {
		try {
			Query query = this.em.createNamedQuery("AntecendentesComunaCalculado.getPerCapitaBasalByIdServicio");
			query.setParameter("idServicio", idServicio);
			List<Object> result =  query.getResultList();
			if(result !=null && result.size()>0){
				return result.get(0);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public Object getDesempenoDificilByIdServicio(Integer idServicio) {
		try {
			Query query = this.em.createNamedQuery("AntecendentesComunaCalculado.getDesempenoDificilByIdServicio");
			query.setParameter("idServicio", idServicio);
			List<Object> result =  query.getResultList();
			if(result !=null && result.size()>0){
				return result.get(0);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return 0;
	}

}
