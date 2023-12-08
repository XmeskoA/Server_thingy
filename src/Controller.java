import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Controller class handles communication between client and the server in a library management system
 */
public class Controller {
    /**
     * Listens for client connections and processes client requests continuously
     * Receives client messages and calls corresponding methods on the server side
     * @throws IOException
     * @throws SQLException
     */
    public static void strom() throws IOException, SQLException {
        ServerSocket serverSocket = new ServerSocket(6969);
        Socket clientSocket = serverSocket.accept();
        System.out.println("Connected!");
        while (true) {
            System.out.println("cakam na pokyn...");
            //Socket clientSocket = serverSocket.accept();
            //System.out.println("cakam na pokyn...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer= new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
            String sprava = reader.readLine();
            if (sprava.equals("addU")) {
                Controller.registracia(clientSocket, reader, writer);
            }
            if (sprava.equals("addUA")) {
                Controller.registraciaCezAdmin(clientSocket, reader, writer);
            }
            if (sprava.equals("userL")){
                System.out.println("Som v userL");
                Controller.loginUserController(clientSocket, reader, writer);
            }
            if (sprava.equals("addB")){
                Controller.addBookController(clientSocket, reader, writer);
            }
            if (sprava.equals("rmvB")){
                Controller.deleteBookController(clientSocket, reader, writer);
            }
            if (sprava.equals("showB")){
                Controller.showBooksController(clientSocket, reader, writer);
            }
            if (sprava.equals("addBtoU")) {
                Controller.addBookToUserController(clientSocket, reader, writer);
            }
            if (sprava.equals("rmvBfromU")) {
                Controller.removeBookFromUserController(clientSocket, reader, writer);
            }
            if (sprava.equals("rmvUA")) {
                Controller.removeUserAsAdmin(clientSocket, reader, writer);
            }
            if (sprava.equals("gimme")){
                Controller.gimmeUsernameForBook(clientSocket,reader, writer);
            }
            if (sprava.equals("exit")){
                clientSocket.close();
            }
            //clientSocket.close();
        }
        //clientSocket.close();
    }

    /**
     * Handles user registration by receiving user data from the client
     * Sends the server response back to the client
     * @param clientSocket Socket communication with client
     * @param reader Receiving data from the client
     * @param writer Sending data to the client
     * @throws IOException
     * @throws SQLException
     */
    public static void registracia(Socket clientSocket, BufferedReader reader, PrintWriter writer) throws IOException, SQLException {
        int i = 0;
        String[] udaje = new String[3];
        udaje[0]= reader.readLine();
        udaje[1]= reader.readLine();
        udaje[2]= reader.readLine();
        String[] uData = Server.addUser(udaje[0], udaje[1], udaje[2]);
        System.out.println(uData[0]);
        if (uData[0].equals("nula")) {
            for (i = 0; i<3; i++) {
                writer.println(uData[i]);
                System.out.println("uData sa rovnaju 0");
            }
        } if(!uData[0].equals("nula")) {
            System.out.println("som signupnuty!");
            for (i = 0; i<3; i++) {
                writer.println(uData[i]);
            }

        }
    }

    /**
     * Handles user registration as Admin by receiving user data from the client
     * Sends the server response back to the client
     * @param clientSocket Socket communication with client
     * @param reader Receiving data from the client
     * @param writer Sending data to the client
     * @throws IOException
     * @throws SQLException
     */
    public static void registraciaCezAdmin(Socket clientSocket, BufferedReader reader, PrintWriter writer) throws IOException, SQLException {
        int i = 0;
        String[] udaje = new String[3];
        udaje[0] = reader.readLine();
        udaje[1] = reader.readLine();
        udaje[2] = reader.readLine();

        String[] uData = Server.addUser(udaje[0], udaje[1], udaje[2]);

        System.out.println(uData[0]);
        if (uData[0].equals("nula")) {
            for (i = 0; i < 3; i++) {
                writer.println(uData[i]);
                System.out.println("uData sa rovnaju 0");
            }
        } else {
            System.out.println("Registracia cez admina OK");
            /*for (i = 0; i < 3; i++) {
                writer.println(uData[i]);
            }*/
        }
    }

    /**
     * Handles user login by receiving login credentials from the client
     * Sends the server response back to the client
     * @param clientSocket Socket communication with client
     * @param reader Receiving data from the client
     * @param writer Sending data to the client
     * @throws IOException
     * @throws SQLException
     */
    public static void loginUserController (Socket clientSocket, BufferedReader reader, PrintWriter writer) throws IOException, SQLException {
        int i=0;
        String email= reader.readLine();
        String passw= reader.readLine();
        String [] uData= Server.loginUser(email, passw);
        //PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
        for (i=0;i<5;i++){
                writer.println(uData[i]);
                System.out.println("odosielam " + uData[i]);
            }
        }

    /**
     * Handles adding a book by receiving book data from the client
     * Performs book addition on the server side
     * @param clientsocket Socket communication with client
     * @param reader Receiving data from the client
     * @param writer Sending data to the client
     * @throws IOException
     * @throws SQLException
     */
        public static void addBookController (Socket clientsocket, BufferedReader reader, PrintWriter writer) throws IOException, SQLException {
            String[] udaje= new String[5];
            for (int i=0;i<5;i++){
                udaje[i]= reader.readLine();
            }
            int ownerID=  Integer.parseInt(udaje[4]);
            Server.addBook(udaje[0], udaje[1], udaje[2], udaje[3], ownerID);
        }

    /**
     * Handles deleting a book by receiving book data from the client
     * Performs book deletion on the server side
     * @param clientsocket Socket communication with client
     * @param reader Receiving data from the client
     * @param writer Sending data to the client
     * @throws IOException
     * @throws SQLException
     */
        public static void deleteBookController (Socket clientsocket, BufferedReader reader, PrintWriter writer) throws IOException, SQLException {
            String nazov = reader.readLine();
            String temp= reader.readLine();
            int ownerID = Integer.parseInt(temp);
            Server.deleteBook(nazov, ownerID);
        }

    /**
     * Handles showing a book list by invoking a server method
     * Sends output information to the client
     * @param clientsocket Socket communication with client
     * @param reader Receiving data from the client
     * @param writer Sending data to the client
     * @throws SQLException
     */
        public static void showBooksController (Socket clientsocket, BufferedReader reader, PrintWriter writer) throws SQLException {
            Server.zobrazBook(clientsocket, writer);
        }

    /**
     * Handles adding a book to a user's library by receiving user and book data from the client
     * Performs book addition on the server side
     * @param clientSocket Socket communication with client
     * @param reader Receiving data from the client
     * @param writer Sending data to the client
     * @throws IOException
     * @throws SQLException
     */
    public static void addBookToUserController(Socket clientSocket, BufferedReader reader, PrintWriter writer) throws IOException, SQLException {
        String username = reader.readLine();
        String title = reader.readLine();
        String publisher = reader.readLine();
        String ISBN = reader.readLine();
        String author = reader.readLine();
        int userID = Server.getIDbyUsername(username);
        Server.addBook(title, publisher, ISBN, author, userID);
        System.out.println("Server - Book was added OK");
    }

    /**
     * Handles removing a book from a user's library by receiving user and book data from the client
     * Performs book deletion on the server side
     * @param clientSocket Socket communication with client
     * @param reader Receiving data from the client
     * @param writer Sending data to the client
     * @throws IOException
     * @throws SQLException
     */
    public static void removeBookFromUserController(Socket clientSocket, BufferedReader reader, PrintWriter writer) throws IOException, SQLException {
        String username = reader.readLine();
        String title = reader.readLine();
        int userID = Server.getIDbyUsername(username);
        Server.deleteBook(title, userID);
    }

    /**
     * Handles removing a user by receiving user data from the client
     * Performs user removal on the server side
     * @param clientSocket Socket communication with client
     * @param reader Receiving data from the client
     * @param writer Sending data to the client
     * @throws IOException
     */
    public static void removeUserAsAdmin(Socket clientSocket, BufferedReader reader, PrintWriter writer) throws IOException {
        String username = reader.readLine();
        Server.deleteUser(username);
    }

    /**
     * This method is used to get username from ownerID in the book object
     * @param clientsocket
     * @param reader
     * @param writer
     * @throws IOException
     * @throws SQLException
     */
    public static void gimmeUsernameForBook (Socket clientsocket, BufferedReader reader, PrintWriter writer) throws IOException, SQLException {
        int ID = Integer.parseInt(reader.readLine());
        String uname= Server.getUsernameByID(ID);
        if (uname.equals("blyat")){
            System.out.println("blyat");
        }else {
            System.out.println("odosielam "+ uname);
            writer.println(uname);
        }
    }

}

