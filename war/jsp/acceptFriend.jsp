<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
</head>
<body>

<form action="/social/acceptFriend" method="post">

  yourName : <input type="text" name="friendName" /> <br>
  Password : <input type="text" name="password" /> <br>
  SenderName : <input type="text" name="senderName" /> <br>
  
  <input type="submit" value="Accept Request">
  
</form>

<form method="link" action="/social/logout">
    <input type="submit" value="Logout"/>
</form>
</body>
</html>