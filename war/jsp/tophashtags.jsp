<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<link href="/jsp/res/bootstrap.css" rel="stylesheet">
<link rel="stylesheet" href="/jsp/res/normalize.css">
<title>Insert title here</title>
</head>
<body>
<h1>AWESOME TOP HASHTAGS</h1> <br>
<ol>
<c:forEach items="${it.hashtags}" var="hashtag">
<li><strong><c:out value="${hashtag}"></c:out></strong></li>		
<br>
</c:forEach>
</ol>
</body>
</html>