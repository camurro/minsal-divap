package minsal.divap.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.RemesasDAO;

@Stateless
@LocalBean
public class RemesaService {
	@EJB
	private RemesasDAO remesaDAO;




}
