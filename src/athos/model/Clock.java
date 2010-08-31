package athos.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Defines clock time to mark action time.
 * 
 * @author Hongbing Kou
 * @version $Id: Clock.java 281 2005-11-10 22:25:19Z hongbing $
 */
public class Clock {
	/** Data of the clock. */
	private Date date;

	/** Year of the clock. */
	private int year;
	/** Month of the clock. It is numbered from 0 to 11. */
	private int month;
	/** Day of the clock. */
	private int day;

	/** Hour of the clock. */
	private int hour;
	/** Minutes of the clock. */
	private int minute;
	/** Second of the clock. */
	private int second;
	/** Shortest format/ */
	private static SimpleDateFormat HHMMSS_Format = new SimpleDateFormat(
			"hh:mm:ss");
	private static SimpleDateFormat FULL_Format = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss");

	// TODO [clean] remove clock

	/**
	 * Defines a clock instante with date object.
	 * 
	 * @param date
	 *            A date obejct.
	 */
	public Clock(Date date) {
		this.date = date;

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		this.year = cal.get(Calendar.YEAR);
		this.month = cal.get(Calendar.MONTH);
		this.day = cal.get(Calendar.DAY_OF_MONTH);

		this.hour = cal.get(Calendar.HOUR_OF_DAY);
		this.minute = cal.get(Calendar.MINUTE);
		this.second = cal.get(Calendar.SECOND);
	}

	/**
	 * Returns year.
	 * 
	 * @return Year of the clock.
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * Gets month of the clock. January is 0 and december is 11.
	 * 
	 * @return Month of the clock.
	 */
	public int getMonth() {
		return this.month;
	}

	/**
	 * Returns day of the clock.
	 * 
	 * @return Day of the clock.
	 */
	public int getDay() {
		return this.day;
	}

	/**
	 * Gets hour of the clock.
	 * 
	 * @return Hour of the clock.
	 */
	public int getHour() {
		return this.hour;
	}

	/**
	 * Gets minute of the clock.
	 * 
	 * @return Minute of the clock.
	 */
	public int getMinute() {
		return this.minute;
	}

	/**
	 * Gets second of the clock.
	 * 
	 * @return Second of the clock.
	 */
	public int getSecond() {
		return this.second;
	}

	/**
	 * Calculate hashcode for this object.
	 * 
	 * @return Number of seconds as hashcode.
	 */
	public int hashCode() {
		return this.date.hashCode();
	}

	/**
	 * Compares to another clock object.
	 * 
	 * @param o
	 *            Another clock object.
	 * @return +0 if this object is bigger, -0 if this instance is smaller, or 0
	 *         if two objects are equal.
	 */
	public int compareTo(Object o) {
		return this.date.compareTo(((Clock) o).date);
	}

	/**
	 * Checks two objects equal or not.
	 * 
	 * @param o
	 *            Another clock object.
	 * @return True if two are identical in seconds and false otherwise.
	 */
	public boolean equals(Object o) {
		return compareTo(o) == 0;
	}

	/**
	 * Calculates time difference between two clock instances.
	 * 
	 * @param clockA
	 *            First clock.
	 * @param clockB
	 *            Second clock.
	 * @return Time difference in seconds.
	 */
	public static int diff(Clock clockA, Clock clockB) {
		long utcDiff = clockB.date.getTime() - clockA.date.getTime();
		return (int) utcDiff / 1000;
	}

	/**
	 * Returns date object that equals to this clock.
	 * 
	 * @return Date of the clock time.
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Gets string representation of clock.
	 * 
	 * @return Clock string.
	 */
	public String toString() {
		return FULL_Format.format(this.date);
	}

	/**
	 * Gets short name of the clock.
	 * 
	 * @return hh:mm:ss
	 */
	public String getShortestTime() {
		synchronized (HHMMSS_Format) {
			return HHMMSS_Format.format(this.date);
		}
	}

	/**
	 * Increments second by one.
	 * 
	 */
	public void incSecond() {
		this.second++;

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND, this.second);

		this.date = cal.getTime();
	}
}