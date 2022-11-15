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
        String create_pt_lst_table="CREATE TABLE IF NOT EXISTS productlist(id INT(10),name VARCHAR(100),price INT(20),description VARCHAR(1000),product_type VARCHAR(50),rating float(10),primary key (id));";
        stmt.execute(create_pt_lst_table);
        String create_product_link="CREATE TABLE IF NOT EXISTS  productlink(pid INT(10),image_link varchar(1000),product_link VARCHAR(1000),website_link VARCHAR(1000),foreign key (pid) references productlist(id));\n";
        stmt.execute(create_product_link);
    }

    public void insertProducts(String[] id, String[] name,String[] price, String[] description,String[] product_type, String[] rating, String[] image_link, String[] product_link, String[] website_link, Statement stmt) throws SQLException {
        String check="SELECT COUNT(*) FROM productlist";
        ResultSet rs=stmt.executeQuery(check);
        rs.next();
        int m=rs.getInt(1);
        if(m==0){
            for(int i=0;i<name.length;i++)
            {

                description[i]=description[i].replaceAll("[^a-zA-Z0-9]", " ");
                name[i]=name[i].replaceAll("[^a-zA-Z0-9]", " ");
                String insertList = "insert into productlist(id,name,price,description,product_type,rating) values("+id[i]+",'"+name[i]+"',"+price[i]+",'"+description[i]+"','"+product_type[i]+"',"+rating[i]+" )";
                stmt.executeUpdate(insertList);
                String insertLink = "insert into productlink(pid,image_link,product_link,website_link) values("+id[i]+",'"+image_link[i]+"','"+product_link[i]+"','"+website_link[i]+"' )";
                stmt.executeUpdate(insertLink);


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
            System.out.println("Product type does not exist");
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
            System.out.println("Product type does not exist");

            return false;
        }

    }
    public boolean displayLinks(int id) throws SQLException {
        String selectlink="select p.image_link,p.website_link,p.product_link from productlink p join productlist p2 on p2.id = p.pid where p.pid="+id+";";
        ResultSet rs=stmt.executeQuery(selectlink);

        if(rs.next()){
            System.out.format("Image link : "+rs.getString("image_link")+"\nProduct Link : " +rs.getString("product_link")+"\nWebsite link : "+rs.getString("website_link"));
            System.out.println();
            return true;
        }else{
            System.out.println("Product does not exist");
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
        int prod = sc.nextInt();
        String nameToDelete =name.get(prod-1);
        System.out.println("Enter the new rating");
        float rate=sc.nextFloat();
        String updateRate ="update productlist set rating="+rate+" where name='"+nameToDelete+"'";
        stmt.executeUpdate(updateRate);
        System.out.println("Rating successfully updated!");
        String selectrate ="select name,rating from productlist where name='"+nameToDelete+"'";
        ResultSet rs2=stmt.executeQuery(selectrate);
        System.out.println("\nUpdated product with new rating\n");
        System.out.printf("%-70s %-20s","Product name","Rating\n---------------------------------------------------------------------------------------------------------------------------");

        while(rs2.next())
        {
            System.out.println();
            System.out.format("%-70s %-20s",rs2.getString("name"),rs2.getString("rating"));
        }
    }
    public void deleteProduct(Statement stmt) throws SQLException {

        System.out.println("Enter the product id you would like to delete.");
        int prod = sc.nextInt();
        String selecttype = "select name,id from productlist where product_type='"+prod+"'";
        ResultSet rs1=stmt.executeQuery(selecttype);
        int id = 0;
        while(rs1.next()){

            id=rs1.getInt("id");

        }
       // System.out.println(id);



        String deleteProduct ="delete p, p1 from productlink p1 inner join productlist p on p.id = p1.pid where p.id="+id+";";
        stmt.executeUpdate(deleteProduct);
        System.out.println("Product deleted successfully!");
    }

   private static String gettype(int choice){
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

   private static void displayData(ResultSet rs) throws SQLException {
       System.out.printf("%-70s %-20s %8s","Product Name","Price","Rating\n---------------------------------------------------------------------------------------------------------------------------\n");
       while(rs.next()){
           System.out.format("%-70s %-20s %-20s",rs.getString("name") ,rs.getString("price"),rs.getString("rating"));
           System.out.println();
       }
   }

}


