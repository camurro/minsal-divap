package cl.redhat.bandejaTareas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;

import org.apache.log4j.Logger;



@SuppressWarnings({"serial", "cdi-ambiguous-dependency"})
@Dependent
@Startup
public class BandejaProperties extends Properties {

	public BandejaProperties()
	{
	
	}
	public static final String DEFAULT_FILE_NAME = "bandeja.properties";

	private static final String SERVER_CONF_URL = System
			.getProperty("jboss.server.config.dir");

	private static Logger log = Logger.getLogger(BandejaProperties.class);

	@PostConstruct
	protected void init() throws Exception {
		
		log.info("PROPIEDADES DEL SISTEMA CARGADAS EN BANDEJA");
//		log.info(System.getProperties());

		try {
			File externalFile = getExternalConfig();
			InputStream elInput=null;
			if (externalFile==null) {
				log.warn("No se encontr— archivo de configuraci—n "+DEFAULT_FILE_NAME+ " en "+SERVER_CONF_URL+". Se usar‡ el default");
				elInput  = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_FILE_NAME);
			} else {
				log.info("Usando archivo de configuraci—n de Bandeja "+externalFile.getAbsolutePath());
				elInput = new FileInputStream(externalFile);
			}
			
			this.load(elInput);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("No puedo cargar el arhivo properties "
					+ DEFAULT_FILE_NAME, e);
		}
	}

	private File getExternalConfig() {
		if (SERVER_CONF_URL == null)
			return null;
		File ret = new File(SERVER_CONF_URL, DEFAULT_FILE_NAME);

		if (!ret.exists())
			return null;

		return ret;

	}

	public String getHostBRMS() {
		return this.getProperty("brms.host");
	}

	public String getPortBRMS() {
		return this.getProperty("brms.port");
	}

	public String geitOirsUrl() {
		return this.getProperty("oirs.url");
	}
	
	public String getOirsDefaultmail() {
		return this.getProperty("oirs.defaultmail");
	}
}