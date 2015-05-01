<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>Insert title here</title>
</head>
<body>
<img SRC="http://thenextweb.com/wp-content/blogs.dir/1/files/2013/11/social-network-links.jpg" width="1250" height="250">

</form>

<form action="/social/search" method="post">
<input type="text" name="email"  />
<input type="submit" name="Sendrequest" value="Send Request"/>
</form>

<p></p>

<form action="/social/view" method="post">
<input type="submit" name="accept" value="view Request"/>
</form>

<form action = "/social/sendMessage" method "post>
<input type = "submit" name="sendMessage" value= "send Message" />
</form>

<hr>
<Strong>Messages</Strong> <br>
<form method="get" action="/social/msg/select/">
	<input type="hidden" value="<%= (String)request.getAttribute("uname") %>" name="uname">
	<input type="submit" value="New Conversation">
</form> <br> 
<form method="post" action="/social/msg/view/">
	<input type="hidden" value="<%= (String)request.getAttribute("uname") %>" name="uname">
	<input type="submit" value="Send msg">
</form> 
<hr>

<Strong>Notifications:</Strong> <br> 

<form method="post" action="/social/notification/getMessages/">
	<input type="hidden" value="<%= (String)request.getAttribute("uname") %>" name="uname">
	<input type="submit" value="Messages Notifications">
</form> <br>
<form method="post" action="/social/notification/getFriends/">
	<input type="hidden" value="<%= (String)request.getAttribute("uname") %>" name="uname">
	<input type="submit" value="Friends Notifications">
</form>

<hr>


<Strong>Create Posts:</Strong> <br> <hr>

	<form method="post" action="/social/addpost/">
	Timeline <input type="text"  name="timeline"><br>
	Feelings <input type="text"  name="feelings"><br>
	Privacy <select  name="privacy">
	<option value="private">Private</option>
	<option value="public">Public</option>
	<option value="only me">Only Me</option>
	</select><br>
	
	Content <textarea rows="4" cols="50" name="content">
</textarea><br>
	<input type="submit" value="add post">
	</form> <br>
	<br><br><br>
<Strong>Create Page:</Strong> <br> <hr>
	<form method="post" action="/social/addpage/">
	Page Name <input type="text" name="name"><br>
	Page Category <select  name="cat">
	<option value="entertainment">Entertainment</option>
	<option value="Public Figure">Public Figure</option>
	<option value="NGO">NGO</option>
	</select><br>
	<input type="submit" value="create page">
	</form> <br>
<hr>

<Strong>Like Page:</Strong> <br> <hr>
	<form method="post" action="/social/likePage/">
	<input type="hidden" value="<%= (String)request.getAttribute("uname") %>" name="uname">
	Page Name <input type="text" name="name"><br>
	<input type="submit" value="like page">
	</form> <br>
<hr>


<Strong>Display timeline</Strong> <br> 
	<form method="post" action="/social/timeline/">
	Timeline <input type="text" name="timeline"><br>
	<input type="submit" value="Display Timeline">
	</form> <br>
<hr>


</body>
</html>