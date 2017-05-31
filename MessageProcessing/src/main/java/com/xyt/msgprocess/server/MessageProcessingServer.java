package com.xyt.msgprocess.server;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import com.google.gson.Gson;
import com.xyt.msgprocess.entity.Adjustment;
import com.xyt.msgprocess.entity.Notification;
import com.xyt.msgprocess.entity.Operation;
import com.xyt.msgprocess.entity.Type;
import com.xyt.msgprocess.receiver.NotificationReceiver;
import com.xyt.msgprocess.util.LogUtils;

public class MessageProcessingServer {
	private static Logger log = Logger.getLogger(MessageProcessingServer.class.getName());
	private static LogManager logManager = LogManager.getLogManager();
	
	private final String PROPERTIES_FILE = "application.properties";
	private final String PROPERTY_BROKER_URL="BROKER_URL";
	private final String PROPERTY_CONNECTION_FACTORY="CONNECTION_FACTORY";
	private final String PROPERTY_QUEUE_NAME = "QUEUE_NAME";
	private final String PROPERTY_CLIENT_NAME = "CLIENT_NAME";
	
	private Properties prop;
	private Connection connection = null;
	private BrokerService broker = null;
	private Session session = null;
	private Queue queue = null;
	
	public static void main(String[] args) {
		final MessageProcessingServer server = new MessageProcessingServer();
		
		try {
			server.startServer();
		} catch (Exception e) {
			log.severe("Message Processing Server start failed:" + LogUtils.extractExceptionStack(e));
		}
	}
	
	public void startServer() throws Exception {
		setupShutdownHook();
		initLog();
		loadAppProperties();
		createJMSConnection();
		waitTime();
		publishTestMsg();
			
	}

	private void waitTime() throws Exception {
		Thread.sleep(5000);
	}
	
	private void setupShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				try {
					log.info("Clean up resource");
					cleanup();
				} catch (Exception e) {
					log.severe("Exception occured when clean up resouce in cloing application." + LogUtils.extractExceptionStack(e));
				}
			}
		});
	}

	private void initLog() throws Exception {
		InputStream in = getClass().getClassLoader().getResourceAsStream("log.properties");
		logManager.readConfiguration(in);
		logManager.addLogger(log);
	}
	
	private void loadAppProperties() throws Exception{
		log.info("Loading application.properties");
		prop = new Properties();
		InputStream in = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
		prop.load(in);
	}
	
	public void createJMSConnection() throws Exception {
		log.info("Creating JMS Connection");
		broker = BrokerFactory.createBroker(new URI(prop.getProperty(PROPERTY_BROKER_URL)));
		broker.start();
		
		ConnectionFactory cf = new ActiveMQConnectionFactory(prop.getProperty(PROPERTY_CONNECTION_FACTORY));
		connection = cf.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = session.createQueue(prop.getProperty(PROPERTY_QUEUE_NAME));

		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new NotificationReceiver(prop.getProperty(PROPERTY_CLIENT_NAME)));
		connection.start();
		log.info("JMS Connection has been created, start receving message.");

	}
	
	public void cleanup() throws Exception {
		if (connection != null)
			connection.close();
		if (broker != null)
			broker.stop();
		if (session != null)
			session.close();
	}

	public void publishTestMsg() throws Exception {
		log.info("Start sending test message...");
		Gson gson = new Gson();
		MessageProducer producer = session.createProducer(queue);
		for (int i = 0; i < 65; i++) {
			Notification noti = new Notification();
			noti.setType(Type.SALES);
			noti.setProductType("apple");
			if (i > 5 && i < 10)
				noti.setProductType("cherry");

			if (i >= 10 && i < 15)
				noti.setProductType("mango");

			if (i >= 15 && i < 20) {
				noti.setProductType("coconut");
				noti.setOccurrence(i / 3);
			}
			if (i >= 20 && i < 30) {
				Adjustment adj = new Adjustment();
				adj.setOperation(Operation.ADD);
				adj.setValue(new BigDecimal(i / 3));
				noti.setAdjustment(adj);
			}

			if (i >= 30 && i < 40) {
				noti.setProductType("cherry");
				Adjustment adj = new Adjustment();
				adj.setOperation(Operation.SUBSTRACT);
				Random r = new Random();
				int x = r.nextInt(5);
				adj.setValue(new BigDecimal(x));
				noti.setAdjustment(adj);
			}

			if (i >= 40 && i < 50) {
				noti.setProductType("mango");
				Adjustment adj = new Adjustment();
				adj.setOperation(Operation.MULTIPLY);
				Random r = new Random();
				int radomInt = r.nextInt(5);
				adj.setValue(new BigDecimal(radomInt));
				noti.setAdjustment(adj);
			}

			if (i >= 50 && i < 55) {
				noti.setType(Type.PURCHASE);
			}

			if (i >= 55 && i < 60) {
				noti.setProductType("peach");
				Adjustment adj = new Adjustment();
				adj.setOperation(Operation.ADD);
				Random r = new Random();
				int radomInt = r.nextInt(5);
				adj.setValue(new BigDecimal(radomInt));
				noti.setAdjustment(adj);
			}
			if (i >= 60) {
				noti.setProductType("orange");
			}

			Random r = new Random();
			int radomInt = r.nextInt(8);
			noti.setValue(new BigDecimal(radomInt).add(new BigDecimal(i)));
			List<Notification> list = new ArrayList<Notification>();
			list.add(noti);
			String msg = gson.toJson(list);
			log.info(msg);
			producer.send(session.createTextMessage(msg));
		}
	}
}
