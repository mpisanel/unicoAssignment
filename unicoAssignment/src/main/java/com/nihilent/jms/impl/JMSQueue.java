/**
 * 
 */
package com.nihilent.jms.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.nihilent.util.Constants;

/**
 * @author hrishikesh.madur
 *
 */
public class JMSQueue {

	private static JMSQueue jmsQueue = null;

	private Queue integerListQueue;

	private Queue gcdListQueue;

	private ConnectionFactory connectionFactory;

	public static synchronized JMSQueue getInstance() {
		if (jmsQueue == null) {
			jmsQueue = new JMSQueue();
		}
		return jmsQueue;
	}

	private JMSQueue() {
		init();
	}

	public void init() {
		try {
			InitialContext jndi = new InitialContext();
			connectionFactory = (QueueConnectionFactory) jndi
					.lookup(Constants.JMS_QUEUE_CONNECTION_FACTORY);
			integerListQueue = (Queue) jndi.lookup(Constants.JMS_QUEUE_NAME);
			gcdListQueue = (Queue) jndi
					.lookup(Constants.JMS_GCD_LIST_QUEUE_NAME);
		} catch (NamingException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This method sends the messages from List to specified queue 
	 * @param messageList list of messages need to send
	 * @param queue Queue from where messages to be read
	 * @return
	 */
	public String sendMessage(List<Integer> messageList, Queue queue) {
		String status = "failure";
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(queue);
			for (Integer message : messageList) {
				ObjectMessage tm = session.createObjectMessage(message);
				messageProducer.send(tm);
			}
			status = "success";

		} catch (JMSException ex) {
			ex.printStackTrace();
		} finally {
			closeConnections(session, connection);
		}
		return status;
	}

	/**
	 * This method reads the specified number of messages from specified queue 
	 * @param numberOfMesages number of messages to read
	 * @param queue Queue from where messages to be read
	 * @return
	 */
	public List<Integer> readMessage(int numberOfMesages, Queue queue) {
		List<Integer> messageList = new ArrayList<Integer>();
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = queue;
			MessageConsumer consumer = session.createConsumer(dest);
			connection.start();
			while (numberOfMesages > 0) {
				Message m = consumer.receive(1);
				if (m != null) {
					ObjectMessage message = (ObjectMessage) m;
					messageList.add((Integer) message.getObject());
					numberOfMesages--;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			closeConnections(session, connection);
		}
		return messageList;
	}

	/**
	 * This method reads the all messages from specified queue
	 * @param queue Queue from where messages to be read
	 * @return
	 */
	public List<Integer> readAllMessage(Queue queue) {
		List<Integer> messageList = new ArrayList<Integer>();
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Destination dest = queue;
			//MessageConsumer consumer = session.createConsumer(dest);
			
			QueueBrowser queueBrowser = session.createBrowser(queue); 
			
			connection.start();
			Enumeration messageEnumeration = queueBrowser.getEnumeration();
			while (messageEnumeration.hasMoreElements()) {
				//Message m = consumer.receive(1000);
				ObjectMessage message = (ObjectMessage)messageEnumeration.nextElement();
//				if (m != null) {
//					ObjectMessage message = (ObjectMessage) m;
					messageList.add((Integer) message.getObject());
//				} else {
//					break;
//				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			closeConnections(session, connection);
		}
		return messageList;
	}

	private void closeConnections(Session session, Connection connection) {
		if (session != null) {
			try {
				session.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @return the integerListQueue
	 */
	public Queue getIntegerListQueue() {
		return integerListQueue;
	}

	/**
	 * @return the gcdListQueue
	 */
	public Queue getGcdListQueue() {
		return gcdListQueue;
	}

}
