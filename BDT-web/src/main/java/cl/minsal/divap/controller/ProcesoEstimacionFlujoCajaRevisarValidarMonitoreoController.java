package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.service.DistribucionInicialPercapitaService;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.minsal.divap.pojo.GobiernoRegionalPojo;
import cl.minsal.divap.pojo.montosDistribucionPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.JSONHelper;

@Named("procesoEstimacionFlujoCajaRevisarValidarMonitoreoController")
@ViewScoped
public class ProcesoEstimacionFlujoCajaRevisarValidarMonitoreoController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	private UploadedFile calculoPerCapitaFile;
	private UploadedFile valorBasicoDesempenoFile;

	@EJB
	private DistribucionInicialPercapitaService distribucionInicialPercapitaService;
	private boolean errorCarga = false;
	private boolean archivosValidos = false;
	private String docIdDownload;
	private Integer docAsignacionRecursosPercapita;
	private Integer docAsignacionDesempenoDificil;
	private List<Integer> docIds;
	private Integer idDistribucionInicialPercapita;

	public UploadedFile getCalculoPerCapitaFile() {
		return calculoPerCapitaFile;
	}





	@Override
	public String toString() {
		return "ProcesoAsignacionPerCapitaController [validarMontosDistribucion="
				+ "" + "]";
	}

	

	@PostConstruct
	public void init() {
		log.info("ProcesoReliquidacionController tocado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}else{
			
		}
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
//		System.out.println("createResultData usuario-->"
//				+ getSessionBean().getUsername());
//		parameters.put("idLineaProgramatica_", 1);
//		//parameters.put("usuario", getSessionBean().getUsername());
	return parameters;
		//return null;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	
}
