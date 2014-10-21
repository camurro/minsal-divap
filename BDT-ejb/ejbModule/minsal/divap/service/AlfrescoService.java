package minsal.divap.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;

import minsal.divap.vo.BodyVO;
import minsal.divap.vo.DocumentoVO;

import org.alfresco.model.ApplicationModel;
import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.TempFileProvider;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
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
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.commons.io.IOUtils;

import com.ibm.icu.text.Normalizer;

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

	private NodeService nodeService;

	private NamespaceService namespaceService;

	private DictionaryService dictionaryService;

	private ContentService contentService;
	
	private StoreRef storeRef;
	
	private String encoding;

	private static final int BUFFER_SIZE = 1024;
	private static final String MIMETYPE_ZIP = "application/zip";
	private static final String TEMP_FILE_PREFIX = "alf";
	private static final String ZIP_EXTENSION = ".zip";
	
	
	public NodeService getNodeService() {
		return nodeService;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public NamespaceService getNamespaceService() {
		return namespaceService;
	}

	public void setNamespaceService(NamespaceService namespaceService) {
		this.namespaceService = namespaceService;
	}

	public DictionaryService getDictionaryService() {
		return dictionaryService;
	}

	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	public ContentService getContentService() {
		return contentService;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	public StoreRef getStoreRef() {
		return storeRef;
	}

	public void setStoreUrl(String url) {
		this.storeRef = new StoreRef(url);
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	



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
			String authTicket = getTicket();
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
			// Create the POST object and add the parameters
			HttpMethod downloadMethod = new GetMethod(urlString);
			HttpClient client = new HttpClient();
			int statusCode = client.executeMethod(downloadMethod);
			System.out.println("statusCode>>>" + statusCode + "......");
			// + "\n status line \n"
			// +downloadMethod.getStatusLine() + "\nbody \n"
			// +downloadMethod.getResponseBodyAsString());
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

	public void addToZip(NodeRef node, ZipArchiveOutputStream out,
			boolean noaccent, String path) throws IOException {
		System.out.println("Entra al addtoZip");
		QName nodeQnameType = this.nodeService.getType(node);
		
		
		// Special case : links
		if (this.dictionaryService.isSubClass(nodeQnameType,
				ApplicationModel.TYPE_FILELINK)) {
			NodeRef linkDestinationNode = (NodeRef) nodeService.getProperty(
					node, ContentModel.PROP_LINK_DESTINATION);
			if (linkDestinationNode == null) {
				return;
			}
			// Duplicate entry: check if link is not in the same space of the
			// link destination
			if (nodeService
					.getPrimaryParent(node)
					.getParentRef()
					.equals(nodeService.getPrimaryParent(linkDestinationNode)
							.getParentRef())) {
				return;
			}
			nodeQnameType = this.nodeService.getType(linkDestinationNode);
			node = linkDestinationNode;
		}
		String nodeName = (String) nodeService.getProperty(node,
				ContentModel.PROP_NAME);
		nodeName = noaccent ? unAccent(nodeName) : nodeName;
		if (this.dictionaryService.isSubClass(nodeQnameType,
				ContentModel.TYPE_CONTENT)) {
			ContentReader reader = contentService.getReader(node,
					ContentModel.PROP_CONTENT);
			if (reader != null) {
				InputStream is = null;
				try {
					is = reader.getContentInputStream();
				} catch (ContentIOException e) {
					e.printStackTrace();
				}
				String filename = path.isEmpty() ? nodeName : path + '/'
						+ nodeName;
				ZipArchiveEntry entry = new ZipArchiveEntry(filename);
				entry.setTime(((Date) nodeService.getProperty(node,
						ContentModel.PROP_MODIFIED)).getTime());
				entry.setSize(reader.getSize());
				out.putArchiveEntry(entry);
				byte buffer[] = new byte[BUFFER_SIZE];
				while (true) {
					int nRead = is.read(buffer, 0, buffer.length);
					if (nRead <= 0) {
						break;
					}
					out.write(buffer, 0, nRead);
				}
				is.close();
				out.closeArchiveEntry();
			} else {
				System.out.println("Could not read : " + nodeName + "content");
			}
		} else if (this.dictionaryService.isSubClass(nodeQnameType,
				ContentModel.TYPE_FOLDER)
				&& !this.dictionaryService.isSubClass(nodeQnameType,
						ContentModel.TYPE_SYSTEM_FOLDER)) {
			List<ChildAssociationRef> children = nodeService
					.getChildAssocs(node);
			if (children.isEmpty()) {
				String folderPath = path.isEmpty() ? nodeName + '/' : path
						+ '/' + nodeName + '/';
				out.putArchiveEntry(new ZipArchiveEntry(
						new ZipEntry(folderPath)));
			} else {
				for (ChildAssociationRef childAssoc : children) {
					NodeRef childNodeRef = childAssoc.getChildRef();
					addToZip(childNodeRef, out, noaccent,
							path.isEmpty() ? nodeName : path + '/' + nodeName);
				}
			}
		} else {
			System.out.println("Unmanaged type: "
					+ nodeQnameType.getPrefixedQName(this.namespaceService)
					+ ", filename: " + nodeName);
		}
	}

	public void createZipFile(List<String> nodeIds, List<NodeRef> nr, OutputStream os,
			boolean noaccent) throws IOException {
		
		
		
		System.out.println("entra al createZip");
		File zip = null;
		StoreRef sref = nr.get(0).getStoreRef();
		//this.nodeService.getType(nr.get(0));
		
		System.out.println("recorriendo la lista nodeIds");
		for(int i=0; i<nodeIds.size();i++){
			System.out.println("ids de los nodos --> "+nodeIds.get(i));
		}
		
		try {
			if (nodeIds != null && !nodeIds.isEmpty()) {
				zip = TempFileProvider.createTempFile(TEMP_FILE_PREFIX,
						ZIP_EXTENSION);
				
				System.out.println("creando el zip");
				FileOutputStream stream = new FileOutputStream(zip);
				CheckedOutputStream checksum = new CheckedOutputStream(stream,
						new Adler32());
				BufferedOutputStream buff = new BufferedOutputStream(checksum);
				ZipArchiveOutputStream out = new ZipArchiveOutputStream(buff);
				out.setEncoding(encoding);
				out.setMethod(ZipArchiveOutputStream.DEFLATED);
				out.setLevel(Deflater.BEST_COMPRESSION);
				
				try {
					for (String nodeId : nodeIds) {
						NodeRef node = new NodeRef(sref, nodeId);
						addToZip(node, out, noaccent, "");
					}
				} catch (Exception e) {
					System.out.println("aqui se cae");
					System.out.println(e.getMessage());
				} finally {
					out.close();
					buff.close();
					checksum.close();
					stream.close();
					if (nodeIds.size() > 0) {
						InputStream in = new FileInputStream(zip);
						try {
							byte[] buffer = new byte[BUFFER_SIZE];
							int len;
							while ((len = in.read(buffer)) > 0) {
								os.write(buffer, 0, len);
							}
						} finally {
							IOUtils.closeQuietly(in);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Se va al catch");
			
			if (zip != null) {
				zip.delete();
			}
		}
	}

	public static String unAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.NFD, 0);
		return temp.replaceAll("[^\\p{ASCII}]", "");
	}

}