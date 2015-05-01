package com.FCI.SWE.Models;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class FriendRequestsNotification extends AbstractNotification {
	@Override
	public ArrayList<String> getNotification(String uname){
		//return "sendername requested to be a friend with you ya prince ya prince";
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("friendrequests");
		
		PreparedQuery pq = datastore.prepare(gaeQuery);
		ArrayList<String> reqs=new ArrayList<>();
		for (Entity entity : pq.asIterable()) {
			
			//System.out.println(entity.getProperty("name").toString());
			if ( entity.getProperty("friendname").toString().equals(uname)  && entity.getProperty("status").toString().equals("false")) {
				
				reqs.add(entity.getProperty("sendername")
						+" requested to be a friend with you ya prince ya prince");
			}
		}

		return reqs;
	}
}