package com.archer.tools.java;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class TimeUtil {
	private static DateTimeFormatter YYYY_MM_DD_HH_MM_SS_SSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	private static DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static String toYYYYMMDDHHMMSSSSS(TemporalAccessor time) {
		return YYYY_MM_DD_HH_MM_SS_SSS.format(time);
	}
	
	public static String toYYYYMMDDHHMMSS(TemporalAccessor time) {
		return YYYY_MM_DD_HH_MM_SS.format(time);
	}
	
	public static String toYYYYMMDD(TemporalAccessor time) {
		return YYYY_MM_DD.format(time);
	}
	
	public static LocalDateTime parseToLocalDateTime(String reg) {
		try {
			return LocalDateTime.parse(reg, YYYY_MM_DD_HH_MM_SS_SSS);
		} catch(Exception ignore) {}

		try {
			return LocalDateTime.parse(reg, YYYY_MM_DD_HH_MM_SS);
		} catch(Exception ignore) {}

		try {
			return LocalDateTime.parse(reg, YYYY_MM_DD);
		} catch(Exception ignore) {}
		
		return LocalDateTime.parse(reg);
	}
	
	public static LocalDate parseToLocalDate(String reg) {
		try {
			return LocalDate.parse(reg, YYYY_MM_DD_HH_MM_SS_SSS);
		} catch(Exception ignore) {}

		try {
			return LocalDate.parse(reg, YYYY_MM_DD_HH_MM_SS);
		} catch(Exception ignore) {}

		try {
			return LocalDate.parse(reg, YYYY_MM_DD);
		} catch(Exception ignore) {}
		
		return LocalDate.parse(reg);
	}
}
