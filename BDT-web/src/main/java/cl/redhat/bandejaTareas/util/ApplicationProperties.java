package cl.redhat.bandejaTareas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;

import org.apache.log4j.Logger;



@SuppressWarnings("serial")
@ApplicationScoped
@Startup
public class ApplicationProperties extends Properties {

	public static final String DEFAULT_FILE_NAME = "app.properties";

	private static final String SERVER_CONF_URL = System.getProperty("jboss.server.config.dir");

	private static Logger log = Logger.getLogger(ApplicationProperties.class);

	@PostConstruct
	protected void init() throws Exception {
		
		log.info("PROPIEDADES DEL SISTEMA CARGADAS EN BANDEJA");

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

	public String getVersion() {
		return this.getProperty("bandeja.version");
	}
}