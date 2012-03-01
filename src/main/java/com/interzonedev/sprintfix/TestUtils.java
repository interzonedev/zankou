package com.interzonedev.sprintfix;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestUtils {
	private static Log log = LogFactory.getLog(TestUtils.class);

	/**
	 * Parses the specified date string using the specified format into a {@code Date} in the default {@code TimeZone}.
	 * 
	 * @param dateString
	 *            - The date string to be parsed.
	 * @param dateFormat
	 *            - The format of the date to be parsed.
	 * 
	 * @return Returns a {@code Date} object parsed from the specified date string using the specified format in the
	 *         default {@code TimeZone}.
	 */
	public static Date parseDateInDefaultTimeZone(String dateString, String dateFormat) {
		try {
			DateFormat formatter = new SimpleDateFormat(dateFormat);
			Date date = (Date) formatter.parse(dateString);
			return date;
		} catch (ParseException pe) {
			String errorMessage = "parseDateInDefaultTimeZone: Error parsing date " + dateString + " into format "
					+ dateFormat;
			log.error(errorMessage, pe);
			throw new RuntimeException(errorMessage, pe);
		}
	}

	/**
	 * Gets a {@code Date} object set to midnight on January 1, 1970 in the default {@code TimeZone}.
	 * 
	 * @return Returns a {@code Date} object set to midnight on January 1, 1970 in the default {@code TimeZone}.
	 */
	public static Date getEpoch() {
		return parseDateInDefaultTimeZone("1970-01-01 00:00:00.000", "yyyy-MM-dd HH:mm:ss.SSS");
	}
}
