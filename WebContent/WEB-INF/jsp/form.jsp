<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
	<title>Trips Search Main Page</title>
</head>
<body>
	<div style="text-align:center">
		<h2>
			Welcome to TripSearch<br> <br>
		</h2>
	</div>	
		
	<form:form method="POST" action="results" oninput="minRangeOut.value=minLength.value;maxRangeOut.value=maxLength.value">
		<div>
			<table style="margin: 0 auto;">
			<!--
				<tr>
		 			<td><form:label path="startRoad">Go for a trip from:</form:label></td>
		 			<td><form:input path="startRoad"/></td>
		 		</tr>
		 	-->
		 		<tr>
		 			<td><form:label path="startX">X coord.</form:label></td>
		 			<td><form:input path="startX"/></td>
		 		</tr>
		 		<tr>
		 			<td><form:label path="startY">Y coord.</form:label></td>
		 			<td><form:input path="startY"/></td>
		 		</tr>
		 		<tr>
		 			<td>Minimum length:</td>
		 			<td><form:input path="minLength" type="range" min="10" max="120" value="10"/> <output name="minRangeOut" for="minttime">10</output></td>
		 		</tr>
		 		<tr>
		 			<td>Maximum length:</td>
		 			<td><form:input path="maxLength" type="range" min="10" max="120" value="10"/> <output name="maxRangeOut" for="maxttime">10</output></td>
		 		</tr>
	  		</table>
  		</div>
  		<div style="text-align:center">
  			<input type="submit" value="Search"/>
  		</div>
	</form:form>
		
	
</body>
</html>