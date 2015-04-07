/**
 * 
 */
package com.nihilent.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.nihilent.jms.impl.JMSQueue;

/**
 * @author hrishikesh.madur
 *
 */
@Path("restService")
public class RestService {

	@PUT
	@Produces("application/json")
	@Path("push/{int1}/{int2}")
	public String push(@PathParam("int1") int i1, @PathParam("int2") int i2) {
		String status = null;

		System.out.println("Input values are :: " + i1 + " : " + i2);

		List<Integer> messageList = new ArrayList<Integer>();

		messageList.add(i1);
		messageList.add(i2);		

		JMSQueue jmsQueue = JMSQueue.getInstance();

		status = jmsQueue.sendMessage(messageList);

		status = "success";

		return status;
	}

	@GET
	@Produces("application/json")
	@Path("list")
	public List<Integer> list() {
		List<Integer> messageList = new ArrayList<Integer>();

		JMSQueue jmsQueue = JMSQueue.getInstance();

		messageList = jmsQueue.readAllMessage();

		return messageList;
	}

}
