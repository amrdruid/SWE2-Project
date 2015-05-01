package com.FCI.SWE.ServicesModels;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

public class PageEntity {
	
	public String name ;
	public int likes ;
	public String cat ;
	public int reach ;
	public UserEntity owner ;
	
	public PageEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public String savePage(){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		Query gaeQuery = new Query("page");
		
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity pg = new Entity("page", list.size() + 1);

		pg.setProperty("name", name);
		pg.setProperty("owner", owner.getName());
		pg.setProperty("cat", cat);
		pg.setProperty("likes", 0);
		pg.setProperty("reach", 0);

		datastore.put(pg);

		return "OK";
	}
}