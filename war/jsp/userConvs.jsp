<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Insert title here</title>
</head>
<body>

<form method="post" action="/social/msg/send/">
<input type="hidden" value="<%= (String)request.getAttribute("uname") %>" name="uname">
Messages <br>
<c:forEach items="${it.convs}" var="conv">
<input type="radio" name="conv" value="<c:out value="${conv}"></c:out>">
		<c:out value="${conv}"></c:out>
<br>
</c:forEach>
Your msg: <br>
<input type="text" placeholder="Enter msg here" name="content">
<input type="submit" value="Send MSG">
 </form>
</body>
</html>