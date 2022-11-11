import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Product {

    Product(){

    }
    public void createTable(String jsonstring, Statement stmt) throws SQLException {
        String drop_productlist ="DROP TABLE IF EXISTS productlist";
        stmt.executeUpdate(drop_productlist);
        String create_pt_lst_table="CREATE TABLE IF NOT EXISTS productlist(id INT(10)," +
                "name VARCHAR(100),price INT(20),description VARCHAR(1000),product_type VARCHAR(50)," +
                "rating float(10),image_link varchar(1000),product_link VARCHAR(1000)," +
                "website_link VARCHAR(1000));\n";
        stmt.execute(create_pt_lst_table);
    }

    public void insertProducts(String[] id, String[] name,String[] price, String[] description,String[] product_type, String[] rating, String[] image_link, String[] product_link, String[] website_link, Statement stmt) throws SQLException {
        for(int i=0;i<name.length;i++)
        {

            description[i]=description[i].replaceAll("[^a-zA-Z0-9]", " ");
            name[i]=name[i].replaceAll("[^a-zA-Z0-9]", " ");
            String insertList = "insert into productlist(id,name,price,description,product_type,rating,image_link,product_link,website_link) values("+id[i]+",'"+name[i]+"',"+price[i]+",'"+description[i]+"','"+product_type[i]+"',"+rating[i]+",'"+image_link[i]+"','"+product_link[i]+"','"+website_link[i]+"' )";
            stmt.executeUpdate(insertList);

        }

    }
    public void selectAllProducts(Statement stmt) throws SQLException {

        String selectAll ="select name,price,rating from productlist";
        ResultSet rs=stmt.executeQuery(selectAll);

        ArrayList<String> res=new ArrayList<>();

        System.out.printf("%-70s %-20s %8s","Product Name","Price","Rating\n---------------------------------------------------------------------------------------------------------------------------\n");
        while(rs.next()){
            System.out.format("%-70s %-20s %-20s",rs.getString("name") ,rs.getString("price"),rs.getString("rating"));
            System.out.println();
        }

    }


    public void selectProductbyPT(Statement stmt,int choice) throws SQLException {
        String type="";
        if(choice==1)
            type="blush";
        else if(choice==2)
            type="eyeliner";
        else if(choice==3)
            type="foundation";
        else if(choice==4)
            type="lipstick";
        else if(choice==5)
            type="nail_polish";
        else if(choice == 6)
            type="lip_liner";
        else if(choice == 7)
            type="bronzer";
        else if (choice==8)
            type="eyeshadow";
        else if(choice==9)
            type="mascara";

        String selectAll ="select name,price,rating from productlist where product_type='"+type+"'";
        ResultSet rs=stmt.executeQuery(selectAll);
        System.out.printf("%-70s %-20s %8s","Product Name","Price","Rating\n---------------------------------------------------------------------------------------------------------------------------\n");
        while(rs.next()){
            System.out.format("%-70s %-20s %-20s",rs.getString("name") ,rs.getString("price"),rs.getString("rating"));
            System.out.println();
        }

    }






    }


