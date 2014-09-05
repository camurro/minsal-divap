package minsal.divap.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import minsal.divap.enums.TipoDocumentosProcesos;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.DocumentoDistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoOt;
import cl.minsal.divap.model.Plantilla;
import cl.minsal.divap.model.ReferenciaDocumento;



@Singleton
public class DocumentOtDAO {

	@PersistenceContext(unitName="BDT-JPA")
	private EntityManager em;

	public ReferenciaDocumento findById(Integer documentId) {
		return this.em.find(ReferenciaDocumento.class, documentId);
	}

	public Integer save(DocumentoOt documentoOt)
	{
		this.em.persist(documentoOt);
		return documentoOt.getIddocumentoOt();
	}


}
