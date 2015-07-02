
package minsal.divap.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import minsal.divap.enums.Subtitulo;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaComponente;
import cl.minsal.divap.model.ProgramaFechaRemesa;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.ProgramaServicioCore;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;
import cl.minsal.divap.model.ProgramaSubtituloComponentePeso;



@Singleton
public class ProgramasDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;


	public ProgramaAno getProgramaAnoByID(Integer idProgramaAno){
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByIdProgramaAno", ProgramaAno.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			List <ProgramaAno> programas = query.getResultList(); 
			if(programas != null && programas.size() > 0){
				return programas.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ProgramaAno getProgramaAnoByIdPrograma(Integer idPrograma){
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByIdPrograma", ProgramaAno.class);
			query.setParameter("idPrograma", idPrograma);
			List <ProgramaAno> programas = query.getResultList(); 
			if(programas != null && programas.size() > 0){
				return programas.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	public List<Programa> getProgramasByUser(String username){
		try {
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findByUser", Programa.class);
			query.setParameter("usuario", username);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Programa> getComponenteByPrograma(int programaId){
		try {
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findComponentesByPrograma", Programa.class);
			query.setParameter("id", programaId);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public  Programa getProgramaPorID(int programaId){
		System.out.println("\n###\n###\n###\nprogramaId que llega al metodo getProgramaPorID()--->>>"+programaId+"\n###\n###\n###\n");
		try {
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findById", Programa.class);
			query.setParameter("id", programaId);
			return query.getSingleResult(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaAno> getProgramasByUserAno(String username, Integer anoCurso) {
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByUserAno", ProgramaAno.class);
			query.setParameter("usuario", username);
			query.setParameter("ano", anoCurso);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ProgramaAno getProgramasByIdProgramaAno(Integer idProgramaAno) {
		try {
			return em.find(ProgramaAno.class, idProgramaAno);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ProgramaAno getProgramaAnoByIDProgramaAno(Integer idPrograma, Integer ano) {
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByAnoIdPrograma", ProgramaAno.class);
			query.setParameter("idPrograma", idPrograma);
			query.setParameter("ano", ano);

			return query.getSingleResult(); 

		} catch (Exception e) {

		}
		return null;
	}

	public EstadoPrograma getEstadoProgramaByIdEstado(Integer idEstadoPrograma) {
		try {
			TypedQuery<EstadoPrograma> query = this.em.createNamedQuery("EstadoPrograma.findByIdEstadoPrograma", EstadoPrograma.class);
			query.setParameter("idEstadoPrograma", idEstadoPrograma);
			if (query.getSingleResult()!=null)
				return query.getSingleResult(); 

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public void guardarEstadoFlujoCaja(Integer idEstado, Integer idProgramaAno) {
		ProgramaAno programaAno = getProgramaAnoByID(idProgramaAno);
		EstadoPrograma estadoPrograma = new EstadoPrograma(idEstado);
		programaAno.setEstadoFlujoCaja(estadoPrograma);
		this.em.persist(programaAno);
	}

	public ProgramaAno saveProgramaAno(ProgramaAno programaAno) {
		this.em.persist(programaAno);
		return programaAno;
	}

	public AnoEnCurso saveAnoCurso(AnoEnCurso anoCurso) {
		this.em.persist(anoCurso);
		return anoCurso;
	}

	public AnoEnCurso getAnoEnCursoById(int ano) {
		try {
			TypedQuery<AnoEnCurso> query = this.em.createNamedQuery("AnoEnCurso.findByAno", AnoEnCurso.class);
			query.setParameter("ano", ano);
			if (query.getSingleResult()!=null)
				return query.getSingleResult(); 

		} catch (Exception e) {

		}
		return null;
	}

	public Programa getProgramaById(Integer id){
		try {
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findById", Programa.class);
			query.setParameter("id", id);
			List<Programa> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaMunicipalCoreComponente> getProgramaMunicipales(Integer idProgramaAno, List<Integer> idComponentes, Integer idSubtitulo) {
		try {
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByIdProgramaAnoListaComponentesIdSubtitulo", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaMunicipalCoreComponente> getProgramaMunicipales(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo) {
		try {
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByIdProgramaAnoIdComponenteIdSubtitulo", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaMunicipalCoreComponente> getProgramaMunicipalesDetalle(Integer idProgramaAno, List<Integer> idComponentes, Integer idServicio) {
		try {
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByIdProgramaAnoListaComponenteIdServicio", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponentes", idComponentes);
			query.setParameter("idServicio", idServicio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaMunicipalCoreComponente> getHistoricoMunicipal(Integer idProgramaAno, Integer idComponente, Integer idServicio) {
		try {
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByIdProgramaAnoIdComponenteIdServicio", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idServicio", idServicio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaServicioCoreComponente> getHistoricoServicio(Integer idProgramaAno, Integer idComponente, Integer idServicio) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByIdProgramaAnoIdComponenteIdServicio", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idServicio", idServicio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaServicioCoreComponente> getProgramaServicios(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByIdProgramaAnoIdComponenteIdSubtitulo", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idSubtitulo);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaServicioCoreComponente> getProgramaServicios(Integer idProgramaAno) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByIdProgramaAno", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaServicioCoreComponente> getProgramaServicios(Integer idProgramaAno, Integer idServicio) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByIdProgramaAnoIdServicio", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ProgramaAno getProgramaAnoSiguiente(Integer idPrograma, Integer anoSiguiente) {
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByAnoIdPrograma", ProgramaAno.class);
			query.setParameter("idPrograma", idPrograma);
			query.setParameter("ano", anoSiguiente);
			List<ProgramaAno> results = query.getResultList();
			if (results.size() >= 1) {
				return results.get(0);
			} 
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ProgramaAno save(ProgramaAno programaAno) {
		this.em.persist(programaAno);
		return programaAno;
	}

	public List<ProgramaMunicipalCoreComponente> findByServicioComponente(Integer idComponente, Integer idServicio, Integer idProgramaAno){
		try {
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByServicioComponentePrograma", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idProgramaAno", idProgramaAno);

			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<ProgramaServicioCoreComponente> findByIdProgramaAnoIdComponenteIdServicio(Integer idProgramaAno, Integer idComponente, Integer idServicio) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByIdProgramaAnoIdComponenteIdServicio", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idServicio", idServicio);
			
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaServicioCoreComponente> findByServicioComponenteServicios(
			Integer idComponente, Integer idServicio) {
		try {
			System.out.println("idComponente="+idComponente+" idServicio="+idServicio);
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByServicioComponente", ProgramaServicioCoreComponente.class);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idServicio", idServicio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



	public ProgramaMunicipalCoreComponente getProgramaMunicipalCoreComponente(
			Integer idProgramaMunicipalCore, Integer idComponente) {
		try{
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByProgramaMunicipalCoreComponentePK", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idProgramaMunicipalCore", idProgramaMunicipalCore);
			query.setParameter("idComponente", idComponente);
			List<ProgramaMunicipalCoreComponente> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public List<ProgramaServicioCoreComponente> getProgramaServicioCoreComponente(
			Integer idProgramaServicioCore, Integer idComponente) {
		try{
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByProgramaServicioCoreComponentePK", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaServicioCore", idProgramaServicioCore);
			query.setParameter("idComponente", idComponente);
			List<ProgramaServicioCoreComponente> results = query.getResultList();
			return results;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public ProgramaServicioCoreComponente getProgramaServicioCoreComponenteEstablecimiento(
			Integer idComponente, Integer idEstablecimiento, Integer idSubtitulo) {
		try{
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByProgramaServicioCoreComponentePKEstablecimiento", ProgramaServicioCoreComponente.class);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idSubtitulo", idSubtitulo);
			List<ProgramaServicioCoreComponente> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}


	public List<Object[]> getResumenMunicipal(Integer idProgramaAno, Integer idTipoSubtitulo){
		List<Object[]> results = null;
		try {
			Query queryGetResumen = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.getResumen");
			queryGetResumen.setParameter("idProgramaAno", idProgramaAno);
			queryGetResumen.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			results = queryGetResumen.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getResumenServicio(Integer idProgramaAno,	Integer idSubtitulo) {
		List<Object[]> results = null;
		try {
			Query queryGetResumen = this.em.createNamedQuery("ProgramaServicioCoreComponente.getResumen");
			queryGetResumen.setParameter("idProgramaAno", idProgramaAno);
			queryGetResumen.setParameter("idTipoSubtitulo", idSubtitulo);
			results = queryGetResumen.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}

	public Integer getIdProgramaAnoAnterior(Integer programaSeleccionado, int anoAnterior) {
		try{
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.getIdProgramaAnoAnterior", ProgramaAno.class);
			query.setParameter("idPrograma", programaSeleccionado);
			query.setParameter("ano", anoAnterior);
			List<ProgramaAno> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0).getIdProgramaAno();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return  null;
	}

	public List<ProgramaAno> getProgramasBySubtitulo(Integer anoCurso, Subtitulo subtitulo) {
		try {
			List<ProgramaAno> programasAno = null;
			TypedQuery<Componente> queryComponente = this.em.createNamedQuery("Componente.findByIdSubtitulo", Componente.class);
			queryComponente.setParameter("idTipoSubtitulo", subtitulo.getId());
			List<Componente> componentes =  queryComponente.getResultList();
			if(componentes != null && componentes.size() > 0){
				List<Integer> idComponentes = new ArrayList<Integer>();
				for(Componente componente : componentes){
					idComponentes.add(componente.getId());
				}
				TypedQuery<ProgramaAno> queryProgramas = this.em.createNamedQuery("ProgramaAno.findByAnoComponente", ProgramaAno.class);
				queryProgramas.setParameter("idComponentes", idComponentes);
				queryProgramas.setParameter("ano", anoCurso);
				programasAno = queryProgramas.getResultList();
			}
			return programasAno;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaMunicipalCore> getProgramasMunicipalCoreByComuna(Integer idComuna){
		try {
			TypedQuery<ProgramaMunicipalCore> query = this.em.createNamedQuery("ProgramaMunicipalCore.findByComuna", ProgramaMunicipalCore.class);
			query.setParameter("comuna", idComuna);
			return query.getResultList();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}





	public ProgramaMunicipalCore findByProgramaAndAnoAndComuna(Integer idPrograma, Integer ano, Integer idComuna){
		try{
			TypedQuery<ProgramaMunicipalCore> queryProgramaMunCore = this.em.createNamedQuery("ProgramaMunicipalCore.findByProgramaAndAnoAndComuna", ProgramaMunicipalCore.class);
			queryProgramaMunCore.setParameter("idPrograma",idPrograma);
			queryProgramaMunCore.setParameter("ano",ano);
			queryProgramaMunCore.setParameter("idComuna",idComuna);
			List<ProgramaMunicipalCore> results = queryProgramaMunCore.getResultList();
			if (results.size() >= 1)
				return results.get(0);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return null;
	}
	public ProgramaMunicipalCore save(ProgramaMunicipalCore nuevoCore) {
		this.em.persist(nuevoCore);
		return nuevoCore;

	}

	public ProgramaServicioCore save(ProgramaServicioCore nuevoCore) {
		this.em.persist(nuevoCore);
		return nuevoCore;

	}

	public ProgramaMunicipalCoreComponente save(ProgramaMunicipalCoreComponente nuevoComponente) {
		this.em.persist(nuevoComponente);
		return nuevoComponente;

	}

	public ProgramaServicioCoreComponente save(ProgramaServicioCoreComponente nuevoComponente) {
		this.em.persist(nuevoComponente);
		return nuevoComponente;
	}

	public List<ProgramaMunicipalCore> getProgramaMunicipalCoreById(
			Integer idPrograma) {
		try{
			TypedQuery<ProgramaMunicipalCore> queryProgramaMunCore = this.em.createNamedQuery("ProgramaMunicipalCore.findByProgramaAno", ProgramaMunicipalCore.class);
			queryProgramaMunCore.setParameter("programaAno",idPrograma);
			return  queryProgramaMunCore.getResultList();

		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaServicioCore> getProgramaServicioCoreById(
			Integer idPrograma) {
		try{
			TypedQuery<ProgramaServicioCore> queryProgramaMunCore = this.em.createNamedQuery("ProgramaServicioCore.findByProgramaAno", ProgramaServicioCore.class);
			queryProgramaMunCore.setParameter("programaAno",idPrograma);
			return  queryProgramaMunCore.getResultList();

		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public List<ProgramaServicioCore> getProgramaServicioCoreByIdEstablecimiento(Integer idEstablecimiento) {
		try{
			TypedQuery<ProgramaServicioCore> queryProgramaMunCore = this.em.createNamedQuery("ProgramaServicioCore.findByEstablecimiento", ProgramaServicioCore.class);
			queryProgramaMunCore.setParameter("idEstablecimiento",idEstablecimiento);
			return  queryProgramaMunCore.getResultList();

		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	

	public List<ProgramaServicioCoreComponente> getProgramaServiciosResumen(
			Integer idProxAno, List<Integer> idComponentesServicio) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByIdProgramaAnoListaComponentes", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProxAno);
			query.setParameter("idComponentes", idComponentesServicio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public ProgramaMunicipalCore getProgramaMunicipalCoreByComunaProgramaAno(Integer idComuna, Integer idProgramaAno){
		try {
			TypedQuery<ProgramaMunicipalCore> query = this.em.createNamedQuery("ProgramaMunicipalCore.findByProgramaAnoComuna", ProgramaMunicipalCore.class);
			query.setParameter("comuna", idComuna);
			query.setParameter("idProgramaAno", idProgramaAno);
			List<ProgramaMunicipalCore> result = query.getResultList();
			if(result != null && result.size() >0 ){
				return result.get(0);
			}
			return null;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaServicioCoreComponente> getProgramaServiciosResumen(
			Integer idProxAno, Integer idServicio,
			List<Integer> idComponentesServicio) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByIdProgramaAnoListaComponentesidServicio", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProxAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponentes", idComponentesServicio);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


	public ProgramaMunicipalCoreComponente getByIdProgramaMunicipalCore(
			Integer idProgramaMunicipalCore) {
		try{
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByIdProgramaMunicipalCore", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idProgramaMunicipalCore", idProgramaMunicipalCore);
			List<ProgramaMunicipalCoreComponente> results = query.getResultList();
			if (results.size() >= 1)
				return results.get(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public List<ProgramaMunicipalCoreComponente> getByIdComuna(Integer idComuna) {
		try{
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByIdComuna", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idComuna", idComuna);
			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaMunicipalCoreComponente> getByIdComuna(Integer idComuna, Integer ano) {
		try{
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByIdComunaAno", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idComuna", idComuna);
			query.setParameter("ano", ano);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaServicioCoreComponente> findByServicioComponenteSubtitulo(
			int idComponentesServicio, int idServicio, Integer idTipoSubtitulo, Integer idProgramaAno) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdServicio", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponente", idComponentesServicio);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public ProgramaServicioCoreComponente getProgramaServicioCoreComponenteByProgramaAnoEstablecimientoServicioComponenteSubtitulo(
			Integer idProgramaAno, Integer idServicio, Integer idEstablecimiento, Integer idComponente, Integer idTipoSubtitulo) {
		try{
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.findByProgramaAnoEstablecimientoServicioComponenteSubtitulo", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idServicio", idServicio);		
			query.setParameter("idComponente", idComponente);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			return query.getResultList().get(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaFechaRemesa> findRemesasByPrograma(Integer idPrograma) {
		try {
			TypedQuery<ProgramaFechaRemesa> query = this.em.createNamedQuery("ProgramaFechaRemesa.findByPrograma", ProgramaFechaRemesa.class);
			query.setParameter("idPrograma", idPrograma);
			return query.getResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public ProgramaFechaRemesa findRemesaByIdProgramaIdFechaRemesa(Integer idPrograma, Integer idFechaRemesa) {
		try {
			TypedQuery<ProgramaFechaRemesa> query = this.em.createNamedQuery("ProgramaFechaRemesa.findByIdProgramaIdFecha", ProgramaFechaRemesa.class);
			query.setParameter("idPrograma", idPrograma);
			query.setParameter("idFechaRemesa", idFechaRemesa);
			List <ProgramaFechaRemesa> programas = query.getResultList(); 
			if(programas != null && programas.size() > 0){
				return programas.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	

	public Long getMPEstablecimientoProgramaAnoComponenteSubtitulo(Integer idEstablecimiento,
			Integer idProgramaAno, Integer componenteSeleccionado,
			Integer idTipoSubtitulo) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.getMPEstablecimientoProgramaAnoComponenteSubtitulo", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idComponente", componenteSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			List<ProgramaServicioCoreComponente> result = query.getResultList();
			if(result.size() > 0){
				return result.get(0).getTarifa().longValue();
			}else{
				return 0l;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Long getMPEstablecimientosServicioProgramaAnoComponenteSubtitulo(Integer idServcio, Integer idProgramaAno, List<Integer> componentesSeleccionados, Integer idTipoSubtitulo) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.getMPEstablecimientosServicioProgramaAnoComponentesSubtitulo", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServcio);
			query.setParameter("idComponentes", componentesSeleccionados);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			List<ProgramaServicioCoreComponente> result = query.getResultList();
			if(result.size() > 0){
				Long mpFinal = 0L;
				for(ProgramaServicioCoreComponente programaServicioCoreComponente : result){
					mpFinal += programaServicioCoreComponente.getTarifa();
				}
				return mpFinal;
			}else{
				return 0l;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Long getMPComunaProgramaAnoComponenteSubtitulo(Integer idComuna, Integer idProgramaAno, Integer componenteSeleccionado, Integer idTipoSubtitulo) {
		try {
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.getMPComunaProgramaAnoComponenteSubtitulo", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idComponente", componenteSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			List<ProgramaMunicipalCoreComponente> result = query.getResultList();
			if(result.size() > 0){
				return result.get(0).getTarifa().longValue();
			}else{
				return 0l;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Long getMPComunasServicioProgramaAnoComponenteSubtitulo(Integer idServicio,
			Integer idProgramaAno, List<Integer> componentesSeleccionados,
			Integer idTipoSubtitulo) {
		try {
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.getMPComunasServicioProgramaAnoComponentesSubtitulo", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponentes", componentesSeleccionados);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			List<ProgramaMunicipalCoreComponente> result = query.getResultList();
			if(result.size() > 0){
				Long mpFinal = 0L;
				for(ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : result){
					mpFinal += programaMunicipalCoreComponente.getTarifa();
				}
				return mpFinal;
			}else{
				return 0L;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Programa> getProgramasFonasa(boolean revisaFonasa) {
		try {
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findByRevisaFonasa", Programa.class);
			query.setParameter("revisaFonasa", new Boolean(revisaFonasa));
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaAno> findByAno(Integer anoCurso) {
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByAno", ProgramaAno.class);
			query.setParameter("ano", anoCurso);
			return  query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ProgramaServicioCore getProgramaservicioCoreByEstablecimientoProgramaAno(
			Integer idEstablecimiento, Integer idProgramaAno) {
		try{
			TypedQuery<ProgramaServicioCore> queryProgramaMunCore = this.em.createNamedQuery("ProgramaServicioCore.getProgramaservicioCoreByEstablecimientoProgramaAno", ProgramaServicioCore.class);
			queryProgramaMunCore.setParameter("idProgramaAno",idProgramaAno);
			queryProgramaMunCore.setParameter("idEstablecimiento",idEstablecimiento);
			List<ProgramaServicioCore>  result=  queryProgramaMunCore.getResultList();
			if(result!=null && result.size()>0){
				return result.get(0);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return null;
	}

	public ProgramaServicioCoreComponente getServicioCoreComponenteByProgramaAnoIdEstablecimientoComponenteSubtitulo(
			Integer idProgramaAno, Integer idEstablecimiento, Integer componenteSeleccionado,
			Integer idTipoSubtitulo) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.getMPEstablecimientoProgramaAnoComponenteSubtitulo", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idComponente", componenteSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			List<ProgramaServicioCoreComponente> result = query.getResultList();
			if(result.size() > 0){
				return result.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public ProgramaServicioCoreComponente getProgramaServicioCoreComponenteByEstablecimientoProgramaAnoComponenteSubtitulo(Integer idServicio,
			Integer idProgramaAno, Integer componenteSeleccionado, Integer idTipoSubtitulo) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.getServicioProgramaAnoComponenteSubtitulo", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idServicio", idServicio);
			query.setParameter("idComponente", componenteSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			List<ProgramaServicioCoreComponente> result = query.getResultList();
			if(result.size() > 0){
				return result.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public ProgramaServicioCore getProgramaServicioCoreByProgramaAnoEstablecimiento(Integer idProgramaAno, Integer idEstablecimiento){
		try{
			TypedQuery<ProgramaServicioCore> query = this.em.createNamedQuery("ProgramaServicioCore.findByProgramaAnoEstablecimiento", ProgramaServicioCore.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			return query.getResultList().get(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public List<Programa> getAllProgramas() {
		try {
			TypedQuery<Programa> query = this.em.createNamedQuery("Programa.findAll", Programa.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<EstadoPrograma> getAllEstadoProgramas(){
		try {
			TypedQuery<EstadoPrograma> query = this.em.createNamedQuery("EstadoPrograma.findAll", EstadoPrograma.class);
			return query.getResultList(); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public EstadoPrograma getEstadoProgramaById(Integer idEstadoPrograma){
		try{
			TypedQuery<EstadoPrograma> query = this.em.createNamedQuery("EstadoPrograma.findByIdEstadoPrograma", EstadoPrograma.class);
			query.setParameter("idEstadoPrograma", idEstadoPrograma);
			return query.getResultList().get(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ProgramaMunicipalCoreComponente> getByIdComunaIdProgramaAno(Integer idComuna, Integer idProgramaAno) {
		try{
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.findByIdComunaIdProgramaAno", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idProgramaAno", idProgramaAno);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Long getMPComunaProgramaAnoSubtitulo(Integer idComuna, Integer idProgramaAno, Integer componenteSeleccionado, Integer idTipoSubtitulo) {
		try {
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.getMPComunaProgramaAnoComponenteSubtitulo", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			List<ProgramaMunicipalCoreComponente> result = query.getResultList();
			if(result.size() > 0){
				return result.get(0).getTarifa().longValue();
			}else{
				return 0l;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Long getTarifaAnteriorComunaProgramaAnoComponenteSubtitulo(Integer idComuna,	Integer idProgramaAno, Integer componenteSeleccionado, Integer idTipoSubtitulo) {
		try {
			TypedQuery<ProgramaMunicipalCoreComponente> query = this.em.createNamedQuery("ProgramaMunicipalCoreComponente.getMPComunaProgramaAnoComponenteSubtitulo", ProgramaMunicipalCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComuna", idComuna);
			query.setParameter("idComponente", componenteSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			List<ProgramaMunicipalCoreComponente> result = query.getResultList();
			if(result.size() > 0 && result.get(0).getTarifaAnterior() != null){
				return result.get(0).getTarifaAnterior().longValue();
			}else{
				return 0l;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ProgramaSubtituloComponentePeso save(ProgramaSubtituloComponentePeso programaSubtituloComponentePeso) {
		em.persist(programaSubtituloComponentePeso);
		return programaSubtituloComponentePeso;
	}
	
	public ProgramaSubtituloComponentePeso getProgramaSubtituloComponentePesoByProgramaComponenteSubtitulo(Integer idProgramaAno,  Integer idComponente, Integer idSubtitulo) {
		try {
			TypedQuery<ProgramaSubtituloComponentePeso> query = this.em.createNamedQuery("ProgramaSubtituloComponentePeso.findProgramaSubtituloComponentePesoByProgramaComponenteSubtitulo", ProgramaSubtituloComponentePeso.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			query.setParameter("idSubtitulo", idSubtitulo);
			List<ProgramaSubtituloComponentePeso> result = query.getResultList();
			if(result.size() > 0){
				return result.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	public List<ProgramaAno> getProgramasBySubtituloFonasa(Integer anoCurso, Subtitulo subtitulo, Boolean fonasa) {
		try {
			List<ProgramaAno> programasAno = null;
			TypedQuery<Componente> queryComponente = this.em.createNamedQuery("Componente.findByIdSubtitulo", Componente.class);
			queryComponente.setParameter("idTipoSubtitulo", subtitulo.getId());
			List<Componente> componentes =  queryComponente.getResultList();
			if(componentes != null && componentes.size() > 0){
				List<Integer> idComponentes = new ArrayList<Integer>();
				for(Componente componente : componentes){
					idComponentes.add(componente.getId());
				}
				TypedQuery<ProgramaAno> queryProgramas = this.em.createNamedQuery("ProgramaAno.findByAnoComponenteFonasa", ProgramaAno.class);
				queryProgramas.setParameter("idComponentes", idComponentes);
				queryProgramas.setParameter("ano", anoCurso);
				queryProgramas.setParameter("fonasa", fonasa);
				programasAno = queryProgramas.getResultList();
			}
			return programasAno;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ProgramaAno getProgramaAnoByIdProgramaAndAno(Integer idPrograma, Integer ano) {
		try {
			TypedQuery<ProgramaAno> query = this.em.createNamedQuery("ProgramaAno.findByAnoIdPrograma", ProgramaAno.class);
			query.setParameter("idPrograma", idPrograma);
			query.setParameter("ano", ano);
			List <ProgramaAno> programas = query.getResultList(); 
			if(programas != null && programas.size() > 0){
				return programas.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Long getTarifaAnteriorEstablecimientoProgramaAnoComponenteSubtitulo(Integer idEstablecimiento, Integer idProgramaAno, Integer componenteSeleccionado, Integer idTipoSubtitulo) {
		try {
			TypedQuery<ProgramaServicioCoreComponente> query = this.em.createNamedQuery("ProgramaServicioCoreComponente.getMPEstablecimientoProgramaAnoComponenteSubtitulo", ProgramaServicioCoreComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idEstablecimiento", idEstablecimiento);
			query.setParameter("idComponente", componenteSeleccionado);
			query.setParameter("idTipoSubtitulo", idTipoSubtitulo);
			List<ProgramaServicioCoreComponente> result = query.getResultList();
			if(result.size() > 0 && result.get(0).getTarifaAnterior() != null){
				return result.get(0).getTarifaAnterior().longValue();
			}else{
				return 0l;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<ProgramaComponente> getProgramaComponenteByIdPrograma(Integer idProgramaAno) {
		try {
			TypedQuery<ProgramaComponente> query = this.em.createNamedQuery("ProgramaComponente.findByIdProgramaAno", ProgramaComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			List<ProgramaComponente> result = query.getResultList();
			if(result.size() > 0){
				return result;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	public ProgramaComponente getProgramaComponenteByIdProgramaIdComponente(Integer idProgramaAno, Integer idComponente) {
		try {
			TypedQuery<ProgramaComponente> query = this.em.createNamedQuery("ProgramaComponente.findByIdProgramaAnoIdComponente", ProgramaComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("idComponente", idComponente);
			List <ProgramaComponente> programas = query.getResultList(); 
			if(programas != null && programas.size() > 0){
				return programas.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ProgramaComponente getProgramaComponenteByIdProgramaNombreComponente(Integer idProgramaAno, String nombreComponente) {
		try {
			TypedQuery<ProgramaComponente> query = this.em.createNamedQuery("ProgramaComponente.findByIdProgramaAnoNombreComponente", ProgramaComponente.class);
			query.setParameter("idProgramaAno", idProgramaAno);
			query.setParameter("nombreComponente", nombreComponente);
			List <ProgramaComponente> programas = query.getResultList(); 
			if(programas != null && programas.size() > 0){
				return programas.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}

