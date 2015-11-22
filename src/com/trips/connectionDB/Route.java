package com.trips.connectionDB;


import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

class Route {
    public String id;
    public String type;
    public String length;
    public String quality;
    public String bounds;
    public String route;
    public LinkedList<Point> ParsedLinestring;
    
    Route(){
    	ParsedLinestring = new LinkedList<Point>();
    }
   
	public void generateLeafletHtmlView(String filename) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new AssertionError(e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        Double sumLon = 0.0;
        Double sumLat = 0.0;
        Integer summed = 0;
        
        boolean first = true;
        Point prev = new Point();
        for(Point e: ParsedLinestring) {
        	if(first){
        		prev = e;
        		first = false;
        	}
        	else{
	            Double from_lat = prev.x;  // 19
	            Double from_lon = prev.y;
	            Double to_lat = e.x;  // 20
	            Double to_lon = e.y;
	            sumLon += from_lon;
	            sumLon += to_lon;
	            sumLat += from_lat;
	            sumLat += to_lat;
	            summed += 2;
	            prev = e;
        	}
        }
        writer.print(getLeafletHeader(sumLat / summed, sumLon / summed));
        //TODO - is there any nicer way to find max and min?
        first = true;
        for(Point e: ParsedLinestring) {
        	if(first){
        		prev = e;
        		first = false;
        	}
        	else{
        		writer.println(getLeafletPolylineStringFromEdge(prev, e));
        		prev = e;
        	}
            
        }
        writer.print(getLeafletFooter());
        writer.close();
    }

    private String getLeafletPolylineStringFromEdge(Point prev, Point e) {
        Double from_lat = prev.x;  // 19
        Double from_lon = prev.y;
        Double to_lat = e.x;  // 20
        Double to_lon = e.y;
        Integer rescaled = 100;
        return "L.polyline([["+from_lat+","+from_lon+"], ["+to_lat+","+to_lon+"]], {color: \"#17E66A\", opacity: 1}).addTo(map);";
    }

    public static String getColor(int percent) {
        //from http://stackoverflow.com/questions/340209/generate-colors-between-red-and-green-for-a-power-meter
        float hue = (float) (percent/100.0*0.4); // 0.4 = Green, see chart in page linked above
        float saturation = 0.9f;
        float brightness = 0.9f;

        int intColor = Color.HSBtoRGB(hue, saturation, brightness);
        return String.format("#%06X", (0xFFFFFF & intColor)); //Color.getHSBColor((float)H, (float)S, (float)B);
    }

    private String getLeafletHeader(Double latitudeCenter, Double longitudeCenter) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<title>Znaleziona trasa</title>\n" +
                "\t<meta charset=\"utf-8\" />\n" +
                "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "\t<link rel=\"stylesheet\" href=\"http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css\" />\n" +
                "    <style>\n" +
                "        body {\n" +
                "            padding: 0;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "        html, body, #map {\n" +
                "            height: 100%;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<div id=\"map\"></div>\n" +
                "\n" +
                "\t<script src=\"http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js\"></script>\n" +
                "\t<script>\n" +
                "\t\tvar map = L.map('map').setView(["+latitudeCenter+", "+longitudeCenter+"], 15);\n" +
                "\t\tmapLink = '<a href=\"http://openstreetmap.org\">OpenStreetMap</a>';\n"+
                getOSMDefaultStyleTiles();
    }
    private String getOSMDefaultStyleTiles() {
        return  "\t\tL.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
                "\t\t\tmaxZoom: 18,\n" +
                "\t\t\tattribution: '&copy; ' + mapLink + ' Contributors'\n" +
                "\t\t}).addTo(map);\n";
    }

    private String getBLackAndWhiteMapTiles(){
        return  "L.tileLayer('http://openmapsurfer.uni-hd.de/tiles/roadsg/x={x}&y={y}&z={z}', {\n" +
                "\t\t\tminZoom: 0,\n" +
                "\t\t\tmaxZoom: 19,\n" +
                "\t\t\tattribution: 'Imagery from <a href=\"http://giscience.uni-hd.de/\">GIScience Research Group @ University of Heidelberg</a> &mdash; Map data &copy; <a href=\"http://www.openstreetmap.org/copyright\">OpenStreetMap</a>'\n" +
                "\t\t}).addTo(map);\n";
    }

    private String getLeafletFooter() {
        return "\t</script>\n" +
                "</body>\n" +
                "</html>\n";
    }
	
}