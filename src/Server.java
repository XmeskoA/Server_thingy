import java.sql.*;


public class Server {
    Connection con = Database.connect();

    public boolean isAdmin(int godmode) {
        if (godmode == 0) {
            return false;
        } else if (godmode == 1) {
            return true;
        } else return false;
    }

    public boolean userExists(String email) throws SQLException {
        Statement stmt = con.createStatement();
        String uname = "select * from users where email=" + email + ";";
        ResultSet rset = stmt.executeQuery(uname);
        if (!rset.next()) {
            return false;
        } else
            return true;
    }

    public boolean bookExists(String isbn, int ownerID) throws SQLException {
        Statement stmt = con.createStatement();
        String bname = "select * from books where isbn=" + isbn + " AND ownerID=" + ownerID + ";";
        ResultSet rset = stmt.executeQuery(bname);
        if (!rset.next()) {
            return false;
        } else
            return true;
    }

    public void addUser(String uname, String passw, String email) throws SQLException {
        Statement stmt = con.createStatement();
        if (userExists(email) == false) {
            String u_create = "Insert into users (username, password, email, godmode) values (" + uname + "," + passw + "," + email + ",0);";
            stmt.executeUpdate(u_create);
            //socket pridat
            System.out.println("User bol registrovany");
        } else
            System.out.println("User uz existuje");
    }
    public boolean loginUser (String email, String passw) throws SQLException {
        Statement stmt = con.createStatement();
        if (userExists(email)== true) {
            String userData= "select * from users where email=" + email +");";
            ResultSet uData = stmt.executeQuery(userData);
            //returnem uData v sockete
            return true;
        }
        return false;
    }


    public void addBook(String title, String publisher, String isbn, String author, int ownerID) throws SQLException {
        Statement stmt = con.createStatement();
        if (bookExists(isbn, ownerID) == false) {
            String b_create = "Insert into book (title, publisher, isbn, author, ownerID) values ("+title+ "," +publisher + "," + isbn + ","+ author+ "," +ownerID+");";
            stmt.executeUpdate(b_create);
            //poslat cez socket dneska cez github
            System.out.println("Kniha bola registrovana");
        } else
            System.out.println("Kniha uz existuje");
    }
    public void deleteBook (String title, int ownerID) throws SQLException {
        Statement stmt = con.createStatement();
        if (bookExists(title, ownerID) == true) {
            String b_delete = "delete from books where title= " + title + "AND ownerID= " + ownerID + ");";
            stmt.executeUpdate(b_delete);
            //poslat cez socket
            System.out.println("Kniha bola vymazana");
        }
        else  System.out.println("Kniha neexistuje");
    }
    public void zobrazBook () throws SQLException {
        Statement stmt = con.createStatement();
        String vsicko= "select * from books;";
        ResultSet resknihy= stmt.executeQuery(vsicko);
        //poslem resknihy v sockete
    }
}