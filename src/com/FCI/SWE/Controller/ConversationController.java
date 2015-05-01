package com.FCI.SWE.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.ServicesModels.ConversationEntity;
import com.FCI.SWE.ServicesModels.MessageEntity;
import com.FCI.SWE.ServicesModels.UserEntity;

@Path("/msg")
@Produces("text/html")
public class ConversationController {
	@Context private HttpServletRequest request;
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/create")
	@Produces(MediaType.TEXT_PLAIN)
	public String createConv(@FormParam("users") List<String> users,@FormParam("uname") String uname) {
		String serviceUrl = "http://localhost:8888/rest/msg/CreateConversationService";

		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "uname="+uname+"&participants="+users.toString();
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
			JSONObject obj=(JSONObject)parser.parse(retJson);
			return "DONE";

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
		return "";
		
	}

	/**
	 * Action function to response to friend name query, This function will act
	 * as a controller part and it will calls search service to make
	 * registration
	 * 
	 * @param uname
	 *            provided user name
	 * @return Friendpage Viewable
	 */
	@GET
	@Path("/select")
	@Produces("text/html")
	public Response search() {
		// String serviceUrl =
		// "http://fci-emwy-socialnetworkapi.appspot.com/rest/searchService";
		String serviceUrl = "http://localhost:8888/rest/searchService";

		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "uname=*";
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

			Map<String, Vector<UserEntity>> map = new HashMap<String, Vector<UserEntity>>();
			Vector<UserEntity> users = new Vector<UserEntity>();

			for (int i = 0; i < arr.size(); i++) {

				JSONObject obj;
				obj = (JSONObject) arr.get(i);
				// System.out.println("hello"+obj.get("name").toString()+" "+obj.get("password").toString());

				users.add(UserEntity.getUser(obj.get("name").toString(), obj
						.get("password").toString()));
			}

			map.put("users", users);
			return Response.ok(new Viewable("/jsp/createConversation", map)).build();

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
		return null;
	}
	@POST
	@Path("/view")
	@Produces("text/html")
	public Response view(@FormParam("uname") String uname) {
		// String serviceUrl =
		// "http://fci-emwy-socialnetworkapi.appspot.com/rest/searchService";
		String serviceUrl = "http://localhost:8888/rest/msg/getUserConvsService";

		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "uname="+uname;
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("GET");
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
			request.getSession(true);
			request.setAttribute("uname", uname);
			System.out.println(retJson);
			ArrayList<String> rets=ConversationEntity.objectifyParticipants(retJson);
			Vector<String> convs=new Vector<>();
			Map<String, Vector<String>> map =new HashMap<>();
			for (String string : rets) {
				convs.add(string);
			}
			map.put("convs", convs);
			
			return Response.ok(new Viewable("/jsp/userConvs",map)).build();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return null;
	}
	@POST
	@Path("/send")
	@Produces("text/html")
	public String send(@FormParam("uname") String uname,@FormParam("content") String content,
			@FormParam("conv") String conv) {
		// String serviceUrl =
		// "http://fci-emwy-socialnetworkapi.appspot.com/rest/searchService";
		String serviceUrl = "http://localhost:8888/rest/msg/NewMessageService";

		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "uname="+uname+"&group="+conv+"&content="+content;
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
			
			return "OK";

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return null;
	}
	
}
