import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;


public class Server {
    /*public static Statement conToDatabase () throws SQLException {
        Connection con = Database.connect();
        return (Statement) con;
    }*/
    //Connection con = Database.connect();

    public static boolean isAdmin(int godmode) {
        if (godmode == 0) {
            return false;
        } else if (godmode == 1) {
            return true;
        } else return false;
    }

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
                // Extract data if needed
                System.out.println("rset was full for some reason");
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
                // Extract data if needed
                System.out.println("rset was full for some reason");
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

    public static String [] addUser(String uname, String email, String passw) throws SQLException {
        //Statement stmt = conToDatabase();
        String[] uData= new String[3];
        System.out.println("toto je moj email v adduser "+ email);
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
            //System.out.println("User uz existuje");
            uData[0]="nula";
        }
        return (uData);
    }
    public static String[] loginUser (String email, String passw) throws SQLException {
        int a=1;
        String[] uData = new String[5];
        System.out.println("som v loginUser funkcii");

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
            //con.commit();

            while (rset.next()) {
                uData[0] = rset.getString(1);
                System.out.println(uData[0]);
                uData[1] = rset.getString(2);
                System.out.println(uData[1]);
                uData[2] = rset.getString(3);
                System.out.println(uData[2]);
                uData[3] = String.valueOf(rset.getInt(4));
                System.out.println(uData[3]);
                uData[4] = String.valueOf(rset.getInt(5));
                System.out.println(uData[4]);
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
            //poslat cez socket dneska cez github
            System.out.println("Kniha bola registrovana");
            int pocetRiadkov = preparedStatement.executeUpdate();
            if (pocetRiadkov>0){
                System.out.println("Kniha bola registrovana a toto je pocet riadkov " +pocetRiadkov);
            }
            preparedStatement.close();
            con.close();

        } else
            System.out.println("Kniha uz existuje");
    }
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
            //System.out.println("Kniha bola vymazana");
            preparedStatement.close();
            con.close();
        }
        else  System.out.println("Kniha neexistuje");
    }
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
            System.out.println("Total row count: " + rowCount);
        }
        countStatement.close();
        countResultSet.close();
        //System.out.println("tolkoto riadkov mam " + rowCount);
        writer.println(rowCount);
        //poslem resknihy v sockete
        String[] uData= new String[6];
        while (resknihy.next()) {
            uData[0] = String.valueOf(resknihy.getInt(1));
            System.out.println(uData[0]);
            writer.println(uData[0]);
            uData[1] = resknihy.getString(2);
            writer.println(uData[1]);
            System.out.println(uData[1]);
            uData[2] = resknihy.getString(3);
            writer.println(uData[2]);
            System.out.println(uData[2]);
            uData[3] = resknihy.getString(4);
            writer.println(uData[3]);
            System.out.println(uData[3]);
            uData[4] = resknihy.getString(4);
            writer.println(uData[4]);
            System.out.println(uData[4]);
            uData[5] = String.valueOf(resknihy.getInt(6));
            writer.println(uData[5]);
            System.out.println(uData[5]);
        }
        preparedStatement.close();
        resknihy.close();
        con.close();

    }
}