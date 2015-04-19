<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>FriendRequest</title>
</head>
<body>
<form action="/social/accept" method="post">
<input type="hidden" name="to" value="${it.email}" />
 
  <input type="submit" name="accept" value="accept">
  
  </form>
</body>
</html>