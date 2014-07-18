package minsal.divap.service.util;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RestJBPM {

	private static final Map<Class<?>, String> WRAPPER_TYPES = getWrapperTypes();
	private static Map<Class<?>, String> getWrapperTypes()
	{
		Map<Class<?>, String> ret = new HashMap<Class<?>, String>();
		ret.put(Boolean.class, "xs:boolean");
		ret.put(Character.class, "xs:unsignedShort");
		ret.put(Byte.class, "xs:byte");
		ret.put(Short.class, "xs:short");
		ret.put(Integer.class, "xs:int");
		ret.put(Long.class, "xs:long");
		ret.put(Float.class, "xs:float");
		ret.put(Double.class, "xs:double");
		ret.put(String.class, "xs:string");
		ret.put(Void.class, "xs:void");
		return ret;
	}

	public static boolean isWrapperType(Class<?> clazz){
		return WRAPPER_TYPES.containsKey(clazz);
	}

	public static String getWrapperType(Class<?> clazz){
		return WRAPPER_TYPES.get(clazz);
	}

	private static Element getCommandRequest(String deploymentId, String processId) throws ParserConfigurationException{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware( true );
		Document document = documentBuilderFactory.newDocumentBuilder().newDocument();
		Element commandRequest = document.createElement( "command-request" );
		Element deploymentIdNode = document.createElement( "deployment-id" );
		deploymentIdNode.setTextContent( deploymentId );
		commandRequest.appendChild( deploymentIdNode );
		Element ver = document.createElement( "ver" );
		ver.setTextContent( "1" );
		commandRequest.appendChild( ver );
		return commandRequest;
	}

	private static void addParameters(Element command, Map<String, Object>params ){
		Document document = command.getOwnerDocument();
		Element parameterElement = document.createElement( "parameter" );
		command.appendChild( parameterElement );
		for( Entry<String, Object> entry : params.entrySet() ){
			String beanType = null;
			Object beanObject = entry.getValue();
			if(isWrapperType(beanObject.getClass())){
				beanType = getWrapperType(beanObject.getClass());
			}else{
				beanType = beanObject.getClass().getSimpleName();
			}
			Element item = document.createElement( "item" );
			item.setAttribute( "key", entry.getKey() );
			parameterElement.appendChild( item );
			Element value = document.createElement( "value" );
			value.setAttributeNS( "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", beanType );
			value.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
			item.appendChild( value );
			addBeanProperties(command, value, beanObject );
		}
	}

	private static void addBeanProperties(Element command, Element value, Object beanObject) {
		if(isWrapperType(beanObject.getClass())){
			value.setTextContent( beanObject.toString() );
		}else if (beanObject instanceof Collection<?>){
		}else if (beanObject instanceof Map<?,?>){
		}else{
			Field[] fields = beanObject.getClass().getDeclaredFields();
			for(Field f : fields){
				try {
					Document document = command.getOwnerDocument();
					Element newChild = document.createElement( f.getName());
					Object fieldValue = f.get(beanObject);
					if(fieldValue != null){
						newChild.setTextContent(fieldValue.toString());
						value.appendChild(newChild);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void addParametersCommand(Element command, Map<String, Object>params ){
		Document document = command.getOwnerDocument();
		Element parameterElement = document.createElement( "data" );
		command.appendChild( parameterElement );
		for( Entry<String, Object> entry : params.entrySet() ){
			String beanType = null;
			Object beanObject = entry.getValue();
			if(isWrapperType(beanObject.getClass())){
				beanType = getWrapperType(beanObject.getClass());
			}else{
				beanType = beanObject.getClass().getSimpleName();
			}
			Element item = document.createElement( "item" );
			item.setAttribute( "key", entry.getKey() );
			parameterElement.appendChild( item );
			Element value = document.createElement( "value" );
			value.setAttribute("xsi:type", beanType);
			value.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
			item.appendChild( value );
			addBeanProperties(command, value, beanObject );
		}
	}

	public static String getStartProcess( String deploymentId, String processId, Map<String, Object> vars ) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException{
		Element commandRequest = getCommandRequest( deploymentId, processId);
		Element startProcess = commandRequest.getOwnerDocument().createElement( "start-process" );
		startProcess.setAttribute( "processId", processId );
		commandRequest.appendChild( startProcess );
		if(vars != null && vars.size()>0){
			addParameters( startProcess, vars );
		}
		StringWriter xmlWriter = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty("standalone", "yes");
		transformer.transform( new DOMSource( commandRequest ), new StreamResult( xmlWriter ) );
		String jaxbRequestString = xmlWriter.toString();
		return jaxbRequestString;
	}

	public static String getTaskListForPotentialOwner(String deploymentId, String actorId) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		Element commandRequest = getCommandRequest( deploymentId, null);
		Element elementTaskPotentialOwner = commandRequest.getOwnerDocument().createElement( "get-task-as-potential-owner" );
		Element elementUserId = commandRequest.getOwnerDocument().createElement( "user-id" );
		elementUserId.setTextContent(actorId);
		elementTaskPotentialOwner.appendChild(elementUserId);
		Element elementLang = commandRequest.getOwnerDocument().createElement( "language" );
		elementLang.setTextContent("en-UK");
		//elementLang.setTextContent("es-CL");
		elementTaskPotentialOwner.appendChild(elementLang);
		commandRequest.appendChild( elementTaskPotentialOwner );
		return xmlToString(commandRequest);
	}
	private static String xmlToString(Element commandRequest) throws TransformerFactoryConfigurationError, TransformerException{
		StringWriter xmlWriter = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty("standalone", "yes");
		transformer.transform( new DOMSource( commandRequest ), new StreamResult( xmlWriter ) );
		String jaxbRequestString = xmlWriter.toString();
		return jaxbRequestString;
	}

	public static String getClaimTask(String deploymentId, long taskId,
			String actorId) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		Element commandRequest = getCommandRequest( deploymentId, null);
		Element elementClaimTask = commandRequest.getOwnerDocument().createElement( "claim-task" );
		commandRequest.appendChild( elementClaimTask );
		Element elementTaskId = commandRequest.getOwnerDocument().createElement( "task-id" );
		elementTaskId.setTextContent(String.valueOf(taskId));
		elementClaimTask.appendChild(elementTaskId);
		Element elementUserId = commandRequest.getOwnerDocument().createElement( "user-id" );
		elementUserId.setTextContent(actorId);
		elementClaimTask.appendChild(elementUserId);
		return xmlToString(commandRequest);
	}

	public static String getTasksByStatusByProcessInstanceId(
			String deploymentId, String actorId, long processInstanceId)  throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException{
		Element commandRequest = getCommandRequest( deploymentId, null);
		Element elementTaskByStatusProcessInstance = commandRequest.getOwnerDocument().createElement( "get-tasks-by-status-by-processinstanceid" );
		Element elementProcessInstanceId = commandRequest.getOwnerDocument().createElement( "process-instance-id" );
		elementProcessInstanceId.setTextContent(String.valueOf(processInstanceId));
		elementTaskByStatusProcessInstance.appendChild(elementProcessInstanceId);
		Element elementLang = commandRequest.getOwnerDocument().createElement( "language" );
		elementLang.setTextContent("en-UK");
		//elementLang.setTextContent("es-CL");
		elementTaskByStatusProcessInstance.appendChild(elementLang);
		Element elementStatus = commandRequest.getOwnerDocument().createElement( "status" );
		elementStatus.setTextContent("Reserved"); 
		elementTaskByStatusProcessInstance.appendChild(elementStatus);
		commandRequest.appendChild( elementTaskByStatusProcessInstance );
		return xmlToString(commandRequest);
	}
	
	public static String getStartTask(String deploymentId, long taskId,
			String actorId) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		Element commandRequest = getCommandRequest( deploymentId, null);
		Element elementCompositeCommand = commandRequest.getOwnerDocument().createElement( "task-composite-command" );
		Element elementMainCommand = commandRequest.getOwnerDocument().createElement( "mainCommand" );
		elementMainCommand.setAttribute("xsi:type", "startTaskCommand");
		elementMainCommand.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		Element elementTaskId = commandRequest.getOwnerDocument().createElement( "task-id");
		elementTaskId.setTextContent(String.valueOf(taskId));
		elementMainCommand.appendChild(elementTaskId);
		Element elementUserId = commandRequest.getOwnerDocument().createElement( "user-id");
		elementUserId.setTextContent(actorId);
		elementMainCommand.appendChild(elementUserId);
		elementCompositeCommand.appendChild(elementMainCommand);

		Element elementCommands = commandRequest.getOwnerDocument().createElement( "commands" );
		elementCommands.setAttribute("xsi:type", "cancelDeadlineCommand");
		elementCommands.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		Element elementTaskIdCommand = commandRequest.getOwnerDocument().createElement( "task-id");
		elementTaskIdCommand.setTextContent(String.valueOf(taskId));
		elementCommands.appendChild(elementTaskIdCommand);
		Element elementRemoveStartCommand = commandRequest.getOwnerDocument().createElement( "removeStart");
		elementRemoveStartCommand.setTextContent("true");
		elementCommands.appendChild(elementRemoveStartCommand);
		Element elementRemoveEndCommand = commandRequest.getOwnerDocument().createElement( "removeEnd");
		elementRemoveEndCommand.setTextContent("false");
		elementCommands.appendChild(elementRemoveEndCommand);
		elementCompositeCommand.appendChild(elementCommands);

		commandRequest.appendChild( elementCompositeCommand );

		return xmlToString(commandRequest);
	}

	public static String getTasksByStatusByProcessInstanceId(String deploymentId, long processInstanceId, List<String> status)
			throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		Element commandRequest = getCommandRequest( deploymentId, null);
		Element elementTasksByStatusbyProcessinstanceid = commandRequest.getOwnerDocument().createElement( "get-tasks-by-status-by-processinstanceid" );
		commandRequest.appendChild( elementTasksByStatusbyProcessinstanceid );
		Element elementProcessInstanceiId = commandRequest.getOwnerDocument().createElement( "process-instance-id" );
		elementProcessInstanceiId.setTextContent(String.valueOf(processInstanceId));
		elementTasksByStatusbyProcessinstanceid.appendChild(elementProcessInstanceiId);
		Element elementLanguage = commandRequest.getOwnerDocument().createElement( "language" );
		elementLanguage.setTextContent("en-UK");
		//elementLanguage.setTextContent("es-CL");
		elementTasksByStatusbyProcessinstanceid.appendChild(elementLanguage);
		if(status != null && status.size() > 0){
			for(String estado : status){
				Element elementStatus = commandRequest.getOwnerDocument().createElement( "status" );
				elementStatus.setTextContent(estado);
				elementTasksByStatusbyProcessinstanceid.appendChild(elementStatus);
			}
		}
		return xmlToString(commandRequest);
	}

	public static String getCompleteTask(String deploymentId, long taskId,
			String actorId, Map<String, Object> processVariables) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		Element commandRequest = getCommandRequest( deploymentId, null);
		Element elemenTaskComposite= commandRequest.getOwnerDocument().createElement( "task-composite-command" );
		commandRequest.appendChild( elemenTaskComposite );
		Element elementMainCommand = commandRequest.getOwnerDocument().createElement( "mainCommand" );
		elementMainCommand.setAttribute("xsi:type", "completeTaskCommand");
		elementMainCommand.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		Element elementTaskIdMainCommand = commandRequest.getOwnerDocument().createElement( "task-id" );
		elementTaskIdMainCommand.setTextContent(String.valueOf(taskId));
		elementMainCommand.appendChild(elementTaskIdMainCommand);
		Element elementUserIdMainCommand = commandRequest.getOwnerDocument().createElement( "user-id" );
		elementUserIdMainCommand.setTextContent(actorId);
		elementMainCommand.appendChild(elementUserIdMainCommand);
		elemenTaskComposite.appendChild(elementMainCommand);
		addParametersCommand( elementMainCommand, processVariables );
		Element elementCommandOne = commandRequest.getOwnerDocument().createElement( "commands" );
		elementCommandOne.setAttribute("xsi:type", "executeTaskRulesCommand");
		elementCommandOne.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		elemenTaskComposite.appendChild(elementCommandOne);
		//
		Element elementTaskIdCommandOne = commandRequest.getOwnerDocument().createElement( "task-id" );
		elementTaskIdCommandOne.setTextContent(String.valueOf(taskId));
		elementCommandOne.appendChild(elementTaskIdCommandOne);
		Element elementUserIdCommandOne = commandRequest.getOwnerDocument().createElement( "user-id" );
		elementUserIdCommandOne.setTextContent(actorId);
		elementCommandOne.appendChild(elementUserIdCommandOne);
		//
		addParametersCommand( elementCommandOne, processVariables );
		Element elementCommandTwo = commandRequest.getOwnerDocument().createElement( "commands" );
		elementCommandTwo.setAttribute("xsi:type", "processSubTaskCommand");
		elementCommandTwo.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		elemenTaskComposite.appendChild(elementCommandTwo);
		//
		Element elementTaskIdCommandTwo = commandRequest.getOwnerDocument().createElement( "task-id" );
		elementTaskIdCommandTwo.setTextContent(String.valueOf(taskId));
		elementCommandTwo.appendChild(elementTaskIdCommandTwo);
		Element elementUserIdCommandTwo = commandRequest.getOwnerDocument().createElement( "user-id" );
		elementUserIdCommandTwo.setTextContent(actorId);
		elementCommandTwo.appendChild(elementUserIdCommandTwo);
		//
		addParametersCommand( elementCommandTwo, processVariables );
		Element elementCommandThree = commandRequest.getOwnerDocument().createElement( "commands" );
		elementCommandThree.setAttribute("xsi:type", "cancelDeadlineCommand");
		elementCommandThree.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		elemenTaskComposite.appendChild(elementCommandThree);
		//
		Element elementTaskIdCommandThree = commandRequest.getOwnerDocument().createElement( "task-id" );
		elementTaskIdCommandThree.setTextContent(String.valueOf(taskId));
		elementCommandThree.appendChild(elementTaskIdCommandThree);
		Element elementUserIdCommandThree = commandRequest.getOwnerDocument().createElement( "user-id" );
		elementUserIdCommandThree.setTextContent(actorId);
		elementCommandThree.appendChild(elementUserIdCommandThree);
		//
		addParametersCommand( elementCommandThree, processVariables );
		return xmlToString(commandRequest);
	}

	public static void main(String [] args){
		String deploymentId = "cl.minsal.divap:minsal_divap:1.0";
		String processId = "minsal_divap.percapita";
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("uno","1");
		processVariables.put("dos","2");
		processVariables.put("tres", new Integer(3));

		try {
			System.out.println("serializando el comando");
			System.out.println(RestJBPM.getStartProcess(deploymentId, processId, processVariables));
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}


}