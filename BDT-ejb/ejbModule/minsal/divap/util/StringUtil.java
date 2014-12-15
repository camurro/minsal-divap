package minsal.divap.util;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {

    private static final NumberFormat INT_FORMATTER_4CHARS = NumberFormat.getIntegerInstance();
    private static final Pattern URL_PATTERN = Pattern.compile("\\b(https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]",Pattern.CASE_INSENSITIVE);

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
        input = input.replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u').replace('ñ', 'n').replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U').replace('Ñ', 'N');
        return input;
    }

    /**
     *
     *
     * @param input
     * @return la cadena input en minusculas y caracteres distintos de letras y
     *         numeros. Cambia espacios por '-'
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
}