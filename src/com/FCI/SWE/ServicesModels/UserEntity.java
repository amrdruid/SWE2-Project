package com.FCI.SWE.ServicesModels;

import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
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
public class UserEntity {
	private String name;
	private String email;
	private String activeUserEmail;
	private String password;
	private long id;

	/**
	 * Constructor accepts user data
	 * 
	 * @param name
	 *            user name
	 * @param email
	 *            user email
	 * @param password
	 *            user provided password
	 */
	public UserEntity(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public UserEntity(String email) {	
		this.email = email;
	}
	
	public UserEntity(String email1 , String email2){
		activeUserEmail = email1;
		email = email2;
	}
	
	private void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPass() {
		return password;
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

	public static UserEntity getUser(String name, String pass) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("name").toString().equals(name)
					&& entity.getProperty("password").toString().equals(pass)) {
				UserEntity returnedUser = new UserEntity(entity.getProperty(
						"name").toString(), entity.getProperty("email")
						.toString(), entity.getProperty("password").toString());
				returnedUser.setId(entity.getKey().getId());
				System.out.println(returnedUser);
				return returnedUser;
			}
		}

		return null;
	}

	
	public static String getUserFromRequests(String myemail){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
				
		Query gaeQuery = new Query("requests");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for(Entity entity : pq.asIterable()){
			if(entity.getProperty("to").toString().equals(myemail)){
				//System.out.println("Entered the gufr function");
				String returnEmail = entity.getProperty("from").toString();
				
				String magdy = entity.getProperty("statues").toString();
				
				if(magdy.equals("accept") == false){
					System.out.println(magdy + " " + returnEmail);
					return returnEmail;
				}
			}
		}
		
		System.out.println("not found man !");
		return "";
	}
	
	/**
	 * This method will be used to save user object in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public Boolean saveUser() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity employee = new Entity("users", list.size() + 1);

		employee.setProperty("name", this.name);
		employee.setProperty("email", this.email);
		employee.setProperty("password", this.password);
		datastore.put(employee);

		return true;

	}
	public static Boolean search(String email,String from) {
		//System.out.print(email);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		Query gaeQuery1 = new Query("requests");
		PreparedQuery pq1 = datastore.prepare(gaeQuery);
		List<Entity> list = pq1.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(email)) {
				Entity employee = new Entity("requests", list.size() + 1);

				employee.setProperty("from",from);
				employee.setProperty("to", email);
				employee.setProperty("statues", "send");
				datastore.put(employee);
				return true;
			}
			
		}
		return false ;		
	}
	
	public static UserEntity view(String email) {
		//System.out.print(email);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("requests");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("from").toString().equals(email)
				&& entity.getProperty("statues").toString().equals("send")){
					UserEntity returnedUser =
					new UserEntity(entity.getProperty("from").toString());
				return returnedUser;	
				
			}
			
		}
		return null ;		
	}
	
	public static boolean accept(String to,String from) {
	
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("requests");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("from").toString().equals(from)
				&& entity.getProperty("to").toString().equals(to) ){
				entity.setProperty("statues","accept");
				datastore.put(entity);
				return true;
			}
			
		}
		return false ;		
	}
	
}
