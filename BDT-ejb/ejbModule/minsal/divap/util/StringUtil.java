package minsal.divap.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {

    private static final NumberFormat INT_FORMATTER_4CHARS = NumberFormat.getIntegerInstance();
    private static final Pattern URL_PATTERN = Pattern.compile("\\b(https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]",Pattern.CASE_INSENSITIVE);
    
    
    private static final String DEG = "\u00B0";
    private static final String MIN = "\u2032";
    private static final String SEC = "\u2033";
    
    private static final String [] specialCharacters = {DEG, MIN, SEC};

    public static String padNumber(int value, int minCharCount) {
        StringBuilder sb = new StringBuilder(String.valueOf(value));
        char c = '0';
        while (sb.length() < minCharCount) {
            sb.insert(0, c);
        }
        return sb.toString();
    }

    /**
     * 
     * @param value
     * @param fixedCharCount
     * @return
     */
    public static String formatInteger(int value, int fixedCharCount) {
        INT_FORMATTER_4CHARS.setMinimumFractionDigits(fixedCharCount);
        return INT_FORMATTER_4CHARS.format(value);
    }

    /**
     * 
     * @param size
     * @return
     */
    public static String getRandomString(int size) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        int charsSize = chars.length();
        do {
            sb.append(chars.charAt(r.nextInt(charsSize)));
        } while (sb.length() < size);
        return sb.toString();
    }

    /**
     *
     * @param input
     * @return
     */
    public static String capitalize(String input) {
        /*
         * tratamos el caso de 'Álava'
         */
        if (input.startsWith("&")) {
            int pos = input.indexOf(';');
            String ent = input.substring(0, pos + 1);
            int c = Integer.parseInt(ent.substring(2, ent.length() - 1));
            input = String.valueOf((char) c) + input.substring(pos + 1);
        }
        /*
         * compruebo que no este formateado ya
         */
        int c0 = input.charAt(0);
        int c1 = input.charAt(1);
        if (c0 > 64 && c0 < 91 && c1 > 96 && c1 < 123) {
            return input;
        } else {
            return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
        }
    }

    /**
     * 
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        if (value == null) {
            return true;
        }
        return value.trim().length() == 0;
    }

    /**
     * Cambia las ñ por n y quita acentos de las vocales.
     * @param input
     * @return
     */
    public static String removeSpanishAccents(String input) {
        input = input.replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u').replace('ñ', 'n').replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U')
        		.replace('Ñ', 'N').replace('º', ' ').replace('\'', ' ');
        return input;
    }
    
    public static String removeSpecialCharacters(String input) {
    	for(int i = 0; i<input.length(); i++){
    		System.out.print(input.charAt(i));
    	}
    	System.out.println("******************************************************************");
    	for(String specialChar : specialCharacters){
    		input = input.replace(specialChar, "?");
    	}
        return input;
    }

    /**
     *
     *
     * @param input
     * @return la cadena input en minusculas y caracteres distintos de letras y
     *         numeros. Cambia espacios por '-'   Nº
     */
    public static String normalizeString(String input, boolean keepSpaces) {
        StringBuilder sb = new StringBuilder();
        char c;
        int n;
        input = removeSpanishAccents(input.toLowerCase());
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            if (c == ' ') {
                sb.append(keepSpaces ? c : '-');
            } else {
                n = (int) c;
                if ((n > 96 && n < 123) || (n > 47 && n < 58)) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param input
     * @return
     */
    public static String normalizeString(String input) {
        return normalizeString(input, false);
    }

    public static String parseHTML(String text) {
        List<String> links = findUrls(text);

        String a = null;
        for(String l:links){
            a = " <a href='" + l + "'>" + l + "</a> ";
            text = text.replaceAll(l, a);
        }
        return text;
    }

    /**
     *
     * @param text
     * @return
     */
    public static List<String> findUrls(String text) {
        Matcher m = URL_PATTERN.matcher(text);
        List<String> links = new LinkedList<String>();
        while (m.find()) {
            links.add(m.group());
        }
        return links;
    }
    
    
    /**
     * Case insensitive check if a String ends with a specified suffix.
     *
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case insensitive.
     *
     * <pre>
     * StringUtils.endsWithIgnoreCase(null, null)      = true
     * StringUtils.endsWithIgnoreCase(null, "abcdef")  = false
     * StringUtils.endsWithIgnoreCase("def", null)     = false
     * StringUtils.endsWithIgnoreCase("def", "abcdef") = true
     * StringUtils.endsWithIgnoreCase("def", "ABCDEF") = false
     * </pre>
     *
     * @see java.lang.String#endsWith(String)
     * @param str  the String to check, may be null
     * @param suffix the suffix to find, may be null
     * @return <code>true</code> if the String ends with the suffix, case insensitive, or
     *  both <code>null</code>
     * @since 2.4
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return endsWith(str, suffix, true);
    }

    /**
     * Check if a String ends with a specified suffix (optionally case insensitive).
     *
     * @see java.lang.String#endsWith(String)
     * @param str  the String to check, may be null
     * @param suffix the suffix to find, may be null
     * @param ignoreCase inidicates whether the compare should ignore case
     *  (case insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or
     *  both <code>null</code>
     */
    private static boolean endsWith(String str, String suffix, boolean ignoreCase) {
        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
    }
    
    public static String integerWithFormat(Integer numero){
    	DecimalFormat formateador = new DecimalFormat("###,###.##");
    	String resultado = formateador.format(numero);
    	return resultado;
    }
    
    public static String longWithFormat(Long numero){
    	DecimalFormat formateador = new DecimalFormat("###,###.##");
    	String resultado = formateador.format(numero);
    	return resultado;
    }
    
    public static String doubleWithFormat(Double numero){
    	DecimalFormat formateador = new DecimalFormat("###,###.##");
    	String resultado = formateador.format(numero);
    	return resultado;
    }
    
    public static String caracterUnoMayuscula(String palabra){
    	String Letra1 = palabra.substring(0, 1).toUpperCase();
		String Letras = palabra.substring(1, palabra.length()).toLowerCase();
		String resultado = Letra1+Letras;
		return resultado;
    	
    }
    
	public static String formatNumber(Double number){
		System.out.println("format double");
		 Locale chileLocale = new Locale("es", "CL");
		 DecimalFormat formato = (DecimalFormat) DecimalFormat.getInstance(chileLocale);
		return formato.format(number);
	}
	
	
	public static String formatNumber(Long number){
		System.out.println("format long");
		 Locale chileLocale = new Locale("es", "CL");
		 DecimalFormat formato = (DecimalFormat) DecimalFormat.getInstance(chileLocale);
		return formato.format(number);
	}
	
	public static String formatNumber(Integer number){
		System.out.println("format Integer");
		 Locale chileLocale = new Locale("es", "CL");
		 DecimalFormat formato = (DecimalFormat) DecimalFormat.getInstance(chileLocale);
		return formato.format(number);
	}

	public static void main(String[] args){
		System.out.println("Número double : "+ removeSpecialCharacters(removeSpanishAccents("42 Chiloé, Oficio Nº 152 de 26.01.15, Remite Resoluciones que determinan Aporte Estatal Mensual, año 2015.pdf"))); 
		/*System.out.println("Número double : "+ formatNumber(1500.505));
		System.out.println("Número long : "+ formatNumber(500000000l));
		System.out.println("Número integer : "+ formatNumber(455800));*/
	}
}