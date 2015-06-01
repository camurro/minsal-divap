package minsal.divap.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.AntecedentesComunaCalculadoRebaja;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.FactorRefAsigZona;
import cl.minsal.divap.model.FactorTramoPobreza;
import cl.minsal.divap.model.TipoComuna;



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

	public AntecendentesComunaCalculado findByAntecedentesDistrinbucionInicial(Integer idAntecedentesComuna, Integer idDistribucionInicialPercapita) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByAntecedentesDistrinbucionInicial", AntecendentesComunaCalculado.class);
			query.setParameter("idAntecendentesComuna", idAntecedentesComuna);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			List<AntecendentesComunaCalculado> antecendentesComunaCalculado = query.getResultList(); 
			if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
				return antecendentesComunaCalculado.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public AntecendentesComunaCalculado findByAntecedentesDistrinbucionInicialVigente (Integer idAntecedentesComuna, Integer idDistribucionInicialPercapita) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByAntecedentesDistrinbucionInicialVigente", AntecendentesComunaCalculado.class);
			query.setParameter("idAntecendentesComuna", idAntecedentesComuna);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			List<AntecendentesComunaCalculado> antecendentesComunaCalculado = query.getResultList(); 
			if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
				return antecendentesComunaCalculado.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<AntecendentesComunaCalculado> findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigente(Integer idDistribucionInicialPercapita) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByDistribucionInicialPercapitaVigente", AntecendentesComunaCalculado.class);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<AntecendentesComunaCalculado> findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigenteModificacion(Integer idDistribucionInicialPercapita, Integer idModificacionPercapita) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByDistribucionInicialPercapitaVigenteModificacionPercapita", AntecendentesComunaCalculado.class);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idModificacionPercapita", idModificacionPercapita);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<AntecendentesComunaCalculado> findAntecedentesServicioCalculadosByDistribucionInicialPercapitaVigente(Integer idServicio, Integer idDistribucionInicialPercapita) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByServicioDistribucionInicialPercapitaVigente", AntecendentesComunaCalculado.class);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

	public AntecendentesComuna findAntecendentesComunaByComunaServicioAno(String servicio, String comuna, Integer anoCurso) {
		try {
			System.out.println("servicio = " + servicio);
			System.out.println("comuna= " + comuna);
			System.out.println("anoCurso= " + anoCurso);
			TypedQuery<AntecendentesComuna> query = this.em.createNamedQuery("AntecendentesComuna.findAntecendentesComunaByComunaServicioAno", AntecendentesComuna.class);
			query.setParameter("nombreComuna", comuna.toLowerCase());
			query.setParameter("nombreServicio", servicio.toLowerCase());
			query.setParameter("anoEnCurso", anoCurso);
			List<AntecendentesComuna> results = query.getResultList(); 
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public AntecendentesComunaCalculado findAntecendentesComunaCalculadoByServicioComunaAno(Integer servicio, Integer comuna, Integer ano) {
		try {
			System.out.println("comuna="+comuna+" servicio="+servicio+" ano="+ano);
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByComunaServicioAnoCurso", AntecendentesComunaCalculado.class);
			query.setParameter("idComuna", comuna);
			query.setParameter("idServicio", servicio);
			query.setParameter("anoEnCurso", ano);
			List<AntecendentesComunaCalculado> results = query.getResultList(); 
			if(results != null && results.size() > 0){
				return results.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<AntecendentesComunaCalculado> findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(
			Integer servicio, Integer comuna, Integer idDistribucionInicialPercapita) {
		List<AntecendentesComunaCalculado> results = null;
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByComunaServicioDistribucionInicialPercapitaVigente", AntecendentesComunaCalculado.class);
			query.setParameter("idComuna", comuna);
			query.setParameter("idServicio", servicio);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			results = query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}
	
	public List<AntecendentesComunaCalculado> findAntecendentesComunaCalculadoByServicioDistribucionInicialPercapitaVigente(
			Integer servicio, Integer idDistribucionInicialPercapita) {
		List<AntecendentesComunaCalculado> results = null;
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByServicioDistribucionInicialPercapitaVigente", AntecendentesComunaCalculado.class);
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
	
	public List<AntecendentesComuna> getAntecedentesComunaByServicioAno(Integer idServicio, Integer ano){
		List<AntecendentesComuna> resultado = new ArrayList<AntecendentesComuna>();
		try {
			TypedQuery<AntecendentesComuna> query = this.em.createNamedQuery("AntecendentesComuna.findAntecendentesComunaByIdServicioIdAno", AntecendentesComuna.class);
			query.setParameter("idServicio", idServicio);
			query.setParameter("ano", ano);
			List<AntecendentesComuna> results = query.getResultList(); 
			if(results != null && results.size() > 0){
				resultado = results;
			}else{
				resultado = null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return resultado;
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
	public Object getPerCapitaBasalByIdServicio(Integer idServicio, Integer idDistribucionInicialPercapita) {
		try {
			Query query = this.em.createNamedQuery("AntecendentesComunaCalculado.getPerCapitaBasalByIdServicio");
			query.setParameter("idServicio", idServicio);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
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
	public Object getDesempenoDificilByIdServicio(Integer idServicio, Integer idDistribucionInicialPercapita) {
		try {
			Query query = this.em.createNamedQuery("AntecendentesComunaCalculado.getDesempenoDificilByIdServicio");
			query.setParameter("idServicio", idServicio);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			List<Object> result =  query.getResultList();
			if(result !=null && result.size()>0){
				return result.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return 0;
	}
	
	public List<AntecendentesComuna> findAntecendentesComunaByidDistribucionInicialPercapitaModificacionPercapita(Integer idDistribucionInicialPercapita, Integer idModificacionPercapita) {
		try {
			TypedQuery<AntecendentesComuna> query = this.em.createNamedQuery("AntecendentesComuna.findByDistribucionInicialPercapitaModificacionPercapita", AntecendentesComuna.class);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idModificacionPercapita", idModificacionPercapita);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public AntecendentesComunaCalculado findByAntecedentesDistrinbucionInicialVigenteModificacionPercapita(Integer idAntecedentesComuna, Integer idDistribucionInicialPercapita, Integer idModificacionPercapita) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByAntecedentesDistrinbucionInicialVigenteModificacionPercapita", AntecendentesComunaCalculado.class);
			query.setParameter("idAntecendentesComuna", idAntecedentesComuna);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idModificacionPercapita", idModificacionPercapita);
			List<AntecendentesComunaCalculado> antecendentesComunaCalculado = query.getResultList(); 
			if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
				return antecendentesComunaCalculado.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<AntecendentesComunaCalculado> findByAntecedentesModificacionPercapitaVigente(Integer idModificacionPercapita) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByModificacionPercapitaVigente", AntecendentesComunaCalculado.class);
			query.setParameter("idModificacionDistribucionInicialPercapita", idModificacionPercapita);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<AntecendentesComunaCalculado> getAntecendentesComunaCalculadoVigenteByRebaja(Integer idProcesoRebaja) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByAntecendentesComunaCalculadoVigenteRebaja", AntecendentesComunaCalculado.class);
			query.setParameter("idRebaja", idProcesoRebaja);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public AntecendentesComunaCalculado findAntecendentesComunaCalculadoByComunaDistribucionInicialPercapitaVigenteRebaja(Integer idComuna, Integer idDistribucionInicialPercapita, Integer idRebaja) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByAntecedentesComunaDistrinbucionInicialVigenteRebaja", AntecendentesComunaCalculado.class);
			query.setParameter("idRebaja", idRebaja);
			query.setParameter("idDistribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idComuna", idComuna);
			List<AntecendentesComunaCalculado> antecendentesComunaCalculado = query.getResultList(); 
			if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
				return antecendentesComunaCalculado.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public AntecedentesComunaCalculadoRebaja save(AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja) {
		em.persist(antecedentesComunaCalculadoRebaja);
		return antecedentesComunaCalculadoRebaja;
	}
	
	public AntecendentesComunaCalculado findByComunaAnoVigencia(Integer idComuna, Integer ano) {
		AntecendentesComunaCalculado entity = null;
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByComunaAnoVigencia", AntecendentesComunaCalculado.class);
			query.setParameter("idComuna", idComuna);
			query.setParameter("ano", ano);
			List<AntecendentesComunaCalculado> antecendentesComunaCalculado = query.getResultList(); 
			if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
				entity = antecendentesComunaCalculado.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return entity;
	}
	
	public int countAntecendentesComunaCalculadoVigente(Integer ano){
		try {
			TypedQuery<Number> query = this.em.createNamedQuery("AntecendentesComunaCalculado.countAntecendentesComunaCalculadoVigente", Number.class);
			query.setParameter("anoEnCurso", ano);
			return ((Number)query.getSingleResult()).intValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<AntecendentesComunaCalculado> findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigenteServicio(Integer idDistribucionInicialPercapita, Integer idServicio) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByDistribucionInicialPercapitaVigenteServicio", AntecendentesComunaCalculado.class);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			query.setParameter("idServicio", idServicio);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<AntecendentesComunaCalculado> findByAntecedentesDistrinbucionInicialCaducado(Integer idAntecedentesComuna, Integer idDistribucionInicialPercapita) {
		try {
			TypedQuery<AntecendentesComunaCalculado> query = this.em.createNamedQuery("AntecendentesComunaCalculado.findByAntecedentesDistrinbucionInicialCaducado", AntecendentesComunaCalculado.class);
			query.setParameter("idAntecendentesComuna", idAntecedentesComuna);
			query.setParameter("distribucionInicialPercapita", idDistribucionInicialPercapita);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<FactorRefAsigZona> findAllFactorAsigZona(){
		try {
			TypedQuery<FactorRefAsigZona> query = this.em.createNamedQuery("FactorRefAsigZona.findAll", FactorRefAsigZona.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public FactorRefAsigZona findFactorRefAsigZonaById(Integer idFactorRefAsigZona){
		try {
			TypedQuery<FactorRefAsigZona> query = this.em.createNamedQuery("FactorRefAsigZona.findByIdFactorRefAsigZona", FactorRefAsigZona.class);
			query.setParameter("idFactorRefAsigZona", idFactorRefAsigZona);
			List<FactorRefAsigZona> listado = query.getResultList(); 
			if(listado != null && listado.size() > 0){
				return listado.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<FactorTramoPobreza> findAllTramoPobreza(){
		try {
			TypedQuery<FactorTramoPobreza> query = this.em.createNamedQuery("FactorTramoPobreza.findAll", FactorTramoPobreza.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public FactorTramoPobreza findFactorTramoPobrezaById(Integer idFactorTramoPobreza){
		try {
			TypedQuery<FactorTramoPobreza> query = this.em.createNamedQuery("FactorTramoPobreza.findByIdFactorTramoPobreza", FactorTramoPobreza.class);
			query.setParameter("idFactorTramoPobreza", idFactorTramoPobreza);
			List<FactorTramoPobreza> listado = query.getResultList(); 
			if(listado != null && listado.size() > 0){
				return listado.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public List<TipoComuna> findAllTipoComunas(){
		try {
			TypedQuery<TipoComuna> query = this.em.createNamedQuery("TipoComuna.findAll", TipoComuna.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public TipoComuna findTipoComunaById(Integer idTipoComuna){
		try {
			TypedQuery<TipoComuna> query = this.em.createNamedQuery("TipoComuna.findByIdTipoComuna", TipoComuna.class);
			query.setParameter("idTipoComuna", idTipoComuna);
			List<TipoComuna> listado = query.getResultList(); 
			if(listado != null && listado.size() > 0){
				return listado.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
