package minsal.divap.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {
	
	/**
	 * Retorna la fecha del día como string en formato dd-MM-yyyy
	 * @return
	 */
	public static String getFechaDia(){
		return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	}
	
	  // obtener el mes de la fecha.
    public static Integer obtenerMes(Date date){
    	if (null == date){
    	return 0;
    	}
    	else{

    	String formato="MM";
    	SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
    	return Integer.parseInt(dateFormat.format(date));

    	}
    }
    // obtener el día de la fecha.
    public static Integer obtenerDia(Date date){

	    	if (null == date){
	    	return 0;
	    	}
	    	else{
	
	    	String formato="dd";
	    	SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
	    	return Integer.parseInt(dateFormat.format(date));
	
	    	}
    	}
    // obtener el día de la fecha.
    public static Integer obtenerAno(Date date){

	    	if (null == date){
	    	return 0;
	    	}
	    	else{	    		
	    		Calendar calendar = Calendar.getInstance();
	            calendar.setTime(date);
	            return calendar.get(Calendar.YEAR);	
	    	}
    	}
    
	public static String obtenerNombreMes (int mes)
	{
		 String[] mesesNombre = {"Meses","Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
			        "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" };
		 
		 return mesesNombre[mes];
	}
	
  public static Date addMonths(java.util.Date aDate, int number){  
        java.util.Calendar aCalendar = java.util.Calendar.getInstance();  
        aCalendar.setTime(aDate);  
        aCalendar.add(java.util.Calendar.MONTH, number);  
        return aCalendar.getTime();  
    }
    
}
