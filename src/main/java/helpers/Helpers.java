package helpers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Helpers {

	
	static public LocalDate toLocalDate(Date input) {
		
		if( input instanceof java.sql.Date )
			return ((java.sql.Date) input).toLocalDate();
		return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	static public boolean isEqual(Date d1, Date d2) {
		return toLocalDate(d1).isEqual(toLocalDate(d2));
	}
	
	
	static public boolean equals2(Date d1, Date d2) {
		
		long millisInADay = 1000 * 60 * 60 * 24;
		long m1 = d1.getTime() / millisInADay;
		long m2 = d2.getTime() / millisInADay;

		return m1 == m2;
	}
	


}
