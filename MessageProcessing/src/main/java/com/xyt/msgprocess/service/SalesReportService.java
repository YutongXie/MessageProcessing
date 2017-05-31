package com.xyt.msgprocess.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.xyt.msgprocess.entity.Constants;
import com.xyt.msgprocess.entity.Notification;

public class SalesReportService implements ReportService {

	private static SalesReportService instance = new SalesReportService();

	private SalesReportService() {}

	public static synchronized SalesReportService getInstance() {

		if (instance == null) {
			instance = new SalesReportService();
		}
		return instance;
	}

	public String getAdjustmentTitle() {
		StringBuffer title = new StringBuffer();
		title.append("ProductType").append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append("Operation")
				.append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append("Value").append(Constants.LINE_END_INDICATOR);
		title.append("-----------").append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append("---------")
				.append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append("-----").append(Constants.LINE_END_INDICATOR);
		return title.toString();
	}

	public String getSummaryTitle() {
		StringBuffer title = new StringBuffer();
		title.append("ProductType").append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append("Number")
				.append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append("TotalValue")
				.append(Constants.LINE_END_INDICATOR);
		title.append("-----------").append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append("------")
				.append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append("----------")
				.append(Constants.LINE_END_INDICATOR);

		return title.toString();
	}

	public String getSummaryBody(HashMap<String, List<Notification>> map) {
		StringBuffer body = new StringBuffer();
		if (map == null || map.keySet() == null)
			return body.toString();
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String productType = (String) iterator.next();
			List<Notification> notiList = map.get(productType);
			body.append(extractSummary(productType, notiList));
		}
		return body.toString();
	}

	private String extractSummary(String productType, List<Notification> notiList) {
		StringBuffer summary = new StringBuffer();
		summary.append(productType).append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append(notiList.size())
				.append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append(calculateTotalValue(notiList))
				.append(Constants.LINE_END_INDICATOR);
		return summary.toString();
	}

	private BigDecimal calculateTotalValue(List<Notification> notiList) {
		BigDecimal totalValue = new BigDecimal(0.0);
		for (Notification notification : notiList) {
			totalValue = totalValue.add(notification.getValue());
		}

		return totalValue;
	}

	public String getAdjustmentBody(HashMap<String, List<Notification>> map) {
		StringBuffer body = new StringBuffer();
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String productType = (String) iterator.next();
			List<Notification> notiList = map.get(productType);
			body.append(extractAdjustment(notiList));
		}
		return body.toString();
	}

	private String extractAdjustment(List<Notification> notiList) {
		StringBuffer adjustmentInfo = new StringBuffer();
		for (Notification notification : notiList) {
			if (notification.getAdjustment() != null) {
				adjustmentInfo.append(notification.getProductType()).append(Constants.REPORT_FORMAT_INTERVAL_SPACE)
						.append(notification.getAdjustment().getOperation())
						.append(Constants.REPORT_FORMAT_INTERVAL_SPACE).append(notification.getAdjustment().getValue())
						.append(Constants.LINE_END_INDICATOR);
			}
		}

		return adjustmentInfo.toString();
	}
}
