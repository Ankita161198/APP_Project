/**
 * Hello world!
 *
 */


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main
{
    public static void main( String[] args ) throws ParseException, IOException, InterruptedException {

        String url="https://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline";



        HttpRequest request=HttpRequest.newBuilder().GET().uri(URI.create(url)).build();



        HttpClient client=HttpClient.newBuilder().build();

        HttpResponse response=client.send(request, HttpResponse.BodyHandlers.ofString());



  /*  	System.out.println(response.statusCode());

    	System.out.println(response.body());



*/

        String jsonString = (String) response.body();

        JSONParser parser = new JSONParser();

        JSONArray obj  = null;
        try {
            obj = (JSONArray) parser.parse(jsonString);
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
        // JSONObject objects = obj.getJSONArray();
        for(int i=0;i<=obj.size()-1;i++)
        {
            JSONObject obj1 = (JSONObject) obj.get(i);
            System.out.println(obj1.get("name"));
        }

    }
}
