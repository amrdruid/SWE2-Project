package com.FCI.SWE.Models;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.FCI.SWE.ServicesModels.UserEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

public class PagePost extends Post {
	
	public UserEntity pageOwner ;
	public String pageName ;
	public int numOfSeen ;
	
	public PagePost(UserEntity user) {
		// TODO Auto-generated constructor stub
		super();
		this.owner = user ;
	}

	@Override
	public Boolean savePost() {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try{
			
			Entity entity = new Entity("pagePost");
			
			entity.setProperty("owner", owner.getName()); // post owner
			entity.setProperty("content", content);
			entity.setProperty("likes", 0);
			entity.setProperty("seen", 0);
			entity.setProperty("pageName", pageName);

			id = datastore.put(entity).getId();	
			
			txn.commit();
			
		}finally {
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
		if ( id == null )
			return false;
		
		return true;
	}
	
	public static String getAllPosts ( String pageName ){
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		Query gaeQuery = new Query("pagePost");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		JSONArray array = new JSONArray();
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		
		for ( Entity entity : list ){
			
			if ( entity.getProperty("pageName").toString().equals ( pageName ) ){
				
				JSONObject obj = new JSONObject();
				
				obj.put("owner", entity.getProperty("owner").toString());
				obj.put("content", entity.getProperty("content").toString());
				obj.put("seen", entity.getProperty("seen").toString());
				obj.put("likes", entity.getProperty("likes").toString());
				
				array.add(obj);
			}
		}
		
		return array.toString();
	}
	
	public static void like ( String key , String pageName ){
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		Query gaeQuery = new Query("pagePost");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		
		for ( Entity entity : list ){
			
			System.out.println( key + " === " + entity.getKey().toString() );
			
			if ( entity.getProperty("pageName").toString().equals ( pageName ) 
			  && entity.getKey().toString().equals ( key ) ){
				
				System.out.println( "yes" );
				
				entity.setProperty("likes", Integer.parseInt ( entity.getProperty("likes").toString() ) + 1 );
				datastore.put(entity);
			}
		}
	}
	
	public static void seen ( String pageName ){
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		Query gaeQuery = new Query("pagePost");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		
		for ( Entity entity : list ){
		
			if ( entity.getProperty("pageName").toString().equals ( pageName ) ){
				
				entity.setProperty("seen", Integer.parseInt ( entity.getProperty("seen").toString() ) + 1 );
				datastore.put(entity);
			}
		}
	}
}