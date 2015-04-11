package minsal.divap.util;

import org.jboss.security.auth.spi.Util;
public class PasswordGenerator {
   public static void main(String[] args) {
        //System.out.println(new PasswordGenerator().generate("Redhat123_")+"//");
	   //System.out.println(new PasswordGenerator().generate("minSAL.2014")+"//");
	   System.out.println(new PasswordGenerator().generate("Minsal.2015")+"//");
    }
   private String generate(String password) {
     return Util.createPasswordHash("SHA-256", "BASE64", null, null,password);
	 // return Util.createPasswordHash("MD5", null, null, null, password);  
   }
}
