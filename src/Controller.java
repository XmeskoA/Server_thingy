import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Controller {
    public static void strom() throws IOException, SQLException {
        ServerSocket serverSocket = new ServerSocket(6969);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String sprava = reader.readLine();
            if (sprava.equals("addU")) {
                Controller.registracia(clientSocket, reader);

            }
        }
    }

    public static void registracia(Socket clientSocket, BufferedReader reader) throws IOException, SQLException {
        int i = 0;
        String[] udaje = new String[3];
        udaje[0]= reader.readLine();
        udaje[1]= reader.readLine();
        udaje[2]= reader.readLine();
       /* while ((udaje[i]=reader.readLine()) != null) {
            System.out.println(udaje[i]);
            i++;
        }*/
        //String[] uData = new String[5];
        String uData[] = Server.addUser(udaje[0], udaje[1], udaje[2]);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
        System.out.println(uData[0]);
        if (uData[0].equals("nula")) {
            for (i = 0; i == 4; i++) {
                writer.println(uData[i]);
                System.out.println("som tu");
            }
        } else {
            for (i = 0; i == 4; i++) {
                writer.println(uData[i]);
                System.out.println("nie som tu");
            }

        }
    }
}
