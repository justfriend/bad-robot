package com.systex.sop.cvs.ui.customize.other;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import com.qt.datapicker.DatePicker;
import com.systex.sop.cvs.ui.customize.comp.SSSJTextField;

@SuppressWarnings("serial")
public class ObservingTextField extends SSSJTextField implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		Calendar calendar = (Calendar) arg;
		DatePicker dp = (DatePicker) o;
		setText(dp.formatDate(calendar));
	}

}
