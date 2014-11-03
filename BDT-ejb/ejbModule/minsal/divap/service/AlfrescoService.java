package minsal.divap.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.ejb.Stateless;

import minsal.divap.vo.BodyCreateFolderVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReferenciaDocumentoVO;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.auth.NTLMScheme;
import org.apache.commons.httpclient.auth.RFC2617Scheme;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.codehaus.jackson.map.ObjectMapper;

@Stateless
public class AlfrescoService {
	@Resource(name = "alfrescoServer")
	private String alfrescoServer;

	@Resource(name = "alfrescoUsername")
	private String alfrescoUsername;

	@Resource(name = "alfrescoPassword")
	private String alfrescoPassword;

	@Resource(name = "alfrescoSite")
	private String alfrescoSite;

	@Resource(name = "alfrescoUploadDirectory")
	private String alfrescoUploadDirectory;

	@Resource(name = "tmpDownloadDirectory")
	private String tmpDownloadDirectory;

	class ConsoleAuthPrompter implements CredentialsProvider {

		private BufferedReader in = null;

		public ConsoleAuthPrompter() {
			super();
			this.in = new BufferedReader(new InputStreamReader(System.in));
		}

		private String readConsole() throws IOException {
			return this.in.readLine();
		}

		public Credentials getCredentials(final AuthScheme authscheme,
				final String host, int port, boolean proxy)
						throws CredentialsNotAvailableException {
			if (authscheme == null) {
				return null;
			}
			try {
				if (authscheme instanceof NTLMScheme) {
					System.out.println(host + ":" + port
							+ " requires Windows authentication");
					System.out.print("Enter domain: ");
					String domain = readConsole();
					System.out.print("Enter username: ");
					String user = readConsole();
					System.out.print("Enter password: ");
					String password = readConsole();
					return new NTCredentials(user, password, host, domain);
				} else if (authscheme instanceof RFC2617Scheme) {
					System.out.println(host + ":" + port
							+ " requires authentication with the realm '"
							+ authscheme.getRealm() + "'");
					System.out.print("Enter username: ");
					String user = readConsole();
					System.out.print("Enter password: ");
					String password = readConsole();
					return new UsernamePasswordCredentials(user, password);
				} else {
					throw new CredentialsNotAvailableException(
							"Unsupported authentication scheme: "
									+ authscheme.getSchemeName());
				}
			} catch (IOException e) {
				throw new CredentialsNotAvailableException(e.getMessage(), e);
			}
		}
	}

	private String getTicket() throws IOException {
		String alfrescoTiccketURL = alfrescoServer + "/login?u="
				+ alfrescoUsername + "&pw=" + alfrescoPassword;

		HttpClient client = new HttpClient();
		client.getParams().setParameter(CredentialsProvider.PROVIDER,
				new ConsoleAuthPrompter());
		GetMethod httpget = new GetMethod(alfrescoTiccketURL);
		httpget.setDoAuthentication(true);
		String response = null;
		String ticketURLResponse = null;

		try {
			// execute the GET
			int status = client.executeMethod(httpget);

			// aritz
			if (status != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + httpget.getStatusLine());
			}

			// print the status and response
			// System.out.println(httpget.getStatusLine().toString());
			// System.out.println(httpget.getResponseBodyAsString());

			response = new String(httpget.getResponseBodyAsString());
			System.out.println("response = " + response);

			String regex = "<ticket[^>]*>(.+?)</ticket\\s*>";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(response);
			while (matcher.find()) {
				ticketURLResponse = matcher.group(1);
			}
			System.out.println("ticket = " + ticketURLResponse);

		} finally {
			httpget.releaseConnection();
		}
		return ticketURLResponse;
	}

	public BodyVO uploadDocument(File file, String contentType, String folder) {
		BodyVO body = null;
		try {
			System.out.println("alfrescoServer-->"+ alfrescoServer);
			String authTicket = getTicket();
			System.out.println("authTicket-->"+authTicket);
			String urlString = alfrescoServer + "/upload?alf_ticket="
					+ authTicket;
			System.out.println("The upload url::" + urlString);
			HttpClient client = new HttpClient();

			String filename = file.getName();
			String description = "Archivo subido a alfresco";
			String uploaddirectory = ((folder == null) ? alfrescoUploadDirectory
					: (alfrescoUploadDirectory + File.separator + folder));
			System.out.println("uploaddirectory-->" + uploaddirectory);

			PostMethod mPost = new PostMethod(urlString);
			Part[] parts = {
					new FilePart("filedata", filename, file, contentType, null),
					new StringPart("filename", filename),
					new StringPart("description", description),
					// new StringPart("destination", destination),

					// modify this according to where you wanna put your content
					new StringPart("siteid", alfrescoSite),
					new StringPart("containerid", "documentLibrary"),
					new StringPart("uploaddirectory", uploaddirectory) };
			mPost.setRequestEntity(new MultipartRequestEntity(parts, mPost
					.getParams()));
			int statusCode = client.executeMethod(mPost);
			System.out.println("statusLine>>>" + statusCode + "......"
					+ "\n status line \n" + mPost.getStatusLine() + "\nbody \n"
					+ mPost.getResponseBodyAsString());
			if (statusCode == 200) {
				ObjectMapper mapper = new ObjectMapper();
				body = mapper.readValue(mPost.getResponseBodyAsString(),
						BodyVO.class);
			}
			System.out.println("response body->" + body);
			mPost.releaseConnection();

		} catch (Exception e) {
			System.out.println(e);
		}
		return body;
	}

	public void delete(String docAlfresco) {
		try {
			String authTicket = getTicket();
			String urlString = alfrescoServer
					+ "/archive/workspace/SpacesStore/" + docAlfresco
					+ "?alf_ticket=" + authTicket;
			System.out.println("borrar archivo url:::" + urlString);
			// Create the POST object and add the parameters
			HttpMethod deleteMethod = new DeleteMethod(urlString);
			HttpClient client = new HttpClient();
			int statusCode = client.executeMethod(deleteMethod);
			System.out.println("statusLine>>>" + statusCode + "......"
					+ "\n status line \n" + deleteMethod.getStatusLine()
					+ "\nbody \n" + deleteMethod.getResponseBodyAsString());
			if (statusCode == 200) {
				/*
				 * ObjectMapper mapper = new ObjectMapper(); body =
				 * mapper.readValue(mPost.getResponseBodyAsString(),
				 * BodyVO.class);
				 */
			}
			// System.out.println("response body->"+body);
			deleteMethod.releaseConnection();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public DocumentoVO download(String docAlfresco) {
		DocumentoVO documentoVO = null;
		try {
			String authTicket = getTicket();
			String urlString = alfrescoServer
					+ "/node/content/workspace/SpacesStore/" + docAlfresco
					+ "?alf_ticket=" + authTicket;
			System.out.println("download archivo url:::" + urlString);
			HttpMethod downloadMethod = new GetMethod(urlString);
			HttpClient client = new HttpClient();
			int statusCode = client.executeMethod(downloadMethod);
			System.out.println("statusCode>>>" + statusCode + "......");
			if (statusCode == 200) {
				byte[] content = readInputStream(downloadMethod
						.getResponseBodyAsStream());
				documentoVO = new DocumentoVO("documentoDescargado",
						"contentType", content);
			}
			downloadMethod.releaseConnection();
			System.out.println("file download success");
		} catch (Exception e) {
			System.out.println(e);
		}
		return documentoVO;
	}

	private byte[] readInputStream(InputStream input) throws IOException {
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		return output.toByteArray();
	}

	public BodyCreateFolderVO createFolder(String folder) {
		BodyCreateFolderVO body = null;
		try {
			String authTicket = getTicket();
			String urlString = alfrescoServer + "/site/folder/" + alfrescoSite + "/documentLibrary/" + alfrescoUploadDirectory + "/PROCESOS" + "?alf_ticket="
					+ authTicket;
			System.out.println("The upload url::" + urlString);
			HttpClient client = new HttpClient();
			System.out.println("folder-->"+folder);
			PostMethod mPost = new PostMethod(urlString);
			String jSon = "{\"name\": \""+ folder +"\"}";

			System.out.println("jSon-->"+jSon);

			mPost.setRequestEntity(new StringRequestEntity(jSon, "application/json", "UTF-8"));
			//mPost.setRequestEntity(requestEntity);
			int statusCode = client.executeMethod(mPost);
			System.out.println("statusLine>>>" + statusCode + "......"
					+ "\n status line \n"
					+mPost.getStatusLine() + "\nbody \n" +mPost.getResponseBodyAsString());
			if(statusCode == 200){
				ObjectMapper mapper = new ObjectMapper();
				body = mapper.readValue(mPost.getResponseBodyAsString(), BodyCreateFolderVO.class);
				System.out.println("response body->"+body);
			}else if(statusCode == 500){
				System.out.println("Error al crear carpeta " + folder);
				System.out.println(mPost.getResponseBodyAsString());
			}

			mPost.releaseConnection();

		} catch (Exception e) {
			System.out.println(e);
		}
		return body;
	}

}
