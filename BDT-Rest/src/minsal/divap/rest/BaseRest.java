package minsal.divap.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

public class BaseRest {
	@Context
	private ServletContext context;
	@SuppressWarnings("unchecked")
	protected <T> T getService(Class<T> type){
		System.out.println("type.getName()="+type.getName());
		return (T) context.getAttribute(type.getName());
	}
}
