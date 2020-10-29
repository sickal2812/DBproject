import java.sql.*;

public class db_test {
    public static void main(String[] args) {
        Connection con = null;
        // 연결
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //JDBC 드라이버 연결

            // 접속 url과 사용자, 비밀번호
            String url="jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC";
            String user="root";
            String pwd="dlfgns12";

            con = DriverManager.getConnection(url, user, pwd);
            System.out.println("정상적으로 연결되었습니다.");

        } catch (SQLException e) {
            System.err.println("연결할 수 없습니다.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("드라이버를 로드할 수 없습니다.");
            e.printStackTrace();
        }
        
        // 해제
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
