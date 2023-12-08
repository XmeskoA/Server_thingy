import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Basic database operations
 */
public class Database {
    /**
     * Connects to the database
     * @return
     * @throws SQLException
     */
    public static Connection connect() throws SQLException {
            Connection con= null;
            con = DriverManager.getConnection("jdbc:sqlite:F:/FEI/Treti semester/TSIKT/Zavercna_praca/Data.db");
            con.setAutoCommit(true);
        
        return con;
    }

}