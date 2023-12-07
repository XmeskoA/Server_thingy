import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connect() throws SQLException {
            Connection con= null;
            //Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:F:/FEI/Treti semester/TSIKT/Zavercna_praca/Data.db");
            con.setAutoCommit(true);
            //System.out.println("Connected!");
        
        return con;
    }

}