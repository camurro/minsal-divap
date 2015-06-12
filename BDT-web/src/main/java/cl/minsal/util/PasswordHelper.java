package cl.minsal.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.crypto.CryptoUtil;


public class PasswordHelper {

    public static final String patron = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
    public static final String alfabeto_numeros = "123456789";
    public static final String alfabeto_mayusculas = "ABCDEFGHIJKMNPQRSTUVWXYZ";
    public static final String alfabeto_minusculas = "abcdefghijkmnpqrstuvwxyx";
    public static final String CHANGE_PASSWORD = "<p>Bienvenido {0}</p><br><p>{1} con éxito, para acceder utilice las siguientes credenciales.</p><p>usuario: {2}<p>contraseña: {3}<br><p>tenga presente que se solicitara cambiar su contraseña después de ingresar.</p><br>";

    public static void main(String[] args) {
        try {
            PasswordHelper validacionPassword = new PasswordHelper();
            for (int i = 0; i < 5000; i++) {
                String pwd1 = validacionPassword.generarPassword();
                System.out.println("[" + pwd1 + "] ,");
                validacionPassword.validarFormatoContrasena(pwd1);
                System.out.println("--->"
                        + validacionPassword.encriptarMD5base64("PF55cV9i"));
            }
        } catch (PatronInvalidoException e) {
            e.printStackTrace();
        }
    }

    public void validarFormatoContrasena(String texto)
            throws PatronInvalidoException {
        Pattern p = Pattern
                .compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");

        Matcher matcher = p.matcher(texto);

        if (!matcher.matches())
            throw new PatronInvalidoException(this, "Contraseña no coinside con el patron requerido");
    }

    public String generarPassword() {
        String password = "";

        char[] pwd = new char[8];
        pwd[0] = getRandomChar("ABCDEFGHIJKMNPQRSTUVWXYZ");
        pwd[1] = getRandomChar("ABCDEFGHIJKMNPQRSTUVWXYZ");
        pwd[2] = getRandomChar("123456789");
        pwd[3] = getRandomChar("123456789");
        pwd[4] = getRandomChar("abcdefghijkmnpqrstuvwxyx");
        pwd[5] = getRandomChar("ABCDEFGHIJKMNPQRSTUVWXYZ");
        pwd[6] = getRandomChar("123456789");
        pwd[7] = getRandomChar("abcdefghijkmnpqrstuvwxyx");

        password = String.valueOf(pwd);
        return password;
    }

    private char getRandomChar(String alfabeto) {
        Random r = new Random();
        return alfabeto.charAt(r.nextInt(alfabeto.length()));
    }

    public String encriptarMD5base64(String cadena) {
        return CryptoUtil.createPasswordHash("MD5", "BASE64", null, null,
                cadena);
    }

}

class PatronInvalidoException extends Exception
{
  private static final long serialVersionUID = 1L;

  public PatronInvalidoException(PasswordHelper paramPasswordHelper, String message)
  {
    super(message);
  }
}