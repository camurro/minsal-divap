package minsal.divap.service.util;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class RestClientSimple {

	private final String username;
	private final String password;
	private final String baseUrl;
	private final String deploymentId;
	private final String processId;

	public RestClientSimple(final String baseUrl, final String deploymentId, final String processId, final String u, final String p) {
		this.username = u;
		this.password = p;
		this.baseUrl = baseUrl;
		this.deploymentId = deploymentId;
		this.processId = processId;
	}

	public RestClientSimple(final String u, final String p){
		this.baseUrl = null;
		this.deploymentId = null;
		this.processId = null;
		this.username = u;
		this.password = p;
	}

	public void startProcessWithParameters(RestClientSimple client, Map<String, Object> params) throws Exception {
		String newInstanceUrl = baseUrl + "runtime/" + deploymentId 	+ "/execute";
		String data = RestJBPM.getStartProcess(deploymentId, processId, params);
		System.out.println("data"+data);
		String dataFromService = client.getDataFromService(newInstanceUrl, "POST", data, false);
		System.out.println("--------");
		System.out.println(dataFromService);
		System.out.println("--------");
	}

	private String getDataFromService(String urlpath, String method,
			String data, boolean b) throws Exception  {
		HttpClient client = new DefaultHttpClient();
		
		((DefaultHttpClient)client).getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(username, password)
        );
		HttpContext localContext = new BasicHttpContext();

        // Generate BASIC scheme object and stick it to the local execution context
        BasicScheme basicAuth = new BasicScheme();
        localContext.setAttribute("preemptive-auth", basicAuth);

        // Add as the first request interceptor
        ((DefaultHttpClient)client).addRequestInterceptor(new PreemptiveAuth(), 0);
	
		HttpUriRequest request = null;
		if ("GET".equalsIgnoreCase(method)) {
			request = new HttpGet(urlpath);
		} else if ("POST".equalsIgnoreCase(method)) {
			request = new HttpPost(urlpath);
			

			InetAddress inetAddr = InetAddress.getLocalHost();
			String hostname = inetAddr.getHostName();
			request.setHeader("User-Agent", "org.kie.services.client (1 "+ "/" + hostname + ")");
			((HttpPost)request).setEntity(new StringEntity(data ,"application/xml", "UTF-8"));
		}
		try {
			//HttpResponse response = client.execute(request);
			 HttpResponse response = client.execute(request, localContext);
			final int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new Exception("Respuesta inesperada desde servidor " + response.getStatusLine()
						+ " para " + request.getRequestLine());
			}
			String rawResponse = EntityUtils.toString(response.getEntity());
			System.out.println("Call " + request.getURI()+" :: result = " + rawResponse);
			return rawResponse;
		} catch (Exception e) {
			throw e;
		} 
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getBaseUrl() {
		return baseUrl;
	}
	
	 static class PreemptiveAuth implements HttpRequestInterceptor {

	        public void process(
	                final HttpRequest request, 
	                final HttpContext context) throws HttpException, IOException {
	            
	            AuthState authState = (AuthState) context.getAttribute(
	                    ClientContext.TARGET_AUTH_STATE);
	            
	            // If no auth scheme avaialble yet, try to initialize it preemptively
	            if (authState.getAuthScheme() == null) {
	                AuthScheme authScheme = (AuthScheme) context.getAttribute(
	                        "preemptive-auth");
	                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(
	                        ClientContext.CREDS_PROVIDER);
	                HttpHost targetHost = (HttpHost) context.getAttribute(
	                        ExecutionContext.HTTP_TARGET_HOST);
	                if (authScheme != null) {
	                    Credentials creds = credsProvider.getCredentials(
	                            new AuthScope(
	                                    targetHost.getHostName(), 
	                                    targetHost.getPort()));
	                    if (creds == null) {
	                        throw new HttpException("No credentials for preemptive authentication");
	                    }
	                    authState.setAuthScheme(authScheme);
	                    authState.setCredentials(creds);
	                }
	            }
	            
	        }
	        
	    }

}
