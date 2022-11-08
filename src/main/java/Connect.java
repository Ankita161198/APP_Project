import java.sql.*;
class Connect{
    public static void main(String args[]){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306","root","root");
            String table = "CREATE TABLE Person ( P_id INT(20) NOT NULL AUTO_INCREMENT, name VARCHAR(10), PRIMARY KEY ('P_id'))";
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(table);
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
            con.close();
        }catch(Exception e){ System.out.println(e);}
    }
}