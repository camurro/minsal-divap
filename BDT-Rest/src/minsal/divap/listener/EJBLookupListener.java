package minsal.divap.listener;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import minsal.divap.service.DistribucionInicialPercapitaService;
import minsal.divap.service.DocumentAlfrescoService;
import minsal.divap.service.EmailService;
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.RebajaService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;

/**
 * Application Lifecycle Listener implementation class EJBLookupListener
 *
 */
@WebListener
public class EJBLookupListener implements ServletContextListener {

	private ServletContext context;

	@EJB
	private EmailService emailService;
	@EJB
	private DocumentAlfrescoService documentService;
	@EJB
	private DistribucionInicialPercapitaService distribucionInicialPercapitaService;
	@EJB
	private RebajaService rebajaService;
	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
        @EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;

	/**
	 * Default constructor. 
	 */
	public EJBLookupListener() {
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		this.context = event.getServletContext();
		add(EmailService.class.getName(), this.emailService);
		add(DocumentAlfrescoService.class.getName(), this.documentService);
		add(DistribucionInicialPercapitaService.class.getName(), this.distribucionInicialPercapitaService);
		add(RebajaService.class.getName(), this.rebajaService);
		add(EstimacionFlujoCajaService.class.getName(), this.estimacionFlujoCajaService);
		add(RecursosFinancierosProgramasReforzamientoService.class.getName(), this.recursosFinancierosProgramasReforzamientoService);
		
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
	}


	private void add(String key, Object o) {
		System.out.println("EJBLookupListener add key="+key);
		this.context.setAttribute(key, o);
	}

}
