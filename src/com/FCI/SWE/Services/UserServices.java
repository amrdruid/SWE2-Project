package com.FCI.SWE.Services;

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

import javax.swing.text.StyledEditorKit.BoldAction;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.HashtagsManager;
import com.FCI.SWE.Models.User;
import com.FCI.SWE.Models.UserPost;
import com.FCI.SWE.ServicesModels.PageEntity;
import com.FCI.SWE.ServicesModels.UserEntity;

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
@Produces(MediaType.TEXT_PLAIN)
public class UserServices {
	
	
	/*@GET
	@Path("/index")
	public Response index() {
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}*/


		/**
	 * Registration Rest service, this service will be called to make
	 * registration. This function will store user data in data store
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided password
	 * @return Status json
	 */
	@POST
	@Path("/RegistrationService")
	public String registrationService(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {
		UserEntity user = new UserEntity(uname, email, pass);
		user.saveUser();
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}

	/**
	 * Login Rest Service, this service will be called to make login process
	 * also will check user data and returns new user from datastore
	 * @param uname provided user name
	 * @param pass provided user password
	 * @return user in json format
	 */
	
	@POST
	@Path("/sendrequestservice")
	public String searchservice(@FormParam("email") String email,
			@FormParam ("myemail") String from){
		//System.out.println(from);
		JSONObject object = new JSONObject();
		Boolean user = UserEntity.search(email,from);
		if (user == false) {
			object.put("Status", "Failed");

		} else {
			object.put("Status", "OK");
		}
		return object.toString();

	}
	
	
	@POST
	@Path("/viewrequestservice")
	public String viewservice(@FormParam("myemail") String email){
		
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.view(email);
		if (user == null) {
			object.put("Status", "Failed");

		} else {
			object.put("Status","OK");

			object.put("email",user.getEmail());
		}
		return object.toString();

	}
	
	@POST
	@Path("/acceptrequestservice")
	public String acceptservice(@FormParam("to") String to,
			@FormParam("from") String from){
		
		JSONObject object = new JSONObject();
		boolean user = UserEntity.accept(from,to);
		if (user == false) {
			object.put("Status", "Failed");

		} else {
			object.put("Status","OK");

		
		}
		return object.toString();

	}
	
	
	@POST
	@Path("/LoginService")
	public String loginService(@FormParam("uname") String uname,
			@FormParam("password") String pass) {
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.getUser(uname, pass);
		if (user == null) {
			object.put("Status", "Failed");

		} else {
			object.put("Status", "OK");
			object.put("name", user.getName());
			object.put("email", user.getEmail());
			object.put("password", user.getPass());
			object.put("id", user.getId());
		}
		return object.toString();

	}
	
	@POST
	@Path("/timeline")
	public String timeline(@FormParam("timeline") String timeline ) {
		String uname = User.getCurrentActiveUser().getName();
		//System.out.println("I entered here in service now ! - Email : " + timeline);
		return UserPost.getAllPosts ( uname , timeline ) ;
	}
	
	@POST
	@Path("/createPost")
	public String createPost( 
						  @FormParam("content") String content ,
						  @FormParam("privacy") String privacy , 
						  @FormParam("feelings") String feelings ,
						  @FormParam("timeline") String timeline) {
		HashtagsManager.extractHashtags(content);
		HashtagsManager.top10Hashtags();
 
		Boolean creationCheck = false;
		
		String uname = User.getCurrentActiveUser().getName();
		
		System.out.println("Controller : " + uname);
		
		UserEntity user = UserEntity.searchSingleUser(uname);
		
		UserPost p = new UserPost( user );
		p.content = content ;
		p.privacy = privacy ;
		p.feelings = feelings ;
		p.timeline = timeline ;

		creationCheck = p.savePost();

		JSONObject obj = new JSONObject();

		if ( creationCheck && user != null )
			obj.put("status", "ok");
		
		else
			obj.put("status", "failed");

		return obj.toJSONString();
	}
	
	@POST
	@Path("/createPage")
	public String createPage( 
			  @FormParam("name") String name ,
			  @FormParam("cat") String cat ) {
		
		String uname = User.getCurrentActiveUser().getName();
		System.out.println(uname);
		
		UserEntity user = UserEntity.searchSingleUser(uname);
		PageEntity pg = new PageEntity();
		String creationCheck = "/";
		
		pg.cat=cat;
		pg.name=name;
		pg.owner=user;
		pg.savePage();
		creationCheck = pg.savePage();

		JSONObject obj = new JSONObject();

		if ( creationCheck.equals("OK") && user != null )
			obj.put("status", "ok");
		
		else
			obj.put("status", "failed");

		return obj.toJSONString();
	}
	
	@POST
	@Path("/like")
	public void like( @FormParam("key") String key , @FormParam("timeline") String pageName ) {
		UserPost.like ( key , pageName ) ;
	}
	
	
}