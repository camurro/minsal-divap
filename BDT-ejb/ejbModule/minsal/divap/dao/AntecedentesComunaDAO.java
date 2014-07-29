package minsal.divap.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

}