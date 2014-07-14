package minsal.divap.service.task.response.taskpotencialOwner;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deployment-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="task-summary-list">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="command-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="task-summary" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *                             &lt;element name="skipable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="actual-owner" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="created-by" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="created-on" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                             &lt;element name="activation-time" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                             &lt;element name="process-instance-id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *                             &lt;element name="process-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="process-session-id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *                             &lt;element name="parent-id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="index" type="{http://www.w3.org/2001/XMLSchema}long" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "deploymentId",
    "taskSummaryList"
})
@XmlRootElement(name = "command-response")
public class CommandResponse {

    @XmlElement(name = "deployment-id", required = true)
    protected String deploymentId;
    @XmlElement(name = "task-summary-list", required = true)
    protected CommandResponse.TaskSummaryList taskSummaryList;

    /**
     * Gets the value of the deploymentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeploymentId() {
        return deploymentId;
    }

    /**
     * Sets the value of the deploymentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeploymentId(String value) {
        this.deploymentId = value;
    }

    /**
     * Gets the value of the taskSummaryList property.
     * 
     * @return
     *     possible object is
     *     {@link CommandResponse.TaskSummaryList }
     *     
     */
    public CommandResponse.TaskSummaryList getTaskSummaryList() {
        return taskSummaryList;
    }

    /**
     * Sets the value of the taskSummaryList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommandResponse.TaskSummaryList }
     *     
     */
    public void setTaskSummaryList(CommandResponse.TaskSummaryList value) {
        this.taskSummaryList = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="command-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="task-summary" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
     *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}long"/>
     *                   &lt;element name="skipable" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="actual-owner" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="created-by" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="created-on" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *                   &lt;element name="activation-time" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *                   &lt;element name="process-instance-id" type="{http://www.w3.org/2001/XMLSchema}long"/>
     *                   &lt;element name="process-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="process-session-id" type="{http://www.w3.org/2001/XMLSchema}long"/>
     *                   &lt;element name="parent-id" type="{http://www.w3.org/2001/XMLSchema}long"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="index" type="{http://www.w3.org/2001/XMLSchema}long" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "commandName",
        "taskSummary"
    })
    public static class TaskSummaryList {

        @XmlElement(name = "command-name", required = true)
        protected String commandName;
        @XmlElement(name = "task-summary")
        protected List<CommandResponse.TaskSummaryList.TaskSummary> taskSummary;
        @XmlAttribute
        protected long index;

        /**
         * Gets the value of the commandName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCommandName() {
            return commandName;
        }

        /**
         * Sets the value of the commandName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCommandName(String value) {
            this.commandName = value;
        }

        /**
         * Gets the value of the taskSummary property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the taskSummary property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTaskSummary().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CommandResponse.TaskSummaryList.TaskSummary }
         * 
         * 
         */
        public List<CommandResponse.TaskSummaryList.TaskSummary> getTaskSummary() {
            if (taskSummary == null) {
                taskSummary = new ArrayList<CommandResponse.TaskSummaryList.TaskSummary>();
            }
            return this.taskSummary;
        }

        /**
         * Gets the value of the index property.
         * 
         * @return
         *     possible object is
         *     {@link long }
         *     
         */
        public long getIndex() {
            return index;
        }

        /**
         * Sets the value of the index property.
         * 
         * @param value
         *     allowed object is
         *     {@link long }
         *     
         */
        public void setIndex(long value) {
            this.index = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
         *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}long"/>
         *         &lt;element name="skipable" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="actual-owner" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="created-by" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="created-on" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         *         &lt;element name="activation-time" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         *         &lt;element name="process-instance-id" type="{http://www.w3.org/2001/XMLSchema}long"/>
         *         &lt;element name="process-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="process-session-id" type="{http://www.w3.org/2001/XMLSchema}long"/>
         *         &lt;element name="parent-id" type="{http://www.w3.org/2001/XMLSchema}long"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "id",
            "name",
            "subject",
            "description",
            "status",
            "priority",
            "skipable",
            "actualOwner",
            "createdBy",
            "createdOn",
            "activationTime",
            "processInstanceId",
            "processId",
            "processSessionId",
            "parentId"
        })
        public static class TaskSummary {

            protected long id;
            @XmlElement(required = true)
            protected String name;
            @XmlElement(required = true)
            protected String subject;
            @XmlElement(required = true)
            protected String description;
            @XmlElement(required = true)
            protected String status;
            protected int priority;
            @XmlElement(required = true)
            protected String skipable;
            @XmlElement(name = "actual-owner", required = true)
            protected String actualOwner;
            @XmlElement(name = "created-by", required = true)
            protected String createdBy;
            @XmlElement(name = "created-on", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar createdOn;
            @XmlElement(name = "activation-time", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar activationTime;
            @XmlElement(name = "process-instance-id")
            protected long processInstanceId;
            @XmlElement(name = "process-id", required = true)
            protected String processId;
            @XmlElement(name = "process-session-id")
            protected long processSessionId;
            @XmlElement(name = "parent-id")
            protected long parentId;

            /**
             * Gets the value of the id property.
             * 
             */
            public long getId() {
                return id;
            }

            /**
             * Sets the value of the id property.
             * 
             */
            public void setId(long value) {
                this.id = value;
            }

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the subject property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSubject() {
                return subject;
            }

            /**
             * Sets the value of the subject property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSubject(String value) {
                this.subject = value;
            }

            /**
             * Gets the value of the description property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDescription() {
                return description;
            }

            /**
             * Sets the value of the description property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDescription(String value) {
                this.description = value;
            }

            /**
             * Gets the value of the status property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStatus() {
                return status;
            }

            /**
             * Sets the value of the status property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStatus(String value) {
                this.status = value;
            }

            /**
             * Gets the value of the priority property.
             * 
             */
            public int getPriority() {
                return priority;
            }

            /**
             * Sets the value of the priority property.
             * 
             */
            public void setPriority(int value) {
                this.priority = value;
            }

            /**
             * Gets the value of the skipable property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSkipable() {
                return skipable;
            }

            /**
             * Sets the value of the skipable property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSkipable(String value) {
                this.skipable = value;
            }

            /**
             * Gets the value of the actualOwner property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getActualOwner() {
                return actualOwner;
            }

            /**
             * Sets the value of the actualOwner property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setActualOwner(String value) {
                this.actualOwner = value;
            }

            /**
             * Gets the value of the createdBy property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCreatedBy() {
                return createdBy;
            }

            /**
             * Sets the value of the createdBy property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCreatedBy(String value) {
                this.createdBy = value;
            }

            /**
             * Gets the value of the createdOn property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getCreatedOn() {
                return createdOn;
            }

            /**
             * Sets the value of the createdOn property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setCreatedOn(XMLGregorianCalendar value) {
                this.createdOn = value;
            }

            /**
             * Gets the value of the activationTime property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getActivationTime() {
                return activationTime;
            }

            /**
             * Sets the value of the activationTime property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setActivationTime(XMLGregorianCalendar value) {
                this.activationTime = value;
            }

            /**
             * Gets the value of the processInstanceId property.
             * 
             */
            public long getProcessInstanceId() {
                return processInstanceId;
            }

            /**
             * Sets the value of the processInstanceId property.
             * 
             */
            public void setProcessInstanceId(long value) {
                this.processInstanceId = value;
            }

            /**
             * Gets the value of the processId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getProcessId() {
                return processId;
            }

            /**
             * Sets the value of the processId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setProcessId(String value) {
                this.processId = value;
            }

            /**
             * Gets the value of the processSessionId property.
             * 
             */
            public long getProcessSessionId() {
                return processSessionId;
            }

            /**
             * Sets the value of the processSessionId property.
             * 
             */
            public void setProcessSessionId(long value) {
                this.processSessionId = value;
            }

            /**
             * Gets the value of the parentId property.
             * 
             */
            public long getParentId() {
                return parentId;
            }

            /**
             * Sets the value of the parentId property.
             * 
             */
            public void setParentId(long value) {
                this.parentId = value;
            }

        }

    }

}