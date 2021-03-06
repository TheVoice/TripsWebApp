<html>
<head>
	<title>Znaleziona trasa</title>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
    <style>
        body {
            padding: 0;
            margin: 0;
        }
        html, body, #map {
            height: 100%;
            width: 100%;
        }
    </style>
</head>
<body>
	<div id="map"></div>
	
	<script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>
	<script>
		var pointX = "${resultObj.latitudeCenter}"
		var pointY = "${resultObj.longitudeCenter}"
		var startX = "${startPoint.latitudeStart}"
		var startY = "${startPoint.longitudeStart}"
	
		var map = L.map('map',{
			center: L.latLng(pointX,pointY),
			zoom: 13
		});
		mapLink = '<a href="http://openstreetmap.org">OpenStreetMap</a>';
		L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
			maxZoom: 18,
			attribution: '&copy; ' + mapLink + ' Contributors'
		}).addTo(map);
		
		
		var list = ${resultObj.jsonLines};
		console.log(list.length)
		var i,length
		for(i=0,length=list.length;i<length;i++){
			line = list[i]
			from_lat = line.from_lat
			from_lon = line.from_lon
			to_lat = line.to_lat
			to_lon = line.to_lon
			L.polyline([[from_lat,from_lon], [to_lat,to_lon]], {color: "#17E66A", opacity: 1}).addTo(map);
		}
		var distance = "${resultObj.distance}"
		L.circle([startX,startY],111229*distance).addTo(map);
	</script>
</body>
</html>