import java.sql.*;
class Connect{

    public static Connection ConnectDB(){

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/products","root","root");
            System.out.println("JDBC Connection successful");


            //String table = "CREATE TABLE product_type ( PT_id INT(20) NOT NULL AUTO_INCREMENT, PT_name VARCHAR(10), PRIMARY KEY ('PT_id'))";
            //String name="Maybelline";
            //String insert_brand="INSERT INTO product_type values(name)";
//            Statement stmt=con.createStatement();
//            ResultSet rs=stmt.executeQuery(table);
//            while(rs.next())
//               System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
//            con.close();
            return con;
        }catch(Exception e){
            System.out.println(e);

        }

        return null;
    }
}