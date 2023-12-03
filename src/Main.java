import java.io.IOException;
import java.net.ServerSocket;
import java.sql.*;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        Connection con= Database.connect();
        Statement stmt = con.createStatement();
       /* String pokus1= "insert into pokus (int1) values (75);";
        stmt.executeUpdate(pokus1);
        String pokus2= "select int1 from pokus where int1=69;";
        ResultSet rset =stmt.executeQuery(pokus2);
        int cislo= rset.getInt("int1");*/
        ServerSocket ss = new ServerSocket(6969);
        Socket soc = ss.accept();
    }
}