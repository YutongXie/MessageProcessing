1. Program name:
   Message Processing application
   
2. Program usage scope:
   J.P Morgan Java Technical Test
   
3. How to use:
   a. MessageProcessingServer.java is program startup. 
      responsible:
      --create MQ Connection
      --create message consumer
      --publish test message
      --Add shutdown hook(release resource)
      
   b. NotificationReceiver.java is MQ queue listener. Only accept TextMessage.
      Responsible:
      --parse message
      --classify message upon message type, like SALES, PURCHASE, etc
      --process message
      
   c.NotificationProcessor.java is processor, will create proper factory for creating service upon message type.
     using abstract factory pattern for entire flow in order to easy extension in future.
     
     Structure is:
     NotificationProcessorFactory.java
       -SalesNotificationProcessorFactory.java
       
     NotificationService.java
       -SalesNotificationService.java (Singleton pattern)
     
     ReportService.java
       -SalesReportService.java (Singleton pattern)


3. Interface: JSON
   -List<Notification>
   
   sample message:
	[{"type":"SALES","productType":"apple","value":62,"occurrence":0,"adjustment":{"operation":"ADD","value":3}},
	{"type":"SALES","productType":"cherry","value":8,"occurrence":0}
	]
	
4. Output:
   sample of report:
   ProductType	Number	TotalValue
	-----------	------	----------
	apple	28	1684
	cherry	4	34
	mango	4	54
	coconut	12	222
	
	sample of adjustment:
	ProductType	Operation	Value
	-----------	---------	-----
	apple	ADD	3
	cherry	SUBSTRATCT	3
	mango	MULTIPLY	3

5. how to store sales notification:
   -- memory using HashMap<String, List<Notification>>, not use HashTable as program is running in single thread environment
	
6. Dependence:
   --ActiveMQ-all: setup MQ Queue
   --GSON: create/parse JSON
   --Mockito: for unit test
   --Junit:   for unit test
   
7. Log:
   use Java util log with specific output format. Level is INFO.
   
8. Unit test:
   --use Junit + Mockito
   --using Java reflection for private method
   
9. Development environment:
   --Eclipse Neon(4.6.3)
   --JDK 1.8.0_121|64
   
10. How build:
   --Maven