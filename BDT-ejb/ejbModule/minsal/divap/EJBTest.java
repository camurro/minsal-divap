package minsal.divap;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;


/**
 * Session Bean implementation class EJBTest
 */
@LocalBean
@Stateless(mappedName = "ejbtest")
public class EJBTest {

	/**
	 * Default constructor.
	 */
	public EJBTest() {
		// TODO Auto-generated constructor stub
	}

	public void testMethod(){
    System.out.println("*********** Iniciando desde EJBTest **********");
    
    //CLiente Simple al HelloREST
    ClientRequest request = new ClientRequest(
			"http://localhost:8080/BDT-Rest/HelloREST/");
	request.accept("text/plain");
	ClientResponse<String> response;
	try {
		response = request.get(String.class);
		System.out.println(response.getEntity());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    }
}
