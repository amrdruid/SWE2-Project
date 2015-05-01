package com.FCI.SWE.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class HashtagsManager {

	static public int extractHashtags(String content){
		Pattern MY_PATTERN = Pattern.compile("#(\\w+|\\W+)");
		Matcher mat = MY_PATTERN.matcher(content);
		List<String> str=new ArrayList<String>();
		while (mat.find()) {
		  //System.out.println(mat.group(1));
		  str.add(mat.group(1));
		}
		System.out.println(str.toString());
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("hashtags");
		
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		for (int i = 0; i < str.size(); i++) {
			boolean flag=false;
			for (Entity entity : pq.asIterable()) {
				
				//System.out.println(entity.getProperty("name").toString());
				if ( entity.getProperty("name").toString().equals(str.get(i)) ) {
					entity.setProperty("count", 
							Integer.parseInt(entity.getProperty("count").toString())+1);
					datastore.put(entity);
					flag=true;
				}
			}
			if(!flag){
				Entity hashtag = new Entity("hashtags", list.size() + 1);
				hashtag.setProperty("name", str.get(i));
				hashtag.setProperty("count",0);
				datastore.put(hashtag);
			}
		}
		return 0;
	}
	
	public static String searchHashtags(String hashtag){
		return UserPost.getAllHashtagedPosts(hashtag);
	}
	
	public static String top10Hashtags(){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("hashtags").addSort("count", SortDirection.DESCENDING);
		
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		if(list.size()>10)
			return (list.subList(0, 10).toString());
		else{
			return (list.toString());
		}
	}
	public static void main(String[] args) {
		//top10Hashtags();
	}
}