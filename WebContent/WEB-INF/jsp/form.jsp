<%@ page language="java" contentType="text/html; charset=ISO-8859-2"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
	<title>Trips Search Main Page</title>
	<style type="text/css">
	body{
		background-image:url('resources/antiquepaper_1920x1234.jpg');
	}
	</style>
	<script>
		function check_range(){
			var oForm = this.form;
			var oMin = oForm.elements["minSlider"];
			console.log("test");
		}
	</script>
</head>
<body onload="minRangeOut.value=minLength.value;maxRangeOut.value=maxLength.value">
	<div style="text-align:center">
		<h2>
			System wyszukiwania tras wycieczek TripSearch<br> <br>
		</h2>
	</div>	
	
	<form:form method="POST" action="results" oninput="minRangeOut.value=minLength.value;maxRangeOut.value=maxLength.value" onload="minRangeOut.value=minLength.value;maxRangeOut.value=maxLength.value">	
	
		<div>
			<table style="margin: 0 auto;" border="1">
			
				<tr>
		 			<td><form:label path="startRoad">Chcę zacząć wycieczkę z:</form:label></td>
		 			<td><form:input path="startRoad"/></td>
		 		</tr>
		 		<tr>
		 			<td>Minimalna długość trasy:</td>
		 			<td><form:input name="minSlider" path="minLength" type="range" min="0" max="60" value="10"/> <output id="minRangeOut" name="minRangeOut" for="minttime"></output>km</td>
		 		</tr>
		 		<tr>
		 			<td>Maksymalna długość trasy:</td>
		 			<td><form:input name="maxSlider" path="maxLength" type="range" min="0" max="60" value="10"/> <output id="maxRangeOut" name="maxRangeOut" for="maxttime"></output>km</td>
		 		</tr>
		 		<tr>
		 			<td>Typ wycieczki:</td>
		 			<td><form:select path="strategy">
		 				<form:option value="byFoot">Piesza</form:option>
		 				<form:option value="byBike">Rowerowa</form:option>
		 			</form:select></td>
		 		</tr>
		 		<tr>
		 			<td colspan="2" align="center"><input type="submit" name="submit_button" value="Wyszukiwanie trasy"/></td>
		 		</tr>
	  		</table>
  		</div>
  		<div style="text-align:center">
  			<p style="color:red">${command.message }</p>
  		</div>
	</form:form>
		
	
</body>
</html>