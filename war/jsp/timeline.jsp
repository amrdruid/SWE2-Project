<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://w...content-available-to-author-only...3.org/TR/html4/loose.dtd">
<html>
 
<head>
	<title>Listed Users</title>
</head>

<body>
 
 <c:forEach items="${it.posts}" var="post">
 
 <p>
 	Feeling
	<c:out value="${post.feelings}"></c:out>
 </p>
 
 <p>
	Privacy
	<c:out value="${post.privacy}"></c:out>
 </p>
 
  <p>
	Post : 
	<c:out value="${post.timeline}"></c:out>
 </p>

	<br>
 	<hr>
</c:forEach>
 
</body>
</html>