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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


//import org.json.JSONString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.Random;

public class Main
{

    public static int getRandomValue(int Min, int Max){
        return ThreadLocalRandom
                .current()
                .nextInt(Min, Max + 1);
    }
    public static void main( String[] args ) throws ParseException, IOException, InterruptedException, SQLException, org.json.simple.parser.ParseException {

        Connection conn = Connect.ConnectDB();
        Random rand = new Random();
        int random_rate=rand.nextInt();
        //connection successful
        System.out.println("connection string :- " + conn);

        String url = "https://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline";
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //create Statement object using connectionString
        Statement stmt = conn.createStatement();

        //Drop all tables if exists
        droptables(stmt);

        String jsonString = (String) response.body();

        //creation of brand table
        CreateBrandTable(stmt);
        CreateProductType(jsonString,stmt);
        CreateRatingTable(jsonString,stmt);
        CreateProductList(stmt);
       // CreateProductList(stmt,pt1);



    }

    public static void droptables(Statement stmt) throws SQLException {
        String drop_productlist ="DROP TABLE IF EXISTS product_list";
        String drop_rating="DROP TABLE IF EXISTS rating";

        String drop_product_type="DROP TABLE IF EXISTS product_type";
        String drop_brands = "DROP TABLE IF EXISTS brands";
        stmt.executeUpdate(drop_productlist);
        stmt.executeUpdate(drop_rating);
        stmt.executeUpdate(drop_product_type);
        stmt.executeUpdate(drop_brands);
    }

    public static void CreateBrandTable(Statement stmt) throws SQLException {
        String[] brands = {"Maybelline", "Nykka", "Faces Canada", "Lakme"};

        //String queries for table brands
        String db = "CREATE DATABASE IF NOT EXISTS products";

        String create_brand = "CREATE TABLE  IF NOT EXISTS brands ( brand_id INT(4) NOT NULL AUTO_INCREMENT, brand_name VARCHAR(100), PRIMARY KEY (brand_id))";

        //execute statements for table brands
        stmt.executeUpdate(db);
        //stmt.executeUpdate(drop_table);
        stmt.execute(create_brand);


        //insert data into table brand
        for (int i = 0; i <= 3; i++) {

            String insertBrand = "insert into brands(brand_id,brand_name) values(" + (i + 1) + ",'" + brands[i] + "')";
            stmt.executeUpdate(insertBrand);
        }

    }
    private static void CreateProductType(String jsonstring,Statement stmt) throws SQLException, org.json.simple.parser.ParseException {
        String create_pt_table = "CREATE TABLE  IF NOT EXISTS product_type ( PT_id INT(10) , PT_name VARCHAR(100), PRIMARY KEY (PT_id))";
        stmt.execute(create_pt_table);
        String[] res=jsonArrayOutput(jsonstring,"product_type");
        Set<String> setRes = new HashSet<>(Arrays.asList(res));
        String[] result = new String[setRes.size()];
        setRes.toArray(result);
        for (int i = 0; i < result.length; i++) {

            String insertProductType = "insert into product_type(PT_id,PT_name) values(" + (i + 1) + ",'" + result[i] + "')";
            stmt.executeUpdate(insertProductType);
        }
    }
    private static void CreateRatingTable(String jsonstring,Statement stmt) throws SQLException, org.json.simple.parser.ParseException {

        //String queries for table rating

        String create_rating = "CREATE TABLE  IF NOT EXISTS rating ( r_id INT(10) NOT NULL AUTO_INCREMENT, rating_name FLOAT(5), PRIMARY KEY (r_id))";


        stmt.execute(create_rating);
        String[] res=jsonArrayOutput(jsonstring,"rating");
        System.out.println(res);
        Set<String> setRes = new HashSet<>(Arrays.asList(res));
        String[] result = new String[setRes.size()];
        setRes.toArray(result);

        //insert data into table rating
        float val = 0.0f;
        for (int i = 0; i < result.length; i++) {
            if (result[i] != "null"){
                //result[i] = String.valueOf(0.0F);
                 val= Float.parseFloat(result[i]);

            }else{
                result[i] = String.valueOf(val);
                val = Float.parseFloat(result[i]);

            }
            System.out.println(val);

            String insertRating = "insert into rating(r_id, rating_name) values(" + (i + 1) + "," + val + ")";
            stmt.executeUpdate(insertRating);
        }

    }


    private static void CreateProductList(Statement stmt) throws SQLException {
        String create_pt_lst_table = "CREATE TABLE IF NOT EXISTS product_list(PL_id INT(10),\n" +
                "PL_name VARCHAR(100),price INT(20),description VARCHAR(100),\n" +
                "brand_id INT(10),\n" +
                "pt_id INT(10),\n" +
                "r_id INT(10),\n" +
                "PRIMARY KEY (PL_id),\n" +
                "FOREIGN KEY (brand_id) references brands(brand_id),\n" +
                "FOREIGN KEY (pt_id) references product_type(pt_id) ,\n" +
                "foreign key (r_id) references rating(r_id)\n" +
                "\n" +
                ");";
        stmt.execute(create_pt_lst_table);

    }

    public static String[] jsonArrayOutput(String jsonstring,String columnName) throws org.json.simple.parser.ParseException {

        JSONParser parser = new JSONParser();
        JSONArray arr = (JSONArray) parser.parse(jsonstring);
        String[] result = new String[arr.size()];
        System.out.println(jsonstring);
        for (int i = 0; i <= arr.size() - 1; i++) {

            JSONObject obj1 = (JSONObject) arr.get(i);
            result[i] = String.valueOf(obj1.get(columnName));
        }
        return result;
    }


}
