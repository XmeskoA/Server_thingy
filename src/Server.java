import java.sql.*;


public class Server {
    public static Statement conToDatabase () throws SQLException {
        Connection con = Database.connect();
        return (con.createStatement());
    }
    //Connection con = Database.connect();

    public static boolean isAdmin(int godmode) {
        if (godmode == 0) {
            return false;
        } else if (godmode == 1) {
            return true;
        } else return false;
    }

    public static boolean userExists(String email) throws SQLException {
        ResultSet rset;
        try (Statement stmt = conToDatabase()) {
            String uname = "select * from users where email='" +email+ "';";
            rset = stmt.executeQuery(uname);
        }
        if (!rset.next()) {
            return false;
        } else
            return true;
    }

    public static boolean bookExists(String isbn, int ownerID) throws SQLException {
        Statement stmt = conToDatabase();
        String bname = "select * from books where isbn='" +isbn+ "' AND ownerID='" +ownerID+ "';";
        ResultSet rset = stmt.executeQuery(bname);
        if (!rset.next()) {
            return false;
        } else
            return true;
    }

    public static String [] addUser(String uname, String passw, String email) throws SQLException {
        Statement stmt = conToDatabase();
        String[] uData= new String[5];
        if (Server.userExists(email) == false) {
            String u_create = "Insert into users (username, password, email, godmode) values ('" +uname+ "','" +passw+ "','" +email+ "',0);";
            stmt.executeUpdate(u_create);
            System.out.println("User bol registrovany");
        } else{
            //System.out.println("User uz existuje");
            uData[0]="nula";
        }
        return (uData);
    }
    public static String[] loginUser (String email, String passw) throws SQLException {
        Statement stmt = conToDatabase();
        String [] uData= new String[5];
        if (Server.userExists(email)== true) {
            String userData= "select * from users where email='" +email+"');";
            ResultSet resultSet = stmt.executeQuery(userData);
             uData[0]= resultSet.getString("username");
             uData[1]= resultSet.getString("password");
             uData[2]= resultSet.getString("email");
             uData[3]= String.valueOf(resultSet.getInt("id"));
             uData[4]= String.valueOf(resultSet.getInt("godmode"));
             return (uData);
        }else{
            uData[0]="null";
            return (uData);
        }
    }


    public static void addBook(String title, String publisher, String isbn, String author, int ownerID) throws SQLException {
        Statement stmt = conToDatabase();
        if (Server.bookExists(isbn, ownerID) == false) {
            String b_create = "Insert into book (title, publisher, isbn, author, ownerID) values ('" +title+ "','" +publisher+ "','" +isbn+ "','" +author+ "','" +ownerID+"');";
            stmt.executeUpdate(b_create);
            //poslat cez socket dneska cez github
            System.out.println("Kniha bola registrovana");
        } else
            System.out.println("Kniha uz existuje");
    }
    public static void deleteBook (String title, int ownerID) throws SQLException {
        Statement stmt = conToDatabase();
        if (Server.bookExists(title, ownerID) == true) {
            String b_delete = "delete from books where title= '" +title+ "'AND ownerID= '" +ownerID+ "');";
            stmt.executeUpdate(b_delete);
            //poslat cez socket
            System.out.println("Kniha bola vymazana");
        }
        else  System.out.println("Kniha neexistuje");
    }
    public static void zobrazBook () throws SQLException {
        Statement stmt = conToDatabase();
        String vsicko= "select * from books;";
        ResultSet resknihy= stmt.executeQuery(vsicko);
        //poslem resknihy v sockete
    }
}