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


//import org.json.JSONString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main
{
    public static void main( String[] args ) throws ParseException, IOException, InterruptedException, SQLException, org.json.simple.parser.ParseException {

        Connection conn = Connect.ConnectDB();
        //connection successful
        System.out.println("connection string :- " + conn);

        String url = "https://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline";
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //create Statement object using connectionString
        Statement stmt = conn.createStatement();

        //creation of brand table
        CreateBrandTable(stmt);


        String jsonString = (String) response.body();
        JSONParser parser = new JSONParser();
        JSONArray arr = (JSONArray) parser.parse(jsonString);
        String[] resultArray = new String[arr.size()];
        for (int i = 0; i <= arr.size() - 1; i++) {
            JSONObject obj1 = (JSONObject) arr.get(i);
            resultArray[i] = String.valueOf(obj1.get("product_type"));
        }
        Set<String> res = new HashSet<>(Arrays.asList(resultArray));
        String[] pt = new String[res.size()];
        res.toArray(pt);
        CreateProductType(stmt,pt);
        CreateProduct(stmt,pt);

    }

    private static void CreateProduct(Statement stmt, String[] pt) {
        String drop_pt_lst_table = "DROP TABLE IF EXISTS product_list";
    }

    private static void CreateProductType(Statement stmt, String[] pt) throws SQLException {
        String drop_pt_table = "DROP TABLE IF EXISTS product_type";
        String create_pt_table = "CREATE TABLE  IF NOT EXISTS product_type ( PT_id INT(10) , PT_name VARCHAR(100), PRIMARY KEY (PT_id))";
        stmt.executeUpdate(drop_pt_table);
        stmt.execute(create_pt_table);

        for (int i = 0; i < pt.length; i++) {

            String insertProductType = "insert into product_type(PT_id,PT_name) values(" + (i + 1) + ",'" + pt[i] + "')";
            stmt.executeUpdate(insertProductType);
        }
    }

    public static void CreateBrandTable(Statement stmt) throws SQLException {
        String[] brands = {"Maybelline", "Nykka", "Faces Canada", "Lakme"};

        //String queries for table brands
        String db = "CREATE DATABASE IF NOT EXISTS products";
        String drop_table = "DROP TABLE IF EXISTS brands";
        String create_brand = "CREATE TABLE  IF NOT EXISTS brands ( brand_id INT(4) NOT NULL AUTO_INCREMENT, brand_name VARCHAR(100), PRIMARY KEY (brand_id))";

        //execute statements for table brands
        stmt.executeUpdate(db);
        stmt.executeUpdate(drop_table);
        stmt.execute(create_brand);


        //insert data into table brand
        for (int i = 0; i <= 3; i++) {

            String insertBrand = "insert into brands(brand_id,brand_name) values(" + (i + 1) + ",'" + brands[i] + "')";
            stmt.executeUpdate(insertBrand);
        }

    }
}
