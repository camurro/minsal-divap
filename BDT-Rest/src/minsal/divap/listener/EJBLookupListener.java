package minsal.divap.listener;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import minsal.divap.service.DistribucionInicialPercapitaService;
import minsal.divap.service.DocumentService;
import minsal.divap.service.EmailService;

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
	private DocumentService documentService;
	@EJB
	private DistribucionInicialPercapitaService distribucionInicialPercapitaService;

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
		add(DocumentService.class.getName(), this.documentService);
		add(DistribucionInicialPercapitaService.class.getName(), this.distribucionInicialPercapitaService);
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
