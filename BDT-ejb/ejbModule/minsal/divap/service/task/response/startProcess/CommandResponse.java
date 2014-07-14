package minsal.divap.service.task.response.startProcess;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="process-instance">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="command-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="process-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *                   &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="index" type="{http://www.w3.org/2001/XMLSchema}byte" />
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
    "processInstance"
})
@XmlRootElement(name = "command-response")
public class CommandResponse {

    @XmlElement(name = "deployment-id", required = true)
    protected String deploymentId;
    @XmlElement(name = "process-instance", required = true)
    protected CommandResponse.ProcessInstance processInstance;

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
     * Gets the value of the processInstance property.
     * 
     * @return
     *     possible object is
     *     {@link CommandResponse.ProcessInstance }
     *     
     */
    public CommandResponse.ProcessInstance getProcessInstance() {
        return processInstance;
    }

    /**
     * Sets the value of the processInstance property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommandResponse.ProcessInstance }
     *     
     */
    public void setProcessInstance(CommandResponse.ProcessInstance value) {
        this.processInstance = value;
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
     *         &lt;element name="process-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
     *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *       &lt;/sequence>
     *       &lt;attribute name="index" type="{http://www.w3.org/2001/XMLSchema}byte" />
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
        "processId",
        "id",
        "state"
    })
    public static class ProcessInstance {

        @XmlElement(name = "command-name", required = true)
        protected String commandName;
        @XmlElement(name = "process-id", required = true)
        protected String processId;
        protected Long id;
        protected byte state;
        @XmlAttribute
        protected Byte index;

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
         * Gets the value of the id property.
         * 
         */
        public Long getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         */
        public void setId(Long value) {
            this.id = value;
        }

        /**
         * Gets the value of the state property.
         * 
         */
        public byte getState() {
            return state;
        }

        /**
         * Sets the value of the state property.
         * 
         */
        public void setState(byte value) {
            this.state = value;
        }

        /**
         * Gets the value of the index property.
         * 
         * @return
         *     possible object is
         *     {@link Byte }
         *     
         */
        public Byte getIndex() {
            return index;
        }

        /**
         * Sets the value of the index property.
         * 
         * @param value
         *     allowed object is
         *     {@link Byte }
         *     
         */
        public void setIndex(Byte value) {
            this.index = value;
        }

    }

}