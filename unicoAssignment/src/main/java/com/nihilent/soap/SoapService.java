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

	@WebMethod
	public int gcd() throws Exception {
		int gcdNumber = 0;
		JMSQueue jmsQueue = JMSQueue.getInstance();
		try {
			List<Integer> messageList = jmsQueue.readMessage(2);
			System.out.println(" messageList : " + messageList);
			if (messageList != null && !messageList.isEmpty()) {
				if (messageList.size() == 2) {
					gcdNumber = MathUtil.gcd(messageList.get(0),
							messageList.get(1));
				} else {
					gcdNumber = MathUtil.gcd(messageList.get(0), 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gcdNumber;
	}

	@WebMethod
	public List<Integer> gcdList() throws Exception {

		List<Integer> result = new ArrayList<Integer>();
		try {
			JMSQueue jmsQueue = JMSQueue.getInstance();
			List<Integer> messageList = jmsQueue.readAllMessage();

			System.out.println(" messageList : " + messageList);
			if (messageList != null && !messageList.isEmpty()) {
				int i = 0;
				while (i < messageList.size()) {
					if (i == messageList.size()) {
						result.add((Integer) MathUtil.gcd(messageList.get(i), 0));
					} else {
						result.add((Integer) MathUtil.gcd(messageList.get(i),
								messageList.get(i + 1)));
					}

					i = i + 2;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@WebMethod
	public int gcdSum() throws Exception {
		int sum = 0, i = 0;
		try {
			JMSQueue jmsQueue = JMSQueue.getInstance();
			List<Integer> messageList = jmsQueue.readAllMessage();
			System.out.println(" messageList : " + messageList);
			if (messageList != null && !messageList.isEmpty()) {
				while (i < messageList.size()) {
					if (i == messageList.size()) {
						sum = sum
								+ (Integer) MathUtil.gcd(messageList.get(i), 0);
					} else {
						sum = sum
								+ (Integer) MathUtil.gcd(messageList.get(i),
										messageList.get(i + 1));
					}
					i = i + 2;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sum;
	}

}
