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
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main
{
    public static void main( String[] args ) throws ParseException, IOException, InterruptedException, SQLException, org.json.simple.parser.ParseException {
        Scanner sc=new Scanner(System.in);
        Connection conn = Connect.ConnectDB();
        System.out.println("connection string :- " + conn);
        String url = "https://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline";
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Statement stmt = conn.createStatement();
        String jsonstring = (String) response.body();

        Product p = new Product();

        p.createTable(jsonstring,stmt);

        String[] id=jsonArrayOutput(jsonstring,"id");
        String[] name=jsonArrayOutput(jsonstring,"name");
        String[] price=jsonArrayOutput(jsonstring,"price");
        String[] description =jsonArrayOutput(jsonstring,"description");
        String[] rating=jsonArrayOutput(jsonstring,"rating");
        String[] product_type=jsonArrayOutput(jsonstring,"product_type");
        String[] image_link=jsonArrayOutput(jsonstring,"image_link");
        String[] product_link=jsonArrayOutput(jsonstring,"product_link");
        String[] website_link=jsonArrayOutput(jsonstring,"website_link");

        p.insertProducts(id,name,price,description,product_type,rating,image_link,product_link,website_link,stmt);

        System.out.println("\nWelcome to Maybelline Product Tracker\n");
        while(true){
            System.out.println( displayMenu());
            System.out.println("Enter your choice :- ");
            int choice=sc.nextInt();
            switch (choice){
                case 1:
                    p.selectAllProducts(stmt);
                    break;
                case 2:
                    System.out.println(displayproductType());
                    int pt=sc.nextInt();
                    p.selectProductbyPT(stmt,pt);
                    break;
                case 3:
                    System.out.println("Enter a rating, you will be given a list of all products with same or higher rating :- ");
                    float r=sc.nextFloat();
                    p.displayProductbyRating(stmt,r);
                    break;
                case 4:
                    System.out.println("Select a product type you wish to update the rating of.");
                    System.out.println(displayproductType());
                    int pt1=sc.nextInt();
                    p.updateRating(stmt,pt1);
                    break;
                case 5:
                    System.out.println("Select a product type you wish to delete");
                    System.out.println(displayproductType());
                    int pt2=sc.nextInt();
                    p.deleteProduct(stmt,pt2);
                    break;
                case 6:
                    System.exit(0);
            }
        }

   }
    public static String[] jsonArrayOutput(String jsonstring,String columnName) throws org.json.simple.parser.ParseException {
        // System.out.println(jsonstring);
        JSONParser parser = new JSONParser();
        JSONArray arr = (JSONArray) parser.parse(jsonstring);
        String[] result = new String[arr.size()];
        for (int i = 0; i <= arr.size() - 1; i++) {

            JSONObject obj1 = (JSONObject) arr.get(i);
            result[i] = String.valueOf(obj1.get(columnName));
        }
        return result;
    }

    public static String displayMenu(){
        String res="\n*********************************************************************************************************************************************************\nPlease select the operation that you want to perform\n1.Display all products\n2.Display all products for a product type\n3.Display all products by rating\n4.Update rating for a product\n5.Delete a product\n6.Exit\n*********************************************************************************************************************************************************\n";
        return res;
    }

    public static String displayproductType(){
        return "\n1.blush\n2.eyeliner\n3.foundation\n4.lipstick\n5.nail_polish\n6.lip_liner\n7.bronzer\n8.eyeshadow\n9.mascara";
    }

}
