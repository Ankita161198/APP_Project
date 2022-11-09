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
        String create_brand = "CREATE TABLE  IF NOT EXISTS brands ( brand_id INT(4) NOT NULL AUTO_INCREMENT, brand_name VARCHAR(10), PRIMARY KEY (brand_id))";
        Statement stmt=conn.createStatement();
        stmt.executeUpdate(db);
        stmt.execute(create_brand);


//        for(int i=0;i<4;i++)
//        {
//           String s="INSERT INTO brands values ("+brands[i]+")";
//           stmt.executeUpdate(s);
//        }
       // ptmst.executeUpdate();
//        for(int i=0;i<3;i++){
//            PreparedStatement stmt1= conn.prepareStatement("insert into brands values(brand_name = ?)");
//            System.out.println(brands[i]);
//            System.out.println(brands[i+1]);
//            System.out.println(brands[i+2]);
//            stmt1.setString(1,"Nykka");
//            String j= String.valueOf(stmt1.executeUpdate());
//            System.out.println(j + "record loaded");
//        }




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
