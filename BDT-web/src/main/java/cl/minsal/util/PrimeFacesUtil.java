package cl.minsal.util;

import org.primefaces.context.RequestContext;

public class PrimeFacesUtil {
	
	public static void ejecutarJavaScript(String comando){
		RequestContext.getCurrentInstance().execute(comando);
	}

}
