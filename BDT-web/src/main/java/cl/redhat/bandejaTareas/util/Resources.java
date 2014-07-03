package cl.redhat.bandejaTareas.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence
 * context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
public class Resources {

	@Inject
	ApplicationProperties appProps;
	
	@Produces
	public Logger produceLog(InjectionPoint injectionPoint) {

		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass()
				.getName());
	}
	
	@Produces
	@Named("bandejaVersion")
	public String produceBandejaVersion(InjectionPoint injectionPoint) {
		return appProps.getVersion();
	}

}
