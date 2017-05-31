package com.xyt.msgprocess.service;

import java.util.List;

import com.xyt.msgprocess.entity.Notification;

public interface NotificationService {

	public void serve(List<Notification> notiList);
	
	public void setReportService(ReportService service);
}
