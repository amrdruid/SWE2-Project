<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
</head>
<body>
<h1>YOUR NOTIFICATIONS YA WELEH</h1> <br>
<ol>
<c:forEach items="${it.notfs}" var="notf">
<li><strong><c:out value="${notf}"></c:out></strong></li>		
<br>
</c:forEach>
</ol>
</body>
</html>