package minsal.divap.doc.util;


/**
 * Esta clase provee la funcionalidad de convertir un numero representado en
 * digitos a una representacion en letras. Mejorado para leer centavos
 *
 * @author Camilo Nova
 */
public abstract class NumberToLetters {
	private static final int  Unidad = 1; 
	private static final int  Decena = 10;
	private static final int  Centena = 100;

	public static String readNumber( String number, String sepDecimal, String sMoney ){

		String V[] = initVector();
		String s = "";
		String z = "";
		String c = "";
		String e = " ";
		String t;

		int l = number.length();
		int k = number.indexOf( sepDecimal );
		int u = 1;
		int n = 0;
		int j = 0;
		int b = 0;
		int d, p, r;

		try{
			//obtiene los decimales
			if( k >= 0 ) { 
				c = number.substring( k + 1, l );  
				l = k; 
			}

			if ( l <= 15 ) {
				for( int i = l ; i >= 1; i-- ){
					d = Integer.parseInt( String.valueOf( number.charAt( i - 1 ) ));
					n = ( d * u ) + n;

					switch( u ){
					case Unidad:
						s = V[ n ];
						if ( i == l && n == 1 ) b++;
						break;
					case Decena:                                               
						p = d - 2;

						if( p < 0 )  
							s = V[ n ];                                     
						else{
							t =  V[ 20 + p ];

							if( n % 10 != 0 )
								s  =  (d == 2)? "veinti" + s : t + " y " + s;
							else   
								s = t;
						}
						break;
					case Centena:
						p = d - 1;
						t = V[ 30 + p ];

						if( n % 100  == 0 )
						{ s = ""; e = ""; } 
						else
							if( d == 1 ) t += "to"; 

						s = t + e + s;                                      
						z = ( s + z );  
						break;         
					} 

					e = " ";    
					//ini. calcula los miles, millones, billones
					r = l - i;                               
					if( r > 0 && r % 3 == 0  ){
						p = ( r > 10 ) ?  2 : j++ & 1;    
						t = V[ 40 + p ];

						if( p > 0 )
							if( ( n == 1 && i > 1 ) || n > 1  ) t += "es";

						z = e + t + e + z;
					}
					//fin.

					//reinicia las variables
					if ( u == Centena ){  
						u = 1;  n = 0;  s = "";  
					} else {
						u *= 10;                                
					}
				}

			}     

			//ini. adiciona la moneda y los centavos
			if ( !c.equals("") ){
				//c = " con " + c + " centavos";
				int centavos = Integer.parseInt(c);
				if (centavos == 1){
					c = " con un centavo";
				}else if (centavos > 1){
					c = " con " + convertNumber(c, V) + "centavos";
				}
			}
			if ( !sMoney.equals("") )       
				sMoney = " " + sMoney;
			else
				if( b > 0 ) z += "o"; 
			//fin.

			z = ( s + z ) + sMoney + c;
		}
		catch(NumberFormatException ex){
			z = "ERROR [readNumber]: Formato numerico incorrecto.";
		}
		return z;
	}

	private static String convertNumber(String number, String V[]) {
		if (number.length() > 3)
			throw new NumberFormatException("La longitud maxima debe ser 3 digitos");
		// Caso especial con el 100
		if (number.equals("100")) {
			return "cien";
		}
		StringBuilder output = new StringBuilder();
		if (getDigitAt(number, 2) != 0){
			output.append(CENTENAS[getDigitAt(number, 2) - 1].toLowerCase());
		}
		int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1)) + String.valueOf(getDigitAt(number, 0)));
		if (k <= 20){
			output.append(UNIDADES[k].toLowerCase());
		}else if (k > 30 && getDigitAt(number, 0) != 0){
			output.append(DECENAS[getDigitAt(number, 1) - 2].toLowerCase() + "y " + UNIDADES[getDigitAt(number, 0)].toLowerCase());
		}else{
			output.append(DECENAS[getDigitAt(number, 1) - 2].toLowerCase() + UNIDADES[getDigitAt(number, 0)].toLowerCase());
		}
		return output.toString();
	}

	private static int getDigitAt(String origin, int position) {
		if (origin.length() > position && position >= 0)
			return origin.charAt(origin.length() - position - 1) - 48;
		return 0;
	}
	
	private static final String[] UNIDADES = { "", "UN ", "DOS ", "TRES ",
		"CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ", "DIEZ ",
		"ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS",
		"DIECISIETE", "DIECIOCHO", "DIECINUEVE", "VEINTE" };
		private static final String[] DECENAS = { "VENTI", "TREINTA ", "CUARENTA ",
		"CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA ",
		"CIEN " };
		private static final String[] CENTENAS = { "CIENTO ", "DOSCIENTOS ",
		"TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ",
		"SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS " };


	private static String[] initVector(  ){
		String V[] =  { "cero", "un", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez",
		"once", "doce", "trece", "catorce", "quince", "dieciseis", "diecisiete", "dieciocho", "diecinueve", "veinte",
		"treinta", "cuarenta", "cincuenta", "secenta", "setenta", "ochenta", "noventa", "", "",
		"cien", "doscientos", "trescientos", "cuatrocientos", "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos",
		"", "mil", "millon", "billon"};
		return V;
	}

	public static void main(String[] args){
		System.out.println("total 999.999.999.999,999 =" + NumberToLetters.readNumber("978996599999,999", "," ,"pesos"));
		System.out.println("total 370.721.238.492 =" + NumberToLetters.readNumber("370721238492", "," ,"pesos")); 
	}
}