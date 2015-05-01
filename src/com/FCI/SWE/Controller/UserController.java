package com.FCI.SWE.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;

import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.User;
import com.FCI.SWE.Models.UserPost;
import com.FCI.SWE.ServicesModels.UserEntity;
import com.google.appengine.api.search.query.ExpressionParser.negation_return;
//import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;

/**
 * This class contains REST services, also contains action function for web
 * application
 * 
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 *
 */
@Path("/")
@Produces("text/html")
public class UserController {
	/**
	 * Action function to render Signup page, this function will be executed
	 * using url like this /rest/signup
	 * 
	 * @return sign up page
	 */
	User activeuser= new User();

	@GET
	@Path("/signup")
	public Response signUp() {
		return Response.ok(new Viewable("/jsp/register")).build();
	}

	/**
	 * Action function to render home page of application, home page contains
	 * only signup and login buttons
	 * 
	 * @return enty point page (Home page of this application)
	 */
	@GET
	@Path("/")
	public Response index() {
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}
	
//	@GET
//	@Path("/sendMessage")
//	public Response sendMessage() {
//		return Response.ok(new Viewable("/jsp/sendMessage")).build();
//	}
	
	
	@POST
	@Path("/Logout")
	public Response signout() {
		activeuser=null;
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}

	
	@POST
	@Path("/search")
	public String Sendrequest(@FormParam("email") String email) {
		String urlParameters = "email="+email+"&myemail="+User.getCurrentActiveUser().getEmail();

		String retJson = Connection.connect(
				"http://localhost:8888/rest/sendrequestservice", urlParameters,
				"POST", "application/x-www-form-urlencoded;charset=UTF-8");

		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").toString().equals("Failed")){
				return "not found";
			}
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Request has been sent ";
	}

	
	@POST
	@Path("/view")
	public Response viewrequest() {
		
		String urlParameters = "myemail="+User.getCurrentActiveUser().getEmail();

		String retJson = Connection.connect(
				"http://localhost:8888/rest/viewrequestservice", urlParameters,
				"POST", "application/x-www-form-urlencoded;charset=UTF-8");

		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").toString().equals("Failed")){
				return null;
			}
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("email", object.get("email").toString());
			return Response.ok(new Viewable("/jsp/view", map)).build();
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//activeuser.setEmail(user.getEmail());
		return Response.ok(new Viewable("/jsp/home")).build();
	}

	
	@POST
	@Path("/accept")
	public String acceptrequest(@FormParam("email") String to) {
		
		to = User.getCurrentActiveUser().getEmail();
		System.out.println("THIS IS TOOO : " + to);
		
		String urlParameters = "to="+to+"&from="+User.getCurrentActiveUser().getEmail();

		String retJson = Connection.connect(
				"http://localhost:8888/rest/viewtrequestservice", urlParameters,
				"POST", "application/x-www-form-urlencoded;charset=UTF-8");

		UserEntity myUser = new UserEntity(to);
		String from = myUser.getUserFromRequests(to);

		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").toString().equals("Failed") || from == ""){
				return null;
			}
			
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//activeuser.setEmail(user.getEmail());
		

		
		System.out.println("Got Here "  + to + " " + from);
		myUser.accept(to, from);
		return "Success";
	}
	
	/**
	 * Action function to render login page this function will be executed using
	 * url like this /rest/login
	 * 
	 * @return login page
	 */
	@GET
	@Path("/login")
	public Response login() {
		return Response.ok(new Viewable("/jsp/login")).build();
	}

	/**
	 * Action function to response to signup request, This function will act as
	 * a controller part and it will calls RegistrationService to make
	 * registration
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided user password
	 * @return Status string
	 */
	
	@POST
	@Path("/response")
	@Produces(MediaType.TEXT_PLAIN)
	public String response(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {

		String serviceUrl = "http://localhost:8888/rest/RegistrationService";
		String urlParameters = "uname=" + uname + "&email=" + email
				+ "&password=" + pass;
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			// System.out.println(retJson);
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Registered Successfully";

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */
		return "Failed";
	}
	
	@POST
	@Path("/responseMessage")
	@Produces(MediaType.TEXT_PLAIN)
	public String MessageResponse(@FormParam("email")String to,
	@FormParam("text")String text){
		
		return "failed";
	}

	/**
	 * Action function to response to login request. This function will act as a
	 * controller part, it will calls login service to check user data and get
	 * user from datastore
	 * 
	 * @param uname
	 *            provided user name
	 * @param pass
	 *            provided user password
	 * @return Home page view
	 */
	@POST
	@Path("/home")
	@Produces("text/html")
	public Response home(@FormParam("uname") String uname,
			@FormParam("password") String pass) {
		String urlParameters = "uname=" + uname + "&password=" + pass;
		//System.out.println("Here i stand");
		String retJson = Connection.connect(
				"http://localhost:8888/rest/LoginService", urlParameters,
				"POST", "application/x-www-form-urlencoded;charset=UTF-8");

		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			
			if (object.get("Status").equals("Failed"))
				return null;
			Map<String, String> map = new HashMap<String, String>();
			User user = User.getUser(object.toJSONString());
			map.put("name", user.getName());
			map.put("email", user.getEmail());
			//activeuser.setEmail(user.getEmail());
			return Response.ok(new Viewable("/jsp/home", map)).build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */
		return null;
	}
	
	@POST
	@Path("/addpage")
	@Produces(MediaType.TEXT_PLAIN)
	public String createPage(@FormParam("uname") String uname , 
			  @FormParam("name") String name ,
			  @FormParam("cat") String cat ) {

		//String serviceUrl = "http://fci-emwy-socialnetworkapi.appspot.com/rest/acceptFriendService";
		String serviceUrl = "http://localhost:8888/rest/createPage";
		System.out.println("controller");
		
//		String retJson1 = Connection.connect(
//				"http://localhost:8888/rest/createPage", serviceUrl,
//				"POST", "application/x-www-form-urlencoded;charset=UTF-8");
//		
//		JSONParser parser1 = new JSONParser();
//		Object obj1;
		
		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "uname=" + uname + "&name="
					+ name + "&cat=" + cat;
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000); // 60 Seconds
			connection.setReadTimeout(60000); // 60 Seconds
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {

				retJson += line;
			}
			writer.close();
			reader.close();

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			
			if (object.get("status").equals("ok")) {

				return "Page Created Successfully";
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */
		return "Failed";
	}
	
	@POST
	@Path("/addpost")
	@Produces(MediaType.TEXT_PLAIN)
	public String addpost( 
			  @FormParam("content") String content ,
			  @FormParam("privacy") String privacy , 
			  @FormParam("feelings") String feelings ,
			  @FormParam("timeline") String timeline) {

		String uname = User.getCurrentActiveUser().getName();
		
		//String serviceUrl = "http://fci-emwy-socialnetworkapi.appspot.com/rest/acceptFriendService";
		String serviceUrl = "http://localhost:8888/rest/createPost";
		System.out.println("controller");
		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "uname=" + uname + "&content="
					+ content + "&privacy=" + privacy+"&feelings=" + feelings+"&timeline=" + timeline;
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000); // 60 Seconds
			connection.setReadTimeout(60000); // 60 Seconds
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {

				retJson += line;
			}
			writer.close();
			reader.close();

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			
			if (object.get("status").equals("ok")) {

				return "Post Sent Successfully";
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */
		return "Failed";
	}
	
	@POST
	@Path("/timeline")
	@Produces("text/html")
	public Response viewTimeline(@FormParam("timeline") String timeline){

		String uname = User.getCurrentActiveUser().getName();
		
		System.out.println("I entered here ! ,, Email : " + timeline);
		
		String serviceUrl = "http://localhost:8888/rest/timeline";
		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "uname="+uname+"&timeline="+timeline;
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000); // 60 Seconds
			connection.setReadTimeout(60000); // 60 Seconds
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {

				retJson += line;
			}
			writer.close();
			reader.close();
			
			JSONParser parser = new JSONParser();
			JSONArray arr = (JSONArray) parser.parse(retJson);
 
			Map<String, Vector<UserPost>> map = new HashMap<String, Vector<UserPost>>();
			Vector<UserPost> posts = new Vector<UserPost>() ;
			
			for (int i = 0 ; i < arr.size() ; i++){
 
				JSONObject obj;
				obj = (JSONObject)arr.get(i);
				
				UserPost thispost=new UserPost(UserEntity.searchSingleUser(obj.get("owner").toString()));
				thispost.timeline=obj.get("content").toString();
				thispost.feelings=obj.get("feelings").toString();
				thispost.privacy=obj.get("privacy").toString();
				thispost.likes=Integer.parseInt(obj.get("likes").toString());
				posts.add( thispost );
				
				System.out.println("Back to controller baby !!");
				System.out.println(thispost.timeline);
			}
 
			map.put ( "posts" , posts );
			return Response.ok(new Viewable("/jsp/timeline" , map)).build();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */
		return null;
	}
	
	
	
	

}