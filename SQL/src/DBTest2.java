import java.sql.*;
import java.io.*;
import java.util.Scanner;

public class DBTest2
{
    public static void main (String args []) throws SQLException, IOException{
        Scanner scanner=new Scanner(System.in);
        Connection conn=null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //JDBC 드라이버 연결
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String dbacct, passwrd, dbname, ssn, lname, Fname;
        String salary,select,where,from;

        //System.out.println("Enter database account:");
        dbacct="root";
        //System.out.println("Enter password:");
        passwrd="dlfgns12";
        //System.out.println("Enter database name:");
        dbname="mydb";

        String url="jdbc:mysql://localhost:3306/"+dbname+"?serverTimezone=UTC";
        conn=DriverManager.getConnection(url, dbacct, passwrd);
        
        select = "* ";
        from = "employee";
        where = "123456789";
        
        String stmt1="select "+ select + "from "+ from  +" where "+"ssn ="+"?";
        PreparedStatement p=conn.prepareStatement(stmt1);
        System.out.println(p);
        
        //System.out.println("Enter a Social Security Number: ");
        //Statement의 첫번째 ?에 넣는다.
        p.clearParameters();
        System.out.println(p);
        p.setNString(1, where);
        System.out.println(p);
        ResultSet r=p.executeQuery();
        System.out.println(r);

        while(r.next()){
            lname=r.getString(1) + " " +r.getString(3)+ " " +r.getString(4);
            System.out.println(lname);
        }

        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
        }
    }
}
