package com.FCI.SWE.ServicesModels;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class MessageEntity {
	private ConversationEntity conversationEntity;
	private String sender;
	private String content;
	public MessageEntity(String sender,String content,ConversationEntity conversationEntity) {
		// TODO Auto-generated constructor stub
		this.sender=sender;
		this.content=content;
		this.conversationEntity=conversationEntity;
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

	public static ArrayList<MessageEntity> getMessages(ConversationEntity conv) {
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("messages");
		ArrayList<MessageEntity> returnedMsgs=new ArrayList<>();
 		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		for (Entity entity : pq.asIterable()) {
			
			//System.out.println(entity.getProperty("name").toString());
			if ( entity.getProperty("conversation").toString().equals(conv.getName()) ) {
				
				
				MessageEntity ms=new MessageEntity(entity.getProperty(
						"sender").toString(), entity.getProperty(
						"content").toString(), conv);
				returnedMsgs.add(ms);
			}
		}

		return returnedMsgs;
	}
	
	/**
	 * This method will be used to save conversation object in datastore
	 * 
	 * @return boolean if conversation is saved correctly or not
	 */
	public Boolean saveMessage() {
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		Query gaeQuery = new Query("messages");
		
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity msg = new Entity("messages", list.size() + 1);
		
		msg.setProperty("sender", this.sender);
		msg.setProperty("content", this.content);
		msg.setProperty("conversation",this.conversationEntity.getName());
		datastore.put(msg);
		
		
		return true;
	}
	@Override
	public String toString(){
		return "Conversation name: "+conversationEntity.getName()+" Sender: "+sender+
				" Content: "+ content+"\n"; 
	}
}