import java.sql.*;
class Connect{

    public static Connection ConnectDB(){

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/products","root","root");
            System.out.println("JDBC Connection successful");
            return con;
        }catch(Exception e){
            System.out.println(e);

        }

        return null;
    }
}