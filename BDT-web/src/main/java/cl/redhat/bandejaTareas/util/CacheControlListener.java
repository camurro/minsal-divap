package cl.redhat.bandejaTareas.util;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jorge Morando
 *
 */
public class CacheControlListener implements PhaseListener
{
	private static final long serialVersionUID = -8848042363049753234L;

	public PhaseId getPhaseId()
    {
        return PhaseId.RENDER_RESPONSE;
    }

    public void afterPhase(PhaseEvent event)        
    {
    }

    public void beforePhase(PhaseEvent event)
    {
       FacesContext facesContext = event.getFacesContext();
       HttpServletResponse response = (HttpServletResponse) facesContext
                .getExternalContext().getResponse();
       response.addHeader("Pragma", "no-cache");
       response.addHeader("Cache-Control", "no-cache");
       // Stronger according to blog comment below that references HTTP spec
       response.addHeader("Cache-Control", "no-store");
       response.addHeader("Cache-Control", "must-revalidate");
       // some date in the past
       response.addHeader("Expires", "Fri, 11 Jul 1983 13:50:00 GMT");
    }
} 