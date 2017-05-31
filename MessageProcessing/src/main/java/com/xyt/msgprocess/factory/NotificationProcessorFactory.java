package com.xyt.msgprocess.factory;

import com.xyt.msgprocess.service.NotificationService;
import com.xyt.msgprocess.service.ReportService;

public interface NotificationProcessorFactory {
	
	public NotificationService createNotiService();
	public ReportService createReportService();
}
