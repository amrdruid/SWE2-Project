package com.FCI.SWE.Models;

import com.FCI.SWE.ServicesModels.UserEntity;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UserPost extends Post {

	public String feelings;
	public String timeline;
	public String privacy;
	public String myPost;

	public UserPost(UserEntity owner) {
		super();
		// TODO Auto-generated constructor stub
		this.owner = owner;
	}
	
	public String getFeelings(){
		return feelings;
	}
	
	public String getPost(){
		return myPost;
	}
	
	public String getPrivacy(){
		return privacy;
	}
	
	public String getTimeline(){
		return timeline;
	}

	@Override
	public Boolean savePost() {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();

		try {

			Entity entity = new Entity("userPost");

			entity.setProperty("owner", owner.getName());
			entity.setProperty("content", content);
			entity.setProperty("privacy", privacy);
			entity.setProperty("feelings", feelings);
			entity.setProperty("likes", 0);
			entity.setProperty("timeline", timeline);
			id = datastore.put(entity).getId();

			txn.commit();

		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		if (id == null)
			return false;

		return true;
	}

	public static String getAllHashtagedPosts(String hashtag) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("userPost");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		JSONArray array = new JSONArray();
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : list) {
			if (entity.getProperty("content").toString().contains(hashtag)) {
				JSONObject obj = new JSONObject();

				obj.put("owner", entity.getProperty("owner").toString());
				obj.put("content", entity.getProperty("content").toString());
				obj.put("feelings", entity.getProperty("feelings").toString());
				obj.put("privacy", entity.getProperty("privacy").toString());
				obj.put("likes", entity.getProperty("likes").toString());

				array.add(obj);

			}
		}

		return array.toString();
	}

	public static String getAllPosts(String uname, String timeline) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("userPost");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		JSONArray array = new JSONArray();
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : list) {

			System.out.println(UserEntity.isFriend(uname, timeline) + " "
					+ uname + " ** " + timeline);

			if (entity.getProperty("timeline").toString().equals(timeline)
					&& ((UserEntity.isFriend(uname, timeline) && entity
							.getProperty("privacy").toString().equals("friends")) || entity
							.getProperty("privacy").toString().equals("public")) || uname.equals(entity
									.getProperty("timeline").toString())) {
				
				//System.out.println("Is it even close ? ");
				
				JSONObject obj = new JSONObject();
				
				obj.put("owner", entity.getProperty("owner").toString());
				obj.put("content", entity.getProperty("content").toString());
				obj.put("feelings", entity.getProperty("feelings").toString());
				obj.put("privacy", entity.getProperty("privacy").toString());
				obj.put("likes", entity.getProperty("likes").toString());

				array.add(obj);
			}
		}

		return array.toString();
	}

	public static void like(String key, String timeline) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("userPost");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : list) {

			if (entity.getProperty("timeline").toString().equals(timeline)
					&& entity.getKey().toString().equals(key)) {

				entity.setProperty("likes", Integer.parseInt(entity
						.getProperty("likes").toString()) + 1);
				datastore.put(entity);
			}
		}
	}
}