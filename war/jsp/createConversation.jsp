<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
</head>
<body>
<form method="post" action="/social/msg/create/">
Friends <br>
<c:forEach items="${it.users}" var="user">
<input type="checkbox" name="users" value="<c:out value="${user.name}"></c:out>">
		<c:out value="${user.name}"></c:out>
<br>
</c:forEach>
conversation name: <br>
<input type="text" placeholder="Enter conversation name" name="uname">
<input type="submit" value="Make Conversation">
 </form>
</body>
</html>