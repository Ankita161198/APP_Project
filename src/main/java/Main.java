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
    public static String[] brands = {"Maybelline", "Nykka", "Faces Canada", "Lakme"};


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
        CreateProductList(jsonString,stmt);
        CreateLinksTable(jsonString,stmt);




    }

    public static void droptables(Statement stmt) throws SQLException {
        String drop_links="DROP TABLE IF EXISTS product_links";
        String drop_productlist ="DROP TABLE IF EXISTS product_list";
        String drop_rating="DROP TABLE IF EXISTS rating";
        String drop_product_type="DROP TABLE IF EXISTS product_type";
        String drop_brands = "DROP TABLE IF EXISTS brands";
        stmt.executeUpdate(drop_links);
        stmt.executeUpdate(drop_productlist);
        stmt.executeUpdate(drop_rating);
        stmt.executeUpdate(drop_product_type);
        stmt.executeUpdate(drop_brands);
    }

    public static void CreateBrandTable(Statement stmt) throws SQLException {

        String db = "CREATE DATABASE IF NOT EXISTS products";
        String create_brand = "CREATE TABLE  IF NOT EXISTS brands ( brand_id INT(4) NOT NULL AUTO_INCREMENT, brand_name VARCHAR(100), PRIMARY KEY (brand_id))";
        stmt.executeUpdate(db);
        stmt.execute(create_brand);

        //insert data into table brand
        for (int i = 0; i <= 3; i++) {

            String insertBrand = "insert into brands(brand_id,brand_name) values(" + (i + 1) + ",'" + brands[i] + "')";
            stmt.executeUpdate(insertBrand);
        }

    }
    private static void CreateProductType(String jsonstring,Statement stmt) throws SQLException, org.json.simple.parser.ParseException {
        String create_pt_table = "CREATE TABLE  IF NOT EXISTS product_type ( PT_id INT(10) , " +
                "PT_name VARCHAR(100), PRIMARY KEY (PT_id))";
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
         //   System.out.println(val);

            String insertRating = "insert into rating(r_id, rating_name) values(" + (i + 1) + "," + val + ")";
            stmt.executeUpdate(insertRating);
        }

    }


    private static void CreateProductList(String jsonstring,Statement stmt) throws SQLException, org.json.simple.parser.ParseException {

        String create_pt_lst_table="CREATE TABLE IF NOT EXISTS product_list(PL_id INT(10),PL_name VARCHAR(100),price INT(20),description VARCHAR(1000),brand_id INT(10),\n" +
                "                PRIMARY KEY (PL_id) ,\n" +
                "                   pt_id INT(10),\n" +
                "                rid INT(10),\n" +
                "                FOREIGN KEY (brand_id) references brands(brand_id),\n" +
                "                FOREIGN KEY (pt_id) references product_type(PT_id),\n" +
                "                FOREIGN KEY  (rid) references rating(r_id));";
        stmt.execute(create_pt_lst_table);

        CreateLinksTable(jsonstring,stmt);

        String[] id=jsonArrayOutput(jsonstring,"id");
        String[] pd_name=jsonArrayOutput(jsonstring,"name");
        String[] price=jsonArrayOutput(jsonstring,"price");
        String[] description =jsonArrayOutput(jsonstring,"description");
        String[] productType=jsonArrayOutput(jsonstring,"product_type");
        String[] rating=jsonArrayOutput(jsonstring,"rating");
        String[] image_link=jsonArrayOutput(jsonstring,"image_link");
        String[] product_link=jsonArrayOutput(jsonstring,"product_link");
        String[] website_link=jsonArrayOutput(jsonstring,"website_link");



        for(int i=0;i<pd_name.length;i++)
        {
            int index=(int)(Math.random() * 4) + 1;
            description[i]=description[i].replaceAll("[^a-zA-Z0-9]", " ");
            pd_name[i]=pd_name[i].replaceAll("[^a-zA-Z0-9]", " ");
            String insertList = "insert into product_list(PL_id,PL_name,price,description,brand_id,pt_id,rid) values("+id[i]+",'"+pd_name[i]+"',"+price[i]+",'"+description[i]+"',"+index+",(select PT_id from product_type where PT_name='"+productType[i]+"'),(select r_id from rating where rating_name="+rating[i]+") )";
            stmt.executeUpdate(insertList);
            String links_List = "insert into product_links(PL_id,image_link,product_link,website_link) values("+id[i]+",'"+image_link[i]+"','"+product_link[i]+"','"+website_link[i]+"')";
            stmt.executeUpdate(links_List);
        }


    }

    public static void CreateLinksTable(String jsonstring,Statement stmt) throws SQLException, org.json.simple.parser.ParseException {
        String create_links = "CREATE TABLE  IF NOT EXISTS product_links ( PL_id INT(10), image_link VARCHAR(500), product_link VARCHAR(500), website_link VARCHAR(500), FOREIGN KEY (PL_id) references product_list(PL_id) );";
        stmt.execute(create_links);
    }

    public static String[] jsonArrayOutput(String jsonstring,String columnName) throws org.json.simple.parser.ParseException {

        JSONParser parser = new JSONParser();
        JSONArray arr = (JSONArray) parser.parse(jsonstring);
        String[] result = new String[arr.size()];
        for (int i = 0; i <= arr.size() - 1; i++) {

            JSONObject obj1 = (JSONObject) arr.get(i);
            result[i] = String.valueOf(obj1.get(columnName));
        }
        return result;
    }


}
