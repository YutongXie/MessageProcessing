package com.xyt.msgprocess.receiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xyt.msgprocess.entity.Notification;
import com.xyt.msgprocess.entity.Type;
import com.xyt.msgprocess.factory.NotificationProcessor;
import com.xyt.msgprocess.util.LogUtils;

public class NotificationReceiver implements MessageListener {
	private Logger log = Logger.getLogger(NotificationReceiver.class.getName());
	private String consumerName;

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public NotificationReceiver(String consumerName) {
		this.consumerName = consumerName;
	}

	public void onMessage(Message msg) {
		try {
			if (msg instanceof TextMessage) {
				String msgText = ((TextMessage) msg).getText();
				log.info(msgText);

				// 1. parse TextMessage
				Gson gson = new Gson();
				List<Notification> notiList = gson.fromJson(msgText, new TypeToken<List<Notification>>() {}.getType());
				if (notiList == null || notiList.size() == 0) {
					log.warning("Invalid message");
				}
				HashMap<Type, List<Notification>> map = classify(notiList);

				map.forEach((type, subList) -> {
					// 2. process notification
					new NotificationProcessor().process(type, subList);;
					
				});

			} else {
				log.warning("JMS Message type not support");
			}
		} catch (JMSException e) {
			log.severe("Failed to process message" + LogUtils.extractExceptionStack(e));
		}

	}

	private HashMap<Type, List<Notification>> classify(List<Notification> list) {
		HashMap<Type, List<Notification>> map = new HashMap<Type, List<Notification>>();
		if (list == null || list.size() == 0)
			return map;

		list.stream().filter(notification -> notification.getType() !=null).forEach(notification -> {
			Type type = notification.getType();
			if (map.containsKey(type)) {
				map.get(type).add(notification);
			} else {
				List<Notification> subList = new ArrayList<Notification>();
				subList.add(notification);
				map.put(type, subList);
			}
			
		});
		return map;
	}

}
