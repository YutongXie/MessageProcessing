package com.xyt.msgprocess.factory;

import com.xyt.msgprocess.service.NotificationService;
import com.xyt.msgprocess.service.ReportService;
import com.xyt.msgprocess.service.SalesNotificationService;
import com.xyt.msgprocess.service.SalesReportService;

public class SalesNotificationProcessorFactory implements NotificationProcessorFactory{

	public NotificationService createNotiService() {
		return SalesNotificationService.getInstance();
	}

	public ReportService createReportService() {
		return SalesReportService.getInstance();
	}

}
