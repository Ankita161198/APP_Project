import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;



import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {


    Connection conn = Connect.ConnectDB();
    //System.out.println("connection string :- " + conn);
    String url = "https://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline";
    HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
    HttpClient client = HttpClient.newBuilder().build();
    HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

    Statement stmt = conn.createStatement();
    String jsonstring = (String) response.body();
    Product p = new Product(stmt);



    public ProductTest() throws IOException, InterruptedException, SQLException {
    }



    @Test
    public void checkIfproductexists() throws SQLException {
        assertTrue(p.selectAllProducts());
    }

    @Test
    public void checkIfproductexistsbyproductType() throws SQLException {
        assertTrue(p.selectProductbyPT(2));
    }

    @Test
    public void checkIfproductdoesnotexistsbyproductType() throws SQLException {
        assertFalse(p.selectProductbyPT(10));
    }

    @Test
    public void checkIfratingexists() throws SQLException {
        assertTrue(p.displayProductbyRating(4.8f));
    }

    @Test
    public void checkIfratingdoesnotexists() throws SQLException {
        assertFalse(p.displayProductbyRating(6.0f));
    }


    

}