import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Controller {
    public static void strom() throws IOException, SQLException {
        ServerSocket serverSocket = new ServerSocket(6969);
        Socket clientSocket = serverSocket.accept();
        while (true) {
            System.out.println("cakam na pokyn...");
            //Socket clientSocket = serverSocket.accept();
            //System.out.println("cakam na pokyn...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String sprava = reader.readLine();
            if (sprava.equals("addU")) {

                Controller.registracia(clientSocket, reader);

            }
            if (sprava.equals("userL")){
                System.out.println("Som v userL");
                Controller.loginUserController(clientSocket, reader);
            }
            if (sprava.equals("exit")){
                clientSocket.close();
            }
            //clientSocket.close();
        }
        //clientSocket.close();
    }

    public static void registracia(Socket clientSocket, BufferedReader reader) throws IOException, SQLException {
        int i = 0;
        String[] udaje = new String[3];
        udaje[0]= reader.readLine();
        udaje[1]= reader.readLine();
        udaje[2]= reader.readLine();
        System.out.println("toto su moje udaje" + udaje[2]+ udaje[1]);
       /*while ((udaje[i]=reader.readLine()) != null) {
            System.out.println(udaje[i]);
            i++;
        }*/
        //String[] uData = new String[5];
        String[] uData = Server.addUser(udaje[0], udaje[1], udaje[2]);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
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
    public static void loginUserController (Socket clientSocket, BufferedReader reader) throws IOException, SQLException {
        int i=0;
        String email= reader.readLine();
        String passw= reader.readLine();
        String [] uData= Server.loginUser(email, passw);
        System.out.println("prisli sme k odosielani login dat");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
        for (i=0;i<5;i++){
                writer.println(uData[i]);
                System.out.println("odosielam" + uData[i]);
            }
        }
    }

