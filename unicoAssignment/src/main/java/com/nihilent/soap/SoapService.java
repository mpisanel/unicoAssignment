/**
 * 
 */
package com.nihilent.soap;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.nihilent.jms.impl.JMSQueue;
import com.nihilent.util.MathUtil;

/**
 * @author hrishikesh.madur
 *
 */
@WebService
public class SoapService {

	/**
	 * Computes and returns the GCD of the first 2 Integers from the queue
	 * messages, also send it to another queue.
	 * 
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	public int gcd() throws Exception {
		int gcdNumber = 0;
		JMSQueue jmsQueue = JMSQueue.getInstance();
		try {
			List<Integer> messageList = jmsQueue.readMessage(2,
					jmsQueue.getIntegerListQueue());

			if (messageList != null && !messageList.isEmpty()) {
				if (messageList.size() == 2) {
					gcdNumber = MathUtil.gcd(messageList.get(0),
							messageList.get(1));
				} else {
					gcdNumber = MathUtil.gcd(messageList.get(0), 0);
				}
				List<Integer> gcdList = new ArrayList<Integer>();
				gcdList.add(gcdNumber);
				String status = jmsQueue.sendMessage(gcdList,
						jmsQueue.getGcdListQueue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gcdNumber;
	}

	/**
	 * Returns the list of computed GCDs
	 * 
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	public List<Integer> gcdList() throws Exception {

		List<Integer> result = new ArrayList<Integer>();
		try {
			JMSQueue jmsQueue = JMSQueue.getInstance();
			List<Integer> messageList = jmsQueue.readAllMessage(jmsQueue
					.getGcdListQueue());

			if (messageList != null && !messageList.isEmpty()) {
				result = messageList;
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * Returns the sum of all computed GCDs
	 * 
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	public int gcdSum() throws Exception {
		int sum = 0, i = 0;
		try {
			JMSQueue jmsQueue = JMSQueue.getInstance();
			List<Integer> messageList = jmsQueue.readAllMessage(jmsQueue
					.getGcdListQueue());

			if (messageList != null && !messageList.isEmpty()) {
				for (Integer gcdNumber : messageList)
					sum = sum + gcdNumber;

			}
		} catch (Exception e) {
			throw e;
		}

		return sum;
	}

}
