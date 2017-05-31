package com.xyt.msgprocess.handler;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.xyt.msgprocess.entity.Constants;

public class MPLogHandler extends Formatter {

	@Override
	public String format(LogRecord record) {

		Date time = new Date();
		time.setTime(record.getMillis());
		return record.getLevel() + "-" + time.toString() + "-Thread:" + record.getThreadID() + "-"
				+ record.getSourceClassName() + "-<" + record.getSourceMethodName() + ">" + Constants.LINE_END_INDICATOR
				+ record.getMessage() + Constants.LINE_END_INDICATOR + Constants.LINE_END_INDICATOR;
	}

}
