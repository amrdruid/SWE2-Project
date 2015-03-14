<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>Insert title here</title>
</head>
<body>

<form action="/social/Logout" method="post">
<input type="submit" name="logout" value="log out"/>
</form>

<form action="/social/search" method="post">
<input type="text" name="email"  />
<input type="submit" name="Sendrequest" value="Send Request"/>
</form>

<form action="/social/view" method="post">
<input type="submit" name="accept" value="view Request"/>
</form>

</body>
</html>