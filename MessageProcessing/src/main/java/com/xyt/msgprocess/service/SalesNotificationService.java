package com.xyt.msgprocess.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.xyt.msgprocess.entity.Adjustment;
import com.xyt.msgprocess.entity.Constants;
import com.xyt.msgprocess.entity.Notification;
import com.xyt.msgprocess.entity.Operation;
import com.xyt.msgprocess.util.LogUtils;
import com.xyt.msgprocess.util.StringUtils;

public class SalesNotificationService implements NotificationService {
	private static Logger log = Logger.getLogger(SalesNotificationService.class.getName());
	private static SalesNotificationService instance = new SalesNotificationService();
	private HashMap<String, List<Notification>> map = new HashMap<String, List<Notification>>();
	private int msgCount;
	private SalesReportService reportService;
	
	private SalesNotificationService(){}
	
	public static synchronized SalesNotificationService getInstance() {
		if(instance == null)
			instance = new SalesNotificationService();
		return instance;
	}
	
	public void serve(List<Notification> notiList){
		try {
			storeNotification(notiList);
			increaseMsgCount();
			if(isTimeToReportDetail()) {
				reportDetail();
			}
			if(isTimeToPausing()) {
				reportAdjustment();
				pausing();
			}
			
		} catch (Exception e) {
			log.severe("Exception occured, " + LogUtils.extractExceptionStack(e));
		}
		
	}
	
	private void storeNotification(List<Notification> noti) {
		noti.stream().forEach((notification) -> store(notification));
	}

	private void store(Notification notification) {
		Notification noti = null;
		if(notification instanceof Notification) {
			noti = (Notification) notification;
			log.info(noti.toString());
		} else {
			return;
		}
		String productType = StringUtils.trimToEmpty(noti.getProductType());
		
		if(StringUtils.isNotBlank(productType)) {
			if(map.containsKey(productType)) {
				if(isBatchMode(noti)) {//MessageType2
					map.get(productType).addAll(noti.clone(noti.getOccurrence()));
				} else if(isAdjustmentMode(noti)) {//MessageType3
					adjustValue(noti, productType);
					map.get(productType).add(noti);
				} else {//MessageType1
					map.get(productType).add(noti);	
				}
				
			} else {
				List<Notification> list = new ArrayList<Notification>();
				if(isBatchMode(noti)) {//MessageType2
					list.addAll(noti.clone(noti.getOccurrence()));
				} else {//MessageType1
					list.add(noti);
				}
				
				map.put(productType, list);
			}
		} else {
			log.warning("Notification's product type is empty, failed to process");
		}
	}

	private void adjustValue(Notification notification, String productType) {
		Adjustment adj = notification.getAdjustment();
		Operation adjOperation = adj.getOperation();
		BigDecimal adjValue = adj.getValue();
		if(adjValue == null) 
			return;
		List<Notification> notiList = map.get(productType);
		if(notiList == null)
			return;
		
		notiList.stream().forEach((existNoti) -> {
			
			switch (adjOperation) {
			case ADD:
				existNoti.setValue(existNoti.getValue().add(adjValue));
				break;
			case SUBSTRACT:
				existNoti.setValue(existNoti.getValue().subtract(adjValue));
				break;
			case MULTIPLY:
				existNoti.setValue(existNoti.getValue().multiply(adjValue));
				break;
				
			default:
				log.warning("Invalid operation");
				break;
			}
			
		});
	}
	
	private void pausing() throws Exception{
		log.info("Received total " + Constants.PAUSING_NO + " message, start pausing and stop accepting new message." );
		log.info("Press ENTER to continue...");
		System.in.read();
	}
	
	private void reportAdjustment(){
		StringBuffer reportContent = new StringBuffer();
		reportContent.append(reportService.getAdjustmentTitle());
		reportContent.append(reportService.getAdjustmentBody(map));
		log.info(reportContent.toString());
	}
	

	private void reportDetail() {
		StringBuffer reportContent = new StringBuffer();
		reportContent.append(reportService.getSummaryTitle());
		reportContent.append(reportService.getSummaryBody(map));
		log.info(reportContent.toString());
	}

	private void increaseMsgCount(){
		this.msgCount ++;
	}

	private int getMsgCount(){
		return this.msgCount;
	}
	
	private boolean isTimeToReportDetail() {
		if(getMsgCount() % Constants.REPORT_CYCLE_NO == 0)
			return true;
		
		return false;
	}
	
	private boolean isTimeToPausing(){
		if(getMsgCount() == Constants.PAUSING_NO)
			return true;
		return false;
	}
	
	/**
	 * MessageType2
	 * @param noti
	 * @return
	 */
	private boolean isBatchMode(Notification noti) {
		if(noti.getOccurrence() > 0)
			return true;
		
		return false;
	}
	
	/**
	 * MessageType3
	 * @param noti
	 * @return
	 */
	private boolean isAdjustmentMode(Notification noti) {
		if (noti.getAdjustment()!= null) 
			return true;
		
		return false;
	}

	public void setReportService(ReportService service) {
		this.reportService = (SalesReportService)service;
		
	}
}
