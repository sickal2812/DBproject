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
        String dbacct, passwrd, dbname, ssn, lname;
        Double salary;

        System.out.println("Enter database account:");
        dbacct=scanner.nextLine();
        System.out.println("Enter password:");
        passwrd=scanner.nextLine();
        System.out.println("Enter database name:");
        dbname=scanner.nextLine();

        String url="jdbc:mysql://localhost:3306/"+dbname+"?serverTimezone=UTC";
        conn=DriverManager.getConnection(url, dbacct, passwrd);

        String stmt1="select Lname, Salary from EMPLOYEE where Ssn=?";
        PreparedStatement p=conn.prepareStatement(stmt1);

        System.out.println("Enter a Social Security Number: ");
        ssn=scanner.nextLine();

        //Statement의 첫번째 ?에 넣는다.
        p.clearParameters();
        p.setString(1, ssn);
        ResultSet r=p.executeQuery();

        while(r.next()){
            lname=r.getString(1);
            salary=r.getDouble(2);
            System.out.println(lname+" "+salary);
        }

        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
        }
    }
}
