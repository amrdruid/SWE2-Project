package com.FCI.SWE.Models;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class NewMessageNotification extends AbstractNotification {
	
	
	@Override
	public ArrayList<String> getNotification(String uname) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("messages");
		
		PreparedQuery pq = datastore.prepare(gaeQuery);
		Query gaeQuery1 = new Query("conversations");
		
		PreparedQuery pq1 = datastore.prepare(gaeQuery1);
		ArrayList<String> reqs=new ArrayList<>();
		for (Entity msg : pq.asIterable()) {
			for (Entity conv : pq1.asIterable()) {
				if(msg.getProperty("conversation").toString().equals(
						conv.getProperty("name").toString()
						) && conv.getProperty("participants").toString().contains(uname) ){
					//System.out.println("helahoba");
					reqs.add("You have a new message from "+msg.getProperty("sender").toString()
							+" in conversation "+msg.getProperty("conversation").toString());
				}
				
			}
		}
		//System.out.println(reqs.toString());
		return reqs;
	}

}