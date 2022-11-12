import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Product {
    Scanner sc=new Scanner(System.in);
    Statement stmt;
    public Product(Statement stmt) {
        this.stmt=stmt;
    }

    public void createTable(String jsonstring, Statement stmt) throws SQLException {
        String create_pt_lst_table="CREATE TABLE IF NOT EXISTS productlist(id INT(10)," +
                "name VARCHAR(100),price INT(20),description VARCHAR(1000),product_type VARCHAR(50)," +
                "rating float(10),image_link varchar(1000),product_link VARCHAR(1000)," +
                "website_link VARCHAR(1000));\n";
        stmt.execute(create_pt_lst_table);
    }

    public void insertProducts(String[] id, String[] name,String[] price, String[] description,String[] product_type, String[] rating, String[] image_link, String[] product_link, String[] website_link, Statement stmt) throws SQLException {
        String check="SELECT COUNT(*) FROM productlist";
        ResultSet rs=stmt.executeQuery(check);
        rs.next();
        int m=rs.getInt(1);
        System.out.println(m);
        if(m==0){
            for(int i=0;i<name.length;i++)
            {

                description[i]=description[i].replaceAll("[^a-zA-Z0-9]", " ");
                name[i]=name[i].replaceAll("[^a-zA-Z0-9]", " ");
                String insertList = "insert into productlist(id,name,price,description,product_type,rating,image_link,product_link,website_link) values("+id[i]+",'"+name[i]+"',"+price[i]+",'"+description[i]+"','"+product_type[i]+"',"+rating[i]+",'"+image_link[i]+"','"+product_link[i]+"','"+website_link[i]+"' )";
                stmt.executeUpdate(insertList);


            }
        }


    }
    public boolean selectAllProducts() throws SQLException {

        String selectAll ="select name,price,rating from productlist";
        ResultSet rs=stmt.executeQuery(selectAll);
        if(rs.next()) {
            displayData(rs);
            return true;
        }else{
            return false;
        }

    }


    public boolean selectProductbyPT(int choice) throws SQLException {
        String type=gettype(choice);
        String selectAll ="select name,price,rating from productlist where product_type='"+type+"'";
        ResultSet rs=stmt.executeQuery(selectAll);
        if(rs.next()) {
            displayData(rs);
            return true;
        }else{
            return false;
        }
    }

    public boolean displayProductbyRating(float rating) throws SQLException {

        String selectAll ="select name,price,rating from productlist where rating>="+rating+" order by rating ";
        ResultSet rs=stmt.executeQuery(selectAll);
        if(rs.next()){
            displayData(rs);
            return true;
        }else{
        return false;
        }

    }

    public void updateRating(int choice) throws SQLException {
        String type=gettype(choice);
        String selectAll ="select name from productlist where product_type='"+type+"'";
        ResultSet rs1=stmt.executeQuery(selectAll);
        String st="";
        int i=1;
        ArrayList<String> name=new ArrayList<>();
        System.out.println("hit");
        while(rs1.next()){
            st+= "\n" +i+ "." + rs1.getString("name");
            i++;
            name.add(rs1.getString("name"));

        }
        System.out.println(st);
        System.out.println("Enter the product you would like to update the rating for");
        int prod= sc.nextInt();
        String nameToDelete=name.get(prod-1);
        System.out.println("Enter the new rating");
        float rate=sc.nextFloat();
        String updateRate ="update productlist set rating="+rate+" where name='"+nameToDelete+"'";
        stmt.executeUpdate(updateRate);
        System.out.println("Rating successfully updated!");
        String selectrate ="select name,rating from productlist where name='"+nameToDelete+"'";
        ResultSet rs2=stmt.executeQuery(selectrate);

        System.out.println("\nUpdated product with new rating\n");
        System.out.printf("%-70s %-20s","Product name","Rating\n---------------------------------------------------------------------------------------------------------------------------");

        while(rs2.next()){
            System.out.println();
            System.out.format("%-70s %-20s",rs2.getString("name"),rs2.getString("rating"));
        }
    }
    public void deleteProduct(Statement stmt,int choice) throws SQLException {
        String type=gettype(choice);
        String selecttype ="select name from productlist where product_type='"+type+"'";
        ResultSet rs1=stmt.executeQuery(selecttype);
        String st="";
        int i=1;
        ArrayList<String> name=new ArrayList<>();

        while(rs1.next()){
            st+= "\n" +i+ "." + rs1.getString("name");
            i++;
            name.add(rs1.getString("name"));
        }
        System.out.println(st);
        System.out.println("Enter the product you would like to delete.");
        int prod= sc.nextInt();
        String nameToDelete=name.get(prod-1);
        String deleteProduct ="delete from productlist where name='"+nameToDelete+"'";
        stmt.executeUpdate(deleteProduct);
        System.out.println("Product deleted successfully!");
    }

   public static String gettype(int choice){
        String type = null;
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

        return type;
   }

   public static void displayData(ResultSet rs) throws SQLException {
       System.out.printf("%-70s %-20s %8s","Product Name","Price","Rating\n---------------------------------------------------------------------------------------------------------------------------\n");
       while(rs.next()){
           System.out.format("%-70s %-20s %-20s",rs.getString("name") ,rs.getString("price"),rs.getString("rating"));
           System.out.println();
       }
   }

    }


