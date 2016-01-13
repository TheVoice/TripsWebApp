<%@ page language="java" contentType="text/html; charset=ISO-8859-2"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lista tras TripSearch</title>
<style type="text/css">
	body{
		background-image:url('resources/antiquepaper_1920x1234.jpg');
	}
</style>
</head>
<body>
	<h2>Proponowane trasy wycieczek:</h2>
	<ul>
		<c:forEach var="route" items="${resultList}" varStatus="loop">
			<li><a href="${requestID}&map${loop.index}" target="_blank">TRASA <c:out value="${loop.count}"/> -> Długość: ${route.length } km</a></li>
		</c:forEach>
	</ul>
	
</body>
</html>