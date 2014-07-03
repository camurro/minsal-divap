package minsal.divap.excel.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelExtensionValidator {
	private Pattern pattern;
	private Matcher matcher;

	private static final String FILE_PATTERN = 
			"([^\\s]+(\\.(?i)(xls|xlsx))$)";

	public ExcelExtensionValidator(){
		pattern = Pattern.compile(FILE_PATTERN);
	}

	public boolean validate(final String fname){
		matcher = pattern.matcher(fname);
		return matcher.matches();
	}

}
