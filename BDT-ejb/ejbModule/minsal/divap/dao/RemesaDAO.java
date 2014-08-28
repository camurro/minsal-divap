package minsal.divap.dao;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.Remesa;


@Singleton
public class RemesaDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;
	


	public Integer crearRemesa(Remesa remesa) {
	
		this.em.persist(remesa);
		return remesa.getIdRemesa();
	}

	public Remesa findById(Integer id){
		return em.find(Remesa.class,id);
		
	}
	
	public Integer actualizarRemesa(Remesa remesa){
		try {
			
			Remesa remesaActualizar =  findById(remesa.getIdRemesa());
			if(remesaActualizar!=null)
			{
			
				remesaActualizar.setValorDia09(remesa.getValorDia09());
				remesaActualizar.setValorDia24(remesa.getValorDia24());
				remesaActualizar.setValorDia28(remesa.getValorDia28());
				this.em.persist(remesaActualizar);
				
				return remesaActualizar.getIdRemesa();
			}
			return null;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
