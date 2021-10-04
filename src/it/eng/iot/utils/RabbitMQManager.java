package it.eng.iot.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import it.eng.iot.configuration.ConfRabbitMQ;

public abstract class RabbitMQManager {
	
	public static Connection createConnection() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory(); //Creates a new instance of the TCP connection between the application and the RabbitMQ broker.
		// Sets the properties of the connection, fetching the values from the configuration.properties
		factory.setUsername(ConfRabbitMQ.getInstance().getString("RMQUsername"));
		factory.setPassword(ConfRabbitMQ.getInstance().getString("RMQPass"));
		factory.setVirtualHost(ConfRabbitMQ.getInstance().getString("RMQVHost")); // Vhost can differentiate access privileges, queues and exchanges
		factory.setHost(ConfRabbitMQ.getInstance().getString("RMQHost"));
		factory.setPort(Integer.valueOf(ConfRabbitMQ.getInstance().getString("RMQPort")));
		Connection connection = factory.newConnection();
		return connection;
	}
	
	public static void sendToQueue(String queue, Object entity) {
		try (Connection connection = createConnection();
			 Channel channel = connection.createChannel()){ // Creates a channel in the TCP connection
	            String exchange = ConfRabbitMQ.getInstance().getString("RMQExchange");
	            channel.exchangeDeclare(exchange, "direct"); // Defines a mode which uses context to route
	            String message = entity.toString(); // A message which should be published
	            channel.queueDeclare(queue, true, false, false, null);
	            channel.queueBind(queue, exchange, queue); //Binds the queue to the exchange
	            channel.basicPublish(exchange, queue, null, message.getBytes("UTF-8"));
			 } 
		catch (IOException e1) {
			e1.printStackTrace();
		} 
		catch (TimeoutException e1) {
			e1.printStackTrace();
		}	
	}
}
