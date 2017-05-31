package com.xyt.msgprocess.factory;

import java.util.List;

import com.xyt.msgprocess.entity.Notification;
import com.xyt.msgprocess.entity.Type;
import com.xyt.msgprocess.service.NotificationService;
import com.xyt.msgprocess.service.ReportService;

public class NotificationProcessor {

	private NotificationService notiService;
	private ReportService reportService;
	private NotificationProcessorFactory factory;

	public void process(Type type, List<Notification> notiList) {
		switch (type) {
		case SALES:
			factory = new SalesNotificationProcessorFactory();
			notiService = factory.createNotiService();
			reportService = factory.createReportService();

			notiService.setReportService(reportService);
			notiService.serve(notiList);
			break;
		case PURCHASE:
			// For future
			break;
		default:
			break;
		}

	}
}
