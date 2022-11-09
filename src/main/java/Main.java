/**
 * Hello world!
 *
 */


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.text.ParseException;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main
{
    public static void main( String[] args ) throws ParseException, IOException, InterruptedException, SQLException {
       //connect conn=new connect();
        Connection conn = connect.ConnectDB();
        String db="CREATE DATABASE IF NOT EXISTS products";
        //String brands[]=new int[100];
        String[] brands={"Maybelline","Nykka","Faces Canada","Lakme"};
        System.out.println("connection string :- "+conn);
        String url="https://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline";
        HttpRequest request=HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
        HttpClient client=HttpClient.newBuilder().build();
        HttpResponse response=client.send(request, HttpResponse.BodyHandlers.ofString());
       // String jsonString = (String) response.body();
      //  System.out.println(jsonString);
       // JSONParser parser = new JSONParser();
        //System.out.println(brands[(int)(Math.random() * ( 3 - 0 ))]);


        Statement stmt=conn.createStatement();
        stmt.executeUpdate(db);
        String drop_table ="DROP TABLE IF EXISTS brands";
        stmt.executeUpdate(drop_table);
        String create_brand = "CREATE TABLE  IF NOT EXISTS brands ( brand_id INT(4) NOT NULL AUTO_INCREMENT, brand_name VARCHAR(100), PRIMARY KEY (brand_id))";
        stmt.execute(create_brand);
        for(int i=0;i<=3;i++)
        {

            String insertBrand = "insert into brands(brand_id,brand_name) values("+(i+1)+",'"+brands[i]+"')";
            stmt.executeUpdate(insertBrand);
        }




//        JSONArray obj  = null;
//        try {
//            obj = (JSONArray) parser.parse(jsonString);
//        } catch (org.json.simple.parser.ParseException e) {
//            throw new RuntimeException(e);
//        }
        // JSONObject objects = obj.getJSONArray();
//        for(int i=0;i<=obj.size()-1;i++)
//        {
//            JSONObject obj1 = (JSONObject) obj.get(i);
//            System.out.println(obj1);
//        }


    }
}
