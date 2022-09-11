package main.java;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

public class App {

    public static void main(String[] args){

        System.out.println("This console app was developed, in order to display earthquakes in a given region, within the last amount of days.");
        System.out.println("Please type the country name and press Enter...");

        Scanner scan = new Scanner(System.in);
        String country = scan.nextLine().toLowerCase();
        
        System.out.println("Please type period of days and press Enter...");
        int days = scan.nextInt();

        App.GetEarthquakes(country, days);
        scan.close();
    }    

    public static void GetEarthquakes(String country, int days){
        List<Earthquake> queryResult = new ArrayList<Earthquake>();

        LocalDate startDate = LocalDate.now().minusDays(days);
        String startTime = startDate.getYear() + "-" + startDate.getMonthValue() + "-" + startDate.getDayOfMonth();
        String earthquakeApiUrl = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime="+ startTime;
        
        String apiResponse = CallAPI(earthquakeApiUrl);
        System.out.println(country + " will be searched in api results... \n");

        if (apiResponse.toLowerCase().contains(country)){
            System.out.println("Api response contains:" + country);
            queryResult = ApiResultParser(apiResponse, country);
            System.out.println("Earthquakes in " + country.toUpperCase() +  " within the past " + days +  " days: \n \n");

            for (Earthquake item : queryResult){
                System.out.println(item.toString()+ "\n");
            }    
        }
        else{
            System.out.println("No Earthquakes were recorded in " + country.toUpperCase() +  " within the past " + days +  " days.");
        }
        
        //return queryResult.toString();
    }
    
    public static String CallAPI(String strRequestURL) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(strRequestURL)).build();
        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .join();

        return response;

    }
    
    public static List<Earthquake> ApiResultParser (String responseString, String country) {

        List<Earthquake> earthquakeList = new ArrayList<Earthquake>();
        System.out.println("ApiResultParser runs... \n");

        JSONObject responseJson = new JSONObject(responseString);

        String earthquakeFeaturesJson = responseJson.get("features").toString();

        JSONArray arrayEarthquakes = new JSONArray(earthquakeFeaturesJson);

        for (int i=0; i < arrayEarthquakes.length(); i++){
            JSONObject arrayItem = arrayEarthquakes.getJSONObject(i);
            
            JSONObject properties = arrayItem.getJSONObject("properties");
            String place = properties.get("place").toString();

            if (place.toLowerCase().contains(country)){

                Long stampDate = properties.getLong("time");

                Timestamp stamp = new Timestamp(stampDate);
                Date date = new Date(stamp.getTime());
                String stringDate = date.toLocalDate().getMonth() + " " + date.toLocalDate().getDayOfMonth() + "," + date.toLocalDate().getYear();

                LocalDateTime dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(stampDate), TimeZone.getDefault().toZoneId());   
                String stringTime = dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond();

                earthquakeList.add(new Earthquake(country, place, properties.get("mag").toString(), stringDate, stringTime));
            }
        }
        return earthquakeList;
    }

}