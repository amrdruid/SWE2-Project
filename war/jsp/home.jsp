<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>Insert title here</title>
</head>
<body>
<p> Welcome b2a ya ${it.email} </p>
<p> This is should be user home page </p>
<p> Current implemented services "http://fci-swe-apps.appspot.com/rest/RegistrationService --- {requires: uname, email, password}" </p>
<p> and "http://fci-swe-apps.appspot.com/rest/LoginService --- {requires: uname,  password}" </p>
<p> you should implement sendFriendRequest service and addFriend service
<form action="/social/Logout" method="post">
<input type="submit" name="logout" value="log out"/>
</form>

<form action="/social/search" method="post">
<input type="text" name="email"  />
<input type="submit" name="Sendrequest" value="Send Request"/>
</form>

<p></p>

<form action="/social/view" method="post">
<input type="submit" name="accept" value="view Request"/>
</form>

</body>
</html>