package org.cryptical.banmanager.utils.serializers;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static Date add(Date old, int years, int months, int days, int hours, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(old);
		cal.set(cal.get(Calendar.YEAR) + years, cal.get(Calendar.MONTH)+months, cal.get(Calendar.DAY_OF_MONTH)+days, cal.get(Calendar.HOUR_OF_DAY)+hours, cal.get(Calendar.MINUTE)+minutes);
		return cal.getTime();
	}
	
	public static Date date(int years, int months, int days, int hours, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.set(years, months, days, hours, minutes);
		return cal.getTime();
	}
	
	public static String serialize(Date date) {
		int year = getCal(date).get(Calendar.YEAR);
		int month = getCal(date).get(Calendar.MONTH);
		int day = getCal(date).get(Calendar.DAY_OF_MONTH);
		int hour = getCal(date).get(Calendar.HOUR_OF_DAY);
		int minute = getCal(date).get(Calendar.MINUTE);
		return (year + "," + month + "," + day + "," + hour + "," + minute);
	}
	
	public static String format(Date date) {
		int year = getCal(date).get(Calendar.YEAR);
		int month = getCal(date).get(Calendar.MONTH)+1;
		int day = getCal(date).get(Calendar.DAY_OF_MONTH);
		int hour = getCal(date).get(Calendar.HOUR_OF_DAY);
		int minute = getCal(date).get(Calendar.MINUTE);
		
		return (hour+":"+minute+" "+day+"-"+month+"-"+year);
	}
	
	public static Date deserialize(String date) {
		if (date.equals("na")) {
			return new Date();
		}
		String[] s = date.split(",");
		int year = Integer.valueOf(s[0]);
		int month = Integer.valueOf(s[1]);
		int day = Integer.valueOf(s[2]);
		int hour = Integer.valueOf(s[3]);
		int minute = Integer.valueOf(s[4]);
		
		return date(year, month, day, hour, minute);
	}
	
	public static Calendar getCal(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	/*
	 * int[0] = years
	 * int[1] = months
	 * int[2] = days
	 * int[3] = hours
	 * int[4] = minutes
	 */
	public static int[] subtract(String string) {
		int[] num = new int[5];
		num[0] = 0;
		num[1] = 0;
		num[2] = 0;
		num[3] = 0;
		num[4] = 0;
		String[] split = string.split("-");
		for (String s : split) {
			if (s.contains("Y")) num[0] = Integer.valueOf(s.replace("Y", "")); 
			if (s.contains("M")) num[1] = Integer.valueOf(s.replace("M", ""));
			if (s.contains("D")) num[2] = Integer.valueOf(s.replace("D", ""));
			if (s.contains("h")) num[3] = Integer.valueOf(s.replace("h", ""));
			if (s.contains("m")) num[4] = Integer.valueOf(s.replace("m", ""));
		}
		return num;
	}
} 
