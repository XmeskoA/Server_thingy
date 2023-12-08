import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;


/**
 * This class handles all the communication with server and all the data checks
 */
public class Server {
    /**
     * This method checks if user that was logged in is Admin or not
     * @param godmode variable that stores user mode
     * @return
     */
    public static boolean isAdmin(int godmode) {
        if (godmode == 0) {
            return false;
        } else if (godmode == 1) {
            return true;
        } else return false;
    }

    /**
     * This method checks if user exists in Database
     * @param email This is an email that client sent this method to verify its existence
     * @return
     * @throws SQLException
     */
    public static boolean userExists(String email) throws SQLException {
        ResultSet rset = null;
        PreparedStatement preparedStatement = null;
        Connection con = null;

        try {
            con = Database.connect();
            String selectQuery = "SELECT * FROM users WHERE email = ? OR username = ? ";
            preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, email);
            rset = preparedStatement.executeQuery();
            //con.commit();

            if (!rset.next()) {
                // ResultSet is empty
                System.out.println("rset was empty for some reason");
                return false;
            } else {
                // ResultSet is not empty
                return true;
            }
        } catch (SQLException e) {
            // Handle the exception
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (rset != null) {
                    rset.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                // Handle the exception during closing
                e.printStackTrace();
            }
        }
    }

    /**
     * Same method as the one above, but for books
     * @param nazov variable stores title of the book
     * @param ownerID variable stores ID of an owner
     * @return
     * @throws SQLException
     */
    public static boolean bookExists(String nazov, int ownerID) throws SQLException {
        ResultSet rset = null;
        PreparedStatement preparedStatement = null;
        Connection con = null;

        try {
            con = Database.connect();
            String selectQuery = "SELECT * FROM books WHERE title = ? AND ownerID = ? ";
            preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setString(1, nazov);
            preparedStatement.setInt(2, ownerID);
            rset = preparedStatement.executeQuery();
            //con.commit();

            if (!rset.next()) {
                // ResultSet is empty
                System.out.println("rset was empty for some reason");
                return false;
            } else {
                // ResultSet is not empty
                return true;
            }
        } catch (SQLException e) {
            // Handle the exception
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (rset != null) {
                    rset.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                // Handle the exception during closing
                e.printStackTrace();
            }
        }
    }

    /**
     * This method adds user to the Database if he isn't already there
     * @param uname variable that stores name of the user
     * @param email variable that stores mail of the user
     * @param passw variable that stores password of the user
     * @return
     * @throws SQLException
     */
    public static String [] addUser(String uname, String email, String passw) throws SQLException {
        String[] uData= new String[3];
        if (!Server.userExists(email)) {
            Connection con= null;
            con= Database.connect();
            PreparedStatement preparedStatement= null;
            String uCreate = "INSERT INTO users (username, password, email, godmode) VALUES (?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(uCreate);
            preparedStatement.setString(1, uname);
            preparedStatement.setString(2, passw);
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4,0);
            //con.commit();
            int pocetRiadkov = preparedStatement.executeUpdate();
            if (pocetRiadkov>0){
            System.out.println("User bol registrovany a toto je pocet riadkov " +pocetRiadkov);
            }
            uData[0]=uname;
            System.out.println("toto je moje meno po registracii " +uname);
            uData[1]= passw;
            uData[2]= email;
            preparedStatement.close();
            con.close();
        } else{
            uData[0]="nula";
        }
        return (uData);
    }

    /**
     * This method verifies if user can login
     * @param email variable stores user email from client
     * @param passw variable stores user password from client
     * @return Array filled with users info from the Database
     * @throws SQLException
     */
    public static String[] loginUser (String email, String passw) throws SQLException {
        int a=1;
        String[] uData = new String[5];

        boolean userExists = Server.userExists(email);
        System.out.println("existuje user? "+userExists);
        Connection con = null;
        con= Database.connect();
        ResultSet rset= null;
        PreparedStatement preparedStatement = null;
        if (userExists) {
            String userDataQuery = "select username, password, email, ID,  godmode from users where email = ? OR username= ? AND password = ? ";
            preparedStatement = con.prepareStatement(userDataQuery);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, passw);
            rset = preparedStatement.executeQuery();

            while (rset.next()) {
                uData[0] = rset.getString(1);
                uData[1] = rset.getString(2);
                uData[2] = rset.getString(3);
                uData[3] = String.valueOf(rset.getInt(4));
                uData[4] = String.valueOf(rset.getInt(5));
            } if(a==0)  {
                uData[0] = "nula";
            }

            preparedStatement.close();
            rset.close();
        }
        //preparedStatement.close();
        con.close();
        return(uData);
    }

    /**
     * This method adds a new book to the database
     * @param title
     * @param publisher
     * @param isbn
     * @param author
     * @param ownerID
     * @throws SQLException
     */
    public static void addBook(String title, String publisher, String isbn, String author, int ownerID) throws SQLException {
        if (!Server.bookExists(isbn, ownerID)) {
            Connection con= null;
            con= Database.connect();
            PreparedStatement preparedStatement= null;
            String bCreate = "Insert into books (title, publisher, isbn, author, ownerID) values (?, ?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(bCreate);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, publisher);
            preparedStatement.setString(3, isbn);
            preparedStatement.setString(4, author);
            preparedStatement.setInt(5, ownerID);
            int pocetRiadkov = preparedStatement.executeUpdate();
            if (pocetRiadkov>0){
                System.out.println("Kniha bola registrovana a toto je pocet riadkov " +pocetRiadkov);
            }
            preparedStatement.close();
            con.close();

        } else
            System.out.println("Kniha uz existuje");
    }

    /**
     * This method deletes a book from the database
     * @param title
     * @param ownerID
     * @throws SQLException
     */
    public static void deleteBook (String title, int ownerID) throws SQLException {
        //Statement stmt = conToDatabase();
        if (Server.bookExists(title, ownerID)) {
            Connection con= null;
            con= Database.connect();
            PreparedStatement preparedStatement= null;
            String bDelete = "delete from books where title= ? and ownerID = ? ";
            preparedStatement = con.prepareStatement(bDelete);
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, ownerID);
            int pocetRiadkov = preparedStatement.executeUpdate();
            if (pocetRiadkov>0){
                System.out.println("Kniha bola vymazana a toto je pocet riadkov " +pocetRiadkov);
            }
            preparedStatement.close();
            con.close();
        }
        else  System.out.println("Kniha neexistuje");
    }

    /**
     * This method takes all books from our book table and sends it to user
     * @param socket
     * @param writer
     * @throws SQLException
     */
    public static void zobrazBook (Socket socket, PrintWriter writer) throws SQLException {
        Connection con= null;
        con= Database.connect();
        PreparedStatement preparedStatement= null;
        ResultSet resknihy= null;
        String vsicko= "select * from books ";
        preparedStatement = con.prepareStatement(vsicko);
        resknihy= preparedStatement.executeQuery();
        Statement countStatement = con.createStatement();
        ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(*) FROM books");
        int rowCount=0;
        if (countResultSet.next()) {
            rowCount = countResultSet.getInt(1);
        }
        countStatement.close();
        countResultSet.close();
        writer.println(rowCount);
        String[] uData= new String[6];
        while (resknihy.next()) {
            uData[0] = String.valueOf(resknihy.getInt(1));
            writer.println(uData[0]);
            uData[1] = resknihy.getString(2);
            writer.println(uData[1]);
            uData[2] = resknihy.getString(3);
            writer.println(uData[2]);
            uData[3] = resknihy.getString(4);
            writer.println(uData[3]);
            uData[4] = resknihy.getString(5);
            writer.println(uData[4]);
            uData[5] = String.valueOf(resknihy.getInt(6));
            writer.println(uData[5]);
        }
        preparedStatement.close();
        resknihy.close();
        con.close();

    }

    /**
     * This method gets us username of an owner of book we previously selected
     * @param username
     * @return
     * @throws SQLException
     */
    public static int getIDbyUsername(String username) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            con = Database.connect();
            String query = "select ID from users where username = ? or email = ?";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("ID");
            } else {
                return -1;
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (con != null) con.close();
        }
    }

    /**
     * This method gets me a username using owners id of a book received from client
     * @param ownerID
     * @return
     * @throws SQLException
     */
    public static String getUsernameByID (int ownerID) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            con = Database.connect();
            String query = "select username from users where ID= ? ";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, ownerID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("username");
            } else {
                return "blyat";
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (con != null) con.close();
        }

    }

    /**
     * This method deletes user from database
     * @param email
     */
    public static void deleteUser (String email) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = Database.connect();
            String deleteUserQuery = "delete from users where email = ? or username = ?";
            preparedStatement = con.prepareStatement(deleteUserQuery);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, email);
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Pouzivatel " + email + " bol odstraneny");
            } else {
                System.out.println("Pouzivatel " + email + " nebol najdeny");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}