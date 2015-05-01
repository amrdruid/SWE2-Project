package com.FCI.SWE.ServicesModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * <h1>User Entity class</h1>
 * <p>
 * This class will act as a model for user, it will holds user data
 * </p>
 *
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 */
public class ConversationEntity {
	
	private String conversationName;
	private ArrayList<String> participants;
	
	/**
	 * Constructor accepts message data
	 * 
	 * @param MessageName
	 *            msg name
	 * @param participants
	 *            arrayList
	 */
	public ConversationEntity(String messageName,ArrayList<String> participants) {
		this.conversationName = messageName;
		this.participants = participants;
	}
	
	
	/**
	 * 
	 * This static method will form UserEntity class using user name and
	 * password This method will serach for user in datastore
	 * 
	 * @param name
	 *            user name
	 * @param pass
	 *            user password
	 * @return Constructed user entity
	 */

	public static ConversationEntity getConversation(String name) {
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("conversations");
		
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		for (Entity entity : pq.asIterable()) {
			
			//System.out.println(entity.getProperty("name").toString());
			if ( entity.getProperty("name").toString().equals(name) ) {
				
				ConversationEntity returnedConv = new ConversationEntity(entity.getProperty(
						"name").toString(), objectifyParticipants(entity.getProperty("participants").toString()));
				
				return returnedConv;
			}
		}

		return null;
	}
	
public static ArrayList<String> getUserConversations(String name) {
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("conversations");
		
		PreparedQuery pq = datastore.prepare(gaeQuery);
		ArrayList<String> returned=new ArrayList<>();
		for (Entity entity : pq.asIterable()) {
			
			//System.out.println(entity.getProperty("name").toString());
			if ( entity.getProperty("participants").toString().contains(name) ) {
				
				ConversationEntity returnedConv = new ConversationEntity(entity.getProperty(
						"name").toString(), objectifyParticipants(entity.getProperty("participants").toString()));
				
				returned.add(returnedConv.getName());
			}
		}

		return returned;
	}

	static public ArrayList<String> objectifyParticipants(String val){
		ArrayList<String> result;
		val=val.substring(1, val.length()-1);
		//val.replace(" ", "");
		result=new ArrayList<>(Arrays.asList(val.split(", ")));
		return result;
	}
	/**
	 * This method will be used to save conversation object in datastore
	 * 
	 * @return boolean if conversation is saved correctly or not
	 */
	public Boolean saveConversation() {
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		Query gaeQuery = new Query("conversations");
		
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity msg = new Entity("conversations", list.size() + 1);

		msg.setProperty("name", this.conversationName);
		msg.setProperty("participants", this.participants);

		datastore.put(msg);

		return true;
	}
	@Override
	public String toString(){
		return "Conversation name: "+conversationName+" Participants: "+participants.toString()+"\n"; 
	}


	public String getName() {
		// TODO Auto-generated method stub
		return this.conversationName;
	}
	public ArrayList<String> getParticipants(){
		return this.participants;
	}

}