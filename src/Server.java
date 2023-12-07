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

    public static boolean bookExists(String isbn, int ownerID) throws SQLException {
        Connection con= Database.connect();
        Statement stmt = con.createStatement();
        String bname = "select * from books where isbn='" +isbn+ "' AND ownerID='" +ownerID+ "';";
        ResultSet rset = stmt.executeQuery(bname);
        //con.commit();
        if (!rset.next()) {
            stmt.close();
            con.close();
            return false;
        } else{
            stmt.close();
            con.close();
            return true;
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
        if (Server.bookExists(isbn, ownerID) == false) {
            Connection con= Database.connect();
            Statement stmt = con.createStatement();
            String b_create = "Insert into book (title, publisher, isbn, author, ownerID) values ('" +title+ "','" +publisher+ "','" +isbn+ "','" +author+ "','" +ownerID+"');";
            stmt.executeUpdate(b_create);
            //poslat cez socket dneska cez github
            System.out.println("Kniha bola registrovana");
            stmt.close();
            con.close();
        } else
            System.out.println("Kniha uz existuje");
    }
    public static void deleteBook (String title, int ownerID) throws SQLException {
        //Statement stmt = conToDatabase();
        if (Server.bookExists(title, ownerID) == true) {
            Connection con= Database.connect();
            Statement stmt = con.createStatement();
            String b_delete = "delete from books where title= '" +title+ "'AND ownerID= '" +ownerID+ "');";
            stmt.executeUpdate(b_delete);
            //poslat cez socket
            System.out.println("Kniha bola vymazana");
            stmt.close();
            con.close();
        }
        else  System.out.println("Kniha neexistuje");
    }
    public static void zobrazBook () throws SQLException {
        Connection con= Database.connect();
        Statement stmt = con.createStatement();
        String vsicko= "select * from books;";
        ResultSet resknihy= stmt.executeQuery(vsicko);
        //poslem resknihy v sockete
        stmt.close();
        con.close();
    }
}