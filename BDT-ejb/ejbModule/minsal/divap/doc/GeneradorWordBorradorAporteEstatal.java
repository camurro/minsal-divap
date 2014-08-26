package minsal.divap.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import minsal.divap.vo.VariacionPoblacionVO;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

public class GeneradorWordBorradorAporteEstatal extends GeneradorWord {

	private String templateFilename;

	public GeneradorWordBorradorAporteEstatal(String fileName, String templateFilename) {
		super(fileName);
		this.templateFilename = templateFilename;
	}

	public String getTemplateFilename() {
		return templateFilename;
	}

	public void setTemplateFilename(String templateFilename) {
		this.templateFilename = templateFilename;
	}

	public  <T> void replaceValues(Map<String, Object> parameters, List<VariacionPoblacionVO> values,  Class<T> clazz) throws IOException, InvalidFormatException {
		if("XWPFDocument".equals(clazz.getSimpleName())){
			saveDocument(replaceValuesDocx(parameters, values));
		}else{
			//saveDocument(replaceValuesDoc(parameters, values));
			saveDocument(replaceValuesDocx(parameters, values));
		}
	}

	private HWPFDocument replaceValuesDoc(Map<String, Object> parameters, List<VariacionPoblacionVO> values) throws FileNotFoundException, IOException {
		File template = new File(this.templateFilename);
		HWPFDocument document = new HWPFDocument(new FileInputStream(template));

		//to replace the content in header and footnote
		/*HeaderStories hs = new HeaderStories(document); 

        Range hsRange = hs.getRange(); 
        hsRange.replaceText("#day#", (String) parameters.get("#day#")); 
        hsRange.replaceText("#mon#", parameters.get("#mon#")); 
        hsRange.replaceText("#yea#", parameters.get("#yea#")); 
        hsRange.replaceText("#n_1#", parameters.get("#n_1#")); 
        hsRange.replaceText("#n_2#", parameters.get("#n_2#")); 
        hsRange.replaceText("#serial#", parameters.get("#serial#"));*/ 

		Range range = document.getRange(); 
		if(parameters != null && !parameters.isEmpty()){
			for (String key : parameters.keySet()) {
				range.replaceText(key, (String)parameters.get(key)); 
			}

			//to replace the content in body
			/*range.replaceText("#n_v_left#", map.get("#n_v_left#")); 
			range.replaceText("#n_v_right#", map.get("#n_v_right#"));*/ 
		}
		TableIterator iterator = new TableIterator(range);
		int contTables = 0;
		while(iterator.hasNext()){
			Table table = iterator.next();
			break;
			/*for(int rowIndex = 0; rowIndex < table.numRows(); rowIndex++){
				if(rowIndex == 0){
					continue;
				}
				TableRow row = table.getRow(rowIndex);
				for(int colIndex = 0; colIndex < row.numCells(); colIndex++){
					TableCell cell = row.getCell(colIndex);
					System.out.println(cell.getParagraph(0).text());
				}
			}*/
		}
		return document;
	}

	private XWPFDocument replaceValuesDocx(Map<String, Object> parameters, List<VariacionPoblacionVO> values) throws FileNotFoundException, IOException, InvalidFormatException {
		File template = new File(this.templateFilename);
		XWPFDocument document = new XWPFDocument(new FileInputStream(template));
		String replacementText = null;
		for (XWPFParagraph p : document.getParagraphs()) {
			for (XWPFRun run : p.getRuns()) {
				String text = run.getText(0);
				//System.out.println("text--->"+text);
				if(parameters != null && !parameters.isEmpty() && text != null){
					for (String key : parameters.keySet()) {
						if(text.contains(key)){
							replacementText = parameters.get(key).toString();
							text = text.replaceAll("\\" + key, replacementText);
							run.setText(text, 0);
						}
					}
				}
			}
		}
		int contRows = 1;
		if((document.getTables() != null && document.getTables().size() > 0) && (values != null && values.size()>0)){
			XWPFTable table = document.getTables().get(0);
			for(VariacionPoblacionVO variacionPoblacionVO : values){
				table.createRow();
				int lastRow = table.getNumberOfRows();
				XWPFTableRow row = table.getRow(lastRow-1);
				int contCells = 0;
				for(XWPFTableCell cell : row.getTableCells()){
					CTTcPr tcpr = cell.getCTTc().addNewTcPr();
					// set vertical alignment to "center"
					CTVerticalJc va = tcpr.addNewVAlign();
					va.setVal(STVerticalJc.CENTER);
					if(contCells == 0){
						XWPFParagraph paragraph = cell.getParagraphs().get( 0 );
						paragraph.setAlignment(ParagraphAlignment.LEFT);
						cell.setText(String.valueOf(contRows));
					} else if(contCells == 1){
						XWPFParagraph paragraph = cell.getParagraphs().get( 0 );
						paragraph.setAlignment(ParagraphAlignment.CENTER);
						cell.setText((variacionPoblacionVO.getRegion()==null)?"":String.valueOf(variacionPoblacionVO.getRegion()));
					} else if(contCells == 2){
						XWPFParagraph paragraph = cell.getParagraphs().get( 0 );
						paragraph.setAlignment(ParagraphAlignment.LEFT);
						cell.setText((variacionPoblacionVO.getServicio()==null)?"": variacionPoblacionVO.getServicio());
					} else if(contCells == 3){
						XWPFParagraph paragraph = cell.getParagraphs().get( 0 );
						paragraph.setAlignment(ParagraphAlignment.LEFT);
						cell.setText((variacionPoblacionVO.getComuna()==null)?"": variacionPoblacionVO.getComuna());
					} else if(contCells == 4){
						XWPFParagraph paragraph = cell.getParagraphs().get( 0 );
						paragraph.setAlignment(ParagraphAlignment.CENTER);
						cell.setText((variacionPoblacionVO.getVariacion()==null)?"": String.valueOf(variacionPoblacionVO.getVariacion()));
					}
					contCells++;
				}
				contRows++;
			}
		}
		return document;
	}
}
