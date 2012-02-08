package com.systex.sop.cvs.ui.customize.other;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observer;

import com.qt.datapicker.DatePicker;

public class SSSDatePicker extends DatePicker {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

	/** Construct **/
	public SSSDatePicker(Observer observer) {
		super(observer);
	}

	/** Construct **/
	public SSSDatePicker(Observer observer, Date selecteddate) {
		super(observer, selecteddate, Locale.TAIWAN);
	}

	/** Construct **/
	public SSSDatePicker(Observer observer, Locale locale) {
		super(observer, new Date(), locale);
	}

	/** Construct **/
	public SSSDatePicker(Observer observer, Date selecteddate, Locale locale) {
		super(observer, selecteddate, locale);
	}

	public Date parseDate(String date) {
		try {
			return sdf.parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	public String formatDate(Date date) {
		if (date == null)
			return "";
		return sdf.format(date);
	}
}
